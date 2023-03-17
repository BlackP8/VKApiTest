package restframework;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import java.io.File;
import java.util.Map;

/**
 * Универсальный класс для выполнения GET и POST запросов и получения их результата
 * @author Pavel Romanov 01.03.2023
 */

public class FrameApiUtil {
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
     * @param baseUrl
     * @param params
     * @param endPoint
     */
    public static Response getWithParam(String baseUrl, Map<String, String> params, String endPoint) {
        RestAssured.baseURI = baseUrl;
        Response response = RestAssured.given().queryParams(params).get(endPoint);
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
    public static Response postWithFile(String baseURL, File file) {
        Response response = RestAssured.given().multiPart(file).post(baseURL);
        return response;
    }
}
