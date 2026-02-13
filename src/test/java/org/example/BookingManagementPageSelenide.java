package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementShould;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class BookingManagementPageSelenide extends PobedaHomePageSelenide {

    @FindBy(className = "dp-ya8faq-root-root")
    private SelenideElement noThanksButtonPopup;

    @FindBy(xpath = "(//*[contains(@class,'dp-rr18i4-root-root')])[2]")
    private SelenideElement bookingManagement;

    @FindBy(xpath = "(//*[contains(@class,'dp-zu3w2f-root-control')])[1]")
    SelenideElement lastNameField;

    @FindBy(xpath = "(//*[contains(@class,'dp-zu3w2f-root-control')])[2]")
    SelenideElement orderNumber;

    @FindBy(xpath = "//button[@class='dp-tsteac-root-root-submitBtn']")
    SelenideElement buttonSearchBM;

    @FindBy(css = "label[for='searchOrderAgreeChb']")
    SelenideElement privacyPolicyCheckbox;

    @FindBy(xpath = "//button[@class='btn btn_search btn_search--order btn_formSearch btn_formSearch_js']")
    SelenideElement buttonFindOrder;

    private String url = "https://pobeda.aero";

    WebElement elementTest2;
    private String urlBookingManagement = "https://www.flypobeda.ru/services/booking-management";

    private String originalWindow;

    //Константы Заголовков в Блоке Информация
    private static final String EXPECTED_ORDER_NUMBER = "Номер заказа или билета";
    private static final String EXPECTED_LAST_NAME_FIELD = "Фамилия клиента";
    private static final String EXPECTED_BUTTON_SEARCH_BM = "Поиск";

    // Конструктор с инициализацией PageFactory
    public BookingManagementPageSelenide() {
    }

    // Метод для "обновления" страницы после переключения окна
    public void refreshPage() {
        WebDriverRunner.getWebDriver().navigate().refresh();
    }


    public void closePromoPopup() {
        if (noThanksButtonPopup.isDisplayed()) {
            noThanksButtonPopup.click();
        }
    }

    public void scrollAndClickBookingManagement() throws InterruptedException {
        executeJavaScript("arguments[0].scrollIntoView(true);", bookingManagement);
        bookingManagement.click();
    }

    // Проверка URL
    public void verifyURL() {
        String actualUrl = WebDriverRunner.url();
        softly.assertThat(actualUrl)
                .as("URL не одинаковые")
                .isEqualTo(urlBookingManagement);
    }

    //Проверка наличия полей «Номер заказа или билета», «Фамилия клиента» и кнопки «Поиск»
    public void BookingManagementIsDisplayed() {
        this.softAssert(() ->
                orderNumber
                        .shouldBe(Condition.visible, Duration.ofSeconds(10))
                        .shouldHave(attribute("placeholder",EXPECTED_ORDER_NUMBER))
        );
        this.softAssert(() ->
                lastNameField
                        .shouldBe(Condition.visible, Duration.ofSeconds(10))
                        .shouldHave(attribute("placeholder", EXPECTED_LAST_NAME_FIELD))
        );
        this.softAssert(() ->
                buttonSearchBM
                        .shouldBe(Condition.visible, Duration.ofSeconds(10))
                        .shouldHave(attribute("placeholder", EXPECTED_BUTTON_SEARCH_BM))
        );
    }

    public void fillSearchFormAndSubmit() throws InterruptedException {
        //refreshPage();
        orderNumber.shouldBe(visible).setValue("XXXXXX");
        lastNameField.shouldBe(visible).setValue("Qwerty");
        buttonSearchBM.click();

        // Сохраняем дескриптор текущей вкладки
        String originalWindow = WebDriverRunner.getWebDriver().getWindowHandle();

        // Переключаемся на другую вкладку
        switchTo().window(1);

        SearchOrderPageSelenide searchOrderPageSelenide = Selenide.page(new SearchOrderPageSelenide());
        searchOrderPageSelenide.confirmAndSearch();

        // Возвращаемся на сохраненную вкладку
        //switchTo().window(originalWindow);
    }

    public void errorMessageDisplayed(String expectedError) {
        try {
            SearchOrderPageSelenide searchOrderPageSelenide = Selenide.page(new SearchOrderPageSelenide());
            searchOrderPageSelenide.setErrors(this.errors);
            searchOrderPageSelenide.errorMessageIsChecked(expectedError);
            //searchOrderPageSelenide.getSoftlyResult();
        } catch (Exception e) {
            System.out.println("Появилась Катча");
        }

    }
}