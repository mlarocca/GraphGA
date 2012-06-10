package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Owner
 */
public class TabLabel extends Button{

    private boolean highlighted = false;

    private static final String TITLE_STYLE_PREFIX = "<center><b style='color:red'>";
    private static final String TITLE_STYLE_SUFFIX = "</b></center>";

    private static final byte BORDER_WIDTH = 1;
    private static final double CHAR_GAP = 6;
    private static final double CHAR_WIDTH = 6;
    private static final String BORDER = BORDER_WIDTH+"px solid " + Utility.LIGHT_BACKGROUND;
    private static final String BORDER_SELECTED = BORDER_WIDTH+"px solid " + "red;";
    private static final String BORDER_HIGHLIGHT = BORDER_WIDTH+"px dashed " + "crimson;";

    public TabLabel(String title, int maxWidth){
        super(TITLE_STYLE_PREFIX+title+TITLE_STYLE_SUFFIX);

        int width = (int) Math.min( CHAR_GAP + title.length() * CHAR_WIDTH , maxWidth);
        setWidth( width );
//        setHeight(height - 2 * BORDER_WIDTH);
//        setAutoFit(true);
        
        setBorder(BORDER);
//        setBackgroundColor(Utility.DARK_BACKGROUND);
    }
    public void setOnClick(final AnimationCallback onClick, final AnimationCallback onCTRLClick){
        addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if ( event.isCtrlKeyDown() )
                    onCTRLClick.execute(false);
                else
                    onClick.execute(false);
             }
        });

    }

    public void highlightTab(){
        highlighted = !highlighted;

        setBorder(highlighted ? BORDER_HIGHLIGHT : BORDER);
    }

    public void selectTab(){
        highlighted = false;
        setBorder(BORDER_SELECTED);
        //setBackgroundColor("yellow");
        setDisabled(true);
    }

    public void deselectTab(){
        highlighted = false;
        setBorder(BORDER);
//        setBackgroundColor("yellow");
        setDisabled(false);
    }



}
