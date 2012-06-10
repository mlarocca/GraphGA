/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

/**
 *
 * @author Eurialo
 */
public class GAElementTern extends Tern<String, Integer, Integer>{

       public GAElementTern( String key, int firstOccurrence, int counter ){
            super(key, firstOccurrence, counter);
       }

       public GAElementTern( String key, int firstOccurrence ){
            super(key, firstOccurrence, 1);
       }

       public void decrementCounter(){
           this.third--;
       }

       public void incrementCounter(){
           this.third++;
       }

       public void setCounter(int val){
           this.third = val;
       }

       public String getKey(){
           return super.getFirst();
       }

       public int getFirstOccurrence(){
           return super.getSecond();
       }

       public int getCounter(){
           return super.getThird();
       }
}
