/*Question No.(5)
 * Algorithm Explanation:
    1. Visual Representation:
    a. Nodes (servers/clients) are represented as circles with unique IDs.
    b. Edges (connections) are represented as lines with associated costs and bandwidths.

    2. Interactive Optimization:
    a. Users can add nodes and edges to the graph.
    b. The "Optimize Network" button will eventually implement algorithms like Kruskal's or Prim's to find a minimum spanning tree (MST) that minimizes total cost while ensuring connectivity.

    3. Dynamic Path Calculation:
    a. The "Calculate Shortest Path" button uses Dijkstra's algorithm to find the path with the maximum bandwidth between two nodes.

    4. Real-time Evaluation:
    a. The total cost and latency of the network are displayed in real-time.
    b. Users can adjust the topology and see the updated results.
 */

 /*
 * Problem Description:
 * The task is to design a GUI-based network optimization application that allows network 
 * administrators to create and visualize a network topology. The goal is to find an optimal 
 * network configuration that minimizes both the total cost and latency of data transmission 
 * while ensuring all nodes (servers/clients) are connected. 
 * 
 * The application should represent servers/clients as nodes in a graph and potential network 
 * connections as edges, with associated costs and bandwidths. The user should be able to 
 * interactively add nodes and edges, calculate the shortest path between nodes based on 
 * maximum bandwidth, and optimize the network topology for cost-efficiency and low latency.

 * Key Features:
 * 1. Visual Representation of the Network:
 *    - Nodes represent servers/clients (circles) with unique IDs.
 *    - Edges represent connections (lines) with associated costs and bandwidths.
 *    - Users can visualize the network and its connections interactively.
 *
 * 2. Interactive Optimization:
 *    - Users can add nodes and edges to the network graph.
 *    - The "Optimize Network" button will implement optimization algorithms (e.g., 
 *      Kruskal's or Prim's algorithm) to minimize total cost while ensuring all nodes 
 *      are connected.
 *
 * 3. Dynamic Path Calculation:
 *    - The "Calculate Shortest Path" button calculates the shortest path between two 
 *      nodes using Dijkstra's algorithm.
 *    - The algorithm considers bandwidth as a weight for the edges to find the 
 *      path with the highest bandwidth.
 *    - The shortest path is visualized on the GUI in red.
 *
 * 4. Real-Time Evaluation:
 *    - Real-time evaluation of the network's total cost and latency is displayed.
 *    - Users can adjust the network topology interactively and see updated results.
 *    - Latency is calculated inversely proportional to the bandwidth of the edge.

 * Data Structures:
 * 1. Node: Represents a server or client in the network with a unique identifier and 
 *    coordinates for visualization.
 * 2. Edge: Represents a connection between two nodes with associated cost and bandwidth.
 * 3. NetworkGraph: A collection of nodes and edges that defines the network. Includes methods 
 *    for adding nodes and edges, calculating the shortest path using Dijkstra's algorithm, 
 *    and calculating total network cost and latency.
 *
 * Algorithms:
 * 1. Dijkstra's Algorithm for Shortest Path Calculation:
 *    - Find the maximum bandwidth path between two nodes by updating the bandwidth 
 *      of neighboring nodes and selecting the node with the highest available bandwidth.
 *
 * 2. Network Optimization (Placeholder):
 *    - Implement algorithms like Kruskal's or Prim's to find the Minimum Spanning Tree 
 *      (MST) for minimizing the total cost of the network while maintaining connectivity.
 *
 * GUI Layout:
 * - A JPanel representing the network, where users can add nodes and edges interactively.
 * - Buttons for adding nodes, calculating the shortest path, and optimizing the network.
 * - A real-time display of the network's total cost and latency.

 * Approach:
 * 1. Start with an empty network and allow the user to add nodes and edges.
 * 2. Provide a mechanism for the user to input the start and end nodes to calculate 
 *    the shortest path based on bandwidth.
 * 3. Implement the "Optimize Network" button to trigger optimization algorithms for 
 *    cost minimization.
 * 4. Continuously update the display with the current network's cost, latency, and 
 *    shortest paths.

 * The following Java code implements the above algorithm using a graphical user interface 
 * (GUI) where users can interactively design a network topology, calculate paths, and optimize 
 * the network for cost and latency.
 */


 import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

class Node {
    String id; // Unique identifier for the node (server/client)
    int x, y; // Coordinates for visualization

    Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Node source, destination; // Source and destination nodes
    int cost; // Cost of the connection
    int bandwidth; // Bandwidth of the connection

    Edge(Node source, Node destination, int cost, int bandwidth) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

class NetworkGraph {
    ArrayList<Node> nodes = new ArrayList<>(); // List of nodes in the graph
    ArrayList<Edge> edges = new ArrayList<>(); // List of edges in the graph

    void addNode(Node node) {
        nodes.add(node); // Add a node to the graph
    }

    void addEdge(Edge edge) {
        edges.add(edge); // Add an edge to the graph
    }

    // Dijkstra's algorithm to find the shortest path based on bandwidth
    ArrayList<Node> findShortestPath(Node start, Node end) {
        Map<Node, Integer> bandwidths = new HashMap<>(); // Stores maximum bandwidth to each node
        Map<Node, Node> previous = new HashMap<>(); // Stores the previous node in the path
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> -bandwidths.get(node))); // Fixed syntax error

        for (Node node : nodes) {
            bandwidths.put(node, Integer.MIN_VALUE); // Initialize bandwidths to minimum
        }
        bandwidths.put(start, Integer.MAX_VALUE); // Start node has maximum bandwidth
        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the node with the highest bandwidth
            for (Edge edge : edges) {
                if (edge.source == current) {
                    Node neighbor = edge.destination;
                    int newBandwidth = Math.min(bandwidths.get(current), edge.bandwidth); // Calculate new bandwidth
                    if (newBandwidth > bandwidths.get(neighbor)) {
                        bandwidths.put(neighbor, newBandwidth); // Update bandwidth
                        previous.put(neighbor, current); // Update previous node
                        queue.add(neighbor); // Add neighbor to the queue
                    }
                }
            }
        }

        // Reconstruct the path
        ArrayList<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse to get the correct order
        return path;
    }

    // Calculate total cost of the network
    int calculateTotalCost() {
        int totalCost = 0;
        for (Edge edge : edges) {
            totalCost += edge.cost; // Sum up the costs of all edges
        }
        return totalCost;
    }

    // Calculate total latency of the network
    int calculateTotalLatency() {
        int totalLatency = 0;
        for (Edge edge : edges) {
            totalLatency += 1000 / edge.bandwidth; // Latency is inversely proportional to bandwidth
        }
        return totalLatency;
    }
}

public class NetworkOptimizationGUI extends JPanel implements ActionListener {
    NetworkGraph graph = new NetworkGraph(); // Create a network graph
    Node selectedNode = null; // Currently selected node for drawing edges
    ArrayList<Node> shortestPath = new ArrayList<>(); // Stores the shortest path

    public NetworkOptimizationGUI() {
        setPreferredSize(new Dimension(800, 600)); // Set panel size
        setBackground(Color.WHITE); // Set background color

        // Add buttons for user interaction
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(this);
        add(addNodeButton);

        JButton calculatePathButton = new JButton("Calculate Shortest Path");
        calculatePathButton.addActionListener(this);
        add(calculatePathButton);

        JButton optimizeButton = new JButton("Optimize Network");
        optimizeButton.addActionListener(this);
        add(optimizeButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the panel

        // Draw nodes
        for (Node node : graph.nodes) {
            g.setColor(Color.BLUE);
            g.fillOval(node.x - 10, node.y - 10, 20, 20); // Draw a circle for the node
            g.setColor(Color.BLACK);
            g.drawString(node.id, node.x - 10, node.y - 15); // Draw the node ID
        }

        // Draw edges
        for (Edge edge : graph.edges) {
            g.setColor(Color.BLACK);
            g.drawLine(edge.source.x, edge.source.y, edge.destination.x, edge.destination.y); // Draw a line for the edge
            g.drawString("C: " + edge.cost + ", B: " + edge.bandwidth,
                    (edge.source.x + edge.destination.x) / 2,
                    (edge.source.y + edge.destination.y) / 2); // Display cost and bandwidth
        }

        // Draw shortest path
        if (!shortestPath.isEmpty()) {
            g.setColor(Color.RED);
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                Node start = shortestPath.get(i);
                Node end = shortestPath.get(i + 1);
                g.drawLine(start.x, start.y, end.x, end.y); // Draw the shortest path in red
            }
        }

        // Display total cost and latency
        g.setColor(Color.BLACK);
        g.drawString("Total Cost: " + graph.calculateTotalCost(), 10, 20);
        g.drawString("Total Latency: " + graph.calculateTotalLatency(), 10, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Node")) {
            String id = JOptionPane.showInputDialog("Enter Node ID:"); // Get node ID from user
            if (id != null) {
                graph.addNode(new Node(id, 400, 300)); // Add a new node at the center
                repaint(); // Redraw the panel
            }
        } else if (e.getActionCommand().equals("Calculate Shortest Path")) {
            String startId = JOptionPane.showInputDialog("Enter Start Node ID:"); // Get start node ID
            String endId = JOptionPane.showInputDialog("Enter End Node ID:"); // Get end node ID
            if (startId != null && endId != null) {
                Node start = graph.nodes.stream().filter(n -> n.id.equals(startId)).findFirst().orElse(null);
                Node end = graph.nodes.stream().filter(n -> n.id.equals(endId)).findFirst().orElse(null);
                if (start != null && end != null) {
                    shortestPath = graph.findShortestPath(start, end); // Calculate shortest path
                    repaint(); // Redraw the panel
                }
            }
        } else if (e.getActionCommand().equals("Optimize Network")) {
            // Placeholder for optimization logic
            JOptionPane.showMessageDialog(this, "Network optimization logic will be implemented here.");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Network Optimization"); // Create the main window
        NetworkOptimizationGUI panel = new NetworkOptimizationGUI(); // Create the panel
        frame.add(panel); // Add the panel to the window
        frame.pack(); // Resize the window to fit the panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
        frame.setVisible(true); // Make the window visible
    }
}
/*
  
 
 */