package org.kclhi.hands.utility;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
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
    private String graphLayout;
    private ArrayList<StringVertex> currentHiderVertices;
    private ArrayList<StringVertex> currentSeekerVertices;

    public GraphPanel() {
    }

    public void setGraphData(ArrayList<StringVertex> vertices, Set<StringEdge> edges, String graphLayout) {
        this.vertices = vertices;
        this.edges = edges;
        assignPositions(graphLayout);
    }

    public void setHiderSeekerData(ArrayList<StringVertex> hiderVertices, ArrayList<StringVertex> seekerVertices) {
        currentHiderVertices = hiderVertices;
        currentSeekerVertices = seekerVertices;
        
        // System.out.println("Hider vertices: " + currentHiderVertices);
        // System.out.println("Seeker vertices: " + currentSeekerVertices);
    }

    public void drawGraph() {
        repaint();
    }

    public void setGraphLayout(String graphLayout) {
        this.graphLayout = graphLayout;
        assignPositions(graphLayout);
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

    private void adjustVertexPositions() {
        int padding = 30; // Padding size
        Map<StringVertex, Point> adjustedPositions = new HashMap<>();
    
        for (Map.Entry<StringVertex, Point> entry : vertexPositions.entrySet()) {
            StringVertex vertex = entry.getKey();
            Point position = entry.getValue();
    
            // Adjust the position
            int x = Math.max(padding, Math.min(getWidth() - padding, position.x));
            int y = Math.max(padding, Math.min(getHeight() - padding, position.y));
    
            adjustedPositions.put(vertex, new Point(x, y));
        }
    
        vertexPositions = adjustedPositions;
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Adjust the vertex positions
        adjustVertexPositions();
    
        // Draw edges first so nodes appear on top
        g2.setColor(Color.BLACK);
        for (StringEdge edge : edges) {
            Point sourcePosition = vertexPositions.get(edge.getSource());
            Point targetPosition = vertexPositions.get(edge.getTarget());
            g2.drawLine(sourcePosition.x, sourcePosition.y, targetPosition.x, targetPosition.y);
        }
    
        // Draw vertices and their outlines
        for (StringVertex vertex : vertices) {
            Point p = vertexPositions.get(vertex);
            g2.setColor(Color.GRAY);
            g2.fillOval(p.x - 10, p.y - 10, 20, 20);
    
            g2.setStroke(new BasicStroke(4)); // Thicker stroke for visibility
            if (currentHiderVertices.contains(vertex)) {
                g2.setColor(Color.BLUE);
                g2.drawOval(p.x - 13, p.y - 13, 26, 26);
            }
            if (currentSeekerVertices.contains(vertex)) {
                g2.setColor(Color.ORANGE);
                g2.drawOval(p.x - 16, p.y - 16, 32, 32);
            }
        }
            // Draw labels for hider and seeker vertices
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("default", Font.BOLD, 16));
        for (StringVertex vertex : currentHiderVertices) {
            Point p = vertexPositions.get(vertex);
            g2.drawString(vertex.toString(), p.x - 20, p.y + 5);
        }
        for (StringVertex vertex : currentSeekerVertices) {
            Point p = vertexPositions.get(vertex);
            g2.drawString(vertex.toString(), p.x - 20, p.y + 5);
        }
    }
}
