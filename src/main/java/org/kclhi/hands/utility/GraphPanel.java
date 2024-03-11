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

    public void setGraphData(ArrayList<StringVertex> vertices, Set<StringEdge> edges, String graphLayout) {
        this.vertices = vertices;
        this.edges = edges;
        assignPositions(graphLayout);
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
    }

    private void assignPositions(String graphLayout) {
        if (graphLayout.equals("Circular")) {
            assignPositionsCircular();
        } else if (graphLayout.equals("Force Directed")) {
            assignPositionsDirected();
        }
    }

    private void assignPositionsDirected() {
        final double repulsion = 5000; // Repulsion constant
        final double attraction = 0.1; // Attraction constant
        final int padding = 20;
        Map<StringVertex, Point> newPositions = new HashMap<>();
    
        // Initialize random positions if not already set
        vertices.forEach(v -> vertexPositions.putIfAbsent(v, new Point(padding + (int)(Math.random() * (getWidth() - 2 * padding)), padding + (int)(Math.random() * (getHeight() - 2 * padding)))));
    
        // Apply forces to each vertex
        for (StringVertex v1 : vertices) {
            double fx = 0, fy = 0;
            for (StringVertex v2 : vertices) {
                if (v1 != v2) {
                    Point p1 = vertexPositions.get(v1), p2 = vertexPositions.get(v2);
                    double dx = p1.x - p2.x, dy = p1.y - p2.y;
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double force = repulsion / distance;
                    fx += force * dx / distance;
                    fy += force * dy / distance;
                }
            }
    
            // Attraction to connected vertices
            for (StringEdge edge : edges) {
                if (edge.getSource().equals(v1) || edge.getTarget().equals(v1)) {
                    StringVertex other = edge.getSource().equals(v1) ? edge.getTarget() : edge.getSource();
                    Point p1 = vertexPositions.get(v1), pOther = vertexPositions.get(other);
                    double dx = p1.x - pOther.x, dy = p1.y - pOther.y;
                    fx -= dx * attraction;
                    fy -= dy * attraction;
                }
            }
    
            // Update position
            Point p = vertexPositions.get(v1);
            int newX = (int) Math.min(getWidth() - padding, Math.max(padding, p.x + fx));
            int newY = (int) Math.min(getHeight() - padding, Math.max(padding, p.y + fy));
            newPositions.put(v1, new Point(newX, newY));
            }
    
        vertexPositions = newPositions;
    }

    private void assignPositionsCircular() {
        double radius = Math.min(getHeight(), getWidth()) / 2.2; // Radius of the circle
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
