package project.testcase;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import project.apiutil.ProjectVkApiUtil;
import project.base.BaseTest;
import project.testcase.steps.VkTestSteps;
import restframework.universalutils.DataGeneratorUtil;
import restframework.universalutils.DataProviderUtil;

/**
 * @author Pavel Romanov 09.03.2023
 */

@Slf4j
public class VkWallTest extends BaseTest {
    @Test(dataProviderClass = DataProviderUtil.class, dataProvider = "testData")
    public void wallTest(String textLength, String owner, String imagePath) {
        log.info("Открываем и проверяем открытие страницы ввода email.");
        VkTestSteps.checkEmailPage();

        log.info("Вводим email и нажимаем кнопку входа.");
        VkTestSteps.inputEmail();
        VkTestSteps.confirmEmail();

        log.info("Открываем и проверяем открытие страницы ввода пароля.");
        VkTestSteps.checkPassportPage();

        log.info("Вводим пароль и нажимаем кнопку подтверждения.");
        VkTestSteps.inputPassword();
        VkTestSteps.confirmPassword();

        log.info("Открываем и проверяем открытие страницы новостей.");
        VkTestSteps.checkFeedPage();

        log.info("Переходим на страницу профиля и проверяем ее открытие.");
        VkTestSteps.goToProfilePage();
        VkTestSteps.checkProfilePage();

        log.info("Создаем запись на стене и проверяем ее автора и текст.");
        String postText = DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength));
        int wallPostId = ProjectVkApiUtil.createWallPost(owner, postText);
        VkTestSteps.checkWallPostOwner(owner, String.valueOf(wallPostId));
        VkTestSteps.checkWallPostText(postText);

        log.info("Меняем текст и добавляем фото на созданную запись, проверяем изменения.");
        postText = DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength));
        String mediaId = ProjectVkApiUtil.savePhotoOnServer(imagePath);
        ProjectVkApiUtil.editPost(postText, owner, String.valueOf(wallPostId), mediaId);
        VkTestSteps.checkWallPostText(postText);
        VkTestSteps.checkWallPostPhoto(mediaId);

        log.info("Добавляем комментарий к созданной записи и проверяем его автора.");
        int comment_id = ProjectVkApiUtil.addComment(DataGeneratorUtil.generateRandomString(Integer.valueOf(textLength)),
                wallPostId, owner);
        VkTestSteps.checkWallPostCommentOwner(owner, String.valueOf(comment_id));

        log.info("Добавляем отметку лайка и проверяем его автора.");
        VkTestSteps.like();
        VkTestSteps.checkLikeOwner(wallPostId, owner);

        log.info("Удаляем созданную запись на стене и проверяем, что ее нет.");
        ProjectVkApiUtil.deletePost(wallPostId, owner);
        VkTestSteps.checkWallPostDeleted();
        ProjectVkApiUtil.deleteAddedPhoto(owner, mediaId);
    }
}
