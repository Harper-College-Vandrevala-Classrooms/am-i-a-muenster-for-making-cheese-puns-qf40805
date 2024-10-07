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
            
            // Process each line of the CSV
            while ((line = br.readLine()) != null) {
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
                if ("1".equals(organic) && moisturePercent > 41.0) { // Corrected line
                  organicMoistureCount++;
              }

                // Count milk types
                if (milkTypeCount.containsKey(milkType)) {
                    milkTypeCount.put(milkType, milkTypeCount.get(milkType) + 1);
                } else {
                    milkTypeCount.put(milkType, 1);
                }

                totalCheeses++;
            }
            br.close();

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

            System.out.println("Calculations completed. Results written to output.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
