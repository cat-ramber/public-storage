package me.code.publicStorage.Repositories;

import me.code.publicStorage.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User,UUID> {

    Optional<User> findByUserName(String userName);
}
