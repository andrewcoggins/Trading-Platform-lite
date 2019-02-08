package brown.platform.accounting.library;

import brown.platform.accounting.IInitialEndowment;
import brown.platform.tradeable.ITradeable;

import java.util.List;
import java.util.Map;

/**
 * Initial Endowment specifies what an agent has at the beginning of a game.
 */
public class InitialEndowment implements IInitialEndowment {

    private double money;
    private Map<String, List<ITradeable>> goods;

    /**
     * Agent's initial endowment.
     * @param money starting money
     * @param goods starting goods.
     */
    public InitialEndowment(double money, Map<String, List<ITradeable>> goods) {
        this.money = money;
        this.goods = goods;
    }

    @Override
    public double getMoney() {
      return this.money;
    }

    @Override
    public Map<String, List<ITradeable>> getGoods() {
      return this.goods;
    }
}
