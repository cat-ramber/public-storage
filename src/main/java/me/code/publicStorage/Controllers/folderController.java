package me.code.publicStorage.Controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.code.publicStorage.Dto.ErrorResponds;
import me.code.publicStorage.Dto.Folder.*;
import me.code.publicStorage.Exceptions.Folder.delete.DeleteFolderException;
import me.code.publicStorage.Exceptions.Folder.view.IdViewException;
import me.code.publicStorage.Exceptions.Folder.view.ViewException;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Exceptions.Folder.create.CreateFolderException;
import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Services.folderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/folder")
public class folderController {
private final folderService folderService;
    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody CreateFolderRequest request) {
        try {
            CreateFolderResponds responds = folderService.createFolder(request);
            Folder createdFolder=folderService.getFolder(responds.getId());
            return ResponseEntity.created(URI.create("/folder")).body(CreateFolderResponds.fromCreateFolder(createdFolder));
        } catch (IdViewException | CreateFolderException | AuthedUserException e) {//separating them so that you see what can go wrong easily
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable String id) {
        DeleteFolderRequest request =new DeleteFolderRequest();
        request.setFolderId(id);
        try {
            Folder folder= folderService.getFolder(request.getFolderId());
            folderService.deleteFolder(request);
            return ResponseEntity.created(URI.create("/user")).body(DeleteFolderResponds.fromDeleteFolder(folder));
        } catch (IdViewException | AuthedUserException | DeleteFolderException e) {
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @GetMapping("/viewId/{id}")
    public ResponseEntity<?> viewFolderId(@PathVariable String id){
        ViewFolderRequest request= new ViewFolderRequest();
        request.setFolderId(id);

        try{
            Folder folder= folderService.getFolder(request.getFolderId());
           ViewFolderResponds responds= folderService.idViewFolderContents(request);
           return ResponseEntity.created(URI.create("/user")).body(ViewFolderResponds.fromViewFolder(folder));
        }catch (AuthedUserException|ViewException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @GetMapping("/view/{path}")
    public ResponseEntity<?> viewFolder(@PathVariable String path){
        try{
            ViewFolderResponds responds=folderService.pathViewFolderContents(path);
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException|ViewException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
}
