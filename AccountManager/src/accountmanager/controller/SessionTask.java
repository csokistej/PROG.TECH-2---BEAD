/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountmanager.controller;

import accountmanager.view.AccountManagerView;
import java.util.TimerTask;

/**
 *
 * @author doobs
 */
public class SessionTask extends TimerTask {

    AccountManagerView view;
    public SessionTask(AccountManagerView view){
        this.view = view;
    }
    
    @Override
    public void run() {
       view.confirmSession();  
    }
    
}
