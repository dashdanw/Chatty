package chatty.view.htmleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class CustomFontDialog extends JDialog {
  /**
   * 
   */
  private static final long serialVersionUID = -2838989889185358578L;

  private JPanel jPanelContents = new JPanel(new GridBagLayout());
  private GridBagConstraints gbc;
  private JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10,
      10));
  private JButton jButtonCancel = new JButton();
  private JButton jButtonOK = new JButton();
  private JPanel jPanelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
  public JLabel jLabelHeader = new JLabel();
  public boolean cancelled = false;
  public JComboBox jComboFontSizes = new JComboBox(new String[] {
      "2", "4", "6", "8", "12" });
  public JComboBox jComboFonts;
  public JLabel jLabelSample = new JLabel();
  private JPanel jPanelSample = new JPanel(new FlowLayout(FlowLayout.CENTER));
  public JTextField jTextColor = new JTextField();
  private JLabel jLabelColor = new JLabel();
  private JButton jButtonColor = new JButton();
  public JTextField jTextBackgroundColor = new JTextField();
  private JLabel jLabelBackgroundColor = new JLabel();
  private JButton jButtonBackgroundColor = new JButton();
  public CustomFontDialog(Frame frame) {
    super(frame, ("Text Properties"), true);
    this.setResizable(false);
    GraphicsEnvironment gEnv = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    String envfonts[] = gEnv.getAvailableFontFamilyNames();
    Vector<String> fonts = new Vector<String>();
    for (int i = 0; i < envfonts.length; i++)
      fonts.add(envfonts[i]);
    jComboFonts = new JComboBox(fonts);
    jPanelHeader.setBackground(Color.WHITE);
    jLabelHeader.setFont(new java.awt.Font("Dialog", 0, 20));
    jLabelHeader.setForeground(new Color(0, 0, 124));
    jLabelHeader.setText(("Customize Text"));
    jLabelHeader.setIcon(Utilities.getImageIcon("resources/icons/edit-custom-header.png"));
    jPanelHeader.add(jLabelHeader);
    this.getContentPane().add(jPanelHeader, BorderLayout.NORTH);
    jPanelContents.setBorder(BorderFactory.createEtchedBorder(Color.white,
        new Color(142, 142, 142)));
    jComboFonts.setMaximumRowCount(9);
    jComboFonts.setBorder(new TitledBorder(BorderFactory.createEmptyBorder(),
        ("Font Family:")));
    jComboFonts.setPreferredSize(new Dimension(200, 50));
    jComboFonts.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_Font(e);
      }
    });
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(10, 10, 5, 5);
    jPanelContents.add(jComboFonts, gbc);
    jComboFontSizes.setEditable(true);
    jComboFontSizes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_Font(e);
      }
    });
    jComboFontSizes.setBorder(new TitledBorder(BorderFactory
        .createEmptyBorder(), ("Font Size:")));
    jComboFontSizes.setPreferredSize(new Dimension(80, 50));
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(10, 5, 5, 10);
    jPanelContents.add(jComboFontSizes, gbc);
    
    jLabelColor.setText(("Font Color:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(5, 20, 5, 5);
    jPanelContents.add(jLabelColor, gbc);
    jTextColor.setPreferredSize(new Dimension(60, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 5, 5, 5);
    jPanelContents.add(jTextColor, gbc);
    jButtonColor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_Color(e);
      }
    });
    jButtonColor.setIcon(Utilities.getImageIcon("resources/icons/edit-color.png"));
    jButtonColor.setPreferredSize(new Dimension(25, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(5, 5, 5, 0);
    jPanelContents.add(jButtonColor, gbc);
    
    jLabelBackgroundColor.setText(("Background Color:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(5, 20, 5, 5);
    jPanelContents.add(jLabelBackgroundColor, gbc);
    jTextBackgroundColor.setPreferredSize(new Dimension(60, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 5, 5, 5);
    jPanelContents.add(jTextBackgroundColor, gbc);
    jButtonBackgroundColor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_BackgroundColor(e);
      }
    });
    jButtonBackgroundColor.setIcon(Utilities.getImageIcon("resources/icons/edit-color.png"));
    jButtonBackgroundColor.setPreferredSize(new Dimension(25, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(5, 5, 5, 0);
    jPanelContents.add(jButtonBackgroundColor, gbc);
    
    jPanelSample.setBackground(Color.white);
    jPanelSample.setBorder(BorderFactory.createTitledBorder(("Sample")));
    jLabelSample.setText(("AaBbCcDd"));
    jLabelSample.setHorizontalAlignment(SwingConstants.CENTER);
    jLabelSample.setVerticalAlignment(SwingConstants.CENTER);
    jLabelSample.setPreferredSize(new Dimension(250, 50));
    jLabelSample.setOpaque(true);
    jPanelSample.add(jLabelSample);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(5, 10, 10, 10);
    jPanelContents.add(jPanelSample, gbc);
    this.getContentPane().add(jPanelContents, BorderLayout.CENTER);

    jButtonCancel.setMaximumSize(new Dimension(100, 26));
    jButtonCancel.setMinimumSize(new Dimension(100, 26));
    jButtonCancel.setPreferredSize(new Dimension(100, 26));
    jButtonCancel.setText(("Cancel"));
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_Cancel(e);
      }
    });
    jButtonOK.setMaximumSize(new Dimension(100, 26));
    jButtonOK.setMinimumSize(new Dimension(100, 26));
    jButtonOK.setPreferredSize(new Dimension(100, 26));
    jButtonOK.setText(("OK"));
    jButtonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_OK(e);
      }
    });
    this.getRootPane().setDefaultButton(jButtonOK);
    buttonsPanel.add(jButtonOK, null);
    buttonsPanel.add(jButtonCancel, null);
    this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    this.pack();
  }

  private void actionPerformed_OK(ActionEvent e) {
    this.dispose();
  }

  private void actionPerformed_Cancel(ActionEvent e) {
    cancelled = true;
    this.dispose();
  }

  private void actionPerformed_Font(ActionEvent e) {
    int[] sizes = { 8, 10, 13, 16, 18, 24, 32 };
    int size = 16;
    String face;
    Font font = jLabelSample.getFont();
    if (jComboFontSizes.getSelectedIndex() > 0)
      size = sizes[jComboFontSizes.getSelectedIndex() - 1];
    if (jComboFonts.getSelectedIndex() > 0)
      face = (String) jComboFonts.getSelectedItem();
    else
      face = font.getName();
    jLabelSample.setFont(new Font(face, Font.PLAIN, size));
  }

  private void actionPerformed_Color(ActionEvent e) {
    UIManager.put("ColorChooser.swatchesNameText", ("Swatches"));
    UIManager.put("ColorChooser.hsbNameText", ("HSB"));
    UIManager.put("ColorChooser.rgbNameText", ("RGB"));
    UIManager
        .put("ColorChooser.swatchesRecentText", ("Recent:"));
    UIManager.put("ColorChooser.previewText", ("Preview"));
    UIManager.put("ColorChooser.sampleText", ("Sample Text")
        + " " + ("Sample Text"));
    UIManager.put("ColorChooser.okText", ("OK"));
    UIManager.put("ColorChooser.cancelText", ("Cancel"));
    UIManager.put("ColorChooser.resetText", ("Reset"));
    UIManager.put("ColorChooser.hsbHueText", ("H"));
    UIManager.put("ColorChooser.hsbSaturationText", ("S"));
    UIManager.put("ColorChooser.hsbBrightnessText", ("B"));
    UIManager.put("ColorChooser.hsbRedText", ("R"));
    UIManager.put("ColorChooser.hsbGreenText", ("G"));
    UIManager.put("ColorChooser.hsbBlueText", ("B2"));
    UIManager.put("ColorChooser.rgbRedText", ("Red"));
    UIManager.put("ColorChooser.rgbGreenText", ("Green"));
    UIManager.put("ColorChooser.rgbBlueText", ("Blue"));
    Color c = JColorChooser.showDialog(this, ("Font Color"),
        Utilities.decodeColor(jTextColor.getText()));
    if (c == null)
      return;
    jTextColor.setText(Utilities.encodeColor(c));
    Utilities.setColorField(jTextColor);
    jLabelSample.setForeground(c);
  }
  
  private void actionPerformed_BackgroundColor(ActionEvent e) {
    UIManager.put("ColorChooser.swatchesNameText", ("Swatches"));
    UIManager.put("ColorChooser.hsbNameText", ("HSB"));
    UIManager.put("ColorChooser.rgbNameText", ("RGB"));
    UIManager
        .put("ColorChooser.swatchesRecentText", ("Recent:"));
    UIManager.put("ColorChooser.previewText", ("Preview"));
    UIManager.put("ColorChooser.sampleText", ("Sample Text")
        + " " + ("Sample Text"));
    UIManager.put("ColorChooser.okText", ("OK"));
    UIManager.put("ColorChooser.cancelText", ("Cancel"));
    UIManager.put("ColorChooser.resetText", ("Reset"));
    UIManager.put("ColorChooser.hsbHueText", ("H"));
    UIManager.put("ColorChooser.hsbSaturationText", ("S"));
    UIManager.put("ColorChooser.hsbBrightnessText", ("B"));
    UIManager.put("ColorChooser.hsbRedText", ("R"));
    UIManager.put("ColorChooser.hsbGreenText", ("G"));
    UIManager.put("ColorChooser.hsbBlueText", ("B2"));
    UIManager.put("ColorChooser.rgbRedText", ("Red"));
    UIManager.put("ColorChooser.rgbGreenText", ("Green"));
    UIManager.put("ColorChooser.rgbBlueText", ("Blue"));
    Color c = JColorChooser.showDialog(this, ("Background Color"),
        Utilities.decodeColor(jTextBackgroundColor.getText()));
    if (c == null)
      return;
    jTextBackgroundColor.setText(Utilities.encodeColor(c));
    Utilities.setColorField(jTextBackgroundColor);
    jLabelSample.setBackground(c);
  }
}
