package brown.channels.library;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import brown.agent.Agent;
import brown.assets.accounting.Ledger;
import brown.bundles.MarketState;
import brown.bundles.SimpleBidBundle;
import brown.channels.IAgentChannel;
import brown.channels.MechanismType;
import brown.messages.auctions.Bid;
import brown.rules.paymentrules.PaymentType;
import brown.setup.Logging;
import brown.valuable.library.Tradeable;

/*
 * Implements IMarket for Simple auctions
 */
public class SimpleAuctionChannel implements IAgentChannel {
	private final Integer ID;
	private final Ledger LEDGER;
	private final SimpleBidBundle HIGHBID;
	private final int ELIGIBILITY;
	
	private final PaymentType PTYPE;
	private final MechanismType MTYPE;
	
	public SimpleAuctionChannel() {
		this.ID = null;
		this.LEDGER = null;
		this.HIGHBID = null;
		this.PTYPE = null;
		this.MTYPE = null;
		this.ELIGIBILITY = 0;
	}
	
	/**
	 * Contructor
	 * @param ID
	 * @param ledger
	 * @param highBid
	 */
	public SimpleAuctionChannel(Integer ID, Ledger ledger, PaymentType ptype, MechanismType mtype,
			SimpleBidBundle highBid, int elig) {
		if (highBid == null || ledger == null) {
			throw new IllegalArgumentException("Null structures");
		}
		this.ID = ID;
		this.LEDGER = ledger;
		this.HIGHBID = highBid;
		this.PTYPE = ptype;
		this.MTYPE = mtype;
		this.ELIGIBILITY = elig;
	}

	@Override
	public Ledger getLedger() {
		return this.LEDGER;
	}

	@Override
	public void dispatchMessage(Agent agent) {
		switch(this.MTYPE) {
		case ContinuousDoubleAuction:
			break;
		case LMSR:
			break;
		case Lemonade:
			break;
		case OpenOutcry:
			agent.onSimpleOpenOutcry(this);
			break;
		case SealedBid:
			agent.onSimpleSealed(this);
			break;
		default:
			Logging.log("[X] unknown PaymentType");
			break;
		}
	}

	@Override
	public Integer getAuctionID() {
		return this.ID;
	}
	
	/**
	 * Returns the payment type
	 * @return
	 */
	public PaymentType getPaymentType() {
		return this.PTYPE;
	}
	
	/**
	 * Returns the high bid
	 * @return double
	 */
	public MarketState getMarketState(Tradeable type) {
		return this.HIGHBID.getBid(type);
	}
	
	/**
	 * Returns if this bundle maximizes revenue
	 * @return boolean
	 */
	public int getEligibility() {
		return this.ELIGIBILITY;
	}

	public void bid(Agent agent, Map<Tradeable, Double> bids) {
		Map<Tradeable, MarketState> fixedBids = new HashMap<Tradeable,MarketState>();
		for (Entry<Tradeable, Double> bid : bids.entrySet()) {
			fixedBids.put(bid.getKey(), new MarketState(agent.ID, bid.getValue()));
			//works here
			System.out.println("fixed bids " + fixedBids);
//			if (fixedBids.size() > 10) {
//				agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
//				fixedBids.clear();
//			}
		}
		if (fixedBids.size() > 0) {
			agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
		}
	}

	public void demandSet(Agent agent, Set<Tradeable> toBid) {
		Map<Tradeable, MarketState> fixedBids = new HashMap<Tradeable,MarketState>();
		for (Tradeable bid : toBid) {
			fixedBids.put(bid, new MarketState(agent.ID, 0));
			if (fixedBids.size() > 10) {
				agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
				fixedBids.clear();
			}
		}
		if (fixedBids.size() != 0) {
			agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
		}
	}
	
	public void xorBid(Agent agent, Map<Set<Tradeable>, Double> toBid) {
		if (3 < toBid.size()) {
			throw new IllegalArgumentException("Attempt to submit too many atomic bids");
		}
		
		Map<Tradeable, MarketState> fixedBids = new HashMap<Tradeable,MarketState>();
		for (Entry<Set<Tradeable>, Double> bid : toBid.entrySet()) {
			if (this.ELIGIBILITY < bid.getKey().size()) {
				throw new IllegalArgumentException("Attempt to submit ineligible bid " + bid.getKey());
			}
			for (Tradeable t : bid.getKey()) {
				fixedBids.put(t, new MarketState(agent.ID, bid.getValue()));
				if (fixedBids.size() > 10) {
					agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
					fixedBids.clear();
				}
			}
		}
		
		if (fixedBids.size() != 0) {
			agent.CLIENT.sendTCP(new Bid(0,new SimpleBidBundle(fixedBids),this.ID,agent.ID));
		}
	}
	
	public Set<Tradeable> getTradeables() {
		return this.HIGHBID.getTradeables();
	}

}