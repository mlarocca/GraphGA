/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

/**
 *
 * @author Owner
 */
public class ExtendedCanvas extends gwt.canvas.client.Canvas{

    private static final byte DASH_LENGTH = 5;

    public ExtendedCanvas(int w, int h) {
        super(w,h);
    }

    public void drawHorizontalDashedLine(int x1, int x2, int y){
        if (x1 > x2 ){
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        beginPath();
        for(int x=x1; x<x2; x+=2*DASH_LENGTH ){
            moveTo(x, y);
            lineTo(x+DASH_LENGTH, y);
        }
        stroke();
    }

    public void drawVerticalDashedLine(int x, int y1, int y2){
        if (y1 > y2 ){
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        beginPath();
        for(int y=y1; y<y2; y+=2*DASH_LENGTH ){
            moveTo(x, y);
            lineTo(x, y+DASH_LENGTH);
        }
        stroke();
    }
}
