package com.certsuite.certsuitetesting.pages.jobpages;

import com.certsuite.certsuitetesting.pages.Page;
import com.certsuite.certsuitetesting.entities.Randomizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public abstract class JobPage extends Page
{

    // Selenium Input Classes
    protected By seleniumText = By.className("seleniumText");
    protected By seleniumDate = By.className("seleniumDate");
    protected By seleniumCheckbox = By.className("seleniumCheckbox");
    protected By seleniumCheckboxList = By.className("seleniumCheckboxList");
    protected By seleniumRadio = By.className("seleniumRadio");
    protected By seleniumImage = By.className("seleniumImage");
    protected By seleniumImageNoButtons = By.className("seleniumImageNoButtons");
    protected By seleniumDDL = By.className("seleniumDDL");
    protected By seleniumTextPlusButton = By.className("seleniumTextPlusButton");
    protected By seleniumTextPlusButtonButton = By.className("seleniumTextPlusButtonButton");
    protected By gotoRCDs = By.cssSelector("[id*=\'RCDs\']");

    enum inputType
    {
        text,
        date,
        checkbox,
        checkboxlist,
        radio,
        image,
        imageNoButtons,
        ddl,
        textPlusButton,
        textPlusButtonButton,
    }

    // Side Menu buttons
    protected By sideMenu = By.className("nav-pills");
    protected By jobDetails = By.linkText("Job Details");
    protected By installationDetails = By.linkText("Installation Details");
    protected By clientDetails = By.linkText("Client Details");
    protected By inspectorDetails = By.linkText("Inspector Details");
    protected By authoriserDetails = By.linkText("Authoriser Details");
    protected By limitations = By.linkText("Limitations");
    protected By conditionOfTheInstallation = By.linkText("Condition of the Installation");
    protected By supplyAndEarthing = By.linkText("Supply and Earthing");
    protected By boards = By.linkText("Boards");
    protected By observations = By.linkText("Observations");
    protected By commentsOnExistingInstallation = By.linkText("Comments On Existing Installation");
    protected By previewReport = By.linkText("Preview Report");
    protected By closeJob = By.linkText("Close Job");

    // Run Report
    protected By runReportButton = By.id("runReportBtn");

    // protected By previewFullReportButton = By.id("trigger*2"); // Don't use, this only works for EICR
    protected By previewButtons = By.cssSelector("[id^=trigger]");
    protected By skipLicenceModalButton = By.id("btnCnfFail");
    protected By viewPDFButton = By.id("viewPDF");
    protected By cancelViewPDFButton = By.xpath("//*[@id=\"1stFooter\"]/button");

    // Used to block entering details in these fields
    // Can cause errors such as when emptying textbox before typing (some fields can't be emptied)
    protected String[] elementsNotToTouch = new String[]{
            "inspectionName"
    };

    protected JobPage(WebDriver driver)
    {
        super(driver);
    }

    public final boolean UseSideMenu(String blockid)
    {
        try
        {
            int id = Integer.parseInt(blockid);
            return this.UseSideMenu(id);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // Look for a side menu button referencing a particular blockid
    public final boolean UseSideMenu(int blockid)
    {
        WaitForX(this.sideMenu);
        var sideBarButtons = driver.findElement(this.sideMenu);
        var list = sideBarButtons.findElements(By.tagName("li"));
        for (var listItem : list)
        {
            String id = listItem.getAttribute("id");
            try
            {
                if ((id.split("*")[1].equals(Integer.toString(blockid))))
                {
                    clickButton(listItem);
                    if (((blockid == 97) || (blockid == 98)))
                    {
                        String className = switch (blockid)
                                {
                                    case 97 -> "jsChecklistTypeModal";
                                    case 98 -> "jsRegCompliedWithModal";
                                    default -> "";
                                };
                        if (WaitForX(By.className(className), Duration.ofSeconds(3)))
                        {
                            Select select = new Select(driver.findElement(By.className(className)));
                            select.selectByIndex(1);
                            clickButton(driver.findElement(By.id("1stFooter")).findElement(By.className("btn-blue")));
                        }
                    }
                    return true;
                }
            }
            catch (Exception ignored)
            {
            }
        }
        return false;
    }

    // Look for field entries on page given a partial id
    public final boolean LookForEntry(String entryid)
    {
        return LookForEntry(entryid, 5);
    }

    public final boolean LookForEntry(String entryid, int seconds)
    {
        if (WaitForX(By.cssSelector(("[id*=\'" + ("*"
                + (entryid + ("*" + "\']"))))), Duration.ofSeconds(seconds)))
        {
            return true;
        }
        return false;
    }

    public final void GotoJobDetails()
    {
        WaitForX(sideMenu);
        var sideBarButtons = driver.findElement(sideMenu);
        var list = sideBarButtons.findElements(By.tagName("li"));
        clickButton(list.get(0));
    }

    // DEPRECATED: TO REMOVE
    public final void GotoBoards()
    {
        clickButton(driver.findElement(boards));
    }

    // DEPRECATED: TO REMOVE
    public final void GotoObservations()
    {
        clickButton(driver.findElement(observations));
    }

    public final boolean CloseJob()
    {
        try
        {
            WaitForX(closeJob);
            clickButton(driver.findElement(closeJob));
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public final boolean RunReport()
    {
        WaitForX(runReportButton);
        clickButton(driver.findElement(runReportButton));
        sleep(1000);
        WaitForX(previewButtons);
        var elements = driver.findElements(this.previewButtons);
        try
        {
            clickButton(elements.get(0));
        }
        catch (Exception ignored)
        {
        }

        long extraTime = TimeDismissModal(this.skipLicenceModalButton);
        long start = System.currentTimeMillis() - extraTime, stop;
        try
        {
            WaitForX(viewPDFButton, Duration.ofMinutes(3));
        }
        catch (Exception e)
        {
            return false;
        }
        stop = System.currentTimeMillis();
        // TODO: Handle length of time report takes
        // stop - start

        DismissModal(this.cancelViewPDFButton);
        return true;
    }

    // Used to look for collect ids of all fields available inside this job
    public final List<String> CollectInputFields()
    {
        // Collect ids of fields that exist
        List<String> entries = new ArrayList<>();
        try
        {
            DismissCookies(2);
            while (true)
            {
                // Deal with modal incase one appears on this page (eg: Boards modal)
                DismissModal(2);
                // Process input fields (enter data?)
                // Collect ids of fields that fail
                entries = Stream.concat(entries.stream(), CollectEntries(inputType.text).stream()).toList();
                entries = Stream.concat(entries.stream(), CollectEntries(inputType.date).stream()).toList();
                entries = Stream.concat(entries.stream(), CollectEntries(inputType.checkbox).stream()).toList();
                // entries = Stream.concat(entries.stream(),CollectEntries(inputType.checkboxlist).stream()).toList();
                entries = Stream.concat(entries.stream(), CollectEntries(inputType.radio).stream()).toList();
                // entries = Stream.concat(entries.stream(),CollectEntries(inputType.image).stream()).toList();
                // entries = Stream.concat(entries.stream(),CollectEntries(inputType.imageNoButtons).stream()).toList();
                entries = Stream.concat(entries.stream(), CollectEntries(inputType.ddl).stream()).toList();
                // entries = Stream.concat(entries.stream(),CollectEntries(inputType.textPlusButton).stream()).toList();
                // entries = Stream.concat(entries.stream(),CollectEntries(inputType.textPlusButtonButton).stream()).toList();

                if (WaitForX(By.className("next-btn"), Duration.ofSeconds(2)))
                {
                    clickButton(driver.findElement(By.className("next-btn")));
                }
                else break;
            }
            return entries;
        }
        catch (Exception e)
        {
            return entries;
        }
    }

    // Collect names of fields that exist on the page
    protected final List<String> CollectEntries(inputType type)
    {
        List<String> entries = new ArrayList<String>();
        List<WebElement> elements = null;
        switch (type)
        {
            case text ->
            {
                WaitForX(this.seleniumText, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumText);
            }
            case date ->
            {
                WaitForX(this.seleniumDate, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumDate);
            }
            case checkbox ->
            {
                WaitForX(this.seleniumCheckbox, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumCheckbox);
            }
            case checkboxlist ->
            {
                WaitForX(this.seleniumCheckboxList, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumCheckboxList);
            }
            case radio ->
            {
                WaitForX(this.seleniumRadio, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumRadio);
            }
            case image ->
            {
                WaitForX(this.seleniumImage, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumImage);
            }
            case imageNoButtons ->
            {
                WaitForX(this.seleniumImageNoButtons, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumImageNoButtons);
            }
            case ddl ->
            {
                WaitForX(this.seleniumDDL, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumDDL);
            }
            case textPlusButton ->
            {
                WaitForX(this.seleniumTextPlusButton, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumTextPlusButton);
            }
            case textPlusButtonButton ->
            {
                WaitForX(this.seleniumTextPlusButtonButton, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumTextPlusButtonButton);
            }
        }
        if ((elements != null))
        {
            for (int i = 0; (i < elements.size()); i++)
            {
                var element = elements.get(i);
                var repeat = true;
                while (repeat)
                {
                    repeat = false;
                    try
                    {
                        entries.add(element.getAttribute("id"));
                    }
                    catch (Exception e)
                    {
                        // Element no longer on page probably
                        // Reset to find elements again
                        switch (type)
                        {
                            case text ->
                            {
                                WaitForX(this.seleniumText, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumText);
                            }
                            case date ->
                            {
                                WaitForX(this.seleniumDate, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumDate);
                            }
                            case checkbox ->
                            {
                                WaitForX(this.seleniumCheckbox, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumCheckbox);
                            }
                            case checkboxlist ->
                            {
                                WaitForX(this.seleniumCheckboxList, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumCheckboxList);
                            }
                            case radio ->
                            {
                                WaitForX(this.seleniumRadio, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumRadio);
                            }
                            case image ->
                            {
                                WaitForX(this.seleniumImage, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumImage);
                            }
                            case imageNoButtons ->
                            {
                                WaitForX(this.seleniumImageNoButtons, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumImageNoButtons);
                            }
                            case ddl ->
                            {
                                WaitForX(this.seleniumDDL, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumDDL);
                            }
                            case textPlusButton ->
                            {
                                WaitForX(this.seleniumTextPlusButton, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumTextPlusButton);
                            }
                            case textPlusButtonButton ->
                            {
                                WaitForX(this.seleniumTextPlusButtonButton, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumTextPlusButtonButton);
                            }
                        }
                        repeat = true;
                    }
                }
            }
        }
        return entries;
    }

    // Used to input data using classes added to input fields during wasp-pop creation process
    // creatingBoards boolean is used to tell whether to create boards/circuits/rcds/obs, or read already exisiting
    public final boolean SetInputElementsInJob()
    {
        return SetInputElementsInJob(true);
    }

    public final boolean SetInputElementsInJob(boolean creatingBoards)
    {
        // Collect ids of fields that failed to take inputs
        List<String> failedFieldEntries = new ArrayList<>();
        var boardsPage = new BoardsJobPage(driver);
        var circuitsPage = new CircuitsJobPage(driver);
        var observationsPage = new ObservationsJobPage(driver);
        try
        {
            DismissCookies(2);
            while (true)
            {
                // Deal with modal incase one appears on this page (eg: Boards modal)
                DismissModal(2);
                if (WaitForX(boardsPage.boardsTable, Duration.ofSeconds(3)))
                {
                    // We are on the boards page
                    driver.findElement(boardsPage.boardsTable);
                    if (creatingBoards)
                    {
                        boardsPage.CreateBoard("B001", 1, 100);
                        DismissModal();
                        var circuits = driver.findElements(By.cssSelector("[id*=\'Circuit\']"));

                        for (var circuit : circuits)
                        {
                            if (circuit.getText().contains("1"))
                            {
                                clickButton(circuit);
                                this.fillEntryData(failedFieldEntries);
                            }
                        }

                        boardsPage.WaitForX(gotoRCDs);
                        clickButton(driver.findElement(this.gotoRCDs));
                        boardsPage.CreateRCD(1);
                        this.fillEntryData(failedFieldEntries);
                        circuitsPage.gotoBoardDetails();
                        this.fillEntryData(failedFieldEntries);
                    }
                    else
                    {
                        WaitForX(By.linkText("B001"), Duration.ofSeconds(3));
                        clickButton(driver.findElement(By.linkText("B001")));
                        DismissModal();
                        this.fillEntryData(failedFieldEntries);
                        boardsPage.GotoCircuitsOfThisBoard();
                        clickButton(circuitsPage.GetCell("1", "Circuit"));
                        this.fillEntryData(failedFieldEntries);
                        boardsPage.WaitForX(gotoRCDs);
                        clickButton(driver.findElement(this.gotoRCDs));
                        WaitForX(By.linkText("RCD001"), Duration.ofSeconds(3));
                        clickButton(driver.findElement(By.linkText("RCD001")));
                        this.fillEntryData(failedFieldEntries);
                    }

                    boardsPage.GotoBoards();
                }
                else if (WaitForX(observationsPage.observationsTable, Duration.ofSeconds(3)))
                {
                    // We are on the Observations page
                    if (creatingBoards)
                    {
                        observationsPage.CreateObservation();
                        sleep(500);
                    }

                    observationsPage.OpenObservation(1);
                    this.fillEntryData(failedFieldEntries);
                    observationsPage.GoBackToObservationsPage();
                }
                else
                {
                    // Otherwise this is another page
                    this.fillEntryData(failedFieldEntries);
                }

                if (WaitForX(By.className("next-btn"), Duration.ofSeconds(4)))
                {
                    clickButton(By.className("next-btn"));
                    if (WaitForX(By.className("jsChecklistTypeModal"), Duration.ofSeconds(2)))
                    {
                        Select select = new Select(driver.findElement(By.className("jsChecklistTypeModal")));
                        select.selectByIndex(1);
                        clickButton(driver.findElement(By.id("1stFooter")).findElement(By.className("btn-blue")));
                    }
                    else if (WaitForX(By.className("jsRegCompliedWithModal"), Duration.ofSeconds(2)))
                    {
                        Select select = new Select(driver.findElement(By.className("jsRegCompliedWithModal")));
                        select.selectByIndex(1);
                        clickButton(driver.findElement(By.id("1stFooter")).findElement(By.className("btn-blue")));
                    }
                }
                else
                {
                    break;
                }
            }
            return failedFieldEntries.size() > 0;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // Take collection of input web elements and process them. Return list of ids of entries that failed
    // TODO: Add params that tell method what to do or what data to enter for example
    protected final List<String> SetInputData(inputType type)
    {
        List<String> failedFieldEntries = new ArrayList<String>();
        List<WebElement> elements = null;
        switch (type)
        {
            case text ->
            {
                WaitForX(this.seleniumText, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumText);
            }
            case date ->
            {
                WaitForX(this.seleniumDate, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumDate);
            }
            case checkbox ->
            {
                WaitForX(this.seleniumCheckbox, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumCheckbox);
            }
            case checkboxlist ->
            {
                WaitForX(this.seleniumCheckboxList, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumCheckboxList);
            }
            case radio ->
            {
                WaitForX(this.seleniumRadio, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumRadio);
            }
            case image ->
            {
                WaitForX(this.seleniumImage, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumImage);
            }
            case imageNoButtons ->
            {
                WaitForX(this.seleniumImageNoButtons, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumImageNoButtons);
            }
            case ddl ->
            {
                WaitForX(this.seleniumDDL, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumDDL);
            }
            case textPlusButton ->
            {
                WaitForX(this.seleniumTextPlusButton, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumTextPlusButton);
            }
            case textPlusButtonButton ->
            {
                WaitForX(this.seleniumTextPlusButtonButton, Duration.ofMillis(100));
                elements = driver.findElements(this.seleniumTextPlusButtonButton);
            }
        }
        if ((elements != null))
        {
            Random rnd = new Random();
            for (WebElement element : elements)
            {
                while (true)
                {
                    try
                    {
                        String[] classes = element.getAttribute("class").split(" ");
                        String id = element.getAttribute("id");
                        try
                        {
                            // Check if element is not in list of elements to ignore, and is visible (ie: not display:none or opacity:0)
                            if ((!this.CheckFieldCanBeModified(id) || !element.isDisplayed()))
                            {
                                // TODO: Warning!!! continue If
                            }

                            // Get max length if exists
                            int maxLength = -1;
                            try
                            {
                                maxLength = Integer.parseInt(element.getAttribute("MaxLength"));
                            }
                            catch (Exception ignored)
                            {

                            }

                            // PROCESS
                            ScrollIntoView(element);
                            String data = "";
                            switch (type)
                            {
                                case text:
                                    try
                                    {
                                        String inputType = element.getAttribute("type");
                                        if ((inputType.equals("number")))
                                        {
                                            Random r = new Random();
                                            data = Integer.toString(r.nextInt(100));
                                        }
                                        else
                                        {
                                            data = Randomizer.RandomString(100);
                                        }
                                    }
                                    catch (Exception ee)
                                    {
                                        data = Randomizer.RandomString(100);
                                    }
                                    element.clear();
                                    element.sendKeys(data);
                                    break;
                                case date:
                                    clickButton(element);
                                    var datePickerDiv = By.id("ui-datepicker-div");
                                    while (true)
                                    {
                                        try
                                        {
                                            var datepickerTable = driver.findElement(datePickerDiv).findElement(By.tagName("tbody"));
                                            var secondRow = datepickerTable.findElements(By.tagName("tr")).get(1);
                                            WebElement day = secondRow.findElements(By.tagName("td")).get(rnd.nextInt(7));
                                            day.click();
                                            break;
                                        }
                                        catch (Exception repeatLoop)
                                        {

                                        }
                                    }
                                    break;
                                case checkbox:
                                    // Check 50% of time
                                    Random rand = new Random();
                                    if (rand.nextInt(2) == 1)
                                    {
                                        clickButton(element);
                                    }
                                    // Some checkboxes will cause a modal popup, if so dismiss
                                    DismissModal(By.id("spnAlertButton"), 1);
                                    break;
                                // case checkboxlist:
                                //     break;
                                case radio:
                                    // Select first value
                                    var radioLabels = element.findElements(By.tagName("label"));
                                    clickButton(radioLabels.get(0));
                                    break;
                                // case image:
                                //     break;
                                // case imageNoButtons:
                                //     break;
                                case ddl:
                                    // Pick second option (first is often empty)
                                    var selectReg = new Select(element);
                                    try
                                    {
                                        selectReg.selectByIndex(rnd.nextInt(1, (selectReg.getOptions().size() - 1)));
                                    }
                                    catch (Exception e)
                                    {
                                        selectReg.selectByIndex(rnd.nextInt(1, selectReg.getOptions().size()));
                                    }
                                    break;
                                // case textPlusButton:
                                //     break;
                                // case textPlusButtonButton:
                                //     break;
                            }
                            break;
                        }
                        catch (Exception ee)
                        {
                            String[] pieces = id.split("*");
                            failedFieldEntries.add(("blockid: "
                                    + (pieces[0] + (" id: " + pieces[2]))));
                            // TODO: Warning!!! continue Catch
                        }
                    }
                    catch (Exception e)
                    {
                        // Element no longer on page probably
                        // Reset to find elements again
                        switch (type)
                        {
                            case text ->
                            {
                                WaitForX(this.seleniumText, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumText);
                            }
                            case date ->
                            {
                                WaitForX(this.seleniumDate, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumDate);
                            }
                            case checkbox ->
                            {
                                WaitForX(this.seleniumCheckbox, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumCheckbox);
                            }
                            case checkboxlist ->
                            {
                                WaitForX(this.seleniumCheckboxList, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumCheckboxList);
                            }
                            case radio ->
                            {
                                WaitForX(this.seleniumRadio, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumRadio);
                            }
                            case image ->
                            {
                                WaitForX(this.seleniumImage, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumImage);
                            }
                            case imageNoButtons ->
                            {
                                WaitForX(this.seleniumImageNoButtons, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumImageNoButtons);
                            }
                            case ddl ->
                            {
                                WaitForX(this.seleniumDDL, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumDDL);
                            }
                            case textPlusButton ->
                            {
                                WaitForX(this.seleniumTextPlusButton, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumTextPlusButton);
                            }
                            case textPlusButtonButton ->
                            {
                                WaitForX(this.seleniumTextPlusButtonButton, Duration.ofMillis(100));
                                elements = driver.findElements(this.seleniumTextPlusButtonButton);
                            }
                        }
                    }
                }
            }
        }
        return failedFieldEntries;
    }

    // Useful for testing syncs
    public final List<String> ReadInputElementsInJob()
    {
        // Collect inputs of all entry fields
        List<String> fieldEntries = new ArrayList<String>();
        DismissCookies(2);
        do
        {
            var boardsPage = new BoardsJobPage(driver);
            var circuitsPage = new CircuitsJobPage(driver);
            var observationsPage = new ObservationsJobPage(driver);
            // Deal with modal incase one appears on this page (eg: Boards hint modal)
            DismissModal(2);
            if (WaitForX(boardsPage.boardsTable, Duration.ofSeconds(3)))
            {
                // If Boards page...
                WaitForX(By.linkText("B001"), Duration.ofSeconds(3));
                clickButton(driver.findElement(By.linkText("B001")));
                DismissModal();
                this.FillFieldEntries(fieldEntries);
                boardsPage.GotoCircuitsOfThisBoard();
                clickButton(circuitsPage.GetCell("1", "Circuit"));
                this.FillFieldEntries(fieldEntries);
                WaitForX(gotoRCDs);
                clickButton(driver.findElement(this.gotoRCDs));
                WaitForX(By.linkText("RCD001"), Duration.ofSeconds(3));
                clickButton(driver.findElement(By.linkText("RCD001")));
                this.FillFieldEntries(fieldEntries);
                boardsPage.GotoBoards();
            }
            else if (WaitForX(observationsPage.observationsTable, Duration.ofSeconds(3)))
            {
                observationsPage.OpenObservation(1);
                // Collect data
                this.FillFieldEntries(fieldEntries);
                observationsPage.GoBackToObservationsPage();
            }
            else
            {
                // Collect data
                this.FillFieldEntries(fieldEntries);
            }

            if (WaitForX(By.className("next-btn"), Duration.ofSeconds(2)))
            {
                clickButton(driver.findElement(By.className("next-btn")));
            }

            if (WaitForX(By.className("jsChecklistTypeModal"), Duration.ofSeconds(2)))
            {
                Select select = new Select(driver.findElement(By.className("jsChecklistTypeModal")));
                select.selectByIndex(1);
                clickButton(driver.findElement(By.id("1stFooter")).findElement(By.className("btn-blue")));
            }
            else if (WaitForX(By.className("jsRegCompliedWithModal"), Duration.ofSeconds(2)))
            {
                Select select = new Select(driver.findElement(By.className("jsRegCompliedWithModal")));
                select.selectByIndex(1);
                clickButton(driver.findElement(By.id("1stFooter")).findElement(By.className("btn-blue")));
            }

        }
        while (WaitForX(By.className("next-btn"), Duration.ofSeconds(2)));

        return fieldEntries;
    }

    // Helper class used by ReadInputElementsInJob() to read inputs on page
    protected final void FillFieldEntries(List<String> fieldEntries)
    {
        // Wait for fields to be visible
        WaitForX(this.seleniumText, Duration.ofMillis(2000));
        WaitForX(this.seleniumDate, Duration.ofMillis(100));
        WaitForX(this.seleniumCheckbox, Duration.ofMillis(100));
        // WaitForX(seleniumCheckboxList, Duration.ofMillis(100));
        WaitForX(this.seleniumRadio, Duration.ofMillis(100));
        // WaitForX(seleniumImage, Duration.ofMillis(100));
        // WaitForX(seleniumImageNoButtons, Duration.ofMillis(100));
        WaitForX(this.seleniumDDL, Duration.ofMillis(100));
        // WaitForX(seleniumTextPlusButton, Duration.ofMillis(100));
        // WaitForX(seleniumTextPlusButtonButton, Duration.ofMillis(100));
        // Collect input fields
        var textElements = driver.findElements(this.seleniumText);
        var dateElements = driver.findElements(this.seleniumDate);
        var checkboxElements = driver.findElements(this.seleniumCheckbox);
        // var checkboxListElements = driver.findElements(seleniumCheckboxList);
        var radioElements = driver.findElements(seleniumRadio);
        // var imageElements = driver.findElements(seleniumImage);
        // var imageNoButtonsElements = driver.findElements(seleniumImageNoButtons);
        var ddlElements = driver.findElements(seleniumDDL);
        // var textPlusButtonElements = driver.findElements(seleniumTextPlusButton);
        // var textPlusButtonButtonElements = driver.findElements(seleniumTextPlusButtonButton);
        // Process input fields (enter data?)
        // Collect ids of fields that fail
        fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(textElements, inputType.text).stream()).toList();
        fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(dateElements, inputType.date).stream()).toList();
        fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(checkboxElements, inputType.checkbox).stream()).toList();
        // fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(checkboxListElements, inputType.checkboxlist).stream()).toList();
        fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(radioElements, inputType.radio).stream()).toList();
        // fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(imageElements, inputType.image).stream()).toList();
        // fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(imageNoButtonsElements, inputType.imageNoButtons).stream()).toList();
        fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(ddlElements, inputType.ddl).stream()).toList();
        // fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(textPlusButtonElements, inputType.textPlusButton).stream()).toList();
        // fieldEntries = Stream.concat(fieldEntries.stream(), GatherInputData(textPlusButtonButtonElements, inputType.textPlusButtonButton).stream()).toList();
    }

    // Read input fields from a job. Used with ReadInputElementsInJob
    protected final List<String> GatherInputData(List<WebElement> elements, inputType type)
    {
        List<String> inputData = new ArrayList<String>();
        for (var element : elements)
        {
            String id = element.getAttribute("id");
            // Check if element is not in list of elements to ignore, and is visible (ie: not display:none or opacity:0)
            if ((!CheckFieldCanBeModified(id) || !element.isDisplayed()))
            {
                // TODO: Warning!!! continue If
            }

            StringBuilder data = new StringBuilder();
            switch (type)
            {
                case text:
                    data = new StringBuilder(element.getAttribute("value"));
                    break;
                case date:
                    WebElement date = null;
                    for (var datePickerAlt : driver.findElements(By.className("datepickeralt")))
                    {
                        if ((datePickerAlt.getAttribute("id").equals(id + "*dpa")))
                        {
                            date = datePickerAlt;
                            break;
                        }
                    }
                    if ((date != null))
                    {
                        data.append(date.getAttribute("value"));
                    }
                    break;
                case checkbox:
                    data.append(element.isSelected());
                    break;
                case checkboxlist:
                    break;
                case radio:
                    var radioLabels = element.findElements(By.tagName("input"));
                    for (int i = 0; (i < radioLabels.size()); i++)
                    {
                        if ((radioLabels.get(i).isSelected()))
                        {
                            data.append(i);
                        }
                    }
                    break;
                case image:
                    break;
                case imageNoButtons:
                    break;
                case ddl:
                    var selectReg = new Select(element);
                    var selectedOption = selectReg.getFirstSelectedOption();
                    data.append(selectedOption.getText());
                    break;
                case textPlusButton:
                    break;
                case textPlusButtonButton:
                    break;
            }
            inputData.add((id + (":" + data)));
        }
        return inputData;
    }

    // Check if field is within list of fields not to touch (to disallow changing job names etc.)
    protected final boolean CheckFieldCanBeModified(String id)
    {
        for (var fieldName : this.elementsNotToTouch)
        {
            if (id.contains(fieldName))
            {
                return false;
            }
        }
        return true;
    }

    // Used to fill input for Entrys in SyncTest
    // Fills list with entrys that failed to take input
    private final void fillEntryData(List<String> failedFieldEntries)
    {
        // Process input fields (enter data?)
        // Collect ids of fields that fail
        failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.text).stream()).toList();
        failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.date).stream()).toList();
        failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.checkbox).stream()).toList();
        // failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.checkboxlist).stream()).toList();
        failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.radio).stream()).toList();
        // failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.image).stream()).toList();
        // failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.imageNoButtons).stream()).toList();
        failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.ddl).stream()).toList();
        // failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.textPlusButton).stream()).toList();
        // failedFieldEntries = Stream.concat(failedFieldEntries.stream(), SetInputData(inputType.textPlusButtonButton).stream()).toList();
        if (WaitForX(By.id("modalContainer"), Duration.ofSeconds(2)))
        {
            var element = driver.findElement(By.id("modalContainer")).findElement(By.className("close"));
            clickButton(element);
        }
        try
        {
            DismissModal(By.id("spnAlertButton"), 2);
        }
        catch (Exception e)
        {

        }
    }
}