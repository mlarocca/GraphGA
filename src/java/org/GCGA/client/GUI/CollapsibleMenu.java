package org.GCGA.client.GUI;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import java.util.ArrayList;

import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

public class CollapsibleMenu extends Canvas{
    private final Label menuText;
    private final ArrayList<Entry> entries = new ArrayList();
    private int collapsedHeight;
    private int extendedHeight;
    private boolean collapsed;

    private final int collapsedWidth;
    private final int extendedWidth;

    private boolean blocked = false;    //Can't add more entries?


    public CollapsibleMenu(String menuText, int collapsedWidth, int extendedWidth) {
        super();
        this.menuText = new Label("<div style='width:100%;'><center>"+menuText+"</center></div>");
        this.menuText.setHeight(Utility.INSTRUCTIONS_PANEL_HEIGHT);
        this.menuText.setWidth(collapsedWidth);
        this.collapsedWidth = collapsedWidth;
        this.extendedWidth = extendedWidth;
        this.collapsedHeight = this.menuText.getOffsetHeight();
        //this.entries = new Entry[entriesText.length];
    //    button = new CollapseButton();
        init();
    }

    public CollapsibleMenu(String menuText, int collapsedWidth, int extendedWidth, String entriesText[], AnimationCallback entriesOnChange[] ) {
        super();
        this.menuText = new Label("<div style='width:100%;'><center>"+menuText+"</center></div>");
        this.menuText.setHeight(Utility.INSTRUCTIONS_PANEL_HEIGHT);
        this.menuText.setWidth(collapsedWidth);
        this.collapsedWidth = collapsedWidth;
        this.extendedWidth = extendedWidth;
        this.collapsedHeight = this.menuText.getOffsetHeight();
        //this.entries = new Entry[entriesText.length];
    //    button = new CollapseButton();
        init();
        if (entriesText.length <=0 || entriesText.length != entriesOnChange.length)
            throw new IllegalArgumentException("Illegal menu entries");
        for(int i=0; i<entriesText.length; i++){
            this.addEntry(entriesText[i], entriesOnChange[i]);
        }
        this.collapse();
    }

    /**
     *
     * @param entryText - Menu entry text
     * @param onClick - method to call on click
     * @throws IllegalStateException - Entries may be added iff the menu has not be blocked yet (i.e. iff hasn't been finalized <=> collapsed)
     */
    public void addEntry(String entryText, AnimationCallback onClick) throws IllegalStateException{
        if ( blocked )
            throw new IllegalStateException("Unable to add any other entry: the menu has been already blocked");
        Entry e;
        String border = this.entries.size() > 0 ? "" : " border-top:1px dashed black;";
        this.entries.add(e = new Entry("<div style='width:100%; padding-left:"+Utility.MARGIN+"px;"+border+"'>"+entryText+"</div>", onClick));
        this.addChild(e);
        e.setLeft(0);
        e.setTop(this.extendedHeight);
        //e.setDisabled(false);
        this.extendedHeight += e.getHeight();

    }

    private void init(){
        collapsed = false;
        this.setMargin(0);
        this.setPadding(0);
        this.addChild(menuText);
        menuText.setLeft(0);
        menuText.setTop(0);
        
        this.extendedHeight = menuText.getOffsetHeight();
//        this.menuText.setVisible(false);
        //this.menuText.setHeight(height);
        for (Label e:entries){

        }
        menuText.setHoverWidth(Utility.HOVER_WIDTH);

        this.setHeight(this.extendedHeight);
        this.setWidth(extendedWidth);

        this.setAnimateTime(500);

        menuText.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (collapsed)
                    expand();
                else
                    collapse();
            }
        });
    }

    /**
     * The menu is complete - no more entries may be created;
     */
    @Override
    public void finalize(){
        collapse();
    }

    private void collapse(){
        blocked = true;
        int i = entries.size()-1;
        for(; i>0; i--)
            entries.get(i).animateHide(AnimationEffect.FADE,null);

        entries.get(0).animateHide(AnimationEffect.FADE, new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                menuText.setDisabled(false);
                collapsed = true;
                for (Label e : entries)
                    removeChild(e);
//                removeChild(button);
//                menuText.animateShow(AnimationEffect.SLIDE);
                animateResize(collapsedWidth, collapsedHeight, new AnimationCallback() {
                    public void execute(boolean earlyFinish) {
                        //extended.setVisible(false);
                        //extended.animateHide(AnimationEffect.)
                        menuText.setDisabled(false);
                        UtilityFunctions.changeWidgetPromptText(menuText, "Click to expand", "Click here in order to open the menu panel.");
                    }
                });
            }
        });

    }

    private void expand(){
        menuText.setDisabled(true);
        collapsed = false;
//        setPrompt("");
        this.animateResize(extendedWidth, extendedHeight, new AnimationCallback() {

            public void execute(boolean earlyFinish) {
                //menuText.animateHide(AnimationEffect.SLIDE);
                //int top = menuText.getBottom();
                for (Entry e :entries){
                    addChild(e);
                    e.animateShow(AnimationEffect.FADE);
                }
                menuText.setDisabled(false);
                UtilityFunctions.changeWidgetPromptText(menuText, "Click to close", "Click here in order to minimize the menu panel.");

//                addChild(button);
            }
        });
    }

    private class Entry extends Label{
        private final Entry thisEntry = this;

        public Entry(String text, final AnimationCallback onClick) {
            super(text);
            this.setBackgroundColor("white");
            this.setHeight(Utility.INSTRUCTIONS_PANEL_HEIGHT);
            this.setWidth(extendedWidth - 2*Utility.MARGIN );
            this.addClickHandler(new ClickHandler() {

                public void onClick(ClickEvent event) {
                    //UtilityFunctions.alert("Not yet");
                    onClick.execute(false);
                }
            });

            this.addMouseOverHandler(new MouseOverHandler() {

                public void onMouseOver(MouseOverEvent event) {
                    thisEntry.setBackgroundColor("orange");
                }
            });

            this.addMouseOutHandler( new MouseOutHandler() {

                public void onMouseOut(MouseOutEvent event) {
                    thisEntry.setBackgroundColor("white");
                }
            });
        }


    }

}
