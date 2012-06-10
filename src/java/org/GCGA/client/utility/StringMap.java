/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

import java.util.ArrayList;

/**
 *
 * @author Eurialo
 */
public class StringMap extends ArrayList<GAElementTern>{

    public StringMap() {
        super();
    }

    public GAElementTern find(String key){
        for (GAElementTern p : this){
            if ( p.getKey().equals(key) )
                return p;
        }
        return null;
    }

    /**
     * @param key - The key
     * @return true     <=> The element already existed
     *         false    <=> The element has been newly created
     */
    public GAElementTern add(String key, int index){
         GAElementTern p = find(key);
         if (p!=null){
             p.incrementCounter();
             return p;
         }else{
             p = new GAElementTern(key, index);
             this.add(p);
             return null;
         }
    }

    /**
     * @param key - The key
     * @return true     <=> The element already existed
     *         false    <=> The element has been newly created
     */
    public void remove(String key){
         GAElementTern p = find(key);
         if (p!=null){
             p.decrementCounter();
             if ( p.getCounter() <= 0)
                 this.remove(p);
         }
    }

    public int getFirstOccurrence(String key) throws IllegalArgumentException{
        GAElementTern p = find(key);
        if (p==null)
            throw new IllegalArgumentException();
        else
            return p.getFirstOccurrence();
    }

    public int getCounter(String key) throws IllegalArgumentException{
        GAElementTern p = find(key);
        if (p==null)
            throw new IllegalArgumentException();
        else
            return p.getCounter();
    }

}
