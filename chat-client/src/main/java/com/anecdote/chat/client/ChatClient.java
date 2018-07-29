package com.anecdote.chat.client;

import com.eclipsesource.json.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nathan
 * Created : 03/03/2018
 */
public class ChatClient
{
  private static final Map<String, Object> UNSEEN_MESSAGES = new ConcurrentHashMap<>();
  protected static final String URI_TEMPLATE = "http://%s:%s/chat/channel/%s";
  private static String channelID = "global";
  private static String host = "localhost";
  private static String senderID;

  public static void main(String[] args)
  {
    host = args.length > 0 ? args[0] : "localhost";
    System.err.println("Connecting to Chat Server on host " + host);
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(String.format(URI_TEMPLATE, host, 8081, channelID));
    SseEventSource eventSource = SseEventSource.target(target).build();
    try {
      eventSource.register(ChatClient::processMessage,
                           (e) -> {
                             System.err.println("Error from chat server : " + e.getMessage());
                             e.printStackTrace();
                           }, () -> {
          System.err.println("Connection lost.");
        });
      eventSource.open();
      System.err.printf("Connected to chat channel #%s.%n", channelID);
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
        @Override
        public void run()
        {
          eventSource.close();
        }
      });
    } catch (Exception e) {
      System.err.println("Error connecting to chat server : " + e.getMessage());
      e.printStackTrace();
    }
    System.err.println("What's your name?");
    Scanner scanner = new Scanner(System.in);
    if (scanner.hasNextLine())
      senderID = scanner.nextLine();
    System.err.println("Hi " + senderID + ". Chat away!");
    sendMessage(target, senderID + " has entered the channel.", "system");
    while (scanner.hasNextLine()) {
      String command = scanner.nextLine().trim();
      if (command.length() > 0) {
        if (command.startsWith("::")) {
          command = command.substring(2);
          System.err.printf("Unsupported command \"%s\"%n", command);
        } else {
          sendMessage(target, command, senderID);
        }
      }
    }
  }

  private static void sendMessage(WebTarget target, String content, String senderID)
  {
    JsonObject message = new JsonObject();
    String messageID = UUID.randomUUID().toString();
    message.set("messageId", messageID);
    message.set("content", content);
    message.set("senderId", senderID);
    String json = message.toString();
//          System.out.println("Posting message : json = " + json);
    UNSEEN_MESSAGES.put(messageID, messageID);
    try {
      Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(json));
      if (response.getStatus() == 200) {
//            System.out.println("Posted message : messageID = " + messageID);
      }
      else if (response.getStatus() == 400) {
        System.err.printf("The message \"%s\" could not be sent : %s%n", content, response.readEntity(String.class));
      }
      else {
        throw new RuntimeException(
          "Response code : " + response.getStatus() + ", Response : " + response.readEntity(String.class));
      }
    } catch (RuntimeException e) {
      System.err.printf("The message \"%s\" could not be sent.%n", content);
      e.printStackTrace();
    }
  }

  private static void processMessage(InboundSseEvent inboundSseEvent)
  {
    JsonObject message = Json.parse(inboundSseEvent.readData()).asObject();
    String messageId = getMessageValue(message, "messageId");
//    System.out.println("Processing message : messageId = " + messageId);
    if (!UNSEEN_MESSAGES.remove(messageId, messageId)) {
      String content = getMessageValue(message, "content");
      String senderID = getMessageValue(message, "senderId");
      String messageLine = String.format("[#%s] %s : \"%s\"", channelID,
                                         senderID != null && senderID.length() > 0 ? senderID : "<anonymous>", content);
      System.out.println(messageLine);
    }
  }

  private static String getMessageValue(JsonObject message, String key)
  {
    JsonValue value = message.get(key);
    if (value.isNull())
      return null;
    return value.asString();
  }
}
