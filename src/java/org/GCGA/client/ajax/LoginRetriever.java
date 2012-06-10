/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.ajax;
import org.GCGA.client.GUI.WebLoader;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;


import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;
import org.GCGA.client.LoginHandler;
import org.GCGA.client.utility.UtilityFunctions;


/**
 *
 * @author Marcello
 * Singleton class
 */
public class LoginRetriever {


    private static LoginRetriever retriever = null;

    private static String registerUrl;
    private static String loginUrl;
    private static String sendPswdUrl;

    public static void setLoginUrl(String urlParam){
            loginUrl = urlParam;
    }

    public static void setRegisterUrl(String urlParam){
            registerUrl = urlParam;
    }

    public static void setSendPswdUrl(String urlParam){
            sendPswdUrl = urlParam;
    }

    private LoginRetriever() {
        setRegisterUrl("../register_json.php");
        setLoginUrl("../login_json.php");
        setSendPswdUrl("../sendPswd_json.php");
        //setUrl("http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&output=json");
    }

    public static LoginRetriever get(){
        if (retriever == null ){
            retriever = new LoginRetriever();
        }
        return retriever;

    }

    public void register(String email){

        if ( !UtilityFunctions.wellFormedEmailAddress(email)  ){
            SC.warn("The email address you inserted doesn't appear to be valid.<br/>Please check it or use another email address.");
            return;
        }

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(registerUrl));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str = "email="+email;

            WebLoader.showLoader();

            builder.sendRequest(params_str, new RequestCallback() {

            public void onError(Request request, Throwable exception) {
           // Couldn't connect to server (could be timeout, SOP violation, etc.)
                WebLoader.hideLoader();
                SC.warn("An error has occurred while trying to register.<br/> The operation could not be completed");
            }

            public void onResponseReceived(Request request, Response response) {
                //alert(request.toString());
                //alert(response.getStatusCode()+" " + response.getStatusText());
               JSONObject jsonObj;
               //JSONArray jsonArr;
               JSONValue valueObject;

               WebLoader.hideLoader();

              if (200 == response.getStatusCode() ) {
                  // Process the response in response.getText()
                    //alert(response.getText());
                    try{
                        valueObject = JSONParser.parse(response.getText());
                    }catch(Exception e){
                        SC.warn("An error has occurred while trying to register");
                        return;
                    }
               }
               else {
                // Handle the error.  Can get the status text from response.getStatusText()
                  try{
                    SC.warn("An error has occurred while trying to register: " + response.getStatusText());
                  }catch(Exception e){
                            SC.warn("An error has occurred while trying to register: ");
                  }finally{
                      throw new IllegalArgumentException("AJAX Request error: ");
                  }
              }
              try {
                  jsonObj = valueObject.isObject();
                  if ( jsonObj.get("registration").isBoolean().booleanValue() )
                      SC.say("Please check your email to get your password in order to sign in.");
                  else{
                      String errorMessage = jsonObj.get("errorMessage").isString().stringValue();
                      SC.warn("It was not possible to complete the registration procedure.<br/>The error reported is: "+errorMessage);
                  }

              }
              catch (Exception e){
                   SC.warn("Registration failed, we are sorry");
                   //alert(e.getMessage());
              }
            }
          });
        } catch (RequestException e) {
          // Couldn't connect to server
          SC.warn("An error has occurred while trying to connect to the server.<br/>We apologize.<br/>Please feel free to report us the problem.");
        }
    }

    public void sendPswd(String email){

        if (!UtilityFunctions.wellFormedEmailAddress(email)  ){
            SC.warn("The email address you inserted doesn't appear to be valid.<br/>Please check it or use another email address.");
            return;
        }

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(sendPswdUrl));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str = "email="+email;

            WebLoader.showLoader();

            builder.sendRequest(params_str, new RequestCallback() {

            public void onError(Request request, Throwable exception) {
           // Couldn't connect to server (could be timeout, SOP violation, etc.)
                WebLoader.hideLoader();
                SC.warn("An error has occurred.<br/> The operation could not be completed");
            }

            public void onResponseReceived(Request request, Response response) {
                //alert(request.toString());
                //alert(response.getStatusCode()+" " + response.getStatusText());
               JSONObject jsonObj;
               //JSONArray jsonArr;
               JSONValue valueObject;

               WebLoader.hideLoader();

              if (200 == response.getStatusCode() ) {
                  // Process the response in response.getText()
                    //alert(response.getText());
                    try{
                        valueObject = JSONParser.parse(response.getText());
                    }catch(Exception e){
                        SC.warn("An error has occurred.<br/> The operation could not be completed");
                        return;
                    }
               }
               else {
                // Handle the error.  Can get the status text from response.getStatusText()
                  try{
                    SC.warn("An error has occurred: " + response.getStatusText());
                  }catch(Exception e){
                            SC.warn("An error has occurred: impossible to complete the operation");
                  }finally{
                      throw new IllegalArgumentException("AJAX Request error: ");
                  }
              }
              try {
                  jsonObj = valueObject.isObject();
                  if ( jsonObj.get("sent").isBoolean().booleanValue() )
                      SC.say("A new password has been generated for your account.<br/>Please check your email to get it in order to sign in.");
                  else{
                      String errorMessage = jsonObj.get("errorMessage").isString().stringValue();
                      SC.warn("It was not possible to generate a new password for this account.<br/>The error reported is: "+errorMessage);
                  }

              }
              catch (Exception e){
                   SC.warn("We are sorry, it was impossible to complete the operation");
                   //alert(e.getMessage());
              }
            }
          });
        } catch (RequestException e) {
          // Couldn't connect to server
          SC.warn("An error has occurred while trying to connect to the server.<br/>We apologize.<br/>Please feel free to report us the problem.");
        }
    }

    public void login(final String email, final String password){
        if (!UtilityFunctions.wellFormedEmailAddress(email)  ){
            SC.warn( "The email address you inserted doesn't appear to be valid.<br/>Please check it or use another email address.");
            return;
        }

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(loginUrl));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str = "email="+email+"&password="+password;

            WebLoader.showLoader();

            builder.sendRequest(params_str, new RequestCallback() {

            public void onError(Request request, Throwable exception) {
           // Couldn't connect to server (could be timeout, SOP violation, etc.)
                WebLoader.hideLoader();
                SC.warn("An error has occurred while trying to register.<br/> The operation could not be completed");
            }

            public void onResponseReceived(Request request, Response response) {
                //alert(request.toString());
                //alert(response.getStatusCode()+" " + response.getStatusText());
               JSONObject jsonObj;
               //JSONArray jsonArr;
               JSONValue valueObject;

               WebLoader.hideLoader();

              if (200 == response.getStatusCode() ) {
                  // Process the response in response.getText()
                    //alert(response.getText());
//                    GeneticAlgorithm.alert(response.getText());

                    try{
                        valueObject = JSONParser.parse(response.getText());
                    }catch(Exception e){
                        SC.warn("An error has occurred while trying to sign in");
                        return;
                    }

               }
               else {
                // Handle the error.  Can get the status text from response.getStatusText()
                  try{
                    SC.warn("An error has occurred while trying to sign in: " + response.getStatusText());
                  }catch(Exception e){
                            SC.warn("An error has occurred while trying to sign in");
                  }finally{
                      throw new IllegalArgumentException("AJAX Request error: ");
                  }
              }
              try {
                  jsonObj = valueObject.isObject();
                  if ( jsonObj.get("logged").isBoolean().booleanValue() ){
                      LoginHandler.get().doLogIn(email, password);
                  }
                  else{
                      String errorMessage = jsonObj.get("errorMessage").isString().stringValue();
                      SC.warn("It was not possible to sign in.<br/>The error reported is: "+errorMessage);
                  }

              }
              catch (Exception e) {
                  SC.warn("We are sorry, it was not possible to sign you in. <br/>Please check the information inserted");
              }
            }
          });
        } catch (RequestException e) {
          // Couldn't connect to server
          SC.warn("An error has occurred while trying to connect to the server.<br/>We apologize.<br/>Please feel free to report us the problem.");
        }
    }


    /*

    private native void alert(String message)/*-{
		alert(message);
    }-* /;

     */

}

