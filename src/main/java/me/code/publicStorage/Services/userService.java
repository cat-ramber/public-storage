package me.code.publicStorage.Services;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.code.publicStorage.Dto.User.ViewAllResponds;
import me.code.publicStorage.Dto.User.CreateUserRequest;
import me.code.publicStorage.Dto.User.CreateUserResponds;
import me.code.publicStorage.Dto.User.LoginResponds;
import me.code.publicStorage.Dto.User.LoginUserRequest;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Exceptions.User.create.CreateUserException;
import me.code.publicStorage.Exceptions.User.create.UserAlreadyExistsException;
import me.code.publicStorage.Exceptions.User.create.UserPasswordException;
import me.code.publicStorage.Exceptions.User.create.UserUsernameException;
import me.code.publicStorage.Exceptions.User.login.LoginException;
import me.code.publicStorage.Models.File;
import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Models.User;
import me.code.publicStorage.Repositories.IFileRepository;
import me.code.publicStorage.Repositories.IFolderRepository;
import me.code.publicStorage.Repositories.IUserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class userService{
    private final IUserRepository IUserRepository;
    private final IFolderRepository folderRepository;
    private final IFileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public CreateUserResponds createUser(CreateUserRequest request)throws CreateUserException {
        if(request.getUsername()==null||request.getUsername().isBlank()){
            throw new UserUsernameException("you need a username");
        }
        if(request.getPassword()==null||request.getPassword().isBlank()){
            throw new UserPasswordException("you need a password");
        }
        if(request.getUsername().trim().length()<3){
            throw new UserUsernameException("your username needs to be at least 3 characters");
        }
        if(request.getPassword().trim().length()<5){
            throw new UserPasswordException("your password needs to be at least 5 characters");
        }
        Optional<User> user = IUserRepository.findByUserName(request.getUsername());
        if(user.isPresent()){
            throw new UserAlreadyExistsException("user already exists");
        }

        String hashedPassword=request.getPassword().trim();
        hashedPassword= passwordEncoder.encode(hashedPassword);
        User createdUser= new User(request.getUsername().trim(),hashedPassword,new Date());
        IUserRepository.save(createdUser);
        log.info("User {} has been created",createdUser.getUserName());
        return new CreateUserResponds(createdUser.getUserName(),createdUser.getCreatedAt());
    }


    public LoginResponds login(LoginUserRequest request) throws LoginException {//create a token from the user login credentials if they exist in the database

        boolean canlogin=true;
        if(request.getUsername()==null||request.getUsername().isBlank()){
            canlogin=false;
        }
        if(request.getPassword()==null||request.getPassword().isBlank()){
            canlogin=false;
        }
        if(!canlogin){
            throw new LoginException("you need a username and password to login");
        }

        String username=request.getUsername().trim();
        Optional<User> optionalUser=IUserRepository.findByUserName(username);

        if (optionalUser.isEmpty()){
            throw new LoginException("check if your username and password are correct");
        }
        User user=optionalUser.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new LoginException("check if your username and password are correct");
        }
       return new LoginResponds("Bearer "+jwtService.createToken(user.getUserId()));
    }

    public User getAuthedUser()throws AuthedUserException{//to get the logged-in user if there is one
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication==null){
            throw new AuthedUserException("couldn't get user from token");
        }
        if(authentication.getPrincipal() instanceof User user){
          Optional<User> optionalUser = IUserRepository.findById(user.getUserId());//apparently needed this since repository is evil
          if(optionalUser.isPresent()){
              return optionalUser.get();
            }
        }

        throw new AuthedUserException("couldn't get user from token");
    }
    @Transactional
    public ViewAllResponds viewAll()throws AuthedUserException {
        User user=getAuthedUser();

        List<Folder> folders= folderRepository.findByUser(user);
        List<String> allFolders=new ArrayList<>();
        for(Folder folder:folders){
            allFolders.add("id: "+folder.getFolderId().toString());
            allFolders.add("name: "+folder.getFolderName());
            allFolders.add("path: "+folder.getPath());
            allFolders.add("");
        }

        List<File> files=fileRepository.findAllByUser(user);
        List<String> allFiles=new ArrayList<>();
        for(File file:files){
            allFiles.add("id: "+file.getFileId());
            allFiles.add("name: "+file.getFileName());
            allFiles.add("path: "+file.getPath());
            allFiles.add("");
        }

        return new ViewAllResponds(allFolders,allFiles,user.getUserName());

    }

}
