import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class AnalyticsDashboard {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static List<GameSale> salesData = new ArrayList<>();

    public static Scanner getScanner() {
        return scanner;
    }

    public static DecimalFormat getDf() {
        return df;
    }

    public static List<GameSale> getSalesData() {
        return salesData;
    }

    public static void setSalesData(List<GameSale> salesData) {
        AnalyticsDashboard.salesData = salesData;
    }
    
    public static class GameSale {
        String title;
        String console;
        String genre;
        String publisher;
        double totalSales;
        String releaseDate;
        // track when record was last updated
        
        public GameSale(String title, String console, String genre, String publisher, 
                       double totalSales, String releaseDate, String lastUpdate) {
            this.title = title;
            this.console = console;
            this.genre = genre;
            this.publisher = publisher;
            this.totalSales = totalSales;
            this.releaseDate = releaseDate;
        }

        public GameSale() {
        }
    }
    
    public static void main(String[] args) {
        try (scanner) {
            loadData();
            
            System.out.println("========================================");
            System.out.println("   MINI DATA ANALYTICS CONSOLE");
            System.out.println("   Video Game Sales Dashboard");
            System.out.println("========================================\n");
            
            boolean running = true;
            while (running) {
                displayMenu();
                int choice = getUserChoice();
                
                switch (choice) {
                    case 1 -> viewDatasetSummary();
                    case 2 -> viewMonthlySales();
                    case 3 -> viewTopPublishers();
                    case 4 -> viewGenreAnalysis();
                    case 5 -> {
                        System.out.println("\nExiting dashboard. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("\nInvalid choice. Please try again.");
                }
                
                if (running) {
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("MAIN MENU");
        System.out.println("========================================");
        System.out.println("1 - View Dataset Summary");
        System.out.println("2 - Monthly Sales");
        System.out.println("3 - Top Publishers");
        System.out.println("4 - Genre Analysis");
        System.out.println("5 - Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }
    
    private static int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void loadData() {
        String csvFile = "vgchartz-2024.csv";
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                if (data.length >= 7) {
                    String title = data[0].replace("\"", "").trim();
                    String console = data[1].replace("\"", "").trim();
                    String genre = data[2].replace("\"", "").trim();
                    String publisher = data[3].replace("\"", "").trim();
                    double totalSales = parseDouble(data[4]);
                    String releaseDate = data[5].replace("\"", "").trim();
                    String lastUpdate = data[6].replace("\"", "").trim();
                    
                    salesData.add(new GameSale(title, console, genre, publisher, 
                                              totalSales, releaseDate, lastUpdate));
                }
            }
            
            System.out.println("Data loaded successfully: " + salesData.size() + " records\n");
            
        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            System.err.println("Please ensure vgchartz-2024.csv is in the same directory.");
        }
    }
    
    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace("\"", "").replace("m", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private static void viewDatasetSummary() {
        System.out.println("\n========================================");
        System.out.println("DATASET SUMMARY");
        System.out.println("========================================");
        
        System.out.println("Total Records: " + salesData.size());
        
        double totalSales = salesData.stream()
                                     .mapToDouble(g -> g.totalSales)
                                     .sum();
        System.out.println("Total Sales: " + df.format(totalSales) + "m");
        
        double avgSales = salesData.stream()
                                   .mapToDouble(g -> g.totalSales)
                                   .average()
                                   .orElse(0.0);
        System.out.println("Average Sales per Game: " + df.format(avgSales) + "m");
        
        long uniqueConsoles = salesData.stream()
                                       .map(g -> g.console)
                                       .distinct()
                                       .count();
        System.out.println("Number of Consoles: " + uniqueConsoles);
        
        long uniqueGenres = salesData.stream()
                                     .map(g -> g.genre)
                                     .distinct()
                                     .count();
        System.out.println("Number of Genres: " + uniqueGenres);
        
        long uniquePublishers = salesData.stream()
                                         .map(g -> g.publisher)
                                         .distinct()
                                         .count();
        System.out.println("Number of Publishers: " + uniquePublishers);
        
        GameSale topGame = salesData.stream()
                                    .max(Comparator.comparingDouble(g -> g.totalSales))
                                    .orElse(null);
        
        if (topGame != null) {
            System.out.println("\nTop Selling Game:");
            System.out.println("  Title: " + topGame.title);
            System.out.println("  Sales: " + df.format(topGame.totalSales) + "m");
            System.out.println("  Console: " + topGame.console);
        }
        
        System.out.println("========================================");
    }
    
    private static void viewMonthlySales() {
        System.out.println("\n========================================");
        System.out.println("MONTHLY SALES ANALYSIS");
        System.out.println("========================================");
        
        Map<String, Double> monthlySales = new HashMap<>();
        
        for (GameSale game : salesData) {
            if (game.releaseDate != null && !game.releaseDate.isEmpty()) {
                String[] parts = game.releaseDate.split(" ");
                if (parts.length >= 2) {
                    String month = parts[0];
                    monthlySales.put(month, monthlySales.getOrDefault(month, 0.0) + game.totalSales);
                }
            }
        }
        
        List<Map.Entry<String, Double>> sortedMonths = new ArrayList<>(monthlySales.entrySet());
        sortedMonths.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        System.out.printf("%-15s %15s%n", "Month", "Total Sales");
        System.out.println("----------------------------------------");
        
        for (Map.Entry<String, Double> entry : sortedMonths) {
            System.out.printf("%-15s %15s%n", 
                            entry.getKey(), 
                            df.format(entry.getValue()) + "m");
        }
        
        System.out.println("========================================");
    }
    
    private static void viewTopPublishers() {
        System.out.println("\n========================================");
        System.out.println("TOP PUBLISHERS");
        System.out.println("========================================");
        
        Map<String, Double> publisherSales = new HashMap<>();
        Map<String, Integer> publisherCount = new HashMap<>();
        
        for (GameSale game : salesData) {
            String publisher = game.publisher;
            publisherSales.put(publisher, publisherSales.getOrDefault(publisher, 0.0) + game.totalSales);
            publisherCount.put(publisher, publisherCount.getOrDefault(publisher, 0) + 1);
        }
        
        List<Map.Entry<String, Double>> sortedPublishers = new ArrayList<>(publisherSales.entrySet());
        sortedPublishers.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        System.out.printf("%-30s %15s %10s%n", "Publisher", "Total Sales", "Games");
        System.out.println("--------------------------------------------------------");
        
        int count = 0;
        for (Map.Entry<String, Double> entry : sortedPublishers) {
            if (count >= 10) break;
            
            String publisher = entry.getKey();
            System.out.printf("%-30s %15s %10d%n", 
                            publisher.length() > 30 ? publisher.substring(0, 27) + "..." : publisher,
                            df.format(entry.getValue()) + "m",
                            publisherCount.get(publisher));
            count++;
        }
        
        System.out.println("========================================");
    }
    
    private static void viewGenreAnalysis() {
        System.out.println("\n========================================");
        System.out.println("GENRE ANALYSIS");
        System.out.println("========================================");
        
        Map<String, Double> genreSales = new HashMap<>();
        Map<String, Integer> genreCount = new HashMap<>();
        
        for (GameSale game : salesData) {
            String genre = game.genre;
            genreSales.put(genre, genreSales.getOrDefault(genre, 0.0) + game.totalSales);
            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }
        
        List<Map.Entry<String, Double>> sortedGenres = new ArrayList<>(genreSales.entrySet());
        sortedGenres.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        System.out.printf("%-20s %15s %10s %15s%n", "Genre", "Total Sales", "Games", "Avg Sales");
        System.out.println("----------------------------------------------------------------");
        
        for (Map.Entry<String, Double> entry : sortedGenres) {
            String genre = entry.getKey();
            double totalSales = entry.getValue();
            int count = genreCount.get(genre);
            double avgSales = totalSales / count;
            
            System.out.printf("%-20s %15s %10d %15s%n", 
                            genre,
                            df.format(totalSales) + "m",
                            count,
                            df.format(avgSales) + "m");
        }
        
        System.out.println("========================================");
    }
}