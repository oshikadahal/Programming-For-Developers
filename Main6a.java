/*
Algorithm Description:

1. **Initialization**:
   - A class `NumberPrinter` is created to handle the printing of `0`, even numbers, and odd numbers.
   - A `ThreadController` class is responsible for managing the three threads and synchronizing their execution.
   - Semaphores (`zeroSemaphore`, `evenSemaphore`, `oddSemaphore`) are used to synchronize the printing order between the threads.

2. **Main Class**:
   - In the `main` method, an instance of `NumberPrinter` is created to handle the printing.
   - The `ThreadController` is initialized with a specified number `n` and the `NumberPrinter` object.

3. **Thread Execution**:
   - Three threads are created:
     - `zeroThread` which prints "0".
     - `evenThread` which prints even numbers.
     - `oddThread` which prints odd numbers.
   - The threads are started to execute in parallel.

4. **Synchronization**:
   - The `zeroThread` starts first, prints "0", and releases either the `evenSemaphore` or `oddSemaphore` based on the current iteration (odd or even).
   - The `evenThread` waits for the `evenSemaphore` to acquire, prints an even number, and then releases the `zeroSemaphore` to allow the next zero to be printed.
   - Similarly, the `oddThread` waits for the `oddSemaphore` to acquire, prints an odd number, and then releases the `zeroSemaphore` to print the next "0".

5. **Termination**:
   - Each thread is joined to ensure that the main thread waits for all threads to finish executing.
   - The final output sequence will be correctly interleaved in the format: `01020304...` based on the number `n`.

6. **Concurrency Control**:
   - The semaphores ensure that only one thread prints at a time in the correct order.
   - This prevents race conditions and ensures the sequence is printed in the desired order.
*/









import java.util.concurrent.Semaphore; // Importing the Semaphore class to control the execution order of threads

class NumberPrinter {
    // Method to print zero (for every element)
    public void printZero() {
        System.out.print("0"); // Printing the value "0"
    }
    
    // Method to print even values
    public void printEven(int num) {
        System.out.print(num); // Printing the even number passed to this method
    }
    
    // Method to print odd values
    public void printOdd(int num) {
        System.out.print(num); // Printing the odd number passed to this method
    }
}

class ThreadController {
    private int n; // Variable to store the limit of the sequence (up to which the sequence will be printed)
    private NumberPrinter printer; // Reference to the NumberPrinter object to call printing methods
    private Semaphore zeroSemaphore = new Semaphore(1); // Semaphore to control the zero printing thread (initially allows one thread)
    private Semaphore evenSemaphore = new Semaphore(0); // Semaphore to control the even printing thread (initially blocks execution)
    private Semaphore oddSemaphore = new Semaphore(0); // Semaphore to control the odd printing thread (initially blocks execution)

    // Constructor to initialize the sequence limit (n) and the printer object
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n; // Setting the limit of the sequence
        this.printer = printer; // Initializing the printer object
    }

    // Method for the zero thread to print 0 and trigger the next thread (even or odd)
    public void zero() {
        try {
            for (int i = 1; i <= n; i++) { // Loop to print "0" for every element until n
                zeroSemaphore.acquire(); // Acquiring the semaphore to ensure synchronization
                printer.printZero(); // Printing the number "0"
                
                // Decide whether to release the even or odd semaphore based on the current index
                if (i % 2 == 0) {
                    evenSemaphore.release(); // If i is even, release the even thread
                } else {
                    oddSemaphore.release(); // If i is odd, release the odd thread
                }
            }
        } catch (InterruptedException e) { // Catch any interrupted exception that may occur
            Thread.currentThread().interrupt(); // Properly handling thread interruption
        }
    }

    // Method for the even thread to print even numbers
    public void even() {
        try {
            for (int i = 2; i <= n; i += 2) { // Loop to print even numbers (2, 4, 6, ...)
                evenSemaphore.acquire(); // Acquiring the semaphore to ensure synchronization
                printer.printEven(i); // Printing the even number
                zeroSemaphore.release(); // Releasing the zero thread to print "0" again
            }
        } catch (InterruptedException e) { // Catch any interrupted exception that may occur
            Thread.currentThread().interrupt(); // Properly handling thread interruption
        }
    }

    // Method for the odd thread to print odd numbers
    public void odd() {
        try {
            for (int i = 1; i <= n; i += 2) { // Loop to print odd numbers (1, 3, 5, ...)
                oddSemaphore.acquire(); // Acquiring the semaphore to ensure synchronization
                printer.printOdd(i); // Printing the odd number
                zeroSemaphore.release(); // Releasing the zero thread to print "0" again
            }
        } catch (InterruptedException e) { // Catch any interrupted exception that may occur
            Thread.currentThread().interrupt(); // Properly handling thread interruption
        }
    }
}

public class Main6a {
    // Main method where the program starts execution
    public static void main(String[] args) {
        int n = 5; // Defining the sequence limit (n = 5)
        NumberPrinter printer = new NumberPrinter(); // Creating an instance of the NumberPrinter class
        ThreadController controller = new ThreadController(n, printer); // Creating the controller with the printer and limit n
        
        // Creating threads for zero, even, and odd number printing
        Thread zeroThread = new Thread(controller::zero); // Thread to print zeroes
        Thread evenThread = new Thread(controller::even); // Thread to print even numbers
        Thread oddThread = new Thread(controller::odd); // Thread to print odd numbers
        
        // Starting the threads
        zeroThread.start(); // Start the zero printing thread
        evenThread.start(); // Start the even printing thread
        oddThread.start(); // Start the odd printing thread
        
        try {
            // Joining the threads to ensure the main thread waits for the completion of all threads
            zeroThread.join(); // Waiting for the zero thread to finish
            evenThread.join(); // Waiting for the even thread to finish
            oddThread.join(); // Waiting for the odd thread to finish
        } catch (InterruptedException e) { // Catch any interrupted exception that may occur
            Thread.currentThread().interrupt(); // Properly handling thread interruption
        }
    }
}
