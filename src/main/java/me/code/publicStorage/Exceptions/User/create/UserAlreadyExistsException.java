package me.code.publicStorage.Exceptions.User.create;

public class UserAlreadyExistsException extends CreateUserException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
