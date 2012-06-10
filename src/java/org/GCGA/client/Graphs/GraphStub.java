/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client.Graphs;

import org.GCGA.client.GraphType;

/**
 *
 * @author Eurialo
 */
public abstract class GraphStub implements Graph {

    public Integer edgesNumber() {
        return 0;
    }

    public Integer verticesNumber() {
        return 0;
    }

    public short getType() {
        return GraphType.ERROR;
    }
}
