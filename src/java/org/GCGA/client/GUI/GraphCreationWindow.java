package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import org.GCGA.client.Graphs.CustomGraph;
import org.GCGA.client.utility.Utility;

public class GraphCreationWindow extends Window {
    
    private static GraphCreationWindow thisWindow = null;

    private static GraphCreationPanel gcp = null;
    
    private GraphCreationWindow(int innerWidth, int innerHeight) {
        super();

        this.setTitle("Graph Creation Tool" );
        this.setShowMinimizeButton(false);
        this.setModalMaskOpacity(70);
        this.setOpacity(75);
        this.setCanDragResize(false);
        this.setCanDragReposition(false);
        this.setIsModal(true);
        this.setShowModalMask(true);

        this.addCloseClickHandler(new CloseClickHandler() {
            public void onCloseClick(CloseClientEvent event) {
                //buttonTouchThis.setTitle("Touch This");
                thisWindow.removeItem(gcp);
                thisWindow.destroy();
                thisWindow = null;
            }
        });
        this.setWidth(innerWidth + Utility.W_MARGIN );
        this.setHeight(innerHeight + Utility.H_MARGIN);

        if (gcp==null){
            GraphCreationPanel.init(innerWidth, innerHeight); 
        }
        gcp = GraphCreationPanel.get();
        this.addItem(gcp);

        this.show();
        this.centerInPage();
    }

    public static void init(int innerWidth, int innerHeight){
        if (thisWindow == null){
            thisWindow = new GraphCreationWindow(innerWidth, innerHeight);
        }
    }

    public static GraphCreationWindow get(){

        if (thisWindow == null){
            GraphCreationWindow.init(Utility.DEFAULT_WINDOW_WIDTH, Utility.DEFAULT_WINDOW_HEIGHT);
        }
        return thisWindow;

    }

    public static CustomGraph getGraph(){
        return GraphCreationPanel.getGraph();
    }

    public static void showWindow(){
        get();
    }
}
