/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import org.GCGA.client.GraphEmbedding;
import org.GCGA.client.ajax.GraphRetriever;
import org.GCGA.client.GraphType;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Eurialo
 */
public class ListPanel extends Canvas{

    final ListPanel thisPanel = this;

    private ListGrid list;

    private int lastVNbr = 0;
    private short lastGraphType = GraphType.ERROR;

    private final Button drawButton;

    private static final int BUTTON_WIDTH = 80;

    int selected;


    private GraphEmbedding[] graphs;

    public ListPanel(int panelSize) {
        super();
        
        drawButton = new Button("Draw");
        drawButton.setDisabled(true);
        drawButton.setWidth(BUTTON_WIDTH);

        UtilityFunctions.setWidgetPrompt(drawButton, "Draw embedding", "The selected graph embedding, if any, will be drawn in a new window.");

        drawButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                //RootPanel.get().add(
                        new GraphWindow(graphs[selected]);// );
            }
        });
        this.addChild(drawButton);

        Button showAllButton = new Button("Show all");
        
        showAllButton.setWidth(BUTTON_WIDTH);
        showAllButton.setLeft( drawButton.getRight() + Utility.MARGIN );
/*
        showAllButton.setPrompt("<b>Show Embeddings</b><p>Every graph embedding stored in our database will be listed, no matter the graph parameters selected from the relative panel.");
        showAllButton.setHoverWidth(Utility.HOVER_WIDTH);
*/
        UtilityFunctions.setWidgetPrompt(showAllButton, "Show Embeddings", "Every graph embedding stored in our database will be listed, no matter the graph parameters selected from the relative panel.");
        showAllButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                thisPanel.retrieveList(0, GraphType.ALL);
            }
        });

        this.addChild(showAllButton);

        list = new ListGrid() {
           
            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                return "font-weight:bold; color:"+Utility.DEFAULT_LIST_ENTRIES_COLOR+";";

                /*if (getFieldName(colNum).equals("email")) {
                        return "font-weight:bold; color:"+Utility.DEFAULT_LIST_ENTRIES_COLOR+";";
                } else {
                    return super.getCellCSSText(record, rowNum, colNum);
                }*/
            }
        };

        list.setSelectionType(SelectionStyle.SINGLE);

        list.setBodyBackgroundColor(Utility.LISTGRID_BACKGROUND);

        list.addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                //Enable the button iff something has been selected
                drawButton.setDisabled(!  event.getState() );
                selected = event.getRecord().getAttributeAsFloat("row").intValue();
            }
        });

        list.addDoubleClickHandler(new DoubleClickHandler() {

            public void onDoubleClick(DoubleClickEvent event) {
                new GraphWindow(graphs[selected]);
            }
        });

        this.addChild( list );
        list.setTop(drawButton.getBottom() + Utility.MARGIN);
        list.setWidth(panelSize);
        list.setHeight(panelSize - list.getTop() );
        list.setShowAllRecords(true);
        list.setBackgroundColor("gray");

        UtilityFunctions.setWidgetPrompt(list, "Best results list", "In order to view the desired embedding select it and click on the 'Draw' button above the list or just double click the related list element. ");
                                 
        ListGridField graphType = new ListGridField("graphType", "Type", 170);
        graphType.setAlign(Alignment.CENTER);

        //graphType.setst
        ListGridField vNbr = new ListGridField("vNbr", "Vertices Nbr", 150);
        vNbr.setAlign(Alignment.CENTER);

        ListGridField crossingNbr = new ListGridField("crossingNbr", "Crossing Number", 200);
        crossingNbr.setAlign(Alignment.CENTER);
        ListGridField valid = new ListGridField("valid", "Valid", 100);
        valid.setAlign(Alignment.CENTER);
        ListGridField email = new ListGridField("email", "Founder's Email");
        email.setAlign(Alignment.CENTER);
        //ListGridField gridPositions = new ListGridField("gridPositions", "V positions");

        list.setFields(graphType, vNbr, crossingNbr , valid, email);
        list.setCanResizeFields(false);

    }

    public void retrieveList(int vNbr, short graphType ){
        if ( graphType!= GraphType.COMPLETE_GRAPH && graphType!= GraphType.COMPLETE_BIPARTITE_GRAPH && graphType!= GraphType.ALL ){
            SC.say("Only Complete Graphs and Complete Bipartite Graphs' results are tracked.\n" +
                    "Please select one of this two options from the menu on the left.");
            return;
        }
        if (vNbr != lastVNbr || graphType != lastGraphType ){
            lastVNbr = vNbr;
            lastGraphType = graphType;
            GraphRetriever.get().retrieve( vNbr,  graphType, this);
        }
    }

    public void populateList(ListGridRecord[] records, GraphEmbedding[] gs ){
        graphs = gs;
        //alert("populating");
        list.setData(records);
        //alert(records.toString());
        list.redraw();
    }

    public static String getIsValidLabel(boolean valid){
        if(valid){
            return "Yes";
        }else{
            return "No";
        }

    }
    /*
    private native void alert(String message)/*-{
		alert(message);
    }-* /;
     *
     */
}
