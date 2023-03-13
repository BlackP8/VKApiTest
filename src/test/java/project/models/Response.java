package project.models;

import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Pavel Romanov 10.03.2023
 */

@Getter
@Jacksonized
public class Response {
    private int post_id;
    private int album_id;
    private int user_id;
    private int comment_id;
    private int server;
    private int liked;
    private int copied;
    private String id;
    private String photo;
    private String hash;
    private String upload_url;
    private String[] parents_stack;
}
