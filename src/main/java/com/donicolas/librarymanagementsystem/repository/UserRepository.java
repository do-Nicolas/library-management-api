package com.donicolas.librarymanagementsystem.repository;

import com.donicolas.librarymanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

}
