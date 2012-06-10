
package org.GCGA.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;

/*
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
 * */

/**
 *
 * @author Owner
 */
public class Chromosome implements Cloneable {

    Gene genes[];
    Integer n; //Numero di nodi nella griglia

    public Chromosome(int l, int n) {
        this.n = n > 0 ? n : 0;
        l = l>0 ? l : 0;
        int tmp;

        genes = new Gene[l];

        for (int i=0; i<l; i++){
            do{
                tmp = (int)Math.floor(Math.random() * n);
            }while ( this.conflict(tmp) );

            genes[i] = new Gene( tmp );
        }
    }

    public Chromosome(int l, int n, boolean takeplace) {
        this.n = n > 0 ? n : 0;
        l = l>0 ? l : 0;
        genes = new Gene[l];

        for (int i=0; i<l; i++){
            genes[i] = new Gene(i);
        }
    }


    //Override per classi dello stesso package con cui implementare clone!!!
    public Chromosome(Gene[] a, int n) {
        int l;
        if (a==null){
            l = 0;
        }
        else{
            l = a.length;
        }

        this.n = n > 0 ? n : 0;
        genes = new Gene[l];
        for(int i=0; i<genes.length; i++){
            genes[i] = (Gene)a[i].clone();
        }
    }

    private Gene getGene(int i) throws IndexOutOfBoundsException{
        if ( i<0 || i>= genesNumber() ){
            throw new IndexOutOfBoundsException();
        }
        //else

        return genes[i];
    }

    public int getGeneValue(int i)throws IndexOutOfBoundsException{
        if ( i<0 || i>= genesNumber() ){
            throw new IndexOutOfBoundsException();
        }
        //else

        return genes[i].getVal();
    }

    public void setGene(int i, Gene val) throws ClassCastException, IndexOutOfBoundsException, ArrayStoreException{
        if ( i<0 || i>= genesNumber() ){
            throw new IndexOutOfBoundsException();
        }
   /*     if ( positions[genes[i].getVal()] == UNUSED_POSITION )
            throw new ArrayStoreException();*/
        genes[i].setValue(val.getVal());
    }

    public void setGene(int i, int val) throws ClassCastException, IndexOutOfBoundsException, NullPointerException, ArrayStoreException{
        if ( i<0 || i>= genesNumber() ){
            throw new IndexOutOfBoundsException();
        }
 /*       if (  positions[genes[i].getVal()] == UNUSED_POSITION)
            throw new ArrayStoreException();*/
        genes[i].setValue(val);

    }

    public void exchangeGenes(int i1, int i2 ){
        if (  i1<0 || i1>= genesNumber()
           || i2<0 || i2>= genesNumber()
           ){
            throw new IndexOutOfBoundsException();
        }
        int v1 = genes[i1].getVal();
        int v2 = genes[i2].getVal();

        genes[i1].setValue(v2);
        genes[i2].setValue(v1);
    }

    public void setGeneRandom(int gene_index, int max_val){

        int tmp;
        do{
            tmp = (int)Math.floor(Math.random() * max_val);
        }while ( this.conflict(tmp) );
        this.setGene(gene_index, new Gene(tmp));
    }

    public boolean hasPosition(int position){
        for (int i=0; i<genes.length; i++)
            if ( genes[i]!=null && genes[i].getVal()==position )
                return true;
        return false;
    }

    public int genesNumber(){
        return genes.length;
    }

    public long getGridPosition(int vertex){
        return genes[vertex].getVal();
    }

    public int[] toArray(){
        int s = genes.length;
        int[] output = new int[s];

        for (int i=0; i<s; i++)
            output[i] = getGeneValue(i);

        return output;
    }

    public JSONArray toJSONArray(){
        JSONArray jarr = new JSONArray();
        int[] arr = toArray();
        for (int i=0; i<arr.length; i++)
            jarr.set(i, new JSONNumber(arr[i]) );
    
        return jarr;
    }

    //return true <=> the position is already taken
    public boolean conflict(int position){
        return hasPosition(position);
    }

    public Object clone(){
        return new Chromosome(genes, n);
    }


    public boolean check(){
        int size = 0;
        /*
        for(int i = 0; i<positions.length; i++)
            if (positions[i]!=UNUSED_POSITION)
                size++;

        if (size != genes.length){
            return false;
        }
        else

         */
            return true;
        /*
        Stack<Long> tmp = new Stack();

        Iterator it = positions.keySet().iterator();

        while( it.hasNext() ){
            tmp.push((Long)it.next());
        }
        while (!tmp.empty()){
            Long v = (Long)tmp.pop();
            if ( tmp.contains( v ) ){
                return false;
            }
        }
        return true;//OK*/

    }

    @Override
    public String toString(){
        String res = "";
        for (int i=0; i<genesNumber(); i++)
            res += genes[i].getVal();
        return res;
    }

}