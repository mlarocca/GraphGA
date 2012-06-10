/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

import java.util.ArrayList;

/**
 *
 * @author Owner
 */
public class RecordFolder extends ArrayList<DataRecordSet>{

    public RecordFolder() {
        super();
    }
    
    public void add(String folderName, String fieldName, Double value){
        for ( DataRecordSet d: this ){
            if (d.equals(folderName)){
                d.add(fieldName, value);
                return;
            }
        }
        //Folder must exist before!
        throw new IllegalArgumentException();
    }

    public byte addFolder(String folderName){
        for (DataRecordSet d : this){
             
            if (d.equals(folderName)){
                return Utility.INVALID_INDEX;
            }
        }
        DataRecordSet tmp;

        tmp = new DataRecordSet(folderName);
        this.add(tmp);
        return (byte)(size()-1);
    }

    public DataRecordSet contains(String folderName){
        for ( DataRecordSet d: this ){
            if (d.equals(folderName))
                return d;
        }
        return null;        
    }

    public String getDataRecordName(int index){
        if (index<0 || index>= this.size() )
            return null;
        return this.get(index).getName();
    }

    public byte getDataRecordSetIndex(String name){
        for(byte i=0; i<size(); i++){
            DataRecordSet tmp = this.get(i);

            if (tmp.getName().equals(name))
                return i;
        }
        return Utility.INVALID_INDEX;
    }

}
