package org.GCGA.client.GUI;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
//import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.BoundaryDropController;
//import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import org.GCGA.client.Graphs.CustomGraph;
import org.GCGA.client.utility.IntegerCallback;
import org.GCGA.client.utility.Pair;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.utility.UtilityFunctions;
import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

/**
 *
 * @author Eurialo
 */
public class GraphCreationPanel extends Canvas{

    private static GraphCreationPanel thisPanel = null;

    private static CustomGraph graph;

    private static AbsolutePanel drawingPanel = null;

    private static PickupDragController dragController;
//    private static DropController dropController;

    private static ArrayList<Vertex> labels;

    private static Vertex selected;


    private static final byte FILE_MENU_WIDTH = 75;

    private static final String GRAPH_DB_COOKIE_NAME = "Graphs_archive";
    private static final String GRAPH_NAME_PREAMBLE = "&n";
    private static final String GRAPH_GRAPH_PREAMBLE = "&g";
    private static final String GRAPH_DATE_PREAMBLE = "&d";

    private static final String GRAPH_ID_FORBIDDEN_CHAR = "&@";

    //Fields name are kept as small as possible in order to reduce the string length because of cookie's size limitations
    private static final String JSON_VERTICES_FIELD = "v";
    private static final String JSON_EDGES_FIELD = "e";

    private static final String JSON_FIRST_EDGE_FIELD = "f";
    private static final String JSON_SECOND_EDGE_FIELD = "s";

    private static final String JSON_X_FIELD = "x";
    private static final String JSON_Y_FIELD = "y";

    private GraphCreationPanel(int width, int height) {
        super();

        this.setWidth(width);
        this.setHeight(height);

        final Canvas wrapper = new Canvas();

            drawingPanel = new AbsolutePanel();
            drawingPanel.setWidth(width+"px");
            drawingPanel.setHeight(height+"px");

        wrapper.addChild(drawingPanel);
//        wrapper.setBorder("2px solid white");
        //this.setStylePrimaryName("graphPanel");
        wrapper.setBackgroundColor(Utility.LIGHT_BACKGROUND);
        //wrapper.setTop(instructionsPanel.getBottom());

        wrapper.addDoubleClickHandler( new DoubleClickHandler() {

            public void onDoubleClick(final DoubleClickEvent dcEvent) {

                //Non aggiunge se si tiene premuto il tasto per selezionare o quello per aggiungere/eliminare archi
                if ( dcEvent.isCtrlKeyDown() || dcEvent.isAltKeyDown() )
                    return;

                graph.addNode();

                addVertex(dcEvent.getX() - wrapper.getAbsoluteLeft(), dcEvent.getY() - wrapper.getAbsoluteTop());
            }
        });

        this.addChild(wrapper);


        CollapsibleMenu fileMenu = new CollapsibleMenu("<b style='color:red; font-family:arial; font-size:12px;'>File</b>", 30, FILE_MENU_WIDTH );

        fileMenu.addEntry("<i style='color:black; font-family:"+Utility.HOVER_FONT+" font-size:11px;'>Save as</i>",
                            new AnimationCallback() {
                                public void execute(boolean earlyFinish) {
                                        saveGraph();
                                }
                            });
        fileMenu.addEntry("<i style='color:black; font-family:"+Utility.HOVER_FONT+" font-size:11px;'>Load</i>",
                            new AnimationCallback() {
                                public void execute(boolean earlyFinish) {
                                        loadGraph();
                                }
                            });

        fileMenu.addEntry("<i style='color:black; font-family:"+Utility.HOVER_FONT+" font-size:11px;'>Clear Saves Cookie</i>",
                            new AnimationCallback() {
                                public void execute(boolean earlyFinish) {
                                        clearSaves();
                                }
                            });

        fileMenu.addEntry("<i style='color:black; font-family:"+Utility.HOVER_FONT+" font-size:11px;'>Clear Graph</i>",
                            new AnimationCallback() {
                                public void execute(boolean earlyFinish) {
                                        clearGraph();
                                }
                            });

        fileMenu.finalize();
        
        fileMenu.setTop(Utility.MARGIN);
        fileMenu.setLeft(Utility.MARGIN);
        fileMenu.setBackgroundColor("yellow");
        fileMenu.setBorder("1px dashed black");
        fileMenu.setOpacity(50);
        fileMenu.bringToFront();
        this.addChild(fileMenu);

        CollapsibleLabel helpLabel = new CollapsibleLabel("<b style='color:red'>Help</b>", "<b style='color:red;'>Double click :</b><i style='color:black;'>   add a vertex</i>&nbsp&nbsp&nbsp" +
                                                                                         "<b style='color:red;'>CTRL + click :</b><i style='color:black;'>   modify selection</i>&nbsp&nbsp&nbsp"+
                                                                                         "<b style='color:red;'>ALT + click :</b><i style='color:black;'>    add/remove edges</i>&nbsp&nbsp&nbsp" +
                                                                                         "<b style='color:red;'>DEL :</b><i style='color:black;'>            remove selected vertices</i>",
                                                        25, width - FILE_MENU_WIDTH - 5*Utility.MARGIN, Utility.INSTRUCTIONS_PANEL_HEIGHT);
        helpLabel.setTop(Utility.MARGIN);
        helpLabel.setLeft(FILE_MENU_WIDTH + 2 * Utility.MARGIN);
        helpLabel.setBackgroundColor("yellow");
        helpLabel.setBorder("1px dashed black");
        //instructionsPanel.setStylePrimaryName("labelInstructions");
        helpLabel.setOpacity(50);
        helpLabel.bringToFront();
        this.addChild(helpLabel);
        
        dragController = new PickupDragController(drawingPanel, true){

            @Override
            public void toggleSelection(Widget draggable){
                super.toggleSelection(draggable);

                for (Vertex v : labels ){
                     if ( context.selectedWidgets.contains(v) ){
                        v.select();
                    }else{
                        v.deselect();
                    }
                }
            }

            @Override
            public void makeDraggable(Widget widget) {
                    super.makeDraggable(widget);
                    DOM.setStyleAttribute(widget.getElement(), "position", "absolute");
                    DOM.setStyleAttribute(widget.getElement(), "zIndex", "100");
            }

            @Override
            public BoundaryDropController newBoundaryDropController( AbsolutePanel boundaryPanel, boolean allowDropping) {
                    return new BoundaryDropController(boundaryPanel, allowDropping) {
                        @Override
                        public void onMove(DragContext context ) {
                                super.onMove(context );
                                Vertex v;
                                Label proxy;
                                for (Widget w : context.selectedWidgets){
                                    try{
                                        v = (Vertex)w;
                                        proxy = v.getProxy();
                                        drawingPanel.setWidgetPosition(proxy, v.getAbsoluteLeft() - drawingPanel.getAbsoluteLeft() , v.getAbsoluteTop() - drawingPanel.getAbsoluteTop());
                                        UIObjectConnector c = UIObjectConnector.getWrapper(proxy);
                                        if (c != null) {
                                                c.update();
                                        }
                                    }catch(ClassCastException e){
                                        continue;
                                    }
                                }
                            }
                    };
            }
        };

        dragController.setBehaviorMultipleSelection(true);

        dragController.addDragHandler( new DragHandler() {

            public void onDragEnd(DragEndEvent event) {
//                Vertex v = (Vertex) event.getSource();
               Vertex v;
                for(Widget w: dragController.getSelectedWidgets() ){
                    try{
                        v = (Vertex) w;
                        drawingPanel.setWidgetPosition(v.getProxy(), v.getLeft(), v.getTop() );
                        Collection cons = UIObjectConnector.getWrapper(v.getProxy()).getConnections();
                        for (Object c_obj: cons) {
                                Connection c = (Connection) c_obj;//i.next();
                                c.update();
                        }
                    }catch(Exception e ){
                        continue;
                    }
                }
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onDragStart(DragStartEvent event) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
//                Vertex v = (Vertex) event.getSource();
                 //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
               // throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        Event.addNativePreviewHandler( new Event.NativePreviewHandler() {

            public void onPreviewNativeEvent(NativePreviewEvent event) {

                

                //if ( event.getNativeEvent().getTypeInt() == Event.ONKEYPRESS ){
                //    alert("key down caught");
                    if ( event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE ){ //KeyboardListener.KEY_DELETE ){
                        deleteSelectedVertices();
                    }

                //}
            }
        } );
/*
        dropController = new AbsolutePositionDropController(drawingPanel);
        dragController.setConstrainWidgetToBoundaryPanel(true);
        //dragController.registerDropController(dropController);
*/
        dragController.setConstrainWidgetToBoundaryPanel(true);
        //dragController.setBehaviorBoundaryPanelDrop(false);
        dragController .setBehaviorConstrainedToBoundaryPanel(true);

        graph = new CustomGraph(0);
        labels = new ArrayList<Vertex>();
        selected = null;
    }

    public static void init(int width, int height){
        if (thisPanel==null)
            thisPanel = new GraphCreationPanel(width, height);
    }

    public static GraphCreationPanel get(){
         if (thisPanel==null)
            thisPanel = new GraphCreationPanel(Utility.DEFAULT_WINDOW_WIDTH, Utility.DEFAULT_WINDOW_HEIGHT);
        return thisPanel;
    }

    public static CustomGraph getGraph(){
        return graph;
    }

    protected static void selectEdge(Widget w){

        dragController.clearSelection();
//        dragController.resetCache();
        Vertex v;
        try{
            v = (Vertex)w;
        }catch(ClassCastException e){
            return;
        }

        if (selected==null || selected.equals(v)){
            selected = v;
            selected.selectShift();
        }else{
            addEdge(v, selected);
            
            selected.deselect();
            selected = null;

        }

        for (Vertex v2 : labels ){
            if (v2!=selected){
                v2.deselect();
            }
        }
    }

    protected static void reactivateAllVertices(){
        int x,y;
        for (Vertex v : labels ){

            x = v.getLeft();
            y = v.getTop();
            drawingPanel.remove(v);

            //drawingPanel.add(v, x, y);
            v.addToPanel(drawingPanel, x, y);
        }

    }
    protected static void deselectEdge(){

        if (selected!=null){
            selected.deselect();
            selected = null;
        }
    }
    
    public static void insertOrdDesc(ArrayList<Vertex> array, Vertex v){
        int i;
        for (i=array.size(); i>0; i--)
            if ( array.get(i-1).compare(v) >= 0 )
                break;
        array.add(i, v);   
    }

    private static void deleteSelectedVertices(){
        Iterator<Widget> sel = dragController.getSelectedWidgets().iterator();

        ArrayList<Vertex> selection = new ArrayList<Vertex>();

        Vertex v;
        while (sel.hasNext()){
            try{
                v = (Vertex)sel.next();
            }catch(ClassCastException e){
                continue;
            }
            insertOrdDesc(selection, v);
        }
 //       alert("inserted OK");

        for ( Vertex w : selection ){
//            alert(""+w.getIndex());
            deleteVertex(w);
        }
        dragController.clearSelection();
    }

    private static void deleteVertex(Vertex v){
        int i;
        graph.removeVertex(v.getIndex());
        labels.remove(i = v.getIndex());
        for ( ; i< labels.size(); i++ ){
            labels.get(i).setNewIndex(i);
        }
        v.removeVertex();

    }

    private static void clearGraph(){
        for (int i=labels.size()-1; i>=0; i--){
            graph.removeVertex(i);
            Vertex v = labels.remove(i);
            v.removeVertex();
        }
    }
    
    private static void addVertex(int x, int y){
        Vertex v = new Vertex(graph.verticesNumber()-1);

        labels.add(v);
        v.addToPanel(drawingPanel, x, y);

        dragController.makeDraggable(v);        
    }

    private static void addEdge(Vertex v, Vertex w){
            //Prova a rimuovere l'arco
            //Restituisce false <=> l'arco non esiste
            if ( !graph.removeEdge(v.getIndex(), w.getIndex() ) ){
                //In quel caso l'aggiunge;
                v.createEdge(w, drawingPanel);
                graph.addEdge(v.getIndex(), w.getIndex());

                reactivateAllVertices();

            }else{
                v.removeEdge(w, drawingPanel);
            }
    }

    private String graphToJSON(){
        JSONObject element = new  JSONObject();
        JSONArray vertices = new JSONArray();
        JSONArray edges = new JSONArray();
        
        
        Pair<Integer, Integer> e;

        int i=0;
        for (Vertex v: labels){
            JSONObject vertex = new JSONObject();
            vertex.put(JSON_X_FIELD, new JSONNumber(v.getLeft()) );
            vertex.put(JSON_Y_FIELD, new JSONNumber(v.getTop()) );
            vertices.set(i++, vertex);
        }

        element.put(JSON_VERTICES_FIELD, vertices );

        for (i=0; i<graph.edgesNumber(); i++ ){
            JSONObject edge = new JSONObject();
            e = graph.getEdgeVertices(i);
            edge.put(JSON_FIRST_EDGE_FIELD, new JSONNumber(e.getFirst()) );
            edge.put(JSON_SECOND_EDGE_FIELD, new JSONNumber(e.getSecond()) );
            edges.set(i, edge);
        }
        element.put(JSON_EDGES_FIELD, edges );

//        UtilityFunctions.alert(element.toString());
        
        return element.toString();
    }

    private void JSONtoGraph(String g){
        JSONValue valueObject;
        JSONObject graphObject;
        valueObject = JSONParser.parse(g);

        if ( (graphObject=valueObject.isObject())==null ){
            SC.warn("ERROR: the saved entry is not well formed!");
        }

        JSONArray vertices = graphObject.get(JSON_VERTICES_FIELD).isArray();
        if (vertices == null)
            return;
        JSONArray edges = graphObject.get(JSON_EDGES_FIELD).isArray();
        if (edges == null)
            return;
        int i, x, y, s = vertices.size();
        JSONObject tmp;
        JSONNumber buffer;

//Vertici
        for(i=0; i<s; i++){
            tmp = vertices.get(i).isObject();
            if (tmp==null){
                clearGraph();
                return;
            }
            buffer = tmp.get(JSON_X_FIELD).isNumber();
            if (buffer==null){
                x = 0;
            }else{
                x = (int) buffer.doubleValue();
            }
            buffer = tmp.get(JSON_Y_FIELD).isNumber();
            if (buffer==null){
                y = 0;
            }else{
                y = (int) buffer.doubleValue();
            }

//            UtilityFunctions.alert("("+x + " , " + y + ")");

            graph.addNode();
            addVertex(x, y);

        }

//Archi
        s = edges.size();
        for(i=0; i<s; i++){
            tmp = edges.get(i).isObject();
            if (tmp==null)
                continue;
            buffer = tmp.get(JSON_FIRST_EDGE_FIELD).isNumber();
            if (buffer==null)
                continue;
            x = (int) buffer.doubleValue();
            buffer = tmp.get(JSON_SECOND_EDGE_FIELD).isNumber();
            if (buffer==null)
                continue;
            y = (int) buffer.doubleValue();
//            UtilityFunctions.alert(x + " -> " + y);
            try{
                addEdge(labels.get(x), labels.get(y));
            }catch(IndexOutOfBoundsException e){
                continue;
            }
        }

    }

    private void loadGraph(){
        if ( !Cookies.isCookieEnabled() ){
            SC.warn("In order to save and load Custom Graphs <b style='color:red'>Cookies MUST be enabled</b>");
            return;
        }

        final String old_value = Cookies.getCookie(GRAPH_DB_COOKIE_NAME) != null ? Cookies.getCookie(GRAPH_DB_COOKIE_NAME) : "";
//        UtilityFunctions.alert(old_value);

        final ArrayList<ListGridRecord> list = new ArrayList();
        final ArrayList<String> graphs = new ArrayList();
        if ( !tokenize(old_value, list, graphs) ){
            SC.say("No graph saved");
            return;
        }

        ListGridField[] fields = new ListGridField[2];
        fields[0] = new ListGridField(GRAPH_NAME_PREAMBLE, "Entry ID", 210) ;
        fields[0].setAlign(Alignment.CENTER);
        //graphType.setst
        fields[1] = new ListGridField(GRAPH_GRAPH_PREAMBLE, "Saved on", 125);
        fields[1].setAlign(Alignment.CENTER);

        new ListSelectionDialog("Select a saving slot", 
                                "<b style='color:red'>Select among the saved custom graphs the one you want to load:</b>",
                                fields, list,
                                new IntegerCallback() {

                                    public void execute(boolean earlyFinish) {
                                        throw new UnsupportedOperationException("Parameter must be an Integer");
                                    }

                                    public void execute(int s) {
                                        if ( s!=Utility.INVALID_INDEX ){
                                            clearGraph();
                                            JSONtoGraph( graphs.get(s) );
                                        }
                                    }
        });       

    }

    /**
     *
     * @param source - The string to be parsed
     * @param list - Output variable
     * @param graphs - Output variable
     * @return
     */
    private boolean tokenize(String source, final ArrayList<ListGridRecord> list, final ArrayList<String> graphs){
        int len = source.length();
        int start_name = UtilityFunctions.tokenFirstOccurrence(source, GRAPH_NAME_PREAMBLE);
        if (start_name == Utility.INVALID_INDEX ){
            return false;
        }
        int row = 0;
        while (start_name<len ){
            start_name += GRAPH_NAME_PREAMBLE.length();
            int end_name = UtilityFunctions.tokenFirstOccurrence(source, start_name, GRAPH_GRAPH_PREAMBLE) ;
            if (end_name == Utility.INVALID_INDEX ){
                break;
            }
            int start_graph = end_name + GRAPH_GRAPH_PREAMBLE.length();
            int end_graph = UtilityFunctions.tokenFirstOccurrence(source, start_graph, GRAPH_DATE_PREAMBLE) ;
            //UtilityFunctions.alert(end_graph);
            if (end_graph == Utility.INVALID_INDEX){
                break;
            }
            int start_date = end_graph + GRAPH_DATE_PREAMBLE.length();
            int end_date = UtilityFunctions.tokenFirstOccurrence(source, start_date, GRAPH_NAME_PREAMBLE) ;
            //UtilityFunctions.alert(end_graph);
            if (end_date == Utility.INVALID_INDEX){
                end_date = source.length();
            }
/*
                        UtilityFunctions.alert("name: "+source.substring(start_name, end_name) + "\n\r\n" +
                                    "data: " +  source.substring(start_graph, end_graph) + "\n\r\n" +
                                    "date: " + source.substring(start_date, end_date) );
*/
            ListGridRecord tmp = new ListGridRecord();
            tmp.setAttribute("row", row++);
            tmp.setAttribute(GRAPH_NAME_PREAMBLE, source.substring(start_name, end_name));
            tmp.setAttribute(GRAPH_GRAPH_PREAMBLE, source.substring(start_date, end_date));
            
            list.add( tmp );
            graphs.add( source.substring(start_graph, end_graph) );

            start_name = end_date;    //Sets the right value for next iteration
        }
        return list.size()>0;
    }

    private void saveGraph(){
        if (graph.verticesNumber()<=0){
            SC.say("Please insert at least one vertex.");
            return;
        }


        final String g = graphToJSON();

        if ( !Cookies.isCookieEnabled() ){
            SC.warn("In order to save Custom Graphs <b style='color:red'>Cookies MUST be enabled</b>");
            return;
        }

        final String old_value = Cookies.getCookie(GRAPH_DB_COOKIE_NAME) != null ? Cookies.getCookie(GRAPH_DB_COOKIE_NAME) : "";

//UtilityFunctions.alert(old_value);

        SC.askforValue("<b style='color:red;'>Insert an ID for the graph to save</b>" +
                "<br><b>Be aware that once cookies gets erased, you<br> won't be able to recover your data anymore.</b>" +
                "<br><b>Take also into consideration that the amount<br> of data that can be saved in a cookie is limited,<br> so you should use it wisely.<br>It's also higly recommended, after saving a<br>graph, to check that it can be correctly loaded.</b>", new ValueCallback() {

            public void execute(String value) {
                if ( value==null || value.equals("") || UtilityFunctions.stringContainsChar(value, GRAPH_ID_FORBIDDEN_CHAR) ){
                    SC.say("Invalid ID");
                    return;
                }
                else{
            //<BEGIN : Encode the current date-hour to a string>
                    Date now = new Date() ;
                    String date = (now.getYear()+1900) + "/";
                //Month
                    String tmp = ""+(now.getMonth()+1);
                    if (tmp.length()==1)
                        tmp="0"+tmp;
                    date += tmp + "/";
                //Day
                    tmp = "" + now.getDate();
                    if (tmp.length()==1)
                        tmp="0"+tmp;
                    date += tmp + " ";
                //Hour
                    tmp = "" + now.getHours();
                    if (tmp.length()==1)
                        tmp="0"+tmp;
                    date += tmp + ":";
                //Hour
                    tmp = "" + now.getMinutes();
                    if (tmp.length()==1)
                        tmp="0"+tmp;
                    date += tmp;
             //<END>
                    Cookies.setCookie(GRAPH_DB_COOKIE_NAME, old_value + GRAPH_NAME_PREAMBLE+value+GRAPH_GRAPH_PREAMBLE+g+GRAPH_DATE_PREAMBLE+date, null);
                }
            }
        });

        return;
    }

    private void clearSaves(){
        if ( !Cookies.isCookieEnabled() ){
            SC.say("<b>Cookies disabled</b>" +
                    "<br>In order to save Custom Graphs <b style='color:red'>Cookies MUST be enabled</b>");
            return;
        }else{
            SC.ask("Are you sure you want to procede?<br><b style='color:red;'>WARNING: all saved custom graphs will be lost!</b>", new BooleanCallback() {

                public void execute(Boolean value) {
                    if (value){
                        Cookies.removeCookie(GRAPH_DB_COOKIE_NAME);
                    }
                }
            });
        }
    }

}
