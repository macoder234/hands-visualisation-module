package org.kclhi.hands.utility;

import java.util.ArrayList;
import java.util.Set;

import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

public interface GraphReadyCallback {
    void onGraphReady(ArrayList<StringVertex> vertices, Set<StringEdge> edges);

    void onHiderSeekerReady(ArrayList<StringVertex> hiderVertices, ArrayList<StringVertex> seekerVertices);
}
