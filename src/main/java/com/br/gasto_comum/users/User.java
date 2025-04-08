package com.br.gasto_comum.users;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "User")
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;
    private String firstName;
    private String lastName;
    private String email;

    public User(UserRequestDTO data) {
        this.login = data.login();
        this.senha = data.senha();
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.email = data.email();
    }
}
