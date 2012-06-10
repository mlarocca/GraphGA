/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.ArrayList;
import org.GCGA.client.utility.IntegerCallback;
import org.GCGA.client.utility.Utility;


public class ListSelectionDialog extends Window{
    
    private ListSelectionDialog thisWindow = this;

    private ListGrid list;

    private int selected = Utility.INVALID_INDEX;

    private IntegerCallback onSubmit;

    private final Button okButton = new Button("OK");
    private final Button cancelButton = new Button("Cancel");

    private static final short WINDOW_HEIGHT = 350;

    public ListSelectionDialog(String title, String message, final ListGridField[] fields, final ArrayList<ListGridRecord> records, final IntegerCallback callback) {

        super();
        onSubmit = callback;

        this.setTitle(title);
        this.setShowMinimizeButton(false);
//        this.setModalMaskOpacity(70);
//        this.setOpacity(75);
        this.setCanDragResize(false);
        this.setCanDragReposition(true);
        this.setIsModal(true);
        this.setShowModalMask(false);

        this.addCloseClickHandler(new CloseClickHandler() {
            public void onCloseClick(CloseClientEvent event) {
                //buttonTouchThis.setTitle("Touch This");
//                thisWindow.removeItem(gcp);
                
                cancel();
            }
        });

        int width = Utility.W_MARGIN;

        for (ListGridField f : fields){
            try{
                width += new Integer( f.getWidth() );
            }catch(Exception e){

            }
        }


        Canvas wrapper = new Canvas();

        VLayout wrapperLayout = new VLayout();
        wrapperLayout.setMembersMargin(Utility.MARGIN);

        Label messageLabel = new Label(message);

        //messageLabel.setTop(0);
        //messageLabel.setLeft(Utility.MARGIN);
        messageLabel.setWidth(width);
        messageLabel.setHeight(Utility.LABEL_HEIGHT);

        wrapperLayout.addMember(messageLabel);

        list = new ListGrid() {

            @Override
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                return "font-weight:bold; color:"+Utility.DEFAULT_LIST_ENTRIES_COLOR+";";
            }
        };

        list.setFields(fields);
        

        list.setSelectionType(SelectionStyle.SINGLE);

        list.setBodyBackgroundColor(Utility.LISTGRID_BACKGROUND);

        list.addSelectionChangedHandler(new SelectionChangedHandler() {

            public void onSelectionChanged(SelectionEvent event) {
                //Enable the button iff something has been selected
                //drawButton.setDisabled(!  event.getState() );
                selected = event.getRecord().getAttributeAsFloat("row").intValue();
            }
        });

        //list.setTop(messageLabel.getBottom() + Utility.MARGIN);
        //list.setLeft(Utility.MARGIN);
        list.setWidth(width);
        list.setHeight(WINDOW_HEIGHT - messageLabel.getHeight() - okButton.getHeight() - 2*Utility.MARGIN - Utility.H_MARGIN );
        list.setShowAllRecords(true);
        list.setCanResizeFields(false);
        list.setBackgroundColor("gray");
        
        ListGridRecord[] recordsArray = new ListGridRecord[records.size()];
        for (int i=0; i<records.size(); i++)
            recordsArray[i] = records.get(i);

        list.setData(recordsArray);

        wrapperLayout.addMember(list);

        //okButton.setTop(list.getBottom() + Utility.MARGIN);
        //cancelButton.setTop(okButton.getTop());

        Canvas buttonWrapper = new Canvas();

        okButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (list.anySelected())
                    submitSelection();
                else
                    return;
            }
        });
        
        cancelButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                cancel();
            }
        });
        okButton.setLeft((width - okButton.getWidth() - cancelButton.getWidth() - Utility.MARGIN) / 2);
        cancelButton.setLeft(okButton.getRight()+Utility.MARGIN);

        buttonWrapper.addChild(okButton);
        buttonWrapper.addChild(cancelButton);

        wrapperLayout.addMember(buttonWrapper);
        wrapperLayout.setTop(0);
        wrapperLayout.setLeft(Utility.MARGIN);
       
        wrapper.addChild(wrapperLayout);

        this.addItem(wrapper);

        this.setWidth(width + Utility.W_MARGIN );
        this.setHeight(WINDOW_HEIGHT);

        this.show();
        this.centerInPage();
    }

    private void submitSelection(){
        onSubmit.execute(selected);
        thisWindow.destroy();
        thisWindow = null;
    }

    private void cancel(){
        thisWindow.destroy();
        thisWindow = null;
    }



}
