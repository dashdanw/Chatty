package chatty.view;

import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import chatty.controller.ChattyController;
import chatty.controller.Settings;
import chatty.model.ChattyModel;

public class ChattyView extends JFrame {
  private static final long serialVersionUID = 7940105305846478152L;
  private UserPanel _uPanel;
  private MessagePanel _mPanel;
  private InputPanel _iPanel;
  public ChattyController _control;
  private JMenuBar _chattyMenu;
 // private JFrame frame = this;

  public ChattyView(final ChattyController chattyController) {
    super(Settings.getInstance().getApplicationName());
    _control = chattyController;
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onClose();
      }
    });

    //setSize(800,600);
    //Toolkit toolkit = Toolkit.getDefaultToolkit();
    //Dimension scrnsize = toolkit.getScreenSize();
    this.setPreferredSize(new Dimension(800,600));
    //this.setMaximumSize(scrnsize);
    setLocationRelativeTo(null);
    // resizable pane (sort of, doesn't work when frame is resized)
    JSplitPane top = new JSplitPane();
    top.setResizeWeight(1);
    //this.getContentPane().add(top, BorderLayout.NORTH);

    _mPanel = new MessagePanel(this);
    _mPanel.setPreferredSize(new Dimension(504, 500));
    top.setLeftComponent(_mPanel);
    _uPanel = new UserPanel(this);
    _uPanel.setPreferredSize(new Dimension(136, 500));
    top.setRightComponent(_uPanel);
    top.setPreferredSize(new Dimension(640, 500));
    _iPanel = new InputPanel(this);
    _iPanel.setPreferredSize(new Dimension(640, 100));

    JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    pane.setResizeWeight(1);
    pane.setTopComponent(top);
    pane.setBottomComponent(_iPanel);
    pane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent pce) {
        // TODO Resize input panel accordingly.
        
      }
    });
    this.getContentPane().add(pane, BorderLayout.CENTER);
    top.setDividerLocation(550);
    pane.setDividerLocation(450);
    
    addWindowListener(new WindowAdapter() {
      public void windowActivated(WindowEvent e) {
        _iPanel.focusOnText();
      }
    });
    _chattyMenu = new ChattyMenu(this);
    this.setJMenuBar(_chattyMenu);

    setVisible(true);
    
    _iPanel.addHook(new Runnable() {
      public void run() {
        _control.sendMessage(_iPanel.getText().replace(System.getProperty("line.separator"), ""));
      }
    });
    this.pack();
  }
  
  public void onClose() {
    _iPanel.onClose();
    _control.onClose();
    dispose();
    System.exit(0);
  }

  public void setModel(final ChattyModel m) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        m.getMessageManager().addObserver(_mPanel);
        m.getUserManager().addObserver(_uPanel);
      }
    });
  }

  public void changeUserName() {
    _control.changeUserName();
  }

  public void setLookAndFeel(String lookAndFeel) {
    try {
      String html = "";
      if (_iPanel!=null) {
        html = _iPanel.getText();
      }
      UIManager.setLookAndFeel(lookAndFeel);
      Settings.getInstance().setLookAndFeel(lookAndFeel);
      Settings.getInstance().save();
      SwingUtilities.updateComponentTreeUI(this);
      _iPanel.setText(html);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public void writeLog(File file) throws IOException {
    _control.writeLog(file);
  }
  
  public void clearChat() {
    _mPanel.clearChat();
  }

  public void printChat() {
    _mPanel.printDocument();
  }

  public void printSetup() {
    _mPanel.printSetup();
  }
}
