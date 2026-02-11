package me.code.publicStorage.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.code.publicStorage.Dto.File.*;
import me.code.publicStorage.Exceptions.File.delete.FileNotFoundException;
import me.code.publicStorage.Exceptions.File.delete.IdDeleteFileException;
import me.code.publicStorage.Exceptions.File.get.GetFileException;
import me.code.publicStorage.Exceptions.File.get.IdGetException;
import me.code.publicStorage.Exceptions.File.get.NotFoundGetException;
import me.code.publicStorage.Exceptions.File.create.*;
import me.code.publicStorage.Exceptions.File.get.PathGetException;
import me.code.publicStorage.Exceptions.Folder.create.PathAlreadyExistsException;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Models.File;
import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Models.User;
import me.code.publicStorage.Repositories.IFileRepository;
import me.code.publicStorage.Repositories.IFolderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Service
@Slf4j
public class fileService {
    private final userService userService;
    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;

    @Transactional
    public CreateFileResponds CreateFile( CreateFileRequest request) throws AuthedUserException, CreateFileException, PathAlreadyExistsException {
        User user=userService.getAuthedUser();

        request.setName(request.getName().replace(':',' '));
        if(request.getName()==null||request.getName().isBlank()){
            throw new FileNameException("your file needs a name");
        }

        if(request.getText()==null||request.getText().isBlank()){
            throw new FileTextException("you need some text to store in the file");
        }

        String parentId=request.getFileParentId();
        if(parentId==null||parentId.isBlank()){
            Optional<File> optionalFile=fileRepository.findByPathAndUser(request.getName(),user);
            if(optionalFile.isPresent()){
                throw new FilePathAlreadyException("you already have a file with the path: "+request.getName());
            }

            File file=new File(user,request.getName(),request.getText());
            file.setPath(request.getName());
            fileRepository.save(file);
            log.info("the user: {} created the file: {}",user.getUserName(),file.getFileId());

            log.info(file.getFileId().toString());
            return new CreateFileResponds(user.getUserName(),file.getFileName(),user.getUserId(),file.getText(),file.getFileId());

        }

        UUID idFromParent;
        try{
            idFromParent=UUID.fromString(request.getFileParentId());
        }catch (IllegalArgumentException ignored){
            throw new ParentIdException("couldn't turn: "+request.getFileParentId()+" into a uuid");
        }

        Optional<Folder> fileParent=folderRepository.findByFolderIdAndUser(idFromParent,user);
        if(fileParent.isEmpty()){
            throw new ParentIdException("id: "+request.getFileParentId()+" for parent wasn't found");
        }

        Folder folder=fileParent.get();
        Optional<File> optionalFile=fileRepository.findByPathAndUser(folder.getPath()+":"+request.getName(),user);
        if(optionalFile.isPresent()){
            throw new PathAlreadyExistsException("you already have a file with the path: "+optionalFile.get().getPath());
        }

        File file=new File(user,request.getName(),request.getText());
        file.setFolder(folder);
        file.setPath(folder.getPath()+":"+request.getName());
        fileRepository.save(file);
        return new CreateFileResponds(
                user.getUserName(),
                file.getFileName(),
                file.getFolder().getFolderId(),
                request.getText(),file.getFileId());
    }


    public DeleteFileResponds deleteFile(DeleteFileRequest request)throws AuthedUserException{
        User user=userService.getAuthedUser();

        if(request.getFileId()==null||request.getFileId().isBlank()){
            throw new IdDeleteFileException("you need to enter a id");
        }
        UUID id;
        try{
            id=UUID.fromString(request.getFileId());
        }catch (IllegalArgumentException ignored){
            throw new IdDeleteFileException("couldn't make "+request.getFileId()+" into a valid id");
        }
        Optional<File> optionalFile=fileRepository.findByFileIdAndUser(id,user);
        if(optionalFile.isEmpty()){
            throw new FileNotFoundException("no file with the id: "+id+" exists");
        }
        File file=optionalFile.get();
         DeleteFileResponds responds = new DeleteFileResponds(user.getUserName(), file.getFileName(),file.getText());
         fileRepository.delete(file);
         log.info("the user: {} deleted the file: {}",user.getUserName(),responds.getName());
         return responds;
    }


    public GetFileResponds getFileWithId( GetFileRequest request)throws AuthedUserException, GetFileException {
        User user=userService.getAuthedUser();

        String fileId=request.getId();
        if(fileId==null||fileId.isBlank()){
            throw new IdGetException("your id may not be blank");
        }

        UUID id;
        try{
            id=UUID.fromString(request.getId());
        }catch (IllegalArgumentException ignored){
            throw new IdGetException("couldn't make: "+request.getId()+" into an id");
        }

        Optional<File> optionalFile=fileRepository.findByFileIdAndUser(id,user);
        if(optionalFile.isEmpty()){
            throw new NotFoundGetException("there is no file with id: "+id);
        }
            File file= optionalFile.get();
        return new GetFileResponds(file.getFileId(),user.getUserName(),file.getFileName(),file.getText());
    }
    public GetFileResponds getFilePath(String path)throws AuthedUserException,GetFileException{
        User user=userService.getAuthedUser();

        if(path==null||path.isBlank()){
            throw new PathGetException("you need a path like folder:folder:folder:file");
        }
        Optional<File> optionalFile=fileRepository.findByPathAndUser(path,user);
        if (optionalFile.isEmpty()) {
            throw new PathGetException("your path is invalid: "+path);
        }
        File file=optionalFile.get();
        return new GetFileResponds(file.getFileId(),user.getUserName(),file.getFileName(),file.getText());
    }
}
