package org.codehaus.jam.test.samples.ejb;

public interface IEnv {
  /**
   * @ejbgen:remote-method 
   *   transaction-attribute = NotSupported
   *
   */
  public TradeResult buy(String customerName, String stockSymbol, int shares);
}
