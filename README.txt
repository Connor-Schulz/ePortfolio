================================================================================
Portfolio Read Me
================================================================================


General Info
================================================================================
Created by: Connor Schulz (1103003) for CIS2430

Last updated: November 29 2021


This program is created to help investors keep track of their different investments.
It allows users to preform various actions on these investments such as:

Buying and Selling investments
Updating prices of all investments
Get the total gain for all investments
Perform an advanced search on all investments

With this program, Investors can manage their portfolio so they can better keep
track of their investments and keep them up to date easily


Assumptions and Limitations
================================================================================
  - Assumes java is already installed
  - Assumes all inputted values are valid stocks and prices
  - Assumes Files used for input follow the required format
  - Assumes constant commission and redemption fees of $9.99 and $45.00 respectively

User Guide
================================================================================
This application can be compiled by cd'ing to the ePortfolio directory and
running the following commands, where filename represents the name of a file:

javac *.java
cd ..
java ePortfolio.Portfolio "filename.txt"

The application should start up in a window and the user will be welcomed by
the main menu. You can input numbers or the names of options to perform them


File Format:

This format is expected for files when being read

"example.txt"
--------------------------------------------------------------------------------
type = "stock"
symbol = "APPL"
name = "Apple Inc."
quantity = "500"
price = "500.0"
bookValue = "55049.99"

type = "mutualfund"
symbol = "SSETX"
name = "BNY Mellon Growth Fund Class I"
quantity = "450"
price = "500.0"
bookValue = "23967.0"

--------------------------------------------------------------------------------

"type" can only be 'stock' or 'mutualfund' (case sensitive)
"quantity" must be a valid Integer
"price" must be a valid Double
"bookValue" must be a valid Double

any deviations will result in an exception


Submenus
--------------------------------------------------------------------------------
Buy
  - Choose the investment type (either Stock or Mutual Fund) by using the drop
    down box.
  - Input the symbol for an investment you would like to buy. ex: "TSLA"
  - Input the name of the investment. ex: "Tesla"
    (this prompt only appears when the investment you are purchasing is new
  - Input the amount of stock/units you would like to purchase. ex: "500"
  - Input the  price of the stock/unit. ex: "153.46"

  - Press Buy once all relevant information has been filled out

  - Reset button will clear all the text fields
--------------------------------------------------------------------------------
Sell
  - Input the symbol for the investment you would like to sell. ex: "TSLA"
  - Input the amount of stock/units you would like to sell. ex: "250"
  - Input the selling price of the stock/unit. ex: "160.50"

  - Press Sell once all relevant information has been filled out

  - Reset button will clear all the text fields
--------------------------------------------------------------------------------
Update
  - For each account, enter a new price. ex: "230.44"
  - Press save to update the price

  - Next button will cycle to the next investment
  - Prev button will cycle to the previous investment
--------------------------------------------------------------------------------
Get gain
  - No input required
  - Displays total and individual gains
--------------------------------------------------------------------------------
Search
  - Input the symbol to search for. (Optional) ex. "TSLA"
  - Input a string of search words broken up by spaces. (Optional) ex. "Tesla Motors"
  - Input a price range low end to search for. (Optional) ex. "0", "1000"
  - Input a price range high end to search for. (Optional) ex. "100", "1000"

  - Press Search once all relevant information has been filled out

  - Reset button will clear all the text fields

  - All investments meeting all search criteria is displayed
--------------------------------------------------------------------------------
Quit
  - No input required
  - Saves investments to file
  - Stops the program
--------------------------------------------------------------------------------

Test Plan
================================================================================

------------
File Input
------------
- No file given (results in program printing an error and exiting)
- Given file does not exist (should result in file being created)
- Given file is empty (accepts file, no investments created)
- Given file has 1 entry (accepts file, one investment created)
- Given file has 2 entries (accepts file, both investments created)
- Given file has 3+ entries (accepts file, all investments created)
- Given file has incorrect format (results in an exception being thrown, current and remaining investments in the file are not created)

------------
File Output
------------

- All current investments should appear in the given file, with correct formatting upon selecting the exit option

------------
Main Menu
------------

Test that Each option on the "Commands" bar changes the display to the correct menu, except for quit which should exit the program.

------------
Buy Option
------------

  - Input for symbol is a currently existing symbol of the same investment type (results in the matching investment being updated with the new information and the name prompt will not show up)
  - Input for symbol is a currently existing symbol of the opposite investment type (results in an error message when buy is pressed)
  - Input for symbol / name is a valid non-empty string (results in input being accepted)
  - Input for symbol / name is empty (results in an error message when buy is pressed)
  - Input for quantity is a valid, positive integer (results in input being accepted)
  - Input for quantity is a valid, negative integer (results in an error message when buy is pressed)
  - Input for quantity is zero (results in an error message when buy is pressed)
  - Input for quantity is not a valid integer (results in an error message when buy is pressed)
  - Input for quantity is empty (results in an error message when buy is pressed)
  - Input for price is a valid, positive double (results in input being accepted)
  - Input for price is a valid, negative double (results in an error message when buy is pressed)
  - Input for price is zero (results in an error message when buy is pressed)
  - Input for price is not a valid double (results in an error message when buy is pressed)
  - Input for price is empty (results in an error message when buy is pressed)

  - Pressing "Reset" empties all of the text boxes
  - Pressing "Buy" creates a new investment and prints it out to the message box (assuming all input is accepted)
------------
Sell Option
------------
- If no investments exist, all buttons and input boxes are locked

- Input for symbol is a currently existing symbol (results in that symbol being sold)
- Input for symbol is not a currently existing symbol (results in an error message when sell is pressed)
- Input for symbol is empty (results in an error message when sell is pressed)
- Input for quantity is a valid, positive integer less than the current amount of units for the investment (results in input being accepted)
- Input for quantity is a valid, positive integer equal to the current amount of units for the investment (results in input being accepted and investment being removed from the list)
- Input for quantity is a valid, positive integer greater than the current amount of units for the investment (results in an error message when sell is pressed)
- Input for quantity is a valid, negative integer (results in an error message when sell is pressed)
- Input for quantity is zero (results in an error message when sell is pressed)
- Input for quantity is not a valid integer (results in an error message when sell is pressed)
- Input for quantity is empty (results in an error message when sell is pressed)
- Input for price is a valid, positive double (results in input being accepted)
- Input for price is a valid, negative double (results in user being re-prompted)
- Input for price is zero (results in an error message when sell is pressed)
- Input for price is not a valid double (results in an error message when sell is pressed)
- Input for price is empty (results in an error message when sell is pressed)

- Pressing "Reset" empties all of the text boxes
- Pressing "Sell" sells the specified investment and prints it out to the message box (assuming all input is accepted)

------------
Update Option
------------
- If no investments exist, all buttons and input boxes are locked

- Input for price is a valid, positive double (results in input being accepted and updated in the investment)
- Input for price is a valid, negative double (results in an error message when save is pressed)
- Input for price is zero (results in an error message when save is pressed)
- Input for price is not a valid double (results in an error message when save is pressed)
- Input for price is empty (results in an error message when save is pressed)

- "Prev" button should cycle to the previous investment, unless it is at the beginning of the list, in which case it should be disabled
- "Next" button should cycle to the next investment, unless it is at the end of the list, in which case it should be disabled
- If only 1 investment exists, both buttons should be disabled

- Pressing "Save" should update the price of the current investment

------------
Get Gain Option
------------

Make sure get gain returns the correct value.
To calculate:
add up each gain for all stocks and mutual funds
For each stock: multiply the stock quantity by the stock price and subtract the commission price (9.99) as well as the current book value
For each Fund: multiply the unit quantity by the unit price and subtract 45 as well as the current book value

Also check situations where no investments exist. Should be 0.0.
------------
Search Option
------------
- If no investments exist, all buttons and input boxes are locked

- Enter nothing for the four fields (results in all existing investments being printed)
- Enter a value for symbol only (results in printing the investment matching that symbol, or nothing if no match exists)
- Enter keywords with varying letter casing (should result in the same investments being printed each time, or nothing if no match exists)
- Enter keywords in different orders (should result in the same investments being printed each time, or nothing if no match exists)
- Enter the same value for low and high price (results in printing all investments with that exact price)
- Enter high price with no low price (results in printing all investments lower than or equal to the upper bound)
- Enter low price with no high price (results in printing all investments higher than or equal to the lower bound)
- Enter a price range with lower and upper bounds (results in printing all investments equal two or between the bounds)
- Enter a price range with a higher low price than high price (results in an error message when search is pressed)
- Input for either price is not a valid double (results in an error message when search is pressed)
- Input for either price is a valid, positive double (results in input being accepted)
- Input for either price is a valid, negative double (results in an error message when search is pressed)
- Input for either price is zero (results in a search being preformed)
- Input for either price is not a valid double (results in an error message when search is pressed)
- Input for either price is empty (results in a search being preformed)
- Test search again after removing an investment and confirm that results are still the same (minus the removed investment)

- Pressing "Reset" empties all of the text boxes
- Pressing "Search" will print out all matching investments based on search criteria (assuming all input is accepted)
------------
Quit Option
------------

Ensure that program saves the investments to the specified file correctly and then exits upon selecting quit


Future Improvements
================================================================================

- Make the code easier to read and modify
- Refactor
