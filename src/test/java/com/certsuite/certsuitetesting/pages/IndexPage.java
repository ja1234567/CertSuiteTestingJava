package com.certsuite.certsuitetesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class IndexPage extends Page {

    By signinButton = By.id("sign-in");

    public IndexPage(WebDriver driver) {
        super(driver);
    }

    public final void GoToLogin(){ GoToLogin(10); }
    public final void GoToLogin(int ts) {
        if (WaitForX(this.signinButton, Duration.ofSeconds(ts))) {
            clickButton(driver.findElement(this.signinButton));
        }

    }
}