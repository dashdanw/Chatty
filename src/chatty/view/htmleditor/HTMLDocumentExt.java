package chatty.view.htmleditor;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.parser.ParserDelegator;

public class HTMLDocumentExt extends HTMLDocument {
  /**
   * 
   */
  private static final long serialVersionUID = 1457504438009706801L;

  public HTMLDocumentExt() {
    super();
    setAsynchronousLoadPriority(4);
    setTokenThreshold(100);
    setParser(new ParserDelegator());
  }

  protected boolean isLink(Element e) {
    return (e.getAttributes().getAttribute(HTML.Tag.A) != null);
  }

  protected void computeLinks(Element e) throws BadLocationException {
    int caretPos = _pane.getCaretPosition();
    try {
      if (isLink(e))
        correctLink(e);
      else
        createLink(e);
    } catch (IOException ex) {
    }
    _pane.setCaretPosition(Math.min(caretPos, getLength()));
  }

  protected void correctLink(Element e) throws BadLocationException,
      IOException {
    int length = e.getEndOffset() - e.getStartOffset();
    boolean endOfDoc = e.getEndOffset() == getLength() + 1;
    if (endOfDoc)
      length--;
    String text = getText(e.getStartOffset(), length);
    setOuterHTML(e, text);
    Matcher spaceMatcher = Pattern.compile("(\\s+)$").matcher(text);
    if (spaceMatcher.find()) {
      String endingSpaces = spaceMatcher.group(1);
      insertString(Math.min(getLength(), e.getEndOffset()), endingSpaces, null);
    }
  }

  protected void createLink(Element e) throws BadLocationException, IOException {
    int caretPos = _pane.getCaretPosition();
    int startOffset = e.getStartOffset();
    int length = e.getEndOffset() - e.getStartOffset();
    boolean endOfDoc = e.getEndOffset() == getLength() + 1;
    if (endOfDoc)
      length--;
    String text = getText(startOffset, length);
    Matcher matcher = Pattern.compile(
        "(?i)(\\b(http://|https://|www.|ftp://|file:/|mailto:)\\S+)(\\s+)")
        .matcher(text);
    if (matcher.find()) {
      String url = matcher.group(1);
      String endingSpaces = matcher.group(3);
      int validPos = startOffset + matcher.start(3) + 1;
      if (validPos > caretPos)
        return;
      Matcher dotEndMatcher = Pattern.compile("([\\W&&[^/]]+)$").matcher(url);
      String endingDots = "";
      if (dotEndMatcher.find()) {
        endingDots = dotEndMatcher.group(1);
        url = dotEndMatcher.replaceFirst("");
      }
      boolean addhttp = false;
      if (url.indexOf("www.") >= 0) {
        if (url.indexOf("http://") < 0) {
          addhttp=true;
        }
      }
      if (addhttp)
        text = matcher.replaceFirst("<font color='blue'><u><a href='http://" + url
            + "'>" + url + "</a></u></font>" + endingDots + endingSpaces);
      else
      text = matcher.replaceFirst("<font color='blue'><u><a href='" + url
          + "'>" + url + "</a></u></font>" + endingDots + endingSpaces);
      setOuterHTML(e, text);
      Matcher spaceMatcher = Pattern.compile("^(\\s+)").matcher(text);
      if (spaceMatcher.find()) {
        String initialSpaces = spaceMatcher.group(1);
        insertString(startOffset, initialSpaces, null);
      }
      spaceMatcher = Pattern.compile("(\\s+)$").matcher(text);
      if (spaceMatcher.find()) {
        String extraSpaces = spaceMatcher.group(1);
        int endoffset = e.getEndOffset();
        if (extraSpaces.charAt(extraSpaces.length() - 1) == '\n') {
          extraSpaces = extraSpaces.substring(0, extraSpaces.length() - 1);
          endoffset--;
        }
        insertString(Math.min(getLength(), endoffset), extraSpaces, null);
      }
    }
  }

  public void remove(int offs, int len) throws BadLocationException {
    super.remove(offs, len);
    Element e = getCharacterElement(offs - len);
    computeLinks(e);
  }

  public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException {
    if (_documentMaxLength>0&&(_pane.getText().length()+str.length()>_documentMaxLength))
      return;
    super.insertString(offs, str, a);
    Element e = getCharacterElement(offs);
    computeLinks(e);
  }
  
  public void insert(int paramInt, ElementSpec[] paramArrayOfElementSpec) throws BadLocationException {
    if (_documentMaxLength>0&&(_pane.getText().length()>_documentMaxLength))
      return;
    super.insert(paramInt, paramArrayOfElementSpec);
  }

  public void insertBeforeEnd(Element element, String html)
      throws BadLocationException, IOException {
    super.insertBeforeEnd(element, html);
    Rectangle rect = _pane.getVisibleRect();
    rect.y = _pane.getHeight();this.
    _pane.scrollRectToVisible(rect);
  }

  JEditorPane _pane;

  public void setContainer(JEditorPane pane) {
    _pane = pane;
  }
  
  private int _documentMaxLength = 0;
  
  public void setDocumentMaxLength(int length)   {
    _documentMaxLength = length;
  }
}