package brown.platform.managers.library;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import brown.communication.messages.IInformationMessage;
import brown.communication.messages.IInformationRequestMessage;
import brown.communication.messages.ITradeMessage;
import brown.communication.messages.ITradeRequestMessage;
import brown.logging.library.PlatformLogging;
import brown.platform.accounting.IOrder;
import brown.platform.managers.IMarketManager;
import brown.platform.market.IMarket;
import brown.platform.market.IMarketBlock;
import brown.platform.market.IMarketRules;
import brown.platform.market.library.SimultaneousMarket;
import brown.platform.tradeable.ITradeable;
import brown.platform.whiteboard.IWhiteboard;
import brown.platform.whiteboard.library.Whiteboard;

/**
 * Market manager stores and handles multiple markets
 * 
 * @author acoggins
 *
 */
public class MarketManager implements IMarketManager {
  // stores all markets in a simulation
  private Map<Integer, IMarket> openMarkets;
  private List<IMarketBlock> allMarkets;
  private IWhiteboard whiteboard; 
  private boolean lock;

  /**
   * Constructor for a market manager initializes ledgers and markets. ledgers
   * tracks the ledgers for all markets at each time markets maps each market at
   * each time to a unique id information is initially a blank prevstateinfo
   * index is initially set to -1 idCount keeps track of the number of markets
   * in the MarketManager- initially, this is 0.
   */
  public MarketManager() {
    this.allMarkets = new LinkedList<IMarketBlock>();
    this.lock = false;
    this.whiteboard = new Whiteboard(); 
  }

  @Override
  public void createSimultaneousMarket(List<IMarketRules> s,
      List<Map<String, Integer>> marketTStrings, Map<String, List<ITradeable>> allTradeables) {
    if (!this.lock) {
      List<Map<String, List<ITradeable>>> marketTradeables =
          new LinkedList<Map<String, List<ITradeable>>>();
      for (Map<String, Integer> tMap : marketTStrings) {
        Map<String, List<ITradeable>> singleMarketTradeables =
            new HashMap<String, List<ITradeable>>();
        for (String tName : tMap.keySet()) {
          List<ITradeable> nameTradeables = allTradeables.get(tName);
          List<ITradeable> mTradeableList =
              nameTradeables.subList(0, tMap.get(tName));
          singleMarketTradeables.put(tName, mTradeableList);
        }
        marketTradeables.add(singleMarketTradeables);
      }
      IMarketBlock marketBlock = new SimultaneousMarket(s, marketTradeables);
      this.allMarkets.add(marketBlock); 
    } else {
      PlatformLogging.log("ERROR: market manager locked.");
    }
  }

  @Override
  public void lock() {
    this.lock = true;
  }

  @Override
  public Map<Integer, List<IOrder>> runSimultaneousMarket() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Integer, ITradeRequestMessage>
      giveTradeRequests(Integer marketID) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void handleTradeMessage(ITradeMessage message) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public IInformationMessage
      handleInformationRequest(IInformationRequestMessage message) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub
    
  }

}
