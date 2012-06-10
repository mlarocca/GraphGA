/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

import org.GCGA.client.Graphs.Complete_Bipartite_Graph;
import org.GCGA.client.Graphs.Square_Grid_Graph;
import org.GCGA.client.Graphs.Complete_Graph;
import org.GCGA.client.Graphs.GraphStub;
import org.GCGA.client.GUI.GraphPanel;
import org.GCGA.client.utility.Pair;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import org.GCGA.client.Graphs.Triangular_Mesh_Graph;

/**
 *
 * @author Marcello
 */
public class GraphEmbedding {

    private int vNbr;
    private short graphType;
    private int gridSideSize;
    private int[] gridPositions = null;
    private GraphStub graph = null;

    public GraphEmbedding(int vN, short gType, int gSSize, JSONArray jarr ) {
        this.graphType = gType;
//alert(this.graphType + " | " + vN);
        switch (graphType){
            case GraphType.COMPLETE_BIPARTITE_GRAPH:
                if ( vN != Complete_Bipartite_Graph.correctNodeNumber(vN) )
                    throw new IllegalArgumentException("Illegal vertices number for this kind of graph");
                break;
            case GraphType.SQUARE_GRID_GRAPH:
                if ( vN != Square_Grid_Graph.correctNodeNumber(vN) )
                    throw new IllegalArgumentException("Illegal vertices number for this kind of graph"); 
                break;
            case GraphType.TRIANGULAR_MESH_GRAPH:
                if ( vN != Triangular_Mesh_Graph.correctNodeNumber(vN) )
                    throw new IllegalArgumentException("Illegal vertices number for this kind of graph");
                break;
            case GraphType.COMPLETE_GRAPH:
                break;
            case GraphType.ERROR:
            default:
                throw new IllegalArgumentException("Invalid graph type");
        }
        
        if ( (this.vNbr = vN) != jarr.size())
            throw new IllegalArgumentException("Positions' array not compatible with supplied vertex number");

        gridSideSize = gSSize;
        
        gridPositions = new int[vNbr];
        try{
            for(int i=0; i<vNbr; i++)
                gridPositions[i] = (int)jarr.get(i).isNumber().doubleValue();
        }catch(JSONException e){
            gridPositions = null;
            throw new IllegalArgumentException("Position's array format exception");
        }
    }
/*
    public boolean draw(GraphPanel gp){

        alert ("Vertices: "+graph.verticesNumber());
        return true;
    }
*/
    private void createGraph(){
        if (graph == null){
            switch (this.graphType){
                case GraphType.COMPLETE_GRAPH:
                    graph = (GraphStub)new Complete_Graph(vNbr);
                    break;
                case GraphType.COMPLETE_BIPARTITE_GRAPH:
                    graph = (GraphStub)new Complete_Bipartite_Graph(vNbr);
                    break;
                case GraphType.SQUARE_GRID_GRAPH:
                    graph = (GraphStub)new Square_Grid_Graph(vNbr);
                    break;
                case GraphType.TRIANGULAR_MESH_GRAPH:
                    graph = (GraphStub)new Triangular_Mesh_Graph(vNbr);
                    break;
                case GraphType.ERROR:
                default:
                    throw new IllegalArgumentException("Invalid graph type");
            }

        }
    }

    public int getVerticesNumber(){
        createGraph();
        return graph.verticesNumber();
    }

    public int getGridSideSize(){
        return gridSideSize;
    }

    public String getGraphTypeLabel(){
        switch (graphType){
            case GraphType.COMPLETE_GRAPH:
                return "Complete Graph";
            case GraphType.COMPLETE_BIPARTITE_GRAPH:
                return "Complete Bipartite Graph";
            case GraphType.SQUARE_GRID_GRAPH:
                return "Square Mesh Graph";
            case GraphType.TRIANGULAR_MESH_GRAPH:
                return "Triangular Mesh Graph";
            default:
                return "Type Error";
        }
    }    


    public boolean drawGraph(GraphPanel gP, Grid grid){
        if (gridPositions == null)
            return false;

        try{
            createGraph();

            //Disegna prima i Vertici...
            for (int i=0; i<vNbr; i++){
                grid.paintVertex(gP, i, gridPositions[i]);
            }
            //...ed infine gli archi
            for (int i=0; i < graph.edgesNumber(); i++ ){
                Pair vertices = graph.getEdgeVertices(i);
                grid.paintEdge(gP, (Integer)vertices.getFirst(), (Integer)vertices.getSecond());
            }
            
            return true;
        }catch(Exception e){
            return false;
        }

    }
/*
    private static native void alert(String message)/*-{
		alert(message);
    }-* /;
 *
 */

}
