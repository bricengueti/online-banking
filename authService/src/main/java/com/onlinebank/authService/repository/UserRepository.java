package com.onlinebank.authService.repository;

import java.util.Optional;

import com.onlinebank.authService.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for User entity.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {


    Optional<User> findByEmail(String email);  // Add this if needed


    boolean existsByEmail(String email);
}