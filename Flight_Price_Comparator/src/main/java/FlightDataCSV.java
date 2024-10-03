import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FlightDataCSV {

    public static void writeCSV(String filename, List<Flight> flights) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write the header
            writer.append("Operator,Flight Number,Price on Cleartrip,Price on Paytm\n");

            // Write each flight's details
            for (Flight flight : flights) {
                writer.append(escapeCSV(flight.getOperator())).append(",")
                        .append(escapeCSV(flight.getFlightNumber())).append(",")
                        .append(escapeCSV(flight.getPriceOnCleartrip())).append(",")
                        .append(escapeCSV(flight.getPriceOnPaytm())).append("\n");
            }
            System.out.println("Flight data has been written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Method to escape special characters in CSV
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // Escape quotes and add quotes around the value
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
