/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import accountmanager.DatabaseInitializer;
import accountmanager.controller.AccountManagerController;
import accountmanager.controller.LoginController;
import accountmanager.entity.Account;
import accountmanager.entity.CollectorAccount;
import accountmanager.entity.Customer;
import accountmanager.entity.Transaction;
import accountmanager.view.AccountManagerView;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author doobs
 */
public class AccountManagerControllerTest {
    AccountManagerView view;
    AccountManagerController controller;
    
    public AccountManagerControllerTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DatabaseInitializer.getInstance().init();

    }
    
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        view = new AccountManagerView(new LoginController("employee1","123"));
        controller = new AccountManagerController(view);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getAllCustomersTest(){
        List<Customer> customers = new ArrayList<>();
        controller.getAllCustomers().stream().forEach(customer -> customers.add(customer));
        assertEquals(4, customers.size());
    }
    
    @Test
    public void getAllAccountsTest(){
        List<Account> accounts = new ArrayList<>();
        controller.getAllAccounts().stream().forEach(account -> accounts.add(account));
        assertEquals(5, accounts.size());
    }
    
    @Test
    public void getAllTransactionsTest(){
        List<Transaction> transactions = new ArrayList<>();
        controller.getAllTransactions().stream().forEach(transaction -> transactions.add(transaction));
        assertEquals(8, transactions.size());
    }
    
    
    @Test
    public void getAccountByAccountNumberTest(){
        List<Account> accounts = controller.getAccountByAccountNumber("10000000-00000000-00000000");
        Account queriedAccount = null;
        if(accounts.size() == 1){
           queriedAccount = accounts.get(0);
        }
        Account account = new Account();
        account.setId(1);
        account.setNumber("10000000-00000000-00000000");
        account.setBalance(150510);
        account.setLocked(false);
        account.setCustomerId(1);
        account.setDate("2017.06.01");
        assertTrue(account.equals(queriedAccount));
    }
    
    @Test
    public void addNewTransactionTest(){
        String[] transactionDate = new String[]{"10000000-00000000-00000000","40000000-00000000-00000000", "5000"};
        controller.addNewTransaction(transactionDate);
        List<Transaction> transactions = new ArrayList<>();
        controller.getAllTransactions().stream().forEach(transaction -> transactions.add(transaction));
        assertEquals(9, transactions.size());
    }
    
    
    @Test
    public void deleteTransactionByIdTest(){
        int testId = 5;
        controller.deleteTransactionById(testId);
        testId = 7;
        controller.deleteTransactionById(testId);
        List<Transaction> transactions = new ArrayList<>();
        controller.getAllTransactions().stream().forEach(transaction -> transactions.add(transaction));
        assertEquals(6, transactions.size());
    }
    
    
    @Test
    public void bookTransactionByIdTest(){
        int testId = 3;
        controller.bookTransactionById(testId);
        List<Transaction> transactions = new ArrayList<>();
        controller.getTransactionById(testId).stream().forEach(transaction -> transactions.add(transaction));
        Transaction transaction = null;
        if(transactions.size() == 1){
            transaction = transactions.get(0);
        }
        assertEquals(true, transaction.isBooked());
    }
    
    @Test
    public void updateAccountBeforeDeleteTest(){
        int testId = 2;
        List<Transaction> transactions = new ArrayList<>();
        controller.getTransactionById(testId).stream().forEach(transaction -> transactions.add(transaction));
        Transaction transaction = null;
        if(transactions.size() == 1){
            transaction = transactions.get(0);
        }
        List<Account> accounts = new ArrayList<>();
        controller.getAccountByAccountNumber(transaction.getSource()).stream().forEach(account -> accounts.add(account));
        Account account = null;
        if(accounts.size() == 1){
            account = accounts.get(0);
        }
        int newBalance = transaction.getAmount() + account.getBalance();
        controller.updateAccountBeforeDelete(newBalance, transaction.getSource());
        accounts.clear();
        controller.getAccountByAccountNumber(transaction.getSource()).stream().forEach(account2 -> accounts.add(account2));
        account = null;
        if(accounts.size() == 1){
            account = accounts.get(0);
        }
        assertTrue(account.getBalance() == newBalance);
    }
    
    @Test
    public void updateAccountBeforeBookingTest(){
        int testId = 3;
        List<Transaction> transactions = new ArrayList<>();
        controller.getTransactionById(testId).stream().forEach(transaction -> transactions.add(transaction));
        Transaction transaction = null;
        if(transactions.size() == 1){
            transaction = transactions.get(0);
        }
        List<Account> accounts = new ArrayList<>();
        controller.getAccountByAccountNumber(transaction.getTarget()).stream().forEach(account -> accounts.add(account));
        Account account = null;
        if(accounts.size() == 1){
            account = accounts.get(0);
        }
        int newBalance = transaction.getAmount() + account.getBalance();
        controller.updateAccountBeforeDelete(newBalance, transaction.getTarget());
        accounts.clear();
        controller.getAccountByAccountNumber(transaction.getTarget()).stream().forEach(account2 -> accounts.add(account2));
        account = null;
        if(accounts.size() == 1){
            account = accounts.get(0);
        }
        assertTrue(account.getBalance() == newBalance);
    }
    
    
    @Test
    public void CollectorAccountTest(){
        String[] transactionDate = new String[]{"40000000-00000000-00000000","50000000-00000000-00000000", "3025"};
        controller.addNewTransaction(transactionDate);
        List<Transaction> transactions = new ArrayList<>();
        int oldBalance = controller.getCollectorAccount().getBalance();
        controller.addNewTransaction(transactionDate);
        int newBalance = controller.getCollectorAccount().getBalance();
        Double cost = 3025 * 0.03;
        int expected = oldBalance + cost.intValue();
        assertEquals(expected, newBalance);
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
