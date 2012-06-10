/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import java.util.ArrayList;
import org.GCGA.client.LoginHandler;
import org.GCGA.client.utility.DataRecordSet;
import org.GCGA.client.utility.DataRecord;
import org.GCGA.client.utility.RecordFolder;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Eurialo
 */
public class AnalysisPanel extends Canvas {

    private static final short WIDTH = Utility.DEFAULT_PANELS_WIDTH;
    private static final short HEIGHT = Utility.DEFAULT_PANELS_WIDTH;
    private static final byte TOP_PANEL_HEIGHT = 25;

    private static final short ZOOM_COMBO_WIDTH = 80;
    private static final short ZOOM_FORM_WIDTH = 130;

    private static final short[] ZOOM_VALUES = {100, 75, 50, 25, 10, 5};

    private static AnalysisPanel thisPanel = null;
    private static DiagramPanel diagramCanvas = null;

    private static Canvas activePanel;

    private static Canvas hiddenPanel;

    private static ComboBoxItem zoomComboBox = new ComboBoxItem();

    private static RecordFolder data = new RecordFolder();
    private static DataRecordSet drs = null;

    private static byte selectedFolder = Utility.INVALID_INDEX;
    private static final ArrayList<DataRecordSet> selection = new ArrayList<DataRecordSet>();

    private static TabLabelSlider panelSlider;

    private AnalysisPanel() {
        super();

        this.setWidth(WIDTH );
        this.setHeight(HEIGHT );

        activePanel = new Canvas();

        activePanel.setWidth(this.getWidth());
        activePanel.setHeight(this.getHeight());
        activePanel.setTop(0);
        activePanel.setLeft(0);

        com.smartgwt.client.widgets.Canvas menuPanel = new com.smartgwt.client.widgets.Canvas();

        menuPanel.setTop(0);
        menuPanel.setLeft(0);
        menuPanel.setWidth(WIDTH);
        menuPanel.setHeight(TOP_PANEL_HEIGHT);

        zoomComboBox.setTitle("ZOOM");

        zoomComboBox.setType("comboBox");

        zoomComboBox.setValueMap(ZOOM_VALUES[0]+"%", ZOOM_VALUES[1]+"%", ZOOM_VALUES[2]+"%", ZOOM_VALUES[3]+"%", ZOOM_VALUES[4]+"%", ZOOM_VALUES[5]+"%");

        zoomComboBox.addChangedHandler(new ChangedHandler() {

            public void onChanged(com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
                //GeneticAlgorithm.alert(event.getValue()+" "+ zoomComboBox.getSelectedRecord().geti);
                String zoom = event.getValue().toString();
                diagramCanvas.setZoom(new Integer(zoom.substring(0, zoom.length()-1) ) );
                update(false);
            }
        });

        zoomComboBox.setWidth(ZOOM_COMBO_WIDTH);

        DynamicForm zoomForm = new DynamicForm();
        zoomForm.setFields(zoomComboBox);
        menuPanel.addChild(zoomForm);
/*
        Button tmp = new Button("+");
        tmp.setWidth(30);
        tmp.setLeft(0);
        
        tmp.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                AnalysisPanel.get().addFolder("Prova " + Math.random());
            }
        });
        
        menuPanel.addChild(tmp);
*/
        //int zPRight = zoomForm.getRight() + Utility.MARGIN;
//                zoomComboBox.getLeft() + zoomComboBox.getWidth() + Utility.MARGIN;
        panelSlider = new TabLabelSlider(WIDTH - ZOOM_FORM_WIDTH - Utility.MARGIN, TOP_PANEL_HEIGHT);

        panelSlider.setLeft(ZOOM_FORM_WIDTH);

        UtilityFunctions.setWidgetPrompt(panelSlider, "Select Diagram", "<i style='color:red;'>Click</i>: Select Category<br/>" +
                                                                        "<i style='color:red;'>CTRL + Click</i>: Add/Remove Category to current diagram");

        menuPanel.addChild(panelSlider);

        menuPanel.setTop(0);        
        activePanel.addChild(menuPanel);

        diagramCanvas = new DiagramPanel(WIDTH, HEIGHT-TOP_PANEL_HEIGHT);
        diagramCanvas.setTop(TOP_PANEL_HEIGHT);
        activePanel.addChild(diagramCanvas);


        hiddenPanel = new Canvas();

        hiddenPanel.setWidth(this.getWidth());
        hiddenPanel.setHeight(this.getHeight());
        hiddenPanel.setTop(0);
        hiddenPanel.setLeft(0);

        Label instructionsLabel = new Label("<h2><center><b style='color:red'>Feature available for registered users only</b></center><br/><br/>Register in order to use analysis panel.<br/>This feature keeps track of the behaviour of the algorithm and presents it in a easily understandable format.<br/>Registration is free of charge.</h2>");

        instructionsLabel.setWidth(hiddenPanel.getWidth());
        instructionsLabel.setTop(0);
        instructionsLabel.setLeft(0);

        hiddenPanel.addChild(instructionsLabel);

        this.addChild(activePanel);
        this.addChild(hiddenPanel);
        updatePanelStatus();
    }

    public void updatePanelStatus(){
        if (LoginHandler.get().isLogged()){
            hiddenPanel.setVisible(false);
            activePanel.setVisible(true);
        }
        else{
            activePanel.setVisible(false);
            hiddenPanel.setVisible(true);
        }
    }

    public static AnalysisPanel get(){

        if (thisPanel == null){
            thisPanel = new AnalysisPanel();
        }
        return thisPanel;
    }

    public void addValue(String folderName, String fieldName, double v){
        data.add(folderName, fieldName, v);
    }

    public void clearValues(){
        data.clear();
        diagramCanvas.reset(true, true);
        zoomComboBox.setValue(ZOOM_VALUES[0]+"%");

        panelSlider.clearTab();
        selection.clear();
    }

    private void paintAxes(){
        Double d;
        double min = Double.MAX_VALUE, max=Double.MIN_VALUE, median=0, fstQ=0, trdQ =0;//avg=0;
        int maxElNbr = 0;
        int count = 0;
        for ( DataRecord dr : drs  ){
            if (dr.isVisible()){
                count++;
                if ( (d = dr.getMax()) > max )
                    max = d;

                if ( (d = dr.getMin()) < min )
                    min = d;
                if ( dr.getData().size() > maxElNbr )
                    maxElNbr = dr.getData().size();
                //avg += dr.getAvg();
                median += dr.getMedian();
                fstQ += dr.getFirstQuartile();
                trdQ += dr.getThirdQuartile();
            }
        }
//        GeneticAlgorithm.alert(max + "  > " + min);
        if (count>0){
            median/=count;
            fstQ/=count;
            trdQ/=count;
        }

        diagramCanvas.drawAxes(maxElNbr, min, max, median, fstQ, trdQ);
    }
/*
    public void setFolderToShow(String folderName){
        if ( data.contains(folderName)!=null )
            lastFolderName = folderName;
    }
*/
    /*
    private void paintAxes(String fieldName) throws IllegalArgumentException{
        lastFieldName = fieldName;
        DataRecord dr = drs.contains(fieldName);
        if ( dr == null || !dr.isVisible() ){
            paintAxes();
        }else{
//            GeneticAlgorithm.alert(dr.getData().size() + " , " + dr.getMin() + " , " + dr.getMax() + " , " + dr.getAvg() );
            diagramCanvas.drawAxes(dr.getData().size(), dr.getMin(), dr.getMax(), dr.getMedian(), dr.getFirstQuartile(), dr.getThirdQuartile() );
        }
    }
*/
    private void paint(boolean notUpdating){
        if (notUpdating)
            diagramCanvas.addLegenda(drs);
        drawCurveFromDataRecordSet(drs);
        for (DataRecordSet sdrs: selection){
            if (notUpdating)
                diagramCanvas.addToLegenda(sdrs);
            drawCurveFromDataRecordSet(sdrs);
        }
    }

    private void drawCurveFromDataRecordSet(DataRecordSet dataRS){
        for ( DataRecord dr : dataRS ){
            if (dr.isVisible())
                diagramCanvas.drawCurve(dr.getData());
            else
                diagramCanvas.accountForHiddenCurve();
        }
        diagramCanvas.adjustMarkersPosition();
    }

    public void repaint(boolean resetMarkersLabels){
        diagramCanvas.reset(false, resetMarkersLabels);
        paintAxes();
        paint(true);
    }

    private void update(boolean resetMarkersLabels){
        diagramCanvas.reset(false, resetMarkersLabels);
        paintAxes();
        paint(false);
    }

    protected static void hideDataSet(DataRecord dr){
        dr.hideDataRecord();
        get().update(false);
    }

    protected static void showDataSet(DataRecord dr){
        dr.showDataRecord();
        get().update(false);
    }

    public void addFolder(final String folderName, final String folderTitle){
        final byte i =  data.addFolder(folderName);
       //(byte)data.size();
        if ( i!= Utility.INVALID_INDEX ){
            panelSlider.addTab(folderTitle,
                    new AnimationCallback() {

                        public void execute(boolean earlyFinish) {
                            changeFolder(i);
                        }
                    },
                    new AnimationCallback() {

                        public void execute(boolean earlyFinish) {
                            changeSelection(i);
                        }
                    }
            );
        }
    }

    public void selectInitialFolder(String folderName){
        
        selectedFolder = data.getDataRecordSetIndex(folderName);
        drs = data.get(selectedFolder);
        panelSlider.selectTab(selectedFolder);
        //selection.clear();
        
    }

    protected static void changeFolder(byte folderIndex){
        //GeneticAlgorithm.alert("" + folderIndex);
        if (folderIndex!=Utility.INVALID_INDEX && folderIndex!=selectedFolder ){
            selectedFolder = folderIndex;
            drs = data.get(selectedFolder);
            selection.clear();
            get().repaint(true);
        }
    }

    /**
     * Adds or Remove a folder to current selection;
     * @param folderIndex
     */
    protected static void changeSelection(byte folderIndex){
        DataRecordSet tmp = data.get(folderIndex);
        if (selection.contains(tmp)){
            selection.remove(tmp);
        }else{
            selection.add(tmp);
        }
        get().repaint(false);
    }

    public double getLastValue(String folderName, String fieldName) throws IllegalArgumentException{
        DataRecordSet tmpDRS = data.contains(folderName);
        if (tmpDRS==null)
            throw  new IllegalArgumentException();
        return tmpDRS.getLastValue(fieldName);
    }

    public boolean isReady(){
        return selectedFolder!=Utility.INVALID_INDEX;
    }

}
