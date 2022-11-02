package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MoneyTransferPage {

    private SelenideElement heading = $("[data-test-id=dashboard]");

    private SelenideElement amountField = $("[data-test-id=\"amount\"] .input__control");
    private SelenideElement whereFrom = $("[data-test-id=\"from\"] input");
    private SelenideElement button = $("[data-test-id=\"action-transfer\"] .button__text");
    private SelenideElement errorNotification = $("[data-test-id=\"error-notification\"] .notification__content");


    public MoneyTransferPage() {
        heading.shouldBe(visible);
    }

    public DashBoardPage transferCardToCard(String amount, DataHelper.Card from) {
        amountField.click();
        amountField.setValue(amount);
        whereFrom.click();
        whereFrom.setValue(from.getNumber());
        button.click();
        return new DashBoardPage();
    }

    public DashBoardPage waitingError() {
        $("#root").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("#root").shouldHave(Condition.exactText("Ошибка"));
        return new DashBoardPage();
    }
}
