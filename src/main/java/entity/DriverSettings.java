package entity;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class DriverSettings {
    private ChromeDriver driver;

    public DriverSettings() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        this.driver = new ChromeDriver(options);
    }

    public ChromeDriver getDriver(boolean headless) {
        if (headless) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10,  TimeUnit.SECONDS);
            return driver;
        } else {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10,  TimeUnit.SECONDS);
            return driver;
        }
    }

    public ChromeDriver getDriver(String vendorCode) {
        this.driver.manage().timeouts().implicitlyWait(10,  TimeUnit.SECONDS);
        this.driver.get("https://baza.drom.ru/oem/" + vendorCode);
        return driver;
    }
}
