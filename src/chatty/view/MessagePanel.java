package chatty.view;

import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;
import chatty.view.htmleditor.HTMLEditor;

public class MessagePanel extends HTMLEditor implements Observer {

  private static final long serialVersionUID = -7367865981828969704L;
  public MessagePanel(ChattyView view) {
    super(false);
    //this.enableDebugEditing();
    this.setSize(200, 200);
  }
  public void addMessage(Object msg) {
    //System.out.println("Message Panel::Insert Message--->"+msg.toString());
    this.appendHTML(msg.toString());
  }
  
  @Override
  public void update(Observable o, final Object arg) {

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        addMessage(arg);        
      }
    });
  }
  public void clearChat() {
    this.setHTML("");
  }
  
}
