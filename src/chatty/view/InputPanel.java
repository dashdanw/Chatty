package chatty.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import chatty.view.htmleditor.HTMLEditor;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Component;

public class InputPanel extends JPanel {

  private static final long serialVersionUID = 3913364768622037207L;
  private HTMLEditor _htmlField;
  private JButton _btn;
  private List<Runnable> _hooks;
  public InputPanel(ChattyView view) {
    _htmlField = new HTMLEditor(true);
    _htmlField.enableBasicEditing();
    _htmlField.enableExtendedEditing();
    //_htmlField.enableAlignmentEditing();
    //_htmlField.enableFontCustomizationEditing();
    _htmlField.enableWebEditing();
    //_htmlField.enableDebugEditing();
    _htmlField.setDocumentLength(900);
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    add(_htmlField);
    _btn = new JButton("Send");
    _btn.setAlignmentY(Component.TOP_ALIGNMENT);
    _btn.setVerticalAlignment(SwingConstants.TOP);
    add(_btn);
    _btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (_htmlField.getHTMLBody().trim().length() > 0) {
          for (Runnable hook : _hooks) {
            hook.run();
          }
          _htmlField.setDefaultHTML();
          _htmlField.focusOnText();
        }
      }
    });
    _htmlField.addInputListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent arg0) {
        if (arg0.getModifiers()==0&&arg0.getKeyCode()==KeyEvent.VK_ENTER)
          _btn.doClick();
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }
    });

    _hooks = new ArrayList<Runnable>();
    requestFocus();
  }
  
  public String getText() {
    return _htmlField.getHTMLBody().trim();
  }

  public void focusOnText() {
    _htmlField.focusOnText();
  }
  public void addHook(Runnable hook) {
    _hooks.add(hook);
  }
  public void removeHook(Runnable hook) {
    _hooks.remove(hook);
  }

  public void clear() {
    _htmlField.setDefaultHTML();
    _htmlField.focusOnText();
  }

  public void onClose() {
    _htmlField.onClose();
  }

  public void setText(String html) {
    _htmlField.setHTML(html);
    _htmlField.focusOnText();
  }
}