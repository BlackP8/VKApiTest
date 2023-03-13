package project.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class ProfilePage extends Form {
    private static final String PROFILE_PAGE_IDENTIFIER = "//*[@class='ProfileHeader']";
    private static final String PROFILE_PAGE_IDENTIFIER_NAME = "profile_header";
    private static final String POST_ID_SAMPLE = "post%1$s_%2$s";
    private static final String PHOTO_ID_PARAM_NAME = "href";

    private final ITextBox postTextBox = getElementFactory().getTextBox(By.xpath("//*[contains(@class, 'post_text')]"),
            "post_text");
    private final ILink postAttach = getElementFactory().getLink(By.xpath("//*[contains(@class,'image_cover')]"),
            "post_attach_link");
    private final IButton likeButton = getElementFactory().getButton(By.xpath("//*[contains(@class,'PostButtonReactions--post')]"),
            "like_button");
    private final ILink showCommentsLink = getElementFactory().getLink(By.xpath("//*[contains(@class,'replies_next_label')]"),
            "show_comment_link");
    private static ITextBox post = null;

    public ProfilePage() {
        super(By.xpath(PROFILE_PAGE_IDENTIFIER), PROFILE_PAGE_IDENTIFIER_NAME);
    }

    public boolean isProfilePageDisplayed() {
        this.state().waitForDisplayed();
        return this.state().isExist();
    }

    public boolean isPostTextCorrect(String expectedText) {
        return postTextBox.getText().equals(expectedText);
    }

    public boolean isPostOwnerCorrect(String ownerId, String postId) {
        String id = String.format(POST_ID_SAMPLE, ownerId, postId);
        post = getElementFactory().getTextBox(By.id(id), "");
        post.getJsActions().scrollIntoView();
        return post.state().isDisplayed();
    }

    public boolean isPostPhotoCorrect(String photoId) {
        return postAttach.getAttribute(PHOTO_ID_PARAM_NAME).contains(photoId);
    }

    public boolean isCommentCorrect(String ownerId, String comment_id) {
        String id = String.format(POST_ID_SAMPLE, ownerId, comment_id);
        showCommentsLink.state().waitForDisplayed();
        if (showCommentsLink.state().isDisplayed()) {
            showCommentsLink.click();
        }
        getElementFactory().getTextBox(By.id(id), "").state().waitForDisplayed();
        return getElementFactory().getTextBox(By.id(id), "").state().isDisplayed();
    }

    public void clickLikeBtn() {
        likeButton.click();
    }

    public boolean isPostDeleted() {
        post.state().waitForNotDisplayed();
        return post.state().isDisplayed();
    }
}
