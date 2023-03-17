package project.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class Request {
    private String access_token;
    private String v;
    private String owner_id;
}
