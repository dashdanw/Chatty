package chatty.model;

import static org.junit.Assert.*;

import java.util.Date;
//import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.*;

import chatty.controller.Settings;


public class TestMessage {
  private Message om1, om2;
  private Message im1, im2, im3, im4;
  @Before public void setUp() {
    om1 = new OutgoingMessage("frobnicate lexical asteroid quux");
    om2 = new OutgoingMessage("frobnicate lexical asteroid quux");
//    im4 = new IncomingMessage(Settings.getInstance().getCurrentUser(), "frobnicate lexical asteroid quux", UUID.randomUUID(), new Date());
//    im1 = new IncomingMessage(Settings.getInstance().getCurrentUser(), "yo mang", UUID.randomUUID(), new Date());
//    im2 = new IncomingMessage(Settings.getInstance().getCurrentUser(), "yo mang", uu, new Date());
//    im3 = new IncomingMessage(Settings.getInstance().getCurrentUser(), "no mang", uu, new Date());
  }

  @Test public void testUser() {
    assertTrue(om1.getSender() == Settings.getInstance().getCurrentUsername());
  }

  @Test public void testToString() {
    String s = om1.toString();
    String s2 = im1.toString();
    assertTrue(Pattern.matches("^\\[.*?\\]\\s+<Me>: frobnicate lexical asteroid quux$", s));
    assertTrue(Pattern.matches("^\\[.*?\\]\\s+<Me>: yo mang$", s2));
  }
  @Test public void testTimestamp() {
    Date d = new Date();
    Date t = om1.getTimestamp();
    Date t2 = im1.getTimestamp();
    assertTrue(t.before(d) || t.equals(d));
    assertTrue(t2.before(d) || t2.equals(d));
  }
  @Test public void testEquals() {
    assertFalse(om1.equals(new OutgoingMessage("fooooo")));
    assertTrue(om1.equals(om1));
    assertFalse(om1.equals(om2));
    assertFalse(om1.equals(im1));
    assertFalse(im1.equals(om1));
    assertFalse(im1.equals(im2));
    assertTrue(im2.equals(im2));
    assertTrue(im2.equals(im3));
    assertFalse(om1.equals(im4));
  }
  
  
}