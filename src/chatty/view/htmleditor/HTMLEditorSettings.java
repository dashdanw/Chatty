package chatty.view.htmleditor;

import java.util.prefs.Preferences;
import javax.swing.UIManager;

public class HTMLEditorSettings {

  private static class SettingsHolder {
    public static final HTMLEditorSettings settings = new HTMLEditorSettings();
  }
  public static HTMLEditorSettings getInstance() {
    return SettingsHolder.settings;
  }
  
  // preference keys
  private static final String FONT = "font";
  private static final String FONT_SIZE = "font_size";
  private static final String FONT_COLOR = "font_color";
  private static final String FONT_BACKGROUND_COLOR = "font_background_color";
  
  // defaults
  private static final String DEFAULT_FONT = UIManager.getDefaults().getFont("TextArea.font").getFamily();
  private static final String DEFAULT_FONT_SIZE = "12";
  private static final String DEFAULT_FONT_COLOR = "#000000";
  private static final String DEFAULT_FONT_BACKGROUND_COLOR = "#FFFFFF";
  
  private String _font;
  private String _font_size;
  private String _font_color;
  private String _font_background_color;
  private final String _moduleName;
  private Preferences _prefs;
  private HTMLEditorSettings() {
    _prefs = Preferences.userNodeForPackage(HTMLEditorSettings.class);
    _font = DEFAULT_FONT;
    _font_size = DEFAULT_FONT_SIZE;
    _font_color = DEFAULT_FONT_COLOR;
    _font_background_color = DEFAULT_FONT_BACKGROUND_COLOR;
    _moduleName = "HTMLEditor";
  }
  
  public void initialize() {
    _font = _prefs.get(FONT, DEFAULT_FONT);
    _font_size = _prefs.get(FONT_SIZE, DEFAULT_FONT_SIZE);
    _font_color = _prefs.get(FONT_COLOR, DEFAULT_FONT_COLOR);
    _font_background_color = _prefs.get(FONT_BACKGROUND_COLOR, DEFAULT_FONT_BACKGROUND_COLOR);
  }
  
  public void save() {
    _prefs.put(FONT, _font);
    _prefs.put(FONT_SIZE, _font_size);
    _prefs.put(FONT_COLOR, _font_color);
    _prefs.put(FONT_BACKGROUND_COLOR, _font_background_color);
  }
  
  public String getFont() {
    return _font;
  }
  
  public String getFontSize() {
    return _font_size;
  }
  
  public String getFontColor() {
    return _font_color;
  }
  
  public String getFontBackgroundColor() {
    return _font_background_color;
  }
  
  public void setFont(String font) {
    _font = font;
  }
  
  public void setFontSize(String fontSize) {
    _font_size = fontSize;
  }
  
  public void setFontColor(String fontColor) {
    _font_color = fontColor;
  }
  
  public void setFontBackgroundColor(String fontBackgroundColor) {
    _font_background_color = fontBackgroundColor;
  }

  public String getModuleName() {
    return _moduleName;
  }
  
}