package chatty.view.htmleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ImageDialog extends JDialog implements WindowListener {
    /**
   * 
   */
  private static final long serialVersionUID = -5950013578865055937L;
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel header = new JLabel();
    JPanel areaPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc;
    JLabel labelImageFile = new JLabel();
    public JTextField fileField = new JTextField();
    JButton buttonBrowse = new JButton();
    JLabel labelAltText = new JLabel();
    public JTextField altField = new JTextField();
    JLabel labelTitleText = new JLabel();
    public JTextField titleField = new JTextField();
    JLabel labelWidth = new JLabel();
    public JTextField textWidthField = new JTextField();
    JLabel labelHeight = new JLabel();
    public JTextField textHeightField = new JTextField();
    JLabel labelHSpace = new JLabel();
    public JTextField textHSpaceField = new JTextField();
    JLabel labelVSpace = new JLabel();
    public JTextField textVSpaceField = new JTextField();
    JLabel labelBorder = new JLabel();
    public JTextField textBorderField = new JTextField();
    JLabel labelAlignment = new JLabel();
    String[] aligns = {"left", "right", "top", "middle", "bottom", "absmiddle",
        "texttop", "baseline"}; 
    public JComboBox comboAlignments = new JComboBox(aligns);
    
    JLabel labelHyperlink = new JLabel();
    public JTextField textURLField = new JTextField();
    JPanel panelButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    JButton buttonOK = new JButton();
    JButton buttonCancel = new JButton();
    public boolean cancelled = false;

    public ImageDialog(Frame frame) {
        super(frame, ("Insert an Image..."), true);
        this.setResizable(false);
        headerPanel.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
        headerPanel.setBackground(Color.WHITE);
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(("Image"));
        header.setIcon(new ImageIcon(
                ImageDialog.class.getResource(
                        "resources/icons/edit-image-header.png")));
        headerPanel.add(header);
        this.getContentPane().add(headerPanel, BorderLayout.NORTH);

        areaPanel.setBorder(new EtchedBorder(Color.white, new Color(142, 142,
                142)));
        labelImageFile.setText(("Image URL:"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelImageFile, gbc);
        fileField.setMinimumSize(new Dimension(200, 25));
        fileField.setPreferredSize(new Dimension(285, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(10, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        areaPanel.add(fileField, gbc);
        /*buttonBrowse.setMinimumSize(new Dimension(25, 25));
        buttonBrowse.setPreferredSize(new Dimension(25, 25));
        buttonBrowse.setIcon(new ImageIcon(
                ImageDialog.class.getResource(
                        "resources/icons/edit-file-open.png")));
        buttonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPerformed_browse(e);
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(buttonBrowse, gbc);*/
        
        /*labelTitleText.setText("Title Text:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelTitleText, gbc);
        titleField.setPreferredSize(new Dimension(315, 25));
        titleField.setMinimumSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        areaPanel.add(titleField, gbc);
        labelAltText.setText("ALT Text:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelAltText, gbc);
        altField.setPreferredSize(new Dimension(315, 25));
        altField.setMinimumSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        areaPanel.add(altField, gbc);
        
        labelWidth.setText("Width:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelWidth, gbc);
        textWidthField.setPreferredSize(new Dimension(30, 25));
        textWidthField.setMinimumSize(new Dimension(30, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textWidthField, gbc);
        labelHeight.setText("Height:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelHeight, gbc);
        textHeightField.setMinimumSize(new Dimension(30, 25));
        textHeightField.setPreferredSize(new Dimension(30, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textHeightField, gbc);
        labelHSpace.setText("H. Space:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelHSpace, gbc);
        textHSpaceField.setMinimumSize(new Dimension(30, 25));
        textHSpaceField.setPreferredSize(new Dimension(30, 25));
        textHSpaceField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textHSpaceField, gbc);
        labelVSpace.setText("V. Space:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelVSpace, gbc);
        textVSpaceField.setMinimumSize(new Dimension(30, 25));
        textVSpaceField.setPreferredSize(new Dimension(30, 25));
        textVSpaceField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textVSpaceField, gbc);
        labelBorder.setText("Border:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelBorder, gbc);
        textBorderField.setMinimumSize(new Dimension(30, 25));
        textBorderField.setPreferredSize(new Dimension(30, 25));
        textBorderField.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textBorderField, gbc);
        labelAlignment.setText("Alignment:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 50, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelAlignment, gbc);
        comboAlignments.setBackground(new Color(230, 230, 230));
        comboAlignments.setFont(new java.awt.Font("Dialog", 1, 10));
        comboAlignments.setPreferredSize(new Dimension(100, 25));
        comboAlignments.setSelectedIndex(0);
        comboAlignments.setEditable(false);
        comboAlignments.setEnabled(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(comboAlignments, gbc);
        labelHyperlink.setText(("Hyperlink"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(labelHyperlink, gbc);
        textURLField.setPreferredSize(new Dimension(315, 25));
        textURLField.setMinimumSize(new Dimension(200, 25));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 5, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        areaPanel.add(textURLField, gbc);*/
        this.getContentPane().add(areaPanel, BorderLayout.CENTER);

        buttonOK.setMaximumSize(new Dimension(100, 26));
        buttonOK.setMinimumSize(new Dimension(100, 26));
        buttonOK.setPreferredSize(new Dimension(100, 26));
        buttonOK.setText(("OK"));
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
        panelButtonPanel.add(buttonOK, null);
        panelButtonPanel.add(buttonCancel, null);
        this.getContentPane().add(panelButtonPanel, BorderLayout.SOUTH);
        this.pack();
        super.addWindowListener(this);
    }

    public ImageDialog() {
        this(null);
    }


    void actionPerformed_OK(ActionEvent e) {
        this.dispose();
    }

    void actionPerformed_Cancel(ActionEvent e) {
        cancelled = true;
        this.dispose();
    }

    private ImageIcon getPreviewIcon(java.io.File file) {
        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        ImageIcon thmb = null;
        if (tmpIcon.getIconHeight() > 48) {
            thmb = new ImageIcon(tmpIcon.getImage()
                    .getScaledInstance( -1, 48, Image.SCALE_DEFAULT));
        }
        else {
            thmb = tmpIcon;
        }
        if (thmb.getIconWidth() > 350) {
            return new ImageIcon(thmb.getImage()
                    .getScaledInstance(350, -1, Image.SCALE_DEFAULT));
        }
        else {
            return thmb;
        }
    }

    public void updatePreview() {
        try {
            if (new java.net.URL(fileField.getText()).getPath() != "")
                header.setIcon(getPreviewIcon(new java.io.File(
                        new java.net.URL(fileField.getText()).getPath())));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void windowOpened(WindowEvent e) {
    }
    public void windowClosing(WindowEvent e) {
        cancelled = true;
        this.dispose();
    }
    public void windowClosed(WindowEvent e) {
    }
    public void windowIconified(WindowEvent e) {
    }
    public void windowDeiconified(WindowEvent e) {
    }
    public void windowActivated(WindowEvent e) {
    }
    public void windowDeactivated(WindowEvent e) {
    }

    void actionPerformed_browse(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle(("Choose an Image File..."));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(
                new chatty.view.htmleditor.filechooser.ImageFilter());
        chooser.setAccessory(
                new chatty.view.htmleditor.filechooser.ImagePreview(
                        chooser));
        chooser.setPreferredSize(new Dimension(600, 325));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                fileField.setText(chooser.getSelectedFile().getPath());
                header.setIcon(getPreviewIcon(chooser.getSelectedFile()));
            }
            catch (Exception ex) {
                fileField.setText(chooser.getSelectedFile().getPath());
            }
            try {
                ImageIcon img = new ImageIcon(chooser.getSelectedFile()
                        .getPath());
                textWidthField.setText(new Integer(img.getIconWidth()).toString());
                textHeightField
                        .setText(new Integer(img.getIconHeight()).toString());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}