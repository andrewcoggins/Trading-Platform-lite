package brown.auction.preset;

import brown.auction.rules.library.LemonadeActivity;
import brown.auction.rules.library.LemonadeAnonymousPolicy;
import brown.auction.rules.library.LemonadeGroupedPayment;
import brown.auction.rules.library.LemonadeQuery;
import brown.auction.rules.library.NoAllocation;
import brown.auction.rules.library.NoRecordKeeping;
import brown.auction.rules.library.OneGrouping;
import brown.auction.rules.library.OneShotTermination;

/**
 * Lemonade game where bids are anonymous.
 * @author acoggins
 *
 */
public class LemonadeAnonRules extends AbsMarketRules {
  private int numSlots;
  
  public LemonadeAnonRules(int numSlots) {
    super(new NoAllocation(),
        new LemonadeGroupedPayment(numSlots), 
        new LemonadeQuery(),
        new OneGrouping(),        
        new LemonadeActivity(numSlots),
        new LemonadeAnonymousPolicy(numSlots), 
        new OneShotTermination(),
        new NoRecordKeeping());
    this.numSlots = numSlots;
  }


  @Override
  public AbsMarketRules copy() {
    return new LemonadeAnonRules(this.numSlots);
  } 
}
