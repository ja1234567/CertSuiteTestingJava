package com.certsuite.certsuitetesting.pages.jobpages;

import com.certsuite.certsuitetesting.entities.Randomizer;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class CircuitsJobPage extends JobPage {

    By circuitsTable = By.id("circuit131-list");
    By addNewCircuitButton = By.id("addBtnInTable");
    By createCircuitCircuitNumberSelect = By.id("circuit-no-focus");
    By createCircuitSubmitButton = By.className("new-circuit-submit");
    By goBackToBoardsButton = By.id("closeBrdTop");
    By gotoBoardDetailsButton = By.className("gen129sm");

    // MaxZs Input
    By maxZsButton = By.className("jsZsMax");
    By maxZsValueInput = By.className("gen-modal-focus-control");

    // Modals
    By saveGenModalControl = By.id("saveGenControl");
    By modalValueInput = By.className("genModalControl");

    public CircuitsJobPage(WebDriver driver) {
        super(driver);
    }

    // Breadcrumbs are the 3 links above the circuits table for navigation. We use these because using the ids here failed. Selenium is unhappy with the a tag ids
    public final void ClickBreadcrumb(int bc) {
        while(true){
            try {
                var elements = driver.findElements(By.className("table-breadcrumb-link"));
                if ((elements.size() > 0)) {
                    clickButton(elements.get(bc));
                    break;
                }
            }
            catch (StaleElementReferenceException repeatLoop) {

            }
        }
    }

    public final boolean CreateCircuit(int circuitNumber) {
        try {
            WaitForX(this.circuitsTable);
            WaitForX(this.addNewCircuitButton);
            clickButton(driver.findElement(this.addNewCircuitButton));
            // Choose Circuit Number
            WaitForX(this.createCircuitCircuitNumberSelect);
            var selectPurpose = new Select(driver.findElement(this.createCircuitCircuitNumberSelect));
            selectPurpose.selectByIndex((circuitNumber - 1));
            WaitForX(this.createCircuitSubmitButton);
            clickButton(driver.findElement(this.createCircuitSubmitButton));
            sleep(2000);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public enum inputModalType {
        ddl,
        textarea,
        textmini,
    }

    public final Duration FillData(int circuitNumber, String inputField, inputModalType imt, String input)
        { return FillData(circuitNumber, inputField, imt, input, -1, -1); }
    public final Duration FillData(int circuitNumber, String inputField, inputModalType imt, String input, int rowNumber, int columnNumber) {
        WaitForX(circuitsTable);
        long start = System.currentTimeMillis();
        while(true){
            try {
                Actions actions = new Actions(driver);
                WebElement element;
                // Get the cell wanted, either via the cicuit and column names, or row and column numbers
                if (((rowNumber > 0)
                        && (columnNumber >= 0))) {
                    element = GetCell("" + circuitNumber, inputField, rowNumber, columnNumber);
                }
                else {
                    element = GetCell("" + circuitNumber, inputField);
                }

                // Get the div inside the cell which is clickable
                var insideElement = element.findElement(By.tagName("div"));
                if(insideElement != null){
                    actions.moveToElement(insideElement);
                    actions.perform();

                    clickButton(insideElement,element);
                }
                break;
            }
            catch (StaleElementReferenceException repeatLoop) {

            }
        }

        switch (imt)
        {
            case ddl ->
            {
                String listItem = ("li" + input);
                WaitForX(By.id(listItem));
                try
                {
                    clickButton(driver.findElement(By.id(listItem)));
                }
                catch (ElementClickInterceptedException ecie2)
                {
                    JavascriptExecutor jse = ((JavascriptExecutor) (driver));
                    jse.executeScript("arguments[0].click()", driver.findElement(By.id(listItem)));
                }
                // Sometimes clicking Done fails. If so, a WebDriverTimeoutException will be thrown.
                // If so, try again
                if (WaitForX(modalDoneButton, Duration.ofSeconds(3)))
                {
                    start = System.currentTimeMillis();
                    clickButton(driver.findElement(modalDoneButton));
                    WaitUntilGone(modalBackdropFade);
                    return Duration.ofMillis(System.currentTimeMillis() - start);
                }

                return Duration.ZERO;
            }
            case textarea ->
            {
                WaitForX(By.id("genModalControl"));
                WaitForX(By.tagName("textarea"));
                WebElement textareaControl = driver.findElement(By.id("genModalControl"));
                WebElement textarea = textareaControl.findElement(By.tagName("textarea"));
                int length = Integer.parseInt(textarea.getAttribute("maxlength"));
                textarea.sendKeys(Randomizer.RandomString(length));
                WaitForX(modalDoneButton);

                while(true){
                    try{
                        start = System.currentTimeMillis();
                        clickButton(driver.findElement(modalDoneButton));
                        WaitUntilGone(modalBackdropFade);
                        break;
                    }
                    catch (Exception repeatLoop)
                    {

                    }
                }
                return Duration.ofMillis(System.currentTimeMillis() - start);
            }
            case textmini ->
            {
                WaitForX(By.id("genModalControl"));
                WaitForX(By.className("input-mini"));
                WebElement textminiControl = driver.findElement(By.id("genModalControl"));
                WebElement textmini = textminiControl.findElement(By.className("input-mini"));
                textmini.sendKeys(Randomizer.RandomString(Integer.parseInt(textmini.getAttribute("maxlength"))));
                WaitForX(modalDoneButton);
                while(true){
                    try
                    {
                        start = System.currentTimeMillis();
                        clickButton(driver.findElement(modalDoneButton));
                        WaitUntilGone(modalBackdropFade);
                        break;
                    }
                    catch (Exception repeatLoop)
                    {

                    }
                }
                return Duration.ofMillis(System.currentTimeMillis() - start);
            }
        }
        return Duration.ZERO;
    }

    // TODO: Switch to use FillData method
    public final boolean CalculateMaxZs(int circuitNumber) {
        try {
            Actions action = new Actions(driver);
            WaitForX(circuitsTable);
            WebElement cell;
            cell = GetCell("" + circuitNumber, "Max Disconnection Time (s)");
            clickButton(cell);
            WaitForX(By.id("li1"));
            clickButton(driver.findElement(By.id("li1")));
            WaitForX(modalDoneButton);
            clickButton(driver.findElement(modalDoneButton));
            WaitUntilGone(modalBackdropFade);
            cell = GetCell("" + circuitNumber, "BS (EN)");
            clickButton(cell);
            WaitForX(By.id("li0"));
            clickButton(driver.findElement(By.id("li0")));
            WaitForX(modalDoneButton);
            clickButton(driver.findElement(modalDoneButton));
            WaitUntilGone(modalBackdropFade);
            cell = GetCell("" + circuitNumber, "Type");
            clickButton(cell);
            WaitForX(By.id("li0"));
            clickButton(driver.findElement(By.id("li0")));
            WaitForX(modalDoneButton);
            clickButton(driver.findElement(modalDoneButton));
            WaitUntilGone(modalBackdropFade);
            cell = GetCell("" + circuitNumber, "Rating");
            clickButton(cell);
            WaitForX(By.id("li4"));
            clickButton(driver.findElement(By.id("li4")));
            WaitForX(modalDoneButton);
            clickButton(driver.findElement(modalDoneButton));
            WaitUntilGone(modalBackdropFade);
            WebElement maxZsCell = GetCell("" + circuitNumber, "Max permitted Zs");
            String maxZsData = maxZsCell.getText();
            // TODO: Check MaxZs Value
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // No longer used
    public final boolean EnterValue(int circuitNumber, String columnName, String value) {
        try {
            WaitForX(circuitsTable);
            WebElement cell = GetCell("" + circuitNumber, columnName);
            clickButton(cell);
            sleep(2000);
            Actions action = new Actions(driver);
            action.sendKeys(value).perform();
            action.sendKeys(Keys.ENTER);
            sleep(2000);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final WebElement GetCell(String circuitName, String col){ return GetCell(circuitName, col, -1, -1); }
    public final WebElement GetCell(String circuitName, String col, int rowNumber, int columnNumber) {
        // Recreate table/tableRows each time to avoid StaleElementReferenceExceptions
        WaitForX(this.circuitsTable);
        // WebElement table = driver.findElement(circuitsTable);
        WaitForX(By.tagName("tbody"));
        WaitForX(By.tagName("tr"));
        // List<WebElement> tableRows = table.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        sleep(500);
        if (((rowNumber < 0) || (columnNumber < 0))) {
            int circuitNameIndex = this.GetColIndex("Circuit");
            if ((circuitNameIndex == -1)) {
                return null;
            }

            int colIndex = this.GetColIndex(col);
            if ((colIndex == -1)) {
                return null;
            }

            for (int i = 0; (i < driver.findElement(this.circuitsTable).findElement(By.tagName("tbody")).findElements(By.tagName("tr")).size()); i++) {
                List<WebElement> data = driver.findElement(this.circuitsTable).findElement(By.tagName("tbody"))
                                                .findElements(By.tagName("tr")).get(i).findElements(By.tagName("td"));
                if ((data.get(circuitNameIndex).getText().equals(circuitName))) {
                    return data.get(colIndex);
                }
            }
        }
        else {
            List<WebElement> data = driver.findElement(this.circuitsTable).findElement(By.tagName("tbody"))
                                                .findElements(By.tagName("tr")).get((rowNumber - 1)).findElements(By.tagName("td"));
            return data.get(columnNumber);
        }
        return null;
    }

    private final int GetColIndex(String headerTitle) {
        WaitForX(this.circuitsTable);
        // WebElement table = driver.findElement(circuitsTable);
        WaitForX(By.tagName("tr"));
        WaitForX(By.tagName("th"));
        // List<WebElement> tableRows = driver.findElement(circuitsTable).findElements(By.tagName("tr"));
        // List<WebElement> tableHeaders = driver.findElement(circuitsTable).findElements(By.tagName("tr"))[2].findElements(By.tagName("th"));
        // Reinitialize table/tableRows/tablesHeaders each time to avoid StaleElementReferenceExceptions
        int headerCount = 0;
        while(true){
            try {
                headerCount = driver.findElement(this.circuitsTable).findElements(By.tagName("tr")).get(2).findElements(By.tagName("th")).size();
                break;
            }
            catch (StaleElementReferenceException repeatLoop) {

            }
        }

        for (int i = 0; i < headerCount; i++) {
            while(true){
                try {
                    if (((driver.findElement(this.circuitsTable).findElements(By.tagName("tr")).get(2)
                                .findElements(By.tagName("th")).get(i).getAttribute("title").equals(headerTitle))
                            || (driver.findElement(this.circuitsTable).findElements(By.tagName("tr")).get(2)
                                      .findElements(By.tagName("th")).get(i).getText().equals(headerTitle)))) {
                        return i;
                    }
                    break;
                }
                catch (StaleElementReferenceException repeatLoop) {

                }
            }
        }
        return -1;
    }

    public final void GoBackToBoards() {
        clickButton(this.goBackToBoardsButton);
    }

    public final void gotoBoardDetails() {
        clickButton(this.gotoBoardDetailsButton);
    }
}