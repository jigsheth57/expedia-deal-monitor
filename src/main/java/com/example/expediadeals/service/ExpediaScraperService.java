package com.example.expediadeals.service;

import com.example.expediadeals.model.VacationDeal;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ExpediaScraperService {

    private static final Logger logger = LoggerFactory.getLogger(ExpediaScraperService.class);

    public List<VacationDeal> findVacationDeals(String originCity) {
        // Consider making this configurable or passed in
        if (!"Chicago".equalsIgnoreCase(originCity)) {
            logger.warn("This scraper is currently optimized for Chicago. Received: {}", originCity);
            // Optionally return empty or throw an exception
        }

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36");


        WebDriver driver = new ChromeDriver(options);
        List<VacationDeal> deals = new ArrayList<>();

        try {
            // Navigate to Expedia (Example: Package Deals from Chicago)
            // The exact URL and navigation steps will change frequently with Expedia's site structure.
            // This is a simplified example. You'll need to inspect Expedia's current site.
            driver.get("https://www.expedia.com/packages");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            logger.info("Navigated to Expedia. Searching for deals from {}", originCity);

            // --- Example Steps (These will need to be adapted to Expedia's current site) ---
            // 1. Find and fill the "Leaving from" field
            try {
                WebElement leavingFromButton = driver.findElement(By.cssSelector("button[data-stid='location-field-origin-menu-trigger']"));
                leavingFromButton.click();
                WebElement leavingFromInput = driver.findElement(By.cssSelector("input[data-stid='location-field-origin-dialog-input']"));
                leavingFromInput.sendKeys(originCity);
                Thread.sleep(1000); // Wait for suggestions
                WebElement firstSuggestion = driver.findElement(By.cssSelector("li[data-stid='location-field-origin-result-item'] button")); // Adjust selector
                firstSuggestion.click();
                Thread.sleep(500);
                driver.findElement(By.cssSelector("button[data-stid='location-field-dialog-done-button']")).click();

            } catch (Exception e) {
                logger.error("Error interacting with 'Leaving from' field: {}", e.getMessage());
                // Fallback or re-throw if critical
            }
            
            // 2. (Potentially) Find and fill "Going to" (if not looking for general deals)

            // 3. Find and click the search button
            try {
                 WebElement searchButton = driver.findElement(By.cssSelector("button[data-testid='submit-button']")); // Adjust selector
                 searchButton.click();
                 logger.info("Search submitted.");
                 Thread.sleep(5000); // Wait for results to load
            } catch (Exception e) {
                logger.error("Error clicking search button: {}", e.getMessage());
            }


            // 4. Extract deal information - This is highly dependent on Expedia's HTML structure
            // You'll need to use browser developer tools to find the correct selectors.
            // This is a placeholder for the complex part of scraping.
            List<WebElement> dealElements = driver.findElements(By.cssSelector("div[data-stid='property-listing']")); // Example selector, **WILL CHANGE**
            logger.info("Found {} potential deal elements.", dealElements.size());


            for (int i = 0; i < Math.min(dealElements.size(), 5); i++) { // Limit to first 5 deals
                WebElement dealElement = dealElements.get(i);
                try {
                    String title = "N/A";
                    String price = "N/A";
                    String detailsUrl = "N/A";

                    try {
                        title = dealElement.findElement(By.cssSelector("h3[data-stid='listing-title']")).getText(); // **WILL CHANGE**
                    } catch (Exception e) { logger.warn("Could not find title for a deal."); }

                    try {
                        price = dealElement.findElement(By.cssSelector("span[data-stid='price-summary']")).getText(); // **WILL CHANGE**
                    } catch (Exception e) { logger.warn("Could not find price for a deal."); }

                     try {
                        detailsUrl = dealElement.findElement(By.cssSelector("a[data-stid='open-hotel-information']")).getAttribute("href"); // **WILL CHANGE**
                        if (detailsUrl != null && !detailsUrl.startsWith("http")) {
                            detailsUrl = "https://www.expedia.com" + detailsUrl;
                        }
                    } catch (Exception e) { logger.warn("Could not find details URL for a deal."); }


                    if (!"N/A".equals(title)) {
                        deals.add(new VacationDeal(title, price, "Dates not extracted in this example", originCity, detailsUrl));
                        logger.info("Extracted Deal: {} - {}", title, price);
                    }
                } catch (Exception e) {
                    logger.error("Error extracting individual deal information: {}", e.getMessage(), e);
                }
            }

            if (deals.isEmpty() && !dealElements.isEmpty()) {
                logger.warn("Deal elements were found, but no information could be extracted. Selectors might be outdated.");
            } else if (deals.isEmpty()) {
                logger.warn("No deals found or extracted from {}.", originCity);
            }


        } catch (Exception e) {
            logger.error("An error occurred during Selenium scraping: {}", e.getMessage(), e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return deals;
    }
}
