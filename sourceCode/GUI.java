package sourceCode;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI implements ActionListener {
   private static final int WINDOW_WIDTH = 1100;
   private static final int WINDOW_HEIGHT = 800;
   private static final int LABEL_HEIGHT = 40;
   private static final int LABEL_WIDTH = 250;
   private static final int BUT_WIDTH = 250;
   private static final int BUT_HEIGHT = 40;
   private static final int DROP_HEIGHT = 40;
   private static final int DROP_WIDTH = 250;
   private List<String> urlDropList;
   private List<String> dbDropList;
   private DatabaseReader db;
   JFrame window = new JFrame("SQL CLIENT APPLICATION - (MJL - CNT 4714 - SPRING 2025 - PROJECT 3)");
   JPanel topSection = new JPanel();
   JPanel topRight = new JPanel();
   JPanel topLeft = new JPanel();
   JPanel botSection = new JPanel();
   private final JLabel connectionTitle = new JLabel("Connection Details");
   private final JLabel blankFiller = new JLabel("");
   private final JLabel sqlTitle = new JLabel("Enter an SQL Command");
   private final JLabel urlPropLabel = new JLabel("DB URL Properties");
   private final JLabel userPropLabel = new JLabel("Users");
   private final JLabel usernameLabel = new JLabel("Username");
   private final JLabel passwordLabel = new JLabel("Password");
   private static JTextField usernameText = new JTextField();
   private static JTextField passwordText = new JTextField();
   private JTable resultTable;
   private JScrollPane tableScrollPane;

   private JScrollPane scrollPane;
   private static JTextArea sqlCmdText = new JTextArea();
   JComboBox<String> urlDrop = new JComboBox();
   JComboBox<String> userDrop = new JComboBox();
   private static JLabel connectionLabel = new JLabel("NO CONNECTION ESTABLISHED");
   private static JLabel resultLabel = new JLabel("SQL Execution Result Window");
   private static JTextArea resultArea = new JTextArea("");
   private static final JLabel botTitle = new JLabel("NO CONNECTIONS ESTABLISHED");
   JButton connectBut = new JButton("Connect to Database");
   JButton disconnectBut = new JButton("Disconnect From Database");
   JButton clearSQLBut = new JButton("Clear SQL Command");
   JButton executeSQLBut = new JButton("Execute SQL Command");
   JButton clearResBut = new JButton("Clear Results Window");
   JButton exitAppBut = new JButton("Close application");

   public GUI(DatabaseReader initDB) {
      this.db = initDB;
   }

   // Init global vars
   String selectedUser, selectedURL, enteredUser, enteredPass;

   public void restartVars(){
      selectedUser = "";
      selectedURL = "";
      enteredUser = "";
      enteredPass = "";
   }

   public void populateUserDrop() {
      try {
         this.urlDropList = this.db.getUsers();
         userDrop.removeAllItems(); // Clear existing items
         for (String user : urlDropList) {
            userDrop.addItem(user);
         }
      } catch (SQLException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this.window, "Error fetching users from database.");
      }

   }

   public void populateDatabaseDrop() {
      try {
         List<String> databases = this.db.getDatabases();  // Fetch list of databases

         this.urlDrop.removeAllItems();  // Clear dropdown before adding new items

         for (String db : databases) {
            this.urlDrop.addItem(db);  // Add each database to the dropdown
         }
      } catch (SQLException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(this.window, "Error fetching databases from database.");
      }
   }


   public void createWindow() {
      this.window.setSize(1100, 800);
      this.window.setDefaultCloseOperation(3);
      this.window.setResizable(true);
      this.populateUserDrop();
      this.populateDatabaseDrop();

      DefaultTableModel tableModel = new DefaultTableModel();
      resultTable = new JTable(tableModel); // Initialize JTable with DefaultTableModel
      tableScrollPane = new JScrollPane(resultTable); // Add JScrollPane to allow scrolling

      Container var1 = this.window.getContentPane();
      var1.setLayout(new GridLayout(2, 1));
      var1.add(this.topSection);
      var1.add(this.botSection);
      this.topSection.setLayout(new GridLayout(1, 2));
      this.topSection.add(this.topLeft);
      this.topSection.add(this.topRight);
      this.topRight.setBackground(Color.LIGHT_GRAY);

      // Top left
      this.topLeft.add(this.connectionTitle);
      this.topLeft.add(this.blankFiller);
      this.topLeft.add(this.urlPropLabel);
      this.topLeft.add(this.urlDrop);
      this.topLeft.add(this.userPropLabel);
      this.topLeft.add(this.userDrop);
      this.topLeft.add(this.usernameLabel);
      this.topLeft.add(usernameText);
      this.topLeft.add(this.passwordLabel);
      this.topLeft.add(passwordText);
      this.topLeft.add(this.connectBut);
      this.topLeft.add(this.disconnectBut);

      // Setting sizes
      this.connectionTitle.setPreferredSize(new Dimension(500, 30));
      this.urlPropLabel.setPreferredSize(new Dimension(250, 40));
      this.userPropLabel.setPreferredSize(new Dimension(250, 40));
      this.usernameLabel.setPreferredSize(new Dimension(250, 40));
      usernameText.setPreferredSize(new Dimension(250, 40));
      this.passwordLabel.setPreferredSize(new Dimension(250, 40));
      passwordText.setPreferredSize(new Dimension(250, 40));
      this.connectBut.setPreferredSize(new Dimension(250, 40));
      this.disconnectBut.setPreferredSize(new Dimension(250, 40));
      this.userDrop.setPreferredSize(new Dimension(250, 40));
      this.urlDrop.setPreferredSize(new Dimension(250, 40));

      // Top right
      this.topRight.add(this.sqlTitle);
      this.topRight.add(sqlCmdText);
      this.topRight.add(this.clearSQLBut);
      this.topRight.add(this.executeSQLBut);

      // Seting sizes
      this.sqlTitle.setPreferredSize(new Dimension(500, 30));
      sqlCmdText.setPreferredSize(new Dimension(500, 200));
      this.clearSQLBut.setPreferredSize(new Dimension(250, 40));
      this.executeSQLBut.setPreferredSize(new Dimension(250, 40));

      // Setting colors
      this.connectionTitle.setForeground(Color.BLUE);
      this.sqlTitle.setForeground(Color.BLUE);
      this.clearSQLBut.setForeground(Color.RED);
      this.executeSQLBut.setForeground(Color.GREEN);
      this.botSection.setBackground(Color.GRAY);

      // Adding bottom section
      this.botSection.add(connectionLabel);
      this.botSection.add(resultLabel);
      this.botSection.add(tableScrollPane);

      //this.botSection.add(resultArea);
      this.botSection.add(this.clearResBut);
      this.botSection.add(this.exitAppBut);


      // Setting sizes
      connectionLabel.setPreferredSize(new Dimension(950, 30));
      resultLabel.setPreferredSize(new Dimension(545, 30));
      //resultArea.setPreferredSize(new Dimension(1000, 250));
      tableScrollPane.setPreferredSize(new Dimension(1000, 250));

      this.clearResBut.setPreferredSize(new Dimension(250, 40));
      this.exitAppBut.setPreferredSize(new Dimension(250, 40));

      // Setting colors
      connectionLabel.setOpaque(true);
      connectionLabel.setBackground(Color.LIGHT_GRAY);
      connectionLabel.setForeground(Color.RED);
      connectionLabel.setFont(new Font(botTitle.getFont().getName(), 1, 20));
      connectionLabel.setHorizontalAlignment(0);
      resultLabel.setHorizontalAlignment(0);
      resultArea.setEditable(false);


      this.connectBut.addActionListener(this);
      this.clearSQLBut.addActionListener(this);
      this.executeSQLBut.addActionListener(this);
      this.disconnectBut.addActionListener(this);
      this.clearResBut.addActionListener(this);
      this.exitAppBut.addActionListener(this);
      this.window.setVisible(true);
   }


   public void actionPerformed(ActionEvent e) {
      // Check if the source of the event is the clearSQLBut button
      if (e.getSource() == clearSQLBut) {
         sqlCmdText.setText("");
      }

      if (e.getSource() == exitAppBut) {
         System.exit(0);  // Exit the application
         this.window.dispose();  // Close the window
      }

      if (e.getSource() == clearResBut) {
         resultArea.setText("");  // Clear results window
      }

      if (e.getSource() == disconnectBut) {
         // Clear the variables
         resultArea.setText("");  // Clear results window


         if (selectedURL == null || selectedURL.isEmpty()) {
            connectionLabel.setText("You are not connected to anything");
         } else{
            connectionLabel.setText("Disconnected from: " + selectedURL);
            restartVars();
            connectionLabel.setForeground(Color.RED);
         }
      }

      if (e.getSource() == connectBut) {
         selectedUser = (String) userDrop.getSelectedItem();
         selectedURL = (String) urlDrop.getSelectedItem();
         enteredUser = usernameText.getText();
         enteredPass = passwordText.getText();

         try {
            // Validate login credentials using checkIfLoginValid method
            String validationResult = db.checkIfLoginValid(selectedUser, selectedURL, enteredUser, enteredPass);

            if (validationResult.equals("SUCCESS")) {
               // If login is valid, update connection label and set color to green
               String fullURL = db.getFullURL(selectedURL);
               connectionLabel.setText("CONNECTED TO: " + fullURL);
               connectionLabel.setForeground(Color.GREEN);
            } else {
               // If login is invalid, show specific error message
               connectionLabel.setText("CONNECTION FAILED");
               connectionLabel.setForeground(Color.RED);
               JOptionPane.showMessageDialog(window, validationResult, "Login Error", JOptionPane.ERROR_MESSAGE);
               restartVars();
            }
         } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(window, "Database Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
         }
      }

      // Assuming you have a JTable named resultTable in your GUI
      if (e.getSource() == executeSQLBut) {
         // Retrieve the SQL query from the text area
         String sqlQuery = sqlCmdText.getText().trim();

         // Ensure the query is not empty
         if (sqlQuery.isEmpty()) {
            JOptionPane.showMessageDialog(window, "SQL Command cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         // Ensure a connection exists before executing SQL
         if (selectedURL == null || selectedURL.isEmpty()) {
            JOptionPane.showMessageDialog(window, "No database connection established.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         try {
            // Execute the query and fetch the result as a DefaultTableModel
            DefaultTableModel tableModel = db.executeSQL(sqlQuery);

            // Set the table model to the resultTable
            resultTable.setModel(tableModel);

         } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(window, "SQL Execution Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         }
      }

   }
}