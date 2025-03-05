/*
 * Problem: You have a network of 'n' devices. Each device can either install a communication module at a given cost 
 * or communicate with other devices through direct connections. The goal is to minimize the total cost of connecting 
 * all devices.
 *
 * The approach used to solve this problem is based on Kruskal's Algorithm for Minimum Spanning Tree (MST) 
 * and the Union-Find (Disjoint-Set) data structure.
 *
 * Key Steps:
 * 1. Treat the problem as a graph where:
 *    - Devices are nodes.
 *    - Communication modules are treated as "virtual" edges that connect the devices to a "super node" (node 0).
 *    - Direct connections between devices are real edges.
 *
 * 2. Add virtual edges for each device:
 *    - Each device has a virtual edge connecting it to the super node (0) with the cost of the communication module.
 *
 * 3. Add edges for the given direct connections between devices, where each connection has a specified cost.
 *
 * 4. Sort all edges (both virtual and real) by their cost to ensure we process the smallest edges first, as Kruskal's 
 *    algorithm requires edges to be processed in increasing order of cost.
 *
 * 5. Use Union-Find data structure to detect cycles while selecting edges. This ensures that we only include edges that 
 *    connect disconnected components, forming a tree (MST).
 *
 * 6. Iterate over the sorted edges, and keep adding the smallest valid edges (those connecting two previously unconnected 
 *    components) to the result. Continue until all devices are connected.
 *
 * 7. Return the total cost of the selected edges, which is the minimum cost to connect all devices.
 *
 * Algorithm Complexity:
 * - Time Complexity: O(E log E) where E is the number of edges (due to sorting). The Union-Find operations (find and union) 
 *   take nearly constant time due to path compression and union by rank.
 * - Space Complexity: O(n) for storing the parent and rank arrays in the Union-Find structure, where n is the number of devices.
 *
 * Notes:
 * - The Union-Find (Disjoint-Set) data structure is used for efficiently finding and merging connected components.
 * - We treat module installation as "virtual edges" that connect each device to a hypothetical super node (node 0).
 * - Kruskal's algorithm ensures that we choose the minimum-cost edges first, building the minimum spanning tree (MST).
 *
 * Example:
 * Input:
 *   n = 3
 *   modules = [1, 2, 2]
 *   connections = [[1, 2, 1], [2, 3, 1]]
 *
 * Output: The minimum cost to connect all devices is 3
 * Explanation: Install the module on device 1 (cost = 1) and connect devices 2 and 3 to device 1 (cost = 2).
 *
 */




import java.util.*;

public class NetworkConnection3a {

    // Union-Find Data Structure for Kruskal's Algorithm
    static class UnionFind {
        int[] parent, rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;  // Initialize each node as its own parent
                rank[i] = 0;    // Initialize rank to 0
            }
        }

        // Find the root of the set containing x, with path compression
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Path compression
            }
            return parent[x];
        }

        // Union of two sets
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                // Union by rank to keep tree shallow
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    // Method to find the minimum cost to connect all devices
    public static int minCost(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>();

        // Add edges for installing communication modules (virtual edges)
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]});  // [0, i+1, cost] is a virtual edge connecting to "super node"
        }

        // Add edges for the given direct connections
        for (int[] connection : connections) {
            edges.add(new int[]{connection[0], connection[1], connection[2]});
        }

        // Sort the edges by their cost
        edges.sort(Comparator.comparingInt(a -> a[2]));

        // Initialize Union-Find
        UnionFind uf = new UnionFind(n + 1); // n + 1 because we have a "super node"

        int totalCost = 0;
        int edgesUsed = 0;

        // Iterate over the sorted edges and build the MST
        for (int[] edge : edges) {
            int device1 = edge[0];
            int device2 = edge[1];
            int cost = edge[2];

            // If the two devices are in different components, union them
            if (uf.find(device1) != uf.find(device2)) {
                uf.union(device1, device2);
                totalCost += cost;
                edgesUsed++;

                // If we've used n edges (which means we have connected all devices), stop
                if (edgesUsed == n) {
                    break;
                }
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        // Example input
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {
            {1, 2, 1},
            {2, 3, 1}
        };

        // Call the minCost method
        int result = minCost(n, modules, connections);

        // Output the result
        System.out.println("The minimum cost to connect all devices is: " + result);
    }
}

/*Output :The minimum cost to connect all devices is: 3 */

