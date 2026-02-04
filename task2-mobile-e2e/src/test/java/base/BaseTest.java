package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;

import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected AndroidDriver driver;
    protected HomePage home;

    protected static final String PKG = "com.monefy.app.lite";
    protected static final String ACT = "com.monefy.activities.main.MainActivity_"; // ✅ correct activity

    @BeforeClass(alwaysRun = true)
    public void setUpDriver() {
        try {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setDeviceName("emulator-5554")
                    .setAppPackage(PKG)
                    .setAppActivity(ACT)
                    .setNoReset(true)
                    .setNewCommandTimeout(Duration.ofSeconds(300));

            options.setCapability("appium:autoGrantPermissions", true);
            options.setCapability("appium:appWaitPackage", PKG);
            options.setCapability("appium:appWaitActivity", "*");
            options.setCapability("appium:appWaitDuration", 60000);

            options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(180));
            options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(180));

            URL serverUrl = new URL("http://127.0.0.1:4723/");
            driver = new AndroidDriver(serverUrl, options);

            // Use explicit waits only
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        } catch (Exception e) {
            driver = null;
            throw new RuntimeException("Failed to create Appium session: " + e.getMessage(), e);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void openHome() {
        if (driver == null) throw new SkipException("Driver is null.");
        driver.activateApp(PKG);
        home = new HomePage(driver);
        home.ensureOnMainScreen();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
