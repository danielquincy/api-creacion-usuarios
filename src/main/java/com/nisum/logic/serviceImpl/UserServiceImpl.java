package com.nisum.logic.serviceImpl;

import com.nisum.logic.service.UserService;
import com.nisum.model.crud.UserCrud;
import com.nisum.model.dto.UserDto;
import com.nisum.model.dto.UserDtoSaved;
import com.nisum.model.entity.Phone;
import com.nisum.model.entity.User;
import com.nisum.utils.AlterObjects;
import com.nisum.utils.exception.UserApiBussinesException;
import com.nisum.utils.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserCrud oUserCrud;
    private final PasswordEncoder oPasswordEncoder;
    private final JwtUtils oJwtUtils;

    @Autowired
    public UserServiceImpl(UserCrud oUserCrud,
                           JwtUtils oJwtUtils) {
        this.oUserCrud = oUserCrud;
        this.oJwtUtils = oJwtUtils;
        this.oPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Método para obtener el usuario que corresponde a un id específico.
     *
     * @param prId de tipo UUID, contiene el id a buscar en la base de datos.
     * @return un objeto de tipo User si encuentra el id proporcionado, sino retorna una excepción de tipo
     * UserApiBussinesException indicando que no encontró el usuario.
     * @throws UserApiBussinesException checked, esta será manejada por la clase HandlerExceptionCustom la cual
     *                                  retornará un objeto de tipo ErrorDto.
     * @see User
     */
    public User getUserById(final UUID prId) throws UserApiBussinesException {
        return oUserCrud.findById(prId).orElseThrow(
                () -> new UserApiBussinesException("User not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Método para obtener el usuario que corresponde a un email específico.
     * Si no encuentra al usuario, no retornará una excepción.
     *
     * @param prEmail de tipo String, contiene el email a buscar en la base de datos.
     * @return un objeto de tipo User si encuentra el email proporcionado, sino retorna Optional.empty().
     * @see User
     */
    public Optional<User> getUserByEmail(final String prEmail) {
        return oUserCrud.findByEmail(prEmail);
    }

    /**
     * Método para obtener el usuario que corresponde a un email específico.
     * Si no encuentra al usuario, deberá retonar una excepción.
     *
     * @param prEmail de tipo String, contiene el email a buscar en la base de datos.
     * @return un objeto de tipo UserDto si encuentra el email proporcionado, sino retorna una excepción de tipo
     * UserApiBussinesException indicando que no encontró el usuario.
     * @throws UserApiBussinesException checked, esta será manejada por la clase HandlerExceptionCustom la cual
     *                                  retornará un objeto de tipo ErrorDto.
     * @see User
     */
    public UserDto getUserDtoByEmail(final String prEmail) throws UserApiBussinesException {
        User user = this.getUserByEmail(prEmail).orElseThrow(
                () -> new UserApiBussinesException("Usuario No Encontrado", HttpStatus.NOT_FOUND));
        return new UserDto(user);
    }

    /**
     * Método para obtener el catálogo completo de los usuarios.
     *
     * @return lista con objetor de tipo UserDto con los datos de los usuarios registrados en la base de datos.
     */
    public List<UserDto> getAllUser() {
        List<UserDto> lstUsers = new ArrayList<>();
        oUserCrud.findAll().forEach(x -> lstUsers.add(new UserDto(x)));
        return lstUsers;
    }

    /**
     * Método para registrar un nuevo usuario.
     * Si el email ya existe, debe retornar una excepción de tipo UserApiBussinesException indicando que ya existe.
     * Debe encriptar el password.
     * Debe generar un token y guardarlo en el registro del usuario.
     *
     * @param prUserDto dto con los datos basicos a registrar para un nuevo usuario (name, email, password, phones).
     * @return objeto de tipo UserDtoSaved que contiene solo las propiedades que se desean retornar el nuevo usuario.
     * @throws UserApiBussinesException checked, esta será manejada por la clase HandlerExceptionCustom la cual
     *                                  retornará un objeto de tipo ErrorDto.
     */
    public UserDtoSaved saveUser(final UserDto prUserDto) throws UserApiBussinesException {
        try {
            User prNewUser = new User(prUserDto);
            validUser(prNewUser);
            if (oUserCrud.findByEmail(prNewUser.getEmail()).isPresent()) {
                throw new UserApiBussinesException("El correo ya esta registrado. ", HttpStatus.FOUND);
            }
            prNewUser.setPassword(oPasswordEncoder.encode(prNewUser.getPassword()));
            prNewUser.setCreated(AlterObjects.getTimeNow());
            prNewUser.setToken(oJwtUtils.generateToken(prNewUser.getEmail()));
            oUserCrud.save(prNewUser);
            return new UserDtoSaved(prNewUser);
        } catch (Exception e) {
            if (e instanceof UserApiBussinesException) {
                throw e;
            } else {
                throw new UserApiBussinesException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Método para guardar datos indicando que un usuario inició sesión.
     * Se guardará el token asignado y la fecha y hora en que inició sesión.
     *
     * @param prUserUpdate objeto de tipo dto con los datos a actualizar al objeto User.
     * @throws UserApiBussinesException checked, se lanzará si el id proporcionado no se encuentra en la base de datos.
     *                                  Esta será manejada por la clase HandlerExceptionCustom la cual retornará
     *                                  un objeto de tipo ErrorDto.
     */
    public void saveLoginUser(final UserDto prUserUpdate) throws UserApiBussinesException {
        User user = this.getUserById(prUserUpdate.getId());
        user.setToken(prUserUpdate.getToken());
        user.setLastLogin(AlterObjects.getTimeNow());
        user.setModified(AlterObjects.getTimeNow());
        oUserCrud.save(user);
    }

    /**
     * Método privado de la implemetanción que permite validar los datos de un usuario a registrar o de un
     * usuario a modificar.
     *
     * @param prUser objeto de tipo User que contiene los datos del usuario a enviar a la base de datos.
     * @throws UserApiBussinesException checked, retornará los mensajes de las validaciones que no se cumplan.
     *                                  Esta será manejada por la clase HandlerExceptionCustom la cual
     *                                  retornará un objeto de tipo ErrorDto.
     */
    private void validUser(final User prUser) throws UserApiBussinesException {
        if (prUser == null) {
            throw new UserApiBussinesException("Error getting user data", HttpStatus.PARTIAL_CONTENT);
        }
        if (prUser.getName() == null || prUser.getName().trim().isEmpty()) {
            throw new UserApiBussinesException("name: required", HttpStatus.PARTIAL_CONTENT);
        }
        if (prUser.getEmail() == null || prUser.getEmail().trim().isEmpty()) {
            throw new UserApiBussinesException("email: required", HttpStatus.PARTIAL_CONTENT);
        } else if (!validReg(prUser.getEmail(), AlterObjects.regExpEmail)) {
            throw new UserApiBussinesException("email format: invalid, required format example aaaaaaa@dominio.cl",
                    HttpStatus.BAD_REQUEST);
        }
        if (prUser.getPassword() == null || prUser.getPassword().trim().isEmpty()) {
            throw new UserApiBussinesException("password: required", HttpStatus.PARTIAL_CONTENT);
        } else if (!validReg(prUser.getPassword(), AlterObjects.regExpPassword)) {
            throw new UserApiBussinesException("password format: invalid, "
                    + "required minimum 1 uppercase letter, lowercase letters and minimum 2 numbers",
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Método para validar expresiones regulares, espera como parámetro el string a evaluar y la expresión regular
     * a verificar.
     *
     * @param value      String a validar.
     * @param expression String con la expresión regular a aplicar.
     * @return true si el string a validar cumple los requisitos de la expresión regular,
     * de lo contrario, retornará false.
     */
    private Boolean validReg(final String value, final String expression) {
        Pattern pat = Pattern.compile(expression);
        Matcher mat = pat.matcher(value);
        return mat.find();
    }

}
