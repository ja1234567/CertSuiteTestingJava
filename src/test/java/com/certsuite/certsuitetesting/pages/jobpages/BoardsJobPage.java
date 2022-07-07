package com.certsuite.certsuitetesting.pages.jobpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

public class BoardsJobPage extends JobPage {

    public By boardsTable = By.id("board90-list");
    public By closeCircuitsModalButton = By.className("new-circuits-submit");

    protected By addNewButton = By.id("addBtnInTable");
    By createBoardBoardNameInput = By.id("board-name-focus");
    By createBoardBoardTypeRadioButtonTitle = By.id("brdUnitTypeTitle");
    By createBoardSubmitButton = By.className("new-board-submit");
    By cancelCreateBoardButton = By.id("btnCnfFail");
    By addBoardButton = By.id("addBtnInTable");
    By addCircuitsNoOfPhasesDDL = By.id("circuit-no-1phase-focus-batch");
    By selectCircuitTemplateDDL = By.id("circuit-type-1phase-focus-batch");

    // Inside Board
    By boardLocationInput = By.name("1*6*boardLocation*0*129*2");
    By designationDLL = By.name("1*6*boardDesignation*0*129*7");
    By nominalVoltageInput = By.name("1*6*boardNominalVolt*0*129*14");
    By circuitsButton = By.id("Gen*131*Circuits");
    By circuitsTable = By.id("circuit131-list");

    // Inside Circuit
    By circuitPhaseInput = By.name("1*8*circuitPhase*0*132*7");
    By maxDiscTimeDDL = By.name("1*8*circuitMaxDiscTimesPerm*0*132*11");
    By OPDTypeDDL = By.name("1*8*circuitOPDTypeNo*0*132*14");

    public BoardsJobPage(WebDriver driver) {
        super(driver);
    }

    public final boolean FindPreExistingCircuit(String reference) {
        try {
            // Open Job
            WaitForX(this.circuitsTable);
            sleep(2000);
            WebElement table = driver.findElement(this.circuitsTable);
            List<WebElement> tableRows = table.findElements(By.tagName("tr"));
            List<WebElement> tableData;
            for (WebElement element : tableRows) {
                tableData = element.findElements(By.tagName("td"));
                if ((tableData.size() > 0)) {
                    String circuitNo = tableData.get(0).getText();
                    String circuitDescription = tableData.get(1).getText();
                    if (((reference.equalsIgnoreCase(circuitNo)) || (reference.equalsIgnoreCase(circuitDescription))))
                    {
                        var columns = element.findElements(By.tagName("a"));
                        try {
                            clickButton(columns.get(0));
                            return true;
                        }
                        catch (Exception e) {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final void GotoCircuitsOfThisBoard() {
        WaitForX(this.circuitsButton);
        clickButton(driver.findElement(this.circuitsButton));
    }

    // TODO: Get rid of this?
    public final boolean EnterCircuitDetails(int maxDiscTime, int OPDType) {
        try {
            /// / Enter Board Location
            // WaitForX(circuitPhaseInput);
            // driver.findElement(circuitPhaseInput).Clear();
            // driver.findElement(circuitPhaseInput).SendKeys(phase);
            // Enter Board Designation
            WaitForX(this.maxDiscTimeDDL);
            var selectReg = new Select(driver.findElement(this.maxDiscTimeDDL));
            selectReg.selectByIndex(maxDiscTime);
            String reg1 = selectReg.getFirstSelectedOption().getText();
            // Enter Board Designation
            WaitForX(this.OPDTypeDDL);
            selectReg = new Select(driver.findElement(this.OPDTypeDDL));
            selectReg.selectByIndex(OPDType);
            String reg2 = selectReg.getFirstSelectedOption().getText();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // TODO: Get rid of this? like above
    // Match return structure with input params of EnterCircuitDetails()
    public final String[] GetCircuitDetails() {
        // WaitForX(circuitPhaseInput);
        WaitForX(this.maxDiscTimeDDL);
        WaitForX(this.OPDTypeDDL);
        return new String[] {
                new Select(driver.findElement(this.maxDiscTimeDDL)).getFirstSelectedOption().getText(),
                new Select(driver.findElement(this.OPDTypeDDL)).getFirstSelectedOption().getText()
        };
    }

    // TODO: Get rid of this?
    // Match params with return structure of GetBoardDetails()
    public final boolean EnterBoardDetails(String location, int designation, String nomVolt) {
        try {
            // Enter Board Location
            WaitForX(this.boardLocationInput);
            driver.findElement(this.boardLocationInput).clear();
            driver.findElement(this.boardLocationInput).sendKeys(location);
            // Enter Board Designation
            WaitForX(this.designationDLL);
            var selectReg = new Select(driver.findElement(this.designationDLL));
            selectReg.selectByIndex(designation);
            // Enter Nominal Voltage
            WaitForX(this.nominalVoltageInput);
            driver.findElement(this.nominalVoltageInput).clear();
            driver.findElement(this.nominalVoltageInput).sendKeys(nomVolt);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // TODO: Get rid of this? like above
    public final String[] GetBoardDetails() {
        WaitForX(this.boardLocationInput);
        WaitForX(this.designationDLL);
        WaitForX(this.nominalVoltageInput);
        return new String[] {
                driver.findElement(this.boardLocationInput).getText(),
                (new Select(driver.findElement(this.designationDLL)).getFirstSelectedOption().getText()),
                driver.findElement(this.nominalVoltageInput).getText()};
    }

    public final void CreateRCD(int rcdNumber) {
        // Create new RCD
        WaitForX(this.addNewButton);
        clickButton(driver.findElement(this.addNewButton));
        // Go into RCD
        String rcd = "" + rcdNumber;
        rcd = ("000".substring(0, (3 - rcd.length())) + rcd);
        By rcdCreated = By.cssSelector(("[id*=\'RCD"
                + (rcd + "\']")));
        WaitForX(rcdCreated);
        // var element = driver.findElement(rcdCreated);
        sleep(2000);
        try {
            for(WebElement row : driver.findElements(By.tagName("tr"))){
                if(row.getAttribute("id").contains("RCD" + rcd)){
                    clickButton(row);
                    break;
                }
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    // TODO: add ability to create different types of board (not just DB)
    public final boolean CreateBoard(String boardName, int amountOfCircuits, boolean evBoard){ return CreateBoard(boardName, amountOfCircuits, 0, evBoard); }
    public final boolean CreateBoard(String boardName, int amountOfCircuits, int milliseconds){ return CreateBoard(boardName, amountOfCircuits, milliseconds, false); }
    public final boolean CreateBoard(String boardName, int amountOfCircuits, int milliseconds, boolean evBoard) {
        try {
            if ((milliseconds > 0)) {
                WaitForX(this.addNewButton, Duration.ofMillis(milliseconds));
            }
            else {
                WaitForX(this.addNewButton);
            }
            clickButton(driver.findElement(this.addNewButton));
            WaitForX(this.createBoardBoardNameInput);
            WaitForX(this.createBoardBoardTypeRadioButtonTitle);
            WaitForX(this.createBoardSubmitButton);
            String boardType = "Distribution Board";
            if (evBoard) {
                boardType = "EV Charging Point";
            }

            for (var radioButton : driver.findElements(By.tagName("label"))) {
                // TODO: stop looking for text here
                if (radioButton.getText().equals(boardType)) {
                    // Create Board
                    clickButton(radioButton);
                    driver.findElement(this.createBoardBoardNameInput).clear();
                    driver.findElement(this.createBoardBoardNameInput).sendKeys(boardName);
                    driver.findElement(this.createBoardSubmitButton).click();
                    if (WaitForX(this.cancelCreateBoardButton, Duration.ofSeconds(3))) {
                        DismissModal(modalXButton);
                        // Board already exists
                        return false;
                    }

                    WaitForX(this.closeCircuitsModalButton, Duration.ofSeconds(10));
                    if ((amountOfCircuits != 0)) {
                        for (var radioBtn : driver.findElements(By.name("rblcircuit"))) {
                            // Create Circuits
                            if ((radioBtn.getAttribute("value").equals("4"))) {
                                clickButton(radioBtn);
                                if (WaitForX(this.addCircuitsNoOfPhasesDDL, Duration.ofSeconds(5))) {
                                    var selectPhases = new Select(driver.findElement(this.addCircuitsNoOfPhasesDDL));
                                    selectPhases.selectByValue(Integer.toString(amountOfCircuits));
                                    // var selectTemplate = new Select(driver.findElement(selectCircuitTemplateDDL));
                                    // selectTemplate.SelectByValue("2a452b84-5106-4c40-8855-1bf631d32db5"); // Hard-coded grab Cooker Template
                                    By doneButton = By.className("new-circuits-submit");
                                    WaitForX(doneButton);
                                    clickButton(driver.findElement(doneButton));
                                    // By cancelButton = By.id("btnCnfFail");
                                    // if(WaitForX(cancelButton, TimeSpan.FromSeconds(3))){
                                    //  driver.findElement(cancelButton).Click();
                                    // }
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        clickButton(driver.findElement(this.closeCircuitsModalButton));
                    }
                    return true;
                }
            }
            // Couldn't find job type
            return false;
        }
        catch (Exception e) {
            // Failed to create board
            return false;
        }
    }

    // Check if board exists
    public final boolean FindPrexistingBoard(String boardname) {
        try {
            WebElement Cell1 = GetTableCell("board90-list", (boardname.substring(0, 10) + " ..."));
            if ((Cell1 != null)) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
}