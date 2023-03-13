package universaltools;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Pavel Romanov 27.02.2023
 */

@Slf4j
public class ConfigUtil {
    private static final String PATH_TO_CONFIG_FILE = "src/test/resources/config.json";
    private static final String PATH_TO_CRED_FILE = "src/test/resources/credentials.json";
    private static JSONParser parser = new JSONParser();
    private static JSONObject jsonConfObject;
    private static JSONObject jsonTestObject;
    private static JSONObject jsonCredObject;

    public static void setConfig() {
        jsonConfObject = setJSONObject(jsonConfObject, PATH_TO_CONFIG_FILE);
    }

    public static void setCredentials() {
        jsonCredObject = setJSONObject(jsonCredObject, PATH_TO_CRED_FILE);
    }

    private static JSONObject setJSONObject(JSONObject object, String pathToFile) {
        try(BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            Object obj = parser.parse(reader);
            object = (JSONObject) obj;
        }
        catch (IOException | ParseException e) {
            log.error(e.getMessage());
        }
        return object;
    }

    public static JSONObject setTestData(String filePath) {
        return setJSONObject(jsonTestObject, filePath);
    }

    public static String getConfProperty(String key) {
        return (String) jsonConfObject.get(key);
    }

    public static String getCredentials(String key) {
        return (String) jsonCredObject.get(key);
    }
}
