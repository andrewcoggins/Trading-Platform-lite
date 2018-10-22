package brown.user.server; 

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import brown.auction.preset.AbsMarketRules;
import brown.auction.preset.SMRADiscovery;
import brown.auction.preset.SSSPReserveRules;
import brown.auction.value.manager.library.SMRAConfig;
import brown.mechanism.tradeable.ITradeable;
import brown.mechanism.tradeable.library.SimpleTradeable;
import brown.platform.server.library.RunServer;
import brown.platform.server.library.SimulMarkets;
import brown.platform.server.library.Simulation;
import brown.system.setup.library.SMRASetup;

/**
 * a typical SMRA auction with a price discovery round
 * followed by a settlement round.
 * @author acoggins
 *
 */
public class SMRASecondPriceServer { 
  
  private static int numSims = 1;
  private static int numTradeables = 7;
  private static int delayTime = 3; 
  private static int lag = 300;
  
  public static void main(String[] args) throws InterruptedException { 
    // Create tradeables
    Set<ITradeable> allTradeables = new HashSet<ITradeable>(); 
    List<ITradeable> allTradeablesList = new LinkedList<ITradeable>();
    // add tradeables.
    for (int i = 0; i < numTradeables; i++) {
      allTradeables.add(new SimpleTradeable(i));
      allTradeablesList.add(new SimpleTradeable(i));
    }
    // construct sequence.
    List<SimulMarkets> seq = new LinkedList<SimulMarkets>();
    //construct price discovery rounds.
    List<AbsMarketRules> discoveryMarkets = new LinkedList<AbsMarketRules>();
    // TODO: add base and increments.
    Map<ITradeable, Double> baseVals = new HashMap<ITradeable, Double>(); 
    Map<ITradeable, Double> increments = new HashMap<ITradeable, Double>(); 
    for (int i = 0; i < numTradeables; i++) {
      baseVals.put(allTradeablesList.get(i), 0.0); 
      if (i == 4) {
        increments.put(allTradeablesList.get(i), 0.15);
      } else {
        increments.put(allTradeablesList.get(i), 0.2);
      }
    }
    discoveryMarkets.add(new SMRADiscovery(baseVals, increments)); 
    SimulMarkets discovery = new SimulMarkets(discoveryMarkets); 
    seq.add(discovery);
    //construct settlement round.
    List<AbsMarketRules> settlementMarket = new LinkedList<AbsMarketRules>();
    settlementMarket.add(new SSSPReserveRules()); 
    SimulMarkets settlement = new SimulMarkets(settlementMarket); 
    seq.add(settlement); 
    // initialize the simulation.
    Simulation sim = new Simulation(seq, new SMRAConfig(allTradeables),
        allTradeablesList, 1., new LinkedList<ITradeable>()); 
    RunServer smraServer = new RunServer(2121, new SMRASetup()); 
    smraServer.runSimulation(sim, numSims, delayTime, lag, null); 
  }
}