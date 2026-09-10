package com.techup.spring_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techup.spring_demo.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
