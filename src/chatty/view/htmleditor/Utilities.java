package chatty.view.htmleditor;

import java.awt.Color;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class Utilities {
  public static Hashtable<String, Color> HTMLColors;
  private static Class<Utilities> classLoader = Utilities.class;
  static {
    HTMLColors = new Hashtable<String, Color>();
    HTMLColors.put("red", Color.red);
    HTMLColors.put("green", Color.green);
    HTMLColors.put("blue", Color.blue);
    HTMLColors.put("cyan", Color.cyan);
    HTMLColors.put("magenta", Color.magenta);
    HTMLColors.put("yellow", Color.yellow);
    HTMLColors.put("black", Color.black);
    HTMLColors.put("white", Color.white);
    HTMLColors.put("gray", Color.gray);
    HTMLColors.put("darkgray", Color.darkGray);
    HTMLColors.put("lightgray", Color.lightGray);
    HTMLColors.put("orange", Color.orange);
    HTMLColors.put("pink", Color.pink);
  }

  public static Color getColorForName(String name, Color defaultColor) {
    if (HTMLColors.contains(name.toLowerCase()))
      return (Color) HTMLColors.get(name.toLowerCase());
    return defaultColor;
  }

  public static Color decodeColor(String color, Color defaultColor) {
    String colorVal = "";
    if (color.length() > 0) {
      colorVal = color.trim();
      if (colorVal.startsWith("#"))
        colorVal = colorVal.substring(1);
      try {
        colorVal = new Integer(Integer.parseInt(colorVal, 16)).toString();
        return Color.decode(colorVal.toLowerCase());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else
      return defaultColor;
    return getColorForName(color, defaultColor);
  }

  public static String encodeColor(Color color) {
    return "#" + Integer.toHexString(color.getRGB() - 0xFF000000).toUpperCase();
  }

  public static Color decodeColor(String color) {
    return decodeColor(color, Color.white);
  }
  
  public static int getIntColorFromHex(String hex) {
    return Integer.parseInt(hex,16);
  }

  public static String getHexColorFromInt(int color) {
    return Integer.toHexString(new Color(color).getRGB());
  }

  public static void setBackroundColorField(JTextField field) {
    Color c = Utilities.decodeColor(field.getText());
    field.setBackground(c);
    field.setForeground(new Color(~c.getRGB()));
  }

  public static void setColorField(JTextField field) {
    Color c = Utilities.decodeColor(field.getText(), Color.black);
    field.setForeground(c);
  }

  public static ImageIcon getImageIcon(String path) {
    return new ImageIcon(classLoader.getResource(path));
  }
  
  public static InputStream getResourceAsInputStream(String path) {
    return classLoader.getResourceAsStream(path);
  }
  
  public static String getHTMLBetweenTags(String tag, String html) {
    Pattern htmlFinder = Pattern.compile( ".*\\<"+tag+">([a-zA-Z0-9 ]*)\\</"+tag+">.*" );
    Matcher m = htmlFinder.matcher(html);
    if (m.matches())
      return m.group(1);
    else
      return "";
  }
}