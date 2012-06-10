/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import java.util.ArrayList;
import org.GCGA.client.utility.DataRecord;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Eurialo
 */
public class LegendaPanel extends Canvas{

    private final int width;
    private int rowSize;

    private static final byte DEFAULT_ROW_SIZE = 20;
    private static final byte COLOR_BOX_SIZE = 15;

    private ArrayList<LegendaEntry> entries = new ArrayList<LegendaEntry>();

    private Canvas separator = null;

    private static final byte SEPARATOR_MARGIN = 1;

    private static final String LEGENDA_BORDER = "1px dashed black";
    private static final String SEPARATOR_BORDER = "1px dashed red";
    private static final String SEPARATOR_BACKGROUND = "rgba(255,0,0,0.25)";

    public LegendaPanel(int w){
        this(w, DEFAULT_ROW_SIZE);
    }

    public LegendaPanel(int w, int row_size) {
        super();
        width = w;
        rowSize = row_size;
        this.setWidth(width);
        this.setHeight(rowSize);

        setBackgroundColor("gray");
        setBorder(LEGENDA_BORDER);
        setOpacity(50);

        UtilityFunctions.setWidgetPrompt(this, "Legenda", "Click on the color boxes to hide/show curves in the diagram.<br/>" +
                                                          "At least one of the entries in the main selected category must be shown in order to draw the diagram.");

    }

    public LegendaEntry addEntry(String text, String color, boolean isVisible){
        
        LegendaEntry l = new LegendaEntry(text, color, isVisible);
        entries.add(l);
        this.setHeight( entries.size() * rowSize);
        l.setTop((entries.size()-1)*rowSize);
        this.addChild(l);
        return l;
    }

    public int getNextColorIndex(){
        return entries.size();
    }

    public void addSeparator(){
        if ( separator == null ){
            separator = new Canvas();
            separator.setWidth(width - 2 * SEPARATOR_MARGIN);
            separator.setHeight(this.getHeight() - 2 * SEPARATOR_MARGIN);
            separator.setTop(SEPARATOR_MARGIN);
            separator.setLeft(SEPARATOR_MARGIN);
            separator.setBackgroundColor(SEPARATOR_BACKGROUND);
            separator.setBorder(SEPARATOR_BORDER);
            this.addChild(separator);
            separator.sendToBack();
        }
    }

    public class LegendaEntry extends Canvas{

        final Canvas colorBox = new Canvas();
        final String color;
        /*
        private final Canvas colorBox;
        private final LegendaEntry thisEntry = this;
        */
        public LegendaEntry(final String text, final String col, boolean isVisible) {
            super();
            color = col;
            //GeneticAlgorithm.alert("Creation " + text);
            this.setWidth(width);
            this.setHeight(rowSize);
            colorBox.setBackgroundColor(color);
            colorBox.setBorder("1px solid black");
            colorBox.setWidth(COLOR_BOX_SIZE);
            colorBox.setHeight(COLOR_BOX_SIZE);
            colorBox.setTop((rowSize-COLOR_BOX_SIZE )/2 );
            colorBox.setLeft(Utility.MARGIN );

            setShow(isVisible);

            this.addChild(colorBox);
            Label textBox = new Label("<b style='color:black;'>"+ text+"</b>");
            textBox.setTop(0);
            textBox.setLeft(colorBox.getRight() + Utility.MARGIN);
            textBox.setWidth(width - textBox.getLeft() - Utility.MARGIN);   //Just to make it as thin as it can be
            textBox.setHeight(rowSize);

            this.addChild(textBox);
        }

        private void setShow(boolean show){
            if (show){
                colorBox.setBackgroundColor(color);
            }else{
                colorBox.setBackgroundColor(Utility.LIGHT_BACKGROUND);
            }
        }

        public void addClickHandler(final AnimationCallback onClick){
            colorBox.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    onClick.execute(false);

                }
            });
        }
        
        protected void changeStatus(DataRecord dr){
            //GeneticAlgorithm.alert(text+" show! " + thisEntry.show );
            if (dr.isVisible()){
                AnalysisPanel.hideDataSet(dr);
            }else{
                AnalysisPanel.showDataSet(dr);
            }
            setShow(dr.isVisible());
        }
    }

}
