package com.seleniumpom.tests.framework;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;



public class WebDriverWrapper {

    public final static long TYPICAL_INTERVAL = 100; //0.1 second wait should be minimum during testing.  When going live, and running tests in parallel recommend we shift this higher to be nice to our CPUs.
    public final static int WAIT_TIME_FOR_JAVA_SCRIPT_ALERT = 2; //NOTE: This wait time is in seconds because that is what is used by WebDriverWait.class

	private WebDriver driver = null;
    private Set<String> AllBrowserWindows = null;
    private BrowserUtil browserUtil = new BrowserUtil();

    protected Logger log = Logger.getLogger(getClass());

	//region Initialization/Termination Methods
    public void closeWebDriver()
    {
        try{
            driver.quit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

	public void getSeleniumInstance(BrowserUtil.DriverType driverType, BrowserUtil.BrowserType browserType)
	{
        log.info("Starting new browser: " + browserType.toString());
		driver = browserUtil.GetWebDriver(driverType, browserType);
		driver.manage().timeouts().implicitlyWait(0,TimeUnit.MILLISECONDS);
	}
    //endregion

    //region BrowserManipulation
	public void maximizeWindow()
	{
		driver.manage().window().maximize();
	}

	public void navigateToUrl(String url)
	{
		driver.navigate().to(url);
	}
	//endregion

    //region Element Verification
	private WebElement getElement(String xpath, int MaxWaitTime)
    {
		int currentWaitTime = 0;
		WebElement webElement = null;
		while (currentWaitTime < MaxWaitTime)
		{
            try{
                webElement = driver.findElement(By.xpath(xpath));
                if (webElement.isDisplayed())
                        return webElement;
            }
            catch (Exception e)
            {
                try {
                    Thread.sleep(TYPICAL_INTERVAL);
                } catch (InterruptedException ex) {
                    //Do nothing
                }
                log.info("Failed to find element with xpath: " +xpath+ " at currentWaitTime= "+currentWaitTime);
            }
            finally {
                currentWaitTime += TYPICAL_INTERVAL;
            }
		}
        log.error("getElement: "+xpath+ " threw exception when attempting to find element. currentWaitTime= "+currentWaitTime);
		throw new NoSuchElementException("Unable to find element at " +xpath+ " after waiting "+currentWaitTime+"ms"); //If we get here, the element wasn't found during MaxWaitTime
	}

    public boolean elementIsVisible(String xpath, int waitTime)
    {
        WebElement webElement = null;
        try{
            webElement = getElement(xpath, waitTime);
            if (webElement.isDisplayed())
                return true;
        }
        catch (NoSuchElementException e)
        {
            log.info("After waiting "+waitTime+ "ms, element at xpath " +xpath+ " was not visible.");
        }
        return false;
    }

    public void waitForElementToBeVisible(String xpath, int waitTime)
    {
        WebElement webElement = null;
        try{
            webElement = getElement(xpath, waitTime);
            if (webElement != null)
                return;
        }
        catch (NoSuchElementException e)
        {
            throw new NoSuchElementException("Waited maximum amount of time (" + waitTime + " milliseconds for element to be visible. Element " +xpath);
        }
    }

    public void waitForElementToNotBeVisible(String xpath, int MaxWaitTime)
    {
        boolean isElementVisible = true;
        int currentWaitTime = 0;
        do try {
            WebElement webElement = driver.findElement(By.xpath(xpath));
            isElementVisible = webElement.isDisplayed();
            Thread.sleep(TYPICAL_INTERVAL);
            currentWaitTime += TYPICAL_INTERVAL;
        } catch (Exception e) {
            isElementVisible = false; //This is okay, as it is possible the element had already disappeared by the time this method was called.
        } while (currentWaitTime < MaxWaitTime && isElementVisible);
    }

    public void waitForElementsToExist(String xpath, int MaxWaitTime, int expectedCount)
    {
        boolean doElementsExist = true;
        int currentWaitTime = 0;
        do try {
            List<WebElement> webElement = driver.findElements(By.xpath(xpath));
            doElementsExist = (webElement.size() == expectedCount);
            Thread.sleep(TYPICAL_INTERVAL);
            currentWaitTime += TYPICAL_INTERVAL;
        } catch (Exception e) {
            doElementsExist = false; //This is okay, as it is possible the element had already disappeared by the time this method was called.
        } while (currentWaitTime < MaxWaitTime && !doElementsExist);
        log.info("Waited for elements to exist. Waited: " + currentWaitTime + "/" + MaxWaitTime + " for "+xpath);
    }

    public void waitForElementToNotExist(String xpath, int MaxWaitTime)
    {
        boolean doesElementExist = true;
        int currentWaitTime = 0;
        do try {
            WebElement webElement = driver.findElement(By.xpath(xpath));
            Thread.sleep(TYPICAL_INTERVAL);
            currentWaitTime += TYPICAL_INTERVAL;
        } catch (Exception e) {
            doesElementExist = false; //This is okay, as it is possible the element had already disappeared by the time this method was called.
        } while (currentWaitTime < MaxWaitTime && doesElementExist);
        //System.out.println("waitForElementToNotExist "+by.toString()+ "currentWaitTime = " +currentWaitTime + "doesElementExist= "+doesElementExist);
    }

    public boolean isJavaScriptAlertOnPage()
    {
        try
        {
            driver.switchTo().alert();
            return true;
        }
        catch (NoAlertPresentException e)
        {
            return false;
        }
    }

    public int getCountOfElements(String xpath, int waitTime)
    {
        try {
            waitForElementToBeVisible(xpath, waitTime);
            List<WebElement> webElements = driver.findElements(By.xpath(xpath));
            return webElements.size();
        } catch (Exception e) {
            return 0;
        }
    }
    //endregion

    //region Browser/Window Interactions
    // This method operates under the assumption that there are only two windows being tested.  If there are more, then we need to refactor.
    public void switchToNewWindow()
    {
        AllBrowserWindows = driver.getWindowHandles();
        driver.switchTo().window((String)AllBrowserWindows.toArray()[1]); //switch to the 2nd window
    }

    public void returnToOriginalWindow()
    {
        Assert.assertNotNull(AllBrowserWindows);
        driver.switchTo().window((String)AllBrowserWindows.toArray()[0]);
    }

    public void switchToIFrameWithId(String iFrameId)
    {
        driver.switchTo().frame(iFrameId);
    }

    public void switchToIFrameByIndex(int index)
    {
        driver.switchTo().frame(index);
    }

    public void returnOutOfIFrame()
    {
        driver.switchTo().defaultContent();
    }

    public void clickOkOnJavaScriptAlert()
    {
        waitForJavaScriptToDisplay();
        if (isJavaScriptAlertOnPage())
            driver.switchTo().alert().accept();
        else throw new NoAlertPresentException();
    }

    public void clickCancelJavaScriptAlert()
    {
        waitForJavaScriptToDisplay();
        if (isJavaScriptAlertOnPage())
            driver.switchTo().alert().dismiss();
        else throw new NoAlertPresentException();
    }

    private void waitForJavaScriptToDisplay()
    {
        WebDriverWait javaScriptWait = new WebDriverWait(driver, WAIT_TIME_FOR_JAVA_SCRIPT_ALERT);
        javaScriptWait.until(ExpectedConditions.alertIsPresent());
    }
    //endregion

    //region ElementInteractions
    public void clickElement(String xpath, int waitTime)
    {
        try
        {
            WebElement webElement = getElement(xpath, waitTime);
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", webElement, "style", "border: 2px solid yellow; color: yellow; font-weight: bold;");
            webElement.click();
            log.info("Attempted 1st click on  "+xpath);
        }
        catch (Exception e)
        {
            //If click fails, attempt a brief sleep and click again
            try {
                Thread.sleep(TYPICAL_INTERVAL);
            } catch (InterruptedException e1) {
            }
            waitForElementToNotExist("//div[@class='blockUI blockOverlay']",waitTime);  //TODO: Remove this - it exists for experimentation on Jenkins
            waitForElementToNotExist("//div[@class='ui-widget-overlay']", waitTime);
            WebElement webElement = getElement(xpath, waitTime);
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", webElement, "style", "border: 2px solid orange; color: orange; font-weight: bold;");
            webElement.click();
            log.info("FIRST CLICK FAILED!  Attempted 2nd click on  "+xpath);
        }
    }

    public void doubleClickElement(String xpath, int waitTime)
    {
        try
        {
            WebElement webElement = getElement(xpath, waitTime);
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", webElement, "style", "border: 2px solid yellow; color: green; font-weight: bold;");
            Actions action = new Actions(driver);
            action.doubleClick(webElement).perform();
            log.info("Attempted 1st DOUBLE-click on  "+xpath);
        }
        catch (Exception e)
        {
            //If click fails, attempt a brief sleep and click again
            try {
                Thread.sleep(TYPICAL_INTERVAL);
            } catch (InterruptedException e1) {
            }
            waitForElementToNotExist("//div[@class='blockUI blockOverlay']",waitTime);  //TODO: Remove this - it exists for experimentation on Jenkins
            waitForElementToNotExist("//div[@class='ui-widget-overlay']", waitTime);
            WebElement webElement = getElement(xpath, waitTime);
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", webElement, "style", "border: 2px solid orange; color: blue; font-weight: bold;");
            Actions action = new Actions(driver);
            action.doubleClick(webElement).perform();
            log.info("Attempted 1st DOUBLE-click on  "+xpath);
            log.info("FIRST CLICK FAILED!  Attempted 2nd click on  "+xpath);
        }
    }

    public String getString(String xpath)
    {
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public String getValue(String xpath)
    {
        return driver.findElement(By.xpath(xpath)).getAttribute("value");
    }

    public void sendKeysToElement(String keys, String xpath, int maxWaitTime)
    {
        waitForElementToBeVisible(xpath, maxWaitTime);
        WebElement webElement = driver.findElement(By.xpath(xpath));
        webElement.clear(); //Clears element before sending keys.  If we need to append text to existing text field, a new method will be needed.
        webElement.sendKeys(keys);
    }

    public void clearTextElement(String xpath, int maxWaitTime)
    {
        waitForElementToBeVisible(xpath, maxWaitTime);
        WebElement webElement = driver.findElement(By.xpath(xpath));
        webElement.clear();
    }

    public void sendKeysToElementWithoutClearing(String keys, String xpath, int maxWaitTime)
    {
        waitForElementsToExist(xpath, maxWaitTime, 1);
        WebElement webElement = driver.findElement(By.xpath(xpath));
        webElement.sendKeys(keys);
    }

    public void moveToElement(String xpath, int maxWaitTime)
    {
        WebElement webElement = getElement(xpath, maxWaitTime);
        Actions Act = new Actions(driver);
        Act.moveToElement(webElement).build().perform();
    }
    //endregion

    public void temporaryTimeTest()
    {
        String timeStart = new SimpleDateFormat("HH.mm.ss.").format(new java.util.Date());
        log.info("   TIMER TEST:  Started: "+timeStart);
        log.info("   TIMER TEST:  Sleeping for 60,000 ms");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String timeEnd = new SimpleDateFormat("HH.mm.ss.").format(new java.util.Date());
        log.info("   TIMER TEST:  Ended: "+timeEnd);
    }
}
