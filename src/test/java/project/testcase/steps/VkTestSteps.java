package project.testcase.steps;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import project.apiutil.ProjectApiUtil;
import project.models.Like;
import project.pageobjects.EmailPage;
import project.pageobjects.FeedPage;
import project.pageobjects.PasswordPage;
import project.pageobjects.ProfilePage;

/**
 * @author Pavel Romanov 13.03.2023
 */

@Slf4j
public class VkTestSteps {
    private static EmailPage emailPage = new EmailPage();
    private static PasswordPage passwordPage = new PasswordPage();
    private static FeedPage feedPage = new FeedPage();
    private static ProfilePage profilePage = new ProfilePage();

    public static void checkEmailPage() {
        Assert.assertTrue(emailPage.isEmailPageDisplayed(), "Страница авторизации не открылась");
    }

    public static void inputEmail(String email) {
        emailPage.enterEmail(email);
    }

    public static void confirmEmail() {
        emailPage.clickSignInBtn();
    }

    public static void checkPassportPage() {
        Assert.assertTrue(passwordPage.isPasswordPageDisplayed(), "Страница для ввода пароля не открылась.");
    }

    public static void inputPassword(String password) {
        passwordPage.enterPassword(password);
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

    public static void checkWallPostPhoto(String imagePath, String photoId) {
        Assert.assertTrue(profilePage.isPostPhotoCorrect(imagePath, photoId),
                "Ожидаемая и фактическая картинка не одинаковые.");
    }

    public static void checkWallPostCommentOwner(String owner, String comment_id) {
        Assert.assertTrue(profilePage.isCommentCorrect(owner, comment_id),
                "Комментарий не добавился или пользователь неправильный.");
    }

    public static void like() {
        profilePage.clickLikeBtn();
    }

    public static void checkLikeOwner(Like like, int likedValue) {
        Assert.assertTrue(Integer.valueOf(ProjectApiUtil.getLiked(like).getLiked()) == likedValue,
                "Лайк не поставлен или пользователь некорректен.");
    }

    public static void checkWallPostDeleted() {
        Assert.assertFalse(profilePage.isPostDeleted(), "Запись не удалилась.");
    }
}
