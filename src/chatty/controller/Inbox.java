package chatty.controller;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import chatty.model.IncomingMessage;

public class Inbox {
  private Listener _listener;
  private ByteMaster _bm;
  private ChattyController _owner;
  private Set<UUID> _uuids;
  public Inbox(ChattyController c) {
    _uuids = new HashSet<UUID>();
    _bm = new ByteMaster();
    _listener = new Listener();
    _owner = c;
  }
  
  public void start() {
    _listener.start();
  }
  
  public void stop() {
    _listener.stop();
  }
  public synchronized void messageRecieved(byte[] msg) {
    IncomingMessage im = _bm.decode(msg);
    if (im == null) {
      System.out.format("bm couldnt decode %s\n", new String(msg));
      return;
    }
    if (!_uuids.contains(im.getMessageID())) {
      _uuids.add(im.getMessageID());
      _owner.messageRecieved(im);
    }
  }
  
  private class Listener implements Runnable {
    private boolean _running;
    private MulticastSocket _socket;
    private InetAddress _group;
    public Listener() {
      _running = false;
      try {
        _socket = new MulticastSocket(ChattyController.PORT);
        _group = InetAddress.getByName(ChattyController.GROUP);
        _socket.joinGroup(_group);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    
    public void start() {
      _running = true;
      new Thread(this).start();
    }
    
    public void stop() {
      _running = false;
      try {
        _socket.leaveGroup(_group);
      } catch(Exception e) {
        e.printStackTrace();
      } finally {
        _socket.close();
      }
    }
    
    public void run() {
      while (_running) {
        byte[] buff = new byte[ChattyController.PACKET_SIZE];
        DatagramPacket pkt = new DatagramPacket(buff, buff.length);
        try {
          _socket.receive(pkt);
          messageRecieved(pkt.getData());
        } catch (IOException e) {
          e.printStackTrace();
        } 
      }
    }
  }
}
