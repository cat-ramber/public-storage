package me.code.publicStorage.Controllers;

import lombok.AllArgsConstructor;
import me.code.publicStorage.Dto.ErrorResponds;
import me.code.publicStorage.Dto.Folder.*;
import me.code.publicStorage.Exceptions.Folder.delete.DeleteFolderException;
import me.code.publicStorage.Exceptions.Folder.view.ViewException;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Exceptions.Folder.create.CreateFolderException;
import me.code.publicStorage.Services.folderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/folder")
public class folderController {
private final folderService folderService;
    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody CreateFolderRequest request) {
        try {
            CreateFolderResponds responds = folderService.createFolder(request);
            return ResponseEntity.ok().body(responds);
        } catch (CreateFolderException | AuthedUserException e) {//separating them so that you see what can go wrong easily
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFolder(@RequestBody DeleteFolderRequest request) {
        try {
            DeleteFolderResponds responds = folderService.deleteFolder(request);
            return ResponseEntity.ok(responds);
        } catch (AuthedUserException | DeleteFolderException e) {
            System.out.println("sent");
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @GetMapping("/viewId")
    public ResponseEntity<?> viewFolderId(@RequestBody ViewFolderRequest request){
        try{
           ViewFolderResponds responds= folderService.idViewFolderContents(request);
           return ResponseEntity.ok(responds);
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
