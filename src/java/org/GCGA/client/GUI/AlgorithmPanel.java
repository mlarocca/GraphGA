/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.smartgwt.client.widgets.Canvas;
import org.GCGA.client.utility.Utility;


/**
 *
 * @author Marcello
 */
public class AlgorithmPanel extends Canvas{

    private static AlgorithmPanel thisPanel = null;
    private static SWFWidget movie;

    private AlgorithmPanel(int panelSize) {
            super("AlgorithmPanel");
            // TODO Auto-generated constructor stub
            this.setWidth(panelSize);
            this.setHeight(panelSize);

            movie = new SWFWidget("SWF/Algorithm.swf", panelSize, panelSize);
            movie.addParam("wmode", "transparent");

            this.addChild(movie);
            //RootPanel.get().add(this);
            this.setTop(0);
            this.setHeight(0);
            this.setOpacity(100);
            movie.setVisible(true);        
    }

    public static AlgorithmPanel get(){
        if (thisPanel == null){
            thisPanel = new AlgorithmPanel(Utility.DEFAULT_PANELS_WIDTH);
        }
        return thisPanel;
    }
    
    public void showPanel(){
        movie.setVisible(true);
        this.setVisible(true);        
    }

    public void hidePanel(){
        movie.setVisible(false);
        this.setVisible(false);
    }
/*
    private native void alert(String message)/*-{
		alert(message);
    }-* /;
 * 
 */
}
