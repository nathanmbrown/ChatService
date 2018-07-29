package com.anecdote.chat.service;

import com.anecdote.chat.controller.ChatController;
import com.anecdote.chat.model.ChatChannel;
import com.anecdote.chat.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
@Service
public class ChatService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

  private static Map<String, ChatMessageQueue> _messageQueues = new ConcurrentHashMap<>();

  private final ChatChannelsServiceProxy _channelsServiceProxy;

  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  public ChatService(ChatChannelsServiceProxy channelsServiceProxy)
  {
    this._channelsServiceProxy = channelsServiceProxy;
  }

  public boolean sendMessage(String channelID, ChatMessage message)
  {
    ChatChannel chatChannel = _channelsServiceProxy.find(channelID);
    if (chatChannel != null) {
      ChatMessageQueue queue = getChannelQueue(channelID);
      queue.queue(message);
      return true;
    }
    return false;
  }

  public ChatMessageQueue getChannelQueue(String channelID)
  {
    ChatChannel chatChannel = _channelsServiceProxy.find(channelID);
    if (chatChannel != null) {
      return _messageQueues.computeIfAbsent(channelID, id -> new ChatMessageQueueImpl());
    }
    return null;
  }

  private static class ChatMessageQueueImpl implements ChatMessageQueue
  {
    private volatile Collection<SseEmitter> _emitters = new HashSet();

    //    private Flux<ChatMessage> _flux = Flux.just(new ChatMessage("TestID", "TestSender", "TestChannelID", "Test Message."));
//
    @Override
    public void queue(ChatMessage message)
    {
      _emitters.forEach(emitter -> {
        try {
          emitter.send(message);
        } catch (IOException e) {
          LOGGER.warn("Could not deliver message : {}", e.getMessage(), e);
        }
      });
    }

    @Override
    public SseEmitter getEmitter()
    {
      SseEmitter emitter = new SseEmitter();
      addEmitter(emitter);
      return emitter;
    }

    private void addEmitter(SseEmitter emitter)
    {
      HashSet<SseEmitter> newEmitters = new HashSet<>(_emitters);
      newEmitters.add(emitter);
      emitter.onCompletion(() -> removeEmitter(emitter));
      emitter.onError(throwable -> {
        removeEmitter(emitter);
        LOGGER.warn("Emitter produced error : {}", throwable.getMessage(), throwable);
      });
      emitter.onTimeout(() -> LOGGER.warn("Emitter timed out"));
      _emitters = newEmitters;
    }

    private void removeEmitter(SseEmitter emitter)
    {
      HashSet<SseEmitter> newEmitters = new HashSet<>(_emitters);
      newEmitters.remove(emitter);
      _emitters = newEmitters;
    }
  }
}
