package com.nisum.web;

import com.nisum.logic.service.UserService;
import com.nisum.model.dto.LoginDto;
import com.nisum.model.dto.UserDto;
import com.nisum.model.dto.UserDtoSaved;
import com.nisum.model.entity.User;
import com.nisum.utils.exception.UserApiBussinesException;
import com.nisum.utils.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    /**
     * Instancia que nos permite modificar los datos del usuario que inicia sesión, para registrar la última fecha de
     * inicio de sesión así como el token asignado.
     */
    private final UserService oUserService;
    /**
     * Instancia que nos brinda métodos para generar y validar los token de los usuarios.
     */
    private final JwtUtils oJwtUtils;
    private final AuthenticationManager oAuthenticationManager;

    @Autowired
    public AuthController(final UserService oUserService,
                          final JwtUtils oJwtUtils ,
                          final AuthenticationManager authenticationManager) {
        this.oUserService = oUserService;
        this.oJwtUtils = oJwtUtils;
        this.oAuthenticationManager = authenticationManager;
    }

    @GetMapping("/hi")
    public String welcome() {
        return "Welcome to my first api";
    }

    /**
     * Endpoint para solicitar el inicio de sesión usando email y password.
     * Se usa Spring Security para la validación de los datos y posteriormente se le genera un token al
     * usuario. Dicho token se persistirá en los datos del usuario, y le permitirá acceder a los otros endpoints.
     *
     * @param user tipo UserLoginDto contiene email y password del usuario.
     * @return status 200 si fue exitoso y el token asignado al usuario. Si los datos del usuario son incorrectos,
     * el authenticate de Spring lanzará una excepción.
     * @throws UserApiBussinesException checked.
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> login(final @RequestBody LoginDto user) throws UserApiBussinesException {
        try {
            oAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword()));
        } catch (Exception e) {
            if (e instanceof UserApiBussinesException) {
                throw e;
            } else {
                throw new UserApiBussinesException("email or password incorrect", HttpStatus.BAD_REQUEST);
            }
        }
        String token = oJwtUtils.generateToken(user.getEmail());
        UserDto userDto = oUserService.getUserDtoByEmail(user.getEmail());
        userDto.setToken(token);
        oUserService.saveLoginUser(userDto);
        return ResponseEntity.ok("{ \"token\": \"" + token + "\" }");
    }

    /**
     * Método para verificar si el usuario admin ya existe en la base de datos en memoria, si no existe se registrará.
     *
     * @return objeto de tipo UserDto con los datos de usuario administrador, si ya existía en la base de datos,
     * buscará los datos del registro en la base de datos,
     * sino se guardará el registro de usuario admin y retornará los datos de dicho registro.
     */
    @RequestMapping(method = RequestMethod.POST,
            value = "/init",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> saveUser() {
        User user = oUserService.getUserByEmail("useradmin@gmail.com").orElse(null);
        if (user == null) {
            UserDto userAdmin = new UserDto();
            userAdmin.setName("Administrador");
            userAdmin.setEmail("admin@admin.com");
            userAdmin.setPassword("Pass12345");
            UserDtoSaved userDtoSaved = oUserService.saveUser(userAdmin);
            user = oUserService.getUserById(userDtoSaved.getId());
        }
        return ResponseEntity.ok(new UserDto(user));
    }
}
