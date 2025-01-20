package com.rakshithr.enotes_api_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private  String email;

    private String password;

    private String mobNo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private AccountStatus status;

}
