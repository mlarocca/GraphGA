/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.google.gwt.user.client.EventListener;
import com.smartgwt.client.widgets.Canvas;

import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import org.GCGA.client.utility.Utility;

public class ButtonImg extends Canvas implements EventListener {

	private static final String IMAGE_PATH = Utility.IMAGE_DIR + "menu/";

	private final Image imgNormal;
//	private final Image imgDown;
	private final Image imgOver;

	public ButtonImg(String imgN, String imgO,  int width, int height) { /*String imgD,*/
		super();
		//this.active = false;

		//label.setAutoWidth();
		//this.setAutoWidth();
                this.setWidth(width);
                this.setHeight(height);

		imgNormal = new Image(IMAGE_PATH + imgN, width, height);
//		ttImage = new Img(IMAGE_PATH + ICON_PREFIX+ img);

		imgOver = new Image(IMAGE_PATH + imgO, width, height);
/*
                imgDown = new Image(IMAGE_PATH + imgD, width, height);

		imgDown.setLeft(0);
		imgDown.setTop(0);
		imgDown.setWidth(width);
		imgDown.setHeight(height);
                
                imgDown.setVisible(false);
                
                this.addChild( imgDown );
*/
		imgOver.setLeft(0);
		imgOver.setTop(0);
		imgOver.setWidth(width);
		imgOver.setHeight(height);
                
                imgOver.setVisible(false);
                
                this.addChild( imgOver );
                
		imgNormal.setLeft(0);
		imgNormal.setTop(0);
		imgNormal.setWidth(width);
		imgNormal.setHeight(height);
                
                imgNormal.setVisible(true);
                
                this.addChild( imgNormal );
                
//		final ButtonImg thisButton = this;

/*
                this.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent event) {
                //            	thisButton.setOpacity(75);
                                imgNormal.setVisible(false);
                                imgOver.setVisible(false);
                                imgDown.setVisible(true);
        //                        onMouseDown(event);
                    }
                });

                this.addMouseUpHandler(new MouseUpHandler() {
                    public void  onMouseUp(MouseUpEvent event) {
                        imgOver.setVisible(true);
                        imgDown.setVisible(false);
                    }
                });
*/
		this.addMouseOverHandler(new MouseOverHandler(){
                    public void onMouseOver(MouseOverEvent event) {
                        imgOver.setVisible(true);
                        imgNormal.setVisible(false);
                    }
		});

		this.addMouseOutHandler(new MouseOutHandler(){
                    public void onMouseOut(MouseOutEvent event) {
                        imgOver.setVisible(false);
                        imgNormal.setVisible(true);
                    }
		});

	}

        @Override
        public void setDisabled(boolean disabled){
            super.setDisabled(disabled);
            if (disabled){
//                imgDown.setVisible(false);
                imgOver.setVisible(false);
                imgNormal.setVisible(true);
                this.setOpacity(50);
                //this.setVisible(false);
            }
            else
                this.setOpacity(100);
                //this.setVisible(true);
        }
}