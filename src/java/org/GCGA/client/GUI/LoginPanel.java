/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.GCGA.client.LoginHandler;
import org.GCGA.client.ajax.LoginRetriever;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Owner
 */
public class LoginPanel extends Canvas{
    private static TextItem emailText;

    private static CheckboxItem showEmailCheckBoxItem;
    private static CheckboxItem rememberCheckBoxItem;

    private static Button registerButton;
    private static Button loginButton;
    private static Button sendPswdButton;

    private static Canvas signedOutCanvas;
    private static Canvas signedInCanvas = null;

    private static Label userLabel = null;
//    private static final byte EMAIL_PANEL_HEIGHT = 80;

    private static final byte BUTTON_WIDTH = 90;
    private static final short TEXT_FIELD_WIDTH = 225;

    public LoginPanel(int width) {

     final DynamicForm emailForm = new DynamicForm();

        signedOutCanvas = new Canvas();

         emailText = new TextItem();
         emailText.setTitle("Email");
         //verticesItem.setRequired(true);
//         emailText.setLeft(Utility.MARGIN);

         emailText.setWidth( TEXT_FIELD_WIDTH );
         emailText.setTitleAlign(Alignment.RIGHT);

         UtilityFunctions.setWidgetPrompt(emailText, "Insert your email", "If you agree to provide your email address it will be stored along with information about the embeddings you may find.<br/>It won't be used for any other purpose nor sold or transferred in any way to third parts.");

         //emailText.disable();
         emailForm.setFields(emailText);
         final DynamicForm showEmailForm = new DynamicForm();
         showEmailCheckBoxItem = new CheckboxItem();
//         showEmailCheckBoxItem.setLeft(Utility.MARGIN);
//         showEmailCheckBoxItem.setTop(20);
         showEmailCheckBoxItem.setTitleOrientation(TitleOrientation.RIGHT);
         showEmailCheckBoxItem.setTitle("Show my email");

         UtilityFunctions.setWidgetPrompt(showEmailCheckBoxItem, "Make your email visible", "By selecting this option your email <b>will be made visible to everybody</b> who visits the website.<br/>Be sure to agree with that.");

         showEmailForm.setFields(showEmailCheckBoxItem);

         final DynamicForm rememberForm = new DynamicForm();
         rememberCheckBoxItem = new CheckboxItem();
//         showEmailCheckBoxItem.setLeft(Utility.MARGIN);
//         showEmailCheckBoxItem.setTop(20);
         rememberCheckBoxItem.setTitleOrientation(TitleOrientation.RIGHT);
         rememberCheckBoxItem.setTitle("Remember me on this computer");

         UtilityFunctions.setWidgetPrompt(rememberCheckBoxItem, "Stay logged in", "Set cookies in order to keep you logged in on this computer (up to one week). Please be aware that if you are not the only one accessing the computer this option would allow others to use your account.");

         rememberForm.setFields(rememberCheckBoxItem);

         emailForm.setTop( Utility.MARGIN );
         emailForm.setLeft( Utility.MARGIN );
         showEmailForm.setTop( emailForm.getBottom() + Utility.MARGIN );
         showEmailForm.setLeft( Utility.MARGIN );
         rememberForm.setTop( showEmailForm.getBottom() + Utility.MARGIN );
         rememberForm.setLeft( Utility.MARGIN );

         //         emailForm.setHeight(100);
         signedOutCanvas.setWidth( width );
         signedOutCanvas.setTop( Utility.MARGIN );
         signedOutCanvas.setLeft( Utility.MARGIN );

         signedOutCanvas.addChild(emailForm);
         signedOutCanvas.addChild(showEmailForm);
         signedOutCanvas.addChild(rememberForm);

         registerButton = new Button("Register");
         registerButton.setWidth(BUTTON_WIDTH);
         registerButton.setTop(Utility.MARGIN);
         registerButton.setLeft( width - registerButton.getWidth() - Utility.MARGIN );
         registerButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                LoginHandler.get().register(getEmailText());
            }
         });

         UtilityFunctions.setWidgetPrompt(registerButton, "Register", "In order to use all the website's advanced features registration is required.<br/>Registration is free of charge; your email address is needed only to verify your identity: it will never ever be given or sold to third parts.");

         signedOutCanvas.addChild(registerButton);

         loginButton = new Button("Sign in");
         loginButton.setWidth(BUTTON_WIDTH);
         loginButton.setTop(registerButton.getBottom() + Utility.MARGIN );
         loginButton.setLeft(registerButton.getLeft() );

         loginButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                LoginHandler.get().logIn(getEmailText());
            }
         });
         UtilityFunctions.setWidgetPrompt(loginButton, "Sign in", "After registering check your email for the password the website assigned to you and sign in.<br/>Don't worry, it's as easy as this.");

         signedOutCanvas.addChild(loginButton);

         sendPswdButton = new Button("Re-Send Pswd");
         sendPswdButton.setWidth(BUTTON_WIDTH);
         sendPswdButton.setTop(loginButton.getBottom() + Utility.MARGIN );
         sendPswdButton.setLeft(loginButton.getLeft() );

         sendPswdButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                LoginRetriever.get().sendPswd(getEmailText());
            }
         });
         UtilityFunctions.setWidgetPrompt(sendPswdButton, "Forgot password", "If you can't remember your password a new one will be sent to you by providing your email address (the one used to register).");
         signedOutCanvas.addChild(sendPswdButton);

         //signedOutCanvas.setHeight(EMAIL_PANEL_HEIGHT);
         signedOutCanvas.setBorder(Utility.INNER_PANEL_BORDER);

         this.addChild(signedOutCanvas);
    }

    public void showLogIn(String username){
        signedOutCanvas.setVisible(false);
        if ( signedInCanvas == null){
            signedInCanvas = new Canvas();
            signedInCanvas.setTop(signedOutCanvas.getTop());
            signedInCanvas.setLeft(signedOutCanvas.getLeft());
            signedInCanvas.setWidth(signedOutCanvas.getWidth());
            signedInCanvas.setHeight(signedOutCanvas.getHeight());
            signedInCanvas.setBorder(Utility.INNER_PANEL_BORDER);

            userLabel = new Label("<b>Welcome, <i style='color:red'> "+username+"</i></b>");
            userLabel.setWidth(signedInCanvas.getWidth()-2*Utility.MARGIN);
            userLabel.setHeight(Utility.LABEL_HEIGHT);
            userLabel.setTop(Utility.MARGIN);
            userLabel.setLeft(Utility.MARGIN);
            signedInCanvas.addChild(userLabel);

            Button logoutButton = new Button("Sign out");
            logoutButton.setTop(userLabel.getBottom()+Utility.MARGIN);
            logoutButton.setLeft(Utility.MARGIN);

            logoutButton.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    //showLogOut();
                    LoginHandler.get().logOut();
                }
            });

            UtilityFunctions.setWidgetPrompt(logoutButton, "Sign out", "Click here in order to be logged off and have your data erased from the cookies.");

            signedInCanvas.addChild(logoutButton);

            this.addChild(signedInCanvas);
        }else{
            signedInCanvas.removeChild(userLabel);
            userLabel = new Label("<b>Welcome, <i style='color:red'> "+username+"</i></b>");
            userLabel.setWidth(signedInCanvas.getWidth()-2*Utility.MARGIN);
            userLabel.setHeight(Utility.LABEL_HEIGHT);
            userLabel.setTop(Utility.MARGIN);
            userLabel.setLeft(Utility.MARGIN);
            signedInCanvas.addChild(userLabel);
        }
        
        signedInCanvas.setVisible(true);
    }

    public void showLogOut(){
        signedInCanvas.setVisible(false);
        signedOutCanvas.setVisible(true);
    }

    public String getEmailText(){
        if ( emailText.getValue()==null )
            return "";
        //else
        return emailText.getValue().toString();
    }

    public boolean getShowEmail(){
        return showEmailCheckBoxItem.getValueAsBoolean();
    }


}
