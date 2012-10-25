package chatty.controller;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import chatty.model.IncomingMessage;
import chatty.model.OutgoingMessage;

public class TestByteMaster {
  ByteMaster bm;
  @Before
  public void setUp() throws Exception {
    bm = new ByteMaster();
  }

  @Test
  public void testSignon() {
    String uname = "foo";
    Date now = new Date();
    byte[] bs = bm.encodeSignOnMessage(uname, now);
    assertTrue(bs[0] == ByteMaster.SIGNON_MESSAGE);
    IncomingMessage im = bm.decode(bs);
    assertTrue(im != null);
    assertTrue(im.getNotice() == ByteMaster.SIGNON_MESSAGE);
    assertTrue(im.getSender().equals(uname));
  }
  @Test
  public void testSignoff() {
    String uname = "foo";
    Date now = new Date();
    byte[] bs = bm.encodeSignOffMessage(uname, now);
    assertTrue(bs[0] == ByteMaster.SIGNOFF_MESSAGE);
    IncomingMessage im = bm.decode(bs);
    assertTrue(im != null);
    assertTrue(im.getNotice() == ByteMaster.SIGNOFF_MESSAGE);
    assertTrue(im.getSender().equals(uname));
  }
  @Test
  public void testChangeName() {
    String uname = "foo";
    String uname2 = "bar";
    byte[] bs = bm.encodeNameChange(uname, uname2);
    assertTrue(bs[0] == ByteMaster.NAME_CHANGE);
    IncomingMessage im = bm.decode(bs);
    assertTrue(im != null);
    assertTrue(im.getNotice() == ByteMaster.NAME_CHANGE);
    assertTrue(im.getSender().equals(uname2));
  }
  @Test
  public void testChat() {
    String uname = "foo";
    String txt = "asdadsfasdf asdf asd fsdfa dfs   adsf   adfs  f";
    Date now = new Date();
    OutgoingMessage om = new OutgoingMessage(uname, txt, now);
    byte[] bs = bm.encodeOutgoingMessage(om);
    assertTrue(bs[0] == ByteMaster.CHAT_MESSAGE);
    IncomingMessage im = bm.decode(bs);
    assertFalse(im.isNotice());
    assertTrue(im.getSender().equals(uname));
    assertTrue(im.getText().equals(txt));
  }
}
