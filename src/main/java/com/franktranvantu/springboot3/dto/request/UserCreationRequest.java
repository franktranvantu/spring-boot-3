package com.franktranvantu.springboot3.dto.request;

import com.franktranvantu.springboot3.validator.Birthdate;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "Username must be at least {min} characters")
    String username;
    @Size(min = 4, message = "Password must be at least {min} characters")
    String password;
    String firstName;
    String lastName;
    @Birthdate(min = 18)
    LocalDate dob;
}
