import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class WebCrawler {
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>()); // To store visited URLs
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>(); // Thread-safe queue for URLs
    private final ExecutorService threadPool; // Thread pool for concurrent execution

    // Constructor: Initializes thread pool with N threads
    public WebCrawler(int numThreads) {
        threadPool = Executors.newFixedThreadPool(numThreads);
    }

    // Function to start crawling
    public void startCrawling(String startUrl, int maxPages) {
        urlQueue.add(startUrl); // Add initial URL to queue

        while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
            String url = urlQueue.poll(); // Get next URL
            if (url == null || visitedUrls.contains(url)) {
                continue; // Skip if already visited
            }
            visitedUrls.add(url); // Mark as visited
            threadPool.execute(() -> crawl(url, maxPages)); // Submit URL for processing
        }

        // Shut down thread pool when all tasks are finished
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(10, TimeUnit.MINUTES); // Wait for all threads to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function to crawl a single page
    private void crawl(String url, int maxPages) {
        try {
            // Fetch page content
            String htmlContent = fetchHtml(url);

            // Extract URLs from the content
            List<String> extractedUrls = extractUrls(htmlContent);

            // Add new URLs to queue
            for (String newUrl : extractedUrls) {
                if (visitedUrls.size() >= maxPages) {
                    break; // Stop if max pages reached
                }
                if (!visitedUrls.contains(newUrl)) {
                    urlQueue.add(newUrl);
                }
            }

            // Print crawled URL
            System.out.println("Crawled: " + url);

        } catch (IOException e) {
            System.err.println("Failed to fetch: " + url);
        }
    }

    // Function to fetch HTML content from a URL
    private String fetchHtml(String urlString) throws IOException {
        StringBuilder content = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up HTTP request headers
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Simulate browser request

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Append HTML content
            }
        }
        return content.toString();
    }

    // Function to extract URLs from HTML content using regex
    private List<String> extractUrls(String htmlContent) {
        List<String> urls = new ArrayList<>();
        String regex = "href=[\"'](http[s]?://.*?)[\"']"; // Regex to find links
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            urls.add(matcher.group(1)); // Add found URL
        }
        return urls;
    }

    // Main function to run the crawler
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(5); // Create a crawler with 5 threads
        crawler.startCrawling("https://example.com", 10); // Start crawling from example.com, max 10 pages
    }
}
