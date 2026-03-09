package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.code.publicStorage.Controllers.userController;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class GetFileResponds extends RepresentationModel<@NonNull GetFileResponds> {
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
    public static GetFileResponds fromGetFile(GetFileResponds responds){
        responds.add(linkTo(methodOn(userController.class).viewAllFoldersConnectedToUser()).withRel("view user connected folders/files"));
        return responds;
    }
}
