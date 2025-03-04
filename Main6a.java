/*
Algorithm:
1. Create a `NumberPrinter` class with methods `printZero()`, `printEven(int)`, and `printOdd(int)`.
2. Implement a `ThreadController` class to synchronize three threads:
   - `ZeroThread` prints 0 before every number.
   - `EvenThread` prints even numbers.
   - `OddThread` prints odd numbers.
3. Use three `Semaphore` objects:
   - `zeroSemaphore` starts with 1, allowing `ZeroThread` to execute first.
   - `evenSemaphore` and `oddSemaphore` start at 0, blocking even and odd printing until a zero is printed.
4. Thread Execution:
   - `ZeroThread` prints `0` and signals either `evenSemaphore` or `oddSemaphore` based on `i` value.
   - `EvenThread` waits for `evenSemaphore`, prints an even number, then signals `zeroSemaphore`.
   - `OddThread` waits for `oddSemaphore`, prints an odd number, then signals `zeroSemaphore`.
5. Start and join all threads in `Main` to ensure proper execution order.
6. The result is an interleaved sequence "0102030405" for `n=5`.
*/








import java.util.concurrent.Semaphore;

class NumberPrinter {
    public void printZero() {
        System.out.print("0");
    }
    
    public void printEven(int num) {
        System.out.print(num);
    }
    
    public void printOdd(int num) {
        System.out.print(num);
    }
}

class ThreadController {
    private int n;
    private NumberPrinter printer;
    private Semaphore zeroSemaphore = new Semaphore(1);
    private Semaphore evenSemaphore = new Semaphore(0);
    private Semaphore oddSemaphore = new Semaphore(0);

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void zero() {
        try {
            for (int i = 1; i <= n; i++) {
                zeroSemaphore.acquire();
                printer.printZero();
                if (i % 2 == 0) {
                    evenSemaphore.release();
                } else {
                    oddSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void even() {
        try {
            for (int i = 2; i <= n; i += 2) {
                evenSemaphore.acquire();
                printer.printEven(i);
                zeroSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void odd() {
        try {
            for (int i = 1; i <= n; i += 2) {
                oddSemaphore.acquire();
                printer.printOdd(i);
                zeroSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main6a {
    public static void main(String[] args) {
        int n = 5;
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);
        
        Thread zeroThread = new Thread(controller::zero);
        Thread evenThread = new Thread(controller::even);
        Thread oddThread = new Thread(controller::odd);
        
        zeroThread.start();
        evenThread.start();
        oddThread.start();
        
        try {
            zeroThread.join();
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
