package base;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

public class UiHelper {

    private final AndroidDriver driver;

    public UiHelper(AndroidDriver driver) {
        this.driver = driver;
    }


    public void tapDelete() {
        // Monefy usually has delete icon with this id on transaction details
        driver.findElement(By.id("com.monefy.app.lite:id/delete")).click();
    }


    public void tapOkIfDialogComes() {
        try {
            driver.findElement(By.id("android:id/button1")).click();
        } catch (NoSuchElementException ignored) {}
    }


    public void tapCancelIfDialogComes() {
        try {
            driver.findElement(By.id("android:id/button2")).click();
        } catch (NoSuchElementException ignored) {}
    }


    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
