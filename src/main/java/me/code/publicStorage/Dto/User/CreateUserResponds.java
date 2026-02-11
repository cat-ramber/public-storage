package me.code.publicStorage.Dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateUserResponds {
    private String username;
    private Date createdAt;

    public CreateUserResponds(String username,Date createdAt){
        this.username=username;
        this.createdAt=createdAt;
    }
}
