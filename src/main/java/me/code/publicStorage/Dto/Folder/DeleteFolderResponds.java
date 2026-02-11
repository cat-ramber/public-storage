package me.code.publicStorage.Dto.Folder;

import lombok.Getter;
import lombok.Setter;
import me.code.publicStorage.PublicStorageApplication;
import org.hibernate.dialect.function.ListaggStringAggEmulation;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Getter
@Setter
public class DeleteFolderResponds {
    private String userName;
    private String folderName;
    private List<String> foldersChildren;

    public DeleteFolderResponds(String userName, String folderName, List<String> foldersChildren){
        this.userName=userName;
        this.folderName=folderName;
        this.foldersChildren=foldersChildren;
    }
}
