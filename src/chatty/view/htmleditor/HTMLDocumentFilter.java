package chatty.view.htmleditor;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
  
  public class HTMLDocumentFilter extends DocumentFilter {
    public static void main(String args[]) {
      JFrame frame = new JFrame("Filter Test");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JTextField textFieldOne = new JTextField();
      Document textDocOne = textFieldOne.getDocument();
      DocumentFilter filterOne = new HTMLDocumentFilter();
      ((AbstractDocument) textDocOne).setDocumentFilter(filterOne);
      frame.add(textFieldOne);

      frame.setSize(250, 150);
      frame.setVisible(true);
    }

    public HTMLDocumentFilter() {

    }

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
        AttributeSet attr) throws BadLocationException {
      super.insertString(fb, offset, string, attr);
    }

    public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
        throws BadLocationException {
      super.remove(fb, offset, length);
    }

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
        AttributeSet attrs) throws BadLocationException {
      super.replace(fb, offset, length, text, attrs);
    }
  }



