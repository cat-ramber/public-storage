package me.code.publicStorage.Dto.Folder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.code.publicStorage.Controllers.folderController;
import me.code.publicStorage.Models.Folder;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class CreateFolderResponds extends RepresentationModel<@NonNull CreateFolderResponds> {
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
    public static CreateFolderResponds fromCreateFolder(Folder folder){
        String parent;
        if(folder.getFolderParent()==null||folder.getFolderParent().getFolderId()==null){
            parent="";
        }else{
            parent=folder.getFolderParent().getFolderId().toString();
        }
        CreateFolderResponds responds=new CreateFolderResponds(
                folder.getFolderName(),
                folder.getUser().getUserName(),
                String.valueOf(parent),
                String.valueOf(folder.getFolderId()));

        responds.add(linkTo(methodOn(folderController.class).viewFolderId(folder.getFolderId().toString())).withRel("getFolder"));

        //for some reason it treats it like a get
        responds.add(linkTo(methodOn(folderController.class).deleteFolder(folder.getFolderId().toString())).withRel("deleteFolder").withType("DELETE"));

        return responds;
    }
}
