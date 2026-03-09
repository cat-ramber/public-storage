package me.code.publicStorage.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false,unique = true)
    private String userName;

    private String password;//changed nullable to true since oAuth2 can also be used to login

    private Date createdAt;

    private String oidcId;

    private String oidcProvider;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Folder> folders;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> files;

    public User(String userName,String password,Date createdAt){
        this.userName=userName;
        this.password=password;
        this.createdAt=createdAt;
    }
    public User(String userName,String oidcId,String oidcProvider,Date createdAt){
        this.userName=userName;
        this.oidcId=oidcId;
        this.oidcProvider=oidcProvider;
        this.createdAt=createdAt;
    }


}
