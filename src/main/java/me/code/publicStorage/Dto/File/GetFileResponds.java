package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetFileResponds {
    private UUID fileId;
    private String userName;
    private String fileName;
    private String text;
    public GetFileResponds(UUID fileId, String userName, String fileName, String text) {
        this.fileId = fileId;
        this.userName = userName;
        this.fileName = fileName;
        this.text = text;
    }
}
