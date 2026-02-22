package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Задание №3. Page Object. Результаты поиска
1. Перейти на сайт pobeda.aero.
2. Убедиться, что сайт открылся:
а) текст заголовка страницы: Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы;
б) на странице есть логотип Победы.
3. Проскроллить страницу чуть ниже и кликнуть на пункт «Управление бронированием».
4. Убедиться, что открылась необходимая страница:
а) есть поле «Номер заказа или билета»;
б) есть поле «Фамилия клиента»;
в) есть кнопка «Поиск».
5. Ввести в поля ввода данные:
номер заказа – XXXXXX, фамилия – Qwerty
и нажать кнопку «Поиск».
6. Убедиться, что в новой вкладке на экране отображается текст ошибки «Заказ с указанными параметрами не найден».
 */
public class Pobeda_testEx3 {
    static WebDriver driver;
    static SoftAssertions softly = new SoftAssertions();

    @BeforeAll
    public static void openDriver() {
        // 1. Настраиваем драйвер автоматически
        WebDriverManager.chromedriver().setup();
        // 2. Создаем опции
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"))//Убирает "Chrome is being controlled..
                .setExperimentalOption("useAutomationExtension", false)//Отключает расширение автоматизации
                .addArguments("--disable-blink-features=AutomationControlled")//Прячет автоматизацию от JavaScript
                .addArguments("--disable-infobars")
                .addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addArguments("--start-maximized")
                .addArguments("--disable-notifications")//Отключает всплывающие попапы
                .addArguments("--remote-allow-origins=*");//Разрешает удаленные подключения к драйверу
        // 3. Создаем драйвер
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    @Test
    public void testTitlePage() {
        // 1. Перейти на сайт
        PobedaHomePage pobedaPage = new PobedaHomePage(driver, softly);
        pobedaPage.open();
        pobedaPage.verifyURL();//Проверка, что нужный нам сайт открылся
        pobedaPage.verifyTitle("Авиакомпания «Победа» - купить авиабилеты онлайн, дешёвые билеты на самолёт, прямые и трансферные рейсы с пересадками");
        pobedaPage.isLogoDisplayed();
    }

    @Test
    public void testISearchResults() throws InterruptedException {
        // 1. Перейти на сайт
        BookingManagementPage pobedaPage = new BookingManagementPage(driver, softly);
        pobedaPage.open();
        pobedaPage.scrollAndClickBookingManagement();// Скролл и клик на пункт «Управление бронированием»
        pobedaPage.verifyURL();
        pobedaPage.BookingManagementIsDisplayed();// Проверка, что открылась нужная страница
        pobedaPage.fillSearchFormAndSubmit();// Заполнение формы и ее отправление
        pobedaPage.errorMessageDisplayed("Заказ с указанными параметрами не найден");// Проверка отображения ошибки
    }

    @AfterAll
    public static void closeDriverAndGetSoftlyAssert() {
        driver.quit();
        // ВСЕ проверки будут выполнены, даже если первые упали
        softly.assertAll(); // Здесь бросится исключение со ВСЕМИ ошибками
    }
}