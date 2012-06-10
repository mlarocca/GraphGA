/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.Label;

/**
 *
 * @author Owner
 */
public class Image extends Label{

    public Image(String source) {
        super("<img src='"+source+"'/>");
        setAutoFit(true);
    }

    public Image(String source, int width, int height) {
        super("<img src='"+source+"' style='width:"+width+"px; height:"+height+"px;'/>");
        //setWidth(width);
        //setHeight(height);
    } 

}
