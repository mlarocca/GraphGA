/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import org.GCGA.client.News;
import org.GCGA.client.ajax.NewsRetriever;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Marcello
 */
public class NewsPanel extends Window{

    private static NewsPanel thisPanel = null;

    private ListGrid list;

    private static final short TITLE_WIDTH = 242;
    private static final byte STICKY_WIDTH = 18;

    private News[] news = null;

    private NewsPanel(int panelWidth, int panelHeight) {
        super();

        this.setTitle("News");
        this.setShowMinimizeButton(false);
        this.setShowCloseButton(false);
        this.setCanDragReposition(false);
        this.setWidth(panelWidth);
        this.setHeight(panelHeight);

        Canvas wrapper = new Canvas();
        wrapper.setPadding(2);

        list = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                /*String style ;
                if (rowNum % 2 == 0){
                    style = "font-weight:bold; background:#AAAAAA;";
                }
                else{
                    style = "font-weight:bold; background:#CCCCCC;";
                }
                */
                if (getFieldName(colNum).equals("date")) {
                    return "font-weight:bold; color:"+Utility.NEWS_DATE_COLOR+";";
                } else{
                    return "font-weight:bold; color:"+Utility.DEFAULT_LIST_ENTRIES_COLOR+";";
                }
                //return style;
            }

        };

        list.setBodyBackgroundColor(Utility.LISTGRID_BACKGROUND);
        list.setShowHeader(false);
        list.addCellDoubleClickHandler( new CellDoubleClickHandler() {

            public void onCellDoubleClick(CellDoubleClickEvent event) {
                //alert(event.getRecord().getAttribute("row"));
                int r = event.getRecord().getAttributeAsInt("row");
                new NewsWindow(news[r]);
            }
        });

        UtilityFunctions.setWidgetPrompt(list, "News", "Double click to open news in a separate window");
        list.setSelectionType(SelectionStyle.SINGLE);

        wrapper.addChild( list );
        //list.setTop( MARGIN);
        list.setWidth(panelWidth - Utility.W_MARGIN_REDUCED);
        list.setHeight(panelHeight - Utility.H_MARGIN_REDUCED );
        list.setShowAllRecords(true);

        ListGridField title = new ListGridField("title", "Title", TITLE_WIDTH);
        ListGridField sticky = new ListGridField("sticky", "", STICKY_WIDTH);
            sticky.setAlign(Alignment.CENTER);
            sticky.setType(ListGridFieldType.IMAGE);
            //sticky.setImageURLPrefix(Utility.IMAGE_DIR);
            sticky.setImageURLSuffix(".png");
            sticky.setPrompt("Sticky post");
            sticky.setCanEdit(false);
        ListGridField date = new ListGridField("date", "Date");

        //ListGridField gridPositions = new ListGridField("gridPositions", "V positions");

        list.setFields(sticky, date, title);
        list.setCanResizeFields(false);

        this.addItem(wrapper);

    }

    public static void init(int panelWidth, int panelHeight){
        if (thisPanel==null){
            thisPanel = new NewsPanel(panelWidth, panelHeight);
            thisPanel.retrieveList();
        }

    }

    public static NewsPanel get(){
        if (thisPanel==null)
            NewsPanel.init(Utility.NEWS_WINDOW_SIZE, Utility.DEFAULT_TOP_PANEL_HEIGHT);
        return thisPanel;
    }

    public void retrieveList( ){
        if (news==null)
            NewsRetriever.get().retrieve( this );
        
    }

    public void populateList(ListGridRecord[] records, News[] ns ){
        news = ns;
        //alert("populating");
        list.setData(records);
        //alert(records.toString());
        list.redraw();
    }

}
