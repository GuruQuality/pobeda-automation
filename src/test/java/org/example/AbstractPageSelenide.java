package org.example;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementShould;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public abstract class AbstractPageSelenide {
    protected SoftAssertions softly;
    protected List<AssertionError> errors = new ArrayList<>();

    public void setSoftly(SoftAssertions softly) {
        this.softly = softly;
    }

    public void setErrors(List<AssertionError> errors) {
        this.errors = errors;
    }

    public SoftAssertions getSoftlyResult() {
        // Добавляем все ошибки в SoftAssertions
        assertSoftly(softly -> {
            errors.forEach(error -> {
                softly.fail("Найдена ошибка: " + error.getMessage());
            });
        });
        return softly;
    }

    public void softAssert(Runnable task) {
        try {
            task.run();
        } catch (ElementShould e) {
            errors.add(e);
            //System.out.println(e.getMessage());
        } catch (Throwable e) {
            errors.add(new AssertionError(e));
            //System.out.println("Ошибка при выполнении: " + e.getMessage());
        }
    }
}