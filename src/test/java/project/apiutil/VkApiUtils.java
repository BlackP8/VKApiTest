package project.apiutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.ec.rfc8032.Ed448;
import project.models.*;
import restframework.RequestManager;
import universaltools.ConfigUtil;

import java.io.File;

/**
 * @author Pavel Romanov 09.03.2023
 */

@Slf4j
public class VkApiUtils {
    private static final String TOKEN_PARAM_NAME = "vkToken";
    private static final String WALL_POST_REQUEST_PARAM_NAME = "wallPostRequest";
    private static final String WALL_POST_SERVER_PARAM_NAME = "wallPhotosServer";
    private static final String POST_COMMENT_PARAM_NAME = "addPostComment";
    private static final String SAVE_WALL_PHOTO_PARAM_NAME = "saveFileOnServerRequest";
    private static final String EDIT_POST_REQUEST_PARAM_NAME = "editPostRequest";
    private static final String PHOTO_ID_RESPONSE_PATH = "response.id";
    private static final String MEDIA_ID_TEMPLATE = "photo%1$s_%2$s";
    private static final String LIKED_RESPONSE_TYPE_PARAM = "post";
    private static final String LIKED_RESPONSE_PARAM_NAME = "isLikedRequest";
    private static final String DELETE_POST_PARAM_NAME = "deletePostRequest";
    private static final String DELETE_PHOTO_PARAM_NAME = "deletePhotoRequest";
    private static final String TOKEN = ConfigUtil.getConfProperty(TOKEN_PARAM_NAME);

    public static int createWallPost(String ownerId, String postText) {
        String request = String.format(ConfigUtil.getConfProperty(WALL_POST_REQUEST_PARAM_NAME), ownerId, TOKEN, postText);
        ResponseWrapper wallPost = RequestManager.executeGet(request).getBody().as(ResponseWrapper.class);
        return wallPost.getResponse().getPost_id();
    }

    public static String uploadPhotoOnServer(String imagePath) {
        ResponseWrapper server = RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(WALL_POST_SERVER_PARAM_NAME),
                TOKEN)).getBody().as(ResponseWrapper.class);
        File image = new File(imagePath);
        ObjectMapper mapper = new ObjectMapper();
        Response sentFile = null;
        try {
            sentFile = mapper.readValue(RequestManager.executePostWithFile(server.getResponse().getUpload_url(), image)
                    .getBody().asString(), Response.class);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        String request = String.format(ConfigUtil.getConfProperty(SAVE_WALL_PHOTO_PARAM_NAME), TOKEN,
                sentFile.getServer(), sentFile.getHash());
        return String.valueOf(RequestManager.executeasd(request, sentFile.getPhoto()).jsonPath()
                .getList(PHOTO_ID_RESPONSE_PATH).get(0));
    }

    public static void editPost(String newText, String ownerId, String postId, String photoId) {
        String media_Id = String.format(MEDIA_ID_TEMPLATE, ownerId, photoId);
        RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(EDIT_POST_REQUEST_PARAM_NAME), TOKEN, ownerId,
                postId, newText, media_Id));
    }

    public static int addComment(String commentText, int postId, String owner) {
        ResponseBody body = RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(POST_COMMENT_PARAM_NAME),
                owner, TOKEN, postId, commentText)).getBody();
        ResponseWrapper comment = body.as(ResponseWrapper.class);
        return comment.getResponse().getComment_id();
    }

    public static boolean isLikeUserCorrect(int postId, String owner) {
        ResponseWrapper like = RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(LIKED_RESPONSE_PARAM_NAME),
                TOKEN, owner, LIKED_RESPONSE_TYPE_PARAM, owner, postId)).getBody().as(ResponseWrapper.class);
        return like.getResponse().getLiked() == 1;
    }

    public static void deletePost(int postId, String owner) {
        RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(DELETE_POST_PARAM_NAME), TOKEN, owner, postId));
    }

    public static void deleteAddedPhoto(String owner, String photoId) {
        RequestManager.executeGet(String.format(ConfigUtil.getConfProperty(DELETE_POST_PARAM_NAME), TOKEN, owner, photoId));
    }
}
