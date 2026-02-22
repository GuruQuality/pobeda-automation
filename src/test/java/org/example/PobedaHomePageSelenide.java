package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.actions;
import static com.codeborne.selenide.Selenide.webdriver;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class PobedaHomePageSelenide extends AbstractPageSelenide{

    @FindBy(xpath = "//canvas")
    SelenideElement logo2;

    @FindBy(xpath = "//a[@href='/information']")
    SelenideElement informationMenu;

    @FindBy(xpath = "//a[@href='/information#flight']")
    SelenideElement flightPreparation;

    @FindBy(xpath = "//a[@href='/information#useful']")
    SelenideElement usefulInfo;

    @FindBy(xpath = "//a[@href='/information#company']")
    SelenideElement aboutCompany;

    public String url = "https://www.flypobeda.ru/";

    //Константы Заголовков
    private static final String EXPECTED_FLIGHT_PREP = "Подготовка к полёту";
    private static final String EXPECTED_USEFUL_INFO = "Полезная информация";
    private static final String EXPECTED_ABOUT_COMPANY = "О компании";

    // Конструктор по умолчанию (без параметров)
    public PobedaHomePageSelenide() {
    }

    // Открыть сайт
    public void open() {
        Selenide.open(url);
    }

    // Получить заголовок страницы
    public String getTitle() {
        return Selenide.title();
    }

    // Проверка заголовка (простая версия)
    public void verifyTitle(String expectedTitle) {
        String actualTitle = Selenide.title();
        softly.assertThat(actualTitle)
                .as("Заголовок не совпадает!", expectedTitle, actualTitle)
                .isEqualTo(expectedTitle);
    }

    // Проверка отображения логотипа
    public void isLogoDisplayed() {
        try {
            logo2.should(visible, Duration.ofSeconds(10));
        } catch (AssertionError e) {
            softly.fail("Логотип Победы не отображается на странице", e);
        }
    }

    // Проверка URL
    public void verifyURL() {
        String actualUrl = WebDriverRunner.url();
        softly.assertThat(actualUrl)
                .as("URL не одинаковые")
                .isEqualTo(url);
    }

    public void moveToInformationMenu() {
        informationMenu.shouldBe(visible);
        actions().moveToElement(informationMenu).perform();
    }

    //Проверка заголовков в Popup "Информация"
    public void verifyPopupHeaders() {
        assertSoftly(softly -> {
            // Selenide сам выбросит сообщение, но его нельзя кастомизировать
            flightPreparation.shouldHave(text(EXPECTED_FLIGHT_PREP));
            usefulInfo.shouldHave(text(EXPECTED_USEFUL_INFO));
            aboutCompany.shouldHave(text(EXPECTED_ABOUT_COMPANY));
        });
    }
}
