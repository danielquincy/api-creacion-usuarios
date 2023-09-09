package com.nisum.logic.serviceImpl;

import com.nisum.logic.service.UserApiDetailsSecurityService;
import com.nisum.model.crud.UserCrud;
import com.nisum.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserApiDetailsSecurityServiceImpl  implements UserApiDetailsSecurityService {
    private final UserCrud oUserCrud;

    @Autowired
    public UserApiDetailsSecurityServiceImpl(UserCrud oUserCrud) {
        this.oUserCrud = oUserCrud;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = oUserCrud.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Nombre de Usaurio es Inválido"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .credentialsExpired(false)
                .accountLocked(false)
                .accountExpired(false)
                .disabled(false)
                .build();
    }

    @Override
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        return Optional.empty();
    }


}
