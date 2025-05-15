package com.example.orbitaldemo.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private CharSequence password;

    @NotBlank(message = "Team name is required")
    private String teamName;

    @NotNull(message = "Country is required")
    private Long countryId;

}
