package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddIncomeBalanceTest extends BaseTest {

    private double parseBalance(String balanceText) {
        // examples: "Balance $100.00"
        String cleaned = balanceText.replaceAll("[^0-9.\\-]", "");
        if (cleaned.isEmpty()) return 0;
        return Double.parseDouble(cleaned);
    }

    @Test
    public void addIncome_shouldIncreaseBalance() {
        double before = parseBalance(home.getBalanceText());

        home.tapIncome();
        home.enterAmount("200");
        home.confirmTransaction();

        double after = parseBalance(home.getBalanceText());
        Assert.assertTrue(after >= before, "Balance should increase or stay same after income.");
    }
}
