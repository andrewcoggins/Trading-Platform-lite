package brown.auction.value.valuation.library;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import brown.platform.tradeable.ITradeable;
import brown.platform.tradeable.library.SimpleTradeable;

public class AdditiveValuationTest {
  
  @Test
  public void testAdditiveValuation() {
    ITradeable tradeable = new SimpleTradeable(0); 
    Map<ITradeable, Double> tMap = new HashMap<ITradeable, Double>();
    tMap.put(tradeable, 1.0); 
    AdditiveValuation a = new AdditiveValuation(tMap); 
    assertTrue(a.getValuation(tradeable) == 1.0); 
  }

}
