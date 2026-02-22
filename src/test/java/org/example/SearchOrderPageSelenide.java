package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.support.FindBy;


import java.time.Duration;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class SearchOrderPageSelenide extends PobedaHomePageSelenide {
    @FindBy(css = "label[for='searchOrderAgreeChb']")
    SelenideElement privacyPolicyCheckbox;

    @FindBy(xpath = "//button[@class='btn btn_search btn_search--order btn_formSearch btn_formSearch_js']")
    SelenideElement buttonFindOrder;

    @FindBy(css = "div.message_error")
    SelenideElement actualErrorMessage;

    private String url = "https://pobeda.aero";

    private String urlBookingManagement = "https://www.flypobeda.ru/services/booking-management";

    private SoftAssertions softly;

    // Конструктор с инициализацией PageFactory
    public SearchOrderPageSelenide() {
    }

    // Метод для "обновления" страницы после переключения окна
    public void refreshPage() {
        WebDriverRunner.getWebDriver().navigate().refresh();
    }

    public void confirmAndSearch() throws InterruptedException {
        privacyPolicyCheckbox.shouldBe(Condition.visible, Duration.ofSeconds(80));
        removeBannerFromDOM();
        privacyPolicyCheckbox.click();
        buttonFindOrder.click();
    }

    public void removeBannerFromDOM() {
        executeJavaScript("document.querySelector('.common-warning')?.remove();");//С ? Элемента нет -> ничего не делает, ошибки нет
        System.out.println("✓ Баннер уничтожен");
    }

    public void errorMessageIsChecked(String expectedError) {
        this.softAssert(() ->
                actualErrorMessage.shouldBe(Condition.visible, Duration.ofSeconds(10)).shouldBe(Condition.exactText(expectedError))
        );
    }
}