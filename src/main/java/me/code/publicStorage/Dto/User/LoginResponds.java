package me.code.publicStorage.Dto.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponds {
    private String Token;

    public LoginResponds(String Token){
        this.Token=Token;
    }
}
