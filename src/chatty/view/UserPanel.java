package chatty.view;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import chatty.model.UserManager;

public class UserPanel extends JScrollPane implements Observer, FileDropSource {

  private static final long serialVersionUID = 2709426959755724759L;
  private JList _uList;
  private DefaultListModel _mdl;
  private JPopupMenu menu;

  public UserPanel(ChattyView view) {
    super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);    _uList = new JList();
    
    menu = new JPopupMenu();
    JMenuItem userInfo = new JMenuItem("Get User Information");

    JMenuItem privateMessage = new JMenuItem("Send Private Message");

    JMenuItem sendFile = new JMenuItem("Send File");

    JMenuItem blockUser = new JMenuItem("Block User");

    menu.add(userInfo);
    menu.add(privateMessage);
    menu.add(sendFile);
    menu.add(blockUser);

    //FileTransferHandler fth = new FileTransferHandler(this);
    //fth.setHandler(view._control);
    _uList = new JList();
    _uList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //_uList.setTransferHandler(fth);
    _mdl = new DefaultListModel();
    _uList.setModel(_mdl);
    /*_uList.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.isPopupTrigger()) {
          doPopup(arg0.getX(), arg0.getY());
        }
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.isPopupTrigger()) {
          _uList.setSelectedIndex(_uList.locationToIndex(arg0.getPoint()));
          doPopup(arg0.getX(), arg0.getY());
        }
      }

      @Override
      public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.isPopupTrigger()) {
          _uList.setSelectedIndex(_uList.locationToIndex(arg0.getPoint()));
          doPopup(arg0.getX(), arg0.getY());
        }
      }
    });*/
    setViewportView(_uList);
    
    repaint();
  }

  protected void doPopup(int x, int y) {
    // TODO Auto-generated method stub
    menu.show(this, x, y);
  }

  @Override
  public void update(final Observable o, final Object arg) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (o instanceof UserManager) {
          UserManager um = (UserManager) o;
          List<String> users = new ArrayList<String>(um.getOnlineUsers());
          Collections.sort(users);
          _mdl.clear();
          for (String user : users) {
            _mdl.addElement(user);
          }
          repaint();
        }
      }
    });
  }

  @Override
  public String getUserName() {
    // TODO Auto-generated method stub
    return (String) _uList.getSelectedValue();
  }
}
