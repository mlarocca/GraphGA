/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import org.GCGA.client.GUI.GraphCreationPanel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import java.util.Collection;
import org.GCGA.client.utility.Utility;
import pl.balon.gwt.diagrams.client.connection.Connection;
import pl.balon.gwt.diagrams.client.connector.UIObjectConnector;

/**
 *
 * @author Marcello
 */
public class Vertex extends Label {
    private int index;

    private Label proxy = null;

    private AbsolutePanel parent = null;

    //private v parent;

    public Vertex(int i) {
        super(i+"");
        index = i;
        setWidth(Utility.ORIGINAL_VERTEX_SIZE+"px");
        setHeight(Utility.ORIGINAL_VERTEX_SIZE+"px");
        setStylePrimaryName("labelNode");
    }

    public void addToPanel(AbsolutePanel panel, int x, int y){
        if (proxy == null){
            proxy = new Label() ;
            DOM.setStyleAttribute(proxy.getElement(), "position", "absolute");
//            DOM.setStyleAttribute(proxy.getElement(), "zIndex", "100");
        }
        parent = panel;

        proxy.sinkEvents(Event.ONCLICK);
        proxy.setPixelSize(Utility.ORIGINAL_VERTEX_SIZE, Utility.ORIGINAL_VERTEX_SIZE);
        proxy.setStylePrimaryName("labelHidden");
        panel.add(proxy,x, y);
        panel.add(this,x,y);
    }

    public int getIndex(){
        return index;
    }

    public void setNewIndex(int newIndex){
        index = newIndex;
        this.setText(index+"");
    }

    public void select(){
        setStylePrimaryName("labelSelectedNode");
    }

    public void selectShift(){
        setStylePrimaryName("labelSelectedShiftNode");
    }

    public void deselect(){
        setStylePrimaryName("labelNode");
    }

    public int compare(Vertex w){
        return this.getIndex() - w.getIndex();
    }

    public void createEdge(Vertex w, AbsolutePanel panel){
        
/*          Connector c1 = w.createConnector();
            Connector c2 = this.createConnector();
*/
            Connection edge = new EdgeConnection(this, w);

            edge.appendTo(panel);
            edge.update();
    }

    public void removeEdge(Vertex w, AbsolutePanel panel){
        try{
            Collection cons = getIncidentsEdges();
            for (Object o : cons){
                try{
                    EdgeConnection c = (EdgeConnection) o;
                    if ( c.equals(this, w) )
                         c.remove();
                    //c.remove();
                    //alert("Removing " + c.toString() );
                }catch(ClassCastException e){
                    continue;
                }
            }
        }catch (NullPointerException e){

        }
    }

    protected UIObjectConnector createConnector() {
            return UIObjectConnector.wrap(proxy);
    }

    public Label getProxy(){
        return proxy;
    }

    public int getLeft() throws IllegalArgumentException{
        try{
            AbsolutePanel p = (AbsolutePanel)getParent();
            return p.getWidgetLeft(this);
        }catch(Exception e){
            throw new IllegalArgumentException();
        }
        //return this.getAbsoluteLeft()- getParent().getAbsoluteLeft();
    }

    public int getTop() throws IllegalArgumentException{
        try{
            AbsolutePanel p = (AbsolutePanel)getParent();
            return p.getWidgetTop(this);
        }catch(Exception e){
            throw  new IllegalArgumentException();
        }
        //return this.getAbsoluteTop() - getParent().getAbsoluteTop();
    }

    @Override
    public void onBrowserEvent(Event event){
        //alert(event.getTypeInt()+ " | " + DOM.eventGetType(event) + " <= " + Event.ONMOUSEOVER );

        if (event.getTypeInt() == Event.ONCLICK || event.getTypeInt() == Event.ONMOUSEDOWN ){
            //alert(""+ (event.getTypeInt() == Event.ONMOUSEDOWN));
            if (  DOM.eventGetAltKey(event) ){
                GraphCreationPanel.selectEdge(this);
            }else{
                GraphCreationPanel.deselectEdge();
                super.onBrowserEvent(event);
            }
        }
        else
            super.onBrowserEvent(event);

    }

/*
    private static native void alert(String message)/*-{
		alert(message);
    }-* /;
 * 
 */

    private Collection getIncidentsEdges() throws NullPointerException{
        UIObjectConnector wrapper = UIObjectConnector.getWrapper(this.getProxy());
        if (wrapper!=null){
            return wrapper.getConnections();
        }else{
            throw new NullPointerException();
        }
    }

    public void removeVertex(){
        //alert("Starting to remove");
        try{
            Collection cons = getIncidentsEdges();
            for (Object o : cons){
                try{
                    Connection c = (Connection) o;
                    c.remove();
                }catch(ClassCastException e){
                    continue;
                }
            }
        }catch (NullPointerException e){
            
        }

        if (parent!=null){
            //alert("Parent != null");
            parent.remove(proxy);
            proxy = null; //So that can be deleted
            parent.remove(this);
        }else{
            parent = (AbsolutePanel)this.getParent();
            //alert("Parent ? " + (parent!=null) );
            parent.remove(proxy);
            proxy = null; //So that can be deleted
            parent.remove(this);
        }
    }


}
