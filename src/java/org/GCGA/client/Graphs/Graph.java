/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.Graphs;

import org.GCGA.client.utility.Pair;

/**
 *
 * @author Owner
 */
public interface Graph {

    public Pair<Integer, Integer> getEdgeVertices(int edgeNbr) throws ArrayIndexOutOfBoundsException;
    public Integer edgesNumber();

    public Integer verticesNumber();

    public short getType();

}