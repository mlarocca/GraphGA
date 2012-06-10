package org.GCGA.client.GUI;
//import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Label;
import org.GCGA.client.algorithm.GeneticAlgorithm;
//import com.google.gwt.user.client.ui.AbsolutePanel;
import com.smartgwt.client.widgets.Canvas;
//import com.smartgwt.client.widgets.Label;
import java.util.ArrayList;
import org.GCGA.client.GA_Element;
import org.GCGA.client.Grid;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Owner
 */
public class GraphPanel extends Canvas{

    private GA_Element selectedGAElement;
    private Grid grid = null;

//    private Canvas wrapper = new Canvas();

    private ExtendedCanvas drawingCanvas;

    private final ArrayList<VertexLabel> vertexlabels = new ArrayList<VertexLabel>();
//    private final ArrayList<Label> vertexlabels = new ArrayList<Label>();

    private int vertexSize;

    private static final short DEFAULT_WIDTH = Utility.DEFAULT_PANELS_WIDTH;
    private int size;

    public GraphPanel() {
        super();
        this.setSize(DEFAULT_WIDTH);
        setBackgroundColor(Utility.LIGHT_BACKGROUND);
        drawingCanvas = new ExtendedCanvas(this.getWidth(), this.getHeight());
        this.addChild(drawingCanvas);
//        this.setStylePrimaryName("graphPanel");
        //this.setLocation(0, 0);
//        wrapper.setBackgroundColor("white");
//        this.addChild(wrapper);
    }

    public GraphPanel(GeneticAlgorithm g) {
        super();
        this.setSize(DEFAULT_WIDTH);
        //this.setLocation(0, 0);
        grid = GeneticAlgorithm.getGrid();
        grid.setGrid(getSize());

        setBackgroundColor(Utility.LIGHT_BACKGROUND);
        drawingCanvas = new ExtendedCanvas(this.getWidth(), this.getHeight());
        this.addChild(drawingCanvas);
//        wrapper.setBackgroundColor("white");
//        this.addChild(wrapper);
    }

/*
    public GraphPanel(GeneticAlgorithm g, int pSize ) {
        super();

        this.setSize(pSize);
        //this.setLocation(0, 0);
        grid = GeneticAlgorithm.getGrid();
        grid.setGrid(getSize());
    }
*/
    /**
     *
     * @param g
     * @param vNbr - The number of vertex to be drawn
     */
    public void setGrid(GeneticAlgorithm g, int vNbr){
        clearPanel();
        this.setSize(DEFAULT_WIDTH);
        //this.setLocation(0, 0);
        grid = GeneticAlgorithm.getGrid();
        grid.setGrid(getSize());
        setVertices(vNbr);
    }

    /**
     *
     * @param gr
     * @param vNbr - The number of vertex to be drawn
     * @param size - Panel widht & height
     */
    public void setGrid(Grid gr, int vNbr, int size){
        clearPanel();
        this.setSize(size);
        //this.setLocation(0, 0);
        grid = gr;
        grid.setGrid(getSize());
        setVertices(vNbr);
    }
/*
    public void setGrid(Grid gr){
        grid = gr;
        grid.setGrid(getSize());
    }
*/
    public void setGraph(GA_Element s){
        selectedGAElement = s;
    }

    private void setVertices(int vNbr){
        int i;
        vertexSize = grid.getVertexSize();

        //Adds missing vertices
        for (i=vertexlabels.size(); i<vNbr; i++){
//            GeneticAlgorithm.alert("Adding" + i);
            vertexlabels.add(createVertex(i));
        }
        //Remove exceding vertices
        for (i=vertexlabels.size()-1; i>=vNbr; i--){
//            GeneticAlgorithm.alert("Removing "+i);
            this.removeChild(vertexlabels.get(i));
            vertexlabels.remove(i);
        }

    }

    public void paint() {
        if (grid!=null){
            grid.drawGrid(this);
        }

//        GeneticAlgorithm.alert("Paint ");
        if(selectedGAElement!=null)
            selectedGAElement.drawGraph(this);
    }

    public void setSize(int s){
        size = s;
        super.setSize((s+Utility.MARGIN)+"px",(s+Utility.MARGIN)+"px");
    }

    public int getSize(){
        return size;
    }

    public void setLabelPosition(Canvas l, int x, int y){
//        this.sete
        l.setLeft(x+"px");
        l.setTop(y+"px");
//        super.setWidgetPositionImpl(l, x,y );
    }

    public void clearPanel(){
        clearGrid();
        /*DELETE ROUTINE*/
        /*
        if (vertexlabels!=null)
            for (int i=0; i< vertexlabels.length; i++){
                this.removeChild(vertexlabels.get(i));
                vertexlabels[i]=null;
            }
         */
    }

    public void clearGrid(){
        drawingCanvas.clear();
    }

    public void drawLine(int x1, int y1, int x2, int y2, String style){
        
            drawingCanvas.setStrokeStyle(style);
            drawingCanvas.beginPath();
                drawingCanvas.moveTo(x1, y1);
                drawingCanvas.lineTo(x2, y2);
            drawingCanvas.stroke();
    }

    public void drawDashedLine(int x1, int y1, int x2, int y2, String style){

        if (y1==y2){
            drawingCanvas.setStrokeStyle(style);
            drawingCanvas.drawHorizontalDashedLine(x1, x2, y1);
        }else if (x1==x2){
            drawingCanvas.setStrokeStyle(style);
            drawingCanvas.drawVerticalDashedLine(x1, y1, y2);
        }else
            drawLine(x1, y1, x2, y2, style);
    }


    private VertexLabel createVertex(int vertexIndex){
//        Canvas wrapper = new Canvas();
        VertexLabel vertex = new VertexLabel(vertexIndex, vertexSize);

//        vertex.setBorder("1px solid black");
        this.addChild(vertex);

        return vertex;
//        wrapper.getElement().appendChild(vertex.getElement());

//this.getElement().appendChild(vertex.getElement());

//RootPanel.e
/*
        this.addChild(wrapper);
        
        return wrapper;
 */
    }

    public void drawVertex(int vertexIndex, int x, int y){
//        GeneticAlgorithm.alert(vertexlabels.get(vertexIndex).getTitle());
        vertexlabels.get(vertexIndex).setSize(vertexSize);
        setLabelPosition(vertexlabels.get(vertexIndex), x, y );
//        vertexlabels.get(vertexIndex).bringToFront();
    }

    public void drawEdge(int vIndex, int wIndex){
        VertexLabel v=vertexlabels.get(vIndex), w=vertexlabels.get(wIndex);
        drawLine(v.getLeft()+v.getWidth()/2, v.getTop()+v.getHeight()/2, w.getLeft()+w.getWidth()/2, w.getTop()+w.getHeight()/2, "black");
    }

    private class VertexLabel extends Canvas{

        private final Label label;

        public VertexLabel(int n, int s) {
            this(n+"", s);
        }

        public VertexLabel(String title, int vSize) {
            label = new Label(title);
            label.setStylePrimaryName("labelNode");
            this.addChild(label);
            setSize(vSize);
        }

        public void setSize(int size){
            setHeight(size);
            setWidth(size);
        }

        @Override
        public void setHeight(int height){
            label.setHeight(height+"px");
            super.setHeight(height);
        }

        @Override
        public void setHeight(String height){
            label.setHeight(height);
            super.setHeight(height);
        }

        @Override
        public void setWidth(int width){
            label.setWidth(width+"px");
            super.setWidth(width);
        }

        @Override
        public void setWidth(String width){
            label.setWidth(width);
            super.setWidth(width);
        }
    }

}

