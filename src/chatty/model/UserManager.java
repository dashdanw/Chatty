package chatty.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chatty.controller.ByteMaster;
import chatty.controller.Settings;


public class UserManager extends Observable {
  private static final int TIMEOUT_MS = 5000;
  
  // user's are just stored as their strings.  information about them is stored
  // in these maps. Getting a consistent view of the system is too difficult due to its
  // distributed nature so our best bet is to not rely on an accurate userID, etc.  all
  // we can guarantee about a given user is that they're using a user name.  We need to
  // associate data with them, however storing this data in objects would raise issues
  // regarding the identity of the objects and the users.  Two objects might be distinct,
  // despite representing the same user. Because of this, we'll store this data 
  // relationally, allowing individual portions of the system to only have access to the
  // data which they need.
  
  private Set<String> _users;
  private Map<String,String> _aliases;
  private Map<String,Date> _lastSeen;

//  private ChattyModel _owner;
  public UserManager(ChattyModel chattyModel) {
    super();
//    _owner = chattyModel;
    _users = new HashSet<String>();
    _aliases = new HashMap<String,String>();
    _lastSeen = new HashMap<String,Date>();

  }

  private void addUser(String user) {
    String uname = user;
    boolean found_unique = !_users.contains(user);
    int counter = 0;
    while (!found_unique) {
      uname = user + " (" + counter++ + ")";
      if (!_users.contains(uname)) {
        found_unique = true;
      }
    }
    
    _users.add(uname);
    
    if (user != uname) {
      _aliases.put(user, uname);
    }
    
    _lastSeen.put(uname, new Date());
    
    setChanged();
  }

  private void removeUser(String user) {
    _users.remove(user);
    _aliases.remove(user);
    _lastSeen.remove(user);
    setChanged();
  }
  
  public String getOrCreateUser(String name) {
    if (_aliases.containsKey(name)) {
      return _aliases.get(name);
    } else {
      return name;
    }
  }
  
  public void messageReceived(Message m) {
    if (m instanceof IncomingMessage) {
      IncomingMessage im = ((IncomingMessage)m);
      if (im.isNotice()) {
        switch (im.getNotice()) { // TODO: Refactor incoming message. what a mess.
        case ByteMaster.SIGNON_MESSAGE:
          addUser(im.getSender());
          setChanged();
          break;
        case ByteMaster.SIGNOFF_MESSAGE:
          removeUser(im.getSender());
          setChanged();
          break;
        case ByteMaster.NAME_CHANGE: // UGGGGGH. this is so crufty. :/
          Pattern p = Pattern.compile("^(.*?) has changed their name to (.*?) #HOST\\{ (.*?) \\}$");
          Pattern p2 = Pattern.compile("^(.*?) #HOST\\{ (.*?) \\}$");
          String txt = im.getText();
          Matcher mm = p2.matcher(txt);
          if (mm.matches()) {
            im.setMessage(mm.group(1));
          }
          Matcher mat = p.matcher(txt);
          if (!mat.matches()) {
            System.out.format("Could not parse name change in usermanager. MESSAGE: %s\n", im.toString());
            break;
          }
          String g3 = mat.group(3);
          removeUser(mat.group(1)+"@"+g3);
          addUser(mat.group(2)+"@"+g3);
          setChanged();
          break;
        case ByteMaster.PING:
          if (_users.contains(im.getSender())) {
            _lastSeen.put(im.getSender(), new Date());
          } else {
            addUser(im.getSender());
            setChanged();
          }
          break;
        default:
          System.out.format("UNKNOWN NOTICE TYPE! NOTICE: %s,  MESSAGE: %s\n", im.getNotice(), im.toString());
        }
      } else {
        String uname = m.getSender();
        if (uname != Settings.getInstance().getCurrentUsername()) {
          if (!_users.contains(uname)) {
            addUser(uname);
          } else {
            _lastSeen.put(uname, new Date());
          }
        }
      }
    }
    notifyObservers();
  }
  private final static Pattern USER_PATTERN = Pattern.compile("^(.*?)@(.*?)$");
  public Collection<String> getOnlineUsers() {
    Collection<String> c = new ArrayList<String>();
    for (String u : _users) {
      Matcher m = USER_PATTERN.matcher(u);
      if (m.matches()) {
        c.add(m.group(1)+" ("+m.group(2)+")");
      } else c.add(u);
    }
    return c;
  }

  public void userSignOn(String user) {
    addUser(user);
    notifyObservers();
  }

  public void userSignOff(String user) {
    removeUser(user);
    notifyObservers();
  }

  public void userChangeName(String oldname, String newname) {
    if (newname != oldname) {
      removeUser(oldname);
      addUser(newname);
      setChanged();
    }
    notifyObservers();
  }
  
  public void pruneUsers() {
    List<String> l = new ArrayList<String>();
    String cname = Settings.getInstance().getCurrentUsername();
    for (String uname : _lastSeen.keySet()) {
      if (uname.equals(cname)) continue;
      Date dt = _lastSeen.get(uname);
      if (hasTimedOut(dt)) {
        l.add(uname);
      }
    }
    // we don't do this above because modifying lastseen while 
    // we're iterating over it could have unexpected results
    for (String uname : l) {
      removeUser(uname);
    }
    notifyObservers();
  }

  private boolean hasTimedOut(Date ts) {
    Calendar c = Calendar.getInstance();
    c.setTime(ts);
    c.clear(Calendar.YEAR);
    c.clear(Calendar.MONTH);
    c.clear(Calendar.DATE);
    Calendar now = Calendar.getInstance();
    now.clear(Calendar.YEAR);
    now.clear(Calendar.MONTH);
    now.clear(Calendar.DATE);
    //System.out.format("now: %s\ncee: %s, \ndelta: %s\n\n", now, c, );
    return ((now.getTimeInMillis()-c.getTimeInMillis()) > TIMEOUT_MS);
  }
}
