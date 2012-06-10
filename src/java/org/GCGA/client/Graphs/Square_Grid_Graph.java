package org.GCGA.client.Graphs;

import org.GCGA.client.GraphType;
import org.GCGA.client.utility.Pair;

/**
 *
 * @author Owner
 */
public class Square_Grid_Graph extends GraphStub{
    protected Pair<Integer, Integer> edges[];

    protected Integer n, e;
    protected Integer side;

    public Square_Grid_Graph(Integer nodes) {
        n = correctNodeNumber(nodes);
        side = (int)Math.sqrt(n);
        e = (side-1) * (2 * side);
        edges = new Pair[e];

        set_edges();
    }

    public short getType(){
        return GraphType.SQUARE_GRID_GRAPH;
    }

    public static int correctNodeNumber(int nodes){
        nodes = (int)Math.ceil(Math.sqrt( nodes ));
        return nodes * nodes; //Nodes are numbered from 0 to n-1
    }

    private void set_edges(){

        int c=0, i, j;
        for (i=0; i<side-1; i++){
            for (j=0; j<side-1; j++){
                edges[c++] = new Pair<Integer, Integer>(i*side+j , i*side+(j+1));   //Arco al vertice seguente (nella riga)...
                edges[c++] = new Pair<Integer, Integer>(i*side+j , (i+1)*side+j);   //...ed arco al vertice sottostante (il seguente nella colonna)
            }
            //INVARIANTE: j==side-1
            edges[c++] = new Pair<Integer, Integer>(i*side+j , (i+1)*side+j);   //Solo arco al vertice sottostante (il seguente nella colonna)
        }
        //Invariante: i==side-1
        for (j=0; j<side-1; j++){
            //ULTIMA RIGA
            edges[c++] = new Pair<Integer, Integer>(i*side+j , i*side+(j+1));   //Solo arco al vertice seguente (nella riga)
        }
        if (c!=e){
            throw new Error("Non tutti gli archi inizializzati " +c + " " + e);
        }
    }

    public Pair<Integer, Integer> getEdgeVertices(int edgeNbr) throws ArrayIndexOutOfBoundsException{
        if (edgeNbr <0 || edgeNbr>=e ){
            throw new ArrayIndexOutOfBoundsException();
        }
        return edges[edgeNbr];
    }

    public Integer edgesNumber(){
        return e;
    }

    public Integer verticesNumber(){
        return n;
    }

}