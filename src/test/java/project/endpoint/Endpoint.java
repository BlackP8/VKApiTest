package project.endpoint;

public enum Endpoint {
    WALL_POST("wall.post"),
    WALL_UPLOAD_SERVER("photos.getWallUploadServer"),
    CREATE_COMMENT("wall.createComment"),
    SAVE_WALL_PHOTO("photos.saveWallPhoto"),
    WALL_EDIT("wall.edit"),
    LIKED("likes.isLiked"),
    WALL_DELETE("wall.delete"),
    DELETE_PHOTO("photos.delete");

    private final String value;

    Endpoint(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return value;
    }
}
