package ePortfolio;

/** Represents a stock investment.
 * @author Connor Schulz (1103003)
*/
public class Stock extends Investment{

  /** Initializes the Stock variables
  @param newSymbol is a symbol of an investment
  @param newName is the investment name
  @param newQuantity is a quantity of an investment
  @param newPrice is the price of the investment
  */
  public Stock(String newSymbol, String newName, int newQuantity, double newPrice) throws Exception {
    super(newSymbol,newName,newQuantity,newPrice);
  }

  /** Copy constructor
    @param stock is the Stock object to be copied
  */
  public Stock(Stock stock) {
    super(stock);
  }

  /** calculates the book value when buying stock
    @param newQuantity is the stock quantity after buying
    @param newPrice is the new stock price
  */
  @Override
  public void calcBookValueBuy(int newQuantity, double newPrice) {
    double result;
    result = (newQuantity * newPrice) + 9.99;
    this.setBookValue(this.getBookValue()+result);
  }

  /** calcPayment calculates the payment when selling stock
    @param soldQuantity is the stock quantity before selling
    @return a double containg the payment after selling
  */
  @Override
  public double calcPayment(int soldQuantity) {
    double result;
    result = soldQuantity * this.getPrice() - 9.99;
    return result;
  }

}
