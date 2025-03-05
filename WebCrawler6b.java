/*
Algorithm Description:

1. **Class Definitions**:
    - **CrawlerA** and **CrawlerB** are two classes that represent separate threads for downloading the content of a URL.
    - Both classes extend `Thread` and override the `run()` method to start the crawling process for a given URL.

2. **CrawlerA and CrawlerB Class Workflow**:
    - Each class has a constructor that accepts a URL to crawl.
    - Inside the `run()` method, the `downloadPage()` method is called, which:
        - Opens a stream to the given URL using `BufferedReader` and `InputStreamReader`.
        - Reads the content line by line and stores it in a `StringBuilder`.
        - After reading the content, it calls `saveToFile()` to save the content into a text file, where the URL is sanitized (non-alphanumeric characters replaced with underscores) to create a valid file name.
        - If successful, a message is printed indicating that the page was successfully crawled. In case of an exception, an error message is printed.
        
3. **Main Class - WebCrawler6b**:
    - A list of URLs (`urls`) is defined with the URLs that need to be crawled.
    - Instances of `CrawlerA` and `CrawlerB` are created for each URL.
    - Both threads (`crawlerA` and `crawlerB`) are started using the `start()` method, which initiates the `run()` method in both threads.
    
4. **Thread Management**:
    - The main thread waits for the completion of both threads (`crawlerA` and `crawlerB`) using `join()`. This ensures that the main program doesn’t terminate until both threads have finished their execution.
    
5. **Error Handling**:
    - Exception handling is implemented to catch issues that might occur during the downloading or file saving process. Appropriate error messages are printed if something goes wrong during the process.
    
6. **Completion**:
    - Once both threads have completed their execution, a message "Crawling finished!" is printed to indicate the successful completion of the crawling process.
*/





import java.io.*;
import java.net.*;
import java.util.*;

// Class A - Downloads content from a URL
class CrawlerA extends Thread {
    private String url; // URL to crawl

    public CrawlerA(String url) {
        this.url = url; // Assign URL
    }

    @Override
    public void run() {
        downloadPage(url); // Start downloading
    }

    private void downloadPage(String urlString) {
        try {
            URL url = new URL(urlString); // Create URL object
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); // Open stream
            String line;
            StringBuilder content = new StringBuilder(); // Store content

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Read and append content
            }
            reader.close(); // Close reader

            saveToFile(urlString, content.toString()); // Save content to file
            System.out.println("Crawled: " + urlString); // Print success message
        } catch (Exception e) {
            System.out.println("Failed to crawl: " + urlString + " - " + e.getMessage()); // Print error message
        }
    }

    private void saveToFile(String url, String content) {
        try {
            String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt"; // Create filename
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); // Open file writer
            writer.write(content); // Write content to file
            writer.close(); // Close writer
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage()); // Print error message
        }
    }
}

// Class B - Another thread for downloading a different URL
class CrawlerB extends Thread {
    private String url; // URL to crawl

    public CrawlerB(String url) {
        this.url = url; // Assign URL
    }

    @Override
    public void run() {
        downloadPage(url); // Start downloading
    }

    private void downloadPage(String urlString) {
        try {
            URL url = new URL(urlString); // Create URL object
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); // Open stream
            String line;
            StringBuilder content = new StringBuilder(); // Store content

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Read and append content
            }
            reader.close(); // Close reader

            saveToFile(urlString, content.toString()); // Save content to file
            System.out.println("Crawled: " + urlString); // Print success message
        } catch (Exception e) {
            System.out.println("Failed to crawl: " + urlString + " - " + e.getMessage()); // Print error message
        }
    }

    private void saveToFile(String url, String content) {
        try {
            String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt"; // Create filename
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); // Open file writer
            writer.write(content); // Write content to file
            writer.close(); // Close writer
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage()); // Print error message
        }
    }
}

// Main class to start the crawler
public class WebCrawler6b {
    public static void main(String[] args) {
        List<String> urls = Arrays.asList(
                "https://www.example.com", // First URL
                "https://www.wikipedia.org" // Second URL
        );

        // Creating threads for each URL
        CrawlerA crawlerA = new CrawlerA(urls.get(0)); // Thread for first URL
        CrawlerB crawlerB = new CrawlerB(urls.get(1)); // Thread for second URL

        // Start threads
        crawlerA.start(); // Start first thread
        crawlerB.start(); // Start second thread

        // Wait for threads to complete
        try {
            crawlerA.join(); // Wait for first thread
            crawlerB.join(); // Wait for second thread
        } catch (InterruptedException e) {
            e.printStackTrace(); // Print error if interrupted
        }

        System.out.println("Crawling finished!"); // Print completion message
    }
}
