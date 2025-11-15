package com.automation.megamind.base;
import com.automation.megamind.base.WebDriverContext;
import com.automation.megamind.utils.Log;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario; // CRITICAL for Extent Reports screenshot attachment
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;
// Assuming these static imports are correctly resolving for the user
import static com.automation.megamind.constants.Constants.CONFIG_PROPERTIES;
import static com.automation.megamind.utils.PropertyReader.getProperty;
/**
 * Cucumber Hooks class for managing the WebDriver lifecycle.
 * Uses Dependency Injection (DI) to share the WebDriver instance via WebDriverContext.
 */
public class Hooks {
    // The shared context object is injected by PicoContainer
    private final WebDriverContext context;
    // Constructor Injection: Cucumber/PicoContainer will create and share this.
    public Hooks(WebDriverContext context) {
        this.context = context;
    }
    /**
     * @Before hook: Runs before every scenario. Initializes the WebDriver.
     */
//    @Before(order = 1)
//    public void launchApp(){
//        String browserType = getProperty(CONFIG_PROPERTIES, "browserType");
//        String url = getProperty(CONFIG_PROPERTIES, "url");
//        long timeout = Long.parseLong(getProperty(CONFIG_PROPERTIES, "timeout"));
//
//        WebDriver driver;
//
//        if(browserType.equalsIgnoreCase("chrome")){
//            // Relying on Selenium Manager (preferred for modern Selenium)
//            driver = new ChromeDriver();
//        } else if (browserType.equalsIgnoreCase("edge")) {
//            driver = new EdgeDriver();
//        } else {
//            // Log error with SLF4J if available, otherwise use System.err
//            Log.error("Unsupported browser type: " + browserType + ". Defaulting to Chrome.");
//            driver = new ChromeDriver();
//        }
//
//        // Assign the initialized driver to the shared context
//        this.context.driver = driver;
//
//        this.context.driver.manage().window().maximize();
//        this.context.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
//        this.context.driver.get(url);
//    }

    @Before(order = 1)
    public void launchApp(){
        String browserType = getProperty(CONFIG_PROPERTIES, "browserType");
        String url = getProperty(CONFIG_PROPERTIES, "url");
        long timeout = Long.parseLong(getProperty(CONFIG_PROPERTIES, "timeout"));
        WebDriver driver;
        // ----------------------------------------
        // HEADLESS MODE FOR CHROME & EDGE
        // ----------------------------------------
        if(browserType.equalsIgnoreCase("chrome")){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");    // HEADLESS MODE
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
        } else if (browserType.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless=new");    // HEADLESS MODE
            options.addArguments("--window-size=1920,1080");
            driver = new EdgeDriver(options);
        } else {
            Log.error("Unsupported browser type: " + browserType + ". Defaulting to Chrome.");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);
        }
        // Assign the driver to PicoContainer context
        this.context.driver = driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        driver.get(url);
    }

    /**
     * @After hook: Runs after every scenario. Closes the browser and takes a screenshot on failure.
     * The ExtentCucumberAdapter in the TestRunner automatically picks up this attachment.
     */
    @After(order = 1) // Set an order to ensure screenshot capture happens before closing the browser
    public void shutdown(Scenario scenario){
        if (context.driver != null) {
            // --- EXTENT REPORT INTEGRATION POINT ---
            // If the scenario failed, capture and attach the screenshot
            if (scenario.isFailed() && context.driver instanceof TakesScreenshot) {
                try {
                    // Capture the screenshot as byte array
                    final byte[] screenshot = ((TakesScreenshot) context.driver).getScreenshotAs(OutputType.BYTES);
                    // Attach the screenshot to the Cucumber Scenario object
                    // This is the CRITICAL step the ExtentCucumberAdapter looks for!
                    scenario.attach(screenshot, "image/png", "Screenshot: " + scenario.getName());
                    Log.info("Screenshot attached for failed scenario: " + scenario.getName());
                } catch (Exception e) {
                    Log.error("Could not take screenshot for failed scenario: " + e.getMessage());
                }
            }
            // Quit the driver
            context.driver.quit();
            context.driver = null;
        }
    }
}
 
 