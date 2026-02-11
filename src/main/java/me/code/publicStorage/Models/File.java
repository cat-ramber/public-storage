package me.code.publicStorage.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="files",uniqueConstraints = {
        @UniqueConstraint(columnNames ={"path","userId"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID fileId;

    @Column(nullable = false)
    private String fileName;

    private String path;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folderId")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false)
    private User user;

    public File(User user,String fileName,String text){
        this.fileName=fileName;
        this.text=text;
        this.user=user;
    }


}
