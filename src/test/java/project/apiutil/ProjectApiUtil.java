package project.apiutil;

import project.endpoint.Endpoint;
import project.models.*;
import restframework.FrameApiUtil;
import restframework.universalutils.ConfigUtil;
import restframework.universalutils.ConvertUtil;

import java.io.File;

/**
 * Класс-утилита для работы с результатами API запросов из методов фреймворка в соответствии с заданием
 * @author Pavel Romanov 09.03.2023
 */

public class ProjectApiUtil {
    private static final String BASE_URL_PARAM_NAME = "apiRequestURL";
    private static final String PHOTO_ID_RESPONSE_PATH = "response.id";
    private static final String RESPONSE_JSONPATH = "response";

    /**
     * Метод для создания новой записи на стене
     * @param createdPost
     */
    public static Post createWallPost(Post createdPost) {
        return ConvertUtil.convertMapToModel(FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME),
                ConvertUtil.convertModelToMap(createdPost), Endpoint.WALL_POST.getStringValue()).jsonPath()
                .getJsonObject(RESPONSE_JSONPATH), Post.class);
    }

    /**
     * Метод для получения URL для загрузки фотографии на сервер VK
     * @param addedPhoto
     */
    private static Photo getServerUploadUrl(Photo addedPhoto) {
        return ConvertUtil.convertMapToModel(FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME),
                ConvertUtil.convertModelToMap(addedPhoto), Endpoint.WALL_UPLOAD_SERVER.getStringValue()).jsonPath()
                .getJsonObject(RESPONSE_JSONPATH), Photo.class);
    }

    /**
     * Метод для передачи файла из локального хранилища на сервер
     * @param imagePath
     * @param addedPhoto
     */
    private static Photo transferFileOnServer(String imagePath, Photo addedPhoto) {
        return ConvertUtil.readValueJsonString(FrameApiUtil.postWithFile(getServerUploadUrl(addedPhoto)
                .getUpload_url(), new File(imagePath)).getBody().asString(), Photo.class);
    }

    /**
     * Метод для сохранения переданного файла на сервер VK
     * @param imagePath
     * @param version
     */
    public static String savePhotoOnServer(String imagePath, Photo addedPhoto, String version, String token) {
        Photo photoToSave = transferFileOnServer(imagePath, addedPhoto);
        photoToSave.setAccess_token(token);
        photoToSave.setV(version);
        return String.valueOf(FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME), ConvertUtil.convertModelToMap(photoToSave),
                        Endpoint.SAVE_WALL_PHOTO.getStringValue()).jsonPath().getList(PHOTO_ID_RESPONSE_PATH).get(0));
    }

    /**
     * Метод для изменения текста и картинки записи на стене
     * @param editedPost
     */
    public static void editPost(Post editedPost) {
        FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME), ConvertUtil.convertModelToMap(editedPost),
                Endpoint.WALL_EDIT.getStringValue());
    }

    /**
     * Метод для добавления комментария к передаваемой записи на стене
     * @param comment
     */
    public static Post addComment(Post comment) {
        return ConvertUtil.convertMapToModel(FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME),
                ConvertUtil.convertModelToMap(comment), Endpoint.CREATE_COMMENT.getStringValue()).jsonPath()
                .getJsonObject(RESPONSE_JSONPATH), Post.class);
    }

    /**
     * Метод для проверки лайка от правильного пользователя
     * @param like
     */
    public static Like getLiked(Like like) {
        return ConvertUtil.convertMapToModel(FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME),
                ConvertUtil.convertModelToMap(like), Endpoint.LIKED.getStringValue()).jsonPath()
                .getJsonObject(RESPONSE_JSONPATH), Like.class);
    }

    /**
     * Метод для удаления определенного поста со стены
     * @param deletablePost
     */
    public static void deletePost(Post deletablePost) {
        FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME), ConvertUtil.convertModelToMap(deletablePost),
                Endpoint.WALL_DELETE.getStringValue());
    }

    /**
     * Метод для удаления добавленной фотографии из альбома
     * @param deletablePhoto
     */
    public static void deleteAddedPhoto(Photo deletablePhoto) {
        FrameApiUtil.getWithParam(ConfigUtil.getConfProperty(BASE_URL_PARAM_NAME), ConvertUtil.convertModelToMap(deletablePhoto),
                Endpoint.DELETE_PHOTO.getStringValue());
    }
}
