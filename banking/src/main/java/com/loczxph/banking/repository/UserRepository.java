package com.loczxph.banking.repository;

import com.loczxph.banking.model.Role;
import com.loczxph.banking.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    User findByAccount_Id(String accountNumber);

    boolean existsByEmail(String email);
}
