/*
Algorithm: Minimum Rewards Distribution

1. **Initial Setup**:
   - First, we initialize an array `rewards` of the same length as `ratings`, where every employee gets at least one reward initially.
   
2. **First Pass (Left to Right)**:
   - Traverse the ratings array from left to right.
   - If the current employee's rating is higher than the previous employee's rating, increment their reward by 1 compared to the previous employee's reward. This ensures that employees with higher ratings receive more rewards than their adjacent colleagues on the left.

3. **Second Pass (Right to Left)**:
   - After the first pass, traverse the ratings array from right to left.
   - If the current employee's rating is higher than the next employee's rating, ensure that the current employee gets a reward higher than the next employee's reward. We do this by updating their reward to be the maximum of the current reward and the next employee’s reward + 1. This ensures the "greater than adjacent" rule is maintained even in the reverse direction.

4. **Summing the Rewards**:
   - Finally, we sum up all the rewards in the `rewards` array to get the total number of rewards required.

5. **Time Complexity**:
   - **O(n)**: The algorithm performs two linear passes over the ratings array, where `n` is the number of employees. Each pass takes O(n) time.

6. **Space Complexity**:
   - **O(n)**: An additional array `rewards` of size `n` is used to store the rewards for each employee.

7. **Edge Cases**:
   - If there is only one employee, the result is `1` reward, as every employee must receive at least one reward.
   - In case all ratings are the same, each employee would get one reward.

Example Explanation:
Example 1:
Input: ratings = [1, 0, 2]
Step-by-step:
- Initially, each employee gets one reward: [1, 1, 1]
- Left to right pass: [1, 1, 2] (Employee with rating 2 gets more than the previous one)
- Right to left pass: [2, 1, 2] (Employee with rating 1 remains with 1 reward, while the others remain correct)
- Final total rewards: 5

Example 2:
Input: ratings = [1, 2, 2]
Step-by-step:
- Initially: [1, 1, 1]
- Left to right pass: [1, 2, 1]
- Right to left pass: [1, 2, 1]
- Final total rewards: 4

The approach ensures that each employee gets at least one reward, and the rule of giving more rewards to employees with higher ratings than their adjacent colleagues is adhered to.

*/


import java.util.Arrays;

public class RewardDistribution2a { // Class name RewardDistribution 

    // Function to calculate the minimum number of rewards for each reward  
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        // If there is only one employee, they get one reward
        if (n == 1) return 1;

        // Creating  an array to store rewards for each employee in the array
        int[] rewards = new int[n];

        // Initializing rewards array with 1 reward for each employee  in the array
        Arrays.fill(rewards, 1);

        // First pass: Traverse from left to right  
        for (int i = 1; i < n; i++) {
            // If the current rating is greater than the previous one, increment the reward
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Second pass: Traverse from right to left 
        for (int i = n - 2; i >= 0; i--) {
            // If the current rating is greater than the next one, ensure a higher reward than the next employee
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Sum up the rewards to get the total number of rewards needed 
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;
    }

    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Minimum Rewards (Example 1): " + minRewards(ratings1)); // Output: 5

        // Example 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Minimum Rewards (Example 2): " + minRewards(ratings2)); // Output: 4
    }
}
//Output: 
//Minimum Rewards (Example 1): 5
//Minimum Rewards (Example 2): 4
