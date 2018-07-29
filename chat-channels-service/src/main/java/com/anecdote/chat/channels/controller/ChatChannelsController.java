package com.anecdote.chat.channels.controller;

import com.anecdote.chat.channels.model.ChatChannel;
import com.anecdote.chat.channels.service.ChatChannelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Nathan
 * Created : 02/03/2018
 */

@RefreshScope
@RestController
public class ChatChannelsController
{
  private final ChatChannelsService _chatChannelsService;

  @Autowired
  public ChatChannelsController(ChatChannelsService _chatChannelsService)
  {
    this._chatChannelsService = _chatChannelsService;
  }

  @RequestMapping("/channels/find/{id}")
  public ChatChannel find(@PathVariable String id)
  {
    return _chatChannelsService.findByID(id);
  }

  @RequestMapping("/channels/findAll")
  public Collection<ChatChannel> findAll()
  {
    return _chatChannelsService.findAll();
  }

  @RequestMapping("/channels/create")
  public ChatChannel createStream(@RequestParam("ownerID") String ownerID, @RequestParam("name") String name)
  {
    return _chatChannelsService.createStream(ownerID, name);
  }
}
