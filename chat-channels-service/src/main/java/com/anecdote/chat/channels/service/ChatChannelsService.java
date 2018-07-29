package com.anecdote.chat.channels.service;

import com.anecdote.chat.channels.model.ChatChannel;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
@Service
public class ChatChannelsService
{
  private static Map<String, ChatChannel> _streamRepository = new HashMap<>();

  static {
    _streamRepository.put("global", new ChatChannel("global", "Global", null));
  }

  public ChatChannel findByID(String id)
  {
    return _streamRepository.get(id);
  }

  public Collection<ChatChannel> findAll()
  {
    return _streamRepository.values();
  }

  public ChatChannel createStream(String ownerID, String name)
  {
    String id = UUID.randomUUID().toString();
    ChatChannel chatChannel = new ChatChannel(id, name, ownerID);
    _streamRepository.put(id, chatChannel);
    return chatChannel;
  }
}
