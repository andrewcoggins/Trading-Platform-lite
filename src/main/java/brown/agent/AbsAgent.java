package brown.agent;

import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import brown.exceptions.AgentCreationException;
import brown.messages.library.AbsMessage;
import brown.messages.library.BankUpdateMessage;
import brown.messages.library.GameReportMessage;
import brown.messages.library.MarketUpdateMessage;
import brown.messages.library.PrivateInformationMessage;
import brown.messages.library.RegistrationMessage;
import brown.setup.ISetup;
import brown.tradeable.ITradeable;

/**
 * every agent class extends this class.
 * @author andrew
 *
 */
public abstract class AbsAgent extends AbsClient implements IAgent { 
  protected double monies;
  protected List<ITradeable> goods;
  
  /**
   * 
   * AbsAgent takes in a host, a port, an ISetup.
   * @param host
   * @param port
   * @param gameSetup
   * @throws AgentCreationException
   */
  public AbsAgent(String host, int port, ISetup gameSetup)
      throws AgentCreationException {
    super(host, port, gameSetup);
    final AbsAgent agent = this;
    // All agents listen for messages.
    CLIENT.addListener(new Listener() {
      public void received(Connection connection, Object message) {
        synchronized (agent) {
          if (message instanceof AbsMessage) {
            AbsMessage theMessage = (AbsMessage) message;
            theMessage.dispatch(agent);
          }
        }
      }
    });

    CLIENT.sendTCP(new RegistrationMessage(-1));
    
    this.monies = 0.0;
    this.goods = new LinkedList<ITradeable>();
  }
  
  public void onBankUpdate(BankUpdateMessage bankUpdate) {
    this.monies += bankUpdate.moniesChanged;
    this.goods.add(bankUpdate.tradeableAdded);
    this.goods.remove(bankUpdate.tradeableLost);
  }
  
  public void onPrivateInformation(PrivateInformationMessage registration) {
  }
  
  public void onGameReport(GameReportMessage gameReport) {
  }

  public void onMarketUpdate(MarketUpdateMessage marketUpdateMessage) {
  }  
}

  
 