package project.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder
public class Like extends Request {
    private String user_id;
    private String type;
    private String item_id;
    private String liked;
    private String copied;
}
