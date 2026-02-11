package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFileResponds {
    private String username;
    private String name;
    private String lostText;
    public DeleteFileResponds(String userName, String fileName, String lostText) {
        this.username = userName;
        this.name = fileName;
        this.lostText = lostText;
    }


}
