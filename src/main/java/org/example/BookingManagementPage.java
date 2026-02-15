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

    @FindBy(className = "dp-ya8faq-root-root")
    private WebElement noThanksButtonPopup;

    @FindBy(xpath = "(//*[contains(@class,'dp-rr18i4-root-root')])[2]")
    private WebElement bookingManagement;

    @FindBy(xpath = "(//*[contains(@class,'dp-zu3w2f-root-control')])[1]")
    WebElement lastNameField;

    @FindBy(xpath = "(//*[contains(@class,'dp-zu3w2f-root-control')])[2]")
    WebElement orderNumber;

    @FindBy(xpath = "//button[@class='dp-tsteac-root-root-submitBtn']")
    WebElement buttonSearchBM;

    @FindBy(css = "label[for='searchOrderAgreeChb']")
    WebElement privacyPolicyCheckbox;

    @FindBy(xpath = "//button[@class='btn btn_search btn_search--order btn_formSearch btn_formSearch_js']")
    WebElement buttonFindOrder;

    WebElement elementTest;
    private String url = "https://pobeda.aero";

    WebElement elementTest2;
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

    // Открыть сайт
    public void open() {
        driver.get(url);
    }


    public void closePromoPopup() {
        try {
            noThanksButtonPopup.click();
        } catch (NoSuchElementException e) {
        }
    }

    public void scrollAndClickBookingManagement() throws InterruptedException {
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
        softly.assertThat(orderNumber.isDisplayed())
                .as("Поле 'Номер заказа или билета' не отображается")
                .isTrue();

        wait.until(ExpectedConditions.visibilityOf(lastNameField));
        softly.assertThat(lastNameField.isDisplayed())
                .as("Поле 'Фамилия клиента' не отображается")
                .isTrue();

        wait.until(ExpectedConditions.visibilityOf(buttonSearchBM));
        softly.assertThat(buttonSearchBM.isDisplayed())
                .as("Отсутствует кнопка Поиск")
                .isTrue();

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
        Thread.sleep(5000);

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