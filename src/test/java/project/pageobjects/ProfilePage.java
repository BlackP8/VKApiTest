package project.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILink;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import restframework.universalutils.ImageComparator;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class ProfilePage extends Form {
    private static final String PROFILE_PAGE_IDENTIFIER = "//*[@class='ProfileHeader']";
    private static final String PROFILE_PAGE_IDENTIFIER_NAME = "profile_header";
    private static final String POST_ID_SAMPLE = "post%1$s_%2$s";
    private static final String IMAGE_LINK_PATH = "//*[contains(@href, '%s')]";
    private static final String IMAGE_LINK_NAME = "image_link";
    private static final String IMAGE_SRC_PARAM = "src";

    private final ITextBox postTextBox = getElementFactory().getTextBox(By.xpath("//*[contains(@class, 'post_text')]"),
            "post_text");
    private final IButton likeButton = getElementFactory().getButton(By.xpath("//*[contains(@class,'PostButtonReactions--post')]"),
            "like_button");
    private final ILink showCommentsLink = getElementFactory().getLink(By.xpath("//*[contains(@class,'replies_next_label')]"),
            "show_comment_link");
    private final ILink openedPostImage = getElementFactory().getLink(By.xpath("//*[@id='pv_photo']//img"),
            "wall_post_image");
    private final IButton closeImageButton = getElementFactory().getButton(By.className("pv_close_btn"), "close_button");

    private static ITextBox post = null;
    private static ILink wallPostImageLink = null;

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

    public boolean isPostPhotoCorrect(String imagePath, String photoId) {
        wallPostImageLink = getElementFactory().getLink(By.xpath(String.format(IMAGE_LINK_PATH, photoId)), IMAGE_LINK_NAME);
        wallPostImageLink.click();
        openedPostImage.state().waitForDisplayed();
        boolean result = ImageComparator.isImagesEqual(imagePath, openedPostImage.getAttribute(IMAGE_SRC_PARAM));
        closeImageButton.click();
        return result;
    }

    private void clickShowCommentsLink() {
        showCommentsLink.state().waitForDisplayed();
        if (showCommentsLink.state().isDisplayed()) {
            showCommentsLink.click();
        }
    }

    public boolean isCommentCorrect(String ownerId, String comment_id) {
        String id = String.format(POST_ID_SAMPLE, ownerId, comment_id);
        clickShowCommentsLink();
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
