package com.anecdote.chat.service;

import com.anecdote.chat.model.ChatMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @author Nathan
 * Created : 02/03/2018
 */
public interface ChatMessageQueue
{
  void queue(ChatMessage message);

  SseEmitter getEmitter();
}
