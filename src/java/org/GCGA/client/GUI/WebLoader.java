/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.smartgwt.client.widgets.Canvas;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
/**
 *
 * @author Marcello
 */
public class WebLoader extends Canvas {

    private static WebLoader loader = null;
	private static SWFWidget bar;
//	private static Label title;
	
	private static final short WIDTH = 200;
	private static final byte HEIGHT = 60;
//	private static final String TITLE_STYLE = "labelLoader";
	/**
	 * 
	 */
	private WebLoader() {
		super();
		// TODO Auto-generated constructor stub
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);

		bar = new SWFWidget("SWF/LoadingBar.swf", 200, 60);
		bar.addParam("wmode", "transparent");

		this.addChild(bar);
		this.setOpacity(85);
                RootPanel.get().add(this);
		
		this.hide();
	}
	 
	public static WebLoader getInstance() {
            if (loader == null){
                loader = new WebLoader();
            }
	     
            return loader;
	   }
	
	private static void adjustPostition(){
		int x = (Window.getClientWidth() - getInstance().getWidth()) /2;
		int y = (Window.getClientHeight() - getInstance().getHeight()) /2 ;
//		System.out.println(Window.getClientHeight() +" - "+ LoaderLabel.getInstance().getHeight() + " | " + x + " , " + y);
		getInstance().setLeft( x );
		getInstance().setTop( y );
		
	}
	
	public static void showLoader(){
//		System.out.println(tabs + "ShowLoader " + (bar==null));
//		tabs += "\t";
		if (bar!=null)
			bar.setVisible(true);
		WebLoader.adjustPostition();
		getInstance().setVisible(true);
		getInstance().bringToFront();
	}

	public static void hideLoader(){
//		tabs = tabs.length() > 0 ? tabs.substring(0, tabs.length()-1 ) : "";
//		System.out.println(tabs + "HideLoader " + (bar==null) );
		
		if (bar!=null)
			bar.setVisible(false);
		getInstance().setVisible(false);
//		LoaderLabel.getInstance().sendToBack();
	}
	
	public static void startLoader(){
		
		WebLoader.showLoader();
//		LoaderAnimation.start();
	}
	public static void stopLoader(){
//		LoaderAnimation.stop();
		WebLoader.hideLoader();
		//System.out.println("HIDING");
	}
	
}
