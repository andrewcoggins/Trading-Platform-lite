package brown.platform.market.library;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import brown.auction.marketstate.library.MarketState;
import brown.auction.preset.AbsMarketRules;
import brown.auction.prevstate.library.BlankStateInfo;
import brown.auction.prevstate.library.PrevStateInfo;
import brown.logging.library.PlatformLogging;
import brown.mechanism.tradeable.ITradeable;
import brown.platform.accounting.library.Ledger;
import brown.platform.market.IMarketManager;
import brown.platform.server.library.SimulMarkets;

/**
 * Market manager stores and handles multiple markets 
 * @author acoggins
 *
 */
public class MarketManager implements IMarketManager {
  // stores all ledgers in a simulation
	private List<Map<Market, Ledger>> ledgers;
	// stores all markets in a simulation
	public List<Map<Integer, Market>> markets;
	private PrevStateInfo information;
	public Integer index; 
	private Integer idCount;
	private boolean lock;
	
	/**
	 * Constructor for a market manager initializes ledgers and markets.
	 * ledgers tracks the ledgers for all markets at each time
	 * markets maps each market at each time to a unique id
	 * information is initially a blank prevstateinfo
	 * index is initially set to -1
	 * idCount keeps track of the number of markets in the MarketManager- 
	 * initially, this is 0.
	 */
	public MarketManager() {
		this.ledgers = new LinkedList<Map<Market, Ledger>>();
		this.markets = new LinkedList<Map<Integer, Market>>();	
		this.information = new BlankStateInfo();
		this.index = -1; 
		this.idCount = 0;
		this.lock = false;
	}

  public void addSimulMarket(SimultaneousMarket s, List<ITradeable> tradeables, List<Integer> agents) {
		if (!this.lock){
			this.index++;
			this.ledgers.add(new ConcurrentHashMap<Market, Ledger>());
			this.markets.add(new ConcurrentHashMap<Integer, Market>());
			for (AbsMarketRules preset : s.markets) {
				this.open(preset, idCount, tradeables, agents);
				idCount++;
			}
		} else {
			PlatformLogging.log("Creation denied: market manager locked.");
		}
	}
	  
	/**
	 * Opens a market
	 * @param market
	 * @return
	 */
  @Override
	public boolean open(AbsMarketRules rules, Integer marketID, List<ITradeable> tradeables, List<Integer> agents) {
	  Market market = new Market(rules.copy(), new MarketState(marketID,tradeables,this.information));
	   if (ledgers.get(index).containsKey(market)) {
	      return false;
	   }
	   market.setGroupings(agents);
	   this.ledgers.get(index).put(market, new Ledger(market.getMarketID()));
	   this.markets.get(index).put(market.getMarketID(), market);
	   return true;
	}
	
	 /**
   * Closes a market 
   * @param server
   * @param ID
   * @param closingState
   */
  @Override
  public void close(Integer ID) {
    Market toClose = this.markets.get(index).get(ID);
    toClose.close();
    this.markets.get(index).put(ID, toClose);
  }

	/**
	 * Gets the ledger for this market ID
	 * @param ID
	 * @return
	 */
  @Override
	public Ledger getLedger(Integer ID) {
		return ledgers.get(index).get(markets.get(index).get(ID));
	}

	/**
	 * Gets the market for this ID
	 * @param ID
	 * @return
	 */
  @Override
	public Market getMarket(Integer ID) {
	    return markets.get(index).get(ID);	  
	}

	
	public boolean MarketOpen(Integer ID) {
	  if (index == -1) return false; 
	  if (markets.get(index).containsKey(ID)) {	
	    return markets.get(index).get(ID).isOpen();
	  }
	  return false; 
	 }
	  
	
	/**
	 * Gets all of the auctions
	 * @return
	 */
  @Override
	public Collection<Market> getAuctions() {
		return this.markets.get(index).values();
	}

  @Override
  public void update(Integer marketID) {
   this.information.combine(this.markets.get(index).get(marketID).constructSummaryState());
  }
  
  @Override
  public boolean anyMarketsOpen() {
    boolean toReturn = false;
    for (Market m : this.getAuctions()) {
      if (!m.isOver()) {
        toReturn = true;
        break;
      }
    }
    return toReturn;    
  }

  @Override
  public void reset() {
    this.index = -1;
    this.ledgers = new LinkedList<Map<Market, Ledger>>();
    this.markets = new LinkedList<Map<Integer, Market>>();  
    this.information = new BlankStateInfo();
  }

  
  public void initializeInfo(PrevStateInfo info) {
    this.information = info;
  }

  public void updateAllInfo() {
    for (Market market: this.markets.get(index).values()) {
      this.information.combine(market.constructSummaryState());
    }
  }


  public void lock(){
  	this.lock = true;
  }
}
