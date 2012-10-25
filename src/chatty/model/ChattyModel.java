package chatty.model;



import java.io.File;
import java.io.IOException;

import chatty.controller.ChattyController;

public class ChattyModel {
//  private ChattyController _controller;
  private MessageManager _msgManager;
  private UserManager _usrManager;
  
  public ChattyModel(ChattyController c) {
//    _controller = c;
    _msgManager = new MessageManager(this);
    _usrManager = new UserManager(this);
  }
  
  public void messageReceived(Message m) {
    _usrManager.messageReceived(m);
    _msgManager.messageReceived(m);
  }
  
  public MessageManager getMessageManager() {
    return _msgManager;
  }
  
  public UserManager getUserManager() {
    return _usrManager;
  }

  public String getOrCreateUser(String username) {
    return _usrManager.getOrCreateUser(username);
  }

  public void sendMessage(OutgoingMessage outgoingMessage) {
    // TODO Auto-generated method stub
    
  }

  public void clearTimedout() {
    _usrManager.pruneUsers();
  }

  public void writeLog(File file) throws IOException {
    _msgManager.writeLog(file);
  }
}
