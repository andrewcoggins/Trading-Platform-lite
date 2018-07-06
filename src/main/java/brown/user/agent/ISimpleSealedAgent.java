package brown.user.agent;

import brown.mechanism.channel.library.SealedBidChannel;

public interface ISimpleSealedAgent {

  /**
   * Provides agent response to sealed-bid auction
   * @param channel - simple agent channel
   */
  public void onSimpleSealed(SealedBidChannel channel);
  
}
