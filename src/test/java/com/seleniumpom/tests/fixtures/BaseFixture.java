package com.seleniumpom.tests.fixtures;

import com.seleniumpom.tests.framework.WebDriverWrapper;
import com.seleniumpom.tests.mappings.BaseMapping;
import org.openqa.selenium.StaleElementReferenceException;


public class BaseFixture {

    private WebDriverWrapper selenium;
    private BaseMapping map;

    public BaseFixture(WebDriverWrapper selenium, BaseMapping map)
    {
        this.selenium = selenium;
        this.map = map;
    }

    /*Basic browser functions
    Navigate
    RefreshPage
    BrowserBack
    HandleJavascriptAlert
    */

    //region Basic Element Interactions
    public void sendKeysToElement(String keys, String xPath, int maxWaitTime)
    {
        selenium.clearTextElement(xPath,maxWaitTime);
        waitOnOverlay();
        selenium.sendKeysToElement(keys, xPath, maxWaitTime);
    }

    public void sendKeysWithoutClearingToElementBy(String keys, String xPath, int maxWaitTime)
    {
        waitOnOverlay();
        selenium.sendKeysToElementWithoutClearing(keys, xPath, maxWaitTime);
    }

    public void waitForElementToLoad(String xPath, int maxWaitTime)
    {
        selenium.waitForElementToBeVisible(xPath, maxWaitTime);
    }

    public void waitForElementsToExist(String xPath, int maxWaitTime, int expectedCount)
    {
        selenium.waitForElementsToExist(xPath, maxWaitTime, expectedCount);
    }

    public void waitForElementToNotBeVisible(String xpath, int maxWaitTime)
    {
        selenium.waitForElementToNotBeVisible(xpath, maxWaitTime);
    }

    public void clickElement(String xPath)
    {
        clickElement(xPath, map.TIMEOUT);
    }

    public void clickElement(String xPath, int timeOut)
    {
        selenium.waitForElementToBeVisible(xPath, timeOut);
        waitOnOverlay();
        selenium.clickElement(xPath, timeOut);
    }

    public void doubleClickElement(String xPath, int timeOut)
    {
        selenium.waitForElementToBeVisible(xPath, timeOut);
        waitOnOverlay();
        selenium.doubleClickElement(xPath, timeOut);
    }

    private void waitOnOverlay()
    {
        selenium.waitForElementToNotExist(map.overlay(),map.TIMEOUT);
    }

    public boolean isElementVisible(String xpath)
    {
        return isElementVisibleWithTimeout(xpath, map.TIMEOUT);
    }

    public boolean isElementVisibleWithTimeout(String xpath, int timeout)
    {
        try {
            return selenium.elementIsVisible(xpath, timeout);
        }
        catch (StaleElementReferenceException e){
            return selenium.elementIsVisible(xpath, timeout);
        }
    }

    public int getElementCount(String xPath, int timeout)
    {
        return selenium.getCountOfElements(xPath, timeout);
    }

    public boolean isTextInRowWithOtherText(String text1, String text2)
    {
        return selenium.elementIsVisible(map.textInRowWithOtherText(text1, text2),map.TIMEOUT);
    }
    //endregion

    public void moveToElement(String xpath, int timeout)
    {
        selenium.moveToElement(xpath, timeout);
    }

    public void clickLinkWithText(String text)
    {
        clickElement(map.linkWithText(text));
    }

    public void clickOnXButtonToCloseDialogWindow()
    {
        clickElement(map.xButtonToCloseDialogWindow());
    }

    public void selectOptionWithTextFromDropdown(String dropdownText, String optionText)
    {
        clickElement(map.dropdownWithIdByXpath(dropdownText));
        clickElement(map.optionWithTextInDropdownWithIdByXpath(optionText, dropdownText));
    }

    public void clickCheckboxInGridRowWithText(String text)
    {
        clickElement(map.checkboxInGridRowWithText(text));
    }

    public void uploadFile(String filePath)
    {
        sendKeysWithoutClearingToElementBy(filePath, map.fileUploadLink(), map.TIMEOUT);
    }

    //region Expand/Contract buttons (+plus and -minus buttons for expanding tree nodes)
    public void clickOnPlusButtonInRowWithText(String text)
    {
        moveToElement(map.PlusButtonInRowWithText(text), map.TIMEOUT);
        clickElement(map.PlusButtonInRowWithText(text));
    }
    //endregion

    //region This area for selectors used to validate page loads -- we might get rid of it if we don't see benefit from its use

    public boolean isTextOfPageHeaderVisible(String headerText)
    {
        return isElementVisible(map.headerOnPageWithText(headerText));
    }

    //endregion

    //region Common Elements in the New UI (like New Observations or Programs)

    public void clickOnXButtonToCloseNewUIModal()
    {
        clickElement(map.newUiXButtonToCloseModalWindow());
    }
}
