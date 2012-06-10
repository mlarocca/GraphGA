/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.utility;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;

/**
 *
 * @author Owner
 */
public class UtilityFunctions {

    private static UtilityFunctions thisClass=null;

    private UtilityFunctions(){
        thisClass = this;
    }

    public static UtilityFunctions get(){
        if (thisClass==null)
            thisClass = new UtilityFunctions();
        return thisClass;
    }

    public static void setWidgetPrompt(Canvas w, String title, String body){
        w.setHoverWidth(Utility.HOVER_WIDTH);
        w.setHoverMoveWithMouse(Boolean.TRUE);
        w.setHoverStyle("padding:0px;");
        w.setPrompt(Utility.HOVER_TITLE_PREAMBLE + title+ Utility.HOVER_TITLE_CLOSURE + Utility.HOVER_BODY_PREAMBLE + body + Utility.HOVER_BODY_CLOSURE);
        w.setHoverOpacity(Utility.HOVER_OPACITY);
    }

    public static void setWidgetPrompt(FormItem w, String title, String body){
        w.setHoverWidth(Utility.HOVER_WIDTH);
        w.setHoverStyle("padding:0px;");
        w.setPrompt(Utility.HOVER_TITLE_PREAMBLE + title+ Utility.HOVER_TITLE_CLOSURE + Utility.HOVER_BODY_PREAMBLE + body + Utility.HOVER_BODY_CLOSURE);
        w.setHoverOpacity(Utility.HOVER_OPACITY);
    }

    public static void changeWidgetPromptText(Canvas w, String title, String body){
        w.setHoverStyle("padding:0px;");
        w.setPrompt(Utility.HOVER_TITLE_PREAMBLE + title+ Utility.HOVER_TITLE_CLOSURE + Utility.HOVER_BODY_PREAMBLE + body + Utility.HOVER_BODY_CLOSURE);
    }

    public static int MCD(int a, int b){
        int r=1;
        if (a<b){
            r=a;
            a=b;
            b=r;
            r=1;
        }
        while (r!=0){
            r = b%a;
            b=a;
            a=r;
        }
        return b;
    }


    public static boolean wellFormedEmailAddress(String email){

        String[] parts = email.split("@");

//GeneticAlgorithm.alert(email + " -> " + parts.length);
        if (parts.length!=2)
            return false;

        String account = parts[0];
        String domain = parts[1];
    //Account
        char first = account.charAt(0);
        //Check fst character
        if (   (  (first < 'A' || first > 'Z') && (  first < 'a' || first > 'z' )  )   )
            return false;
        if (stringContainsChar(account , Utility.INVALID_EMAIL_CHARACTERS ))
            return false;
//GeneticAlgorithm.alert("Account ok");
    //Domain
        if (stringContainsChar(domain , Utility.INVALID_EMAIL_CHARACTERS ))
            return false;
//GeneticAlgorithm.alert("Checking domain parts: " + domain);
        parts = domain.replace(".", "@").split("@");    //split doesn't work with '.'
//GeneticAlgorithm.alert("len: " + parts.length);
        if (parts.length < 2)
            return false;
//GeneticAlgorithm.alert("Checking each domain part");
        for (String s : parts ){
            if ( s.length() < 2 )
                return false;
        }
        //Checks last portion of the domain
//GeneticAlgorithm.alert("Checking last domain part");

        int l = parts[parts.length-1].length();
        if (l<2 || l>5) //for .it to .store <-> 2 to 5 chars
            return false;

        return true;

    }

    /**
     *
     * @param str   - The string to check
     * @param characters - The character to look for
     * @return - true <=> at least one occurrence of any one of the characters appears in str
     */
    public static boolean stringContainsChar(String str, String characters){
        try{
            for( char c : characters.toCharArray() ){
                if (str.contains(c+""))
                    return true;
            }
            return false;
        }catch(Exception e){
            return true;
        }
    }

    public static int tokenFirstOccurrence(String str, String token){
        int i=0;
        int t_len=token.length();
        int len = str.length();
//      alert(token);
        while (i<len-t_len){
//            alert(str.substring(i, i+t_len));
            if ( str.substring(i, i+t_len).equals(token) )
                return i;
            else
                i++;
        }
        return Utility.INVALID_INDEX;
    }

    public static int tokenFirstOccurrence(String str, int beginIndex, String token){
        int tmp = tokenFirstOccurrence(str.substring(beginIndex), token);
        if (tmp==Utility.INVALID_INDEX)
            return tmp;
        else
            return beginIndex + tmp;
    }

    public static void alert (Object o) throws ClassCastException{
        try{
            alert(o.toString());
        }catch (Exception e){
            throw new ClassCastException("Impossible to print value");
        }
    }

    public static native void alert(String message)/*-{
		alert(message);
    }-*/;

}
