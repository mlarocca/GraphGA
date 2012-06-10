package org.GCGA.client.Graphs;

import org.GCGA.client.GraphType;
import org.GCGA.client.utility.Pair;

/**
 *
 * @author Owner
 */
public class Complete_Graph extends GraphStub{
    protected Pair<Integer, Integer> edges[];

    protected Integer n, v;

    public Complete_Graph(Integer nodes) {
        n = nodes; //Nodes are numbered from 0 to n-1
        v = n * (n-1) /2;
        edges = new Pair[v];

        set_edges();
    }

    public short getType(){
        return GraphType.COMPLETE_GRAPH;
    }

    private void set_edges(){

        int c=0;
        for (int i=0; i<n-1; i++){
            for (int j=i+1; j<n; j++){
                edges[c++] = new Pair<Integer, Integer>(i, j);
            }
        }
        if (c!=v){
            throw new Error("Non tutti gli archi inizializzati " +c + " " + v);
        }
    }

    public Pair<Integer, Integer> getEdgeVertices(int edgeNbr) throws ArrayIndexOutOfBoundsException{
        if (edgeNbr <0 || edgeNbr>=v ){
            throw new ArrayIndexOutOfBoundsException();
        }
        return edges[edgeNbr];
    }

    public Integer edgesNumber(){
        return v;
    }

    public Integer verticesNumber(){
        return n;
    }

}


