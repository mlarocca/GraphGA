/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

import java.util.ArrayList;

/**
 *
 * @author Marcello
 */
public class DataRecordSet extends ArrayList<DataRecord>{

    private final String setName;

    public DataRecordSet(String sName) {
        super();
        setName = sName;
    }

    public String getName(){
        return setName;
    }

    public boolean equals(String sName){
        return setName.equals(sName);
    }

    public void add(String fieldName, Double value){
        for ( DataRecord d: this ){
            if (d.equals(fieldName)){
                d.addData(value);
                return;
            }
        }
        DataRecord tmp;

        tmp = new DataRecord(fieldName);
        this.add(tmp);
        tmp.addData(value);
    }

    public DataRecord contains(String fieldName){
        for ( DataRecord d: this ){
            if (d.equals(fieldName))
                return d;
        }
        return null;
    }
/*
    public DataRecord get(String fieldName){
        return contains(fieldName);
    }
*/

    public void hideField(String fieldName) throws IllegalArgumentException{
        DataRecord d = contains(fieldName);
        if (d==null)
            throw new IllegalArgumentException();
        d.hideDataRecord();
    }

    public void showField(String fieldName) throws IllegalArgumentException{
        DataRecord d = contains(fieldName);
        if (d==null)
            throw new IllegalArgumentException();
        d.showDataRecord();
    }


    public double getLastValue(String fieldName) throws IllegalArgumentException{
        DataRecord d = contains(fieldName);
        if (d==null)
            throw new IllegalArgumentException();
        else
            return d.getLastValue();
    }
}
