package brown.market.preset.library; 

import brown.market.preset.AbsMarketPreset;
import brown.rules.library.NoRecordKeeping;
import brown.rules.library.OneGrouping;
import brown.rules.library.OneShotActivity;
import brown.rules.library.OneShotTermination;
import brown.rules.library.SSSPAnonymous;
import brown.rules.library.SimpleSimultaneousQuery;
import brown.rules.library.VCGAllocation;
import brown.rules.library.VCGPayment;
import brown.rules.library.XRoundTermination;

public class SimpleVCG extends AbsMarketPreset {
  private int numRuns;
  
  
  public SimpleVCG(int numRuns) {
    super(new VCGAllocation(),
        new VCGPayment(),
        new SimpleSimultaneousQuery(),
        new OneGrouping(),
        new OneShotActivity(),
        new SSSPAnonymous(),
        new OneShotTermination(),
        new XRoundTermination(numRuns),
        new NoRecordKeeping());
    this.numRuns = numRuns;
  }

  @Override
  public AbsMarketPreset copy() {
    return new SimpleVCG(this.numRuns);
  }
}