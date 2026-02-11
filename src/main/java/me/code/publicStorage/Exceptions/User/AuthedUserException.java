package me.code.publicStorage.Exceptions.User;

public class AuthedUserException extends RuntimeException {
    public AuthedUserException(String message) {
        super(message);
    }
}
