package io.github.zapolyarnydev.videoservice.exception;

public class VideoMetadataNotFoundByShortIdException extends RuntimeException {
  public VideoMetadataNotFoundByShortIdException(String shortId) {
    super(String.format("Couldn't find video with the short id \"%s\"", shortId));
  }
}
