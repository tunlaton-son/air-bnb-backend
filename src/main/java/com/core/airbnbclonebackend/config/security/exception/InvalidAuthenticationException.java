package com.core.airbnbclonebackend.config.security.exception;

public class InvalidAuthenticationException extends RuntimeException {

  public InvalidAuthenticationException() {
    super("Invalid Email or Password");
  }
}
