package me.code.publicStorage.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.code.publicStorage.Dto.Folder.*;
import me.code.publicStorage.Exceptions.Folder.create.PathAlreadyExistsException;
import me.code.publicStorage.Exceptions.Folder.delete.DeleteFolderException;
import me.code.publicStorage.Exceptions.Folder.delete.DeleteIdException;
import me.code.publicStorage.Exceptions.Folder.delete.InvalidFolderException;
import me.code.publicStorage.Exceptions.Folder.view.IdViewException;
import me.code.publicStorage.Exceptions.Folder.view.PathViewException;
import me.code.publicStorage.Exceptions.Folder.view.ViewException;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Exceptions.Folder.create.CreateFolderException;
import me.code.publicStorage.Exceptions.Folder.create.FolderNameException;
import me.code.publicStorage.Exceptions.Folder.create.FolderParentException;
import me.code.publicStorage.Models.File;
import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Models.User;
import me.code.publicStorage.Repositories.IFileRepository;
import me.code.publicStorage.Repositories.IFolderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class folderService {
    private final userService userService;
    private final IFolderRepository folderRepository;
    private final IFileRepository fileRepository;

    @Transactional
    public CreateFolderResponds createFolder(CreateFolderRequest request)throws CreateFolderException,AuthedUserException {
        User user = userService.getAuthedUser();

        request.setName(request.getName().replace(':',' ').trim());
        if (request.getName() == null || request.getName().isBlank()) {
            throw new FolderNameException("the folder needs a name");
        }

        Folder createdFolder = new Folder(user, request.getName());

        UUID parentId = null;
        try {
            if(request.getFolderParentId()!=null&&!request.getFolderParentId().isBlank()){
                parentId = UUID.fromString(request.getFolderParentId());
            }
        }catch (IllegalArgumentException ignored){
            throw new FolderParentException("invalid id in folder parent");
        }

        if (parentId != null) {
          Optional<Folder> optionalFolder = folderRepository.findByFolderIdAndUser(parentId,user);

            if (optionalFolder.isPresent()) {

                Folder parentfolder = optionalFolder.get();

                if (user.equals(parentfolder.getUser())) {
                    createdFolder.setFolderParent(parentfolder);
                    createdFolder.setPath(parentfolder.getPath()+":"+createdFolder.getFolderName());
                    if(folderRepository.findByPathAndUser(createdFolder.getPath(),user).isPresent()){
                        throw new PathAlreadyExistsException("the folder: "+createdFolder.getFolderName()+" couldn't be created since path already exists: "+createdFolder.getPath());
                    }
                    folderRepository.save(createdFolder);
                    log.info("Folder: {} has been created by {}", createdFolder.getFolderName(), user.getUserName());
                    return new CreateFolderResponds(
                            createdFolder.getFolderName(),
                            createdFolder.getUser().getUserName(),
                            createdFolder.getFolderParent().getFolderId().toString(),createdFolder.getFolderId().toString());
                }
            }
            throw new FolderParentException("either you're logged into the wrong account or you entered the wrong id for the parent folder");
        }
        createdFolder.setPath(createdFolder.getFolderName());
        if(folderRepository.findByPathAndUser(createdFolder.getPath(),user).isPresent()){
            throw new PathAlreadyExistsException("the folder: "+createdFolder.getFolderName()+" couldn't be created since path already exists: "+createdFolder.getPath());
        }
        folderRepository.save(createdFolder);
        log.info("Folder: {} has been created by {}", createdFolder.getFolderName(), user.getUserName());
        return new CreateFolderResponds(createdFolder.getFolderName(), user.getUserName(), user.getUserId().toString(),createdFolder.getFolderId().toString());//if no parent was given connect with user
    }



    public DeleteFolderResponds deleteFolder(DeleteFolderRequest request)throws DeleteFolderException,AuthedUserException {
        User user =userService.getAuthedUser();

        if(request.getFolderId()==null||request.getFolderId().isBlank()){
            throw new DeleteIdException("your id can not be empty");
        }
        UUID deleteId;

        try{
          deleteId = UUID.fromString(request.getFolderId());
        }catch (IllegalArgumentException ignored){
            throw new DeleteIdException(request.getFolderId()+" couldn't be made into an id");
        }

        Optional<Folder> optionalFolder = folderRepository.findByFolderIdAndUser(deleteId,user);
        Folder folder=null;

        if(optionalFolder.isPresent()){
            folder=optionalFolder.get();
        }


        if(folder!=null){
            if (folder.getUser().equals(user)) {

                List<String> foldersChildren= new ArrayList<>();

                if(folder.getFolderChildren()!=null){
                    for(Folder folderChild:folder.getFolderChildren()){
                        foldersChildren.add("Folder: "+folderChild.getFolderName());
                    }
                }

                if(folder.getFileChildren()!=null){
                    for(File fileChild:folder.getFileChildren()){
                        foldersChildren.add("File: "+fileChild.getFileName());
                    }
                }

                DeleteFolderResponds responds= new DeleteFolderResponds(
                        user.getUserName(),
                        folder.getFolderName(),foldersChildren
                );
                folderRepository.delete(folder);
                log.info("Folder: {} from {} has been deleted", folder.getFolderName(), user.getUserName());
                return responds;
            }

        }
        throw new InvalidFolderException("either you're logged into the wrong account or the folder doesn't exist");
    }



    public ViewFolderResponds pathViewFolderContents(String path)throws ViewException,AuthedUserException {

        User user=userService.getAuthedUser();

        if(path.isBlank()){
            throw new PathViewException("you need a valid path like folder:folder");
        }
       Optional<Folder> optionalFolder= folderRepository.findByPathAndUser(path,user);
        if(optionalFolder.isEmpty()){
            throw new PathViewException("your path is invalid: "+path);
        }
        Folder folder=optionalFolder.get();
        List<String> respondslist=new ArrayList<>();
        for(Folder childFolder:folder.getFolderChildren()){
            respondslist.add("Folder: "+childFolder.getFolderName());
        }
        for(File fileChild:folder.getFileChildren()){
            respondslist.add("File: "+fileChild.getFileName());
        }
        return new ViewFolderResponds(
                respondslist,
                folder.getFolderName(),
                user.getUserName(),
                folder.getFolderId()
                );
    }



    public ViewFolderResponds idViewFolderContents(ViewFolderRequest request)throws ViewException,AuthedUserException{
        User user=userService.getAuthedUser();

       String folderId= request.getFolderId();

        if (folderId==null||folderId.isBlank()) {
            throw  new IdViewException("your id may not be blank");
        }
        try{
            UUID id=UUID.fromString(folderId);
            Optional<Folder> optionalFolder=folderRepository.findByFolderIdAndUser(id,user);
            if(optionalFolder.isEmpty()){
                throw new IdViewException("no folder with the id:"+request.getFolderId()+" was found");
            }

                Folder folder =optionalFolder.get();
                List<String> childlist=new ArrayList<>();

                for(Folder childFolder:folder.getFolderChildren()){
                    childlist.add("Folder: "+childFolder.getFolderName());
                }
                for(File childFile:folder.getFileChildren()){
                    childlist.add("File: "+childFile.getFileName());
                }

                return new ViewFolderResponds(
                        childlist,
                        folder.getFolderName(),
                        user.getUserName(),
                        folder.getFolderId());
        }catch (IllegalArgumentException ignored){
            throw new IdViewException("couldn't parse "+folderId+" into uuid");
        }
    }

}
