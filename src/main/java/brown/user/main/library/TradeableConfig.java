package brown.user.main.library;

import java.lang.reflect.Constructor;

import brown.user.main.ITradeableConfig;


public class TradeableConfig implements ITradeableConfig {
  
  private String tradeableName; 
  private Integer numTradeables; 
  
  
  public TradeableConfig(String tradeableName, Integer numTradeables) {
    this.tradeableName = tradeableName; 
    this.numTradeables = numTradeables;    
  }

  @Override
  public String getTradeableName() {
    
    return this.tradeableName;
  }

  @Override
  public Integer getNumTradeables() {
    
    return this.numTradeables;
  }

  @Override
  public String toString() {
    return "TradeableConfig [tradeableName=" + tradeableName
        + ", numTradeables=" + numTradeables + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((numTradeables == null) ? 0 : numTradeables.hashCode());
    result = prime * result
        + ((tradeableName == null) ? 0 : tradeableName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TradeableConfig other = (TradeableConfig) obj;
    if (numTradeables == null) {
      if (other.numTradeables != null)
        return false;
    } else if (!numTradeables.equals(other.numTradeables))
      return false;
    if (tradeableName == null) {
      if (other.tradeableName != null)
        return false;
    } else if (!tradeableName.equals(other.tradeableName))
      return false;
    return true;
  }


}
