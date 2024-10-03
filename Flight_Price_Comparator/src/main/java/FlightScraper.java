import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class FlightScraper {
    // Base URLs for flight aggregation websites
    private final String cleartripUrl = "https://www.cleartrip.com/flights/results"; // Base MakeMyTrip URL
    private final String paytmUrl = "https://tickets.paytm.com/flights"; // Base Paytm URL

    // Scrape flight details from MakeMyTrip
    public List<Map<String, String>> scrapeCleartrip(String travelDate) {
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe"); // Set path to chromedriver

        // Initialize ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized"); // Start browser maximized

        // Create a WebDriver instance with ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        List<Map<String, String>> flightData = new ArrayList<>();

        try {
            // Set page load timeout to 20 seconds
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            // Set implicit wait for elements to 20 seconds
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Hardcoded values for the fixed cities
            String fromCityId = "BLR"; // Bangalore IATA code
            String toCityId = "DEL";   // Delhi IATA code

            // Convert travel date from MM/DD/YYYY to DD/MM/YYYY for MakeMyTrip
            LocalDate date = LocalDate.parse(travelDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Parse input date
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Format to DD/MM/YYYY

            // Construct the MakeMyTrip URL using the formatted date
            String url = cleartripUrl + "?adults=1&childs=0&infants=0&class=Economy&depart_date=" + formattedDate
                    + "&from=" + fromCityId + "&to=" + toCityId + "&intl=n";

            driver.get(url);

            // Wait for the flight rows to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("fs-3 fw-400 c-neutral-900"))); // Adjust as needed

            // Scrape flight data from MakeMyTrip
            List<WebElement> flights = driver.findElements(By.cssSelector("fs-3 fw-400 c-neutral-900")); // Adjust the selector based on the actual MakeMyTrip structure

            for (WebElement flight : flights) {
                Map<String, String> flightInfo = new HashMap<>();
                String operator = flight.findElement(By.cssSelector("fw-500 fs-2 c-neutral-900")).getText();
                String flightNumber = flight.findElement(By.cssSelector("fs-1 c-neutral-400 pt-1")).getText();
                String price = flight.findElement(By.cssSelector("m-0 fs-5 fw-700 c-neutral-900 ta-right false")).getText();

                if (isNonStop(flight)) {
                    flightInfo.put("operator", operator);
                    flightInfo.put("flightNumber", flightNumber);
                    flightInfo.put("price", price);
                    flightData.add(flightInfo);
                }
            }
        } catch (TimeoutException e) {
            // Handle timeout if necessary (you can keep or remove the output)
        } catch (NoSuchElementException e) {
            // Handle the case where elements are not found (you can keep or remove the output)
        } finally {
            driver.quit(); // Ensure the driver quits after usage
        }
        return flightData; // Return the scraped flight data
    }

    // Scrape flight details from Paytm using IATA codes
    public List<Map<String, String>> scrapePaytm(String travelDate) {
        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver.exe"); // Set path to chromedriver

        // Initialize ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized"); // Start browser maximized

        // Create a WebDriver instance with ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        List<Map<String, String>> flightData = new ArrayList<>();

        try {
            // Set page load timeout to 20 seconds
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            // Set implicit wait for elements to 20 seconds
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Format the travel date to YYYY-MM-DD for Paytm
            LocalDate date = LocalDate.parse(travelDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Parse input date
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Format to YYYY-MM-DD

            // Construct the Paytm URL using IATA codes
            String url = paytmUrl + "/flightSearch/BLR-Bengaluru/DEL-Delhi/1/0/0/E/" + formattedDate;
            driver.get(url);

            // Wait for the flight rows to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("_2RaZo"))); // Adjust as needed

            // Scrape flight data from Paytm
            List<WebElement> flights = driver.findElements(By.cssSelector("_2RaZo")); // Adjust the selector based on the actual Paytm structure

            for (WebElement flight : flights) {
                Map<String, String> flightInfo = new HashMap<>();
                String operator = flight.findElement(By.cssSelector("_2cP56")).getText();
                String flightNumber = flight.findElement(By.cssSelector("_3tMEB")).getText();
                String price = flight.findElement(By.cssSelector("_2MkSl")).getText();

                if (isNonStop(flight)) {
                    flightInfo.put("operator", operator);
                    flightInfo.put("flightNumber", flightNumber);
                    flightInfo.put("price", price);
                    flightData.add(flightInfo);
                }
            }
        } catch (TimeoutException e) {
            // Handle timeout if necessary (you can keep or remove the output)
        } catch (NoSuchElementException e) {
            // Handle the case where elements are not found (you can keep or remove the output)
        } finally {
            driver.quit(); // Ensure the driver quits after usage
        }
        return flightData; // Return the scraped flight data
    }

    // Check if the flight is non-stop
    private boolean isNonStop(WebElement flight) {
        try {
            String stopsInfo = flight.findElement(By.cssSelector(".stops")).getText();
            return stopsInfo.contains("non-stop"); // Adjust based on actual website text
        } catch (NoSuchElementException e) {
            return false; // Default to false if we cannot find stops information
        }
    }
}
