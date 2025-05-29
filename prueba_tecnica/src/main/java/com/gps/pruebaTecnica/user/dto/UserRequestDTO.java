package com.gps.pruebaTecnica.user.dto;

import com.gps.pruebaTecnica.user.domain.Role;
import com.gps.pruebaTecnica.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank
    private String username;

    private String password;
    @Email
    private String email;

    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;

    @NotEmpty
    private Set<String> roles;

    public UserRequestDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.documentType = user.getDocumentType();
        this.documentNumber = user.getDocumentNumber();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());
    }
}
