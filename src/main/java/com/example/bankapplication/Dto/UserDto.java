package com.example.bankapplication.Dto;

import com.example.bankapplication.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String otherName;
    private Gender gender;
    private String address;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
}
