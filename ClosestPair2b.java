//  Creating a Function to find the lexicographically smallest pair of points with the smallest Manhattan distance
// Approach:
// 1. Initialize the minimum distance (`minDistance`) to a large value (Integer.MAX_VALUE).
// 2. Create an array `result` to store the indices of the closest pair of points.
// 3. Use two nested loops to iterate through all pairs of points. 
//    - The outer loop (i) traverses all points, and the inner loop (j) compares the current point (i) to all subsequent points (j > i).
// 4. For each pair (i, j), compute the Manhattan distance: 
//    - distance(i, j) = |x_coords[i] - x_coords[j]| + |y_coords[i] - y_coords[j]|
// 5. If the current distance is smaller than the `minDistance`, update the `minDistance` and store the current pair of indices (i, j).
// 6. If the distance is equal to the minimum, perform a lexicographical comparison of the indices to ensure the smallest pair is selected:
//    - A pair (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or (i1 == i2 and j1 < j2).
// 7. After iterating through all pairs, return the indices of the closest pair.
//
// Example:
// Input: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3]
// Output: [0, 3]
// Explanation: The pair (0, 3) has the smallest Manhattan distance of 1, which is the smallest possible distance.





public class ClosestPair2b { //the smallest possible distance between two pairs 

    // Function to find the lexicographically smallest pair of points with the smallest distance
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE; // Initializing the minimum distance to a large value to avoid overflow issues 
        int[] result = new int[2]; // Array to store the result indices

        // Iterate through all pairs of points in the array and calculating the distance between them.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculating  the Manhattan distance between points (i, j)
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Checking  if the current distance is smaller than the minimum distance found so far in the distance array.
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance; // Update the minimum distance
                    result[0] = i; // Update the first index of the result
                    result[1] = j; // Update the second index of the result
                }
            }
        }

        return result; // Return the result indices of the result array 
    }

    public static void main(String[] args) {
        // Example input parameters ;
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Finding the closest pair of points to the point in the region 
        int[] result = findClosestPair(x_coords, y_coords);

        // Printing the result
        System.out.println("The indices of the closest pair of points are: [" + result[0] + ", " + result[1] + "]");
    }
}
//Output:
//Closest pair: [0, 3]

