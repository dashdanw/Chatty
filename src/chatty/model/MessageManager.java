package chatty.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Observable;

import chatty.controller.ByteMaster;
//import java.util.Queue;

public class MessageManager extends Observable {
  private List<Message> _collection;

  // private ChattyModel _owner;
  public MessageManager(ChattyModel chattyModel) {
    super();
    _collection = new ArrayList<Message>();
  }

  public void messageReceived(Message m) {
    addMessage(m);
  }

  public List<Message> getMessageList() {
    return _collection;
  }

  public void addMessage(Message message) {
    if (message instanceof IncomingMessage) {
      if (((IncomingMessage)message).getNotice() == ByteMaster.PING) {
        return;
      }
    }
    _collection.add(message);
    setChanged();
    notifyObservers(message);
  }
  public void writeLog(File file) throws IOException {
    FileOutputStream fs = null;
    try {
      fs = new FileOutputStream(file);
      outputMessages(fs);
    } finally {
      if (fs != null) {
        fs.close();
      }
    }
  }
  public void outputMessages(OutputStream os) throws IOException {
    PrintStream ps = new PrintStream(os);
    ps.format(
        "<html>" +
    		  "<head>" +
    		    "<title>Chatty Log</title>" +
    		  "</head>" +
    		  "<body>" +
    		    "<h1>Chatty Log: Stored on %tc</h1>", new Date());
    for (Message m : _collection) {
      ps.println(m.toString());
    }
    ps.println("<p>End of log</p></body></html>");
    if (ps.checkError()) {
      throw new IOException("Error during log output");
    }
  }
}
