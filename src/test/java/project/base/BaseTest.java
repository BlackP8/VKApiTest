package project.base;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import universaltools.ConfigUtil;

/**
 * @author Pavel Romanov 09.03.2023
 */

public abstract class BaseTest {
    private static Browser browser;
    private static final String URL_PARAM_NAME = "url";

    @BeforeMethod
    public void setup(ITestContext context) {
        ConfigUtil.setConfig();
        ConfigUtil.setCredentials();
        browser = AqualityServices.getBrowser();
        browser.maximize();
        browser.goTo(ConfigUtil.getConfProperty(URL_PARAM_NAME));
        browser.waitForPageToLoad();
    }

    @AfterMethod
    public void quit() {
        if (AqualityServices.isBrowserStarted()) {
            browser.quit();
        }
    }
}
