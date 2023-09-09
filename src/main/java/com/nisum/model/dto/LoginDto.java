package com.nisum.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDto {

    @NotNull(message = "El correo no puede ser nulo")
    @NotEmpty(message = "Este campo no debe estar vacio")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
            , message = "El formato del correo no es correcto")
    private String email;

    @NotNull(message = "El correo no puede ser nulo")
    @NotEmpty(message = "Este campo no debe estar vacio")
    private String password;

}
