package ePortfolio;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.PrintWriter;

/** Represents and Controls a investment portfolio.
 * @author Connor Schulz (1103003)
*/
public class Portfolio {

//==============================================================================
//  Global Variables
//==============================================================================

  private static ArrayList<Investment> investList = new ArrayList<Investment>(1);

  private static HashMap<String, ArrayList<Integer>> nameMap = new HashMap<>();

  private static String fileSaveName = "";

//==============================================================================
//  Type Validation Method
//==============================================================================
  /** Validates strings
  @param stringToCheck is the string to check
  @param required is a boolean which is false if strings can be empty
  @return true if considered valid and false otherwise
  */
  public static boolean validateString(String stringToCheck, boolean required) {
    if (stringToCheck.equals("") && required == true) {return false;}
    return true;
  }

  /** Validates integers
  @param stringToCheck is the string to check
  @param required is a boolean which is false if strings can be empty
  @return true if considered valid and false otherwise
  */
  public static boolean validateInt(String stringToCheck, boolean required) {
    if (stringToCheck.equals("") && required == false) {return true;}
    try {
      Integer.parseInt(stringToCheck);
    } catch (Exception e) {return false;}
    return true;
  }

  /** Validates doubles
  @param stringToCheck is the string to check
  @param required is a boolean which is false if strings can be empty
  @return true if considered valid and false otherwise
  */
  public static boolean validateDouble(String stringToCheck, boolean required) {
    if (stringToCheck.equals("") && required == false) {return true;}
    try {
      Double.parseDouble(stringToCheck);
    } catch (Exception e) {return false;}
    return true;
  }


//==============================================================================
//  Help Methods
//==============================================================================

  /** Finds the index of an investment
  @param symbol is the symbol of the investment to find
  @return index of the investment or -1 if not found
  */
  public static int findInvestment(String symbol) {
    Investment tempInvestment; // Holds current stock object
    int foundIndex = -1;     // holds index of stock in stock list

    // Go through each item in the list
    for(int i = 0; i < investList.size(); i++) {
      tempInvestment = investList.get(i); // Get the next object

      // If a match is found, set 'foundIndex' to the index
      if (tempInvestment.getSymbol().equals(symbol)) {
        foundIndex = i;
      }
    }
    return foundIndex;
  }

  /** Cleans a string by converting to lowercase and removing excess whitespace
  @param input is the string to clean
  @return cleaned string
  */
  private static String cleanInput(String input) {
    input = input.toLowerCase();
    input = input.replaceAll("\\s","");
    return input;
  }

  /** Returns a string containing all investments
  @return a string containing all investments
  */
  public static String printInvestments() {
    Investment tempInvest;
    String returnString = "";
    int i;

    returnString += "STOCKS===========================\n";

    // Print all stocks
    for(i = 0;i<investList.size();i++) {
      tempInvest = investList.get(i);
      if(tempInvest instanceof Stock) {
        returnString += tempInvest.toString()+"\n";
      }
    }

    returnString += "MUTUAL FUNDS=====================\n";

    // Print all mutual funds
    for(i = 0;i<investList.size();i++) {
      tempInvest = investList.get(i);
      if(tempInvest instanceof Mutual) {
        returnString += tempInvest.toString()+"\n";
      }
    }
    return returnString;
  }


//==============================================================================
//  HashMap Methods
//==============================================================================

  /** will remove all instances of an index from the hash map and will
  completely delete the key if it is the last index associated with it
  @param index is the index of the Investment to remove*/
  private static void removeFromMap(Integer index) {
  ArrayList<Integer> locations = new ArrayList<Integer>(1);
  String[] toRemove = new String[nameMap.size()];
  int removeIndex = 0;
  int i,j;

  // Iterate through all keys in map
  for(String currentKey: nameMap.keySet()) {

    // Retrieve the list of indexes and remove the correct one
    locations = nameMap.get(currentKey);
    locations.remove(index);
    locations.trimToSize();

    // Decrease the value of all indexes greater than the current one
    for(j = 0; j < locations.size(); j++) {
      if (locations.get(j) > index) {
        locations.set(j,locations.get(j)-1);
      }
    }

    // Add the key to the to Remove list if it's empty
    if(locations.size() == 0) {
      toRemove[removeIndex] = currentKey;
      removeIndex++;
    } else {
      nameMap.put(currentKey,locations);
    }


  }

  // Remove all keys in toRemove
  for(i = 0; i < removeIndex; i++) {
    nameMap.remove(toRemove[i]);
  }
}

  /** tokenizes the contents of a string and adds them to a hash map
  @param name is the name of the investment to tokenize to the hash map*/
  private static void tokenizeToMap(String name) {
  String[] words;
  ArrayList<Integer> locations = new ArrayList<Integer>(1);
  int i;

  // Make the string all lowercase with any duplicate and padding whitespace removed
  name = name.trim().replaceAll(" +", " ").toLowerCase();
  words = name.split(" ");

  // Iterate through all keys
  for(i = 0; i < words.length; i++) {

    locations = nameMap.get(words[i]);

    // Check if list is null, increase capacity otherwise
    if (locations == null) {
      locations = new ArrayList<Integer>(1);
    } else {
      locations.ensureCapacity(locations.size()+1);
    }

    // Check if index already exists in array, trim to size otherwise
    if (locations.indexOf(investList.size()-1) == -1) {
      locations.add(investList.size()-1);
    } else {
      locations.trimToSize();
    }

    // Add array back to the key
    nameMap.put(words[i],locations);

  }

}


//==============================================================================
//  Get Methods
//==============================================================================

  /** returns an investment given an index
  @param index of the investment to retrieve
  @return Investment object*/
  public static Investment getInvestment(int index) {

    if(investList.size() == 0) {
      try {
        return new Stock("-","-",0,0); // return empty stock
      } catch (Exception e) {
        System.out.println(e);
        return null;
      }
    }
    int maxCount = investList.size();
    return investList.get(index % maxCount);
  }

  /** returns the size of the invest list
  @return the size of the invest list*/
  public static int getInvestListSize() {
    return investList.size();
  }

  /** returns the user-defined I/O file name
  @return file name*/
  public static String getFileName() {
    return fileSaveName;
  }


//==============================================================================
//  Primary Methods for Modifying Investments
//==============================================================================

  /** Buy is used to buy stock for new and existing investments
  @param type is the type of investment to buy
  @param symbol is the symbol to represent the investment
  @param name is the investment name
  @param quantity is the amount of units/stocks to buy
  @param price is the price per unit/share
  @return a string containing status and invest object if succesfull */
  public static String buy(String type, String symbol, String name, int quantity, double price) {
    Investment newInvestment = null;

    // Decide which type of investment to buy

    try {

      if(type.equals("stock")) {
        newInvestment = new Stock(symbol,name,quantity,price);
      } else if (type.equals("mutualfund")) {
        newInvestment = new Mutual(symbol,name,quantity,price);
      }

    } catch (Exception e) {
      return e.toString();
    }


    // Check if investment symbol exists in other investment type
    if(findInvestment(symbol) != -1) {
      if(type.equals("stock") && investList.get(findInvestment(symbol)) instanceof Mutual) {
          return "ERROR: Stock symbol '"+symbol+"' exists in mutual fund. No purchase can be made.";
      } else if(type.equals("mutualfund") && investList.get(findInvestment(symbol)) instanceof Stock) {
          return "ERROR: Mutual symbol '"+symbol+"' exists in stock fund. No purchase can be made.";
      } else {
        newInvestment = investList.get(findInvestment(symbol));
        newInvestment.setQuantity(quantity+newInvestment.getQuantity());
        newInvestment.setPrice(price);
      }
    }

    newInvestment.calcBookValueBuy(quantity,price);

    investList.ensureCapacity(investList.size()+1);
    investList.add(newInvestment);
    tokenizeToMap(newInvestment.getName());

    return quantity+" purchases successfully made for "+symbol+"\n\n"+newInvestment.toString();
  }

  /** Sell is used to sell stocks/units from an investment
  @param symbol is the symbol to represent the investment
  @param quantity is the amount of units/stocks to sell
  @param price is the price per unit/share
  @return a string containing status of the purchase */
  public static String sell(String symbol, int quantity, double price) {
    Investment sellInvestment;
    int matchIndex;      // Index of the matching investment in the array
    String name;       // The name of the investment
    int oldQuantity; // Holds the original quantity before updating

    // try to match the symbol to an existing investment
    matchIndex = findInvestment(symbol);

    // IF LIST FOUND A MATCH
    if (matchIndex != -1) {
      sellInvestment = investList.get(matchIndex);   // Get the stock Object
      oldQuantity = sellInvestment.getQuantity();  // Get the current quantity

      if(oldQuantity >= quantity) {

        sellInvestment.setPrice(price);
        sellInvestment.setQuantity(oldQuantity - quantity);
        sellInvestment.calcBookValueSell(oldQuantity);

        // Remove the investment if all stocks are sold
        if(sellInvestment.getQuantity() == 0) {
          removeFromMap(investList.indexOf(sellInvestment));
          investList.remove(matchIndex);
          investList.trimToSize();
        }

        // Print the Payment earned from selling
        return "Payment from selling "+quantity+" shares from "+symbol+" is: "+sellInvestment.calcPayment(quantity);

      } else {
        // Print an error message if the stock to sell exceeds the amount of available stock
        return "ERROR: Cannot sell "+quantity+" shares when only "+sellInvestment.getQuantity()+" exist.";
      }

    } else {
      // Print a error if no investments were found
      return "ERROR: No investments exist with symbol: '"+symbol+"'";
    }

  }

  /** Update updates the prices of all current investments
  @param tempInvestment is the investment to update
  @param newPrice is the new price of the investment
  @return a string containing status of the updated investment */
  public static String update(Investment tempInvestment, double newPrice) {
    String returnString = "";
    tempInvestment.setPrice(newPrice);

    returnString += "Investment successfully updated.\n";
    returnString += tempInvestment.toString() + "\n\n";
    return returnString;
  }

  /** GetGain Calculates the total gain of all investments
  @return a string containing the total gain*/
  public static String getGain() {
    Investment tempInvestment;
    double totalGain = 0;
    int i;

    // Iterate through all stocks and add gain to the total
    for(i = 0;i<investList.size();i++) {
      tempInvestment = investList.get(i);
      totalGain += tempInvestment.calcGain();
    }

    return ""+totalGain;
  }

  /** GetGain Calculates the individual gains of all investments
  @return a string containing all gains individually*/
  public static String getIndividualGain() {
    Investment tempInvestment;
    String allGains = "";
    int i;

    // Iterate through all stocks and add the gain to the string
    for(i = 0;i<investList.size();i++) {
      tempInvestment = investList.get(i);
      allGains += "Symbol: \"" + tempInvestment.getSymbol() + "\"\n";
      allGains += "Gain:   " + tempInvestment.calcGain()  + "\n\n";
    }

    return allGains;
  }

  /** Search function to help the user search for an investment based upon a
  Symbol, Keywords, and a price range
  @param symbol is the symbol of a investment
  @param keywordString is a string of keywords to search for
  @param priceLow is the low end of the price
  @param priceHigh is the high end of the price
  @return a string containing investment object info for any matching investments */
  public static String search(String symbol, String keywordString, double priceLow, double priceHigh) {
    ArrayList<Integer> indexes = new ArrayList<Integer>(investList.size());
    Investment tempInvestment;
    String matchString = "";
    String[] keywordArray; // Holds all keywords to search for in array form
    boolean printMatch;  // True if all search criteria match an investment
    int matchAmount;   // Amount of matched investments found from the keywords
    int i;           // Counter for loop

    // Set high and low values to the 2 extreme ends if they are not specified
    if (priceLow == -1)  {priceLow = 0;}
    if (priceHigh == -1) {priceHigh = Integer.MAX_VALUE;}

    // Split all keywords into an array
    keywordArray = keywordString.split(" ");

    // If keyword string is not empty, use the words as
    // keys to access the hashmap index arrays. Use retainAll to remove any
    // non-common elements
    if(!keywordString.equals("")) {

      indexes = nameMap.get(cleanInput(keywordArray[0]));
      for(i=0; i < keywordArray.length; i++) {
        if (indexes != null) {
          indexes.retainAll(nameMap.get(cleanInput(keywordArray[i])));
        }
      }

    } else {
      // Fill the index array with all possible indexes if keyword string is empty
      for(i=0; i < investList.size(); i++) {
        indexes.add(i);
      }
    }

    // set match amount to 0 if indexes is null
    if (indexes == null) {
      matchAmount = 0;
    } else {
      matchAmount = indexes.size();
    }

    // Iterate through all stocks if no keywords are provided
    for(i = 0;i<matchAmount;i++) {
      printMatch = true;

      // Get investment
      tempInvestment = investList.get(indexes.get(i));

      // Check if the investment symbol matches the search one (if valid)
      if (tempInvestment.getSymbol().equals(symbol) == false && symbol.equals("") == false) {
        printMatch = false;

      // Check if the investment price is within the search range (if valid)
      } else if (priceLow > tempInvestment.getPrice() || priceHigh < tempInvestment.getPrice() ) {
        printMatch = false;
      }

      // Add the investment if it matches all search criteria
      if (printMatch == true) {
        matchString += tempInvestment.toString()+"\n";
      }

    }
    return matchString;
  }


//==============================================================================
//  File I/O Methods
//==============================================================================

  /** Creates an investment from an array of values
  @param attributes is a list of attributes for the investment class*/
  public static void createFromFile(String[] attributes) {
    Investment newInvestment;

    // Set new investment to the correct type
    try{

      if(attributes[0].equals("stock")) {
        newInvestment = new Stock(attributes[1],attributes[2],Integer.parseInt(attributes[3]),Double.parseDouble(attributes[4]));
      } else if (attributes[0].equals("mutualfund")) {
        newInvestment = new Mutual(attributes[1],attributes[2],Integer.parseInt(attributes[3]),Double.parseDouble(attributes[4]));
      } else {
        return;
      }
    } catch (Exception e) {
      System.out.println(e);
      return;
    }

    newInvestment.setBookValue(Double.parseDouble(attributes[5]));

    // Add to the array list and tokenize to the hash map
    investList.ensureCapacity(investList.size()+1);
    investList.add(newInvestment);
    tokenizeToMap(newInvestment.getName());
  }

  /** Loads all investments from a file
  @param fileName is the name of a file to be loaded from*/
  public static void fileRead(String fileName) {
    String[] investValues = new String[6];
    String line;
    int i;

    try {

      // Open the file
      File f = new File(fileName);

      // Check if file exists, and create it if it doesnt
      if (f.exists() == false) {
        System.out.println("Could not find \""+fileName+"\". Creating file with given name instead");
        f.createNewFile();
        return;
      }

      Scanner scanner = new Scanner(f);


      // Read each line in the file
      while (scanner.hasNextLine()) {
        // Get the 6 lines and Retrieve the part with the data
        for(i = 0 ; i < 6 ; i++) {
          line = scanner.nextLine();
          investValues[i] = line.split("\"")[1];
        }
        // Create the investments and skip over the blank line if it exists
        createFromFile(investValues);
        if (scanner.hasNextLine()) {
          line = scanner.nextLine();
        }
      }

    } catch (Exception e) {
      System.out.println("ERROR: Could not open or read \""+fileName+"\"");
    }
    System.out.println("File Contents read successfully.");
    printInvestments();
  }

  /** Saves all current investments to a file
  @param fileName is the name of a file to be saved to*/
  public static void fileSave(String fileName) {
    Investment currentInvestment;
    String type = "";
    int i;

    try {

      PrintWriter fileWriter = new PrintWriter(fileName,"UTF-8");

      // Loop through each investment
      for (i = 0; i < investList.size(); i++) {
        currentInvestment = investList.get(i);

        // Set the type field depending on the investment type
        if(currentInvestment instanceof Stock) {
          type = "type = \"stock\"\n";
        } else {
          type = "type = \"mutualfund\"\n";
        }

        // Write the type and investment to the file
        fileWriter.println(type+currentInvestment.toString());
      }

      fileWriter.close();
    } catch (Exception e) {
      System.out.println("ERROR: Could not open or read, \""+fileName+"\"");
      return;
    }
    System.out.println("Investments saved.");
  }


//==============================================================================
//  Main Method
//==============================================================================

  /** Main function to handle menu options and call methods
   @param args is the command line arguments*/
  public static void main(String[] args) {

    // Print a message and exit if anything but 1 command line argument is given
    if (args.length != 1) {
      System.out.println("ERROR: running this program requires the format 'java ePortfolio.Portfolio \"filename\"'");
      return;
    }

    fileSaveName = args[0];

    // Read in information from the given file name
    fileRead(fileSaveName);

    Display layout = new Display();
    layout.setVisible(true);

  }
}
