package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
/*
Общее описание к задачам
Написать три автотеста, связанных с функционалом сайта «pobeda.aero». При разработке автотестов необходимо использовать паттерны Page Object Model и Page Factory, а также расставить явные и неявные ожидания там, где они нужны.
Критерии выполнения практического задания
Должно быть 4 java-файла с Page Object Model + Page Factory и 1 файл с самим тестом. За выполнение этого пункта – 2 балла. Если файлов меньше – 1 балл.
В каждом файле с POM у каждого WebElement должна быть аннотация @FindBy и функция с методом initElements(). За выполнение этого пункта – 1 балл. Если хотя бы в одном файле такого нет – 0 баллов.
В каждом файле с POM названия переменных несут смысл их назначения, названия функций также несут смысловую нагрузку. Если все это выполнено – 2 балла. Если пара переменных или названий функций не несут смысла их назначения, то оценка – 1 балл. Если больше двух – 0 баллов.
В тестовом файле есть 3 тестовых метода, один Before метод и один After метод. За выполнение этого пункта – 1 балл.

Задание №1. Page Object. Всплывающее окно
1. Перейти на сайт pobeda.aero.
2. Убедиться, что сайт открылся:
а) текст заголовка страницы: Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы;
б) на странице есть логотип Победы.
3. Навести мышку на пункт «Информация».
4. Убедиться, что появилось всплывающее окно, которое содержит следующие заголовки: «Подготовка к полету», «Полезная информация», «О компании».
 */
public class Pobeda_testEx1 {
    static WebDriver driver;
    static SoftAssertions softly = new SoftAssertions();

    @BeforeAll
    public static void openDriver() {
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
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));//Дождаться прогрузки страницы
    }

    @Test
    public void testTitlePage() throws InterruptedException {
        // 1. Перейти на сайт
        PobedaHomePage pobedaPage = new PobedaHomePage(driver, softly);
        pobedaPage.open();
        pobedaPage.verifyURL();//Проверка, что нужный нам сайт открылся

        pobedaPage.verifyTitle("Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы");
        pobedaPage.isLogoDisplayed();
    }

    @Test
    public void testInformationPopup() {
        // 1. Перейти на сайт
        PobedaHomePage pobedaPage = new PobedaHomePage(driver, softly);
        pobedaPage.open();
        pobedaPage.moveToInformationMenu();// Навести мышку на пункт «Информация»
        pobedaPage.verifyPopupHeaders();//появилось всплывающее окно с заголовками
    }

    @AfterAll
    public static void closeDriverAndGetSoftlyAssert() {
        driver.quit();
        // ВСЕ проверки будут выполнены, даже если первые упали
        softly.assertAll(); // Здесь бросится исключение со ВСЕМИ ошибками
    }
}