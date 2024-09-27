package tek.retail.tdd.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseSetup {

    private static final Logger LOGGER = LogManager.getLogger(BaseSetup.class);
    protected static final long WAIT_FOR_SECONDS = 20;
    private static WebDriver driver;
    private final Properties properties;

    public BaseSetup() {
        // Reading config file
        try {

            String configFile = System.getProperty("user.dir") + "/src/test/resources/configs/dev.config.properties";
            LOGGER.debug("Reading config file {}", configFile);
            InputStream inputStream = new FileInputStream(configFile);
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.debug("Config file error with message {}", e.getMessage());
            throw new RuntimeException("Config file error with message " + e.getMessage());
        }

    }

    public void setupBrowser() {
        String url = properties.getProperty("ui.url");
        String browserType = properties.getProperty("ui.browser");
        boolean isHeadless = Boolean.parseBoolean(properties.getProperty("ui.browser.headless"));
        LOGGER.debug("Opening browser type {} and isHeadless {}", browserType, isHeadless);

        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (isHeadless) options.addArguments("--headless");
            driver = new ChromeDriver(options);
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (isHeadless) options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        } else if (browserType.equalsIgnoreCase("edge")) {
            EdgeOptions options = new EdgeOptions();
            if (isHeadless) options.addArguments("--headless");
            driver = new EdgeDriver(options);
        } else
            throw new RuntimeException("Wrong browser type, choose chrome, firefox or edge");

        LOGGER.debug("Navigating to url {}", url);
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_FOR_SECONDS));

    }

    public void quitBrowser() {
        if (driver != null) {
            LOGGER.debug("Quit the browser");
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
