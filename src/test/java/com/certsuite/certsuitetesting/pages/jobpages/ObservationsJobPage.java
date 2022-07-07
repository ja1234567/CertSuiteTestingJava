package com.certsuite.certsuitetesting.pages.jobpages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ObservationsJobPage extends JobPage {

    public By observationsTable = By.id("obsInspection99-list");
    By addNewObsButton = By.id("addBtnInTable");
    By observationDescInput = By.id("1*4*observationText*0*60*1");
    By outcomeCodeSelect = By.id("1*4*outcomeID*1*60*2");
    By doneButton = By.id("closeObsTop");

    public ObservationsJobPage(WebDriver driver) {
        super(driver);
    }

    public final void CreateObservation(){ CreateObservation(0); }
    public final void CreateObservation(int rowNumber) {
        WaitForX(this.addNewObsButton);
        clickButton(driver.findElement(this.addNewObsButton));
    }

    public final void OpenObservation(){ OpenObservation(1); }
    public final void OpenObservation(int rowNumber) {
        while(true){
            try {
                WaitForX(this.observationsTable);
                List<WebElement> columns =
                        driver.findElement(this.observationsTable).findElement(By.tagName("tbody"))
                              .findElements(By.tagName("tr")).get((rowNumber - 1)).findElements(By.tagName("td"));
                var openButton = columns.get(1).findElement(By.tagName("a"));
                clickButton(openButton);
                break;
            }
            catch (StaleElementReferenceException repeatLoop) {

            }
        }
    }

    public final boolean GoBackToObservationsPage() {
        return clickButton(this.doneButton);
    }
}