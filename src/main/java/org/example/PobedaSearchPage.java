package org.example;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PobedaSearchPage extends PobedaHomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "(//*[@placeholder='Откуда'])[1]")
    WebElement fromField;

    @FindBy(xpath = "(//*[@placeholder='Куда'])[1]")
    WebElement toField;

    @FindBy(xpath = "(//*[@placeholder='Туда'])[1]")
    WebElement departureDate;


    @FindBy(xpath = "(//*[@placeholder='Обратно'])[1]")
    WebElement arrivalDate;

    @FindBy(xpath = "//button[contains(@class,'dp-1ye1u0p-root-root-root')]")
    WebElement searchButton;

    @FindBy(xpath = "(//*[contains(@class,'dp-1dr6zbu-root')])[7]")
    WebElement redBorderItem;

    @FindBy(className = "dp-ya8faq-root-root")
    private WebElement noThanksButtonPopup;

    private String url = "https://pobeda.aero";

    private SoftAssertions softly;

    // Конструктор с инициализацией PageFactory
    public PobedaSearchPage(WebDriver driver, SoftAssertions sofly) {
        this.driver = driver;
        this.softly = sofly;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //Метод initElements будет создавать все параметры типа WebbElements
        PageFactory.initElements(driver, this);
    }

    // Открыть сайт
    public void open() {
        driver.get(url);
    }

    //Проверка полей блока Поиск
    public void verifySearchBlock() {
        wait.until(ExpectedConditions.visibilityOf(fromField));
        softly.assertThat(fromField.isDisplayed())
                .as("Отсутствует поле Откуда")
                .isTrue();
        softly.assertThat(toField.isDisplayed())
                .as("Отсутствует поле Куда")
                .isTrue();
        softly.assertThat(departureDate.isDisplayed())
                .as("Отсутствует поле Дата вылета Туда")
                .isTrue();
        softly.assertThat(arrivalDate.isDisplayed())
                .as("Отсутствует поле Дата вылета Обратно")
                .isTrue();
    }

    public void enterToCity(String city, String city2) {
        fromField.clear();
        fromField.sendKeys(city);
        toField.clear();
        toField.sendKeys(city2);
    }

    // Нажатие Кнопки Поиска
    public void clickSearch() {
        searchButton.click();
    }

    //Проверка, что около поля «Туда» появилась красная обводка
    public boolean hasRedBorderOnDepartureDate() {
        List<WebElement> webElementList = driver.findElements(By.xpath("//*[contains(@class,'dp-1dr6zbu-root')]"));
        String borderColor = redBorderItem.getCssValue("border-color");
        String fromFieldHtml = redBorderItem.getAttribute("outerHTML");
        return borderColor.contains("213, 0, 98") || borderColor.contains("#d50062");
        //    border-color: var(--dp-35);  d5 - сколько красного, 00 - сколького зеленого, 62 - сколько синего (это в 16-ти ричной системе), 213, 0, 98 - десятичная система
        //data-failed="true"
        //ожидаемый цвет в виде строки "rgb(213, 0, 98)"
    }
    public void closePromoPopup(){
        try {
            noThanksButtonPopup.click();
        }catch (NoSuchElementException e){
        }
    }
}