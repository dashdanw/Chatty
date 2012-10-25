package chatty.view;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ChattyMenu extends JMenuBar {
  private static final long serialVersionUID = -6767220025582467796L;

  public ChattyMenu(final ChattyView parent) {
    super();

    JMenu menu = new JMenu("File");
    add(menu);
    JMenuItem writeLog = new JMenuItem("Save Log");
    writeLog.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
        ActionEvent.CTRL_MASK));
    writeLog.setMnemonic(KeyEvent.VK_C);
    writeLog.getAccessibleContext().setAccessibleDescription(
        "Write the chat transcript to a log file.");
    writeLog.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        FileDialog fd = new FileDialog(parent, "Save Chatty Log...",
            FileDialog.SAVE);
        fd.setModal(true);
        fd.setLocationRelativeTo(null);
        fd.setVisible(true);
        if (fd.getDirectory() == null)
          return;
        File file = new File(fd.getDirectory()
            + System.getProperty("file.separator") + fd.getFile());
        boolean failed = false;
        try {
          parent.writeLog(file);
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(parent,
              "Something went wrong when trying to save the log.",
              "File error", JOptionPane.ERROR_MESSAGE);
          failed = true;
        }
        if (!failed) {
          JOptionPane.showMessageDialog(parent, "File successfully saved.");
        }
      }
    });
    menu.add(writeLog);
    
    menu.addSeparator();
    
    JMenuItem mntmPrintSetup = new JMenuItem("Print Setup");
    mntmPrintSetup.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parent.printSetup();
      }
    });
    menu.add(mntmPrintSetup);

    JMenuItem mntmPrintChat = new JMenuItem("Print Chat");
    mntmPrintChat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
        ActionEvent.CTRL_MASK));
    mntmPrintChat.setMnemonic(KeyEvent.VK_C);
    mntmPrintChat.getAccessibleContext().setAccessibleDescription(
        "Print the chat transcript.");
    mntmPrintChat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parent.printChat();
      }
    });
    menu.add(mntmPrintChat);
    
    menu.addSeparator();

    JMenuItem mntmClearChat = new JMenuItem("Clear Chat");
    mntmClearChat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parent.clearChat();
      }
    });
    menu.add(mntmClearChat);
    
    menu.addSeparator();

    JMenuItem mntmExit = new JMenuItem("Exit");
    mntmExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        parent.onClose();
      }
    });
    menu.add(mntmExit);

    // SETTINGS
    menu = new JMenu("Edit");
    add(menu);
    JMenuItem changename = new JMenuItem("Change Username");
    changename.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
        ActionEvent.ALT_MASK));
    changename.setMnemonic(KeyEvent.VK_C);
    changename.getAccessibleContext().setAccessibleDescription(
        "Change your user name.");
    changename.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        parent.changeUserName();
      }
    });
    menu.add(changename);

    JMenu mnAppearance = new JMenu("Appearance");

    ButtonGroup bg = new ButtonGroup();

    String lafId = UIManager.getLookAndFeel().getID();
    UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();

    for (int i = 0; i < lafInfo.length; i++) {
      String laf = lafInfo[i].getClassName();
      String name = lafInfo[i].getName();

      Action action = new ChangeLookAndFeelAction(laf, name, parent);
      JRadioButtonMenuItem mi = new JRadioButtonMenuItem(action);
      mnAppearance.add(mi);
      bg.add(mi);

      if (name.equals(lafId)) {
        mi.setSelected(true);
      }
    }

    menu.add(mnAppearance);
  }

  class ChangeLookAndFeelAction extends AbstractAction {
    /**
     * 
     */
    private static final long serialVersionUID = -202085100593481184L;
    private String laf;
    private ChattyView parent;

    protected ChangeLookAndFeelAction(String laf, String name, ChattyView parent) {
      this.laf = laf;
      this.parent = parent;
      putValue(Action.NAME, name);
      putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
    }

    public void actionPerformed(ActionEvent e) {
      try {
        parent.setLookAndFeel(laf);
      } catch (Exception ex) {
        System.out.println("Failed loading L&F: " + laf);
      }
    }
  }
}
