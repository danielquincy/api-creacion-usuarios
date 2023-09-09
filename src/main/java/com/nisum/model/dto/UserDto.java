package com.nisum.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nisum.model.entity.Phone;
import com.nisum.model.entity.User;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.nisum.utils.AlterObjects.getTimeNow;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private UUID id;
    private String name;
    @NotNull(message = "El correo no puede ser nulo")
    @NotEmpty(message = "Este campo no debe estar vacio")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
            , message = "Este correo no tiene un patron valido")
    private String email;
    @NotNull(message = "El password no puede ser nulo")
    @NotEmpty(message = "Este campo no debe estar vacio")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{8,}$"
            ,message = "Este password no tiene un patron valido")
    private String password;
    private List<PhoneDto> phones;
    private boolean isActive;
    private Timestamp lastLogin;
    private Timestamp created;
    private String token;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.created = Objects.isNull(user.getCreated())
                ? getTimeNow()
                : user.getCreated();

        this.lastLogin = Objects.isNull(user.getLastLogin())
                ? getTimeNow()
                : user.getLastLogin();

        this.token = user.getToken();
        this.isActive = user.getIsActive();
        this.phones = new ArrayList<>();

        if (user.getPhones() != null) {
            user.getPhones().forEach(x -> phones.add(new PhoneDto(x)));
        }
    }

    public UserDto(String name, String email, String password, boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    public <E> UserDto(String usuarioTest, String mail, String prueba123, ArrayList<E> es, String s) {
    }
}

