import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

public class FlightPriceComparator {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Get travel date input from the user in MM/DD/YYYY format
        System.out.print("Enter the travel date (MM/DD/YYYY): ");
        String travelDate = scanner.nextLine();

        FlightScraper scraper = new FlightScraper();

        // Scrape flight data from Cleartrip and Paytm using the specified date
        List<Map<String, String>> cleartripData = scraper.scrapeCleartrip(travelDate);
        List<Map<String, String>> paytmData = scraper.scrapePaytm(travelDate);

        // List to hold the flight objects
        List<Flight> flights = new ArrayList<>();

        // Compare and populate flight data
        for (Map<String, String> cleartripFlight : cleartripData) {
            String operator = cleartripFlight.get("operator");
            String flightNumber = cleartripFlight.get("flightNumber");
            String cleartripPrice = cleartripFlight.get("price");

            // Find matching flight in Paytm data
            for (Map<String, String> paytmFlight : paytmData) {
                if (paytmFlight.get("flightNumber").equals(flightNumber)) {
                    String paytmPrice = paytmFlight.get("price");
                    // Create Flight object and add to the list
                    flights.add(new Flight(operator, flightNumber, cleartripPrice, paytmPrice));
                }
            }
        }

        // Write the flight details to a CSV file
        FlightDataCSV.writeCSV("flight_data.csv", flights);



        scanner.close();
    }
}
