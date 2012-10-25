package chatty.view.htmleditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JEditorPane;

public class HTMLEditorPane extends JEditorPane {
  /**
   * 
   */
  private static final long serialVersionUID = 3754750391942066463L;

  boolean antiAliasing = true;

  public HTMLEditorPane(String text) {
    super("text/html", text);
  }

  public boolean isAntialiasing() {
    return antiAliasing;
  }

  public void setAntialiasing(boolean state) {
    antiAliasing = state;
  }

  public void paint(Graphics g) {
    if (antiAliasing) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      super.paint(g2);
    } else {
      super.paint(g);
    }
  }

  public void setSize(Dimension d) {
    /*if (d.width < getParent().getSize().width) {
      d.width = getParent().getSize().width;
    }*/
    super.setSize(d);
  }

  public boolean getScrollableTracksViewportWidth() {
    /*return true;*/
    return super.getScrollableTracksViewportWidth();
  }
}