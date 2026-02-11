package me.code.publicStorage.Controllers;

import lombok.AllArgsConstructor;
import me.code.publicStorage.Dto.ErrorResponds;
import me.code.publicStorage.Dto.File.*;
import me.code.publicStorage.Exceptions.File.delete.DeleteFileException;
import me.code.publicStorage.Exceptions.File.get.GetFileException;
import me.code.publicStorage.Exceptions.File.create.CreateFileException;
import me.code.publicStorage.Exceptions.Folder.create.PathAlreadyExistsException;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Repositories.IFolderRepository;
import me.code.publicStorage.Services.fileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class fileController {
    private final fileService fileService;
    private final IFolderRepository folderRepository;
    @PostMapping("/create")
    public ResponseEntity<?> createFile(@RequestBody CreateFileRequest request){
        try{
            CreateFileResponds responds=fileService.CreateFile(request);
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException | CreateFileException | PathAlreadyExistsException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestBody DeleteFileRequest request){
        try{
            DeleteFileResponds responds= fileService.deleteFile(request);
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException| DeleteFileException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }

    @GetMapping("/getId")
    public ResponseEntity<?> getWithId(@RequestBody GetFileRequest request){
        try{
            GetFileResponds responds=fileService.getFileWithId(request);
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException| GetFileException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
    @GetMapping("get/{path}")
    public ResponseEntity<?> getWithPath(@PathVariable String path){
        try{
            GetFileResponds responds=fileService.getFilePath(path);
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException|GetFileException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
}
