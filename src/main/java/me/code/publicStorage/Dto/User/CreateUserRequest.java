package me.code.publicStorage.Dto.User;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateUserRequest {
    private String username;
    private String password;
}
