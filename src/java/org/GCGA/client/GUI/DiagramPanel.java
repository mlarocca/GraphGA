/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.MouseMoveEvent;
import com.smartgwt.client.widgets.events.MouseMoveHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import java.util.ArrayList;
import org.GCGA.client.utility.DataRecordSet;
import org.GCGA.client.utility.DataRecord;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Marcello
 */
public class DiagramPanel extends Canvas{
    private int width ;
    private int height ;
    byte colorCounter;

    private double DX, DY;
    private double MAX, MIN;

    private double Y_SCALE_FACTOR = 1;

    private static final String[] COLORS = {"#FF0000", "#00FF00", "#0000FF", "#00FFFF", "black", "#FF00FF", "orange", "indigo", "brown", "navy", "green", "#FFFF00", "black"};
    private static final byte AXES_MARGIN = 25;
    private static final byte AXES_DELTA = 50;
    private static final byte TAG_LENGTH = 10;

    private static final byte X_LABEL_AXES_DELTA_SLOTS = 2;//Number of slots occupied by xAxisLabel

    private static final short LEGENDA_PANEL_WITH = 250;
    private static final byte LEGENDA_PANEL_TOP = 50;

    private static final byte MAX_LABEL_CHARS = 5;

    private ExtendedCanvas diagram;

    private Label originLabel, maxXLabel, xAxesLabel;
    private final ArrayList<Label> xAxesLabels = new ArrayList<Label>();
    private final ArrayList<Label> yAxesLabels = new ArrayList<Label>();

    private final ArrayList<TagLabel> markers = new ArrayList<TagLabel>();

    private LegendaPanel legendaPanel;

    private final DiagramPanel thisPanel = this;

    public DiagramPanel(int w, int h) {
        super();
        width = w;
        height = h;
        setBackgroundColor(org.GCGA.client.utility.Utility.LIGHT_BACKGROUND);
        
        diagram = new ExtendedCanvas(width, height);

        this.addChild(diagram);
        colorCounter = 0;

        this.addMouseMoveHandler(new MouseMoveHandler() {

            public void onMouseMove(MouseMoveEvent event) {
                //UtilityFunctions.alert(event.getX()-diagram.getAbsoluteLeft() + " " + event.getY());
                thisPanel.setPrompt("x: "+ makeLabelText(XToValue(event.getX()-diagram.getAbsoluteLeft())) + " y: " + makeLabelText( YToValue(event.getY()-diagram.getAbsoluteTop()) ) );
            }
        });

        this.addMouseOutHandler(new MouseOutHandler() {

            public void onMouseOut(MouseOutEvent event) {
                thisPanel.setPrompt("");
            }
        });

        this.addDoubleClickHandler(new DoubleClickHandler() {

            public void onDoubleClick(DoubleClickEvent event) {
                thisPanel.addMarkerLabel(event.getX() - diagram.getAbsoluteLeft(), event.getY() - diagram.getAbsoluteTop());

            }
        });

    }

    private void addMarkerLabel(int x, int y){
        TagLabel new_l = new TagLabel(x, y);
        new_l.setVisible(false);

        markers.add(new_l);
        thisPanel.addChild(new_l);
        new_l.setAnimateShowTime(1000);
        new_l.animateShow(AnimationEffect.FADE);
    }

    private String makeLabelText(double val){
        String tmpS = ""+val;

        int tmpL = tmpS.length();
        if ( tmpL <= MAX_LABEL_CHARS ){
            return tmpS;
        }else{
            int index = tmpS.indexOf('.');
//            GeneticAlgorithm.alert (val + " \n "+  index + " | " + MAX_LABEL_CHARS);
            if (index<0 || index+1 < MAX_LABEL_CHARS )
                return tmpS.substring(0, MAX_LABEL_CHARS);
            else
                return tmpS.substring(0, index);
        }
    }

    /**
     *
     * @param val
     * @return
     */
    private double valueToY(double val){
        return (height-AXES_MARGIN-(val-MIN) * DY);
    }

    private double YToValue(double y){
        return (height-AXES_MARGIN-y)/DY + MIN;
    }

    private double valueToX(double v){
        return AXES_MARGIN+(v)*DX;
    }

    private double XToValue(double x){
        return (x-AXES_MARGIN)/DX;
    }

    public void drawAxes(int num_values, double min, double max, double median, double firstQuartile, double thirdQuartile ){

        if ( median != 0 )
            MIN =  min < median/10 ? firstQuartile : min ;
        else
            MIN = min;
        if (median!=0)
            MAX = (max > 10 * median ? thirdQuartile : max);
        else
            MAX = max;

        double tmp = (MAX-MIN)*(1/Y_SCALE_FACTOR-1);
        //A determinare la ripartizione dello zoom contribuisce lo spostamento dei dati rispetto alla mediana, ma per non più del 50%
        MAX += tmp * ( MAX-MIN > 0 ? 0.25 + ( 1 -( (MAX-median)/(MAX-MIN) ) )/2 :  0.5  );
        MIN -= tmp * ( MAX-MIN > 0 ? 0.25 + ( 1 -( (median-MIN)/(MAX-MIN) ) )/2  :  0.5  ) ;

//        GeneticAlgorithm.alert(MAX + " > " + MIN);
        DX = ((double)(width - AXES_MARGIN))/(num_values-1);
        DY = (height - AXES_MARGIN)/( (MAX-MIN) > 0 ? MAX-MIN : 1);

        diagram.setStrokeStyle("black");
        diagram.beginPath();
            diagram.moveTo(AXES_MARGIN, height - AXES_MARGIN);
            diagram.lineTo(AXES_MARGIN, 0);
            diagram.moveTo(AXES_MARGIN, height - AXES_MARGIN);
            diagram.lineTo(width, height - AXES_MARGIN);
        diagram.stroke();

        diagram.setStrokeStyle("rgba(125,125,125,0.15)");
//        diagram.beginPath();
        for (int i=1; AXES_MARGIN+i*AXES_DELTA<width; i++){
            diagram.drawVerticalDashedLine(AXES_MARGIN + i*AXES_DELTA, 0, height - AXES_MARGIN);
//            diagram.moveTo(AXES_MARGIN + i*AXES_DELTA , height - AXES_MARGIN);
//            diagram.lineTo(AXES_MARGIN + i*AXES_DELTA, 0);
        }        
        for (int i=1; AXES_MARGIN+i*AXES_DELTA<height; i++){
            diagram.drawHorizontalDashedLine(AXES_MARGIN, width, height - AXES_MARGIN - i*AXES_DELTA);
//            diagram.moveTo(AXES_MARGIN, height - AXES_MARGIN - i*AXES_DELTA);
//            diagram.lineTo(width, height - AXES_MARGIN - i*AXES_DELTA);
        }
        //DRAW X-Axes
        diagram.setStrokeStyle("rgba(0,0,0,0.25)");
        drawLine(AXES_MARGIN, valueToY(0), width, valueToY(0) );
//        diagram.stroke();

//        GeneticAlgorithm.alert(num_values + " | " + max + " > " + min + " || " + MAX + " > " + MIN);

        if (num_values >0 ){

            Label tmpLabel;
            int n = (int)( (width - AXES_MARGIN) / AXES_DELTA );
//            GeneticAlgorithm.alert(n + " " + num_values + " = " + UtilityFunctions.MCD(n, num_values)  );
            int step = n / UtilityFunctions.MCD(n, num_values);
            double num_step = (double)num_values / UtilityFunctions.MCD(n, num_values);
//            GeneticAlgorithm.alert(step + " , " + num_step  );

            int i_xAxesLabel = n/2 - X_LABEL_AXES_DELTA_SLOTS/2 ;
            if ( step<i_xAxesLabel )
                i_xAxesLabel -= (i_xAxesLabel - (i_xAxesLabel/step)*step) - 1; //Allinea allo slot successivo un'etichetta;

            for (int i=step; i<n; i+=step ){
                //Leave space for the axes' label
                if ( i > i_xAxesLabel && i < i_xAxesLabel+ X_LABEL_AXES_DELTA_SLOTS )
                    continue;

                double tmp_x = valueToX( Math.round(num_step*i/step) );

                //tmpLabel = new Label("<center style='color:black;'>"+makeLabelText(XToValue(AXES_MARGIN + i*AXES_DELTA))+"</center>");
                tmpLabel = new Label("<center style='color:black;'>"+makeLabelText(Math.round(num_step*i/step))+"</center>");
                tmpLabel.setWidth(AXES_DELTA);
                tmpLabel.setHeight(AXES_MARGIN);

                tmpLabel.setLeft((int)tmp_x - AXES_DELTA/2 );
                //tmpLabel.setLeft(AXES_MARGIN + i*AXES_DELTA - AXES_DELTA/2 );
                tmpLabel.setTop(height-AXES_MARGIN);
                this.addChild(tmpLabel);
                xAxesLabels.add(tmpLabel);

                //Little line to point out the point
                diagram.setStrokeStyle("red");
                diagram.beginPath();
                    diagram.moveTo(tmp_x, height - AXES_MARGIN);
                    diagram.lineTo(tmp_x, height - AXES_MARGIN - TAG_LENGTH);
                diagram.stroke();


            }

            xAxesLabel = new Label("<u style='color:red;'>Iteration</u>");
//            xAxesLabel.setBorder("1px dashed red");
            xAxesLabel.setWidth(X_LABEL_AXES_DELTA_SLOTS*AXES_DELTA);
            xAxesLabel.setHeight(AXES_MARGIN);
            this.addChild(xAxesLabel);
            xAxesLabel.setLeft(AXES_MARGIN + i_xAxesLabel *AXES_DELTA );
            xAxesLabel.setTop(height-AXES_MARGIN);
            xAxesLabel.setAlign(Alignment.CENTER);

            xAxesLabels.add(xAxesLabel);

            originLabel = new Label("<b style='color:red;'>0</b>");
            originLabel.setWidth(AXES_MARGIN);
            originLabel.setHeight(AXES_MARGIN);
            this.addChild(originLabel);
            originLabel.setLeft(AXES_MARGIN);
            originLabel.setTop(height-AXES_MARGIN);

            maxXLabel = new Label("<b style='color:red;'>"+(num_values-1)+"</b>");
            maxXLabel.setWidth(AXES_MARGIN);
            maxXLabel.setHeight(AXES_MARGIN);
            this.addChild(maxXLabel);
            maxXLabel.setLeft(width-maxXLabel.getWidth());
            maxXLabel.setTop(height-AXES_MARGIN);

        //MIN LABEL

            tmpLabel = new Label("<b style='color:red;'>"+makeLabelText(MIN)+"</b>");
            tmpLabel.setWidth(AXES_MARGIN);
            tmpLabel.setHeight(AXES_MARGIN);

            tmpLabel.setLeft(0);
            tmpLabel.setTop(height - AXES_MARGIN - tmpLabel.getHeight()/2);
            this.addChild(tmpLabel);
            yAxesLabels.add(tmpLabel);

            if (MAX>MIN){
                //IF a straight horizontal line has to be drawn, no need for extra labels;

                n = (int)( (height - AXES_MARGIN) / AXES_DELTA );
                for (int i=1; i<n; i++ ){

                    tmpLabel = new Label("<p style='color:black;'>"+makeLabelText((MIN + i*(MAX-MIN)/n))+"</p>");
                    tmpLabel.setWidth(AXES_MARGIN);
                    tmpLabel.setHeight(AXES_MARGIN);

                    tmpLabel.setLeft(0);
                    tmpLabel.setTop(height-AXES_MARGIN - i *AXES_DELTA - tmpLabel.getHeight()/2);
                    this.addChild(tmpLabel);
                    yAxesLabels.add(tmpLabel);

                }

            //MAX LABEL
                tmpLabel = new Label("<b style='color:red;'>"+ makeLabelText(MAX) +"</b>");
                tmpLabel.setWidth(AXES_MARGIN);
                tmpLabel.setHeight(AXES_MARGIN);
                this.addChild(tmpLabel);
                tmpLabel.setLeft(0);
                tmpLabel.setTop(0);
                yAxesLabels.add(tmpLabel);

            }

        }
    }

    private String getColor(byte color){
       color = color < COLORS.length ? color : (byte)(COLORS.length-1);
       return COLORS[color];
    }

    public void accountForHiddenCurve(){
        colorCounter++;
    }

    public void drawCurve(ArrayList<Double> data ){

        int i;
        double oldY;

        oldY = valueToY(data.get(0));// height - AXES_MARGIN - (data.get(0)-MIN) * DY;

//String res="" + oldY;
        diagram.setStrokeStyle(getColor(colorCounter));
        

        for( i=1; i<data.size(); i++ ){
            try{
                drawLine(valueToX(i-1), oldY, valueToX(i), (oldY = valueToY(data.get(i)) )  );//(height-AXES_MARGIN-(data.get(i)-MIN) * DY)) );
            }catch(Exception e){
                //continue;
            }
        }
 // GeneticAlgorithm.alert(res);
        
        
        //Increment the index, if it is possible (all the next curves will be drawn in black)
        colorCounter++;
    }

    private void drawLine(double x1, double y1, double x2, double y2){
        y1 = y1 < -height ? -height : y1 > 2*height ? 2*height : y1;
        y2 = y2 < -height ? -height : y2 > 2*height ? 2*height : y2;
        diagram.beginPath();
            diagram.moveTo(x1, y1);
            diagram.lineTo(x2, y2);
        diagram.stroke();
    }

    public void addLegenda( DataRecordSet data ){
        if (legendaPanel!=null)
            removeChild(legendaPanel);
        legendaPanel = new LegendaPanel(LEGENDA_PANEL_WITH);
        
        for (final DataRecord dr: data){
            final LegendaPanel.LegendaEntry tmpLE = legendaPanel.addEntry(dr.getDataField(), getColor((byte)legendaPanel.getNextColorIndex()), dr.isVisible());
            tmpLE.addClickHandler(new AnimationCallback() {

                public void execute(boolean earlyFinish) {
                    tmpLE.changeStatus(dr);
                }
            });

        }

        legendaPanel.setLeft(width - LEGENDA_PANEL_WITH - org.GCGA.client.utility.Utility.MARGIN );
        legendaPanel.setTop(LEGENDA_PANEL_TOP);
        addChild(legendaPanel);
    }

    public void addToLegenda( DataRecordSet data ){
        //byte i=legendaPanel.getC;
        legendaPanel.addSeparator();
        for (final DataRecord dr: data){
            final LegendaPanel.LegendaEntry tmpLE = legendaPanel.addEntry(dr.getDataField(), getColor((byte)legendaPanel.getNextColorIndex()), dr.isVisible());
            tmpLE.addClickHandler(new AnimationCallback() {

                public void execute(boolean earlyFinish) {
                    tmpLE.changeStatus(dr);
                }
            });
        }

    }
/*
    public void zoomOut(){
        Y_SCALE_FACTOR *=0.75;
    }
 */
    /**
     *
     * @param zoom - The zoom to apply (percent)
     */
    public void setZoom(int zoom){
        //GeneticAlgorithm.alert(zoom+ "");
        Y_SCALE_FACTOR = zoom /100.0;
        //GeneticAlgorithm.alert(Y_SCALE_FACTOR+ "");
    }

    public void reset(boolean resetScaleFactor, boolean clearMarkers){
        if (resetScaleFactor)
            Y_SCALE_FACTOR = 1;
        if (clearMarkers)
            clearMarkers();
        clear();
    }

    private void clearMarkers(){
        for (Label l : markers)
            try{
                removeChild(l);
            }catch(Exception e){
                continue;
            }
        markers.clear();
    }

    public void adjustMarkersPosition(){
        for (TagLabel l : markers){
            int new_y = (int)Math.round(valueToY(l.getValue() ));
            if ( new_y < 0 || new_y > diagram.getHeight() - l.getHeight() )
                try{
                    removeChild(l);
                }catch(Exception e){
                    continue;
                }
            else{
                l.setTop(new_y);
            }
        }
    }

    @Override
    public void clear(){
        //super.clear()
        diagram.clear();
        colorCounter = 0;

        for (Label l : yAxesLabels)
            try{
                removeChild(l);
            }catch(Exception e){
                continue;
            }

        yAxesLabels.clear();

        for (Label l : xAxesLabels)
            try{
                removeChild(l);
            }catch(Exception e){
                continue;
            }

        xAxesLabels.clear();

        try{
            removeChild(originLabel);
            removeChild(maxXLabel);
//            removeChild(xAxesLabel);
        }catch(Exception e){
            
        }

    }

    private class TagLabel extends Label{

        private final double value;

        public TagLabel(int x, int y) {
            super("<b style='color:black;'>x: "+ makeLabelText(XToValue(x)) + " y: " + makeLabelText( YToValue(y) ) + "</b>");
            value = YToValue(y);
            setBackgroundColor("pink");
            setBorder("1px dashed black;");
            setLeft( x );
            setTop( y );
            setWrap(false);
            setHeight(Utility.LABEL_HEIGHT);
            setAutoWidth();
        }

        public double getValue(){
            return value;
        }


    }
}
