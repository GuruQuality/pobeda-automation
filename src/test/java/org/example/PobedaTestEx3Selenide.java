package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

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
public class PobedaTestEx3Selenide {
    static SoftAssertions softly = new SoftAssertions();

    @BeforeAll
    public static void openDriver() {
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
        pobedaPage.verifyTitle("Авиакомпания «Победа» - купить авиабилеты онлайн, дешёвые билеты на самолёт, прямые и трансферные рейсы с пересадками");
        pobedaPage.isLogoDisplayed();
    }

    @Test
    public void testISearchResults() throws InterruptedException {
        // 1. Перейти на сайт
        BookingManagementPageSelenide pobedaPage = open("https://pobeda.aero", BookingManagementPageSelenide.class);
        pobedaPage.setSoftly(softly);
        // 1. Перейти на сайт
        pobedaPage.closePromoPopup();
        pobedaPage.scrollAndClickBookingManagement();// Скролл и клик на пункт «Управление бронированием»
        //pobedaPage.verifyURL();
        pobedaPage.BookingManagementIsDisplayed();// Проверка, что открылась нужная страница
        pobedaPage.fillSearchFormAndSubmit();// Заполнение формы и ее отправление
        pobedaPage.errorMessageDisplayed("Заказ с указанными параметрами не найден");// Проверка отображения ошибки
        softly = pobedaPage.getSoftlyResult();
    }

    @AfterAll
    public static void closeDriverAndGetSoftlyAssert() {
        // ВСЕ проверки будут выполнены, даже если первые упали
        softly.assertAll(); // Здесь бросится исключение со ВСЕМИ ошибками
    }
}