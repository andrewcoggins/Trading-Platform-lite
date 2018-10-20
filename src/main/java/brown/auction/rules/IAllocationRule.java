package brown.auction.rules;

import brown.auction.marketstate.IMarketState;

/**
 * An allocation rule allocates tradeables to agents.
 * @author andrew
 */
public interface IAllocationRule {

  /**
   * Sets an allocation in the market internal state.
   * @param state market state.
   */
   void setAllocation(IMarketState state);

}
