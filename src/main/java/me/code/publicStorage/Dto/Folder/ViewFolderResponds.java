package me.code.publicStorage.Dto.Folder;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.code.publicStorage.Controllers.userController;
import me.code.publicStorage.Models.File;
import me.code.publicStorage.Models.Folder;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class ViewFolderResponds extends RepresentationModel<@NonNull ViewFolderResponds> {
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

    public static ViewFolderResponds fromViewFolder(Folder folder){
        List<String> children=new ArrayList<>();

        if(folder.getFolderChildren()!=null){
            for(Folder childfolder:folder.getFolderChildren()){
                children.add(childfolder.getFolderName());
            }
        }
        if(folder.getFileChildren()!=null){
            for(File childfile: folder.getFileChildren()){
                children.add(childfile.getFileName());
            }
        }
        ViewFolderResponds responds= new ViewFolderResponds(
                children,
                folder.getFolderName(),
                folder.getUser().getUserName(),
                folder.getFolderId());
        responds.add(linkTo(methodOn(userController.class).viewAllFoldersConnectedToUser()).withRel("view user connected folders/files"));

        return responds;
    }

}
