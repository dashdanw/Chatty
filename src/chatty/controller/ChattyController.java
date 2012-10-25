package chatty.controller;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import chatty.model.*;
import chatty.view.*;

public class ChattyController {
  
  public static final int PORT = 40511;
  public static final String GROUP = "233.0.113.0";
  public static final int PACKET_SIZE = 1024;
  
  private ChattyView _view;
  private ChattyModel _model;
  private Inbox _inbox;
  private Outbox _outbox;
  private ScheduledExecutorService _executor;
  public ChattyController() {
    Settings.getInstance().initialize();
    _view = new ChattyView(this);
    _model = new ChattyModel(this);
    try {
      _inbox = new Inbox(this);
      _outbox = new Outbox(this);
      _inbox.start();
      _outbox.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
    _view.setModel(_model);
    onInit();
    _executor = Executors.newSingleThreadScheduledExecutor();
    _executor.scheduleWithFixedDelay(new Runnable() {
      public void run() {
        pollUsers();
      }
    }, 0, 1000, TimeUnit.MILLISECONDS);
    _executor.scheduleWithFixedDelay(new Runnable() {
      public void run() {
        _model.clearTimedout();
      }
    }, 0, 1300, TimeUnit.MILLISECONDS);
  }

  public static void main(String[] args) throws Exception {
    new ChattyController();
  }

  public void messageRecieved(IncomingMessage incomingMessage) {
    _model.messageReceived(incomingMessage);
  }
  public void pollUsers() {
    _outbox.pollUsers();
  }
  public String getOrCreateUser(String username) {
    return _model.getOrCreateUser(username);
  }

  public void sendMessage(String str) {
    _outbox.sendMessage(str);
  }
  
  private void onInit() {
    _outbox.sendSignOn(Settings.getInstance().getCurrentUsername());
  }
  
  public void onClose() {
    _outbox.sendSignOff(Settings.getInstance().getCurrentUsername());
    Settings.getInstance().save();
    _executor.shutdown();
  }
  
  public void changeUserName() {
    Settings s = Settings.getInstance();
    String old = s.getCurrentUsername();
    s.changeUserName();
    String curr = s.getCurrentUsername();
    if (old != curr) {
      _outbox.sendNameChange(old, curr);
    }
  }

  public void writeLog(File file) throws IOException {
    _model.writeLog(file);
  }
}
