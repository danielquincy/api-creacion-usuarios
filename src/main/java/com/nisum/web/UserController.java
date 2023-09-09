package com.nisum.web;

import com.nisum.logic.service.UserService;
import com.nisum.model.dto.LoginDto;
import com.nisum.model.dto.UserDto;
import com.nisum.model.dto.UserDtoSaved;
import com.nisum.model.entity.User;
import com.nisum.utils.AlterObjects;
import com.nisum.utils.exception.UserApiBussinesException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @apiNote
 */
@Tag(
        name = "Api REST para Usuarios de Nisum",
        description = "La API puede crear, loguear y obtener todos los usuarios")
@RestController
@RequestMapping(value = "/nisum")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDtoSaved> saveUser(final @RequestBody UserDto prUserDto) throws UserApiBussinesException {
        return ResponseEntity.ok(userService.saveUser(prUserDto));
    }
}
