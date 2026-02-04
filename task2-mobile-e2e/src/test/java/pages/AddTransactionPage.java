package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class AddTransactionPage {

    private final AndroidDriver driver;

    private final By amountField = By.id("com.monefy.app.lite:id/amount_text");
    private final By confirmButton = By.id("com.monefy.app.lite:id/keyboard_action_button");

    // If your app is not English, change 'Food' to your language
    private final By categoryFood = By.xpath("//android.widget.TextView[@text='Food']");

    public AddTransactionPage(AndroidDriver driver) {
        this.driver = driver;
    }

    public void enterAmount(String amount) {
        // Monefy uses on screen keypad, so tap digits instead of sendKeys
        for (char ch : amount.toCharArray()) {
            driver.findElement(By.id("com.monefy.app.lite:id/buttonKeyboard" + ch)).click();
        }
    }


    public void confirmAndSelectFood() {
        driver.findElement(confirmButton).click();
        driver.findElement(categoryFood).click();
    }
}
