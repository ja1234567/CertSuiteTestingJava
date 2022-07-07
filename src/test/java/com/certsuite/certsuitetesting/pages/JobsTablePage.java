package com.certsuite.certsuitetesting.pages;

import com.certsuite.certsuitetesting.entities.JobType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.List;

public class JobsTablePage extends Page {

    By currentJobsTable = By.id("inspection56-list");
    By forApprovalJobsTable = By.id("inspection81-list");
    By approvedJobsTable = By.id("inspection82-list");
    By archivedJobsTable = By.id("inspection57-list");
    By createJobOpenModalButton = By.id("addBtnInTable");
    By createJobJobNameInput = By.id("inspection-name-focus");
    By createJobsJobTypeRadioButtonContainer = By.id("rblJobType");
    By createJobsSubmitButton = By.className("new-inspection-submit");

    public JobsTablePage(WebDriver driver) {
        super(driver);
    }

    public final void OpenPreexistingJob(String JobReference) {
        try {
            // Open Job
            WaitForX(this.currentJobsTable);
            sleep(2000);
            WebElement table = driver.findElement(this.currentJobsTable);
            List<WebElement> tableRows = table.findElements(By.tagName("tr"));
            List<WebElement> tableData;
            for (WebElement element : tableRows) {
                tableData = element.findElements(By.tagName("td"));
                if ((tableData.size() > 0)) {
                    String JobName = tableData.get(1).getText();
                    String UCN = tableData.get(4).getText();
                    if (JobReference.equalsIgnoreCase(JobName) || UCN.equalsIgnoreCase(JobReference)) {
                        var columns = element.findElements(By.tagName("a"));
                        try {
                            try {
                                clickButton(columns.get(0));
                            }
                            catch (Exception e) {
                                ScrollIntoView(columns.get(0));
                                clickButton(columns.get(0));
                            }
                        }
                        catch (Exception ignored) { }
                    }
                }
            }
        }
        catch (Exception ignored) { }
    }

    public final boolean CreateJob(JobType jobType) { return CreateJob(jobType); }
    public final boolean CreateJob(JobType jobType, String name) {
        try {
            // Open CreateJob modal
            WaitForX(this.createJobOpenModalButton);
            clickButton(driver.findElement(this.createJobOpenModalButton));
            // Create Job
            WaitForX(this.createJobJobNameInput);
            WaitForX(this.createJobsJobTypeRadioButtonContainer);
            WaitForX(this.createJobsSubmitButton);
            for (var radioButton : driver.findElements(By.tagName("label"))) {
                try {
                    var elements = radioButton.findElements(By.name("rblReport"));
                    for (var element : elements) {
                        String value = element.getAttribute("value");
                        if ((value.equals(jobType.filterid))) {
                            clickButton(radioButton);
                            if ((name != null)) {
                                driver.findElement(this.createJobJobNameInput).clear();
                                driver.findElement(this.createJobJobNameInput).sendKeys(name);
                            }
                            else {
                                driver.findElement(this.createJobJobNameInput).clear();
                                driver.findElement(this.createJobJobNameInput).sendKeys(("Selenium Test " + dtf.format(LocalDateTime.now())));
                            }

                            clickButton(driver.findElement(this.createJobsSubmitButton));
                            return true;
                        }
                    }
                }
                catch (Exception ignored) { }
            }

            return false;
        }
        catch (Exception e) {
            return false;
        }
    }

    public final boolean OpenPreexistingJob(String tableid, String UCN) {
        try {
            WebElement Cell1 = GetTableCell(tableid, UCN, 1);
            if ((Cell1 != null)) {
                clickButton(Cell1);
                return true;
            }
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
}