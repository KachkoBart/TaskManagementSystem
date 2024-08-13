package org.example.taskmanagementsystem.Api.Repository;

import org.example.taskmanagementsystem.Api.Repository.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean deleteByEmail(String email);
}
