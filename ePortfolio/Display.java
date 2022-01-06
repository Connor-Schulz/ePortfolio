package ePortfolio;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Represents and Controls the display for the portfolio class.
 * 
 * @author Connor Schulz (1103003)
 */
public class Display extends JFrame {

  public Display() {
    super();
    prepareGUI();
    showMenuBar();
  }

  // ==============================================================================
  // Constants
  // ==============================================================================

  static final int INPUT_LABEL_FONT_SIZE = 22; // Font size for labels

  static final Color guiDarkBlue = new Color(40, 50, 60);
  static final Color guiDarkGreen = new Color(25, 45, 43);
  static final Color guiDarkBrown = new Color(70, 60, 55);
  static final Color guiWhite = new Color(220, 220, 230);

  // ==============================================================================
  // Global Variables
  // ==============================================================================

  // Holds the long message displayed to the user on the program start
  String openingMessage = "\n\n\nWelcome to ePortfolio.\n\n\nChoose a command from the \"Commands\" menu to buy or sell an investment, update prices for all investmentments, get gain for the portfolio, search for relevant investments, or quit the program.";

  // Portfolio object to access Portfolio functions
  private Portfolio portfolioFunctions = new Portfolio();

  JTextField inputField1, inputField2, inputField3, inputField4; // Text fields shared between various menu options
  JPanel controlPanel, dynamicPanel; // The two main panels in the window
  JButton prevButton, nextButton; // prev/next buttons for the update menu
  JScrollPane messageScrollBars; // Scroll pane that the message box sits in
  int updateCycleLocation = 0; // Holds the index of the current investment for the update window
  JComboBox<String> comboBox; // Combobox for the buy menu
  JTextArea messageBox; // The message box for displaying information to the user

  // ==============================================================================
  // Menu Option Listeners
  // ==============================================================================

  // Change display to buy menu
  private class BuyListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showBuyMenu();
    }
  }

  // Change display to sell menu
  private class SellListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showSellMenu();
    }
  }

  // Change display to update menu
  private class UpdateListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      updateCycleLocation = 0; // Reset cycle to 0
      showUpdateMenu();
      if (portfolioFunctions.getInvestListSize() != 0) {
        setUpdateBoxes(updateCycleLocation);
      }
    }
  }

  // Change display to gain menu
  private class GetGainListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showGainMenu();
    }
  }

  // Change display to search menu
  private class SearchListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      showSearchMenu();
    }
  }

  // Exit the program
  private class QuitListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      portfolioFunctions.fileSave(portfolioFunctions.getFileName());
      System.exit(0);
    }
  }

  // ==============================================================================
  // Support Button Listeners
  // ==============================================================================

  // Resets text boxes
  private class ResetListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      resetTextFields();
    }
  }

  // Cycles to the next investment
  private class NextListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      updateCycleLocation++;
      setUpdateBoxes(updateCycleLocation);

      // Disable/enable next/prev buttons if they reach the end of the list
      if (updateCycleLocation == portfolioFunctions.getInvestListSize() - 1) {
        nextButton.setEnabled(false);
      }
      if (updateCycleLocation > 0) {
        prevButton.setEnabled(true);
      }
    }
  }

  // Cycles to the previous investment
  private class PrevListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      updateCycleLocation--;
      setUpdateBoxes(updateCycleLocation);

      // Disable/enable next prev buttons if they reach the end of the list
      if (updateCycleLocation == 0) {
        prevButton.setEnabled(false);
      }
      if (updateCycleLocation < portfolioFunctions.getInvestListSize()) {
        nextButton.setEnabled(true);
      }

    }
  }

  // ==============================================================================
  // Main Button Functions
  // ==============================================================================

  // Logic for Buy button in buy menu
  private class BuyInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the info out of all of the boxes
      String type = comboBox.getSelectedItem().toString();
      String symbol = inputField1.getText();
      String name = inputField2.getText();
      String quantity = inputField3.getText();
      String price = inputField4.getText();

      // Validate symbol
      if (portfolioFunctions.validateString(symbol, true) == false) {
        errorMessage += "ERROR: symbol is a required field and cannot be empty.\n\n";
      }

      // Validate name (required only if symbol is unique to the portfolio)
      if (portfolioFunctions.validateString(name, true) == false && portfolioFunctions.findInvestment(symbol) == -1) {
        errorMessage += "ERROR: name is a required field for new investments and cannot be empty.\n\n";
      }

      // Validate quantity
      if (portfolioFunctions.validateInt(quantity, true) == false) {
        errorMessage += "ERROR: quantity is a required field and must be a valid, positive integer.\n\n";
      } else if (Integer.parseInt(quantity) < 1) {
        errorMessage += "ERROR: quantity must be a positive integer greater than 0.\n\n";
      }

      // Validate price
      if (portfolioFunctions.validateDouble(price, true) == false) {
        errorMessage += "ERROR: price is a required field and must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "ERROR: price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Set text to error message

      // If no errors are found, run the desired function and update the text with the
      // return
      if (errorMessage.equals("")) {
        if (name.equals("")) {
          name = " ";
        }
        messageBox
            .setText(portfolioFunctions.buy(type, symbol, name, Integer.parseInt(quantity), Double.parseDouble(price)));
      }
    }
  }

  // Logic for Sell button in sell menu
  private class SellInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the info out of all of the boxes
      String symbol = inputField1.getText();
      String quantity = inputField2.getText();
      String price = inputField3.getText();

      // Validate symbol
      if (portfolioFunctions.validateString(symbol, true) == false) {
        errorMessage += "ERROR: symbol is a required field and cannot be empty.\n\n";
      }

      // Validate quantity
      if (portfolioFunctions.validateInt(quantity, true) == false) {
        errorMessage += "ERROR: quantity is a required field and must be a valid, positive integer.\n\n";
      } else if (Integer.parseInt(quantity) < 1) {
        errorMessage += "ERROR: quantity must be a positive integer greater than 0.\n\n";
      }

      // Validate price
      if (portfolioFunctions.validateDouble(price, true) == false) {
        errorMessage += "ERROR: price is a required field and must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "ERROR: price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Set text to error message

      // If no errors are found, run the desired function and update the text with the
      // return
      if (errorMessage.equals("")) {
        messageBox.setText(portfolioFunctions.sell(symbol, Integer.parseInt(quantity), Double.parseDouble(price)));
      }

    }
  }

  // Logic for Save button in update menu
  private class UpdateInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the new price
      String price = inputField3.getText();

      // Validate price
      if (portfolioFunctions.validateDouble(price, true) == false) {
        errorMessage += "ERROR: price is a required field and must be a valid, positive double.\n\n";
      } else if (Double.parseDouble(price) <= 0) {
        errorMessage += "ERROR: price must be a positive double greater than 0.\n\n";
      }

      messageBox.setText(errorMessage); // Set text to error message

      // If no errors are found, run the desired function and update the text with the
      // return
      if (errorMessage.equals("")) {
        messageBox.setText(portfolioFunctions.update(portfolioFunctions.getInvestment(updateCycleLocation),
            Double.parseDouble(price)));
      }

    }
  }

  // Logic for Search button in search menu
  private class SearchInvestmentListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String errorMessage = "";

      // Get the info out of all of the boxes
      String symbol = inputField1.getText();
      String nameKeys = inputField2.getText();
      String lowPrice = inputField3.getText();
      String highPrice = inputField4.getText();

      // Validate low price
      if (portfolioFunctions.validateDouble(lowPrice, false) == false) {
        errorMessage += "ERROR: low price must be a valid, positive double.\n\n";
      } else if (!lowPrice.equals("") && Double.parseDouble(lowPrice) < 0) {
        errorMessage += "ERROR: low price must be a positive double greater than or equal to 0.\n\n";
      }

      // Validate high price
      if (portfolioFunctions.validateDouble(highPrice, false) == false) {
        errorMessage += "ERROR: high price must be a valid, positive double.\n\n";
      } else if (!highPrice.equals("") && Double.parseDouble(highPrice) < 0) {
        errorMessage += "ERROR: high price must be a positive double greater than or equal to 0.\n\n";
      }

      // Validate that highPrice is higher or equal to low price
      if (portfolioFunctions.validateDouble(lowPrice, true) == true
          && portfolioFunctions.validateDouble(highPrice, true) == true) {
        if (Double.parseDouble(highPrice) < Double.parseDouble(lowPrice)) {
          errorMessage += "ERROR: high price must be greater or equal to low price.\n\n";
        }
      }

      messageBox.setText(errorMessage); // Set text to error message

      // Set high/low price to "-1" if it is not specified
      if (lowPrice.equals("")) {
        lowPrice = "-1";
      }
      if (highPrice.equals("")) {
        highPrice = "-1";
      }

      // If no errors are found, run the desired function and update the text with the
      // return
      if (errorMessage.equals("")) {
        messageBox.setText(
            portfolioFunctions.search(symbol, nameKeys, Double.parseDouble(lowPrice), Double.parseDouble(highPrice)));
      }
    }
  }

  // ==============================================================================
  // Gui Setup
  // ==============================================================================

  /** Creates the main window for the GUI */
  private void prepareGUI() {
    // Setup the window
    setTitle("ePortfolio");
    setSize(900, 600);
    setLayout(new BorderLayout());
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Setup panels
    controlPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBrown);
    dynamicPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBrown);

    showMainMenu();

    // add panels to window
    add(dynamicPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.NORTH);

  }

  /** Creates the menu button bar for the GUI */
  private void showMenuBar() {
    /*
     * JLabel commandLabel = new JLabel("Commands");
     * commandLabel.setFont(new Font("monospaced", Font.PLAIN, 22));
     * commandLabel.setForeground(Color.white);
     * commandLabel.setBorder(new EmptyBorder(0, 10, 10, 0));
     */
    // setUp button panel
    JPanel backPanel = new JPanel(new BorderLayout());
    backPanel.setBackground(guiDarkBlue);
    JPanel panel = new JPanel(new GridLayout(1, 6, 20, 10));
    panel.setBackground(guiDarkBlue);
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // panel.setBorder(new EmptyBorder(10, 10, -1, 10));

    // 8 function buttons
    JButton buyButton = new JButton("Buy");
    BuyListener buyListener = new BuyListener();
    buyButton.addActionListener(buyListener);

    JButton sellButton = new JButton("Sell");
    SellListener sellListener = new SellListener();
    sellButton.addActionListener(sellListener);

    JButton updateButton = new JButton("Update");
    UpdateListener updateListener = new UpdateListener();
    updateButton.addActionListener(updateListener);

    JButton getGainButton = new JButton("Get Gain");
    GetGainListener getGainListener = new GetGainListener();
    getGainButton.addActionListener(getGainListener);

    JButton searchButton = new JButton("Search");
    SearchListener searchListener = new SearchListener();
    searchButton.addActionListener(searchListener);

    JButton quitButton = new JButton("Quit");
    QuitListener quitListener = new QuitListener();
    quitButton.addActionListener(quitListener);

    // adding buttons to panel
    panel.add(buyButton);
    panel.add(sellButton);
    panel.add(updateButton);
    panel.add(getGainButton);
    panel.add(searchButton);
    panel.add(quitButton);

    // adding panel to control panel
    backPanel.add(formatLabel(new JLabel("Commands"), 26), BorderLayout.WEST);
    backPanel.add(panel, BorderLayout.CENTER);
    controlPanel.add(backPanel, BorderLayout.CENTER);
  }

  // ==============================================================================
  // Menu Option Graphics
  // ==============================================================================

  /** Displays the main menu */
  private void showMainMenu() {
    modifyScrollMessageBox(openingMessage, 16, -1);

    dynamicPanel.removeAll();
    dynamicPanel.add(messageScrollBars, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /** Displays the buy menu */
  private void showBuyMenu() {
    // Create panels
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(5, 2, 0, 10)), -1, -1, guiDarkGreen);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiDarkGreen);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);

    // Create the combo box
    String[] comboBoxOptions = { "stock", "mutualfund" };
    comboBox = new JComboBox<>(comboBoxOptions);

    // Create text fields
    inputField1 = new JTextField(); // Symbol
    inputField2 = new JTextField(); // Name
    inputField3 = new JTextField(); // Quantity
    inputField4 = new JTextField(); // Price

    // Reset button
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, true);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Buy button
    JButton buyButton = formatButton(new JButton("Buy"), 100, 150, true);
    BuyInvestmentListener buyListener = new BuyInvestmentListener();
    buyButton.addActionListener(buyListener);

    // Modify borders of the button panel so buttons fit better
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to fit properly
    modifyScrollMessageBox("", 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Type"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(comboBox);
    inputPanel.add(formatLabel(new JLabel("Symbol"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Name"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Quantity"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField3);
    inputPanel.add(formatLabel(new JLabel("Price"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField4);

    // Add all the buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(buyButton);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel
    backPanel.add(formatLabel(new JLabel("Buying an investment"), 22), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /** Displays the sell menu */
  private void showSellMenu() {
    boolean investmentsExist = true; // true if investments exist
    String boxMessage = ""; // message to display in message box

    // Check if investments exist
    if (portfolioFunctions.getInvestListSize() == 0) {
      investmentsExist = false;
      boxMessage = "- No investments exist -";
    }

    // Create panels
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(3, 2, 0, 30)), -1, -1, guiDarkGreen);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiDarkGreen);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);

    // Create text fields
    inputField1 = createTextField(investmentsExist); // Symbol
    inputField2 = createTextField(investmentsExist); // Quantity
    inputField3 = createTextField(investmentsExist); // Price

    // Reset Button
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, investmentsExist);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Sell Button
    JButton sellButton = formatButton(new JButton("Sell"), 100, 150, investmentsExist);
    SellInvestmentListener sellListener = new SellInvestmentListener();
    sellButton.addActionListener(sellListener);

    // Modify borders of the button panel so buttons fit better
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Quantity"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Price"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField3);

    // Add all the buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(sellButton);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel
    backPanel.add(formatLabel(new JLabel("Selling an investment"), 22), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /** Displays the update menu */
  private void showUpdateMenu() {
    boolean investmentsExist = true; // true if investments exist
    String boxMessage = ""; // message to display in message box

    // Check if investments exist
    if (portfolioFunctions.getInvestListSize() == 0) {
      investmentsExist = false;
      boxMessage = "- No investments exist -";
    }

    // Create panels
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(3, 2, 0, 30)), -1, -1, guiDarkGreen);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(3, 1, 10, 15)), -1, 300, guiDarkGreen);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);

    // Create text fields
    inputField1 = createTextField(false); // Symbol
    inputField2 = createTextField(false); // Name
    inputField3 = createTextField(investmentsExist); // Price

    // Previous Button
    prevButton = formatButton(new JButton("Prev"), 100, 150,
        investmentsExist && portfolioFunctions.getInvestListSize() > 1 && updateCycleLocation != 0);
    PrevListener prevListener = new PrevListener();
    prevButton.addActionListener(prevListener);

    // Next Button
    nextButton = formatButton(new JButton("Next"), 100, 150,
        investmentsExist && portfolioFunctions.getInvestListSize() > 1);
    NextListener nextListener = new NextListener();
    nextButton.addActionListener(nextListener);

    // Save Button
    JButton saveButton = formatButton(new JButton("Save"), 100, 150, investmentsExist);
    UpdateInvestmentListener updateListener = new UpdateInvestmentListener();
    saveButton.addActionListener(updateListener);

    // Modify borders of the button panel so buttons fit better
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Name"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Price"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField3);

    // Add all the buttons to the button panel
    buttonPanel.add(prevButton);
    buttonPanel.add(nextButton);
    buttonPanel.add(saveButton);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Messages"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel
    backPanel.add(formatLabel(new JLabel("Updating investments"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /** Displays the get gain menu */
  private void showGainMenu() {
    String boxMessage = portfolioFunctions.getIndividualGain();

    // Check if investments exist
    if (portfolioFunctions.getInvestListSize() == 0) {
      boxMessage = "- No investments exist -";
    }

    // Create panels
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(1, 2, 0, 30)), -1, -1, guiDarkGreen);
    JPanel fillerPanel = formatPanel(new JPanel(new GridLayout(1, 1)), 100, 350, guiDarkGreen);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);

    // Create text fields
    inputField1 = createTextField(false); // Total Gain
    inputField1.setText(portfolioFunctions.getGain());

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 300);

    // Add all input boxes and labels to the input panel

    inputPanel.add(formatLabel(new JLabel("Total gain"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField1);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Individual gains"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel
    backPanel.add(formatLabel(new JLabel("Getting total gain"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(fillerPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  /** Displays the search menu */
  private void showSearchMenu() {
    boolean investmentsExist = true; // true if investments exist
    String boxMessage = ""; // message to display in message box

    // Check if investments exist
    if (portfolioFunctions.getInvestListSize() == 0) {
      investmentsExist = false;
      boxMessage = "- No investments exist -";
    }

    // Create panels
    JPanel backPanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);
    JPanel inputPanel = formatPanel(new JPanel(new GridLayout(4, 2, 0, 20)), -1, -1, guiDarkGreen);
    JPanel buttonPanel = formatPanel(new JPanel(new GridLayout(2, 1, 10, 30)), -1, 300, guiDarkGreen);
    JPanel messagePanel = formatPanel(new JPanel(new BorderLayout()), -1, -1, guiDarkBlue);

    // Create text fields
    inputField1 = createTextField(investmentsExist); // Symbol
    inputField2 = createTextField(investmentsExist); // Name Keywords
    inputField3 = createTextField(investmentsExist); // Low Price
    inputField4 = createTextField(investmentsExist);
    ; // High Price

    // Reset button
    JButton resetButton = formatButton(new JButton("Reset"), 100, 150, investmentsExist);
    ResetListener resetListener = new ResetListener();
    resetButton.addActionListener(resetListener);

    // Search button
    JButton searchButton = formatButton(new JButton("Search"), 100, 150, investmentsExist);
    SearchInvestmentListener searchListener = new SearchInvestmentListener();
    searchButton.addActionListener(searchListener);

    // Modify borders of the button panel so buttons fit better
    buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Modify the scroll box to fit properly
    modifyScrollMessageBox(boxMessage, 16, 180);

    // Add all input boxes and labels to the input panel
    inputPanel.add(formatLabel(new JLabel("Symbol"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField1);
    inputPanel.add(formatLabel(new JLabel("Name keywords"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField2);
    inputPanel.add(formatLabel(new JLabel("Low price"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField3);
    inputPanel.add(formatLabel(new JLabel("High price"), INPUT_LABEL_FONT_SIZE));
    inputPanel.add(inputField4);

    // Add all the buttons to the button panel
    buttonPanel.add(resetButton);
    buttonPanel.add(searchButton);

    // Add labels and the scroll box to the message panel
    messagePanel.add(formatLabel(new JLabel("Search results"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    messagePanel.add(messageScrollBars, BorderLayout.CENTER);

    // Add panels to backPanel
    backPanel.add(formatLabel(new JLabel("Searching investments"), INPUT_LABEL_FONT_SIZE), BorderLayout.NORTH);
    backPanel.add(inputPanel, BorderLayout.CENTER);
    backPanel.add(buttonPanel, BorderLayout.EAST);
    backPanel.add(messagePanel, BorderLayout.SOUTH);

    // Remove all panels from the dynamic panel, and add the new ones
    dynamicPanel.removeAll();
    dynamicPanel.add(backPanel, BorderLayout.CENTER);
    dynamicPanel.revalidate();
  }

  // ==============================================================================
  // Help Functions
  // ==============================================================================

  /**
   * Formats a J label based on passed values
   * 
   * @param label    is the label to modify
   * @param fontSize is the fontsize of the JLabel
   * @return the formatted JLabel
   */
  private JLabel formatLabel(JLabel label, int fontSize) {
    label.setFont(new Font("Courier", Font.PLAIN, fontSize));
    label.setForeground(guiWhite);
    label.setBorder(new EmptyBorder(0, 10, 10, 10));
    return label;
  }

  /**
   * Changes the message box based on passed values
   * 
   * @param message  displayed in the box
   * @param fontSize is the fontsize of the message box
   * @param height   is the height in pixels of the message box
   */
  private void modifyScrollMessageBox(String message, int fontSize, int height) {
    messageBox = new JTextArea(message);
    messageScrollBars = new JScrollPane(messageBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    messageBox.setBackground(guiDarkGreen);
    messageBox.setForeground(guiWhite);
    messageBox.setLineWrap(true);
    messageBox.setWrapStyleWord(true);
    messageBox.setFont(new Font("Courier", Font.PLAIN, fontSize));
    messageBox.setBorder(new EmptyBorder(10, 10, 10, 10));
    messageBox.setEditable(false);

    if (height > 0) {
      messageScrollBars.setPreferredSize(new Dimension(800, height));
    }

  }

  /**
   * Displays investment information for an investment in the non editable boxes
   * in the update menu
   * 
   * @param index the index of the investment to display
   */
  private void setUpdateBoxes(int index) {
    Investment investment = portfolioFunctions.getInvestment(index);
    inputField1.setText(investment.getSymbol());
    inputField2.setText(investment.getName());
  }

  /** Resets all four text fields to empty */
  private void resetTextFields() {
    inputField1.setText("");
    inputField2.setText("");
    inputField3.setText("");
    inputField4.setText("");
  }

  /**
   * Returns a new JTextField
   * 
   * @param editable is true if the user can edit the text field
   * @return returns the new text field
   */
  private JTextField createTextField(boolean editable) {
    JTextField field = new JTextField();
    if (editable == false) {
      field.setEditable(false);
      field.setForeground(Color.white);
      field.setBackground(Color.darkGray);
      field.setText("");
    }
    return field;
  }

  /**
   * Formats a JPanel based on passed values
   * 
   * @param panel  is the panel to format
   * @param height is the height in pixels of the panel
   * @param width  is the width in pixels of the panel
   * @param color  is the color of the panel
   * @return the formatted JPanel
   */
  private JPanel formatPanel(JPanel panel, int height, int width, Color color) {
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    panel.setBackground(color);

    if (width > 0 && height > 0) {
      panel.setPreferredSize(new Dimension(width, height));
    }
    return panel;
  }

  /**
   * Formats a JButton based on passed values
   * 
   * @param button  is the button to format
   * @param height  is the height in pixels of the button
   * @param width   is the width in pixels of the button
   * @param enabled true if the button should be enabled
   * @return the formatted JButton
   */
  private JButton formatButton(JButton button, int height, int width, boolean enabled) {
    button.setBorder(new EmptyBorder(10, 10, 10, 10));
    button.setEnabled(enabled);

    if (width > 0 && height > 0) {
      button.setPreferredSize(new Dimension(width, height));
    }
    return button;
  }

}
