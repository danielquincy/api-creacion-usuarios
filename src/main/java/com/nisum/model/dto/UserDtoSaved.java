package com.nisum.model.dto;


import com.nisum.model.entity.User;
import com.nisum.utils.AlterObjects;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Data
public class UserDtoSaved {
    private UUID id;
    private Timestamp created;
    private Timestamp modified;
    private Timestamp lastLogin;
    private String token;
    private Boolean isActive;

    public UserDtoSaved() {

    }

    public UserDtoSaved(UUID id,
                        Timestamp created,
                        Timestamp modified,
                        Timestamp lastLogin,
                        String token,
                        Boolean isActive){
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

    public UserDtoSaved(User user) {
        this.id = user.getId();
        this.created = Objects.isNull(user.getCreated())
                ? AlterObjects.getTimeNow()
                : user.getCreated();
        this.lastLogin = Objects.isNull(user.getLastLogin())
                ? AlterObjects.getTimeNow()
                : user.getLastLogin();

        this.token = user.getToken();
        this.isActive = user.getIsActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoSaved that = (UserDtoSaved) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
