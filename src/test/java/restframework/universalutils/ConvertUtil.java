package restframework.universalutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
public class ConvertUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public  static <T> Map<String, String> convertModelToMap(T model) {
        Map<String, String> map = mapper.convertValue(model, new TypeReference<Map<String, String>>() {});
        return map;
    }

    public static <T> T readValueJsonString(String jsonString, Class<T> valueType) {
        try {
             return mapper.readValue(jsonString, valueType);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> T convertMapToModel(Map<String, String> map, Class<T> valueType) {
        return mapper.convertValue(map, valueType);
    }
}
