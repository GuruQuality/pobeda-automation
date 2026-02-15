package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
/*
Задание №2. Page Object. Инициирование поиска
1. Перейти на сайт pobeda.aero.
2. Убедиться, что сайт открылся:
а) текст заголовка страницы: Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы;
б) на странице есть логотип Победы.
3. Проскроллить страницу к блоку поиска билета и убедиться, что блок с поиском билета действительно отображается (есть поле Откуда, Куда, Дата вылета Туда, Дата вылета Обратно)
4. Выбрать (или ввести) следующие критерии поиска:
откуда – Москва (без выбора аэропорта) + нажать Enter
куда – Санкт-Петербург + нажать Enter.
5. Нажать кнопку «Поиск».
6. Убедиться, что около поля «Туда» появилась красная обводка.
 */
public class Pobeda_testEx2 {
    static WebDriver driver;
    static SoftAssertions softly = new SoftAssertions();


    @BeforeEach
    public void openDriver() {
        // 1. Настраиваем драйвер автоматически
        WebDriverManager.chromedriver().setup();

        // 2. Создаем опции
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        // 3. Создаем драйвер
        driver = new ChromeDriver();
        //driver.get("https://www.pobeda.aero/");
        driver.manage().timeouts().getPageLoadTimeout();//Дождаться прогрузки страницы
    }

    @Test
    public void testTitlePage() throws InterruptedException {
        //SoftAssertions softly = new SoftAssertions();
        // 1. Перейти на сайт
        PobedaHomePage pobedaPage = new PobedaHomePage(driver, softly);
        pobedaPage.open();
        pobedaPage.verifyURL();//Проверка, что нужный нам сайт открылся

        pobedaPage.verifyTitle("Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы");
        pobedaPage.isLogoDisplayed();
    }

    @Test
    public void testISearchBlock() throws InterruptedException {
        // 1. Перейти на сайт
        PobedaSearchPage pobedaPage = new PobedaSearchPage(driver, softly);
        pobedaPage.open();
        pobedaPage.verifySearchBlock();//появилось всплывающее окно с заголовками
        pobedaPage.enterToCity("Москва","Санкт-Петербург");//добавление городов в блок Поиска
        pobedaPage.closePromoPopup();
        pobedaPage.clickSearch();
        pobedaPage.hasRedBorderOnDepartureDate();
    }

    @AfterAll
    public static void closeDriverAndGetSoftlyAssert() {
        driver.quit();
        // ВСЕ проверки будут выполнены, даже если первые упали
        softly.assertAll(); // Здесь бросится исключение со ВСЕМИ ошибками
    }
}