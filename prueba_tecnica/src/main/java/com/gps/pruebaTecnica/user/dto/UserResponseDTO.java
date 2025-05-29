package com.gps.pruebaTecnica.user.dto;

import com.gps.pruebaTecnica.user.domain.Role;
import com.gps.pruebaTecnica.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private Set<String> roles;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.documentType = user.getDocumentType();
        this.documentNumber = user.getDocumentNumber();
        this.roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
