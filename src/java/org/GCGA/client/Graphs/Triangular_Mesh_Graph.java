package org.GCGA.client.Graphs;

import org.GCGA.client.GraphType;
import org.GCGA.client.algorithm.GeneticAlgorithm;
import org.GCGA.client.utility.Pair;

public class Triangular_Mesh_Graph extends GraphStub{
    protected Pair<Integer, Integer> edges[];

    protected Integer n, e;

    public Triangular_Mesh_Graph(Integer nodes) {
        n = correctNodeNumber(nodes);
        e = correctEdgeNumber(nodes);
        edges = new Pair[e];

        set_edges();
    }

    @Override
    public short getType(){
        return GraphType.TRIANGULAR_MESH_GRAPH;
    }

    public static int correctNodeNumber(int nodes){
        double m;
        nodes--;
        do{
            m = Math.sqrt(8*(++nodes)+1)-1;

        }while(m%2!=0);

        return nodes;
    }

    public static int correctEdgeNumber(int nodes){
        double m;
        do{
            m = Math.sqrt(8*(--nodes)+1)-1;

        }while(nodes>1 && m%2!=0);

        return nodes*3;
    }
    private void set_edges(){

        int c=0, j, k, row_size;
        for (k=0, row_size=1; k<n-row_size; k+=row_size++  ){
            for (j=0; j<row_size; j++){
                edges[c++] = new Pair<Integer, Integer>(k+j , k+j+row_size);   //Arco al figlio sinistro...
                edges[c++] = new Pair<Integer, Integer>(k+j , k+j+row_size+1);   //...ed arco al figlio destro
                edges[c++] = new Pair<Integer, Integer>(k+j+row_size , k+j+row_size+1);   //...ed arco tra i figli
            }
        }

        if (c!=e){
//            GeneticAlgorithm.alert(c + " != " + e );
            throw new Error("Non tutti gli archi inizializzati " + c + " " + e);
        }
    }

    public Pair<Integer, Integer> getEdgeVertices(int edgeNbr) throws ArrayIndexOutOfBoundsException{
        if (edgeNbr <0 || edgeNbr>=e ){
            throw new ArrayIndexOutOfBoundsException();
        }
        return edges[edgeNbr];
    }

    @Override
    public Integer edgesNumber(){
        return e;
    }

    @Override
    public Integer verticesNumber(){
        return n;
    }

}