
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager.entity;

import java.util.Objects;

/**
 *
 * @author doobs
 */
public class Account extends PersistentEntity {
 
    private String number;
    private int balance;
    private boolean locked;
    private int customerId;
    private String date;
    
    public Account() {
    }
    
    public Account(String number, int balance, boolean locked, String date) {
        this.number = number;
        this.balance = balance;
        this.locked = locked;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.number);
        hash = 23 * hash + this.customerId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
        if (this.balance != other.balance) {
            return false;
        }
        if (this.locked != other.locked) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public Object get(int columnIndex) {
       switch (columnIndex) {
            case 0:
                return id;
            case 1:
                return number;
            case 2:
                return balance;
            case 3:
                return locked;
            case 4:
                return customerId;
            case 5:
                return date;
            default:
                return null;
        }
    }

    @Override
    public void set(int columnIndex, Object value) {
       switch (columnIndex) {
            case 0:
                setId((Integer) value);
                break;
            case 1:
                setNumber((String) value);
                break;
            case 2:
                setBalance((Integer) value);
                break;
            case 3:
                setLocked((Boolean) value);
                break;
            case 4:
                setCustomerId((Integer) value);
                break;
            case 5:
                setDate((String) value);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID: " + id + "\t" + number + "\tbalance: " + balance + "\t" + customerId);
        if(isLocked()){
            stringBuilder.append(" [LOCKED]");                    
        }
        return stringBuilder.toString();
    }
    
    
    
    
    
}
