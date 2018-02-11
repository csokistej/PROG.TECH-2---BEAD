
package accountmanager.controller;

import accountmanager.entity.Account;
import accountmanager.entity.Employee;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import static java.util.Objects.hash;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author doobs
 */
public class LoginController {
    
    private String employeeID;
    private String password;
    private boolean logged;
    private Timer timer;  
    private Connection connection;
    private Properties properties; 
    
    //QUERIES
    private static final String GET_EMPLOYEE_SQL = "SELECT * FROM EMPLOYEES WHERE EMPLOYEE_ID = ? AND PASSWORD = ?";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/accountmanager";
    
    
    public LoginController(String employeeID, String password) {
        this.employeeID = employeeID;
        this.password = MD5(password);
        properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");   //vaskos a védelem :)
        logged = false;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
    
    public Employee fromEmployeeResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("ID"));
        employee.setEmployeeID(resultSet.getString("EMPLOYEE_ID"));
        employee.setPassword(resultSet.getString("PASSWORD"));
        return employee;
    }
    
    public List<Employee> login(){
        List<Employee> items = new ArrayList<>();
        try {
          connection = DriverManager.getConnection(DATABASE_URL, properties);
          PreparedStatement statement = connection.prepareStatement(GET_EMPLOYEE_SQL);
          statement.setString(1, employeeID);
          statement.setString(2, password);
          statement.executeQuery();
          setLogged(true);
          close(statement);
        } catch (SQLException ex){
            ex.printStackTrace();
        } finally {
            return items;
        }
    }
    
    public void startSession(){
        timer = new Timer();
    }
    
    public void resetSessionTime(TimerTask tt, long delay, long period){
        timer.purge();
        timer.schedule(tt, delay, period);
    }
    
    public void schedule(TimerTask tt, long delay, long period){
        timer.purge();
        timer.schedule(tt, delay, period);
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
    
    private void close(Statement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    
}
