
package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;


    private final By balanceText = By.id("com.monefy.app.lite:id/balance_amount");
    private final By transactionsList = By.id("com.monefy.app.lite:id/expandableListViewTransactions");
    private final By incomeButton = By.id("com.monefy.app.lite:id/income_button");
    private final By expenseButton = By.id("com.monefy.app.lite:id/expense_button");


    private final By incomeTotalText = By.id("com.monefy.app.lite:id/income_amount_text");
    private final By expenseTotalText = By.id("com.monefy.app.lite:id/expense_amount_text");


    private final By transactionAmountText = By.id("com.monefy.app.lite:id/textViewTransactionAmount");
    private final By groupHeaderIcon = By.id("com.monefy.app.lite:id/imageViewCategoryImage");

    // ----- Add/Edit screen  -----
    private final By amountText = By.id("com.monefy.app.lite:id/amount_text");
    private final By deleteIcon = By.id("com.monefy.app.lite:id/delete");
    private final By categoryActionButton = By.id("com.monefy.app.lite:id/keyboard_action_button"); // "CHOOSE CATEGORY"/"CHANGE CATEGORY"

    // Dialog confirm
    private final By dialogOk1 = By.id("android:id/button1");
    private final By dialogOkText = By.xpath("//*[@text='OK' or @text='Ok' or @text='YES' or @text='Yes' or @text='Delete']");

    // Toolbar back on edit screen
    private final By navigateUp = AppiumBy.accessibilityId("Navigate up");

    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void ensureOnMainScreen() {

        backToSummaryIfNeeded();

        long end = System.currentTimeMillis() + 25000;
        while (System.currentTimeMillis() < end) {
            if (isPresent(balanceText) || isPresent(transactionsList)) return;
            sleep(300);
        }
        throw new TimeoutException("Home screen not detected (balance/transactions not found).");
    }

    public String getBalanceText() {
        ensureOnMainScreen();
        return driver.findElement(balanceText).getText();
    }

    public String getIncomeTotalText() {
        ensureOnMainScreen();
        return driver.findElement(incomeTotalText).getText();
    }

    public String getExpenseTotalText() {
        ensureOnMainScreen();
        return driver.findElement(expenseTotalText).getText();
    }

    /** Converts "$200.00" or "Balance $100.00" to 200.00 / 100.00 */
    public double parseMoney(String raw) {
        if (raw == null) return 0.0;
        String cleaned = raw.replaceAll("[^0-9.\\-]", "");
        if (cleaned.isEmpty() || cleaned.equals("-") || cleaned.equals(".")) return 0.0;
        return Double.parseDouble(cleaned);
    }

    public void tapIncome() {
        ensureOnMainScreen();
        driver.findElement(incomeButton).click();
        waitForPresent(amountText, 10);
    }

    public void tapExpense() {
        ensureOnMainScreen();
        driver.findElement(expenseButton).click();
        waitForPresent(amountText, 10);
    }

    public void enterAmount(String amount) {
        waitForPresent(amountText, 10);

        for (char c : amount.toCharArray()) {
            if (!Character.isDigit(c)) continue;
            By digitBtn = By.id("com.monefy.app.lite:id/buttonKeyboard" + c);
            waitForClickable(digitBtn, 10).click();
        }
    }

    /**
     * Robust "choose category" that works for BOTH income + expense.
     */
    public void confirmTransaction() {
        // Step 1: Click the action button if present
        WebElement actionBtn = waitForClickable(categoryActionButton, 12);
        actionBtn.click();

        // Step 2: click first available category item
        boolean clicked = clickFirstCategoryItemRobust();

        if (!clicked) {
            throw new TimeoutException("Could not find any category item to click after opening category chooser.");
        }

        // Step 3: Wait to land back on home
        long end = System.currentTimeMillis() + 20000;
        while (System.currentTimeMillis() < end) {
            if (isPresent(balanceText) || isPresent(transactionsList)) return;
            sleep(300);
        }
        // If not home, try backing out once
        backToSummaryIfNeeded();
        if (!(isPresent(balanceText) || isPresent(transactionsList))) {
            throw new TimeoutException("After choosing category, app did not return to home.");
        }
    }

    private boolean clickFirstCategoryItemRobust() {
        // Try multiple locator strategies; retry with small scrolls
        List<By> candidates = Arrays.asList(
                // GridView clickable children
                By.xpath("//android.widget.GridView//*[@clickable='true']"),
                By.xpath("//android.widget.GridView//android.view.ViewGroup[@clickable='true']"),
                By.xpath("//android.widget.GridView//android.widget.FrameLayout[@clickable='true']"),

                // RecyclerView clickable children
                By.xpath("//androidx.recyclerview.widget.RecyclerView//*[@clickable='true']"),
                By.xpath("//androidx.recyclerview.widget.RecyclerView//android.view.ViewGroup[@clickable='true']"),

                // ListView clickable children
                By.xpath("//android.widget.ListView//*[@clickable='true']")
        );

        long end = System.currentTimeMillis() + 20000;

        while (System.currentTimeMillis() < end) {
            for (By by : candidates) {
                List<WebElement> els = driver.findElements(by);
                if (els != null && !els.isEmpty()) {
                    for (WebElement e : els) {
                        try {
                            if (e.isDisplayed() && e.isEnabled()) {
                                e.click();
                                return true;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }

            // nothing found yet -> small scroll down then retry
            swipeUpSmall();
            sleep(300);
        }

        return false;
    }

    private WebElement findAnyTransactionRow() {
        // textViewTransactionAmount is inside a LinearLayout row (not clickable itself)
        // so click its parent (..).
        List<WebElement> amounts = driver.findElements(transactionAmountText);
        for (WebElement amount : amounts) {
            try {
                WebElement parentRow = amount.findElement(By.xpath(".."));
                if (parentRow != null && parentRow.isDisplayed()) return parentRow;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private void expandAllVisibleGroups() {
        List<WebElement> headers = driver.findElements(groupHeaderIcon);
        for (WebElement h : headers) {
            try {
                h.click();
                sleep(200);
            } catch (Exception ignored) {
            }
        }
    }

    private void confirmDeleteIfDialog() {
        if (waitForPresent(dialogOk1, 2)) {
            try {
                driver.findElement(dialogOk1).click();
                return;
            } catch (Exception ignored) {
            }
        }
        if (waitForPresent(dialogOkText, 2)) {
            try {
                driver.findElement(dialogOkText).click();
            } catch (Exception ignored) {
            }
        }
    }

    public void backToSummaryIfNeeded() {
        for (int i = 0; i < 5; i++) {
            if (isPresent(balanceText) || isPresent(transactionsList)) return;

            // If edit screen toolbar back exists, use it
            if (isPresent(navigateUp)) {
                try {
                    driver.findElement(navigateUp).click();
                } catch (Exception e) {
                    driver.navigate().back();
                }
            } else {
                driver.navigate().back();
            }
            sleep(400);
        }
    }

    private boolean isPresent(By by) {
        try {
            return !driver.findElements(by).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean waitForPresent(By by, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(d -> !d.findElements(by).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private WebElement waitForClickable(By by, int seconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return w.until(d -> {
            List<WebElement> els = d.findElements(by);
            if (els.isEmpty()) return null;
            WebElement el = els.get(0);
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });
    }

    private void swipeUpSmall() {
        // small swipe up (scroll down)
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int startY = (int) (size.height * 0.75);
        int endY = (int) (size.height * 0.45);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(300), PointerInput.Origin.viewport(), x, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipe));
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
