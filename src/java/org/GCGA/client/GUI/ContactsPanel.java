package org.GCGA.client.GUI;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import org.GCGA.client.LoginHandler;
import org.GCGA.client.ajax.EmailSender;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;


public class ContactsPanel extends Canvas{

    private static ContactsPanel thisPanel = null;

    private static Canvas wrapperFrom = new Canvas();
    private static Canvas wrapperBody = new Canvas();
    private static Canvas wrapperSubject = new Canvas();
    private static TextItem emailFromAddress;
    private static TextItem emailSubject;
    private static TextAreaItem emailBody;
    private static Button sendButton;
    private static Button resetButton;



    private static final byte BUTTON_WIDTH = 60;
    private static final byte BUTTON_MARGIN = 2;
    //private static final short TEXT_FIELD_WIDTH = 225;
    private static final byte LABEL_WIDTH = 50;
    private static final byte TEXT_FIELD_HEIGHT = 30;


    private ContactsPanel(int panelWidth, int panelHeight) {
        super();
        this.setWidth(panelWidth);
        this.setHeight(panelHeight);
        this.setLeft(0);
        this.setTop(0);
        this.setMargin(0);
        this.setPadding(0);


        sendButton = new Button("Send");
        sendButton.setWidth(BUTTON_WIDTH);
        sendButton.setTop(Utility.MARGIN);
        sendButton.setLeft( LABEL_WIDTH + 2 * Utility.MARGIN + BUTTON_MARGIN);
        sendButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                sendEmail();
            }
        });

        UtilityFunctions.setWidgetPrompt(sendButton, "Send", "After filling all the due filds, click on this button to send your message.");

        this.addChild(sendButton);

        resetButton = new Button("Reset");
        resetButton.setWidth(BUTTON_WIDTH);
        resetButton.setTop(Utility.MARGIN);
        resetButton.setLeft( panelWidth - resetButton.getWidth() -  Utility.MARGIN - BUTTON_MARGIN);
        resetButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                resetForm();
            }
        });

        UtilityFunctions.setWidgetPrompt(resetButton, "Reset", "Reset the form content.");

        this.addChild(resetButton);


        final DynamicForm emailFromForm = new DynamicForm();

        wrapperFrom = new Canvas();

        wrapperFrom.setWidth( panelWidth );
        wrapperFrom.setTop(sendButton.getBottom());
        wrapperFrom.setLeft(0);
        wrapperFrom.setMargin(0);
        wrapperFrom.setPadding(0);

        Label fromLabel = new Label("<b style='color:red;'>From:</b>");
        fromLabel.setWidth(LABEL_WIDTH);
        fromLabel.setHeight(TEXT_FIELD_HEIGHT);
        fromLabel.setLeft(Utility.MARGIN);
        fromLabel.setTop(Utility.MARGIN);
        fromLabel.setAlign(Alignment.RIGHT);

        wrapperFrom.addChild(fromLabel);

        emailFromForm.setTop( Utility.MARGIN );
        emailFromForm.setLeft( fromLabel.getRight() + Utility.MARGIN );

        emailFromAddress = new TextItem();
        emailFromAddress.setShowTitle(false);

        
        emailFromAddress.setTitleAlign(Alignment.RIGHT);
        //verticesItem.setRequired(true);
        //         emailFromAddress.setLeft(Utility.MARGIN);

        emailFromAddress.setWidth( panelWidth - fromLabel.getRight() - 2*Utility.MARGIN );

        UtilityFunctions.setWidgetPrompt(emailFromAddress, "Insert your email", "Your email will be used only and exclusively to answer your message; it won't be used for any other purpose nor sold or transferred in any way to third parts.");

        //emailFromAddress.disable();
        emailFromForm.setFields(emailFromAddress);
        wrapperFrom.addChild(emailFromForm);
        this.addChild(wrapperFrom);



        final DynamicForm emailSubjectForm = new DynamicForm();

        wrapperSubject = new Canvas();
        wrapperSubject.setWidth( panelWidth );
        wrapperSubject.setTop( wrapperFrom.getTop() + TEXT_FIELD_HEIGHT );
        wrapperSubject.setLeft( 0 );

        Label emailSubjectLabel = new Label("<b style='color:red;'>Subject:</b>");
        emailSubjectLabel.setWidth(LABEL_WIDTH);
        emailSubjectLabel.setHeight(TEXT_FIELD_HEIGHT);
        emailSubjectLabel.setLeft(Utility.MARGIN);
        emailSubjectLabel.setTop(Utility.MARGIN);
        emailSubjectLabel.setAlign(Alignment.RIGHT);

        wrapperSubject.addChild(emailSubjectLabel);

        emailSubject = new TextItem();

        emailSubject.setWidth( panelWidth - emailSubjectLabel.getRight() - 2 * Utility.MARGIN );
        emailSubject.setShowTitle(false);

        emailSubjectForm.setFields(emailSubject);

        emailSubjectForm.setTop( Utility.MARGIN );
        emailSubjectForm.setLeft(emailSubjectLabel.getRight() +  Utility.MARGIN );

        //         emailSubjectForm.setHeight(100);

        wrapperSubject.addChild(emailSubjectForm);

        this.addChild(wrapperSubject);
        
        
        
        final DynamicForm emailBodyForm = new DynamicForm();

        wrapperBody = new Canvas();
        wrapperBody.setWidth( panelWidth );
        wrapperBody.setTop( wrapperSubject.getTop() + TEXT_FIELD_HEIGHT );
        wrapperBody.setLeft( 0 );

        emailBody = new TextAreaItem();

        emailBody.setWidth( panelWidth - 2 * Utility.MARGIN );
        emailBody.setHeight(panelHeight - wrapperSubject.getTop() - TEXT_FIELD_HEIGHT - Utility.MARGIN );
        emailBody.setShowTitle(false);

        emailBodyForm.setFields(emailBody);

        emailBodyForm.setTop( Utility.MARGIN );
        emailBodyForm.setLeft( Utility.MARGIN );

        //         emailBodyForm.setHeight(100);

        wrapperBody.addChild(emailBodyForm);

        this.addChild(wrapperBody);

    }

    public static ContactsPanel get(){
        if ( thisPanel == null)
            thisPanel = new ContactsPanel(Utility.DEFAULT_PANELS_WIDTH, Utility.DEFAULT_PANELS_WIDTH);

        return thisPanel;
    }

    public void sendEmail(){
        EmailSender.get().sendEmail(getEmailFromAddress(), getEmailSubject(), getEmailBody());
        getEmailFromAddress();

    }

    private void resetForm(){
        emailBody.clearValue();
        if (!emailFromAddress.isDisabled())
            emailFromAddress.clearValue();
        emailSubject.clearValue();
    }

    private String getEmailFromAddress(){
        if ( emailFromAddress.getValue()==null )
            return "";
        //else
        return emailFromAddress.getValue().toString();
    }

    private String getEmailSubject(){
        if ( emailSubject.getValue()==null )
            return "";
        //else
        return emailSubject.getValue().toString();
    }

    private String getEmailBody(){
        if ( emailBody.getValue()==null )
            return "";
        //else
        return emailBody.getValue().toString();
    }

    public void init(){
        if (LoginHandler.get().isLogged()){
            emailFromAddress.setValue(LoginHandler.get().getEmail());
            emailFromAddress.setDisabled(true);
        }else{
            emailFromAddress.setDisabled(false);
        }
    }

}