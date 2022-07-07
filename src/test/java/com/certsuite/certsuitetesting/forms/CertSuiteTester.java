package com.certsuite.certsuitetesting.forms;

import com.certsuite.certsuitetesting.pages.LoginPage;
import io.appium.java_client.android.AndroidDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class CertSuiteTester extends JFrame
{
    private JPanel mainFrame;
    private JTabbedPane tabPane;
    private JButton startButton;

    private JTextField URLTextField;
    private JTextField emailTextField;
    private JTextField passwordTextField;
    private JButton saveButton;

    private JRadioButton window1Chrome;
    private JRadioButton window1Firefox;
    private JRadioButton window1Edge;
    private JRadioButton window1Android;
    private JRadioButton window2Chrome;
    private JRadioButton window2Firefox;
    private JRadioButton window2Edge;
    private JRadioButton window2Android;
    private JButton testButton;

    public CertSuiteTester(String appname){
        super(appname);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainFrame);
        setResizable(false);
        pack();

        createListeners();
    }

    public void createListeners(){
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch(1){
                    case 1:
                        WebDriver driver = createWebDriver(1);
                        LoginPage loginPage = new LoginPage(driver);
                        loginPage.Login(emailTextField.getText(),passwordTextField.getText());
                        break;
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new CertSuiteTester("CertSuite Tester");
        frame.setVisible(true);
    }

    public WebDriver createWebDriver(int window)
    {
        JRadioButton chrome = null, firefox = null, edge = null, android = null;
        switch(window){
            case 1:
                chrome = window1Chrome;
                firefox = window1Firefox;
                edge = window1Edge;
                android = window1Android;
                break;
            case 2:
            default:
                chrome = window2Chrome;
                firefox = window2Firefox;
                edge = window2Edge;
                android = window2Android;
                break;
        }
        if(chrome.isSelected()){
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }
        else if(firefox.isSelected()){
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver();
        }
        else if(edge.isSelected()){
            WebDriverManager.edgedriver().setup();
            return new EdgeDriver();
        }
        else if(android.isSelected()){
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName","Android 30");
            //cap.setCapability("udid","");
            cap.setCapability("platformName","Android");
            cap.setCapability("patformVersion","11");
            //cap.setCapability("appPackage","megger.seleniumtest");
            //cap.setCapability("appActivity","");

            try{
                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                AndroidDriver driver = new AndroidDriver(url, cap);
                String appPath = "C:\\Users\\jarnold\\source\\repos\\CertSuiteTests\\Testing Form\\drivers\\CS-seleniumtest.apk";
                driver.installApp(appPath);
                driver.activateApp("megger.seleniumtest");
                return driver;
            }
            catch(MalformedURLException mue){
                return null;
            }
        }
        return null;
    }
}
