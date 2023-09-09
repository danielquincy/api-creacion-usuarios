package com.nisum.model.crud;

import com.nisum.model.entity.Phone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneCrud extends CrudRepository<Phone, Long> {
}
