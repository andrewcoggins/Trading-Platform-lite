package brown.system.client;

import brown.communication.messages.IErrorMessage;
import brown.communication.messages.IRegistrationMessage;

/**
 * Agents are responsible for receiving messages from the server,
 * constructing bids, and sending them back to the server.
 * @author andrew
 *
 */
public interface IClient { 

  /**
   * Gets called when the server sends a registration method back to the agent (2nd part of handshake)
   * @param registration - includes the agent's new ID
   */
  void onRegistration(IRegistrationMessage registration);
 
  /**
   * Whenever a message is rejected, a rejection message is sent
   * @param message - includes a string about rejection
   */
  void onErrorMessage(IErrorMessage message);

}

  
 