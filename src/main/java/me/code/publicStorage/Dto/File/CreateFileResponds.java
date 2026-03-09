package me.code.publicStorage.Dto.File;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.code.publicStorage.Controllers.fileController;
import me.code.publicStorage.Models.File;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class CreateFileResponds extends RepresentationModel<@NonNull CreateFileResponds> {
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
    public static CreateFileResponds fromCreateFile(CreateFileResponds responds){
        responds.add(linkTo(methodOn(fileController.class).getWithId(responds.fileId.toString())).withRel("get file with id"));
        return responds;
    }
}
