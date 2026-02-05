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

    @FindBy(xpath = "//title")
    WebElement pageTitle;

    @FindBy(xpath = "//*[contains(@class,'dp-1a6miki-root-root-root')]/*[1]")
    WebElement logo;

    @FindBy(xpath = "//*[contains(@class,'dp-1x07rlv-lottie')]/*[1]")
    WebElement logo2;

    @FindBy(xpath = "//a[@href='/information']")
    WebElement informationMenu;

    @FindBy(xpath = "//a[@href='/information#flight']")
    WebElement flightPreparation;

    @FindBy(xpath = "//a[@href='/information#useful']")
    WebElement usefulInfo;

    @FindBy(xpath = "//a[@href='/information#company']")
    WebElement aboutCompany;

    private String url = "https://pobeda.aero";

    private SoftAssertions softly;

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
        // 4. Получить внутренний HTML
//        String innerHtml = logo2.getAttribute("innerHTML");
//        String outerHtml = logo2.getAttribute("outerHTML");
//        System.out.println("Внутренний HTML: " + innerHtml);
//        System.out.println("Наружний HTML: " + outerHtml);
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
        softly.assertThat(flightPreparation.isDisplayed())
                .as("Заголовок 'Подготовка к полету' не отображается")
                .isTrue();
        softly.assertThat(usefulInfo.isDisplayed())
                .as("Заголовок 'Полезная информация' не отображается")
                .isTrue();
        softly.assertThat(aboutCompany.isDisplayed())
                .as("Заголовок 'О компании' не отображается")
                .isTrue();
    }
}