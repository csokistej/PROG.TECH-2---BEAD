/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager.controller;

import accountmanager.entity.Account;
import accountmanager.entity.CollectorAccount;
import accountmanager.entity.Customer;
import accountmanager.entity.Transaction;
import accountmanager.view.AccountManagerView;
import accountmanager.view.model.GenericTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author doobs
 */
public class AccountManagerController {
 
    private final AccountManagerView view;
    final GenericTableModel<Customer> customersTableModel = new GenericTableModel(Customer.COLUMN_NAMES);
    private Connection connection;
    private Properties properties;
    private CollectorAccount collectorAccount;
    private LoginController loginController;
    
    //QUERIES
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/accountmanager";
    private static final String GET_ALL_CUSTOMER_SQL = "SELECT * FROM customers";
    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, ADDRESS, PHONE) VALUES(?,?,?,?)";
    private static final String GET_ACCOUNTS_BY_CUSTOMER_ID_SQL = "SELECT * FROM ACCOUNTS WHERE CUSTOMER_ID = ?";
    private static final String GET_ALL_TRANSACTIONS_BY_ACCOUNT_NUMBER_SQL = "SELECT * FROM TRANSACTIONS WHERE SOURCE = ? OR TARGET = ?";
//    private static final String GET_OUTCOME_TRANSACTIONS_BY_ACCOUNT_ID_SQL = "SELECT * FROM TRANSACTIONS WHERE SOURCE = (SELECT ID FROM ACCOUNTS WHERE ACCOUNT_ID = ?)";
//    private static final String GET_INCOME_TRANSACTIONS_BY_ACCOUNT_ID_SQL = "SELECT * FROM TRANSACTIONS WHERE TARGET = (SELECT ID FROM ACCOUNTS WHERE ACCOUNT_ID = ?)";
    private static final String BOOK_TRANSACTION_BY_ID_SQL = "UPDATE TRANSACTIONS SET BOOKED = TRUE WHERE ID = ?";
    private static final String INSERT_NEW_TRANSACTION_SQL = "INSERT INTO TRANSACTIONS (SOURCE, TARGET, AMOUNT, BOOKED, DATE) VALUES(?,?,?,?,?)";
    private static final String GET_ALL_TRANSACTIONS_SQL = "SELECT * FROM TRANSACTIONS WHERE BOOKED = FALSE";
    private static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_NUMBER = ?";
    private static final String UPDATE_ACCOUNT_BEFORE_ACTION_BY_ACCOUNT_NUMBER_SQL = "UPDATE ACCOUNTS SET BALANCE = ? WHERE ACCOUNT_NUMBER = ?";
    private static final String DELETE_TRANSACTION_BY_ID_SQL = "DELETE FROM TRANSACTIONS WHERE ID = ?";
    private static final String UPDATE_TARGET_BEFORE_BOOKING_BY_ACCOUNT_NUMBER_SQL = "UPDATE ACCOUNTS SET BALANCE = ? WHERE NUMBER = ?";
    private static final String GET_ALL_ACCOUNTS_SQL = "SELECT * FROM ACCOUNTS";
    private static final String GET_ACCOUNT_BY_ID_SQL = "SELECT * FROM ACCOUNTS WHERE ID = ?";
    private static final String GET_TRANSACTION_BY_ID_SQL = "SELECT * FROM TRANSACTIONS WHERE ID = ?";
    
    
    public AccountManagerController(AccountManagerView view) {
        this.view = view;
        properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");   //vaskos a védelem :)
        collectorAccount = new CollectorAccount(1 , "99999999-99999999-99999999", 66666666 );
    }

    public CollectorAccount getCollectorAccount() {
        return collectorAccount;
    }
    
    
    public Customer fromCustomerResultSet(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultSet.getInt("ID"));
        customer.setFirstName(resultSet.getString("FIRST_NAME"));
        customer.setLastName(resultSet.getString("LAST_NAME"));
        customer.setAddress(resultSet.getString("ADDRESS"));
        customer.setPhone(resultSet.getString("PHONE"));
        return customer;
    }
    
    public Account fromAccountResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getInt("ID"));
        account.setNumber(resultSet.getString("ACCOUNT_NUMBER"));
        account.setBalance(resultSet.getInt("BALANCE"));
        account.setLocked(resultSet.getBoolean("LOCKED"));
        account.setCustomerId(resultSet.getInt("CUSTOMER_ID"));
        account.setDate(resultSet.getString("DATE"));
        return account;
    }
    
    public Transaction fromTransactionResultSet(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getInt("ID"));
        transaction.setSource(resultSet.getString("SOURCE"));
        transaction.setTarget(resultSet.getString("TARGET"));
        transaction.setAmount(resultSet.getInt("AMOUNT"));
        transaction.setBooked(resultSet.getBoolean("BOOKED"));
        transaction.setDate(resultSet.getString("DATE"));
        return transaction;
    }
    
    public Statement fromCustomerEntity(String query, Customer entity, boolean withId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getAddress());
        statement.setString(4, entity.getPhone());
        if (withId) {
            statement.setInt(5, entity.getId());
        }
        return statement;
    }
    
    public Statement fromAccountEntity(String query, Account entity, boolean withId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getNumber());
        statement.setInt(2, entity.getBalance());
        statement.setBoolean(3, entity.isLocked());
        statement.setInt(4, entity.getCustomerId());
        statement.setString(5, entity.getDate());
        if (withId) {
            statement.setInt(6, entity.getId());
        }
        return statement;
    }
    
    public Statement fromTransactionEntity(String query, Transaction entity, boolean withId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getSource());
        statement.setString(2, entity.getTarget());
        statement.setInt(3, entity.getAmount());
        statement.setBoolean(4, entity.isBooked());
        statement.setString(5, entity.getDate());
        if (withId) {
            statement.setInt(6, entity.getId());
        }
        return statement;
    }
    
    public List<Customer> getAllCustomers(){
        List<Customer> items = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ALL_CUSTOMER_SQL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(fromCustomerResultSet(resultSet));
            }
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return items;
        }
    }
    
    public List<Account> getAllAccounts(){
       List<Account> items = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ALL_ACCOUNTS_SQL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(fromAccountResultSet(resultSet));
            }
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return items;
        }  
    }
    
    public List<Account> getAccountsByCustomerId(int id){
        List<Account> items = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ACCOUNTS_BY_CUSTOMER_ID_SQL);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(fromAccountResultSet(resultSet));
            }
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return items;
        }  
    }
    
    public List<Transaction> getTransactionsByAccountNumber(String number){
        List<Transaction> items = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ALL_TRANSACTIONS_BY_ACCOUNT_NUMBER_SQL);
            statement.setString(1, number);
            statement.setString(2, number);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(fromTransactionResultSet(resultSet));
            }
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return items;
        } 
    }
    
    public List<Transaction> getAllTransactions(){
        List<Transaction> items = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ALL_TRANSACTIONS_SQL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(fromTransactionResultSet(resultSet));
            }
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return items;
        } 
    }
    
    public void updateAccountBeforeNewTransaction(int newBalance, String number){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_BEFORE_ACTION_BY_ACCOUNT_NUMBER_SQL);
            statement.setInt(1, newBalance);
            statement.setString(2, number);
            statement.executeUpdate();
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void addNewTransaction(String[] transactionData){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            String source = transactionData[0].trim();
            String target = transactionData[1].trim();
            int amount = Integer.parseInt(transactionData[2]);
            LocalTime localTime = LocalTime.now();
            LocalDateTime dt = localTime.atDate(LocalDate.now());
            Integer year = dt.getYear();
            Integer month = dt.getMonthValue();
            Integer day = dt.getDayOfMonth();
            String date = year.toString() + ".";
            if(month < 10){
                date += "0";
            }
            date += month.toString() + ".";
            if(day < 10){
                date +="0";
            }
            date += day.toString();
            System.out.println(date);
            Transaction transaction = new Transaction(source, target, amount, false, date);
            Double val = transaction.getAmount() * 0.03;
            collectorAccount.deposit(val.intValue());
            PreparedStatement statement = (PreparedStatement) fromTransactionEntity(INSERT_NEW_TRANSACTION_SQL, transaction, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public List<Account> getAccountById(int id){
       List<Account> accounts = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ACCOUNT_BY_ID_SQL);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                accounts.add(fromAccountResultSet(resultSet));
            }
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        } finally {
            return accounts;
        } 
    }
    
    public List<Transaction> getTransactionById(int id){
       List<Transaction> transactions = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_TRANSACTION_BY_ID_SQL);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                transactions.add(fromTransactionResultSet(resultSet));
            }
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        } finally {
            return transactions;
        } 
    }

    public List<Account> getAccountByAccountNumber(String number){
        List<Account> accounts = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(GET_ACCOUNT_BY_ACCOUNT_NUMBER_SQL);
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                accounts.add(fromAccountResultSet(resultSet));
            }
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        } finally {
            return accounts;
        }
    }
    
    public void updateAccountBeforeDelete(int newBalance, String number){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_BEFORE_ACTION_BY_ACCOUNT_NUMBER_SQL);
            statement.setInt(1, newBalance);
            statement.setString(2, number);
            statement.executeUpdate();
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void deleteTransactionById(int id){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(DELETE_TRANSACTION_BY_ID_SQL);
            statement.setInt(1, id);
            statement.executeUpdate();
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void updateAccountBeforeBooking(int newBalance, String number){
         try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(UPDATE_ACCOUNT_BEFORE_ACTION_BY_ACCOUNT_NUMBER_SQL);
            statement.setInt(1, newBalance);
            statement.setString(2, number);
            statement.executeUpdate();
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void bookTransactionById(int id){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = connection.prepareStatement(BOOK_TRANSACTION_BY_ID_SQL);
            statement.setInt(1, id);
            statement.executeUpdate();
            close(statement);
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void createCustomer(Customer entity) {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = (PreparedStatement) fromCustomerEntity(INSERT_CUSTOMER_SQL, entity, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void close(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    
    
}
