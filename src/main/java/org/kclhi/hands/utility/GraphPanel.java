package org.kclhi.hands.utility;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.kclhi.hands.graph.HiddenObjectGraph;
import org.kclhi.hands.graph.StringEdge;
import org.kclhi.hands.graph.StringVertex;

public class GraphPanel extends JPanel {
    private ArrayList<StringVertex> vertices;
    private Set<StringEdge> edges;
    private Map<StringVertex, Point> vertexPositions = new HashMap<>();

    public GraphPanel() {
    }

    public void setGraphData(ArrayList<StringVertex> vertices, Set<StringEdge> edges) {
        this.vertices = vertices;
        this.edges = edges;
        assignVertexPositions();
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
    }


    private void assignVertexPositions() {
        double radius = getHeight() / 2.5; // Radius of the circle
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int n = vertices.size();
        int i = 0;

        for (StringVertex vertex : vertices) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            vertexPositions.put(vertex, new Point(x, y));
            i++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw vertices
        for (StringVertex vertex : vertices) {
            Point p = vertexPositions.get(vertex);
            g.fillOval(p.x - 10, p.y - 10, 20, 20);
        }
        // Draw edges
        for (StringEdge edge : edges) {
            Point sourcePosition = vertexPositions.get(edge.getSource());
            Point targetPosition = vertexPositions.get(edge.getTarget());
            g.drawLine(sourcePosition.x, sourcePosition.y, targetPosition.x, targetPosition.y);
        }
    }
}
