package com.nisum;


import com.nisum.logic.serviceImpl.UserServiceImpl;
import com.nisum.model.crud.UserCrud;
import com.nisum.model.dto.UserDto;
import com.nisum.model.dto.UserDtoSaved;
import com.nisum.model.entity.User;
import com.nisum.utils.AlterObjects;
import com.nisum.utils.exception.UserApiBussinesException;
import com.nisum.utils.security.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserApiServiceImplTest {
    @Mock
    private UserCrud oUserCrud;
    @Mock
    private JwtUtils ojwtUtils;
    @InjectMocks
    UserServiceImpl oUserService;


    private User userAdmin;
    private UserDto userDtoSave;
    private UserDtoSaved userDtoSaved;

    private String email = "admin@admin.com";
    private String password = "$2a$10$6qbSdgBHLS92zJrhd1vvneLS6HfU0Gi/mRZDXbfAgQSium33gBIHS";
    private String token = "9b4edee1cb0041bd85c8a93958d34ada9e55d566be234dda8d4fe8645244636b";

    @BeforeEach
    void setUp() throws Exception {
        init();
    }

    private void init() throws Exception {
        userAdmin = new User(UUID.fromString("1122d098-4bf4-47fd-89b5-74bfcc163c0e"),
                "Administrador",
                email,
                password,
                AlterObjects.getTimeNow(),
                AlterObjects.getTimeNow(),
                AlterObjects.getTimeNow(),
                token,
                true, null);

        userDtoSave = new UserDto("Usuario Test", "usertest@gmail.com", "Prueba123", new ArrayList<>(), "");
        userDtoSaved = new UserDtoSaved(UUID.fromString("1122d098-4bf4-47fd-89b5-74bfcc163c0d"),
                AlterObjects.getTimeNow(),
                AlterObjects.getTimeNow(),
                AlterObjects.getTimeNow(),
                "",
                true);
    }

    @Test
    void getUserById() {
        Mockito.when(oUserCrud.findById(UUID.fromString("1122d098-4bf4-47fd-89b5-74bfcc163c0e")))
                .thenReturn(java.util.Optional.ofNullable(userAdmin));
        User usuarioEncontrado = oUserService.getUserById(userAdmin.getId());
        User usuarioEsperado = userAdmin;
        Assertions.assertEquals(usuarioEsperado, usuarioEncontrado);
    }

    @Test
    void getUserByEmail() {
        Mockito.when(oUserCrud.findByEmail("admin@admin.com"))
                .thenReturn(java.util.Optional.ofNullable(userAdmin));
        User usuarioEncontrado = oUserService.getUserByEmail(userAdmin.getEmail()).orElse(null);
        User usuarioEsperado = userAdmin;
        Assertions.assertEquals(usuarioEsperado, usuarioEncontrado);
    }

    @Test
    void getAllUser() {
        Mockito.when(oUserCrud.findAll()).thenReturn(Collections.singletonList(userAdmin));
        List<UserDto> lstEncontrado = oUserService.getAllUser();
        List<UserDto> lstEsperado = Collections.singletonList(new UserDto(userAdmin));
        Assertions.assertEquals(lstEncontrado.size(), lstEsperado.size());
    }

    @Test
    void saveUser() {
        Mockito.when(oUserCrud.findByEmail(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(oUserCrud.save(Mockito.any())).thenAnswer(x -> new User(userDtoSave));
        userDtoSave.setName("Administrador");
        userDtoSave.setEmail("admin@admin.com");
        userDtoSave.setPassword("Pass12345");
        UserDtoSaved encontrado = oUserService.saveUser(userDtoSave);
        encontrado.setId(userDtoSaved.getId());
        UserDtoSaved esperado = userDtoSaved;
        Assertions.assertEquals(encontrado, esperado);
    }

    @Test
    void saveUserEmailExist() {
        Mockito.when(oUserCrud.findByEmail(Mockito.any()))
                .thenReturn(java.util.Optional.ofNullable(userAdmin));
        Assertions.assertThrows(UserApiBussinesException.class,
                () -> oUserService.saveUser(new UserDto(userAdmin)),
                "El correo ya existe");
    }
}
