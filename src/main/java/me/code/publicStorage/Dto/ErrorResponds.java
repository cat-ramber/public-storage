package me.code.publicStorage.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponds {
   private String Error;

    public ErrorResponds(String Error){
        this.Error=Error;
    }
}
