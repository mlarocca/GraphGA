/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import java.util.Date;
import org.GCGA.client.GUI.AnalysisPanel;
import org.GCGA.client.GUI.PersonalInfoPanel;
import org.GCGA.client.ajax.LoginRetriever;

/**
 *
 * @author Owner
 */
public class LoginHandler {

    private static LoginHandler thisHandler = null;

    private static String email = null;
//    private static String password = null; //Encoded Password;
    private static boolean logged = false;

    private static final String REGISTRATION_COOKIE = "GraphGARegistration";
    private static final String SEPARATOR = "/";

    private LoginHandler() {

        String cookie;
        if ( (cookie = Cookies.getCookie(REGISTRATION_COOKIE))!=null ){
//            GeneticAlgorithm.alert("Cookie: " +cookie);
            String[] values = cookie.split(SEPARATOR);
            try{
                LoginRetriever.get().login(values[0], values[1]);
//                email = values[0];
//                password = values[1];
//                logged = true;
            }catch(Exception e){
                //IMPOSSIBILE LOGIN
            }
        }
        /*else{
            if ( Cookies.isCookieEnabled() )
                Cookies.setCookie(REGISTRATION_COOKIE, "0", null);
        */
    }

    public void logIn(final String userEmail){
        
        SC.askforValue("Insert Password", "Please insert password for "+userEmail, new ValueCallback(){

            public void execute(String value) {
                if (value!=null && !value.equals(""))
                    LoginRetriever.get().login(userEmail, value);
            }
        } );
    }

    public void logOut(){
        SC.ask("Are you sure you want to log out?", new BooleanCallback() {

            public void execute(Boolean value) {
                if (value){
                    doLogOut();
                }
            }
        });
    }

    public void register(String userEmail){
        LoginRetriever.get().register(userEmail);
    }

    public void doLogIn(String userEmail, String pswd){
        email = userEmail;
//        password = pswd;

        logged = true;
        Date howLong = new Date();
        howLong.setTime(howLong.getTime() + 1000 * 60 * 60 * 24 * 7);//seven days

//        GeneticAlgorithm.alert("Cookie -> " +email+SEPARATOR+pswd);
        if ( Cookies.isCookieEnabled() )
                Cookies.setCookie(REGISTRATION_COOKIE, email+SEPARATOR+pswd, howLong);

        PersonalInfoPanel.showLogin(email);
        AnalysisPanel.get().updatePanelStatus();
    }

    public void doLogOut(){
        email = null;
        logged = false;
//        password = null;
        Cookies.removeCookie(REGISTRATION_COOKIE);
        PersonalInfoPanel.hideLogin();
        AnalysisPanel.get().updatePanelStatus();
    }
    
    public static LoginHandler get(){
        if (thisHandler==null)
            thisHandler = new LoginHandler();
        return thisHandler;
    }

    public boolean isLogged(){
        return logged;
    }

    /**
     *
     * @return null <=> user is not logged in
     *         user's email <=> user is logged in
     */
    public String getEmail(){
        if (logged)
            return email;
        else
            return "";
    }

}
