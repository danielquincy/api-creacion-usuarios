package com.nisum.utils;

import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@NoArgsConstructor
public final class AlterObjects {
    public static String getMessage(MessageSource messageSource, String messageKey, Object... arguments) {
        return messageSource.getMessage(messageKey, arguments, Locale.getDefault());
    }

    public static final String regExpEmail
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";
    public static final String regExpPassword
            = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    public static Timestamp getTimeNow() {
        return new Timestamp(new java.util.Date().getTime());
    }

    public static Timestamp toTimestamp(Object value) throws Exception {
        try {
            DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = formato.parse(value.toString());

            return new Timestamp(date.getTime());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static Date toDate(Object value) throws Exception {
        try {
            DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return formato.parse(value.toString());

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
