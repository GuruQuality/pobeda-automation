package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.assertj.core.api.SoftAssertions;

import java.time.Duration;

public class PobedaHomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//canvas")
    WebElement logo2;

    @FindBy(xpath = "//a[@href='/information']")
    WebElement informationMenu;

    @FindBy(xpath = "//a[@href='/information#flight']")
    WebElement flightPreparation;

    @FindBy(xpath = "//a[@href='/information#useful']")
    WebElement usefulInfo;

    @FindBy(xpath = "//a[@href='/information#company']")
    WebElement aboutCompany;

    public String url = "https://pobeda.aero";

    private SoftAssertions softly;

    //Константы Заголовков
    private static final String EXPECTED_FLIGHT_PREP = "Подготовка к полёту";
    private static final String EXPECTED_USEFUL_INFO = "Полезная информация";
    private static final String EXPECTED_ABOUT_COMPANY = "О компании";

    // Конструктор по умолчанию (без параметров)
    public PobedaHomePage() {
    }

    // Конструктор с инициализацией PageFactory
    public PobedaHomePage(WebDriver driver, SoftAssertions sofly) {
        this.driver = driver;
        this.softly = sofly;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        //Метод initElements будет создавать все параметры типа WebbElements
        PageFactory.initElements(driver, this);
    }

    // Открыть сайт
    public void open() {
        driver.get(url);
    }

    // Получить заголовок страницы
    public String getTitle() {
        return driver.getTitle();
    }

    // Проверка заголовка (простая версия)
    public void verifyTitle(String expectedTitle) {
        String actualTitle = getTitle();
        softly.assertThat(actualTitle)
                .as("Заголовок не совпадает!", expectedTitle, actualTitle)
                .isEqualTo(expectedTitle);
    }

    // Проверка отображения логотипа
    public void isLogoDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logo2));
            softly.assertThat(logo2.isDisplayed())
                    .as("Логотип Победы отображается на странице")
                    .isTrue();
            System.out.println("Логотип Победы отображается на странице");
        } catch (TimeoutException e) {
            softly.assertThat(false)
                    .as("Логотип Победы не отображается на странице. Причина: " + e.getRawMessage())
                    .isTrue();
        }
    }

    // Проверка URL
    public void verifyURL() {
        String actualUrl = driver.getCurrentUrl();
        softly.assertThat(actualUrl)
                .as("URL не одинаковые")
                .isEqualTo(url);
    }

    public void moveToInformationMenu() {
        wait.until(ExpectedConditions.visibilityOf(informationMenu));
        new Actions(driver)
                .moveToElement(informationMenu)
                .perform();
    }

    //Проверка заголовков в Popup "Информация"
    public void verifyPopupHeaders() {
        wait.until(ExpectedConditions.visibilityOf(flightPreparation));
        softly.assertThat(flightPreparation.getText())
                .as("Проверка заголовка '%s'", EXPECTED_FLIGHT_PREP)
                .isEqualTo(EXPECTED_FLIGHT_PREP);
        softly.assertThat(usefulInfo.getText())
                .as("Проверка заголовка '%s'", EXPECTED_USEFUL_INFO)
                .isEqualTo(EXPECTED_USEFUL_INFO);
        softly.assertThat(aboutCompany.getText())
                .as("Проверка заголовка '%s'", EXPECTED_ABOUT_COMPANY)
                .isEqualTo(EXPECTED_ABOUT_COMPANY);
    }
}