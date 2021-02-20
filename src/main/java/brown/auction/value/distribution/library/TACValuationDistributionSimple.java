package brown.auction.value.distribution.library;

import java.util.List;

import brown.auction.value.distribution.IValuationDistribution;
import brown.auction.value.generator.IValuationGenerator;
import brown.auction.value.valuation.ISpecificValuation;
import brown.auction.value.valuation.library.TACValuationSimple;
import brown.platform.item.ICart;

/**
 * A simplified version of the TACValuationDistribution- fewer items. 
 * @author andrewcoggins
 *
 */
public class TACValuationDistributionSimple extends AbsValuationDistribution implements IValuationDistribution {

  /**
   * For kryo DO NOT USE
   */
  public TACValuationDistributionSimple() {
    super(null, null);
  }
  
  public TACValuationDistributionSimple(ICart items, List<IValuationGenerator> generators) {
    super(items, generators); 
  }
  
  @Override
  public ISpecificValuation sample(Integer agentID, List<List<Integer>> agentGroups) {
    
    IValuationGenerator idealArrivalDateGenerator = this.generators.get(0); 
    IValuationGenerator idealDepartureDateGenerator = this.generators.get(1);
    IValuationGenerator hotelValueGenerator = this.generators.get(2); 
    IValuationGenerator amusementParkGenerator = this.generators.get(3); 
    IValuationGenerator alligatorWrestlingGenerator = this.generators.get(4); 
    IValuationGenerator museumGenerator = this.generators.get(5); 
    
    return new TACValuationSimple((int) Math.round(idealArrivalDateGenerator.makeValuation()),
        (int) Math.round(idealDepartureDateGenerator.makeValuation()), 
        (int) Math.round(hotelValueGenerator.makeValuation()), 
        (int) Math.round(amusementParkGenerator.makeValuation()), 
        (int) Math.round(alligatorWrestlingGenerator.makeValuation()), 
        (int) Math.round(museumGenerator.makeValuation())); 
  }

}
