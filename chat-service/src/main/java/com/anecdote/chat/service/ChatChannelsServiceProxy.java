package com.anecdote.chat.service;

import com.anecdote.chat.model.ChatChannel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Nathan
 * Created : 03/03/2018
 */
@FeignClient(name = "ChatChannels")
public interface ChatChannelsServiceProxy
{
  @RequestMapping("/channels/find/{id}")
  public ChatChannel find(@PathVariable(value = "id") String id);

  @RequestMapping("/channels/findAll")
  public Collection<ChatChannel> findAll();

  @RequestMapping("/channels/create")
  public ChatChannel createStream(@RequestParam("ownerID") String ownerID, @RequestParam("name") String name);

}
