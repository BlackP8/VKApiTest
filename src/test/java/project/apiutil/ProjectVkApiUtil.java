package project.apiutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import project.models.*;
import restframework.FrameVkApiUtil;
import restframework.universalutils.ConfigUtil;

import java.io.File;

/**
 * Класс-утилита для работы с результатами API запросов из методов фреймворка в соответствии с заданием
 * @author Pavel Romanov 09.03.2023
 */

@Slf4j
public class ProjectVkApiUtil {
    private static final String TOKEN_PARAM_NAME = "vkToken";
    private static final String WALL_POST_REQUEST_PARAM_NAME = "wallPostRequest";
    private static final String WALL_POST_SERVER_PARAM_NAME = "wallPhotosServer";
    private static final String POST_COMMENT_PARAM_NAME = "addPostComment";
    private static final String SAVE_FILE_PARAM_NAME = "saveFileOnServerRequest";
    private static final String EDIT_POST_REQUEST_PARAM_NAME = "editPostRequest";
    private static final String PHOTO_ID_RESPONSE_PATH = "response.id";
    private static final String MEDIA_ID_TEMPLATE = "photo%1$s_%2$s";
    private static final String LIKED_RESPONSE_TYPE_PARAM = "post";
    private static final String LIKED_RESPONSE_PARAM_NAME = "isLikedRequest";
    private static final String DELETE_POST_PARAM_NAME = "deletePostRequest";
    private static final String DELETE_PHOTO_PARAM_NAME = "deletePhotoRequest";
    private static final String PHOTO_PARAM_NAME = "photo";
    private static final String TOKEN = ConfigUtil.getConfProperty(TOKEN_PARAM_NAME);
    private static final int CORRECT_LIKED_VALUE = 1;

    /**
     * Метод для создания новой записи на стене
     * @param ownerId
     * @param postText
     */
    public static int createWallPost(String ownerId, String postText) {
        String request = String.format(ConfigUtil.getConfProperty(WALL_POST_REQUEST_PARAM_NAME), ownerId, TOKEN, postText);
        ResponseWrapper wallPost = FrameVkApiUtil.get(request).getBody().as(ResponseWrapper.class);
        return wallPost.getResponse().getPost_id();
    }

    /**
     * Метод для получения URL для загрузки фотографии на сервер VK
     */
    private static String getServerUploadUrl() {
        ResponseWrapper server = FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(WALL_POST_SERVER_PARAM_NAME),
                TOKEN)).getBody().as(ResponseWrapper.class);
        return server.getResponse().getUpload_url();
    }

    /**
     * Метод для передачи файла из локального хранилища на сервер
     * @param imagePath
     */
    private static Response transferFileOnServer(String imagePath) {
        File image = new File(imagePath);
        Response sentFileParams = null;
        try {
            sentFileParams = new ObjectMapper().readValue(FrameVkApiUtil.executePostWithFile(getServerUploadUrl(), image)
                    .getBody().asString(), Response.class);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return sentFileParams;
    }

    /**
     * Метод для сохранения переданного файла на сервер VK
     * @param imagePath
     */
    public static String savePhotoOnServer(String imagePath) {
        Response fileParams = transferFileOnServer(imagePath);
        String request = String.format(ConfigUtil.getConfProperty(SAVE_FILE_PARAM_NAME), TOKEN, fileParams.getServer(),
                fileParams.getHash());
        return String.valueOf(FrameVkApiUtil.getWithParam(request, fileParams.getPhoto(), PHOTO_PARAM_NAME).jsonPath()
                .getList(PHOTO_ID_RESPONSE_PATH).get(0));
    }

    /**
     * Метод для изменения текста и картинки записи на стене
     * @param newText
     * @param ownerId
     * @param postId
     * @param photoId
     */
    public static void editPost(String newText, String ownerId, String postId, String photoId) {
        String media_Id = String.format(MEDIA_ID_TEMPLATE, ownerId, photoId);
        FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(EDIT_POST_REQUEST_PARAM_NAME), TOKEN, ownerId,
                postId, newText, media_Id));
    }

    /**
     * Метод для добавления комментария к передаваемой записи на стене
     * @param commentText
     * @param postId
     * @param owner
     */
    public static int addComment(String commentText, int postId, String owner) {
        ResponseBody body = FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(POST_COMMENT_PARAM_NAME),
                owner, TOKEN, postId, commentText)).getBody();
        ResponseWrapper comment = body.as(ResponseWrapper.class);
        return comment.getResponse().getComment_id();
    }

    /**
     * Метод для проверки лайка от правильного пользователя
     * @param postId
     * @param owner
     */
    public static boolean isLikeUserCorrect(int postId, String owner) {
        ResponseWrapper like = FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(LIKED_RESPONSE_PARAM_NAME),
                TOKEN, owner, LIKED_RESPONSE_TYPE_PARAM, owner, postId)).getBody().as(ResponseWrapper.class);
        return like.getResponse().getLiked() == CORRECT_LIKED_VALUE;
    }

    /**
     * Метод для удаления определенного поста со стены
     * @param postId
     * @param owner
     */
    public static void deletePost(int postId, String owner) {
        FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(DELETE_POST_PARAM_NAME), TOKEN, owner, postId));
    }

    /**
     * Метод для удаления добавленной фотографии из альбома
     * @param owner
     * @param photoId
     */
    public static void deleteAddedPhoto(String owner, String photoId) {
        FrameVkApiUtil.get(String.format(ConfigUtil.getConfProperty(DELETE_PHOTO_PARAM_NAME), TOKEN, owner, photoId));
    }
}
