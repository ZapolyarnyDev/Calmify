package io.github.zapolyarnydev.userservice.exception;

public class UserNotFoundByHandleException extends RuntimeException {
  public UserNotFoundByHandleException(String handle) {
    super(String.format("Couldn't find user with the handle \"%s\"", handle));
  }
}
