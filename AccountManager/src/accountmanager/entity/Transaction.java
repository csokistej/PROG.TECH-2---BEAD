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
public class Transaction extends PersistentEntity{

    private String source;
    private String target;
    private int amount;
    private String date;
    private boolean booked;
    
    
    public Transaction() {
    }

    public Transaction(String source, String target, int amount, boolean booked, String date) {
        this.source = source;
        this.target = target;
        this.amount = amount;
        this.booked = booked;
        this.date = date;
    }

    
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.source);
        hash = 47 * hash + Objects.hashCode(this.target);
        hash = 47 * hash + this.amount;
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
        final Transaction other = (Transaction) obj;
        if (this.amount != other.amount) {
            return false;
        }
        if (this.booked != other.booked) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.target, other.target)) {
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
                return source;
            case 2:
                return target;
            case 3:
                return amount;
            case 4:
                return booked;
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
                setSource((String) value);
                break;
            case 2:
                setTarget((String) value);
                break;
            case 3:
                setAmount((Integer) value);
                break;
            case 4:
                setBooked((Boolean) value);
                break;
            case 5:
                setDate((String) value);
        }
    }

    @Override
    public String toString() {
       String output =  "ID: " + id + " SRC: " + source + " TGT:  " + target + " AMOUNT: " + amount;
       if(isBooked()){
           output += " [BOOKED]";
       }
       return output;
    }

}
