package me.code.publicStorage.Dto.Folder;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ViewFolderResponds {
    private UUID folderId;
    private String userName;
    private String folderName;
    private List<String> contains;

    public ViewFolderResponds(List<String> contains, String folderName, String userName, UUID folderId) {
        this.contains = contains;
        this.folderName = folderName;
        this.userName = userName;
        this.folderId = folderId;
    }
}
