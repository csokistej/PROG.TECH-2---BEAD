/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager;

import accountmanager.entity.Account;
import accountmanager.entity.Customer;
import accountmanager.entity.Employee;
import accountmanager.entity.Transaction;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author doobs
 */
public class DatabaseInitializer {

    private Connection connection;
    private Properties properties;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/accountmanager";
    private static final String CREATE_CUSTOMERS_TABLE_SQL = "CREATE TABLE CUSTOMERS ("
        + " ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
        + " FIRST_NAME VARCHAR(20) NOT NULL,"
        + " LAST_NAME VARCHAR(20) NOT NULL,"
        + " ADDRESS VARCHAR(40) NOT NULL,"
        + " PHONE VARCHAR(20))";
    private static final String CREATE_ACCOUNTS_TABLE_SQL = "CREATE TABLE ACCOUNTS ("
        + " ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
        + " ACCOUNT_NUMBER VARCHAR(26) NOT NULL,"
        + " BALANCE INT,"
        + " LOCKED BOOLEAN,"
        + " CUSTOMER_ID INT REFERENCES CUSTOMERS(ID),"
        + " DATE VARCHAR(30))";
    private static final String CREATE_TRANSACTIONS_TABLE_SQL = "CREATE TABLE TRANSACTIONS ("
        + " ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
        + " SOURCE VARCHAR(26) REFERENCES ACCOUNTS(ACCOUNT_NUMBER),"
        + " TARGET VARCHAR(26) REFERENCES ACCOUNTS(ACCOUNT_NUMBER),"
        + " AMOUNT INT,"
        + " BOOKED BOOLEAN,"
        + " DATE VARCHAR(30))";
     private static final String CREATE_EMPLOYEES_TABLE_SQL = "CREATE TABLE EMPLOYEES ("
        + " ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
        + " EMPLOYEE_ID VARCHAR(40) NOT NULL,"
        + " PASSWORD VARCHAR(42))";
    
    
    private static final String DELETE_CUSTOMERS_TABLE_SQL = "DROP TABLE CUSTOMERS";
    private static final String DELETE_ACCOUNTS_TABLE_SQL = "DROP TABLE ACCOUNTS";
    private static final String DELETE_TRANSACTIONS_TABLE_SQL = "DROP TABLE TRANSACTIONS";
    private static final String DELETE_EMPLOYEES_TABLE_SQL = "DROP TABLE EMPLOYEES";
    
    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMERS (FIRST_NAME, LAST_NAME, ADDRESS, PHONE) VALUES(?,?,?,?)";
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO ACCOUNTS (ACCOUNT_NUMBER, BALANCE, LOCKED, CUSTOMER_ID, DATE) VALUES (?,?,?,?,?)";
    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO TRANSACTIONS (SOURCE, TARGET, AMOUNT, BOOKED, date) VALUES (?,?,?,?,?)";
    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO EMPLOYEES (EMPLOYEE_ID, PASSWORD) VALUES (?,?)";
    
    public DatabaseInitializer() {
        properties = new Properties();
        properties.put("user", "root");
        properties.put("password", ""); 
    }
 
    
    public void init() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        deleteCustomerTable();
        deleteAccountsTable();  
        deleteTransactionsTable();
        deleteEmployeesTable();
        
        createCustomerTable();
        createAccountsTable();
        createTransactionsTable();
        createEmployeesTable();
        
        uploadCustomersTable();
        uploadAccountsTable();
        uploadTransactionsTable();
        uploadEmployeesTable();
    }
    
    
    public void deleteCustomerTable(){
         try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.executeUpdate(DELETE_CUSTOMERS_TABLE_SQL);
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteAccountsTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.executeUpdate(DELETE_ACCOUNTS_TABLE_SQL);
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteTransactionsTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.executeUpdate(DELETE_TRANSACTIONS_TABLE_SQL);
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void deleteEmployeesTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.executeUpdate(DELETE_EMPLOYEES_TABLE_SQL);
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createCustomerTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.addBatch(CREATE_CUSTOMERS_TABLE_SQL);
            statement.executeBatch();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createAccountsTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.addBatch(CREATE_ACCOUNTS_TABLE_SQL);
            statement.executeBatch();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createTransactionsTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.addBatch(CREATE_TRANSACTIONS_TABLE_SQL);
            statement.executeBatch();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createEmployeesTable(){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            Statement statement = connection.createStatement();
            statement.addBatch(CREATE_EMPLOYEES_TABLE_SQL);
            statement.executeBatch();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
    
    public Statement fromEmployeeEntity(String query, Employee entity, boolean withId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, entity.getEmployeeID());
        statement.setString(2, entity.getPassword());
        if (withId) {
            statement.setInt(3, entity.getId());
        }
        return statement;
    }
    
    public Customer fromCustomerResultSet(ResultSet resultSet){
        return null;
    }
     
    public void insertCustomer(Customer entity) {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = (PreparedStatement)fromCustomerEntity(INSERT_CUSTOMER_SQL, entity, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertAccount(Account entity){
         try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = (PreparedStatement) fromAccountEntity(INSERT_ACCOUNT_SQL, entity, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertTransaction(Transaction entity){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = (PreparedStatement) fromTransactionEntity(INSERT_TRANSACTION_SQL, entity, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void insertEmployee(Employee entity){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, properties);
            PreparedStatement statement = (PreparedStatement) fromEmployeeEntity(INSERT_EMPLOYEE_SQL, entity, false);
            statement.executeUpdate();
            close(statement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void uploadCustomersTable(){
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer("Árpád", "Németh", "1118. Bp. Frankhegy u.8", "06209901142"));
        customers.add(createCustomer("Kata", "Horváth", "1221 Budapest, Pedellus u. 21", "06307894420"));
        customers.add(createCustomer("Eszter", "Kiss", "1035 Budapest, Bécsi út. 80", "06207745546"));
        customers.add(createCustomer("Zoltán", "Szabó", "2400 Budaörs, Rozmaring u.2 /C", "06707798088"));
        customers.stream().forEach(customer -> { 
            insertCustomer(customer);
        });
    }
    
    public void uploadAccountsTable(){
        List<Account> accounts = new ArrayList<>();
        accounts.add(createAccount("10000000-00000000-00000000", 150510, false, 1, "2017.06.01"));
        accounts.add(createAccount("20000000-00000000-00000000", 10055, false, 1, "2017.06.02"));
        accounts.add(createAccount("30000000-00000000-00000000", 254000, false, 2, "2017.06.02"));
        accounts.add(createAccount("40000000-00000000-00000000", 47910, false, 3, "2017.06.02"));
        accounts.add(createAccount("50000000-00000000-00000000", 199500, false, 4, "2017.06.02"));
        accounts.stream().forEach(account -> insertAccount(account));
    }
    
    public void uploadTransactionsTable(){
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createTransaction("10000000-00000000-00000000","20000000-00000000-00000000", 1500, "2017.06.03"));
        transactions.add(createTransaction("20000000-00000000-00000000","30000000-00000000-00000000", 2500, "2017.06.03"));
        transactions.add(createTransaction("20000000-00000000-00000000","10000000-00000000-00000000", 4500, "2017.06.03"));
        transactions.add(createTransaction("30000000-00000000-00000000","10000000-00000000-00000000", 12400, "2017.06.03"));
        transactions.add(createTransaction("40000000-00000000-00000000","50000000-00000000-00000000", 5000, "2017.06.03"));
        transactions.add(createTransaction("10000000-00000000-00000000","40000000-00000000-00000000", 25000, "2017.06.04"));
        transactions.add(createTransaction("50000000-00000000-00000000","20000000-00000000-00000000", 32000, "2017.06.04"));
        transactions.add(createTransaction("40000000-00000000-00000000","50000000-00000000-00000000", 9900, "2017.06.04"));
        transactions.stream().forEach(transaction -> insertTransaction(transaction));
    }
    
    
    public void uploadEmployeesTable() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        List<Employee> employees = new ArrayList<>();
        String password = MD5("123");
        employees.add(createEmployee("employee1", password));
        employees.stream().forEach(employee -> insertEmployee(employee));
    }
    
    private String MD5(String md5) {
        try {
             java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
             byte[] array = md.digest(md5.getBytes());
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < array.length; ++i) {
               sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
             return sb.toString();
         } catch (java.security.NoSuchAlgorithmException e) {
         
         }
         return null;
    }
    
    private Customer createCustomer(String firstName, String lastName, String address, String phone) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setPhone(phone);
        return customer;
    }

    private Account createAccount(String number, int balance, boolean locked, int customerId,String date){
        Account account = new Account();
        account.setNumber(number);
        account.setBalance(balance);
        account.setLocked(locked);
        account.setCustomerId(customerId);
        account.setDate(date);
        return account;
    }
    
    private Transaction createTransaction(String source, String target, int amount, String date){
        Transaction transaction = new Transaction();
        transaction.setSource(source);
        transaction.setTarget(target);
        transaction.setAmount(amount);
        transaction.setBooked(false);
        transaction.setDate(date);
        return transaction;
    }
    
    private Employee createEmployee(String employeeID, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        Employee employee = new Employee(employeeID, password);
        return employee;
    }
    
    
    private void close(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    
    public static DatabaseInitializer getInstance() {
        return DatabaseInitializerHolder.INSTANCE;
    }

    private static class DatabaseInitializerHolder {
        private static final DatabaseInitializer INSTANCE = new DatabaseInitializer();
    }
    
   
    
}
