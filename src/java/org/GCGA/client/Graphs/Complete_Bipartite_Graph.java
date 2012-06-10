package org.GCGA.client.Graphs;

import org.GCGA.client.GraphType;
import org.GCGA.client.utility.Pair;

/**
 *
 * @author Owner
 */
public class Complete_Bipartite_Graph extends GraphStub{
    protected Pair<Integer, Integer> edges[];

    protected Integer n, e;
    
    public Complete_Bipartite_Graph(Integer nodes) {
        n = Complete_Bipartite_Graph.correctNodeNumber(nodes);
        e = n * n / 4;
        edges = new Pair[e];

        set_edges();
    }

    public short getType(){
        return GraphType.COMPLETE_BIPARTITE_GRAPH;
    }

    public static int correctNodeNumber(int nodes){
        int s = (int)Math.ceil((double)nodes/2.0);
        return 2 * s; //Nodes are numbered from 0 to n-1
    }
    private void set_edges(){

        int c=0;
        for (int i=0; i<n/2; i++){
            for (int j=n/2; j<n; j++){
                edges[c++] = new Pair<Integer, Integer>(i, j);
            }
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