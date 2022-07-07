package com.certsuite.certsuitetesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage extends Page {

    By usernameInput = By.id("userName");
    By passwordInput = By.id("password");
    By btnLogin = By.cssSelector(".loginBtn");
    By btnSkipLicenceCheck = By.id("btnCnfFail");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public final void Login(String username, String password) {
        WaitForX(usernameInput);
        WaitForX(passwordInput);
        WaitForX(btnLogin);
        driver.findElement(this.usernameInput).sendKeys(username);
        driver.findElement(this.passwordInput).sendKeys(password);
        clickButton(driver.findElement(this.btnLogin));

        // Wait in case invoice warning modal appears
        if (WaitForX(this.btnSkipLicenceCheck, Duration.ofSeconds(5))) {
            clickButton(driver.findElement(this.btnSkipLicenceCheck));
            WaitUntilGone(modalBackdropFade);
        }
    }
}