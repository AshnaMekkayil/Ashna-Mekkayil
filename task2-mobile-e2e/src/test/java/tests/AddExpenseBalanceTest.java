package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddExpenseBalanceTest extends BaseTest {

    private double parseBalance(String balanceText) {
        String cleaned = balanceText.replaceAll("[^0-9.\\-]", "");
        if (cleaned.isEmpty()) return 0;
        return Double.parseDouble(cleaned);
    }

    @Test
    public void addExpense_shouldDecreaseBalanceOrStaySame() {
        double before = parseBalance(home.getBalanceText());

        home.tapExpense();
        home.enterAmount("100");
        home.confirmTransaction();

        double after = parseBalance(home.getBalanceText());
        Assert.assertTrue(after <= before || after == before,
                "Balance should decrease or stay same after expense.");
    }
}
