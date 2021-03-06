package brown.platform.managers.library;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import brown.logging.library.ErrorLogging;
import brown.logging.library.PlatformLogging;
import brown.platform.item.IItem;
import brown.platform.item.library.Item;
import brown.platform.managers.IItemManager;

/**
 * An ItemManager is an entity that creates tradeables for an auction.
 *
 */

public class ItemManager implements IItemManager {

  private Map<String, IItem> items;
  private boolean lock;

  /**
   * initializes the items variable to an initial empty value
   */
  public ItemManager() {
    this.items = new HashMap<String, IItem>();
    this.lock = false;
  }

  @Override
  public void lock() {
    this.lock = true;
  }

  @Override
  public void createItems(String name, int numTradeables) {
    try {
      if (!lock) {
        this.items.put(name, new Item(name, numTradeables)); 
      } else {
        PlatformLogging.log("Creation denied: tradeable manager locked.");
      }
    } catch (NotStrictlyPositiveException e) {
      ErrorLogging.log("NotStrictlyPositiveException: " + e);
    }
  }

  @Override
  public IItem getItems(String name) {
    if (this.items.containsKey(name)) {
      return this.items.get(name);
    } else {
      ErrorLogging.log("ERROR: ItemManager: no such tradeable exists.");
      return new Item("blank", 0);
    }
  }

}
