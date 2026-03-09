package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.code.publicStorage.Controllers.userController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class DeleteFileResponds extends RepresentationModel<@NonNull DeleteFileResponds> {
    private String username;
    private String name;
    private String lostText;
    public DeleteFileResponds(String userName, String fileName, String lostText) {
        this.username = userName;
        this.name = fileName;
        this.lostText = lostText;
    }
    public static DeleteFileResponds fromDeleteFile(DeleteFileResponds responds){
        responds.add(linkTo(methodOn(userController.class).viewAllFoldersConnectedToUser()).withRel("view user connected folders/files"));
        return responds;
    }


}
