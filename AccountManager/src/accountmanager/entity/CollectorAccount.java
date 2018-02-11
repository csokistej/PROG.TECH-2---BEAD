/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager.entity;

/**
 *
 * @author doobs
 */
public class CollectorAccount {
    
    private final int id;
    private final String number;
    private int balance;
    private boolean underClosing;
    
    public CollectorAccount(int id, String number, int balance){
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isUnderClosing() {
        return underClosing;
    }

    public void setUnderClosing(boolean underClosing) {
        this.underClosing = underClosing;
    }
    
    
    
    public void deposit(int amount){
        int newBalance = getBalance() + amount;
        setBalance(newBalance);
    }
    
    
}
