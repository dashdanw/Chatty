package chatty.view.htmleditor;

import java.awt.*;
import java.awt.print.*;
import javax.swing.JEditorPane;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class HTMLDocumentPrinter implements Printable {
  protected int currentPage = -1;
  protected JEditorPane editorPane;
  protected double pageEnd = 0;
  protected double pageStart = 0;
  protected boolean scaleWidthToFit = true;
  protected PageFormat pageFormat;
  protected PrinterJob printerJob;

  public HTMLDocumentPrinter() {
    editorPane = new JEditorPane();
    pageFormat = new PageFormat();
    printerJob = PrinterJob.getPrinterJob();
  }

  public Document getDocument() {
    if (editorPane != null)
      return editorPane.getDocument();
    else
      return null;
  }

  public boolean getScaleWidthToFit() {
    return scaleWidthToFit;
  }

  public void pageDialog() {
    pageFormat = printerJob.pageDialog(pageFormat);
  }

  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
    double scale = 1.0;
    Graphics2D graphics2D;
    View rootView;
    graphics2D = (Graphics2D) graphics;
    editorPane.setSize((int) pageFormat.getImageableWidth(), Integer.MAX_VALUE);
    editorPane.validate();
    rootView = editorPane.getUI().getRootView(editorPane);
    if ((scaleWidthToFit)) {
      scale = pageFormat.getImageableWidth()
          / editorPane.getMinimumSize().getWidth();
      graphics2D.scale(scale, scale);
    }
    graphics2D.setClip((int) (pageFormat.getImageableX() / scale),
        (int) (pageFormat.getImageableY() / scale),
        (int) (pageFormat.getImageableWidth() / scale),
        (int) (pageFormat.getImageableHeight() / scale));
    if (pageIndex > currentPage) {
      currentPage = pageIndex;
      pageStart += pageEnd;
      pageEnd = graphics2D.getClipBounds().getHeight();
    }
    graphics2D.translate(graphics2D.getClipBounds().getX(), graphics2D
        .getClipBounds().getY());
    Rectangle allocation = new Rectangle(0, (int) -pageStart,
        (int) (editorPane.getMinimumSize().getWidth()),
        (int) (editorPane.getPreferredSize().getHeight()));
    if (printView(graphics2D, allocation, rootView)) {
      return Printable.PAGE_EXISTS;
    } else {
      pageStart = 0;
      pageEnd = 0;
      currentPage = -1;
      return Printable.NO_SUCH_PAGE;
    }
  }

  public void print(HTMLDocument htmlDocument) {
    setDocument("text/html", htmlDocument);
    printDialog();
  }

  public void print(StyledDocument styledDocument) {
    setDocument("text/rtf", styledDocument);
    printDialog();
  }

  public void print(PlainDocument plainDocument) {
    setDocument("text/plain", plainDocument);
    printDialog();
  }

  public void printHtmlString(String html) {
    printHtmlString(html, null);
  }

  public void printHtmlString(String html, Dimension dim) {
    HTMLEditorKit htmk = new HTMLEditorKit();
    HTMLDocument htdoc = (HTMLDocument) htmk.createDefaultDocument();
    editorPane = new JEditorPane();
    editorPane.setEditorKit(htmk);
    editorPane.setDocument(htdoc);
    editorPane.setText(html);
    if (dim != null) {
      setSize(dim);
    }
    printDialog();
  }

  protected void printDialog() {
    if (printerJob.printDialog()) {
      printerJob.setPrintable(this, pageFormat);
      try {
        printerJob.print();
      } catch (PrinterException printerException) {
        pageStart = 0;
        pageEnd = 0;
        currentPage = -1;
        System.out.println("Error Printing Document");
      }
    }
  }

  protected boolean printView(Graphics2D graphics2D, Shape allocation, View view) {
    boolean pageExists = false;
    Rectangle clipRectangle = graphics2D.getClipBounds();
    Shape childAllocation;
    View childView;
    if (view.getViewCount() > 0) {
      if (allocation.getBounds().getY() >= clipRectangle.getY()) {
        if (allocation.getBounds().getMaxY() <= clipRectangle.getMaxY()) {
          view.paint(graphics2D, allocation);
        }
      }
      for (int i = 0; i < view.getViewCount(); i++) {
        childAllocation = view.getChildAllocation(i, allocation);
        if (childAllocation != null) {
          childView = view.getView(i);
          if (printView(graphics2D, childAllocation, childView)) {
            pageExists = true;
          }
        }
      }
    } else {
      if (allocation.getBounds().getMaxY() >= clipRectangle.getY()) {
        pageExists = true;
        if ((allocation.getBounds().getHeight() > clipRectangle.getHeight())
            && (allocation.intersects(clipRectangle))) {
          view.paint(graphics2D, allocation);
        } else {
          if (allocation.getBounds().getY() >= clipRectangle.getY()) {
            if (allocation.getBounds().getMaxY() <= clipRectangle.getMaxY()) {
              view.paint(graphics2D, allocation);
            } else {
              if (allocation.getBounds().getY() < pageEnd) {
                pageEnd = allocation.getBounds().getY();
              }
            }
          }
        }
      }
    }
    return pageExists;
  }

  protected void setContentType(String type) {
    editorPane.setContentType(type);
  }

  protected void setDocument(String type, Document document) {
    setContentType(type);
    editorPane.setDocument(document);
  }

  public void setScaleWidthToFit(boolean scaleWidth) {
    scaleWidthToFit = scaleWidth;
  }

  public void setSize(Dimension dim) {
    editorPane.setMinimumSize(dim);
  }
}
