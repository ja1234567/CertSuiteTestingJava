package com.certsuite.certsuitetesting.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public abstract class Page {

    protected WebDriver driver;
    WebDriverWait wait;
    protected DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    protected By currentJobsButton = By.id("jobButton");
    protected By adminButton = By.id("adminButton");
    protected By dismissModalButton = By.id("dismissHelpPopupBtn");
    protected By modalBackdropFade = By.className("modal-backdrop");
    protected By modalDoneButton = By.id("saveGenControl");
    protected By modalXButton = By.className("cancel-board-button");
    protected By cookiesOkButton = By.id("GDPRConsentBtn");
    protected By profileButton = By.xpath("//*[@id=\"topRightBtns\"]/button[2]");
    protected By logoutButton = By.id("log-out-button");
    protected By confirmLogoutButton = By.className("confirm-logout-submit");

    protected Function<By, Boolean> elementIsVisible = e -> 
            driver.findElements(e).size() > 0 &&
            driver.findElement(e).isDisplayed() && 
            driver.findElement(e).isEnabled();

    protected Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, (Duration.ofSeconds(20)));
    }

    // TODO: Test. WaitFors removed since Java conversion
    public final void Logout() {
        clickButton(this.driver.findElement(this.profileButton));
        clickButton(this.driver.findElement(this.logoutButton));
        clickButton(this.driver.findElement(this.confirmLogoutButton));
    }

    // Look for element for Duration, then give up
    // Use when element might not appear (like with modals that appear on first time)
    public final boolean WaitForX(By element){ return WaitForX(element, Duration.ofSeconds(10)); }
    public final boolean WaitForX(By element, Duration x)
    {
        if (elementIsVisible.apply(element)) return true;

        sleep(500);
        for (int i = 0; (i < 4); i++) {
            if (elementIsVisible.apply(element)) return true;
            sleep((x.toMillis() / 5));
        }
        return elementIsVisible.apply(element);
    }

    // Wait until invisible
    // Use when there is, for example a fade out that might obscure elements behind it
    // TODO: Test (rewritten since Java conversion)
    public final void WaitUntilGone(By element){ WaitUntilGone(element, 20); }
    public final void WaitUntilGone(By element, long seconds) {
        while(true){
            try {
                if(elementIsVisible.apply(element)){
                    for(int i = 0; i < 5; i++){
                        if(!driver.findElement(element).isDisplayed()) return;
                        sleep((seconds * 1000) / 5);
                    }
                }
            }
            catch (StaleElementReferenceException ignored) { }
            catch (NoSuchElementException nsee) {
                break;
            }
        }
    }

    // Scroll until an element that is off the page comes into view. Useful to avoid errors when trying to click an 
    // element that is not visible
    public final void ScrollIntoView(WebElement element) {
        ((JavascriptExecutor)(this.driver)).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public final boolean GotoAccountManagement() {
        try {
            sleep(2000);
            this.WaitForX(this.adminButton);
            Actions action = new Actions(this.driver);
            action.moveToElement(this.driver.findElement(this.adminButton)).perform();
            this.WaitForX(By.linkText("Account Management"));
            clickButton(this.driver.findElement(By.linkText("Account Management")));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final void GotoCurrentJobs() {
        clickButton(this.driver.findElement(this.currentJobsButton));
    }

    // TODO: Make more robust.
    // Possible point of failure, index bug?
    protected final WebElement GetTableCell(String tableRef, String FindString){ return GetTableCell(tableRef, FindString, -1); }
    protected final WebElement GetTableCell(String tableRef, String FindString, int targetColumn) {
        try {
            By currentTable = By.id(tableRef);
            this.WaitForX(currentTable);
            sleep(2000);
            WebElement table = this.driver.findElement(currentTable);
            List<WebElement> tableRows = table.findElements(By.tagName("tr"));
            List<WebElement> tableData;
            for (WebElement element : tableRows) {
                tableData = element.findElements(By.tagName("td"));
                if ((tableData.size() > 0)) {
                    for (WebElement iwe : tableData) {
                        if ((iwe.getText().equalsIgnoreCase(FindString))) {
                            if ((targetColumn == -1)) {
                                return iwe;
                            }
                            else {
                                return tableData.get(targetColumn);
                            }

                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            // Log Error??
            // Throw Error??
            return null;
        }
        return null;
    }

    // Dismiss cookies
    public final void DismissCookies(){ DismissCookies(10); }
    public final void DismissCookies(int seconds) {
        boolean modalAppeared = this.WaitForX(this.cookiesOkButton, Duration.ofSeconds(seconds));
        if (modalAppeared) {
            clickButton(this.driver.findElement(this.cookiesOkButton));
        }
    }

    // Close modal
    // Example of how to use waitForX method to check if element will appear
    public final void DismissModal(){ DismissModal(10); }
    public final void DismissModal(int seconds) {
        if (this.WaitForX(this.dismissModalButton, Duration.ofSeconds(seconds))) {
            // If don't show again checkbox appears, click
            var elements = this.driver.findElements(By.className("dismiss-help-popup"));
            if ((elements.size() > 0)) {
                try {
                    clickButton(elements.get(0));
                }
                catch (ElementClickInterceptedException ecie) {
                    JavascriptExecutor jse = ((JavascriptExecutor)(this.driver));
                    jse.executeScript("arguments[0].click()", elements.get(0));
                }
            }
            clickButton(this.driver.findElement(this.dismissModalButton));
        }
        this.WaitUntilGone(this.modalBackdropFade);
    }

    // Close modal, abnormal dismiss button
    public final void DismissModal(By dismissButton){ DismissModal(dismissButton, 10); }
    public final void DismissModal(By dismissButton, int seconds) {
        if (this.WaitForX(dismissButton, Duration.ofSeconds(seconds))) {
            clickButton(this.driver.findElement(dismissButton));
            this.WaitUntilGone(this.modalBackdropFade);
        }
    }

    // Use when you want to time something but sometimes a modal will appear before starting the job
    // eg: You want to time creating a report but sometimes it will show you the licence check modal before starting
    // When pressing preview report, wait for the licence modal, but if it doesn't appear, keep a copy of how long it took because
    // it would have been creating the report the whole time
    public final long TimeDismissModal(By dismissButton){ return TimeDismissModal(dismissButton, 10); }
    public final long TimeDismissModal(By dismissButton, int seconds) {
        long start = System.currentTimeMillis();
        if (this.WaitForX(dismissButton, Duration.ofSeconds(seconds))) {
            clickButton(this.driver.findElement(dismissButton));
            //this.driver.findElement(dismissButton).Click();
            this.WaitUntilGone(this.modalBackdropFade);
            return 0;
        }
        long stop = System.currentTimeMillis();
        return stop - start;
    }

    // Solid click handling for exceptions
    // Taking By not WebElement
    public final boolean clickButton(By button){ return clickButton(button, 10); }
    public final boolean clickButton(By button, int seconds){
        if (this.WaitForX(button, Duration.ofSeconds(seconds))) {
            int counter = 0;
            do
            {
                try {
                    clickButton(this.driver.findElement(button));
                    return true;
                }
                catch (StaleElementReferenceException sere) {
                    if ((counter > 10)) {
                        return false;
                    }
                    counter++;
                }
            } while(counter < 10);
        }
        return false;
    }

    // Solid click handling for exceptions
    // outerElement is used when the clickable element is centered within another element, and clicking the center of the outer 
    // element will actually click the element we want (eg: buttons within table cell)
    // TODO possibly handle scroll into view here
    public final void clickButton(WebElement element){
        clickButton(element, null);
    }
    public final void clickButton(WebElement element, WebElement outerElement) {
        try {
            element.click();
        }
        catch (ElementClickInterceptedException ecie) {
            if ((outerElement != null)) {
                try {
                    outerElement.click();
                }
                catch (ElementClickInterceptedException ecie2) {
                    JavascriptExecutor jse = ((JavascriptExecutor)(this.driver));
                    jse.executeScript("arguments[0].click()", element);
                }
            }
            else {
                JavascriptExecutor jse = ((JavascriptExecutor)(this.driver));
                jse.executeScript("arguments[0].click()", element);
            }
        }
    }

    public void sleep(Duration time){ sleep(time.toMillis()); }
    public void sleep(long milliseconds){
        try{ 
            Thread.sleep(milliseconds);
        }
        catch( InterruptedException ie)
        { 
            ie.printStackTrace();
        }
    }
}