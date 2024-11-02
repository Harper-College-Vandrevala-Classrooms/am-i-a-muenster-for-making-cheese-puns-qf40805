package com.csc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheeseAnalyzer {

    public static void main(String[] args) {
        String csvFile = "cheese_data.csv"; // Change this to the path of your CSV file
        String outputFile = "output.txt";
        String cheeseWithoutHeadersFile = "cheese_without_headers.csv"; // New file for cheese data without headers
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            String headerLine = br.readLine(); // Read the header line
            
            // Initialize counters
            int pasteurizedCount = 0;
            int rawCount = 0;
            int organicMoistureCount = 0;
            int totalCheeses = 0;
            Map<String, Integer> milkTypeCount = new HashMap<>();
            
            // Create BufferedWriter for the new CSV file
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(cheeseWithoutHeadersFile));
            
            // Process each line of the CSV
            while ((line = br.readLine()) != null) {
                csvWriter.write(line); // Write the current line to the new CSV file
                csvWriter.newLine(); // Add a new line
                
                String[] columns = line.split(",");

                // Assuming columns are in the following order: [Name, MilkTreatmentTypeEn, Organic, MoisturePercent, MilkTypeEn]
                String milkTreatment = columns[1];
                String organic = columns[2];
                double moisturePercent = Double.parseDouble(columns[3]);
                String milkType = columns[4];

                // Count milk treatment types
                if ("pasteurized".equalsIgnoreCase(milkTreatment)) {
                    pasteurizedCount++;
                } else if ("raw".equalsIgnoreCase(milkTreatment)) {
                    rawCount++;
                }

                // Count organic cheeses with moisture percentage > 41.0%
                if ("1".equals(organic) || "yes".equalsIgnoreCase(organic) && moisturePercent > 41.0) { // Updated line
                    organicMoistureCount++;
                }

                // Count milk types
                milkTypeCount.put(milkType, milkTypeCount.getOrDefault(milkType, 0) + 1);
                totalCheeses++;
            }
            br.close();
            csvWriter.close(); // Close the CSV writer

            // Determine the milk type with the most cheeses
            String mostCommonMilkType = null;
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : milkTypeCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostCommonMilkType = entry.getKey();
                }
            }

            // Write results to output.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write("Number of cheeses using pasteurized milk: " + pasteurizedCount + "\n");
                writer.write("Number of cheeses using raw milk: " + rawCount + "\n");
                writer.write("Number of organic cheeses with moisture > 41.0%: " + organicMoistureCount + "\n");
                writer.write("Most common milk type in Canada: " + mostCommonMilkType + "\n");
            }

            System.out.println("Calculations completed. Results written to output.txt and cheese data without headers written to " + cheeseWithoutHeadersFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
