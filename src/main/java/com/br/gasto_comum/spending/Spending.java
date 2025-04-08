package com.br.gasto_comum.spending;

import com.br.gasto_comum.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Spending")
@Table(name = "spending")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Spending {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Double value;
    //private Voucher voucher;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime registration_date = LocalDateTime.now();


    public Spending(SpendingRequestDTO data) {
        this.type = data.type();
        this.value = data.value();
        this.description = data.description();
    }
}
