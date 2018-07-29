package com.anecdote.chat.channels.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
@Data
@AllArgsConstructor
public class ChatChannel
{
  private final String id;
  private String name;
  private final String ownerId;
}
