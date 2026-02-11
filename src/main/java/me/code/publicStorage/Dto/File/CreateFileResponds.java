package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateFileResponds {
    private UUID fileId;
    private String userName;
    private String fileName;
    private UUID fileParentId;
    private String text;
    public CreateFileResponds(String userName, String fileName,UUID fileParentId,String text,UUID fileId) {
        this.userName = userName;
        this.fileName = fileName;
        this.fileParentId = fileParentId;
        this.fileId=fileId;
        this.text=text;
    }
}
