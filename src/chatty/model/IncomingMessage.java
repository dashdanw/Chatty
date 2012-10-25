package chatty.model;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingMessage implements Message {
  private String _sender;
  private String _message;
  private UUID _id;
  private Date _timestamp;
  private int _notice;// THIS IS CRUFT! TODO: REFACTOR!!!!!

  public IncomingMessage() {
    _notice = -1;  
    _timestamp = new Date();
    _sender = "";
    _message = "";
    _id = UUID.randomUUID();
  }
  
  public IncomingMessage(String user, String message, UUID uuid, Date timestamp, int isNotice) {
    _notice = isNotice;
    _id = uuid;
    _message = message;
    _sender = user;
    _timestamp = timestamp;
  }
  public boolean isNotice() {
    return _notice != -1;
  }
  public int getNotice() {
    return _notice;
  }
  public void setNotice(int in) {
    _notice = in;
  }
  public void setSender(String sender) {
    _sender = sender;
  }
  
  public void setMessage(String message) {
    _message = message;
  }
  
  public void setTimestamp(Date timestamp) {
    _timestamp = timestamp;
  }
  
  public void setId(UUID id) {
    _id = id;
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
  
  public String toString() {
    
    if (isNotice()) {
      return String.format("<p>[%tr] *** %s ***</p>", _timestamp, _message);
    } else {
      String message = _message.substring(_message.indexOf('>')+1, _message.lastIndexOf('<'));
      String t = message.replaceAll(" ", "");
      if (t.equalsIgnoreCase(""))
        return "";
      return String.format("<p>[%tr] [%s]: %s</p>", _timestamp, removeAddr(_sender), message);
    }
  }
  
  private String removeAddr(String name) {
    Pattern p = Pattern.compile("^(.*?)@(.*?)$");
    Matcher m = p.matcher(name);
    if (!m.matches()) {
      return name; // eh.
    }
    return m.group(1);
  }

  public boolean equals(Object o) {
    if (!(o instanceof IncomingMessage)) {
      return false;
    }
    return ((IncomingMessage)o).getMessageID().equals(_id);
  }

}
