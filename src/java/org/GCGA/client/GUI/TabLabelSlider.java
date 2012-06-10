/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import java.util.ArrayList;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Owner
 */
public class TabLabelSlider extends Canvas{

    private final TabLabelSlider thisSlider = this;

    private final ArrayList<TabLabel> tabs = new ArrayList<TabLabel>();

    private ButtonImg leftArrow, rightArrow;

    private final int width;
    private final int height;

    private int leftIndex;  //Index of the first tab shown on the left
    private int rightIndex; //Index of the first tab shown on the right

    private boolean arrowsEnabled = false;

//    private final byte MAX_TABS;

    private final int MAX_TAB_WIDTH ;       //Used to assure that at least one tab can be shown together with the arrows;
    private static final byte ARROWS_SIZE = 20;
    private static final byte MARGIN = 1;

    private static final String ARROW_LEFT_NORMAL = "arrow-left_Normal.png";
    private static final String ARROW_LEFT_OVER = "arrow-left_Over.png";
//    private static final String ARROW_LEFT_DOWN = "arrow-left_Down.png";

    private static final String ARROW_RIGHT_NORMAL = "arrow-right_Normal.png";
    private static final String ARROW_RIGHT_OVER = "arrow-right_Over.png";
//    private static final String ARROW_RIGHT_DOWN = "arrow-right_Down.png";

    public TabLabelSlider(int finalWidth, int height) {
        width = finalWidth;
        this.height = height;
        MAX_TAB_WIDTH = width - 2 * Utility.MARGIN - 2 * ARROWS_SIZE;

        //MAX_TABS = (byte) Math.floor( width /(TAB_WIDTH+MARGIN));
        this.setWidth(width);
        this.setHeight(height);
        leftIndex = 0;
    }

    public void addTab(String tabTitle, final AnimationCallback onClick, final AnimationCallback onCTRLClick){
        int lastIndex = tabs.size()-1;
        final TabLabel newTab = new TabLabel(tabTitle, MAX_TAB_WIDTH );
        newTab.setOnClick( new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                onClick.execute(false);
                thisSlider.selectTab(newTab);
            }
        },new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                onCTRLClick.execute(false);
                thisSlider.highlightTab(newTab);
            }
        } );
        tabs.add(newTab);

        if ( lastIndex < 0 ){
            newTab.setLeft(0);
            this.addChild(newTab);
            rightIndex = 0;
        }
        else if ( !arrowsEnabled ){
//            GeneticAlgorithm.alert(rightIndex + " -> " + tabs.get(rightIndex).getTitle() + " | " + (tabs.get(rightIndex).getRight() + MARGIN + newTab.getWidth()) + " < " + width);
            if ( tabs.get(lastIndex).getRight() + MARGIN + newTab.getWidth() < width ){
                newTab.setLeft( tabs.get(lastIndex).getRight() + MARGIN );
                this.addChild(newTab);
            } else {
                arrowsEnabled = true;
                //Removes last tab on the rightIndex

                //Checks if it is necessary to cut one more tab in order to paint arrows
                if ( tabs.get(lastIndex).getRight() + 2 * Utility.MARGIN + 2 * ARROWS_SIZE > width )
                    this.removeChild(tabs.get(lastIndex--));

                TabLabel tmpTab;
                
                for(int i=leftIndex; i < lastIndex; i++){
                    tmpTab = tabs.get(i);
                    tmpTab.animateMove(tmpTab.getLeft() + ARROWS_SIZE + Utility.MARGIN, tmpTab.getTop());
                }

                tmpTab = tabs.get(lastIndex);
                tmpTab.animateMove(tmpTab.getLeft() + ARROWS_SIZE + Utility.MARGIN, tmpTab.getTop(), new AnimationCallback() {

                    public void execute(boolean earlyFinish) {
                        thisSlider.addArrows();
                    }
                });
            }
            rightIndex = lastIndex;
        }
        else //do not change rightIndex!
            rightArrow.setDisabled(false);
    }

    private void addArrows(){
        leftArrow = new ButtonImg(ARROW_LEFT_NORMAL, ARROW_LEFT_OVER, ARROWS_SIZE, ARROWS_SIZE);
        rightArrow = new ButtonImg(ARROW_RIGHT_NORMAL, ARROW_RIGHT_OVER, ARROWS_SIZE, ARROWS_SIZE);

        leftArrow.setLeft(0);
        rightArrow.setLeft( tabs.get(rightIndex).getRight() + Utility.MARGIN );
        leftArrow.setTop((height-ARROWS_SIZE)/2);
        rightArrow.setTop(leftArrow.getTop());

        this.addChild(leftArrow);
        this.addChild(rightArrow);

        leftArrow.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                slideLeft();
            }
        });

        rightArrow.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                slideRight();
            }
        });

        leftArrow.setDisabled(true);
    }

    public void clearTab(){
        for (Canvas c : this.getChildren())
            this.removeChild(c);

        tabs.clear();
        rightArrow = leftArrow = null;
        leftIndex = 0;
        arrowsEnabled = false;
    }

    private void selectTab(TabLabel sel ){
        for (TabLabel t: tabs)
            t.deselectTab();
        sel.selectTab();
    }

    private void highlightTab(TabLabel sel ){
        sel.highlightTab();
    }

    protected void selectTab(byte index){
        if (index < 0 || index>= tabs.size() )
            return;
        tabs.get(index).selectTab();
    }

    private void slideLeft(){
        //rightIndex--;
        int newX = leftArrow.getRight() + Utility.MARGIN;

        TabLabel newTab = tabs.get(leftIndex-1);

        newTab.setOpacity(0);
        newTab.setLeft(newX);
 
        int dx = newTab.getWidth() + MARGIN, oldNewX;

        TabLabel tmp = null;
        boolean overMargin = false;

        int i=leftIndex;
        while(i<=rightIndex){
        //for (int i=leftIndex; i<tabs ; i++){
            oldNewX = newX;
            tmp = tabs.get(i);
            newX = tmp.getLeft() + dx;
            if (newX + tmp.getWidth() + Utility.MARGIN + ARROWS_SIZE <= width )
                tmp.animateMove(newX, tmp.getTop());
            else{
                overMargin = true;
                newX = oldNewX;
                //INVARIANTE: almeno una tab è stata aggiunta (eventualmente newTab) => i>=leftIndex > newLeftIndex [=leftIndex-1]
                tmp = tabs.get(i-1);
                break;
            }
            i++;
        }
        if ( overMargin ){
            int j = i;
            while (j <= rightIndex )
                //tabs.get(j).animateFade(0);
                removeChild(tabs.get(j++));
        }else{
            //INVARIANTE: i > rightIndex
            while(i < tabs.size()){
                oldNewX = newX;
                newX += tmp.getWidth() + MARGIN;

                tmp = tabs.get(i);
                if (newX + tmp.getWidth() + Utility.MARGIN + ARROWS_SIZE <= width ) {
                    tmp.setOpacity(0);
                    tmp.setLeft(newX);
//                    tmp.setTop(tmp.getTop());
                    this.addChild(tmp);
                    tmp.animateFade(100);
                }
                else{
                    newX = oldNewX;
                    //INVARIANTE: almeno una tab è stata aggiunta => i>leftIndex
                    tmp = tabs.get(i-1);
                    break;
                }
                i++;
            }
        }

        rightIndex = i-1;

        //INVARIANTE: leftIndex<=rightIndex => tmp can't be null at this point
        //INVARIANTE: tmp == <last drawn tab>;
        rightArrow.animateMove(newX + tmp.getWidth() + Utility.MARGIN, rightArrow.getTop());

        this.addChild(newTab);
        newTab.animateFade(100);

        leftIndex--;

        if ( rightIndex < tabs.size() - 1 )
            rightArrow.setDisabled(false);
        if (leftIndex <=0 ){
            leftArrow.setDisabled(true);
        }
    }

    private void slideRight(){

        TabLabel newTab = tabs.get(rightIndex+1);
        //rightArrow.setLeft(width-rightArrow.getWidth());
//Determines the position of the right arrow;
        int newX = width - rightArrow.getWidth() - (leftArrow.getWidth() + Utility.MARGIN);

        //Subtract the first Tab
        //INVARIANTE: each Tab can be drawn together with the arrows => at least one tab is drawn
        newX -= ( Utility.MARGIN + newTab.getWidth() );

        int oldNewX = newX;
        int i = rightIndex;
        while( newX >= 0  ){
            oldNewX = newX;
            newX -= (MARGIN + tabs.get(i).getWidth());
            i--;
        }

        newX = width - rightArrow.getWidth() - oldNewX  ;

        rightArrow.animateMove(newX, rightArrow.getTop());

        newX -= ( Utility.MARGIN + newTab.getWidth());

        newTab.setOpacity(0);
        newTab.setLeft(newX);

        boolean overMargin = false;
        
        oldNewX = newX;

        TabLabel tmp = null;

        i = rightIndex;
        while(i >= leftIndex){
            oldNewX = newX;
            tmp = tabs.get(i);
            newX -= (MARGIN + tmp.getWidth());
            if (newX >= leftArrow.getWidth() + Utility.MARGIN )
                tmp.animateMove(newX, tmp.getTop());
            else{
                overMargin = true;
                newX = oldNewX;
                //INVARIANTE: almeno una tab è stata aggiunta (eventualmente newTab) => i<=righIndex < newRightIndex [=rightIndex+1]
                tmp = tabs.get(i+1);
                break;
            }
            i--;
        }
        if ( overMargin ){
            int j = i;
            while (j >= leftIndex )
                //tabs.get(j).animateFade(0);
                removeChild(tabs.get(j--));
        }else{
            //INVARIANTE: i<leftIndex
            while(i >= 0){
                oldNewX = newX;
                newX -= (MARGIN + tmp.getWidth());

                tmp = tabs.get(i);
                if (newX >= leftArrow.getWidth() + Utility.MARGIN ) {
                    tmp.setOpacity(0);
                    tmp.setLeft(newX);
//                    tmp.setTop(tmp.getTop());
                    this.addChild(tmp);
                    tmp.animateFade(100);
                }
                else{
                    break;
                }
                i--;
            }
        }

        leftIndex = i+1;
        rightIndex++;


        //INVARIANTE: leftIndex<=rightIndex => tmp can't be null at this point
        //INVARIANTE: tmp == <last drawn tab>;
        //leftArrow.animateMove(newX - Utility.MARGIN, rightArrow.getTop());

        this.addChild(newTab);
        newTab.animateFade(100);

        if (leftIndex > 0)
            leftArrow.setDisabled(false);
        if (rightIndex >= tabs.size()-1 ){
            rightArrow.setDisabled(true);
        }

    }

}
