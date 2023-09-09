package com.nisum.model.crud;

import com.nisum.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCrud extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);

   @Query("select u from User u left join fetch u.phones")
    List<User> findAllUsers();
}
