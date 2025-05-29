package com.gps.pruebaTecnica.user.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Audited
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document_type", "document_number"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonBackReference
    private Set<Role> roles = new HashSet<>();

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number")
    private String documentNumber;


    public User() {
    }

    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String firstName, String lastName, String documentType,
                String documentNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }

//    public User(@NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String email,
//                @NotBlank(message = "El nombre es obligatorio") String firstName,
//                String lastName,
//                String personType,
//                String documentType,
//                @NotBlank(message = "El número de documento es obligatorio") String documentNumber,
//                @NotBlank(message = "La fecha de nacimiento es obligatoria") String birthDate,
//                String gender,
//                @NotBlank(message = "El teléfono es obligatorio") String phone1,
//                String phone2,
//                String phone3,
//                String corporateEmail,
//                boolean isVip
//    ) {
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.personType = personType;
//        this.documentType = documentType;
//        this.documentNumber = documentNumber;
//        this.birthDate = birthDate;
//        this.placeOfBirth = placeOfBirth;
//        this.gender = gender;
//        this.phone1 = phone1;
//        this.phone2 = phone2;
//        this.phone3 = phone3;
//        this.corporateEmail = corporateEmail;
//        this.isVip = isVip;
//    }

    public User(String firstName, String lastName, String documentType, String documentNumber, String birthDate, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }
}
