package chatty.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Settings {
  // the implementation below is not thread safe, which we need here.
  // for why the implementation further below works (and why just synchronizing)
  // this method doesn't, see http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html
  /*
  private static Settings instance;
  
  public static Settings getInstance() {
    if (instance == null) {
      instance = new Settings();
    }
    return instance;
  }*/
  private static class SettingsHolder {
    public static final Settings instance = new Settings();
  }
  public static Settings getInstance() {
    return SettingsHolder.instance;
  }
  private static final String ILLEGAL_CHARS_REGEX = "[^a-zA-Z0-9-_!?+*]";  
  // settings itself
  
  // preference keys
  private static final String USER_NAME = "user_name";
  private static final String LOOK_AND_FEEL = "look_and_feel";
  
  // defaults
  private static final String DEFAULT_USER_NAME = "chatty_user";
  private static final String DEFAULT_LOOK_AND_FEEL = UIManager.getCrossPlatformLookAndFeelClassName();
  
  private String _current;
  private String _lookAndFeel;
  private final String _appName;
  private Preferences _prefs;
  private Settings() {
    _prefs = Preferences.userNodeForPackage(Settings.class);
    setUserName(DEFAULT_USER_NAME);
    _lookAndFeel = DEFAULT_LOOK_AND_FEEL;
    _appName = "Chatty!";
  }
  
  public void initialize() {
    String uname = _prefs.get(USER_NAME, DEFAULT_USER_NAME);
    if (uname == DEFAULT_USER_NAME) {
      uname = promptForUserName();
      if (uname == null || uname == "") {
        uname = DEFAULT_USER_NAME;
      }
    }
    String laf = _prefs.get(LOOK_AND_FEEL, DEFAULT_LOOK_AND_FEEL);
    setUserName(uname);
    _lookAndFeel = laf;
      try {
        UIManager.setLookAndFeel(_lookAndFeel);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (UnsupportedLookAndFeelException e) {
        e.printStackTrace();
      }
  }
  
  private String promptForUserName() {
    return JOptionPane.showInputDialog("Welcome to Chatty! Please input a user name:");
  }
  public void changeUserName() {
    setUserName(promptForUserName());
  }
  public void save() {
    String uname = _current;
    if (uname != DEFAULT_USER_NAME) {
      _prefs.put(USER_NAME, _current);
    }
    String laf = _lookAndFeel;;
    if (laf != DEFAULT_LOOK_AND_FEEL) {
      _prefs.put(LOOK_AND_FEEL, _lookAndFeel);
    }
  }
  private String fixup(String name) {
    return name.replaceAll(ILLEGAL_CHARS_REGEX, "");
  }
  public void setUserName(String name) {
    if (name == null){
      if (_current == null || _current.length() == 0) {
        _current = DEFAULT_USER_NAME; 
      }
    } else {
      String probablyOkay = fixup(name);
      if (probablyOkay.length() == 0) {
        _current = DEFAULT_USER_NAME;
      } else {
        _current = probablyOkay;
      }
    }    
  }
  
  public void setLookAndFeel(String lookAndFeel) {
    _lookAndFeel = lookAndFeel;
  }
  
  public String getLookAndFeel() {
    return _lookAndFeel;
  }
  private String getHostString() {
    try {
      return InetAddress.getLocalHost().toString();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return "UNKNOWN_HOST";
    }
  }
  
  public String getCurrentUsername() {
    return _current + "@" + getHostString();
  }
  public String getApplicationName() {
    return _appName;
  }
  
}