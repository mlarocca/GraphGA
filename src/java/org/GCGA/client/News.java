/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

/**
 *
 * @author Eurialo
 */
public class News {
    private String title;
    private String body;
    private String day;
    private String time;

    private static final byte DATE_STR_LENGTH = 19;

    public News(String t, String b, String d) {
        title = t;
        body = b;
        storeDate(d);
    }

    private void storeDate(String date){
        if ( !checkDateFormat(date) )
            throw  new IllegalArgumentException("Bad data format");

        String[] tmp = date.split(" ");
        day = tmp[0];
        time = tmp[1];
    }

    private boolean checkDateFormat(String date){
        int tmp = date.length();
        if (tmp!= DATE_STR_LENGTH){
            return false;
        }
        tmp = date.split(" ").length-1;
        if (tmp!=1){
            return false;
        }
        tmp = date.split("-").length-1;
        if (tmp!=2){
            return false;
        }
        tmp = date.split(":").length-1;
        if (tmp!=2){
            return false;
        }

        return true;
    }

    public String getTitle(){
        return title;
    }

    public String getBody(){
        return body;
    }

    public String getDate(){
        return this.getDay();
    }

    public String getDay(){
        return day;
    }

    public String getTime(){
        return time;
    }

}
