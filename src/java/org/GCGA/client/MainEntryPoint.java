/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.GCGA.client;

import org.GCGA.client.GUI.TopPanel;
import org.GCGA.client.GUI.DrawingTabSet;
import org.GCGA.client.GUI.MenuCanvas;
import com.google.gwt.core.client.EntryPoint;
//import com.google.gwt.user.client.ui.Label;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Main entry point.
 *
 * @author Marcello
 */
public class MainEntryPoint implements EntryPoint {
    /** 
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
 
         HLayout leftLayout = new HLayout();
         leftLayout.setWidth100();
         leftLayout.setHeight100();
         leftLayout.setLayoutMargin(0);
         leftLayout.setMembersMargin(5);
         
         //DrawingTabSet dts = new DrawingTabSet();

         MenuCanvas.init( DrawingTabSet.get() );
         leftLayout.addMember(MenuCanvas.get());
         VLayout centralLayout = new VLayout();
         centralLayout.setWidth100();
         centralLayout.setHeight100();
         leftLayout.setLayoutMargin(0);
         leftLayout.setMembersMargin(5);

         centralLayout.addMember(TopPanel.get());
         centralLayout.addMember( DrawingTabSet.get() );

         leftLayout.addMember(centralLayout);
         //leftLayout.setShowEdges(true);

           // leftLayout.setBackgroundColor("white");
         leftLayout.draw();
LoginHandler.get().doLogIn("pippo", "franco");
 //       RootPanel.get().add(canvas);
 //       canvas.draw();
    }

}
