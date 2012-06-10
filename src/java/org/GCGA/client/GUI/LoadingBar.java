/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.widgets.Canvas;
//import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

/**
 *
 * @author Eurialo
 */
public class LoadingBar extends Canvas  {

    //private static SWFWidget bar;
    private Canvas cbar;

    private static final short BAR_WIDTH = 160;

//    private Progressbar pbar;

    private int min;
    private int max;
    private int value;

    public LoadingBar(){
        super();
        this.setWidth(BAR_WIDTH);
        this.setPadding(2);
        this.setBorder("2px solid white");
        this.setHeight(16);

        //RootPanel.get("divLoader").add(this);
    /*
        pbar = new Progressbar();
         pbar.setWidth(1);
         pbar.setHeight(20);
         pbar.setVertical(false);
         this.addChild(pbar);
         this.setID("loadingBar");
         */
        
         cbar = new Canvas();
         cbar.setWidth(1);
         cbar.setHeight(20);
         cbar.setBackgroundColor("gray");
         cbar.setRedrawOnResize(true);
         this.addChild(cbar);
         //this.setID("loadingBar");



        //this.hide();
    }
/*
	private native void alert(String message)/*-{
		alert(message);
	}-* /;
*/
        public native void forceRedraw(JavaScriptObject elm)/*-{
         //var elm = document.getElementById(elmId);
         alert(elm.id + " " + elm.innerHTML);
         elm.style.display='none';
            var redrawFix = elm.offsetHeight;
            elm.style.display='block'; // or other value if required
	}-*/;


                private void showLoader(){

//		System.out.println(tabs + "ShowLoader " + (bar==null));
//		tabs += "\t";
                    /*
        bar = new SWFWidget("SWF/LoadingBar.swf", BAR_WIDTH, 60);
        bar.addParam("wmode", "transparent");
        bar.addParam("codebase", "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab");
        //bar.addInitializationListener(this);
        this.addChild(bar);
        this.setOpacity(75);
*/
		this.bringToFront();
	}

	private void hideLoader(){
//		tabs = tabs.length() > 0 ? tabs.substring(0, tabs.length()-1 ) : "";
//		System.out.println(tabs + "HideLoader " + (bar==null) );
/*
		if (bar!=null)
			bar.setVisible(false);
		this.setVisible(false);*/
           // this.clear();

//		LoadingBar.getInstance().sendToBack();
	}

        public void advance(){
            value++;
            double val = value;
            val /= (max-min);
            val*=BAR_WIDTH;
            cbar.animateResize((int)val, cbar.getHeight());

            cbar.redraw();

            this.redraw();

            //alert(value + " | "+ val );

        }

	public void startLoader(int iniziale, int massimo){
                min=value=iniziale;
                max=massimo;
		this.showLoader();

//		LoaderAnimation.start();
	}
	public void stopLoader(){
//		LoaderAnimation.stop();
                //bar.root().invokeMethod("barPause", null );
		this.hideLoader();
		//System.out.println("HIDING");
	}

	public void redrawLabel(){
		this.redraw();
	}


}
