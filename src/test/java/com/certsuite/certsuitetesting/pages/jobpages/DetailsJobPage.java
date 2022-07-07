package com.certsuite.certsuitetesting.pages.jobpages;

import com.certsuite.certsuitetesting.entities.Constants;
import com.certsuite.certsuitetesting.entities.Randomizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

public class DetailsJobPage extends JobPage {

    // Job Details
    By inspectionNameInput = By.name("1*3*inspectionName*0*14*-3");
    By purposeOfInspectionSelect = By.name("1*3*inspectionPurpose*0*14*3");
    By regCompliedWithSelect = By.name("1*3*regCompliedWith*0*14*6");
    By recordsHeldByInput = By.name("1*41*recordsHeldBy*0*14*21");

    // Installation Details
    By occupierNameInput = By.name("1*3*installationName*0*1*2");
    By occupierTelephoneInput = By.name("1*3*installationTelephone*0*1*4");
    By occupierEmailInput = By.name("1*3*installationEmail*0*1*7");
    By occupierCompanyNameInput = By.name("1*3*installationTradingTitle*0*1*10");
    By occupierCompanyNameAddressLine1Input = By.name("1*3*installationAddress1*0*1*11");
    By occupierTownInput = By.name("1*3*installationTown*0*1*14");
    By occupierPostcodeInput = By.name("1*3*installationPostcode*0*1*16");

    // Client details
    By clientCopyFromInstallationInput = By.name("4****0*-2");

    // Inspector Details
    By inspectorNameInput = By.name("1*3*inspectorName*0*21*2");
    By inspectorTelephoneInput = By.name("1*3*inspectorTelephone*0*21*4");
    By inspectorEmailInput = By.name("1*3*inspectorEmail*0*21*7");
    By inspectorCompanyNameInput = By.name("1*3*inspectorTradingTitle*0*21*11");
    By inspectorCompanyNameAddressLine1Input = By.name("1*3*inspectorAddress1*0*21*12");
    By inspectorTownInput = By.name("1*3*inspectorTown*0*21*15");
    By inspectorPostcodeInput = By.name("1*3*inspectorPostcode*0*21*17");
    By inspectorDetailsOfUKASSelect = By.name("1*3*inspectorUKASReg*0*21*20");
    By inspectorEnrolmentNoInput = By.name("1*3*inspectorEnrolNo*0*21*21");

    // Limitations
    By limitationsAgreedTextArea = By.name("1*41*inspectionLimitations*0*2*1");
    By limitationsAgreedWithInput = By.name("1*41*inspectionLimAgreedWith*0*2*2");

    // Authoriser
    By authoriserCopyFromInspectorInput = By.name("4****55*-1");

    // Comments on Existing Installation
    By commentsOnExistingInstallationInput = By.name("1*41*inspectionGenCondOfInst*0*3*0");

    // Next Buttons
    // Not a good idea to use. Use side menu buttons
    By jobDetailsNextButton = By.id("nextTop*1");
    By installationDetailsNextButton = By.id("nextTop*0");
    By clientDetailsNextButton = By.id("nextTop*21");
    By inspectorDetailsNextButton = By.id("nextTop*55");
    By authoriserNextButton = By.id("nextTop*2");
    By commentsOnExistingInstallationNextButton = By.id("nextTop*85");

    public DetailsJobPage(WebDriver driver) {
        super(driver);
    }

    // Return String[] of the same data set within EnterJobDetails(regCompliedWith, recordsHeldBy)
    public final String[] GetJobDetails() {
        WaitForX(this.regCompliedWithSelect);
        var selectReg = new Select(driver.findElement(this.regCompliedWithSelect));
        String reg = selectReg.getFirstSelectedOption().getText();
        WaitForX(this.recordsHeldByInput);
        String rhb = driver.findElement(this.recordsHeldByInput).getText();
        return new String[] {
                reg,
                rhb
        };
    }

    // If data added here, update GetJobDetails()
    public final boolean EnterJobDetails(int regCompliedWith, String recordsHeldBy) {
        try {
            WaitForX(jobDetails);
            clickButton(driver.findElement(jobDetails));
            // Enter BS Code complied with
            WaitForX(this.regCompliedWithSelect);
            var selectReg = new Select(driver.findElement(this.regCompliedWithSelect));
            selectReg.selectByIndex(regCompliedWith);
            // Enter Records Held By
            WaitForX(this.recordsHeldByInput);
            driver.findElement(this.recordsHeldByInput).clear();
            driver.findElement(this.recordsHeldByInput).sendKeys(recordsHeldBy);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean EnterJobDetails() {
        try {
            WaitForX(jobDetails);
            clickButton(driver.findElement(jobDetails));
            // Enter Purpose of Inspection
            // Example of how to use select elements in Selenium
            WaitForX(this.purposeOfInspectionSelect);
            var selectPurpose = new Select(driver.findElement(this.purposeOfInspectionSelect));
            selectPurpose.selectByIndex(1);
            // Enter BS Code complied with
            WaitForX(this.regCompliedWithSelect);
            var selectReg = new Select(driver.findElement(this.regCompliedWithSelect));
            selectReg.selectByValue("BS 7671: 2018 (Amendment 1: 2020)");
            // Enter Records Held By
            WaitForX(this.recordsHeldByInput);
            driver.findElement(this.recordsHeldByInput).clear();
            driver.findElement(this.recordsHeldByInput).sendKeys(Randomizer.RandomFullName());
            return true;
        }
        catch (Exception e) {
            return false;
        }

    }

    public final boolean EnterInstallationDetails() {
        try {
            WaitForX(installationDetails);
            clickButton(driver.findElement(installationDetails));
            // Enter Occupier Details
            String firstName = Randomizer.RandomName();
            String lastName = Randomizer.RandomName();
            WaitForX(this.occupierNameInput);
            WaitForX(this.occupierTelephoneInput);
            WaitForX(this.occupierEmailInput);
            WaitForX(this.occupierCompanyNameInput);
            WaitForX(this.occupierCompanyNameAddressLine1Input);
            WaitForX(this.occupierTownInput);
            WaitForX(this.occupierPostcodeInput);
            driver.findElement(this.occupierNameInput).sendKeys((firstName + (" " + lastName)));
            driver.findElement(this.occupierTelephoneInput).sendKeys(Randomizer.RandomPhoneNumber());
            driver.findElement(this.occupierEmailInput).sendKeys(Randomizer.RandomEmail(firstName, lastName));
            driver.findElement(this.occupierCompanyNameInput).sendKeys(Randomizer.RandomCompanyName());
            driver.findElement(this.occupierCompanyNameAddressLine1Input).sendKeys(Randomizer.RandomAddress());
            driver.findElement(this.occupierTownInput).sendKeys(Randomizer.RandomTown());
            driver.findElement(this.occupierPostcodeInput).sendKeys(Randomizer.RandomPostcode());
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean EnterClientDetailsCopiedFromInstallation() {
        try {
            WaitForX(clientDetails);
            clickButton(driver.findElement(clientDetails));
            WaitForX(this.clientCopyFromInstallationInput);
            clickButton(driver.findElement(this.clientCopyFromInstallationInput));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean EnterInspectorDetails() {
        try {
            WaitForX(inspectorDetails);
            clickButton(driver.findElement(inspectorDetails));
            // Enter Inspector Details
            String firstName = Randomizer.RandomName();
            String lastName = Randomizer.RandomName();
            WaitForX(this.inspectorNameInput);
            WaitForX(this.inspectorTelephoneInput);
            WaitForX(this.inspectorEmailInput);
            WaitForX(this.inspectorCompanyNameInput);
            WaitForX(this.inspectorCompanyNameAddressLine1Input);
            WaitForX(this.inspectorTownInput);
            WaitForX(this.inspectorPostcodeInput);
            driver.findElement(this.inspectorNameInput).sendKeys((firstName + (" " + lastName)));
            driver.findElement(this.inspectorTelephoneInput).sendKeys(Randomizer.RandomPhoneNumber());
            driver.findElement(this.inspectorEmailInput).sendKeys(Randomizer.RandomEmail(firstName, lastName));
            driver.findElement(this.inspectorCompanyNameInput).sendKeys(Randomizer.RandomCompanyName());
            driver.findElement(this.inspectorCompanyNameAddressLine1Input).sendKeys(Randomizer.RandomAddress());
            driver.findElement(this.inspectorTownInput).sendKeys(Randomizer.RandomTown());
            driver.findElement(this.inspectorPostcodeInput).sendKeys(Randomizer.RandomPostcode());
            // Enter Details of UKAS reg
            WaitForX(this.inspectorDetailsOfUKASSelect);
            var selectReg = new Select(driver.findElement(this.inspectorDetailsOfUKASSelect));
            selectReg.selectByIndex(1);
            // Enter Enrolment number
            driver.findElement(this.inspectorEnrolmentNoInput).sendKeys("" + new Random().nextInt(1000));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean EnterAuthoriserDetailsCopiedFromInstallation() {
        try {
            WaitForX(authoriserDetails);
            clickButton(driver.findElement(authoriserDetails));
            WaitForX(this.authoriserCopyFromInspectorInput);
            clickButton(driver.findElement(this.authoriserCopyFromInspectorInput));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean EnterLimitations() {
        try {
            WaitForX(limitations);
            clickButton(driver.findElement(limitations));
            WaitForX(limitationsAgreedTextArea);
            driver.findElement(limitationsAgreedTextArea).sendKeys(Constants.LoremIpsum);
            WaitForX(limitationsAgreedWithInput);
            driver.findElement(limitationsAgreedWithInput).sendKeys(Randomizer.RandomFullName());
            return true;
        }
        catch (Exception e) {
            return true;
        }

    }

    public final boolean EnterCommentsOnExistingInstallation() {
        try {
            WaitForX(commentsOnExistingInstallation);
            clickButton(driver.findElement(commentsOnExistingInstallation));
            WaitForX(this.commentsOnExistingInstallationInput);
            driver.findElement(this.commentsOnExistingInstallationInput).sendKeys("This is an example comment");
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}