package chatty.model;

import java.nio.ByteBuffer;
//import java.util.Collection;
import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
import java.util.UUID;

import chatty.controller.ChattyController;
import chatty.controller.Settings;

public class OutgoingMessage implements Message {

  private Date _timestamp;
  private String _message;
  private UUID _id;
  private String _sender;
  
  public static final int MAX_TEXT_SIZE = ChattyController.PACKET_SIZE - 16 - 16 - 32 - 8;

  public OutgoingMessage(String message) {
    this(Settings.getInstance().getCurrentUsername(), message, new Date());
  }
  public OutgoingMessage(String sender, String message, Date when) {
    _sender = sender;
    _timestamp = when;
    _message = message;
    _id = UUID.randomUUID();
  }

  public String getSender() {
    return _sender;
  }

  public UUID getMessageID() {
    return _id;
  }
  
  public Date getTimestamp() {
    return _timestamp;
  }
  
  public String getText() {
    return _message;
  }
  
  public byte[] toBytes() {
    String s = toString();
    return ByteBuffer.allocate(s.length()).put(s.getBytes()).array();
  }
  
  public String toString() {
    return String.format("[%tr] <%s>: %s", _timestamp, _sender, _message);
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof OutgoingMessage)) {
      return false;
    }
    return ((OutgoingMessage)o).getMessageID().equals(_id);
  }
}
