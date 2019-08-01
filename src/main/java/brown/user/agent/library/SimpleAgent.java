package brown.user.agent.library;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import brown.auction.value.valuation.IGeneralValuation;
import brown.communication.bid.IBidBundle;
import brown.communication.bid.library.OneSidedBidBundle;
import brown.communication.messages.IInformationMessage;
import brown.communication.messages.ITradeMessage;
import brown.communication.messages.ITradeRequestMessage;
import brown.communication.messages.IValuationMessage;
import brown.communication.messages.library.TradeMessage;
import brown.logging.library.UserLogging;
import brown.platform.item.ICart;
import brown.platform.item.IItem;
import brown.platform.item.library.Cart;
import brown.platform.item.library.Item;
import brown.system.setup.ISetup;
import brown.system.setup.library.Startup;
import brown.user.agent.IAgent;

public class SimpleAgent extends AbsAgent implements IAgent {

  private IGeneralValuation agentValuation; 
  
  public SimpleAgent(String host, int port, ISetup gameSetup) {
    super(host, port, gameSetup);
  }
  
  public SimpleAgent(String host, int port, ISetup gameSetup, String name) {
    super(host, port, gameSetup, name);
  }

  @Override
  public void onInformationMessage(IInformationMessage informationMessage) {
    UserLogging.log("[+] Information Message Received");
  }

  @Override
  public void onTradeRequestMessage(ITradeRequestMessage tradeRequestMessage) {
    UserLogging.log("[+] Trade Request Message Received"); 
    
    Map<ICart, Double> bidMap = new HashMap<ICart, Double>(); 
    List<IItem> bidItems = new LinkedList<IItem>(); 
    
    bidItems.add(new Item("testItem", 1)); 
    
    ICart bidCart = new Cart(bidItems); 
    
    bidMap.put(bidCart, agentValuation.getValuation(bidCart)); 
    IBidBundle oneSided = new OneSidedBidBundle(bidMap);
    ITradeMessage tradeMessage = new TradeMessage(0, this.ID, tradeRequestMessage.getAuctionID(), oneSided);
    System.out.println(tradeMessage); 
    this.CLIENT.sendTCP(tradeMessage); 
  }

  @Override
  public void onValuationMessage(IValuationMessage valuationMessage) {
    UserLogging.log("[+] Valuation Message Received");
    System.out.println(valuationMessage.toString()); 
    this.agentValuation = valuationMessage.getValuation(); 
  }
  
  public static void main(String[] args) {
    new SimpleAgent("localhost", 2121, new Startup(), "solo"); 
    new SimpleAgent("localhost", 2121, new Startup(), "pacifica"); 
    while(true) {}
  }

}
