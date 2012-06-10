/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.Graphs;

import org.GCGA.client.utility.Pair;
import java.util.ArrayList;
import org.GCGA.client.GraphType;
import org.GCGA.client.utility.UtilityFunctions;

/**
 *
 * @author Eurialo
 */
public class CustomGraph extends GraphStub{

    protected ArrayList<Pair<Integer, Integer>> edges;

    protected Integer n;

    public CustomGraph(Integer nodes) {
        n = nodes; //Nodes are numbered from 0 to n-1

        edges = new ArrayList<Pair<Integer, Integer>>();
    }

    public CustomGraph() {
        n = 0; //Nodes are numbered from 0 to n-1

        edges = new ArrayList<Pair<Integer, Integer>>();
    }

    public void addNode(){
        n++;
    }

    public void removeVertex(int i){
        if (i<0 || i>=n){
//            UtilityFunctions.alert(i+"");
            throw new IllegalArgumentException("Index out of range [0.."+(n-1)+"]");
        }
        n--;
        Pair<Integer,Integer> e;
        int v,w;
        //Rimuove tutti gli archi incidenti sul vertice i;
        //Aggiorna tutti gli archi per cui almeno uno dei vertici ha indice k>i (il nuovo valore per lo stesso vertice sarà k-1);
        for (int j=0; j<edges.size(); j++){
            e = edges.get(j);
            v = e.getFirst();
            w = e.getSecond();
            if ( v==i || w==i ){
                edges.remove(j);
                j--;    //Rimuove l'arco corrente, quindi alla prossima iterazione l'elemento successivo sarà all'indice j => j deve mantenere il valore attuale
            } else if ( v > i ){
                 //Gli archi sono coppie ordinate (v,w) => se v>i, allora w>i
                edges.set(j, new Pair((v-1),(w-1)));
            }
            else if ( w > i ){
                // v<i<w
                edges.set(j, new Pair(v,(w-1)));
            }
        }
    }

    @Override
    public short getType(){
        return GraphType.CUSTOM_GRAPH;
    }

    public Pair<Integer, Integer> getEdgeVertices(int edgeNbr) throws ArrayIndexOutOfBoundsException{
        if (edgeNbr <0 || edgeNbr>=edges.size() ){
            throw new ArrayIndexOutOfBoundsException();
        }
        return edges.get(edgeNbr);
    }

    public void addEdge(int v, int w){
        if (v==w)
            throw new IllegalArgumentException("Self edges not ammissible");
        else if (v>w){
            int tmp = v;
            v=w;
            w=tmp;
        }
        Pair<Integer, Integer> e = new Pair(v, w);
      

        //Aggiunge gli archi in ordine
        int i, res;
        for (i=edges.size(); i>0; i--){
            res = edges.get(i-1).compareTo(e);
            if (res == 0){
                throw new IllegalArgumentException("Edge already added to the graph");                
            }else if ( res < 0 )  //INVARIANTE: Non può essere uguale!
                break;
            //if (res > 0 ) continue;
        }
        edges.add(i,e);
    }

    public boolean removeEdge(int v, int w) throws IllegalArgumentException {
        if (v==w)
            throw new IllegalArgumentException("Self edges not ammissible");
        else if (v>w){
            int tmp = v;
            v=w;
            w=tmp;
        }
        Pair<Integer, Integer> e = new Pair(v, w);

        boolean found = false;
        for (Pair<Integer, Integer> e2 : edges ){
            if ( e.equals(e2) ){
                edges.remove(e2);
                found = true;
                break;
            }

        }
        /*
        if ( ! edges.contains(e) ){
            alert("Do not Contain edge " + v + " , " + w);

            return false;
        }*/
        if ( !found )
            return false;
        else
            return true;
    }

    @Override
    public Integer edgesNumber(){
        return edges.size();
    }

    @Override
    public Integer verticesNumber(){
        return n;
    }
/*
    public void print(){
        String txt = "Vertici: "+verticesNumber();

        for (Pair<Integer,Integer> e : edges ){
            txt = txt + "\n" + e.getFirst() + "=>" + e.getSecond();

        }

        alert(txt);

    }
 * 
 */
/*
    private static native void alert(String message)/*-{
		alert(message);
    }-* /;
 *
 */
}
