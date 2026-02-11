package me.code.publicStorage.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Entity()
@Table(name = "folders",uniqueConstraints = {
        @UniqueConstraint(columnNames ={"path","userId"})//if one of these is unique it can save but not if both are the same
})
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Folder {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID folderId;

        private String folderName;

        private String path;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "folderParentId")//used if this folder is in a different folder or if it connects straight to the user
        private Folder folderParent;

        @OneToMany(mappedBy ="folder",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<File> fileChildren;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userId",nullable = false)
        private User user;

        @OneToMany(mappedBy = "folderParent",cascade = CascadeType.ALL, fetch = FetchType.LAZY)//used so that this folder can store other folders
        private List<Folder> folderChildren;

        public Folder(User user,String folderName){
            this.folderName=folderName;
            this.user=user;
        }
    }
