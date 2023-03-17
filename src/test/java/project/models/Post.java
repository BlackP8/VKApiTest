package project.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder
public class Post extends Request {
    private String post_id;
    private String message;
    private String attachment;
    private String comment_id;
    private String[] parents_stack;
    @Builder.Default
    private String type = "post";
}
