package org.example;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SearchOrderPage extends PobedaHomePage {
    private WebDriver driver;
    private WebDriverWait wait;


    @FindBy(css = "label[for='searchOrderAgreeChb']")
    WebElement privacyPolicyCheckbox;

    @FindBy(xpath = "//button[@class='btn btn_search btn_search--order btn_formSearch btn_formSearch_js']")
    WebElement buttonFindOrder;

    @FindBy(css = "div.message_error")
    WebElement actualErrorMessage;

    WebElement elementTest;
    private String url = "https://pobeda.aero";

    WebElement elementTest2;
    private String urlBookingManagement = "https://www.flypobeda.ru/services/booking-management";

    private SoftAssertions softly;

    // Конструктор с инициализацией PageFactory
    public SearchOrderPage(WebDriver driver, SoftAssertions sofly) {
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


    public void confirmAndSearch() throws InterruptedException {
        wait.withTimeout(Duration.ofSeconds(60)).until(ExpectedConditions.visibilityOf(privacyPolicyCheckbox));
        removeBannerFromDOM();
        privacyPolicyCheckbox.click();
        buttonFindOrder.click();
    }

    public void removeBannerFromDOM() {
        // querySelector аналог driver.findElement(By.className("common-warning"));
        ((JavascriptExecutor) driver).executeScript(
                "document.querySelector('.common-warning')?.remove();"//С ? Элемента нет -> ничего не делает, ошибки нет
        );
        System.out.println("✓ Баннер уничтожен");
    }

    public void errorMessageIsChecked(String expectedError) {
        String errorText = actualErrorMessage.getText();
        wait.withTimeout(Duration.ofSeconds(60));
        softly.assertThat(errorText)
                .as("Текст ошибки не совпадает или отсутствует!", expectedError, errorText)
                .isEqualTo(expectedError);
    }
}