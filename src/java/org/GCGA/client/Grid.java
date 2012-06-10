/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

import org.GCGA.client.utility.Point;
import org.GCGA.client.utility.Utility;
import org.GCGA.client.GUI.GraphPanel;

/**
 *
 * @author Owner
 */
public class Grid {

    private int sideSize;
    private int gridSize;
    
    private int vertexSize = Utility.ORIGINAL_VERTEX_SIZE;
    private int delta;

    private int drawingSize;

    /**
     * This version of the constructor assumes the grid will be 4*n^2 in dimension
     * @param n - Number of vertex in the graph
     */
/*   public Grid(int n) {
        //Viene usata una griglia di dimensione proporzionale al quadrato del numero dei vertici
        gridSize = (sideSize = 2 * n) * sideSize;
        emptyLabels = new Widget[sideSize][sideSize];
        vertexlabels = new Label[sideSize];
        for (int i=0; i<sideSize; i++){
            for (int j=0; j<sideSize; j++)
                emptyLabels[i][j] = null;
            vertexlabels[i]=null;
        }
        vertexlabels = new Label[sideSize];
   //     this.clearLabels();
    }
*/
    /**
     * In this version of the constructor the desired sidesize is specified
     * @param sSize - desired side size for the grid
     * @param multiplier - side size multiplicator factor: the final sideSize will be equal to sSize * multiplier
     */
    public Grid(int sSize, float multiplier) {
        //Viene usata una griglia di dimensione proporzionale al quadrato del numero dei vertici
        if ( multiplier <=0 )
            throw new IllegalArgumentException("Invalid multiplier parameter: must be positive!");
        sSize = Math.round(sSize * multiplier);
        gridSize = (sideSize = sSize) * sideSize;
    }

    public void setGrid(int d){
        int w = d;
        int h = d;

        drawingSize = Math.min(w,h);

        delta = (int)Math.floor( (drawingSize - vertexSize)/(sideSize-1) );
        vertexSize = Utility.ORIGINAL_VERTEX_SIZE;

        if ( delta < vertexSize ){
            do{
                vertexSize /= 2;
            } while (delta < vertexSize );
            //Ricalcola delta in base alla nuova dimensione
            delta = (int)Math.floor( (drawingSize - vertexSize)/(sideSize-1) );
        }

    }

    private boolean validPosition(Integer gridPosition){
        if ( gridPosition >= gridSize){
            return false;
        }
        else
            return true;
    }

    public GridCoordinates gridCoordinates(Integer gridPosition) throws IndexOutOfBoundsException{
        if ( !this.validPosition(gridPosition)  ){
            throw new IndexOutOfBoundsException();
        }
        return new GridCoordinates((int)Math.floor(gridPosition/sideSize), (int)(gridPosition % sideSize) );
    }

    public int gridPosition(GridCoordinates c ) throws IndexOutOfBoundsException{
        int tmp = c.row() * sideSize + c.column();
        if ( ! this.validPosition(tmp) ){
            throw new IndexOutOfBoundsException("Coordinate non traducibili in una posizione nella griglia");
        }
        return tmp;
    }

    public int gridSize(){
        return gridSize;
    }

    public int sideSize(){
        return sideSize;
    }

    public Point cartesianCoordinates(Integer gridPosition){
        if ( !this.validPosition(gridPosition)  ){
            throw new IndexOutOfBoundsException();
        }

        GridCoordinates gc = this.gridCoordinates(gridPosition);

        //considera il vertice situato al centro
        return new Point(gc.column()*delta + vertexSize/2 , gc.row()*delta + vertexSize/2);

    }

    public void drawGrid(GraphPanel gP){

        gP.clearGrid();

        for (int i=0; i< sideSize; i++){
            gP.drawDashedLine(i*delta + vertexSize/2, 0, i*delta+vertexSize/2, gP.getHeight(), "rgba(125,125,125,0.15)");
            gP.drawDashedLine(0, i*delta + vertexSize/2, gP.getWidth() , i*delta+vertexSize/2, "rgba(125,125,125,0.15)");
        }
    }


    public void paintVertex(GraphPanel gP, int vertexIndex, int gridPos) throws IndexOutOfBoundsException{

        GridCoordinates gc = this.gridCoordinates(gridPos);

        gP.drawVertex(vertexIndex, gc.column()*delta , gc.row()*delta );

    }

    public void paintEdge(GraphPanel gP, int v, int w) throws IndexOutOfBoundsException{
        gP.drawEdge(v, w);
    }

    public int getVertexSize(){
        return vertexSize;
    }
}
