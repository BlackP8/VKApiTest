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
public class Photo extends Request {
    private String album_id;
    private String upload_url;
    private String photo;
    private String server;
    private String hash;
    private String photo_id;
    private String user_id;
    @Builder.Default
    private String type = "photo";
}
