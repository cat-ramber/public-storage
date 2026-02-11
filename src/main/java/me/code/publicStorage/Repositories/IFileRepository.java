package me.code.publicStorage.Repositories;

import me.code.publicStorage.Models.File;
import me.code.publicStorage.Models.Folder;
import me.code.publicStorage.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFileRepository extends JpaRepository<File,UUID> {

    List<File> findAllByUser(User user);
    Optional<File> findByPathAndUser(String folderName, User user);
    Optional<File> findByFileIdAndUser(UUID id,User user);
}
