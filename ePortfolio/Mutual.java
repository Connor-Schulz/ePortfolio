package ePortfolio;

/** Represents a mutual investment.
 * @author Connor Schulz (1103003)
*/
public class Mutual extends Investment{

  /** Initializes the Mutual variables
  @param newSymbol is a symbol of an investment
  @param newName is the investment name
  @param newQuantity is a quantity of an investment
  @param newPrice is the price of the investment
  */
  public Mutual(String newSymbol, String newName, int newQuantity, double newPrice) throws Exception{
    super(newSymbol,newName,newQuantity,newPrice);
  }

  /** Copy constructor
    @param mutual is the Mutual object to be copied
  */
  public Mutual(Mutual mutual) {
    super(mutual);
  }

  /** calculates the book value when buying mutual funds
    @param newQuantity is the unit quantity after buying
    @param newPrice is the new unit price
  */
  @Override
  public void calcBookValueBuy(int newQuantity, double newPrice) {
    double result;
    result = (newQuantity * newPrice);
    this.setBookValue(this.getBookValue()+result);
  }

  /** calcPayment calculates the payment when selling units
    @param soldQuantity is the unit quantity before selling
    @return a double containg the payment after selling
  */
  @Override
  public double calcPayment(int soldQuantity) {
    double result;
    result = soldQuantity * this.getPrice() - 45;
    return result;
  }

}
