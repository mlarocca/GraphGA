/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

/**
 *
 * @author Owner
 */
public interface GraphType {

    static final short COMPLETE_GRAPH = 0;
    static final short COMPLETE_BIPARTITE_GRAPH = 1;
    static final short SQUARE_GRID_GRAPH = 2;
    static final short TRIANGULAR_MESH_GRAPH = 3;

    static final short CUSTOM_GRAPH = 100;

    static final short ERROR = -1;
    static final short ALL = -2;
    static final short ALL_VALIDS = -3;
}
