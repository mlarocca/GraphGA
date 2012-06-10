package org.GCGA.client;
/**
 *
 * @author Owner
 */
public class Gene implements Cloneable{

    protected Integer val;

    public Gene() {
        val = 0;
    }

    public Gene(Integer val) {
        this.val = val;
    }

    public void setValue(Integer v){
        this.val = v;
    }

    public int getVal(){
        return val;
    }

    public Object clone(){
        return new Gene(this.val);
    }

    public Boolean compare(Integer v){
        return this.val == v;
    }
}