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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class DeleteFolderResponds extends RepresentationModel<@NonNull DeleteFolderResponds> {
    private String userName;
    private String folderName;
    private List<String> foldersChildren;

    public DeleteFolderResponds(String userName, String folderName, List<String> foldersChildren){
        this.userName=userName;
        this.folderName=folderName;
        this.foldersChildren=foldersChildren;
    }
    public static DeleteFolderResponds fromDeleteFolder(Folder folder){
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

        DeleteFolderResponds responds=new DeleteFolderResponds(
                folder.getUser().getUserName(),
                folder.getFolderName(),
                children
        );
        responds.add(linkTo(methodOn(userController.class).viewAllFoldersConnectedToUser()).withRel("view user connected folders/files"));
        return responds;
    }
}
