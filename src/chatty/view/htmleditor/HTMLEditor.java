package chatty.view.htmleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HTMLEditor extends JPanel implements DropTargetListener {
  /**
   * 
   */
  private static final long serialVersionUID = 1126178691970641204L;

  private static final char LINE_BREAK_ATTRIBUTE_NAME = '\r';

  private HTMLEditorPane htmlEditorPane = null;
  private JEditorPane htmlSourcePane = null;
  private JScrollPane jScrollPane = null;
  private HTMLEditorKitExt htmlEditorKit = null;
  private HTMLDocumentExt htmlDocument = null;
  private JToolBar htmlEditorToolBar = null;

  private HTMLDocumentPrinter htmlDocumentPrinter;

  private boolean bold = false;
  private boolean italic = false;
  private boolean underline = false;

  private boolean strikethrough = false;
  private boolean superscript = false;
  private boolean subscript = false;

  private String fontSize = "12";
  private String fontColor = "#000000";
  private String fontBackgroundColor = "#FFFFFF";
  private String font = this.getFont().getFamily();

  private boolean _editable = true;

  private boolean enabledToolBar = false;
  private boolean enabledBasic = false;
  private boolean enabledExtended = false;
  private boolean enabledAlignment = false;
  private boolean enabledFont = false;
  private boolean enabledWeb = false;
  private boolean enableDebug = false;

  private boolean viewSource = false;

  private Border borderUnselected, borderSelected;

  public JButton buttonBold, buttonItalic, buttonUnderline, buttonCustomFont,
      buttonLeftAlign, buttonRightAlign, buttonCenterAlign, buttonImage,
      buttonLink, buttonStrikeThrough, buttonSuperScript, buttonSubScript,
      buttonFontColor, buttonFontBackgroundColor, buttonDebugSource;

  public JComboBox comboFonts, comboFontSizes;

  // private CustomFontDialog _cfd;
  private ImageDialog _idlg;
  private LinkDialog _ldlg;

  public HTMLEditor(boolean editable) {
    HTMLEditorSettings.getInstance().initialize();
    _editable = editable;
    if (editable) {
      // Initialize borders for formatting buttons.
      borderUnselected = BorderFactory.createEtchedBorder(Color.white,
          new Color(142, 142, 142));
      borderSelected = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
          Color.white, Color.white, new Color(142, 142, 142), new Color(99, 99,
              99));
    }
    // Setup the HTML Editor Kit
    htmlEditorKit = new HTMLEditorKitExt();
    htmlEditorKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
    
     setStyleSheet(new InputStreamReader(Utilities.getResourceAsInputStream(
     "resources/css/style.css")));
     
    // Setup the HTML Document
    htmlDocument = new HTMLDocumentExt();
    htmlDocument.getStyleSheet().setBaseFontSize("12");
    // Setup the HTML Editor Pane
    htmlEditorPane = new HTMLEditorPane("");
    htmlEditorPane.setEditorKit(htmlEditorKit);
    htmlEditorPane.setDocument(htmlDocument);
    htmlEditorPane.setEditable(editable);
    htmlDocument.setContainer(htmlEditorPane);
    htmlEditorPane.addHyperlinkListener(new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent arg0) {
        if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
              desktop.browse(new URI(arg0.getURL().toString()));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    });
    if (editable) {
      InputMap iMap = htmlEditorPane.getInputMap();
      ActionMap aMap = htmlEditorPane.getActionMap();

      KeyStroke shiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
          KeyEvent.SHIFT_DOWN_MASK);
      KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

      iMap.put(enter, null);
      iMap.put(shiftEnter, HTMLEditorKit.insertBreakAction);
      aMap.put(HTMLEditorKit.insertBreakAction, lineBreakAction);
      htmlEditorPane.setDropTarget(new DropTarget(this, this));
      htmlEditorPane.addCaretListener(new CaretListener() {
        public void caretUpdate(CaretEvent e) {
          actionPerformed_caretUpdate(e);
        }
      });
    }
    jScrollPane = new JScrollPane();
    jScrollPane.setLayout(new ScrollPaneLayout());
    jScrollPane.getViewport().add(
        htmlEditorPane,
        ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED
            | ScrollPaneLayout.HORIZONTAL_SCROLLBAR_NEVER);
    htmlDocumentPrinter = new HTMLDocumentPrinter();
    htmlDocumentPrinter.setSize(new Dimension(850, 1100));
    htmlDocumentPrinter.setScaleWidthToFit(true);
    this.setLayout(new BorderLayout());
    this.setPreferredSize(new Dimension(200, 100));
    this.add(jScrollPane, BorderLayout.CENTER);
    this.requestFocusInWindow();
  }

  public void actionPerformed_InsertLineBreak(ActionEvent e) {
      try {
        if (e.getModifiers() == 1) {
          int offs = htmlEditorPane.getCaretPosition();
          SimpleAttributeSet attrs;
          attrs = new SimpleAttributeSet(((StyledDocument) htmlDocument)
              .getCharacterElement(offs).getAttributes());
          attrs.addAttribute(LINE_BREAK_ATTRIBUTE_NAME, Boolean.TRUE);
          htmlDocument.insertString(offs, System.getProperty("line.separator"),
              attrs);
          htmlEditorPane.setCaretPosition(offs
              + System.getProperty("line.separator").length());
          // TODO Update background color when that works.
          new StyledEditorKit.FontFamilyAction("font-family", font)
              .actionPerformed(new ActionEvent(this, 0, "font-family"));
          new StyledEditorKit.FontSizeAction("font-size",
              Integer.valueOf(fontSize)).actionPerformed(new ActionEvent(this,
              0, "font-size"));
          new StyledEditorKit.ForegroundAction("font-color",
              Utilities.decodeColor(fontColor, Color.black))
              .actionPerformed(new ActionEvent(this, 0, "font-color"));
        }
      } catch (BadLocationException ex) {
        ex.printStackTrace();
      }
  }

  private void enableToolBarEditor() {
    if (!enabledToolBar) {
      enabledToolBar = true;
      htmlEditorToolBar = new JToolBar();
      htmlEditorToolBar.setRequestFocusEnabled(false);
      htmlEditorToolBar.setFloatable(false);
      htmlEditorToolBar.setToolTipText("");
      this.add(htmlEditorToolBar, BorderLayout.NORTH);
    }
  }

  public void enableBasicEditing() {
    // Add formatting shortcuts.
    enableToolBarEditor();
    if (_editable && !enabledBasic) {
      enabledBasic = true;
      boldAction.putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK));
      italicAction.putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK));
      underlineAction.putValue(Action.ACCELERATOR_KEY,
          KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
      buttonBold = new JButton();
      buttonBold.setAction(boldAction);
      buttonBold.setBorder(borderUnselected);
      buttonBold.setMaximumSize(new Dimension(22, 22));
      buttonBold.setMinimumSize(new Dimension(22, 22));
      buttonBold.setPreferredSize(new Dimension(22, 22));
      buttonBold.setBorderPainted(false);
      buttonBold.setFocusable(false);
      buttonBold.setText("");
      buttonItalic = new JButton();
      buttonItalic.setAction(italicAction);
      buttonItalic.setBorder(borderUnselected);
      buttonItalic.setMaximumSize(new Dimension(22, 22));
      buttonItalic.setMinimumSize(new Dimension(22, 22));
      buttonItalic.setPreferredSize(new Dimension(22, 22));
      buttonItalic.setBorderPainted(false);
      buttonItalic.setFocusable(false);
      buttonItalic.setText("");
      buttonUnderline = new JButton();
      buttonUnderline.setAction(underlineAction);
      buttonUnderline.setBorder(borderUnselected);
      buttonUnderline.setMaximumSize(new Dimension(22, 22));
      buttonUnderline.setMinimumSize(new Dimension(22, 22));
      buttonUnderline.setPreferredSize(new Dimension(22, 22));
      buttonUnderline.setBorderPainted(false);
      buttonUnderline.setFocusable(false);
      buttonUnderline.setText("");
      htmlEditorToolBar.add(buttonBold, null);
      htmlEditorToolBar.add(buttonItalic, null);
      htmlEditorToolBar.add(buttonUnderline, null);
      htmlEditorToolBar.addSeparator();
      htmlEditorPane.getKeymap()
          .addActionForKeyStroke(
              KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK),
              boldAction);
      htmlEditorPane.getKeymap().addActionForKeyStroke(
          KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_MASK),
          italicAction);
      htmlEditorPane.getKeymap().addActionForKeyStroke(
          KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK),
          underlineAction);
    }
  }

  public void enableExtendedEditing() {
    enableToolBarEditor();
    if (_editable && !enabledExtended) {
      enabledExtended = true;
      buttonStrikeThrough = new JButton();
      buttonStrikeThrough.setAction(strikeThroughAction);
      buttonStrikeThrough.setMaximumSize(new Dimension(22, 22));
      buttonStrikeThrough.setMinimumSize(new Dimension(22, 22));
      buttonStrikeThrough.setPreferredSize(new Dimension(22, 22));
      buttonStrikeThrough.setBorderPainted(false);
      buttonStrikeThrough.setFocusable(false);
      buttonStrikeThrough.setText("");
      buttonSuperScript = new JButton();
      buttonSuperScript.setAction(superScriptAction);
      buttonSuperScript.setMaximumSize(new Dimension(22, 22));
      buttonSuperScript.setMinimumSize(new Dimension(22, 22));
      buttonSuperScript.setPreferredSize(new Dimension(22, 22));
      buttonSuperScript.setBorderPainted(false);
      buttonSuperScript.setFocusable(false);
      buttonSuperScript.setText("");
      buttonSubScript = new JButton();
      buttonSubScript.setAction(subScriptAction);
      buttonSubScript.setMaximumSize(new Dimension(22, 22));
      buttonSubScript.setMinimumSize(new Dimension(22, 22));
      buttonSubScript.setPreferredSize(new Dimension(22, 22));
      buttonSubScript.setBorderPainted(false);
      buttonSubScript.setFocusable(false);
      buttonSubScript.setText("");
      htmlEditorToolBar.add(buttonStrikeThrough, null);
      htmlEditorToolBar.add(buttonSubScript, null);
      htmlEditorToolBar.add(buttonSuperScript, null);
      htmlEditorToolBar.addSeparator();
    }
  }

  public void enableAlignmentEditing() {
    enableToolBarEditor();
    if (_editable && !enabledAlignment) {
      enabledAlignment = true;
      buttonLeftAlign = new JButton();
      buttonLeftAlign.setAction(leftAlignAction);
      buttonLeftAlign.setMaximumSize(new Dimension(22, 22));
      buttonLeftAlign.setMinimumSize(new Dimension(22, 22));
      buttonLeftAlign.setPreferredSize(new Dimension(22, 22));
      buttonLeftAlign.setBorderPainted(false);
      buttonLeftAlign.setFocusable(false);
      buttonLeftAlign.setText("");
      buttonRightAlign = new JButton();
      buttonRightAlign.setAction(rightAlignAction);
      buttonRightAlign.setFocusable(false);
      buttonRightAlign.setPreferredSize(new Dimension(22, 22));
      buttonRightAlign.setBorderPainted(false);
      buttonRightAlign.setMinimumSize(new Dimension(22, 22));
      buttonRightAlign.setMaximumSize(new Dimension(22, 22));
      buttonRightAlign.setText("");
      buttonCenterAlign = new JButton();
      buttonCenterAlign.setAction(centerAlignAction);
      buttonCenterAlign.setMaximumSize(new Dimension(22, 22));
      buttonCenterAlign.setMinimumSize(new Dimension(22, 22));
      buttonCenterAlign.setPreferredSize(new Dimension(22, 22));
      buttonCenterAlign.setBorderPainted(false);
      buttonCenterAlign.setFocusable(false);
      buttonCenterAlign.setText("");
      htmlEditorToolBar.add(buttonLeftAlign, null);
      htmlEditorToolBar.add(buttonCenterAlign, null);
      htmlEditorToolBar.add(buttonRightAlign, null);
      htmlEditorToolBar.addSeparator();
    }
  }

  public void enableFontCustomizationEditing() {
    enableToolBarEditor();
    if (_editable && !enabledFont) {
      enabledFont = true;
      font = HTMLEditorSettings.getInstance().getFont();
      fontSize = HTMLEditorSettings.getInstance().getFontSize();
      fontColor = HTMLEditorSettings.getInstance().getFontColor();
      fontBackgroundColor = HTMLEditorSettings.getInstance()
          .getFontBackgroundColor();
      /*
       * buttonCustomFont = new JButton();
       * buttonCustomFont.setAction(customFontAction);
       * buttonCustomFont.setBorder(borderUnselected);
       * buttonCustomFont.setMaximumSize(new Dimension(22, 22));
       * buttonCustomFont.setMinimumSize(new Dimension(22, 22));
       * buttonCustomFont.setPreferredSize(new Dimension(22, 22));
       * buttonCustomFont.setBorderPainted(false);
       * buttonCustomFont.setFocusable(false); buttonCustomFont.setText("");
       * htmlEditorToolBar.add(buttonCustomFont, null);
       * htmlEditorToolBar.addSeparator(); _cfd = new CustomFontDialog(null);
       * _cfd.setModal(true);
       */

      buttonFontColor = new JButton();
      buttonFontColor.setAction(fontColorAction);
      buttonFontColor.setBorder(borderUnselected);
      buttonFontColor.setMaximumSize(new Dimension(22, 22));
      buttonFontColor.setMinimumSize(new Dimension(22, 22));
      buttonFontColor.setPreferredSize(new Dimension(22, 22));
      buttonFontColor.setBorderPainted(false);
      buttonFontColor.setFocusable(false);
      buttonFontColor.setText("");
      // TODO Uncomment when background color works correctly.
      /*
       * buttonFontBackgroundColor = new JButton();
       * buttonFontBackgroundColor.setAction(fontBackgroundColorAction);
       * buttonFontBackgroundColor.setBorder(borderUnselected);
       * buttonFontBackgroundColor.setMaximumSize(new Dimension(22, 22));
       * buttonFontBackgroundColor.setMinimumSize(new Dimension(22, 22));
       * buttonFontBackgroundColor.setPreferredSize(new Dimension(22, 22));
       * buttonFontBackgroundColor.setBorderPainted(false);
       * buttonFontBackgroundColor.setFocusable(false);
       * buttonFontBackgroundColor.setText("");
       */
      /*
       * String envfonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
       * .getAvailableFontFamilyNames(); Vector<String> fonts = new
       * Vector<String>(); for (int i = 0; i < envfonts.length; i++)
       * fonts.add(envfonts[i]);
       */
      comboFonts = new JComboBox(new String[] { "Monospaced", "SansSerif",
          "Serif" });
      comboFonts.setAction(fontAction);
      comboFonts.setBorder(borderUnselected);
      comboFonts.setMaximumSize(new Dimension(140, 22));
      comboFonts.setMinimumSize(new Dimension(140, 22));
      comboFonts.setPreferredSize(new Dimension(140, 22));
      comboFonts.setFocusable(true);
      comboFontSizes = new JComboBox(new String[] { "12", "14", "16", "20" });
      // comboFontSizes = new JComboBox(new String[] { "8", "10", "12", "14",
      // "16", "20", "36" });
      comboFontSizes.setAction(fontSizeAction);
      comboFontSizes.setBorder(borderUnselected);
      comboFontSizes.setMaximumSize(new Dimension(60, 22));
      comboFontSizes.setMinimumSize(new Dimension(60, 22));
      comboFontSizes.setPreferredSize(new Dimension(60, 22));
      comboFontSizes.setFocusable(true);

      comboFonts.setSelectedItem(font);
      comboFontSizes.setSelectedItem(fontSize);

      htmlEditorToolBar.add(buttonFontColor);
      // TODO Uncomment when background color works correctly.
      // htmlEditorToolBar.add(buttonFontBackgroundColor);
      htmlEditorToolBar.add(comboFonts);
      htmlEditorToolBar.add(comboFontSizes);
      htmlEditorToolBar.addSeparator();

      setDefaultHTML();
    }
  }

  public void enableWebEditing() {
    enableToolBarEditor();
    if (_editable && !enabledWeb) {
      enabledWeb = true;
      buttonLink = new JButton();
      buttonLink.setAction(linkAction);
      buttonLink.setMaximumSize(new Dimension(22, 22));
      buttonLink.setMinimumSize(new Dimension(22, 22));
      buttonLink.setPreferredSize(new Dimension(22, 22));
      buttonLink.setBorderPainted(false);
      buttonLink.setFocusable(false);
      buttonLink.setText("");
      buttonImage = new JButton();
      buttonImage.setAction(imageAction);
      buttonImage.setMaximumSize(new Dimension(22, 22));
      buttonImage.setMinimumSize(new Dimension(22, 22));
      buttonImage.setPreferredSize(new Dimension(22, 22));
      buttonImage.setBorderPainted(false);
      buttonImage.setFocusable(false);
      buttonImage.setText("");
      htmlEditorToolBar.add(buttonImage, null);
      htmlEditorToolBar.add(buttonLink, null);
      htmlEditorToolBar.addSeparator();
      _idlg = new ImageDialog(null);
      _idlg.setModal(true);
      _ldlg = new LinkDialog(null);
      _ldlg.setModal(true);
    }
  }

  public void enableDebugEditing() {
    enableToolBarEditor();
    if (!enableDebug) {
      enableDebug = true;
      htmlSourcePane = new JEditorPane("text/plain", "");
      htmlSourcePane.setEditable(_editable);
      buttonDebugSource = new JButton();
      buttonDebugSource.setAction(debugAction);
      buttonDebugSource.setMaximumSize(new Dimension(22, 22));
      buttonDebugSource.setMinimumSize(new Dimension(22, 22));
      buttonDebugSource.setPreferredSize(new Dimension(22, 22));
      buttonDebugSource.setBorderPainted(false);
      buttonDebugSource.setFocusable(false);
      buttonDebugSource.setText("");
      htmlEditorToolBar.add(buttonDebugSource, null);
      htmlEditorToolBar.addSeparator();
    }
  }

  public void setStyleSheet(Reader r) {
    StyleSheet css = new StyleSheet();
    try {
      css.loadRules(r, null);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    htmlEditorKit.setStyleSheet(css);
  }

  public String getHTML() {
    if (viewSource) {
      htmlEditorPane.setText(htmlSourcePane.getText());
      return htmlEditorPane.getText();
    } else
      return htmlEditorPane.getText();
  }

  public void setHTML(String html) {
    htmlEditorPane.setText(html);
    if (html.equalsIgnoreCase(""))
      htmlEditorPane.setCaretPosition(0);
    if (viewSource) {
      htmlSourcePane.setText(htmlEditorPane.getText());
    }
  }

  public void appendHTML(String html) {
    // System.out.println(html);
    if (htmlEditorPane.getCaretPosition() == 0) {
      setHTML(html);
    } else {
      Element[] roots = htmlDocument.getRootElements();
      Element body = null;
      for (int i = 0; i < roots[0].getElementCount(); i++) {
        Element element = roots[0].getElement(i);
        if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
          body = element;
          break;
        }
      }
      try {
        htmlDocument.insertBeforeEnd(body, html);
      } catch (BadLocationException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (viewSource) {
      htmlSourcePane.setText(htmlEditorPane.getText());
    }
  }

  public String getHTMLBody() {
    String html = "";
    if (viewSource) {
      html = htmlSourcePane.getText().trim();
    } else {
      html = htmlEditorPane.getText().trim();
    }
    return html.substring(html.indexOf("<body>") + 6,
        html.lastIndexOf("</body>") - 1);
  }

  public abstract class HTMLEditorAction extends AbstractAction {
    /**
     * 
     */
    private static final long serialVersionUID = -8885249587930714677L;

    HTMLEditorAction(String name, ImageIcon icon) {
      super(name, icon);
      super.putValue(Action.SHORT_DESCRIPTION, name);
    }

    HTMLEditorAction(String name) {
      super(name);
      super.putValue(Action.SHORT_DESCRIPTION, name);
    }
  }

  public Action lineBreakAction = new AbstractAction() {
    /**
     * 
     */
    private static final long serialVersionUID = -4284220902744758817L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_InsertLineBreak(e);
    }
  };

  public Action boldAction = new HTMLEditorAction(("Bold"),
      Utilities.getImageIcon("resources/icons/edit-bold.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -4284220902744758817L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_Bold(e);
    }
  };

  public Action italicAction = new HTMLEditorAction(("Italic"),
      Utilities.getImageIcon("resources/icons/edit-italic.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -6944159286693722427L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_Italic(e);
    }
  };

  public Action underlineAction = new HTMLEditorAction(("Underline"),
      Utilities.getImageIcon("resources/icons/edit-underline.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -2924280559736403678L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_Underline(e);
    }
  };

  /*
   * public Action customFontAction = new HTMLEditorAction(("Customize Font"),
   * Utilities.getImageIcon("resources/icons/edit-custom.png")) {
   *//**
   * 
   */
  /*
   * private static final long serialVersionUID = -2924280559736403678L;
   * 
   * public void actionPerformed(ActionEvent e) {
   * actionPerformed_CustomizeFont(e); } };
   */

  public Action fontColorAction = new HTMLEditorAction(("Font Color"),
      Utilities.getImageIcon("resources/icons/edit-color.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -2924280559736403678L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_FontColor(e);
    }
  };

  public Action fontBackgroundColorAction = new HTMLEditorAction(
      ("Font Background Color"),
      Utilities.getImageIcon("resources/icons/edit-color.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -2924280559736403678L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_FontBackgroundColor(e);
    }
  };

  public Action fontAction = new HTMLEditorAction(("Font"),
      Utilities.getImageIcon("resources/icons/edit-custom.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -2924280559736403678L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_Font(e);
    }
  };

  public Action fontSizeAction = new HTMLEditorAction(("Font Size"),
      Utilities.getImageIcon("resources/icons/edit-custom.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -2924280559736403678L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_FontSize(e);
    }
  };

  public Action leftAlignAction = new HTMLEditorAction(("Align Left"),
      Utilities.getImageIcon("resources/icons/edit-alignleft.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = 3265141590391013516L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_leftAlign(e);
    }
  };

  public Action centerAlignAction = new HTMLEditorAction(("Align Center"),
      Utilities.getImageIcon("resources/icons/edit-aligncenter.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = 1777841375673299143L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_centerAlign(e);
    }
  };

  public Action rightAlignAction = new HTMLEditorAction(("Align Right"),
      Utilities.getImageIcon("resources/icons/edit-alignright.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -8550846282139665201L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_rightAlign(e);
    }
  };

  public Action strikeThroughAction = new HTMLEditorAction(("Strike"),
      Utilities.getImageIcon("resources/icons/edit-strikethrough.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -8550846282139665201L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_strike(e);
    }
  };

  public Action superScriptAction = new HTMLEditorAction(("Superscript"),
      Utilities.getImageIcon("resources/icons/edit-superscript.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -8550846282139665201L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_superscript(e);
    }
  };

  public Action subScriptAction = new HTMLEditorAction(("Subscript"),
      Utilities.getImageIcon("resources/icons/edit-subscript.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = -8550846282139665201L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_subscript(e);
    }
  };

  public Action imageAction = new HTMLEditorAction(("Insert Image"),
      Utilities.getImageIcon("resources/icons/edit-insert-image.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = 4016384089513066116L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_insertImage(e);
    }
  };

  public Action linkAction = new HTMLEditorAction(("Insert Hyperlink"),
      Utilities.getImageIcon("resources/icons/edit-insert-link.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = 665048588823990926L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_insertLink(e);
    }
  };

  public Action debugAction = new HTMLEditorAction(("View HTML Source"),
      Utilities.getImageIcon("resources/icons/edit-custom.png")) {
    /**
     * 
     */
    private static final long serialVersionUID = 665048588823990926L;

    public void actionPerformed(ActionEvent e) {
      actionPerformed_viewHTMLSource(e);
    }
  };

  public void actionPerformed_Bold(ActionEvent e) {
    if (!bold) {
      buttonBold.setBorder(borderSelected);
    } else {
      buttonBold.setBorder(borderUnselected);
    }
    bold = !bold;
    buttonBold.setBorderPainted(bold);
    new StyledEditorKit.BoldAction().actionPerformed(e);
  }

  protected void actionPerformed_subscript(ActionEvent e) {
    if (!subscript) {
      buttonSubScript.setBorder(borderSelected);
    } else {
      buttonSubScript.setBorder(borderUnselected);
    }
    subscript = !subscript;
    buttonSubScript.setBorderPainted(subscript);
    new HTMLEditorKitExt.SubScriptAction().actionPerformed(e);
  }

  protected void actionPerformed_superscript(ActionEvent e) {
    if (!superscript) {
      buttonSuperScript.setBorder(borderSelected);
    } else {
      buttonSuperScript.setBorder(borderUnselected);
    }
    superscript = !superscript;
    buttonSuperScript.setBorderPainted(superscript);
    new HTMLEditorKitExt.SuperScriptAction().actionPerformed(e);
  }

  protected void actionPerformed_strike(ActionEvent e) {
    if (!strikethrough) {
      buttonStrikeThrough.setBorder(borderSelected);
    } else {
      buttonStrikeThrough.setBorder(borderUnselected);
    }
    strikethrough = !strikethrough;
    buttonStrikeThrough.setBorderPainted(strikethrough);
    new HTMLEditorKitExt.StrikeThroughAction().actionPerformed(e);
  }

  protected void actionPerformed_viewHTMLSource(ActionEvent e) {
    if (!viewSource) {
      viewSource = true;
      buttonDebugSource.setToolTipText("View HTML Editor");
      String html = htmlEditorPane.getText();
      htmlSourcePane.setText(html);
      jScrollPane.remove(htmlEditorPane);
      jScrollPane.getViewport().add(htmlSourcePane,
          ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED);
      if (enabledBasic) {
        buttonBold.setEnabled(false);
        buttonItalic.setEnabled(false);
        buttonUnderline.setEnabled(false);
      }
      if (enabledExtended) {
        buttonSuperScript.setEnabled(false);
        buttonSubScript.setEnabled(false);
        buttonStrikeThrough.setEnabled(false);
      }
      if (enabledAlignment) {
        buttonLeftAlign.setEnabled(false);
        buttonRightAlign.setEnabled(false);
        buttonCenterAlign.setEnabled(false);
      }
      if (enabledWeb) {
        buttonLink.setEnabled(false);
        buttonImage.setEnabled(false);
      }
      if (enabledFont) {
        // buttonCustomFont.setEnabled(false);
        buttonFontColor.setEnabled(false);
        // TODO Uncommend when background color works.
        // buttonFontBackgroundColor.setEnabled(false);
        comboFonts.setEnabled(false);
        comboFontSizes.setEnabled(false);
      }
    } else {
      viewSource = false;
      buttonDebugSource.setToolTipText("View HTML Source");
      if (_editable) {
        String html = htmlSourcePane.getText();
        htmlEditorPane.setText(html);
      }
      jScrollPane.remove(htmlSourcePane);
      jScrollPane.getViewport().add(htmlEditorPane,
          ScrollPaneLayout.VERTICAL_SCROLLBAR_AS_NEEDED);
      if (enabledBasic) {
        buttonBold.setEnabled(true);
        buttonItalic.setEnabled(true);
        buttonUnderline.setEnabled(true);
      }
      if (enabledExtended) {
        buttonSuperScript.setEnabled(true);
        buttonSubScript.setEnabled(true);
        buttonStrikeThrough.setEnabled(true);
      }
      if (enabledAlignment) {
        buttonLeftAlign.setEnabled(true);
        buttonRightAlign.setEnabled(true);
        buttonCenterAlign.setEnabled(true);
      }
      if (enabledWeb) {
        buttonLink.setEnabled(true);
        buttonImage.setEnabled(true);
      }
      if (enabledFont) {
        // buttonCustomFont.setEnabled(true);
        buttonFontColor.setEnabled(true);
        // TODO Uncommend when background color works.
        // buttonFontBackgroundColor.setEnabled(true);
        comboFonts.setEnabled(true);
        comboFontSizes.setEnabled(true);
      }
    }
  }

  protected void actionPerformed_FontSize(ActionEvent e) {
    /*
     * if
     * (!fontSize.equalsIgnoreCase(comboFontSizes.getSelectedItem().toString()))
     * { fontSize = comboFontSizes.getSelectedItem().toString(); String text =
     * htmlEditorPane.getSelectedText(); if (text == null) text = ""; String
     * html = "<font size='" + fontSize + "'>" + text + "</font>"; if
     * (htmlEditorPane.getCaretPosition() == htmlDocument.getLength()) html +=
     * ""; htmlEditorPane.replaceSelection(""); System.out.println(html); try {
     * htmlEditorKit.insertHTML(htmlDocument, htmlEditorPane.getCaretPosition(),
     * html, 0, 0, HTML.getTag("font")); } catch (BadLocationException e1) {
     * e1.printStackTrace(); } catch (IOException e1) { e1.printStackTrace(); }
     * }
     */
    fontSize = comboFontSizes.getSelectedItem().toString();
    HTMLEditorSettings.getInstance().setFontSize(fontSize);
    new StyledEditorKit.FontSizeAction("font-size", Integer.valueOf(fontSize))
        .actionPerformed(e);
    focusOnText();
  }

  protected void actionPerformed_Font(ActionEvent e) {
    /*
     * if (!font.equalsIgnoreCase(comboFonts.getSelectedItem().toString())) {
     * font = comboFonts.getSelectedItem().toString(); String text =
     * htmlEditorPane.getSelectedText(); if (text == null) text = ""; String
     * html = "<font face='" + font + "'>" + text + "</font>"; if
     * (htmlEditorPane.getCaretPosition() == htmlDocument.getLength()) html +=
     * ""; htmlEditorPane.replaceSelection(""); System.out.println(html); try {
     * htmlEditorKit.insertHTML(htmlDocument, htmlEditorPane.getCaretPosition(),
     * html, 0, 0, HTML.getTag("font")); } catch (BadLocationException e1) {
     * e1.printStackTrace(); } catch (IOException e1) { e1.printStackTrace(); }
     * }
     */
    font = comboFonts.getSelectedItem().toString();
    HTMLEditorSettings.getInstance().setFont(font);
    new StyledEditorKit.FontFamilyAction("font-family", font)
        .actionPerformed(e);
    focusOnText();
  }

  protected void actionPerformed_FontBackgroundColor(ActionEvent e) {
    // TODO Fix - HTML Not Inserted Correctly
    Color c = JColorChooser.showDialog(this, ("Font Background Color"),
        Utilities.decodeColor(fontBackgroundColor));
    if (c == null)
      return;
    if (!fontBackgroundColor.equalsIgnoreCase(Utilities.encodeColor(c))) {
      fontBackgroundColor = Utilities.encodeColor(c);
      buttonFontBackgroundColor.setForeground((Utilities
          .decodeColor(fontBackgroundColor)));
      /*
       * String text = htmlEditorPane.getSelectedText(); if (text == null) text
       * = ""; String html = "<body bgcolor='" + fontColor + "'>" + text +
       * "</body>"; if (htmlEditorPane.getCaretPosition() ==
       * htmlDocument.getLength()) html += "";
       * htmlEditorPane.replaceSelection(""); System.out.println(html); try {
       * htmlEditorKit.insertHTML(htmlDocument,
       * htmlEditorPane.getCaretPosition(), html, 0, 0, HTML.getTag("body")); }
       * catch (BadLocationException e1) { e1.printStackTrace(); } catch
       * (IOException e1) { e1.printStackTrace(); }
       */
      Color color = Utilities.decodeColor(fontBackgroundColor, Color.white);
      HTMLEditorSettings.getInstance().setFontBackgroundColor(
          fontBackgroundColor);
      new HTMLEditorKitExt.BackgroundAction("bgcolor", color)
          .actionPerformed(e);
      focusOnText();
    }
  }

  protected void actionPerformed_FontColor(ActionEvent e) {
    Color c = JColorChooser.showDialog(this, ("Font Color"),
        Utilities.decodeColor(fontColor));
    if (c == null)
      return;
    if (!fontColor.equalsIgnoreCase(Utilities.encodeColor(c))) {
      fontColor = Utilities.encodeColor(c);
      buttonFontColor.setForeground((Utilities.decodeColor(fontColor)));
      /*
       * String text = htmlEditorPane.getSelectedText(); if (text == null) text
       * = ""; String html = "<font color='" + fontColor + "'>" + text +
       * "</font>"; if (htmlEditorPane.getCaretPosition() ==
       * htmlDocument.getLength()) html += "";
       * htmlEditorPane.replaceSelection(""); System.out.println(html); try {
       * htmlEditorKit.insertHTML(htmlDocument,
       * htmlEditorPane.getCaretPosition(), html, 0, 0, HTML.getTag("font")); }
       * catch (BadLocationException e1) { e1.printStackTrace(); } catch
       * (IOException e1) { e1.printStackTrace(); }
       */
      Color color = Utilities.decodeColor(fontColor, Color.black);
      HTMLEditorSettings.getInstance().setFontColor(fontColor);
      new StyledEditorKit.ForegroundAction("color", color).actionPerformed(e);
      focusOnText();
    }
  }

  public void actionPerformed_Italic(ActionEvent e) {
    if (!italic) {
      buttonItalic.setBorder(borderSelected);
    } else {
      buttonItalic.setBorder(borderUnselected);
    }
    italic = !italic;
    buttonItalic.setBorderPainted(italic);
    new StyledEditorKit.ItalicAction().actionPerformed(e);
  }

  public void actionPerformed_Underline(ActionEvent e) {
    if (!underline) {
      buttonUnderline.setBorder(borderSelected);
    } else {
      buttonUnderline.setBorder(borderUnselected);
    }
    underline = !underline;
    buttonUnderline.setBorderPainted(underline);
    new StyledEditorKit.UnderlineAction().actionPerformed(e);
  }

  public void actionPerformed_leftAlign(ActionEvent e) {
    HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction(
        "leftAlign", StyleConstants.ALIGN_LEFT);
    aa.actionPerformed(e);
  }

  public void actionPerformed_centerAlign(ActionEvent e) {
    HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction(
        "centerAlign", StyleConstants.ALIGN_CENTER);
    aa.actionPerformed(e);
  }

  public void actionPerformed_rightAlign(ActionEvent e) {
    HTMLEditorKit.AlignmentAction aa = new HTMLEditorKit.AlignmentAction(
        "rightAlign", StyleConstants.ALIGN_RIGHT);
    aa.actionPerformed(e);
  }

  public void actionPerformed_insertImage(ActionEvent e) {
    Dimension dlgSize = _idlg.getPreferredSize();
    Dimension frmSize = this.getSize();
    Point loc = this.getLocationOnScreen();
    _idlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    _idlg.setVisible(true);

    if (!_idlg.cancelled) {
      String urlString = _idlg.fileField.getText();
      try {
        String imgTag = "<img src='" + urlString + "'> ";
        /*
         * String w = _idlg.textWidthField.getText(); try { Integer.parseInt(w,
         * 10); imgTag += " width=\"" + w + "\" "; } catch (Exception ex) { //
         * ex.printStackTrace(); } String h = _idlg.textHeightField.getText();
         * try { Integer.parseInt(h, 10); imgTag += " height=\"" + h + "\" "; }
         * catch (Exception ex) { // ex.printStackTrace(); } String hs =
         * _idlg.textHSpaceField.getText(); try { Integer.parseInt(hs, 10);
         * imgTag += " hspace=\"" + hs + "\" "; } catch (Exception ex) { //
         * ex.printStackTrace(); } String vs = _idlg.textVSpaceField.getText();
         * try { Integer.parseInt(vs, 10); imgTag += " vspace=\"" + vs + "\" ";
         * } catch (Exception ex) { // ex.printStackTrace(); } String b =
         * _idlg.textBorderField.getText(); try { Integer.parseInt(b, 10);
         * imgTag += " border=\"" + b + "\" "; } catch (Exception ex) { //
         * ex.printStackTrace(); } if (_idlg.comboAlignments.getSelectedIndex()
         * > 0) imgTag += " align=\"" + _idlg.comboAlignments.getSelectedItem()
         * + "\" ";
         */
        // imgTag += ">";
        /*
         * if (_idlg.textURLField.getText().length() > 0) { imgTag =
         * "<a href=\"" + _idlg.textURLField.getText() + "\">" + imgTag +
         * "</a>"; if (htmlEditorPane.getCaretPosition() ==
         * htmlDocument.getLength()) imgTag += "&nbsp;";
         * htmlEditorKit.insertHTML(htmlDocument,
         * htmlEditorPane.getCaretPosition(), imgTag, 0, 0, HTML.Tag.A); } else
         */
        htmlEditorKit.insertHTML(htmlDocument,
            htmlEditorPane.getCaretPosition(), imgTag, 0, 0, HTML.Tag.IMG);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void actionPerformed_insertLink(ActionEvent e) {
    LinkDialog dlg = new LinkDialog(null);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = this.getSize();
    Point loc = this.getLocationOnScreen();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
        (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    if (htmlEditorPane.getSelectedText() != null)
      dlg.textDescriptionField.setText(htmlEditorPane.getSelectedText());
    dlg.setVisible(true);
    if (dlg.cancelled)
      return;
    String url = dlg.textURLField.getText();
    if (url.equalsIgnoreCase("") || url == null)
      return;
    String aTag = "";
    boolean addhttp = false;
    if (url.indexOf("www.") >= 0) {
      if (url.indexOf("http://") < 0) {
        addhttp = true;
      }
    }
    if (addhttp)
      aTag = "<a href='http://" + url + "'>";
    else
      aTag = "<a href='" + url + "'>";
    String description = dlg.textDescriptionField.getText();
    if (description.equalsIgnoreCase("") || description == null)
      description = dlg.textURLField.getText();
    aTag += "<u><font color='blue'>" + description + "</a></font></u>";
    if (htmlEditorPane.getCaretPosition() == htmlDocument.getLength())
      aTag += "&nbsp;";
    htmlEditorPane.replaceSelection("");
    try {
      htmlEditorKit.insertHTML(htmlDocument, htmlEditorPane.getCaretPosition(),
          aTag, 0, 0, HTML.Tag.A);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /*
   * public void actionPerformed_CustomizeFont(ActionEvent e) { String text =
   * ""; if (htmlEditorPane.getSelectedText() != null) text =
   * htmlEditorPane.getSelectedText(); String tag = ""; String att = ""; tag =
   * "font"; att = setFontProperties(
   * htmlDocument.getCharacterElement(htmlEditorPane.getCaretPosition()),
   * htmlEditorPane.getSelectedText()); if (att == null) return; String html =
   * "<" + tag + att + ">" + text + "</" + tag + ">";
   * System.out.println("Font HTML:" + html); if
   * (htmlEditorPane.getCaretPosition() == htmlDocument.getLength()) html += "";
   * htmlEditorPane.replaceSelection(""); try {
   * htmlEditorKit.insertHTML(htmlDocument, htmlEditorPane.getCaretPosition(),
   * html, 0, 0, HTML.getTag(tag)); if (htmlEditorPane.getCaretPosition() ==
   * htmlDocument.getLength())
   * htmlEditorPane.setCaretPosition(htmlEditorPane.getCaretPosition() - 1); }
   * catch (Exception ex) { ex.printStackTrace(); } }
   */

  /*
   * public String setFontProperties(Element el, String text) { Dimension
   * cfdSize = _cfd.getSize(); Dimension frmSize = this.getSize(); Point loc =
   * this.getLocationOnScreen(); _cfd.setLocation((frmSize.width -
   * cfdSize.width) / 2 + loc.x, (frmSize.height - cfdSize.height) / 2 + loc.y);
   * AttributeSet ea = el.getAttributes(); if
   * (ea.isDefined(StyleConstants.FontFamily))
   * _cfd.jComboFonts.setSelectedItem(ea.getAttribute(
   * StyleConstants.FontFamily).toString()); if (ea.isDefined(HTML.Tag.FONT)) {
   * String s = ea.getAttribute(HTML.Tag.FONT).toString(); String size =
   * s.substring(s.indexOf("size=") + 5, s.indexOf("size=") + 6);
   * _cfd.jComboFontSizes.setSelectedItem(size); } if
   * (ea.isDefined(StyleConstants.Foreground)) {
   * _cfd.jTextColor.setText(Utilities.encodeColor((Color) ea
   * .getAttribute(StyleConstants.Foreground)));
   * Utilities.setColorField(_cfd.jTextColor);
   * _cfd.jLabelSample.setForeground((Color) ea
   * .getAttribute(StyleConstants.Foreground)); } if
   * (ea.isDefined(StyleConstants.Background)) {
   * _cfd.jTextColor.setText(Utilities.encodeColor((Color) ea
   * .getAttribute(StyleConstants.Background)));
   * Utilities.setColorField(_cfd.jTextBackgroundColor);
   * _cfd.jLabelSample.setBackground((Color) ea
   * .getAttribute(StyleConstants.Background)); } if (text != null)
   * _cfd.jLabelSample.setText(text); _cfd.setVisible(true); if (_cfd.cancelled)
   * return null; String attrs = ""; if (_cfd.jComboFontSizes.getSelectedIndex()
   * > 0) attrs += "size=\"" + _cfd.jComboFontSizes.getSelectedItem() + "\""; if
   * (_cfd.jComboFonts.getSelectedIndex() > 0) attrs += "face=\"" +
   * _cfd.jComboFonts.getSelectedItem() + "\""; if
   * (_cfd.jTextColor.getText().length() > 0) attrs += "color=\"" +
   * _cfd.jTextColor.getText() + "\""; if
   * (_cfd.jTextBackgroundColor.getText().length() > 0) attrs += "bgcolor=\"" +
   * _cfd.jTextBackgroundColor.getText() + "\""; if (attrs.length() > 0) return
   * " " + attrs; else return null; }
   */

  void actionPerformed_caretUpdate(CaretEvent e) {
    e.getDot();
    AttributeSet charattrs = null;
    if (htmlEditorPane.getCaretPosition() > 0)
      try {
        charattrs = htmlDocument.getCharacterElement(
            htmlEditorPane.getCaretPosition() - 1).getAttributes();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    else
      charattrs = htmlDocument.getCharacterElement(
          htmlEditorPane.getCaretPosition()).getAttributes();
    if (enabledBasic) {
      if (charattrs.containsAttribute(StyleConstants.Bold, new Boolean(true))) {
        buttonBold.setBorder(borderSelected);
        bold = true;
      } else if (bold) {
        buttonBold.setBorder(borderUnselected);
        bold = false;
      }
      buttonBold.setBorderPainted(bold);
      if (charattrs.containsAttribute(StyleConstants.Italic, new Boolean(true))) {
        buttonItalic.setBorder(borderSelected);
        italic = true;
      } else if (italic) {
        buttonItalic.setBorder(borderUnselected);
        italic = false;
      }
      buttonItalic.setBorderPainted(italic);
      if (charattrs.containsAttribute(StyleConstants.Underline, new Boolean(
          true))) {
        buttonUnderline.setBorder(borderSelected);
        underline = true;
      } else if (underline) {
        buttonUnderline.setBorder(borderUnselected);
        underline = false;
      }
      buttonUnderline.setBorderPainted(underline);
    }
    if (enabledExtended) {
      if (charattrs.containsAttribute(StyleConstants.StrikeThrough,
          new Boolean(true))) {
        buttonStrikeThrough.setBorder(borderSelected);
        strikethrough = true;
      } else if (strikethrough) {
        buttonStrikeThrough.setBorder(borderUnselected);
        strikethrough = false;
      }
      buttonStrikeThrough.setBorderPainted(strikethrough);
      if (charattrs.containsAttribute(StyleConstants.Superscript, new Boolean(
          true))) {
        buttonSuperScript.setBorder(borderSelected);
        superscript = true;
      } else if (superscript) {
        buttonSuperScript.setBorder(borderUnselected);
        superscript = false;
      }
      buttonSuperScript.setBorderPainted(superscript);
      if (charattrs.containsAttribute(StyleConstants.Subscript, new Boolean(
          true))) {
        buttonSubScript.setBorder(borderSelected);
        subscript = true;
      } else if (subscript) {
        buttonSubScript.setBorder(borderUnselected);
        subscript = false;
      }
      buttonSubScript.setBorderPainted(subscript);
    }
  }

  public void focusOnText() {
    htmlEditorPane.requestFocus();
  }

  public void addInputListener(KeyListener l) {
    htmlEditorPane.addKeyListener(l);
  }

  public String getDefaultHTML() {
    return "<html><body bgcolor='" + fontBackgroundColor + "'><p><font face='"
        + font + "' size='" + fontSize + " color='" + fontColor
        + "'> </font></p></body></html>";
  }

  public void setDefaultHTML() {
    setHTML("");
    new StyledEditorKit.FontFamilyAction("font-family", font)
        .actionPerformed(new ActionEvent(this, 0, "font-family"));
    new StyledEditorKit.FontSizeAction("font-size", Integer.valueOf(fontSize))
        .actionPerformed(new ActionEvent(this, 0, "font-size"));
    new StyledEditorKit.ForegroundAction("font-color", Utilities.decodeColor(
        fontColor, Color.black)).actionPerformed(new ActionEvent(this, 0,
        "font-color"));
    focusOnText();
  }

  public void printDocument() {
    if (viewSource) {
      htmlDocumentPrinter.print((PlainDocument) htmlSourcePane.getDocument());
    } else {
      htmlDocumentPrinter.print((HTMLDocument) htmlEditorPane.getDocument());
    }
  }

  public void printSetup() {
    htmlDocumentPrinter.pageDialog();
  }

  public void onClose() {
    HTMLEditorSettings.getInstance().save();
  }

  @Override
  public void dragEnter(DropTargetDragEvent e) {
  }

  @Override
  public void dragExit(DropTargetEvent e) {
  }

  @Override
  public void dragOver(DropTargetDragEvent e) {
  }

  @Override
  public void drop(DropTargetDropEvent e) {
    Transferable t = e.getTransferable();
    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
      try {
        String urlString = (String) t.getTransferData(DataFlavor.stringFlavor);
        if (urlString.equalsIgnoreCase("") || urlString == null) {
          e.dropComplete(false);
          return;
        }
        Dimension d = null;
        //System.out.println("getting dims");
        d = getImageDimension(urlString);
        //System.out.println(d.width+","+d.height);
        String imgTag = "<img src='" + urlString + "' height='400' width='400'> ";
        if (!checkLength(imgTag.length()))
          return;
        htmlEditorKit.insertHTML(htmlDocument,
            htmlEditorPane.getCaretPosition(), imgTag, 0, 0, HTML.Tag.IMG);
        e.dropComplete(true);
      } catch (Exception ex) {
        e.dropComplete(false);
      }
    }
  }
  
  private Dimension getImageDimension(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    ImageInputStream in = ImageIO.createImageInputStream(is);
    try {
            final Iterator readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                    ImageReader reader = (ImageReader) readers.next();
                    try {
                            reader.setInput(in);
                            return new Dimension(reader.getWidth(0), reader.getHeight(0));
                    } finally {
                            reader.dispose();
                    }
            }
    } finally {
            if (in != null) in.close();
    }
    return null;
  }

  private boolean checkLength(int toAdd) {
    if (_documentMaxLength>0&&htmlEditorPane.getText().length()+toAdd>_documentMaxLength)
      return false;
    else
      return true;
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent e) {
  }
  
  private int _documentMaxLength = 0;
  public void setDocumentLength(int length) {
    _documentMaxLength = length;
    htmlDocument.setDocumentMaxLength(length);
  }
}