package restframework;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.io.File;

/**
 * Универсальный класс для выполнения GET и POST запросов и получения их результата
 * @author Pavel Romanov 01.03.2023
 */

public class RequestManager {
    private static final String CONTENT_TYPE_PARAM_NAME = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * Метод для выполнения GET запроса и получения результата
     * @param url
     */
    public static Response executeGet(String url) {
        Response response = RestAssured.given().get(url);
        return response;
    }

    public static Response executeasd(String url, String photo) {
        Response response = RestAssured.given().param("photo", photo).get(url);
        return response;
    }

    /**
     * Метод для выполнения POST запроса и получения результата
     * @param baseURL
     * @param object
     * @param endPoint
     */
    public static Response executePost(String baseURL, JSONObject object, String endPoint) {
        RestAssured.baseURI = baseURL;
        Response response = RestAssured.given().header(CONTENT_TYPE_PARAM_NAME, CONTENT_TYPE_JSON)
                .body(object.toJSONString()).post(endPoint);
        return response;
    }

    public static Response executePostWithFile(String baseURL, File file) {
        Response response = RestAssured.given().multiPart(file).post(baseURL);
        return response;
    }
}
