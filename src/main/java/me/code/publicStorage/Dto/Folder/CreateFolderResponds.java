package me.code.publicStorage.Dto.Folder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFolderResponds {
    private String username;
    private String id;
    private String name;
    private String folderParentId;

    public CreateFolderResponds(String name,String username,String folderParentId,String id){
        this.username=username;
        this.id=id;
        this.name=name;
        this.folderParentId=folderParentId;
    }
}
