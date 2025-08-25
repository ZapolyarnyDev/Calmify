package io.github.zapolyarnydev.videoservice.exception;

import java.util.UUID;

public class VideoMetadataNotFoundException extends RuntimeException {
    public VideoMetadataNotFoundException(UUID id) {
        super(String.format("Couldn't find video with the id \"%s\"", id));
    }
}
