package com.franktranvantu.springboot3.dto.request;

import com.franktranvantu.springboot3.validator.Birthdate;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 4, message = "Password must be at least {min} characters")
    String password;

    String firstName;
    String lastName;

    @Birthdate(min = 18)
    LocalDate dob;

    Set<String> roles;
}
