package chatty.view.htmleditor;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.SizeRequirements;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;

public class HTMLEditorKitExt extends HTMLEditorKit {
  private static final long serialVersionUID = -6028259734802145411L;

  public HTMLEditorKitExt() {
    super();
  }

  public static class BackgroundAction extends StyledEditorKit.StyledTextAction {
    private static final long serialVersionUID = 8331565521948424427L;
    private Color fg;

    public BackgroundAction(String paramString, Color paramColor) {
      super(paramString);
      this.fg = paramColor;
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
      JEditorPane localJEditorPane = getEditor(paramActionEvent);
      if (localJEditorPane == null)
        return;
      Color localColor = this.fg;
      Object localObject;
      if ((paramActionEvent != null)
          && (paramActionEvent.getSource() == localJEditorPane)) {
        localObject = paramActionEvent.getActionCommand();
        try {
          localColor = Color.decode((String) localObject);
        } catch (NumberFormatException localNumberFormatException) {
        }
      }
      if (localColor != null) {
        localObject = new SimpleAttributeSet();
        StyleConstants.setBackground((MutableAttributeSet) localObject,
            localColor);
        setParagraphAttributes(localJEditorPane, (AttributeSet) localObject,
            false);
        // setCharacterAttributes(localJEditorPane, (AttributeSet)localObject,
        // false);
      } else {
        UIManager.getLookAndFeel().provideErrorFeedback(localJEditorPane);
      }
    }
  }

  public static class SuperScriptAction extends
      StyledEditorKit.StyledTextAction {
    /**
     * 
     */
    private static final long serialVersionUID = -7867923107858298364L;

    public SuperScriptAction() {
      super("font-superscript");
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
      JEditorPane localJEditorPane = getEditor(paramActionEvent);
      if (localJEditorPane == null)
        return;
      StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
      MutableAttributeSet localMutableAttributeSet = localStyledEditorKit
          .getInputAttributes();
      boolean bool = !(StyleConstants.isSuperscript(localMutableAttributeSet));
      SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
      StyleConstants.setSuperscript(localSimpleAttributeSet, bool);
      setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
    }
  }

  public static class SubScriptAction extends StyledEditorKit.StyledTextAction {
    /**
     * 
     */
    private static final long serialVersionUID = -7867923107858298364L;

    public SubScriptAction() {
      super("font-subscript");
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
      JEditorPane localJEditorPane = getEditor(paramActionEvent);
      if (localJEditorPane == null)
        return;
      StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
      MutableAttributeSet localMutableAttributeSet = localStyledEditorKit
          .getInputAttributes();
      boolean bool = !(StyleConstants.isSubscript(localMutableAttributeSet));
      SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
      StyleConstants.setSubscript(localSimpleAttributeSet, bool);
      setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
    }
  }

  public static class StrikeThroughAction extends
      StyledEditorKit.StyledTextAction {
    /**
     * 
     */
    private static final long serialVersionUID = -7867923107858298364L;

    public StrikeThroughAction() {
      super("font-strikethrough");
    }

    public void actionPerformed(ActionEvent paramActionEvent) {
      JEditorPane localJEditorPane = getEditor(paramActionEvent);
      if (localJEditorPane == null)
        return;
      StyledEditorKit localStyledEditorKit = getStyledEditorKit(localJEditorPane);
      MutableAttributeSet localMutableAttributeSet = localStyledEditorKit
          .getInputAttributes();
      boolean bool = !(StyleConstants.isStrikeThrough(localMutableAttributeSet));
      SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
      StyleConstants.setStrikeThrough(localSimpleAttributeSet, bool);
      setCharacterAttributes(localJEditorPane, localSimpleAttributeSet, false);
    }
  }
  
  private static class WrappingInlineView extends InlineView {
    WrappingInlineView(Element e) { super(e); }
    public int getBreakWeight(int axis, float pos, float len) {
      return View.GoodBreakWeight;
    }
    public View breakView(int axis, int p0, float pos, float len) {
      if (axis != View.X_AXIS) {
        return this; // don't care, horizontal scrolling is good 
      }
      // check if the position is off the edge, and if it is break the line
      this.checkPainter(); 
      int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
      int start = getStartOffset();
      int end = getEndOffset();
      return (p0 == start && p1 == end) ? this : createFragment(p0, p1);  
    }
  }
  private static class WrappingParagraphView extends ParagraphView {
    public WrappingParagraphView(Element elem) { super(elem); }
    protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
      r = (r != null) ? r : new SizeRequirements();
      r.minimum = (int)layoutPool.getMinimumSpan(axis);
      r.preferred = (int)layoutPool.getPreferredSpan(axis);
      r.maximum = Integer.MAX_VALUE;
      r.alignment = 0.5f;
      if (r.minimum > r.preferred) {
        r.preferred = r.minimum;
      }
      return r;
    }
  }
  private static class WrappingHTMLFactory extends HTMLFactory {
    public View create(Element elem) {
      View view = super.create(elem);
      if (view instanceof ParagraphView) {
        return new WrappingParagraphView(elem);
      } else if (view instanceof InlineView){
        return new WrappingInlineView(elem);
      } else {
        return view;
      }
    }
  }

  public ViewFactory getViewFactory() {
    return new WrappingHTMLFactory();
  }
  
}
