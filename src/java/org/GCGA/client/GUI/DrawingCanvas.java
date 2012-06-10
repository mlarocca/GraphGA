/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import pl.balon.gwt.diagrams.client.connector.Connector;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;
import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connection.StraightTwoEndedConnection;

/**
 *
 * @author Eurialo
 */
public class DrawingCanvas extends AbsolutePanel{

    private static final byte NODESIZE = 25;
    public DrawingCanvas(){
        super();
        this.setWidth("800px");
        this.setHeight("800px");

        // elements to connect
        Widget x = new Label("X");
        Widget y = new Label("Y");
        Widget z = new Label("Z");
        x.setStylePrimaryName("labelNode");
        x.setHeight(NODESIZE+"px");
        x.setWidth(NODESIZE+"px");

        y.setStylePrimaryName("labelEmpty");
        this.add(x, 100, 100);
        this.add(y, 200, 200);
        this.add(z, 300, 300);

        // gwt-diagrams stuff
        Connector c1 = UIObjectConnector.wrap(x);
        Connector c2 = UIObjectConnector.wrap(y);
        //Connector c3 = UIObjectConnector.wrap(z);

        Connection connection1 = new StraightTwoEndedConnection(c1, c2);
        //Connection connection2 = new BezierTwoEndedConnection(c2, c3);
        //Connection connection3 = new StraightTwoEndedConnection(c2, c3);

        connection1.appendTo(this);

    }
}
