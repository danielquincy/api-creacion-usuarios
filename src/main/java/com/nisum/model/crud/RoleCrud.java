package com.nisum.model.crud;

import com.nisum.model.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleCrud extends CrudRepository<Role, Long> {
}
