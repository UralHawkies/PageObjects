package ru.netology.web.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashBoardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.MoneyTransferPage;


import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    @BeforeEach
    void loginToAccount() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @AfterEach
    void returnBalance() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var dashBoardPage = new DashBoardPage();
        var firstCardBalance = dashBoardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = dashBoardPage.getCardBalance(DataHelper.getSecondCard().getId());
        if (firstCardBalance > secondCardBalance) {
            dashBoardPage.replenishSecondCardClick();
            var moneyTransferPage = new MoneyTransferPage();
            moneyTransferPage.transferCardToCard(String.valueOf((firstCardBalance - secondCardBalance) / 2), DataHelper.getFirstCard());
        } else if (firstCardBalance < secondCardBalance) {
            dashBoardPage.replenishFirstCardClick();
            var replenishmentPage = new MoneyTransferPage();
            replenishmentPage.transferCardToCard(String.valueOf((secondCardBalance - firstCardBalance) / 2), DataHelper.getSecondCard());
        }
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        var dashBoardPage = new DashBoardPage();
        dashBoardPage.replenishSecondCardClick();
        var moneyTransferPage = new MoneyTransferPage();
        var amount = 7000;
        moneyTransferPage.transferCardToCard(String.valueOf(amount), DataHelper.getFirstCard());
        var firstCardBalance = dashBoardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = dashBoardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(10000 - amount, firstCardBalance);
        assertEquals(10000 + amount, secondCardBalance);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        var dashBoardPage = new DashBoardPage();
        dashBoardPage.replenishFirstCardClick();
        var moneyTransferPage = new MoneyTransferPage();
        var amount = 3500;
        moneyTransferPage.transferCardToCard(String.valueOf(amount), DataHelper.getSecondCard());
        var firstCardBalance = dashBoardPage.getCardBalance(DataHelper.getFirstCard().getId());
        var secondCardBalance = dashBoardPage.getCardBalance(DataHelper.getSecondCard().getId());
        assertEquals(10000 + amount, firstCardBalance);
        assertEquals(10000 - amount, secondCardBalance);
    }

    @Test
    void shouldNotTransferAmountGreaterBalanceFromSecondToFirstCard() {
        var dashBoardPage = new DashBoardPage();
        dashBoardPage.replenishFirstCardClick();
        var moneyTransferPage = new MoneyTransferPage();
        var amount = 11000;
        moneyTransferPage.transferCardToCard(String.valueOf(amount), DataHelper.getSecondCard());
        moneyTransferPage.waitingError();
        assertEquals(10000, dashBoardPage.getCardBalance(DataHelper.getFirstCard().getId()));
        assertEquals(10000, dashBoardPage.getCardBalance(DataHelper.getSecondCard().getNumber()));
    }
}
