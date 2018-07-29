package com.anecdote.chat.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
@JsonDeserialize
@Value
public class ChatMessage
{
  private final String messageId;
  private final String senderId;
  private final String content;
}
