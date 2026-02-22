package org.example;

import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;

public class PobedaSearchPageSelenide extends PobedaHomePageSelenide {
    @FindBy(xpath = "(//*[@placeholder='Откуда'])[1]")
    SelenideElement fromField;

    @FindBy(xpath = "(//*[@placeholder='Куда'])[1]")
    SelenideElement toField;

    @FindBy(xpath = "(//*[@placeholder='Туда'])[1]")
    SelenideElement departureDate;

    @FindBy(xpath = "(//*[@placeholder='Обратно'])[1]")
    SelenideElement arrivalDate;

    @FindBy(xpath = "//button[contains(@class,'dp-1ye1u0p-root-root-root')]")
    SelenideElement searchButton;

    @FindBy(xpath = "(//*[contains(@class,'dp-1dr6zbu-root')])[7]")
    SelenideElement redBorderItem;

    @FindBy(className = "dp-ya8faq-root-root")
    private SelenideElement noThanksButtonPopup;

    private SoftAssertions softly;

    public void setSoftly(SoftAssertions softly) {
        this.softly = softly;
    }

    // Конструктор с инициализацией PageFactory
    public PobedaSearchPageSelenide() {
    }

    //Проверка полей блока Поиск
    public void verifySearchBlock() {
        fromField.shouldBe(visible, Duration.ofSeconds(10));
        fromField.shouldBe(visible.because("Отсутствует поле Откуда"));
        toField.shouldBe(visible.because("Отсутствует поле Куда"));
        departureDate.shouldBe(visible.because("Отсутствует поле Куда"));
        arrivalDate.shouldBe(visible.because("Отсутствует поле Дата вылета Обратно"));
    }

    public void enterToCity(String city, String city2) {
        fromField.setValue(city);
        toField.setValue(city2);
    }

    // Нажатие Кнопки Поиска
    public void clickSearch() {
        searchButton.click();
    }

    //Проверка, что около поля «Туда» появилась красная обводка
    public boolean hasRedBorderOnDepartureDate() {
        //Получаем значения для дальнейшего использования
        String borderColor = redBorderItem.getCssValue("border-color");
        return borderColor.contains("213, 0, 98") || borderColor.contains("#d50062");
        //    border-color: var(--dp-35);  d5 - сколько красного, 00 - сколького зеленого, 62 - сколько синего (это в 16-ти ричной системе), 213, 0, 98 - десятичная система
        //data-failed="true"
        //ожидаемый цвет в виде строки "rgb(213, 0, 98)"
    }

    public void closePromoPopup() {
        if (noThanksButtonPopup.isDisplayed()) {
            noThanksButtonPopup.click();
        }
    }
}