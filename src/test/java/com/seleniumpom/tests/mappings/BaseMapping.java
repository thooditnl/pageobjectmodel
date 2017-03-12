package com.seleniumpom.tests.mappings;

public abstract class BaseMapping {

    private static BaseMapping instance;

    //TODO: move to a .props file, as it appears that linux Thread.sleep runs faster than on windows. (see stackoverflow: http://stackoverflow.com/questions/3956512/java-performance-issue-with-thread-sleep)
    // Temporarily increasing the values in these timeouts to see if that results in greater stability on linxu (as linux reports searching for 14,000 ms for an element but the page never takes longer than
    // 6 seconds to load when visually inspected via the linux machine.
    public final static int LONG_TIMEOUT = 40000; //40 second wait should be maximum for pages like login, reporting and statistics
    public final static int TIMEOUT = 14000; //14 second wait should be maximum for TYPICAL page content to become visible
    public final static int BRIEF_TIMEOUT = 3000;  //3 second wait for when test should check for an element but test writer is reasonably sure the element won't exist in the first place
    public final static int QUICK_TIMEOUT = 500; //.5 second wait for elements we assume are already on the page and visible


    //region Generic Xpath Selectors
    public String anyElementContainingText(String text)
    {
        return String.format("//*[contains(text(),'%s')]", text);
    }

    public String buttonWithText(String text)
    {
        return String.format("//button[text()='%s']", text);
    }

    public String buttonHyperlinkWithText(String text)
    {
        return String.format("//a[text()='%s']", text);
    }

    public String buttonHyperlinkContainingSpanWithWithText(String text)
    {
        return String.format("//a//span[text()='%s']", text);
    }

    public String buttonContainingSpanWithText(String text)
    {
        return String.format("//button/span[text()='%s']", text);
    }

    public String actionMenuDropdownIcon = "//span[@class='tnlif fa fa-caret-down active-icon']";

    public String inputFieldInRowWithText(String text)
    {
        return rowContainingText(text) + "//input";
    }

    public String textAreaFieldInRowWithText(String text)
    {
        return rowContainingText(text) + "//textarea";
    }

    public String textAreaWithId(String id)
    {
        return String.format("//textarea[@id='%s']", id);
    }

    public String rowContainingText(String text)
    {
        return String.format("(//*[normalize-space()='%s']//ancestor::tr)[last()]", text);
    }

    public String overlay()
    {
        return ("//div[@class='blockUI blockOverlay']");
    }

    public String linkSpanWithText(String text) //Might refactor to just call this linkWithText - depending on how many more cases of //a[text()='... that I find
    {
        return String.format("//a/span[text()='%s']", text);
    }

    public String linkSpanWithNormalizedText(String text)
    {
        return String.format("//a/span[normalize-space()='%s']", text);
    }

    public String linkWithText(String text)
    {
        return String.format("//a[text()='%s']", text);
    }

    public String linkWithNormalizedSpace(String text)
    {
        return String.format("//a[normalize-space()='%s']", text);
    }

    public String linkContainsNormalizedSpace(String text) { return String.format("//a[contains(normalize-space(),'%s')]", text); }

    public String exactText(String text)
    {
        return String.format("//*[text()='%s']", text);
    }

    public String normalizedExactText(String text)
    {
        return String.format("//*[normalize-space()='%s']", text);
    }

    public String buttonHyperlinkWithTextInRowWithText(String buttonText, String rowText)
    {
        return rowContainingText(rowText) + buttonHyperlinkWithText(buttonText);
    }

    public String buttonWithTextInRowWithText(String buttonText, String rowText)
    {
        return rowContainingText(rowText) + buttonWithText(buttonText);
    }

    public String textInRowWithOtherText(String text, String otherText)
    {
        return (rowContainingText(otherText) + normalizedExactText(text));
    }


    //endregion

    public String rowContainingNormalizedExactText(String text)
    {
        return String.format("(//tr[contains(normalize-space(),'%s')])[last()]", text);
    }

    //region Common Elements
    public String homeTabIcon = "//span[@class='tnl-nav-home icon-font fa fa-home']";

    public String inputFieldFollowingLabelWithText(String text)
    {
        return String.format("//label[text()='%s']/following-sibling::*/input", text);
    }

   public String stopMasqueradingLink() {
       return linkSpanWithText("Stop masquerading.");
   }

    public String xButtonToCloseDialogWindow() {
        return "//span[@class='ui-button-icon-primary ui-icon ui-icon-closethick']";
    }

    public String fileUploadLink() {
        return "//input[@type='file']";
    }

    public String inputButtonWithValue(String buttonText)
    {
        return String.format("//input[@value='%s']", buttonText);
    }

    public String optionWithTextInDropdown(String optionText)
    {
        return String.format("//select/option[text()='%s']", optionText);
    }

    public String optionWithTextInDropdownInRowWithText(String rowText, String optionText)
    {
        return rowContainingText(rowText) + optionWithTextInDropdown(optionText);
    }

    public String dropdownWithId(String id)
    {
        return String.format("//select[@id='%s']", id);
    }

    public String optionWithTextInDropdownWithId(String optionText, String dropdownId)
    {
        return String.format(dropdownWithId(dropdownId) + "//option[text()='%s']", optionText);
    }

    //endregion

    //region Expandable Elements (+plus and -minus buttons for expanding tree nodes)
    //Warning: This xpath may be brittle due to the nature of '+' button tree structure
    public String PlusButtonInRowWithText(String text)
    {
        return String.format("(//li[contains(normalize-space(),'%s')]/a/img[@src='/images/icons/plus-big.gif'])[1]",text);
    }

    public String checkboxInGridRowWithText(String text) {
                return String.format("//td[normalize-space()='%s']/parent::tr//input[@type='checkbox']", text);
           }


    public String CheckboxInListWithTextAndInputValue(String text, String inputValue)
    {
        return String.format("//li[contains(normalize-space(),'%s')]/input[@type='checkbox'][@value='%s']",text, inputValue);
    }

    //Used to identify the checkbox that is immediately preceded by another checkbox (used for determining if checkboxes are displayed in order)
    public String CheckboxInListWithTextAndInputValueFollowedByCheckboxInListWithTextAndInputValue(String firstCheckboxText, String firstCheckboxValue, String secondCheckboxText, String secondCheckboxValue)
    {
        return (CheckboxInListWithTextAndInputValue(firstCheckboxText, firstCheckboxValue) + String.format("/following::li[1][contains(normalize-space(),'%s')]/input[@type='checkbox'][@value='%s']", secondCheckboxText, secondCheckboxValue));

    }
    //endregion

    //region This area for selectors used to validate page loads -- we might get rid of it if we don't see benefit from its use

    public String headerOnPageWithText(String text) {
        return String.format("//h1[text()='%s']", text);
    }

    //endregion

    public String dropdownWithIdByXpath(String id) {
        return String.format("//select[@id='%s']", id);
    }

    public String optionWithTextInDropdownWithIdByXpath(String optionText, String dropdownId) {
        return String.format(dropdownWithIdByXpath(dropdownId) + "//option[text()='%s']", optionText);
    }

    //region Common Elements in the New UI (like New Observations or Programs)

    public String divRowWithText(String text)
    {
        return String.format("//div[normalize-space()='%s']/parent::div[@class='row']", text);
    }

    public String newUiXButtonToCloseModalWindow()
    {
        return buttonContainingSpanWithText("×");
    }

    //endregion
}
