package me.code.publicStorage.Controllers;

import lombok.AllArgsConstructor;
import me.code.publicStorage.Dto.*;
import me.code.publicStorage.Dto.User.ViewAllResponds;
import me.code.publicStorage.Dto.User.CreateUserRequest;
import me.code.publicStorage.Dto.User.CreateUserResponds;
import me.code.publicStorage.Dto.User.LoginResponds;
import me.code.publicStorage.Dto.User.LoginUserRequest;
import me.code.publicStorage.Exceptions.User.AuthedUserException;
import me.code.publicStorage.Exceptions.User.create.CreateUserException;
import me.code.publicStorage.Exceptions.User.login.LoginException;
import me.code.publicStorage.Repositories.IUserRepository;
import me.code.publicStorage.Services.userService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class userController {
    IUserRepository IUserRepository;
    userService userService;
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request){
        try{
          CreateUserResponds responds = userService.createUser(request);
           return ResponseEntity.ok().body(responds);
        }catch (CreateUserException e){
           return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest request){
        try{
            LoginResponds responds=userService.login(request);
            return ResponseEntity.ok(responds);
        }catch (LoginException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
    @GetMapping("/view")
    public ResponseEntity<?> viewAllFoldersConnectedToUser(){
        try{
            ViewAllResponds responds=userService.viewAll();
            return ResponseEntity.ok(responds);
        }catch (AuthedUserException e){
            return ResponseEntity.badRequest().body(new ErrorResponds(e.getMessage()));
        }
    }
}
