/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.google.gwt.user.client.Cookies;

import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.GCGA.client.LoginHandler;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Marcello
 */
public class PersonalInfoPanel extends Window{

    private static PersonalInfoPanel thisPanel = null;

    private static LoginPanel emailWrapper;

    private static CheckboxItem hidePanelCheckBoxItem;

    private static final byte PERSONAL_PANEL_HEIGHT = 20;

    
    private PersonalInfoPanel(int panelWidth, int panelHeight) {
        super();

        this.setTitle("Personal Info");
        this.setShowMinimizeButton(false);
        this.setShowCloseButton(false);
        this.setCanDragReposition(false);
        this.setWidth(panelWidth);
        this.setHeight(panelHeight);

        Canvas wrapper = new Canvas();
        wrapper.setPadding(2);
        wrapper.setWidth(this.getWidth() - Utility.W_MARGIN_REDUCED - 2 * Utility.MARGIN );
        this.addItem(wrapper);

        emailWrapper = new LoginPanel(wrapper.getWidth());

        wrapper.addChild(emailWrapper);

         Canvas optionsWrapper = new Canvas();

         final DynamicForm optionsForm = new DynamicForm();
         hidePanelCheckBoxItem = new CheckboxItem();
         hidePanelCheckBoxItem.setLeft(Utility.MARGIN);
         hidePanelCheckBoxItem.setTitleOrientation(TitleOrientation.RIGHT);
         hidePanelCheckBoxItem.setTitle("Hide this panel at loading");

         UtilityFunctions.setWidgetPrompt(hidePanelCheckBoxItem, "Hide this panel", "Select this option to have news and personal info panels minimized by default when the website is loaded.<br/><i style='color:red;'>(Requires cookies enabled)</i>");

        try{
            if ( Cookies.getCookie(Utility.SHOW_TOP_PANEL_COOKIE_NAME )!=null )
                hidePanelCheckBoxItem.setValue(true);
        }catch(Exception e){

        }
         
         hidePanelCheckBoxItem.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                if (hidePanelCheckBoxItem.getValueAsBoolean()){
                    if ( Cookies.isCookieEnabled() )
                        Cookies.setCookie(Utility.SHOW_TOP_PANEL_COOKIE_NAME, "1", null);
                }else{
                    if ( Cookies.isCookieEnabled() )
                        Cookies.removeCookie(Utility.SHOW_TOP_PANEL_COOKIE_NAME);
                }
            }
        });

         optionsForm.setFields(hidePanelCheckBoxItem);

         optionsForm.setTop( Utility.MARGIN );
         optionsForm.setLeft( Utility.MARGIN );

         optionsWrapper.addChild(optionsForm);

         optionsWrapper.setBorder(Utility.INNER_PANEL_BORDER);
         optionsWrapper.setTop( emailWrapper.getTop() + emailWrapper.getHeight() + 2* Utility.MARGIN );
         optionsWrapper.setLeft( Utility.MARGIN );
         optionsWrapper.setWidth(wrapper.getWidth());
         optionsWrapper.setHeight(PERSONAL_PANEL_HEIGHT);

         wrapper.addChild(optionsWrapper);

         LoginHandler.get();

    }

    public static void init(int panelWidth, int panelHeight){
        if (thisPanel==null){
            thisPanel = new PersonalInfoPanel(panelWidth, panelHeight);
        }

    }

    public static PersonalInfoPanel get(){
        if (thisPanel==null)
            NewsPanel.init(Utility.NEWS_WINDOW_SIZE, Utility.DEFAULT_TOP_PANEL_HEIGHT);
        return thisPanel;
    }
/*
    public static String getEmail(){
        return emailWrapper.getEmailText().replace("\"", "").replace("'", "");
    }
*/
    public static void showLogin(String email){
        emailWrapper.showLogIn(email);
    }

    public static void hideLogin(){
        emailWrapper.showLogOut();
    }

    public static boolean getShowEmail(){
        return emailWrapper.getShowEmail();
    }

}
