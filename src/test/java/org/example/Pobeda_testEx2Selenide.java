package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.Selenide.open;

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
public class Pobeda_testEx2Selenide {
    SoftAssertions softly = new SoftAssertions();

    @Before
    public void openDriver() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        System.setProperty("org.openqa.selenium.level", "OFF");
        System.setProperty("webdriver.chrome.silentOutput", "true");
    }

    @Test
    public void testTitlePage() throws InterruptedException {
        // 1. Перейти на сайт
        PobedaHomePageSelenide pobedaPage = open("https://pobeda.aero", PobedaHomePageSelenide.class);
        pobedaPage.setSoftly(softly);
        pobedaPage.verifyURL();//Проверка, что нужный нам сайт открылся
        pobedaPage.verifyTitle("Авиакомпания «Победа» - купить билеты на самолёт дешево онлайн, прямые и трансферные рейсы");
        pobedaPage.isLogoDisplayed();
    }

    @Test
    public void testISearchBlock() throws InterruptedException {
        // 1. Перейти на сайт
        PobedaSearchPageSelenide pobedaSearchPage = open("https://pobeda.aero", PobedaSearchPageSelenide.class);
        pobedaSearchPage.setSoftly(softly);
        pobedaSearchPage.verifySearchBlock();//появилось всплывающее окно с заголовками
        pobedaSearchPage.enterToCity("Москва", "Санкт-Петербург");//добавление городов в блок Поиска
        pobedaSearchPage.closePromoPopup();
        pobedaSearchPage.clickSearch();
        pobedaSearchPage.hasRedBorderOnDepartureDate();
        //Thread.sleep(30000);
    }

    @After
    public void closeDriverAndGetSoftlyAssert() {
        // ВСЕ проверки будут выполнены, даже если первые упали
        softly.assertAll(); // Здесь бросится исключение со ВСЕМИ ошибками
    }
}