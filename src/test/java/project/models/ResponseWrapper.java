package project.models;

import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Pavel Romanov 10.03.2023
 */

@Getter
@Jacksonized
public class ResponseWrapper {
    private Response response;
}
