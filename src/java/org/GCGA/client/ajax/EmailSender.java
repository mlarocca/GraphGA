
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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Marcello
 * Singleton class
 */
public class EmailSender {
    
    private static EmailSender sender = null;

    private static final String toAddress = "marcellolarocca@gmail.com";

    private static String senderUrl;

    public static void setSenderUrl(String urlParam){
            senderUrl = urlParam;
    }

    private EmailSender() {
        setSenderUrl("../contacts_json.php");
    }

    public static EmailSender get(){
        if (sender == null ){
            sender = new EmailSender();
        }
        return sender;

    }

    public void sendEmail(final String fromAddress, final String subject, final String body){

        if ( !UtilityFunctions.wellFormedEmailAddress(fromAddress)  ){
            SC.warn("The email address you inserted doesn't appear to be valid.<br/>Please check it or use another email address.");
            return;
        }

        if (fromAddress.equals("") || body.equals("")){
            SC.say("Please fill in all the required fields.");
            return;
        }

        if (subject.equals("")){
            SC.confirm("The <b style='color:red;'>Subject</b> field is empty: do you want to send your message anyway?", new BooleanCallback() {
                        public void execute(Boolean value) {
                            if (value != null && value) {
                                doSendEmail(fromAddress, subject,  body);
                            }else{
                                return;
                            }
                        }
                    });
        }else{
            doSendEmail(fromAddress, subject,  body);
        }
    }

    private void doSendEmail(final String fromAddress, final String subject, final String body){


        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(senderUrl));
        //alert(URL.encode(url));
        try {
            builder.setRequestData("ActivesOnly=false");
            builder.setHeader("Content-type", "application/x-www-form-urlencoded");

      //Request request =
            String params_str = "to="+toAddress+"&from="+fromAddress+"&subject="+subject+"&body="+body;

            WebLoader.showLoader();

            builder.sendRequest(params_str, new RequestCallback() {

            public void onError(Request request, Throwable exception) {
           // Couldn't connect to server (could be timeout, SOP violation, etc.)
                WebLoader.hideLoader();
                SC.warn("An error has occurred while trying to accomplish your request.<br/> The operation could not be completed");
            }

            public void onResponseReceived(Request request, Response response) {
                //alert(request.toString());
 //              UtilityFunctions.alert(response.getStatusCode()+" " + response.getStatusText());
//               UtilityFunctions.alert(response.getStatusCode()+" " + response.getText());
               JSONObject jsonObj;
               //JSONArray jsonArr;
               JSONValue valueSubject;

               WebLoader.hideLoader();

              if (200 == response.getStatusCode() ) {
                  // Process the response in response.getText()
                    //alert(response.getText());
                    try{
                        valueSubject = JSONParser.parse(response.getText());
                    }catch(Exception e){
                        SC.warn("An error has occurred while trying to deliver your message");
                        return;
                    }
               }
               else {
                // Handle the error.  Can get the status text from response.getStatusText()
                  try{
                    SC.warn("An error has occurred while trying to send your message: " + response.getStatusText());
                  }catch(Exception e){
                            SC.warn("An error has occurred while trying to deliver your message: ");
                  }finally{
                      throw new IllegalArgumentException("AJAX Request error: ");
                  }
              }
              try {
                  jsonObj = valueSubject.isObject();
                  if ( jsonObj.get("sent").isBoolean().booleanValue() )
                      SC.say("Your email has been delivered to our account.<br>Please allow us a few days to consider your inquiries and prepare an adequate answer.");
                  else{
                      String errorMessage = jsonObj.get("errorMessage").isString().stringValue();
                      SC.warn("It was not possible to deliver your message.<br/>The error reported is: "+errorMessage);
                  }

              }
              catch (Exception e){
                   SC.warn("Message deliver failed, we are sorry");
                   //alert(e.getMessage());
              }
            }
          });
        } catch (RequestException e) {
          // Couldn't connect to server
          SC.warn("An error has occurred while trying to connect to the server.<br/>We apologize.<br/>Please feel free to report us the problem.");
        }
    }




}
