
package org.GCGA.client.GUI;

import com.google.gwt.user.client.ui.Image;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

public class CollapsibleLabel extends Canvas{
    private final Label collapsed;
    private final Label extended;
    private int collapsedWidth;
    private int extendedWidth;
    private int height;

    private CollapsibleLabel thisCollapsibleLabel = this;

    private final CollapseButton button;

    public CollapsibleLabel(String collapsedText, String extendedText) {
        super();
        extended = new Label(extendedText);
        collapsed = new Label(collapsedText);
        this.extendedWidth = extended.getOffsetWidth();
        this.height = extended.getOffsetHeight();
        button = new CollapseButton();
        init();
    }
    
    public CollapsibleLabel(String collapsedText, String extendedText, int collapsedWidth, int extendedWidth, int height) {
        super();
        extended = new Label(extendedText);
        collapsed = new Label(collapsedText);
        this.collapsedWidth = collapsedWidth;
        this.extendedWidth = extendedWidth;
        this.height = height;
        button = new CollapseButton();
        init();
    }

    private void init(){
        this.addChild(collapsed);
        this.collapsed.setLeft(Utility.MARGIN);
        this.collapsed.setTop(0);
        this.collapsed.setWidth(collapsedWidth);
        this.collapsed.setVisible(false);
        this.collapsed.setHeight(height);
        this.addChild(extended);
        this.extended.setLeft(Utility.MARGIN);
        this.extended.setTop(0);
        this.extended.setWidth(extendedWidth);
        this.extended.setHeight(height);
        this.collapsed.setDisabled(true);
        
        this.setHoverWidth(Utility.HOVER_WIDTH);
        //this.setHoverHeight(100);

        this.setHeight(height);
        this.setWidth(extendedWidth + 2*Utility.MARGIN );

        this.addChild(button);

        button.setLeft(extendedWidth - button.getWidth() );
//        alert(this.getHeight() + " " +  button.getHeight() + " " + ((this.getHeight() - button.getHeight()) /2) );
        button.setTop( Math.abs((this.getHeight() - button.getHeight()) /2) );


        button.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                collapse();
            }
        });

        this.setAnimateTime(1500);

        this.collapsed.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                expand();
            }
        });
//        this.collapse();
    }

    private void collapse(){
        
        extended.animateHide(AnimationEffect.SLIDE, new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                removeChild(extended);
                removeChild(button);
                collapsed.animateShow(AnimationEffect.SLIDE);
                animateResize(collapsedWidth + 2*Utility.MARGIN, height, new AnimationCallback() {
                    public void execute(boolean earlyFinish) {
                        //extended.setVisible(false);
                        //extended.animateHide(AnimationEffect.)
                        collapsed.setDisabled(false);
                        UtilityFunctions.changeWidgetPromptText(thisCollapsibleLabel, "Click to expand", "Click here in order to open the help panel again.");
                    }
                });
            }
        });

    }

    private void expand(){
        collapsed.setDisabled(true);
        setPrompt("");
        this.animateResize(extendedWidth + 2*Utility.MARGIN, height, new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                collapsed.animateHide(AnimationEffect.SLIDE);
                addChild(extended);
                extended.animateShow(AnimationEffect.SLIDE);
                addChild(button);
            }
        });
    }

    private class CollapseButton extends Canvas{

        public CollapseButton() {
            Image image = new Image(Utility.IMAGE_DIR+"close-button.png",0,0,13,13);
            this.addChild(image);
            this.setWidth(image.getWidth());
            this.setHeight(image.getHeight());

  /*
            this.setWidth(5);
            this.setHeight(5);
            this.setBackgroundColor("gray");
            this.setBorder("1px solid black");
   *
   */
        }


    }

}
