package org.example;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BookingManagementPage extends PobedaHomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "[data-testid='ads-popup-close-icon']")
    private WebElement noThanksButtonPopup;

    @FindBy(xpath = "//a[contains(@class, 'root-root') and contains(text(), 'Управление бронированием')]")
    private WebElement bookingManagement;

    @FindBy(css = "input[placeholder='Фамилия клиента']")
    WebElement lastNameField;

    @FindBy(css = "input[placeholder='Номер бронирования или билета']")
    WebElement orderNumber;

    @FindBy(xpath = "//button[contains(@class,'root-root-submitBtn')]")
    WebElement buttonSearchBM;

    private String urlBookingManagement = "https://www.flypobeda.ru/services/booking-management";

    private SoftAssertions softly;

    private String originalWindow;

    // Конструктор с инициализацией PageFactory
    public BookingManagementPage(WebDriver driver, SoftAssertions sofly) {
        this.driver = driver;
        this.softly = sofly;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        //Метод initElements будет создавать все параметры типа WebbElements
        PageFactory.initElements(driver, this);
    }

    // Метод для "обновления" страницы после переключения окна
    public void refreshPage() {
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(url);
    }


    public void closePromoPopup() {
        try {
            noThanksButtonPopup.click();
        } catch (NoSuchElementException e) {
            System.out.println("Промо-поп-ап не отображается: " + e.getMessage());
        }
    }

    public void scrollAndClickBookingManagement() {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));//Дождаться прогрузки страницы
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", bookingManagement);
        bookingManagement.click();
    }

    // Проверка URL
    public void verifyURL() {
        String actualUrl = driver.getCurrentUrl();
        softly.assertThat(actualUrl)
                .as("URL не одинаковые")
                .isEqualTo(urlBookingManagement);
    }

    //Проверка наличия полей «Номер заказа или билета», «Фамилия клиента» и кнопки «Поиск»
    public void BookingManagementIsDisplayed() {
        //Проверка заголовков в Popup "Информация"
        wait.until(ExpectedConditions.visibilityOf(orderNumber));
        String actualPlaceholderOrderNumberText = orderNumber.getAttribute("placeholder");
        softly.assertThat(actualPlaceholderOrderNumberText)
                .as("Поле 'Номер заказа или билета' не отображается")
                .isEqualTo("Номер бронирования или билета");
        wait.until(ExpectedConditions.visibilityOf(lastNameField));
        String actualPlaceholderLastNameField = lastNameField.getAttribute("placeholder");
        softly.assertThat(actualPlaceholderLastNameField)
                .as("Поле 'Фамилия клиента' не отображается")
                .isEqualTo("Фамилия клиента");
        wait.until(ExpectedConditions.visibilityOf(buttonSearchBM));
        String actualPlaceholderButtonSearchBMText = buttonSearchBM.getText();
        softly.assertThat(actualPlaceholderButtonSearchBMText)
                .as("Кнопка 'ПОИСК' не отображается")
                .isEqualTo("ПОИСК");
    }

    public void fillSearchFormAndSubmit() throws InterruptedException {
        refreshPage();
        wait.until(ExpectedConditions.visibilityOf(orderNumber));
        orderNumber.sendKeys("XXXXXX");
        wait.until(ExpectedConditions.visibilityOf(lastNameField));
        lastNameField.sendKeys("Qwerty");
        buttonSearchBM.click();

        // Сохраняем текущую вкладку
        originalWindow = driver.getWindowHandle();
        wait.withTimeout(Duration.ofSeconds(5));

        // Ждем появления новой вкладки
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        // Переключаемся на новую вкладку
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        SearchOrderPage searchOrderPage = new SearchOrderPage(driver, softly);
        searchOrderPage.confirmAndSearch();
    }

    public void errorMessageDisplayed(String expectedError) {
       try {
           SearchOrderPage searchOrderPage = new SearchOrderPage(driver, softly);
           searchOrderPage.errorMessageIsChecked(expectedError);
       }catch (Exception e){
           System.out.println("Появилась Катча");
       }
    }
}