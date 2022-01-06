package ePortfolio;

/** Represents an investment.
 * @author Connor Schulz (1103003)
*/
public abstract class Investment {
  private String symbol;
  private String name;
  private int quantity;
  private double price;
  private double bookValue;

  /** Initializes the Investment variables
  @param newSymbol is a symbol of an investment
  @param newName is the investment name
  @param newQuantity is a quantity of an investment
  @param newPrice is the price of the investment
  */
  public Investment(String newSymbol, String newName, int newQuantity, double newPrice) throws Exception {
    if (!newSymbol.equals("")) {
      this.symbol = newSymbol;
    } else {
      throw new Exception("Error: symbol cannot be empty string");
    }

    if (!newName.equals("")) {
      this.name = newName;
    } else {
      throw new Exception("Error: name cannot be empty string");
    }

    if (newQuantity > 0) {
      this.quantity = newQuantity;
    } else {
      throw new Exception("Error: quantity must be a positive integer greater than 0");
    }

    if (newPrice > 0) {
      this.price = newPrice;
    } else {
      throw new Exception("Error: price must be a positive double greater than 0");
    }

    this.bookValue = 0;
  }
  /** Copy constructor
    @param investment is the Investment object to be copied
  */
  public Investment(Investment investment) {
    this.symbol    = investment.symbol;
    this.name      = investment.name;
    this.quantity  = investment.quantity;
    this.price     = investment.price;
    this.bookValue = investment.bookValue;
  }

  // Set functions

  /** sets the symbol
    @param newSymbol a string containing an investment symbol
  */
  public void setSymbol(String newSymbol) {
    this.symbol = newSymbol;
  }

  /** sets the name
    @param newName a string containing an investment name
  */
  public void setName(String newName) {
    this.name = newName;
  }

  /** sets the quantity
    @param newQuantity an int containing the quantity of stocks/units
  */
  public void setQuantity(int newQuantity) {
    this.quantity = newQuantity;
  }

  /** sets the price
    @param newPrice a double containing the stock stock/unit;
  */
  public void setPrice(double newPrice) {
    this.price = newPrice;
  }

  /** sets the book value
    @param newBookValue a double containing the book value;
  */
  public void setBookValue(double newBookValue) {
    this.bookValue = newBookValue;
  }

  // Get functions

  /** gets the symbol
    @return a string containing the investment symbol
  */
  public String getSymbol() {
    return this.symbol;
  }

  /** gets the name
    @return a string containing the investment name
  */
  public String getName() {
    return this.name;
  }

  /** gets the quantity
    @return an int containing the stocks/units quantity
  */
  public int getQuantity() {
    return this.quantity;
  }

  /** gets the price
    @return a double containing the stock/unit price
  */
  public double getPrice() {
    return this.price;
  }

  /** gets the book value
    @return a double containing the investment book value
  */
  public double getBookValue() {
    return this.bookValue;
  }

  /** calculates the book value when buying investments
    @param newQuantity is the stock quantity after buying
    @param newPrice is the new stock price
  */
  public abstract void calcBookValueBuy(int newQuantity, double newPrice);


  /** calculates the book value when selling investments
    @param oldQuantity is the stock quantity before selling
  */
  public void calcBookValueSell(int oldQuantity) {
    double result;
    result = bookValue * quantity/oldQuantity;
    this.bookValue = result;
  }

  /** calcPayment calculates the payment when selling investments
    @param soldQuantity is the stock quantity before selling
    @return a double containg the payment after selling
  */
  public abstract double calcPayment(int soldQuantity);


  /**calcGain calculates the gain for an investment
    @return a double containg the calulated gain
  */
  public double calcGain() {
    double result;
    result = calcPayment(this.quantity) - this.bookValue;
    return result;
  }

  /**toString Converts an investment object to a string
    @return a string containg investment object info
  */
  @Override
  public String toString() {
    String investString = "";
    investString+= "symbol    = \""+this.getSymbol()+"\"\n";
    investString+= "name      = \""+this.getName()+"\"\n";
    investString+= "quantity  = \""+this.getQuantity()+"\"\n";
    investString+= "price     = \""+this.getPrice()+"\"\n";
    investString+= "bookValue = \""+this.getBookValue()+"\"\n";

    return investString;
  }

  /**equals compares two investment objects to see if they match
    @param compareTo is an investment to compare the current investment object to
    @return an int that is 1 if the Investments are equal and 0 if they dont
  */
  @Override
  public boolean equals(Object compareTo) {
    boolean isEqual = true;
    if (this != compareTo) {
      isEqual = false;
    }
    return isEqual;
  }

}
