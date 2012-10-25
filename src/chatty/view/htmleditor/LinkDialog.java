package chatty.view.htmleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LinkDialog extends JDialog {
  /**
   * 
   */
  private static final long serialVersionUID = 119799801297349368L;
  
  JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
  public JLabel header = new JLabel();
  JPanel areaPanel = new JPanel(new GridBagLayout());
  GridBagConstraints gbc;
  JLabel labelURL = new JLabel();
  public JTextField textURLField = new JTextField();
  JLabel labelDescription = new JLabel();
  JTextField textDescriptionField = new JTextField();
  JCheckBox checkButtonLaunch = new JCheckBox();
  JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
  JButton buttonOK = new JButton();
  JButton buttonCancel = new JButton();
  String[] aligns = { "", ("left"), ("center"), ("right") };
  public boolean cancelled = false;

  public LinkDialog(Frame frame) {
    super(frame, ("Insert a Hyperlink..."), true);
    this.setResizable(false);
    header.setFont(new java.awt.Font("Dialog", 0, 20));
    header.setForeground(new Color(0, 0, 124));
    header.setText(("Insert Hyperlink"));
    header.setIcon(new ImageIcon(ImageDialog.class
        .getResource("resources/icons/edit-insert-link.png")));
    topPanel.setBackground(Color.WHITE);
    topPanel.add(header);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);

    labelURL.setText(("URL:"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 10, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;
    areaPanel.add(labelURL, gbc);
    textURLField.setPreferredSize(new Dimension(300, 25));
    textURLField.setText("http://");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(10, 5, 5, 10);
    gbc.anchor = GridBagConstraints.WEST;
    areaPanel.add(textURLField, gbc);
    labelDescription.setText(("Description"));
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.insets = new Insets(5, 10, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;
    areaPanel.add(labelDescription, gbc);
    textDescriptionField.setPreferredSize(new Dimension(300, 25));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.insets = new Insets(5, 5, 5, 10);
    gbc.anchor = GridBagConstraints.WEST;
    areaPanel.add(textDescriptionField, gbc);
    checkButtonLaunch.setText(("Open in a New Window:"));
    
    areaPanel.setBorder(BorderFactory.createEtchedBorder(Color.white,
        new Color(142, 142, 142)));
    this.getContentPane().add(areaPanel, BorderLayout.CENTER);

    buttonOK.setMaximumSize(new Dimension(100, 26));
    buttonOK.setMinimumSize(new Dimension(100, 26));
    buttonOK.setPreferredSize(new Dimension(100, 26));
    buttonOK.setText("OK");
    buttonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_OK(e);
      }
    });
    this.getRootPane().setDefaultButton(buttonOK);
    buttonCancel.setMaximumSize(new Dimension(100, 26));
    buttonCancel.setMinimumSize(new Dimension(100, 26));
    buttonCancel.setPreferredSize(new Dimension(100, 26));
    buttonCancel.setText(("Cancel"));
    buttonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actionPerformed_Cancel(e);
      }
    });
    buttonsPanel.add(buttonOK);
    buttonsPanel.add(buttonCancel);
    this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    this.pack();
  }

  public LinkDialog() {
    this(null);
  }

  void actionPerformed_OK(ActionEvent e) {
    this.dispose();
  }

  void actionPerformed_Cancel(ActionEvent e) {
    cancelled = true;
    this.dispose();
  }
}