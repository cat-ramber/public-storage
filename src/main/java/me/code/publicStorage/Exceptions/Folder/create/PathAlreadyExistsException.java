package me.code.publicStorage.Exceptions.Folder.create;

public class PathAlreadyExistsException extends CreateFolderException{
    public PathAlreadyExistsException(String message) {
        super(message);
    }
}
