package com.certsuite.certsuitetesting.testers;

import com.certsuite.certsuitetesting.entities.Constants;
import com.certsuite.certsuitetesting.entities.User;
import com.certsuite.certsuitetesting.pages.IndexPage;
import com.certsuite.certsuitetesting.pages.JobsTablePage;
import com.certsuite.certsuitetesting.pages.LoginPage;
import com.certsuite.certsuitetesting.pages.jobpages.BoardsJobPage;
import com.certsuite.certsuitetesting.pages.jobpages.CircuitsJobPage;
import com.certsuite.certsuitetesting.pages.jobpages.DetailsJobPage;
import com.certsuite.certsuitetesting.pages.jobpages.ObservationsJobPage;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class TestSet
{
    protected List<WebDriver> drivers = new ArrayList<>();
    protected String url;
    protected User account;

    protected List<LoginPage> loginPages = new ArrayList<>();
    protected List<IndexPage> indexPages = new ArrayList<>();
    protected List<JobsTablePage> jobsTablePages = new ArrayList<>();
    protected List<DetailsJobPage> jobDetailsPages = new ArrayList<>();
    protected List<BoardsJobPage> boardsPages = new ArrayList<>();
    protected List<CircuitsJobPage> circuitsPages = new ArrayList<>();
    protected List<ObservationsJobPage> observationsPages = new ArrayList<>();

    protected void Setup(String environment, String userAccount)
    {
        url = Constants.env.get(environment);
        account = Constants.account.get(userAccount);
    }

    protected void Setup(String environment, String username, String password)
    {
        url = environment;
        account = new User(username, password);
    }

    public void Setup(List<WebDriver> drivers)
    {
        loginPages.clear();
        indexPages.clear();
        jobsTablePages.clear();
        jobDetailsPages.clear();
        boardsPages.clear();
        circuitsPages.clear();
        observationsPages.clear();

        for(WebDriver d : drivers)
        {
            d.get(url);

            loginPages.add(new LoginPage(d));
            indexPages.add(new IndexPage(d));
            jobsTablePages.add(new JobsTablePage(d));
            jobDetailsPages.add(new DetailsJobPage(d));
            boardsPages.add(new BoardsJobPage(d));
            circuitsPages.add(new CircuitsJobPage(d));
            observationsPages.add(new ObservationsJobPage(d));

            drivers.add(d);
        }
    }

    // Call after each test
    protected void Stop()
    {
        for (WebDriver d : drivers)
            d.quit();
    }
}
