package me.code.publicStorage.Dto.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ViewAllResponds {
    private String userName;
    private List<String> folders;
    private List<String> files;
    public ViewAllResponds(List<String> folders, List<String> files,String userName) {
        this.userName=userName;
        this.folders = folders;
        this.files = files;
    }

}
