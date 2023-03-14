package restframework;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import java.io.File;

/**
 * Универсальный класс для выполнения GET и POST запросов и получения их результата
 * @author Pavel Romanov 01.03.2023
 */

public class FrameVkApiUtil {
    private static final String CONTENT_TYPE_PARAM_NAME = "Content-Type";

    /**
     * Метод для выполнения GET запроса и получения результата
     * @param url
     */
    public static Response get(String url) {
        Response response = RestAssured.given().get(url);
        return response;
    }

    /**
     * Метод для выполнения GET запроса с параметром
     * @param url
     * @param param
     */
    public static Response getWithParam(String url, String param, String paramName) {
        Response response = RestAssured.given().param(paramName, param).get(url);
        return response;
    }

    /**
     * Метод для выполнения POST запроса и получения результата
     * @param baseURL
     * @param object
     * @param endPoint
     */
    public static Response post(String baseURL, JSONObject object, String endPoint) {
        RestAssured.baseURI = baseURL;
        Response response = RestAssured.given().header(CONTENT_TYPE_PARAM_NAME, ContentType.JSON)
                .body(object.toJSONString()).post(endPoint);
        return response;
    }

    /**
     * Метод для выполнения POST запроса с передачей файла
     * @param baseURL
     * @param file
     */
    public static Response executePostWithFile(String baseURL, File file) {
        Response response = RestAssured.given().multiPart(file).post(baseURL);
        return response;
    }
}
