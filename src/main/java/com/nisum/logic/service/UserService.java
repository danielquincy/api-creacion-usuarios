package com.nisum.logic.service;

import com.nisum.model.dto.UserDto;
import com.nisum.model.dto.UserDtoSaved;
import com.nisum.model.entity.User;
import com.nisum.utils.exception.UserApiBussinesException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserDto> getAllUser();

    User getUserById(final UUID prId) throws UserApiBussinesException;

    Optional<User> getUserByEmail(final String email) throws UserApiBussinesException;

    UserDto getUserDtoByEmail(final String email) throws UserApiBussinesException;

    UserDtoSaved saveUser(final UserDto prNewUser) throws UserApiBussinesException;

    void saveLoginUser(final UserDto prUserUpdate) throws UserApiBussinesException;
}
