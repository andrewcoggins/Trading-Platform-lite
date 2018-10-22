package brown.user.server;

import java.util.LinkedList;
import java.util.List;

import brown.auction.preset.AbsMarketRules;
import brown.auction.preset.ClockAuction;
import brown.auction.value.manager.library.SpecValV3Config;
import brown.mechanism.tradeable.ITradeable;
import brown.mechanism.tradeable.library.SimpleTradeable;
import brown.platform.server.library.RunServer;
import brown.platform.server.library.SimulMarkets;
import brown.platform.server.library.Simulation;
import brown.system.setup.library.SpecValSetup;

/**
 * server main class for combinatorial auctions.
 * @author kerry
 *
 */
public class CombAuctionServer {
  private int initDelay; 
  private int initLag;
  private int lag;      
  private int port;
  private int nSims;
  private double increment;
  private String outFile;
  
  public CombAuctionServer(int initDelay, int initLag, int lag, int port, int nSims, double increment, String outFile) {
    this.initDelay = initDelay;
    this.initLag = initLag;
    this.lag = lag;
    this.port = port;
    this.nSims = nSims;
    this.increment = increment;
    this.outFile = outFile;
  }
  
  
  public CombAuctionServer(int initDelay, int lag, int port, int nSims, double increment, String outFile) {
    this.initDelay = initDelay;
    this.initLag = lag;
    this.lag = lag;
    this.port = port;
    this.nSims = nSims;
    this.increment = increment;
    this.outFile = outFile;
  }
  
  public CombAuctionServer(int initDelay, int lag, int nSims, double increment, String outFile) {
    this(initDelay, lag, 2121, nSims, increment, outFile);
  }

  public void runAll() throws InterruptedException {                
    // Create _ tradeables
    // Create tradeables
    List<ITradeable> allTradeablesList = new LinkedList<ITradeable>();
    // add tradeables.
    for (int i = 0; i < 98; i++) {
      allTradeablesList.add(new SimpleTradeable(i));
    } 
    
    List<AbsMarketRules> market = new LinkedList<AbsMarketRules>();
    market.add(new ClockAuction(this.increment));
    SimulMarkets wrapper = new SimulMarkets(market);
    
    List<SimulMarkets> seq = new LinkedList<SimulMarkets>();  
    seq.add(wrapper);
    
    Simulation testSim = new Simulation(seq,new SpecValV3Config(),
        allTradeablesList,0.0,new LinkedList<ITradeable>());    
    RunServer testServer = new RunServer(port, new SpecValSetup());
    
    testServer.runSimulation(testSim, this.nSims, this.initDelay,this.initLag, this.lag, this.outFile);           
  }

  public static void main(String[] args) throws InterruptedException {
    CombAuctionServer server = new CombAuctionServer(5,100,1,10., null);
    server.runAll();
  }
}
