package project.pageobjects;

import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class FeedPage extends Form {
    private static final String FEED_PAGE_IDENTIFIER = "main_feed";
    private static final String FEED_PAGE_IDENTIFIER_NAME = "feed_block";

    private final ILink myPageLink = getElementFactory().getLink(By.id("l_pr"), "my_page_link");

    public FeedPage() {
        super(By.id(FEED_PAGE_IDENTIFIER), FEED_PAGE_IDENTIFIER_NAME);
    }

    public boolean isFeedPageDisplayed() {
        this.state().waitForDisplayed();
        return this.state().isExist();
    }

    public void clickMyPageLink() {
        myPageLink.click();
    }
}
