/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager;

/**
 *
 * @author doobs
 */
public class CustomDate {
    
    private int year;
    private int month;
    private int day;


    public CustomDate(int year, int month , int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
   

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.year;
        hash = 73 * hash + this.month;
        hash = 73 * hash + this.day;
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
        final CustomDate other = (CustomDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String dateString = year + ".";
        if(month < 10){
            dateString += "0";
        }
        dateString += month + ".";
        if(day < 10){
            dateString += "0";
        }
        dateString += day;
        return dateString;
    }
    
  
    
    
}
