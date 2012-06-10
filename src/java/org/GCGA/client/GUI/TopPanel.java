/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.widgets.Canvas;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Marcello
 */
public class TopPanel extends Canvas{
    private static TopPanel thisPanel = null;

    
    private TopPanel(int panelWidth, int panelHeight) {
        super();
        this.setWidth(panelWidth);
        this.setHeight(panelHeight);

        this.setCanDragResize(true);
        this.setShowResizeBar(true);
        Canvas wrapper = new Canvas();
        wrapper.setMargin(0);
        wrapper.setTop(Utility.MARGIN);
        wrapper.setWidth(panelWidth);
        wrapper.setHeight(panelHeight);
        wrapper.setCanDragResize(true);

        wrapper.addChild(NewsPanel.get());
        PersonalInfoPanel.init(panelWidth - NewsPanel.get().getWidth() - Utility.MARGIN, panelHeight);
        PersonalInfoPanel.get().setLeft(NewsPanel.get().getRight() + Utility.MARGIN);
        wrapper.addChild(PersonalInfoPanel.get());

        this.addChild(wrapper);
        try{
            if ( Cookies.getCookie(Utility.SHOW_TOP_PANEL_COOKIE_NAME )!=null )
                this.hide();
        }catch(Exception e){

        }

    }

    public static void init(int panelWidth, int panelHeight){
        if ( thisPanel == null)
            thisPanel = new TopPanel(panelWidth, panelHeight);
    }

    public static TopPanel get(){
        if ( thisPanel == null)
            thisPanel = new TopPanel(Utility.DEFAULT_PANELS_WIDTH + Utility.W_MARGIN, Utility.DEFAULT_TOP_PANEL_HEIGHT);

        return thisPanel;
    }
    
    public static void collapse(){
        if (thisPanel!=null)
            thisPanel.hide();
    }

}
