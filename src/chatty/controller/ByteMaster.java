package chatty.controller;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chatty.model.IncomingMessage;
import chatty.model.OutgoingMessage;

// BYTEMASTER

// RULER OF THE BYTES

// (THIS IS WHERE MESSAGES ARE ENCODED/DECODED.)

public class ByteMaster {
  // This class should have _NO STATE_.  DO NOT ADD ANY NON-CONSTANT VARIABLES TO IT WITHOUT TALKING TO ME FIRST
  // -Thom
  public static final byte CHAT_MESSAGE           = 101; // visible characters now.
  public static final byte SIGNON_MESSAGE         = 102;
  public static final byte SIGNOFF_MESSAGE        = 103;
  public static final byte NAME_CHANGE            = 104;
  public static final byte PING                   = 105;
  // Not Yet Implemented
  public static final byte USER_PROFILE           = 107;
  public static final byte REQUEST_FILE_TRANSFER  = 108;
  public static final byte FILE_TRANSFER          = 109;
  
  // added when a lot of things weren't working right.
  // probably not necessary in the long run
  public static final Charset ENCODING = Charset.forName("US-ASCII");
  
  //private static final String newLine = System.getProperty("line.separator");


  public ByteMaster() {}
  public IncomingMessage decode(byte[] packet) {
    ByteBuffer bb = ByteBuffer.wrap(packet);
    byte tag = bb.get();
    long high = bb.getLong();
    long low = bb.getLong();
    int size = bb.position();    
    for (int i = size; i < packet.length; ++i) {
      if (packet[i] != 0) {
        ++size;
      } else {
        break;
      }
    }
    byte[] untagged = new byte[size-17];
    bb.get(untagged, 0, size-17);
    IncomingMessage msg;
    switch (tag) {
    case CHAT_MESSAGE:    msg = decodeChatMessage(untagged); break;
    case SIGNON_MESSAGE:  msg = decodeSignOnMessage(untagged); break;
    case SIGNOFF_MESSAGE: msg = decodeSignOffMessage(untagged); break;
    case NAME_CHANGE:     msg = decodeNameChange(untagged); break;
    case PING:            msg = decodePing(untagged); break;
    default:
      handleUnknownMessageType(packet);
      return null;
    }
    if (msg != null) {
      msg.setId(new UUID(high, low));
    }
    return msg;
    
  }
  
  public void handleUnknownMessageType(byte[] packet) {
    System.out.format("ERROR: RECIEVED UNKNOWN MESSAGE TYPE: %s.  MESSAGE CONTENTS: %s\n", packet[0], new String(packet));
  }
  public IncomingMessage decodeSignOffMessage(byte[] packet) {
    Pattern p = Pattern.compile("^\\[(.*?)\\] \\*\\*\\* (.*?) has signed off \\*\\*\\*$");
    String rawmsg = new String(packet, ENCODING);
    
    Matcher m = p.matcher(rawmsg);
    if (!m.matches()) {
      return null;
    }
    String date = m.group(1);
    String user = m.group(2);
    IncomingMessage im = new IncomingMessage();
    im.setNotice(SIGNOFF_MESSAGE);
    im.setSender(user);
    im.setMessage(String.format("%s has signed off", user));
    try {
      im.setTimestamp(SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).parse(date));
    } catch (ParseException e) {
      System.out.printf("WARNING! FAILED TO PARSE DATE FOR MESSAGE. DATE: \"%s\". MESSAGE:\"%s\".\n", date, rawmsg);
    }
    return im;
  }
  
  public byte[] encodeSignOnMessage(String uname, Date now) {
    String s = String.format("[%tr] *** %s has signed on ***", now, uname);
    UUID u = UUID.randomUUID();
    return ByteBuffer.allocate(s.length()+17)
        .put(SIGNON_MESSAGE)
        .putLong(u.getMostSignificantBits())
        .putLong(u.getLeastSignificantBits())
        .put(s.getBytes(ENCODING))
        .array();
  }
  
  public byte[] encodeSignOffMessage(String uname, Date now) {
    String s = String.format("[%tr] *** %s has signed off ***", now, uname);
    UUID u = UUID.randomUUID();
    return ByteBuffer.allocate(s.length()+17)
        .put(SIGNOFF_MESSAGE)
        .putLong(u.getMostSignificantBits())
        .putLong(u.getLeastSignificantBits())
        .put(s.getBytes(ENCODING))
        .array();
  }
  
  public IncomingMessage decodeSignOnMessage(byte[] packet) {
    Pattern p = Pattern.compile("^\\[(.*?)\\] \\*\\*\\* (.*?) has signed on \\*\\*\\*$");
    String rawmsg = new String(packet, ENCODING);

    Matcher m = p.matcher(rawmsg);
    if (!m.matches()) {
      System.out.format("UNKNOWN SIGNON MSG: \"%s\"\n", rawmsg);
      return null;//throw new IllegalArgumentException();
    }
    String date = m.group(1);
    String user = m.group(2);
    IncomingMessage im = new IncomingMessage();
    im.setNotice(SIGNON_MESSAGE);
    im.setSender(user);
    im.setMessage(String.format("%s has signed on", user));
    try {
      im.setTimestamp(SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).parse(date));
    } catch (ParseException e) {
      System.out.printf("WARNING! FAILED TO PARSE DATE FOR MESSAGE. DATE: \"%s\". MESSAGE:\"%s\".\n", date, new String(packet));
    }
    return im;
  }
  

  public List<byte[]> encodeChatMessage(String msg) {
    // TODO Allow multiple pack
    //List<String> msgs = cutUpMsgText(msg);
    List<byte[]> ret = new ArrayList<byte[]>(); 
    /*for (String txt : msgs) {
      ret.add(encodeOutgoingMessage(new OutgoingMessage(txt)));
    }*/
    ret.add(encodeOutgoingMessage(new OutgoingMessage(msg)));
    return ret;
  }

  public byte[] encodeOutgoingMessage(OutgoingMessage om) {
    String s = String.format("[%tr] <%s>: %s", om.getTimestamp(), om.getSender(), om.getText());
    UUID u = om.getMessageID();
    return ByteBuffer.allocate(s.length()+17)
        .put(CHAT_MESSAGE)
        .putLong(u.getMostSignificantBits())
        .putLong(u.getLeastSignificantBits())
        .put(s.getBytes(ENCODING))
        .array();
  }
  
  public List<String> cutUpMsgText(String s) {
    ArrayList<String> l = new ArrayList<String>((s.length()+OutgoingMessage.MAX_TEXT_SIZE-1)/OutgoingMessage.MAX_TEXT_SIZE);
    for (int i = 0; i < s.length(); i += OutgoingMessage.MAX_TEXT_SIZE) {
      l.add(s.substring(i, Math.min(s.length(), i+OutgoingMessage.MAX_TEXT_SIZE)));
    }
    return l;
  }
  
  public IncomingMessage decodeNameChange(byte[] packet) {
    Pattern p = Pattern.compile("^\\[(.*?)\\] \\*\\*\\* (.*?) has changed their name to (.*?) \\*\\*\\*$");
    String rawmsg = new String(packet, ENCODING);
    Matcher m = p.matcher(rawmsg);
    if (!m.matches()) {
      return null;
    }
    
    String date = m.group(1);
    String olduser = m.group(2);
    String user = m.group(3);
    
    IncomingMessage im = new IncomingMessage();
    im.setNotice(NAME_CHANGE);
    im.setSender(user);
    Pattern p2 = Pattern.compile("^(.*?)@(.*?)$");
    Matcher m2 = p2.matcher(olduser);
    Matcher m3 = p2.matcher(user);
    String ua, ub;
    if (!m2.matches()) ua = olduser;
    else ua = m2.group(1);
    if (!m3.matches()) ub = user;
    else ub = m3.group(1);
    im.setMessage(String.format("%s has changed their name to %s #HOST{ %s }", ua, ub, m3.group(2)));
    try {
      im.setTimestamp(SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).parse(date));
    } catch (ParseException e) {
      System.out.printf("FAILED TO PARSE DATE FOR MESSAGE. DATE: \"%s\". MESSAGE:\"%s\".\n", date,rawmsg);
    }
    return im;
  }

  public byte[] encodeNameChange(String old, String curr) {
    String s = String.format("[%tr] *** %s has changed their name to %s ***", new Date(), old, curr);
    UUID u = UUID.randomUUID();
    return ByteBuffer
        .allocate(s.length()+17)
        .put(NAME_CHANGE)
        .putLong(u.getMostSignificantBits())
        .putLong(u.getLeastSignificantBits())
        .put(s.getBytes(ENCODING))
        .array();
  }
  
  public IncomingMessage decodeChatMessage(byte[] packet) {
    
    String rawmsg = new String(packet, ENCODING);
    String lines[] = rawmsg.split("\r?\n|\r");
    rawmsg = "";
    for (int i = 0; i<lines.length; i++)
      rawmsg = rawmsg+lines[i];
    Pattern p = Pattern.compile("^\\[(.*?)\\] <(.*?)>: (.*?)$",Pattern.MULTILINE);
    Matcher m = p.matcher(rawmsg);
    if (!m.matches()) {
      return null;
    }
    String date = m.group(1);
    String user = m.group(2);
    String mesg = m.group(3);
    
    IncomingMessage msg = new IncomingMessage();
    msg.setNotice(-1);
    msg.setId(UUID.randomUUID());
    
    msg.setMessage(mesg);
    
    msg.setSender(user);
    
    try {
      msg.setTimestamp(SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).parse(date));
    } catch (ParseException e) {
      System.out.printf("WARNING! FAILED TO PARSE DATE FOR MESSAGE. DATE: \"%s\". MESSAGE:\"%s\".\n", date, rawmsg);
    }
    return msg;
  }
  public byte[] createPing() {
    String s = String.format("[%tr] #PING{ %s }", new Date(), Settings.getInstance().getCurrentUsername()); 
    UUID u = UUID.randomUUID();
    return ByteBuffer.allocate(s.length()+17)
        .put(PING)
        .putLong(u.getMostSignificantBits())
        .putLong(u.getLeastSignificantBits())
        .put(s.getBytes(ENCODING))
        .array();

  }
  public IncomingMessage decodePing(byte[] packet) {
    Pattern p = Pattern.compile("^\\[(.*?)\\] #PING\\{ (.*?) \\}$");
    String rawmsg = new String(packet, ENCODING);
    Matcher m = p.matcher(rawmsg);
    if (!m.matches()) {
      return null;
    }
    
    String date = m.group(1);
    String user = m.group(2);
    
    IncomingMessage im = new IncomingMessage();
    im.setNotice(PING);
    im.setSender(user);
    im.setMessage("");
    try {
      im.setTimestamp(SimpleDateFormat.getTimeInstance(DateFormat.MEDIUM).parse(date));
    } catch (ParseException e) {
      return null;
    }
    return im;
  }
}
