package project.testcase;

import org.testng.Assert;
import org.testng.annotations.Test;
import project.apiutil.VkApiUtils;
import project.base.BaseTest;
import project.pageobjects.EmailPage;
import project.pageobjects.FeedPage;
import project.pageobjects.PasswordPage;
import project.pageobjects.ProfilePage;
import universaltools.ConfigUtil;
import universaltools.DataGeneratorUtil;
import universaltools.DataProviderUtil;

/**
 * @author Pavel Romanov 09.03.2023
 */

public class VkWallTest extends BaseTest {
    private static final String EMAIL_KEY = "login";
    private static final String PASSWORD_KEY = "password";

    @Test(dataProviderClass = DataProviderUtil.class, dataProvider = "testData")
    public void wallTest(String textLength, String owner, String imagePath) {
        EmailPage emailPage = new EmailPage();
        Assert.assertTrue(emailPage.isMainPageDisplayed(), "Страница авторизации не открылась");

        emailPage.enterEmail(ConfigUtil.getCredentials(EMAIL_KEY));
        emailPage.clickSignInBtn();
        PasswordPage passwordPage = new PasswordPage();
        Assert.assertTrue(passwordPage.isPasswordPageDisplayed(), "Страница для ввода пароля не открылась.");

        passwordPage.enterPassword(ConfigUtil.getCredentials(PASSWORD_KEY));
        passwordPage.clickSubmitBtn();

        FeedPage feedPage = new FeedPage();
        Assert.assertTrue(feedPage.isFeedPageDisplayed(), "Лента с новостями не открылась.");

        feedPage.clickMyPageLink();
        ProfilePage profilePage = new ProfilePage();
        Assert.assertTrue(profilePage.isProfilePageDisplayed(), "Страница профиля не открылась.");

        String postText = DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength));
        int wallPostId = VkApiUtils.createWallPost(owner, postText);
        Assert.assertTrue(profilePage.isPostOwnerCorrect(owner,  String.valueOf(wallPostId)),
                "Запись не появилась или пользователь не корректен.");
        Assert.assertTrue(profilePage.isPostTextCorrect(postText), "Текст записи не соответствует ожидаемому.");

        postText = DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength));
        String mediaId = VkApiUtils.uploadPhotoOnServer(imagePath);
        VkApiUtils.editPost(postText, owner, String.valueOf(wallPostId), mediaId);
        Assert.assertTrue(profilePage.isPostTextCorrect(postText), "Текст записи не изменился.");
        Assert.assertTrue(profilePage.isPostPhotoCorrect(mediaId), "Ожидаемая и фактическая картинка не одинаковые.");

        int comment_id = VkApiUtils.addComment(DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength)),
                wallPostId, owner);
        Assert.assertTrue(profilePage.isCommentCorrect(owner, String.valueOf(comment_id)),
                "Комментарий не добавился или пользователь неправильный.");

        profilePage.clickLikeBtn();
        Assert.assertTrue(VkApiUtils.isLikeUserCorrect(wallPostId, owner),
                "Лайк не поставлен или пользователь некорректен.");

        VkApiUtils.deletePost(wallPostId, owner);
        Assert.assertFalse(profilePage.isPostDeleted(), "Запись не удалилась.");
        VkApiUtils.deleteAddedPhoto(owner, mediaId);
    }
}
