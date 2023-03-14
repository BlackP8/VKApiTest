package project.testcase.steps;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import project.apiutil.ProjectVkApiUtil;
import project.pageobjects.EmailPage;
import project.pageobjects.FeedPage;
import project.pageobjects.PasswordPage;
import project.pageobjects.ProfilePage;
import restframework.universalutils.ConfigUtil;

/**
 * @author Pavel Romanov 13.03.2023
 */

@Slf4j
public class VkTestSteps {
    private static final String EMAIL_KEY = "login";
    private static final String PASSWORD_KEY = "password";

    private static EmailPage emailPage = new EmailPage();
    private static PasswordPage passwordPage = new PasswordPage();
    private static FeedPage feedPage = new FeedPage();
    private static ProfilePage profilePage = new ProfilePage();

    public static void checkEmailPage() {
        Assert.assertTrue(emailPage.isEmailPageDisplayed(), "Страница авторизации не открылась");
    }

    public static void inputEmail() {
        emailPage.enterEmail(ConfigUtil.getCredentials(EMAIL_KEY));
    }

    public static void confirmEmail() {
        emailPage.clickSignInBtn();
    }

    public static void checkPassportPage() {
        Assert.assertTrue(passwordPage.isPasswordPageDisplayed(), "Страница для ввода пароля не открылась.");
    }

    public static void inputPassword() {
        passwordPage.enterPassword(ConfigUtil.getCredentials(PASSWORD_KEY));
    }

    public static void confirmPassword() {
        passwordPage.clickSubmitBtn();
    }

    public static void checkFeedPage() {
        Assert.assertTrue(feedPage.isFeedPageDisplayed(), "Лента с новостями не открылась.");
    }

    public static void goToProfilePage() {
        feedPage.clickMyPageLink();
    }

    public static void checkProfilePage() {
        Assert.assertTrue(profilePage.isProfilePageDisplayed(), "Страница профиля не открылась.");
    }

    public static void checkWallPostOwner(String owner, String wallPostId) {
        Assert.assertTrue(profilePage.isPostOwnerCorrect(owner,  wallPostId),
                "Запись не появилась или пользователь не корректен.");
    }

    public static void checkWallPostText(String expectedPostText) {
        Assert.assertTrue(profilePage.isPostTextCorrect(expectedPostText),
                "Текст записи не соответствует ожидаемому.");
    }

    public static void checkWallPostPhoto(String mediaId) {
        Assert.assertTrue(profilePage.isPostPhotoCorrect(mediaId),
                "Ожидаемая и фактическая картинка не одинаковые.");
    }

    public static void checkWallPostCommentOwner(String owner, String comment_id) {
        Assert.assertTrue(profilePage.isCommentCorrect(owner, comment_id),
                "Комментарий не добавился или пользователь неправильный.");
    }

    public static void like() {
        profilePage.clickLikeBtn();
    }

    public static void checkLikeOwner(int wallPostId, String owner) {
        Assert.assertTrue(ProjectVkApiUtil.isLikeUserCorrect(wallPostId, owner),
                "Лайк не поставлен или пользователь некорректен.");
    }

    public static void checkWallPostDeleted() {
        Assert.assertFalse(profilePage.isPostDeleted(), "Запись не удалилась.");
    }
}
