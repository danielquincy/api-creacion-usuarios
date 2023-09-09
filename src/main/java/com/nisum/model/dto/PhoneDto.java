package com.nisum.model.dto;

import com.nisum.model.entity.Phone;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {
    private String number;
    private String cityCode;
    private String countryCode;

    public PhoneDto(Phone phone) {
        this.number = phone.getNumber();
        this.cityCode = phone.getCitycode();
        this.countryCode = phone.getCitycode();
    }
}
