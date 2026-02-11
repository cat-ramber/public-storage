package me.code.publicStorage.Repositories;

import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IFolderRepository extends JpaRepository<Folder,UUID> {

    Optional<Folder> findByPathAndUser(String folderName, User user);
    Optional<Folder> findByFolderIdAndUser(UUID id,User user);

    List<Folder> findByUser(User user);
}
