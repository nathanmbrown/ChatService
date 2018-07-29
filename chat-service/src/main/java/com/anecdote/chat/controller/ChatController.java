package com.anecdote.chat.controller;

import com.anecdote.chat.model.ChatMessage;
import com.anecdote.chat.service.ChatMessageQueue;
import com.anecdote.chat.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
@RefreshScope
@RestController
public class ChatController
{
  private final ChatService _chatService;

  @Autowired
  public ChatController(ChatService _chatService)
  {
    this._chatService = _chatService;
  }

  @GetMapping("/chat/channel/{id}")
  public ResponseEntity<SseEmitter> openChannelSession(@PathVariable("id") String channelID)
  {
    ChatMessageQueue channelQueue = _chatService.getChannelQueue(channelID);
    if (channelQueue != null)
      return ResponseEntity.ok(channelQueue.getEmitter());
    return new ResponseEntity<>((SseEmitter)null, HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/chat/channel/{id}")
  public ResponseEntity postToChannel(@PathVariable("id") String channelID,
                                      @RequestBody ChatMessage message)
  {
    if (_chatService.sendMessage(channelID, message)) {
      return ResponseEntity.ok("");
    }
    return new ResponseEntity<>("No Channel found with id \"" + channelID + "\"", HttpStatus.BAD_REQUEST);
  }
}
