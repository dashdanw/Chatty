package chatty.model;

import java.util.*;

public interface Message {
  /**
   * getSender: Gets the User who sent this message.  (In outgoing messages, this is the current user.)
   * 
   * @return the user who sent this message
   */
  public String getSender();
  /**
   * getMessageID: gets the universally unique identifier for this message.
   * 
   * @return the ID representing the message.
   */
  public UUID getMessageID();
  /**
   * getTimestamp: gets the date this message was sent.
   * 
   * @return The date representing when this message was sent
   */
  public Date getTimestamp();
  /**
   * getText: gets the text of this message.
   * 
   * @return the text of this message.
   */
  public String getText();
}
