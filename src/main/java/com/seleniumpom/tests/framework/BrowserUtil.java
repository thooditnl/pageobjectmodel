package com.seleniumpom.tests.framework;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bbeck on 7/29/2016.
 */
public class BrowserUtil {

    // Changed enum to uppercase, so it's easier to convert a string to a enum
    public enum BrowserType
    {
        FIREFOX, CHROME, INTERNET_EXPLORER, EDGE, HTML_UNIT_DRIVER
    }

    public enum DriverType
    {
        LOCAL, REMOTE
    }

    public WebDriver GetWebDriver(DriverType driverType, BrowserType browser)
    {
        switch(driverType){
            case LOCAL:
                return getLocalDriver(browser);
            case REMOTE:
                return getRemoteDriver(browser);
            default:
                throw new RuntimeException("Unexpected webdriver type " + driverType.toString());
        }
    }

    private WebDriver getLocalDriver(BrowserType browser){
        //We can include all this in a try catch if we find that the test harness has issues with binding
        try {
            switch(browser)
            {
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", getDriverPath("chromedriver.exe"));
                    return new ChromeDriver();
                case FIREFOX:
                    return new FirefoxDriver();
                case INTERNET_EXPLORER:
                    return new InternetExplorerDriver();
                case EDGE:
                    return new EdgeDriver();
                case HTML_UNIT_DRIVER:
                    return new HtmlUnitDriver();
                default:
                    return new FirefoxDriver(); //Arbitrarily choosing FIREFOX as default choice
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException("Couldn't find appropriate executable with which to build webdriver");
        }
    }

    private WebDriver getRemoteDriver(BrowserType browser){
        TestConfiguration testConfiguration = TestConfiguration.getInstance();
        String gridUrl = testConfiguration.getSeleniumGridURL();

        DesiredCapabilities desiredCapabilities = null;
        switch (browser){
            case CHROME:
                desiredCapabilities = DesiredCapabilities.chrome();
                break;
            case EDGE:
                desiredCapabilities = DesiredCapabilities.edge();
                break;
            case FIREFOX:
                desiredCapabilities = DesiredCapabilities.firefox();
                break;
            case HTML_UNIT_DRIVER:
                desiredCapabilities = DesiredCapabilities.htmlUnitWithJs();
                break;
            case INTERNET_EXPLORER:
                desiredCapabilities = DesiredCapabilities.internetExplorer();
                break;
        }

        try {
            return new RemoteWebDriver(new URL(testConfiguration.getSeleniumGridURL()), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDriverPath(String driverExecutableName)
    {
        try {
            File file = new File(getClass().getClassLoader().getResource(driverExecutableName).getFile());
            Runtime.getRuntime().exec(file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        catch (IOException exception)
        {
            System.out.println("Failed to find driver executable: " + driverExecutableName +" Exception: " + exception.toString());
        }
        return null;
    }

    public String getFilePath(String fileName)
    {
        try {
            File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
            return file.getAbsolutePath();
        }
        catch (Exception exception)
        {
            System.out.println("Failed to find file: " + fileName +" Exception: " + exception.toString());
        }
        return null;
    }
}