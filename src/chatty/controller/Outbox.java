package chatty.controller;
import java.io.IOException;
import java.net.*;
//import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


//import chatty.model.*;
public class Outbox {
  public static final int REPEAT_COUNT = 10;
  private Sender _sender;
  private ByteMaster _bm;
//  private ChattyController _owner;
  public Outbox(ChattyController c) throws IOException {
    _sender = new Sender();
    _bm = new ByteMaster();
//    _owner = c;
  }
  public void start() {
    _sender.start();
  }
  public void stop() {
    _sender.stop();
  }
  public void sendSignOn(String withname) {
    _sender.addMessage(_bm.encodeSignOnMessage(withname, new Date()));
  }
  public void sendMessage(String msg) {
    List<byte[]> msgs = _bm.encodeChatMessage(msg);
    for (byte[] raw : msgs) {
      _sender.addMessage(raw);
    }
  }
  public void sendSignOff(String withname) {
    _sender.addMessage(_bm.encodeSignOffMessage(withname, new Date()));
    try {// wait for that to complete.
      Thread.sleep(400);
    } catch (InterruptedException e) {
      // e.printStackTrace();
      // we're quitting anyway.
    } 
  }
  public void sendNameChange(String old, String curr) {
    _sender.addMessage(_bm.encodeNameChange(old, curr));
  }

  public void pollUsers() {
    _sender.addMessage(_bm.createPing());
  }
  
  private class Sender implements Runnable {
    private boolean _running;
    private Queue<byte[]> _pending;
    private MulticastSocket _socket;
    public Sender() throws IOException {
      _running = false;
      // is this overkill?
      _pending = new ConcurrentLinkedQueue<byte[]>();
      _socket  = new MulticastSocket();
    }
    
    public void start() {
      _running = true;
      new Thread(this).start();
    }
    
    public synchronized void stop() {
      _running = false;
    }
    public synchronized void addMessage(byte[] m) {
      for (int i = 0; i < REPEAT_COUNT; ++i) {
        _pending.offer(m);
      }
    }
    public void run() {
      while (_running) {
        if (!_pending.isEmpty()) {
          for (Iterator<byte[]> iter = _pending.iterator(); iter.hasNext();) {
            boolean didSend;
            try {
              byte[] buff = iter.next();
              InetAddress group = InetAddress.getByName(ChattyController.GROUP);
              DatagramPacket pkt = new DatagramPacket(buff, buff.length, group, ChattyController.PORT);
              _socket.send(pkt);
              didSend = true;
            } catch (IOException e) {
              didSend = false;
            }
            if (didSend) {
              iter.remove();
            }
          }
        }
        
        try {
          Thread.sleep(200);
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
