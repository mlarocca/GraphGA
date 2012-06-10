/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import org.GCGA.client.GUI.Vertex;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;

/**
 *
 * @author Eurialo
 */
public class EdgeConnection extends StraightTwoEndedConnection{

    private int v,w;


    public EdgeConnection(Vertex v, Vertex w) throws IllegalArgumentException{
        super( v.createConnector(), w.createConnector() );
        this.v = v.getIndex();
        this.w = w.getIndex();
        if (this.v==this.w)
            throw new IllegalArgumentException();
    }

    protected int getFirstVertex(){
        return v;
    }
    protected int getSecondVertex(){
        return w;
    }

    public boolean equals(EdgeConnection e){
        return (this.v == e.getFirstVertex() && this.w==e.getSecondVertex())  || (this.w == e.getFirstVertex() && this.v==e.getSecondVertex());
    }

    public boolean equals(Vertex v1, Vertex w1){
        return (this.v == v1.getIndex() && this.w==w1.getIndex())  || (this.w == v1.getIndex() && this.v==w1.getIndex());
    }

}
