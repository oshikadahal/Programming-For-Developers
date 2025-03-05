/*
Algorithm: Find the Kth Smallest Product in Two Sorted Arrays

1. Initialize a Min-Heap (Priority Queue) to store the product values and their corresponding indices from both arrays (returns1 and returns2).
   - The heap will help us efficiently extract the smallest products while processing.
   
2. Insert Initial Elements into the Min-Heap:
   - For each element in `returns1`, calculate the product of that element with the first element of `returns2`.
   - Store these products along with the corresponding indices (returns1 index, returns2 index) in the heap.

3. Process the Heap to Find the Kth Smallest Product:
   - Extract the smallest product from the heap `k` times.
   - After each extraction, store the current smallest product as the result.
   - If the current element's index in `returns2` (let's say `j`) is not the last index, calculate the next product with the same element in `returns1` and the next element in `returns2` (i.e., `returns2[j + 1]`) and push it into the heap.
   
4. After `k` extractions, the result will be the kth smallest product.

Time Complexity: O(k log min(m, n))
- k: Number of extractions (corresponding to the kth smallest product)
- m, n: Lengths of returns1 and returns2
- Heap operations (insert and remove) take O(log min(m, n)) time, which makes the algorithm efficient.

Space Complexity: O(min(m, n))
- The heap stores at most `min(m, n)` elements at any point in time, as each element in `returns1` can only pair with one element from `returns2` in the heap at a time.
*/



import java.util.PriorityQueue;

public class KthSmallestProduct1b { 
    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        // Min-Heap (Priority Queue) to store product values along with indices from returns1 and returns2.
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> 
            Integer.compare(a[0], b[0]) // Sorting by product value (smallest first)  
        );

        // Initializing the heap with the first element from returns1 and first element from returns2
        for (int i = 0; i < returns1.length; i++) {
            // Store {product, index of returns1, index of returns2}
            minHeap.offer(new int[]{returns1[i] * returns2[0], i, 0});
        }

        int result = 0; // Variable to store the kth smallest product

        // Process k smallest elements from the heap
        while (k-- > 0) {
            int[] top = minHeap.poll(); // Extract the smallest product from the heap
            result = top[0]; // Store the current smallest product as the result
            int i = top[1]; // Index from returns1
            int j = top[2]; // Index from returns2

            // If there is a next element in returns2, add the new product to the heap
            if (j + 1 < returns2.length) {
                minHeap.offer(new int[]{returns1[i] * returns2[j + 1], i, j + 1});
            }
        }

        return result; // Returning  the kth smallest product after k extraction
    }

    public static void main(String[] args) {
        // Example test case 1 for the following example:
        int[] returns1 = {-4, -2, 0, 3}; // First sorted array of returns for the first product in the list 
        int[] returns2 = {2, 4}; // Second sorted array of returns for the second product in the list
        int k = 6; // Target kth smallest product 

        // Calling  the function  and printing  the result
        System.out.println("The " + k + "th smallest investment return is: " + 
                            findKthSmallestProduct(returns1, returns2, k));
    }
}
/*
Example 1:

Input: 
returns1 = [2,5]
returns2 = [3,4]
k = 2

Heap Processing:
1st smallest product: 2 × 3 = 6
2nd smallest product: 2 × 4 = 8

Output:
The 2nd smallest investment return is: 8

Example 2:

Input: 
returns1 = [-4,-2,0,3]
returns2 = [2,4]
k = 6

Heap Processing:
1st smallest product: -4 × 4 = -16
2nd smallest product: -4 × 2 = -8
3rd smallest product: -2 × 4 = -8
4th smallest product: -2 × 2 = -4
5th smallest product:  0 × 2 = 0
6th smallest product:  0 × 4 = 0

Output:
The 6th smallest investment return is: 0
*/

