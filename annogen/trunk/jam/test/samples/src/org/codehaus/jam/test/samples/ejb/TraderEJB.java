package org.codehaus.jam.test.samples.ejb;

public class TraderEJB implements IEnv {
  /**
   * @ejbgen:remote-method 
   *   isolation-level = Serializable
   *
   */
  public TradeResult buy(String customerName, String stockSymbol, int shares)
  {
    return null;
  }
}
