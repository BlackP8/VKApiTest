package project.testcase;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import project.apiutil.ProjectApiUtil;
import project.base.BaseTest;
import project.models.Like;
import project.models.Photo;
import project.models.Post;
import project.testcase.steps.VkTestSteps;
import restframework.universalutils.ConfigUtil;
import restframework.universalutils.DataGeneratorUtil;
import restframework.universalutils.DataProviderUtil;

/**
 * @author Pavel Romanov 09.03.2023
 */

@Slf4j
public class VkWallTest extends BaseTest {
    private static final int TEXT_LENGTH = 20;
    private static final int CORRECT_LIKED_VALUE = 1;
    private static final String EMAIL_KEY = "login";
    private static final String PASSWORD_KEY = "password";
    private static final String TOKEN_PARAM_NAME = "vkToken";
    private static final String postText = DataGeneratorUtil.generateRandomString(TEXT_LENGTH);
    private static final String commentText = DataGeneratorUtil.generateRandomString(TEXT_LENGTH);
    private static final String newPostText = DataGeneratorUtil.generateRandomString(TEXT_LENGTH);

    @Test(dataProviderClass = DataProviderUtil.class, dataProvider = "testData")
    public void wallTest(String owner, String imagePath, String version) {
        log.info("Открываем и проверяем открытие страницы ввода email.");
        VkTestSteps.checkEmailPage();

        log.info("Вводим email и нажимаем кнопку входа.");
        VkTestSteps.inputEmail(ConfigUtil.getCredentials(EMAIL_KEY));
        VkTestSteps.confirmEmail();

        log.info("Открываем и проверяем открытие страницы ввода пароля.");
        VkTestSteps.checkPassportPage();

        log.info("Вводим пароль и нажимаем кнопку подтверждения.");
        VkTestSteps.inputPassword(ConfigUtil.getCredentials(PASSWORD_KEY));
        VkTestSteps.confirmPassword();

        log.info("Открываем и проверяем открытие страницы новостей.");
        VkTestSteps.checkFeedPage();

        log.info("Переходим на страницу профиля и проверяем ее открытие.");
        VkTestSteps.goToProfilePage();
        VkTestSteps.checkProfilePage();

        log.info("Создаем запись на стене и проверяем ее автора и текст.");
        Post createdPost = Post.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).owner_id(owner)
                .message(postText).v(version).build();
        String wallPostId = ProjectApiUtil.createWallPost(createdPost).getPost_id();
        VkTestSteps.checkWallPostOwner(owner, wallPostId);
        VkTestSteps.checkWallPostText(postText);

        log.info("Меняем текст и добавляем фото на созданную запись.");
        Photo addedPhoto = Photo.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).v(version).build();
        String photoId = ProjectApiUtil.savePhotoOnServer(imagePath, addedPhoto, version, ConfigUtil.getConfProperty(TOKEN_PARAM_NAME));
        Post editedPost = Post.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).owner_id(owner)
                .post_id(wallPostId).message(newPostText).attachment(addedPhoto.getType() + owner + "_" + photoId).v(version).build();
        ProjectApiUtil.editPost(editedPost);

        log.info("Проверяем изменился ли текст и добавилась ли фотография.");
        VkTestSteps.checkWallPostText(newPostText);
        VkTestSteps.checkWallPostPhoto(imagePath, photoId);

        log.info("Добавляем комментарий к созданной записи и проверяем его автора.");
        Post addedComment = Post.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).owner_id(owner)
                .post_id(wallPostId).message(commentText).v(version).build();
        String comment_id = ProjectApiUtil.addComment(addedComment).getComment_id();
        VkTestSteps.checkWallPostCommentOwner(owner, comment_id);

        log.info("Добавляем отметку лайка и проверяем его автора.");
        VkTestSteps.like();
        Like like = Like.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).owner_id(owner)
                .user_id(owner).type(editedPost.getType()).item_id(wallPostId).v(version).build();
        VkTestSteps.checkLikeOwner(like, CORRECT_LIKED_VALUE);

        log.info("Удаляем созданную запись на стене и проверяем, что ее нет.");
        ProjectApiUtil.deletePost(editedPost);
        VkTestSteps.checkWallPostDeleted();

        log.info("Удаляем сохраненное фото.");
        Photo deletablePhoto = Photo.builder().access_token(ConfigUtil.getConfProperty(TOKEN_PARAM_NAME)).owner_id(owner)
                .photo_id(photoId).v(version).build();
        ProjectApiUtil.deleteAddedPhoto(deletablePhoto);
    }
}
