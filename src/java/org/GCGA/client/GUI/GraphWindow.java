/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.GUI;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.events.RestoreClickEvent;
import com.smartgwt.client.widgets.events.RestoreClickHandler;
import org.GCGA.client.GraphEmbedding;
import org.GCGA.client.Grid;
import org.GCGA.client.utility.Utility;

/**
 *
 * @author Marcello
 */
public class GraphWindow extends Window{

    private GraphWindow thisWindow = this;

    private GraphPanel gp;
    private Grid grid;

    private static final short ANIMATE_TIME = 500;

    public GraphWindow(GraphEmbedding ge) {
        super();

        gp = new GraphPanel();

        gp.setGrid(grid = new Grid( ge.getGridSideSize(), 1 ), ge.getVerticesNumber(), Utility.DEFAULT_WINDOW_WIDTH);

        this.setTitle("Drawing embedding for a " + ge.getGraphTypeLabel() + " with " + ge.getVerticesNumber() + " vertices" );
        this.setShowMinimizeButton(true);
        //this.setModalMaskOpacity(70);
        this.setOpacity(70);
        this.setCanDragResize(false);
        //this.setCanDragReposition(false);
        //this.setIsModal(true);
        //this.setShowModalMask(true);

        this.setAnimateTime(ANIMATE_TIME);
        this.setAnimateMinimize(true);

        this.addCloseClickHandler(new CloseClickHandler() {
            public void onCloseClick(CloseClientEvent event) {
                //buttonTouchThis.setTitle("Touch This");
                thisWindow.destroy();
            }
        });

        this.addMinimizeClickHandler(new MinimizeClickHandler() {

            public void onMinimizeClick(MinimizeClickEvent event) {
               event.cancel();
               thisWindow.minimize();
               thisWindow.animateMove(0, DrawingTabSet.get().getBottom() );
            }
        } );

        this.addRestoreClickHandler( new RestoreClickHandler() {

            public void onRestoreClick(RestoreClickEvent event) {
                event.cancel();
                thisWindow.animateMove(0, 0);
                thisWindow.restore();
                //thisWindow.centerInPage();
            }
        });
        
        this.setWidth(Utility.DEFAULT_WINDOW_WIDTH + Utility.W_MARGIN);
        this.setHeight(Utility.DEFAULT_WINDOW_HEIGHT + Utility.H_MARGIN);

        //this.setHoverWidth(Utility.HOVER_WIDTH);
        //this.setPrompt("<b>Drawing window tip</b><p>You'll have to close the window in order to access the rest of the website.");
        
        Canvas wrapper = new Canvas();
        wrapper.setWidth(gp.getSize());
        wrapper.setHeight(gp.getSize());
        wrapper.setMargin(0);
        wrapper.addChild(gp);
        wrapper.setTop(0);
        wrapper.setLeft(0);
        this.addItem(wrapper);

        this.show();
        this.centerInPage();
        
        grid.drawGrid(gp);
        ge.drawGraph(gp, grid);

    }
}
