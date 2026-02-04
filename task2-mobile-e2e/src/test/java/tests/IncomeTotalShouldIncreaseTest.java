// src/test/java/tests/IncomeTotalShouldIncreaseTest.java
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IncomeTotalShouldIncreaseTest extends BaseTest {

    @Test
    public void incomeTotal_shouldIncreaseAfterAddingIncome() {

        double before = home.parseMoney(home.getIncomeTotalText());

        home.tapIncome();
        home.enterAmount("10");
        home.confirmTransaction();

        double after = home.parseMoney(home.getIncomeTotalText());

        Assert.assertTrue(after >= before + 10.0,
                "Income total did not increase by expected amount. Before=" + before + " After=" + after);
    }
}
