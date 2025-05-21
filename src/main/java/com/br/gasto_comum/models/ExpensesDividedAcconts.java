package com.br.gasto_comum.models;

import com.br.gasto_comum.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "ExpensesDividedAcconts")
@Table(name = "expensesdividedacconts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ExpensesDividedAcconts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double value;
    private LocalDateTime date_payment;
    @ManyToOne
    @JoinColumn(name = "spending_id")
    private Spending spending;

    public ExpensesDividedAcconts(User user, Spending spending) {
        this.user = user;
        this.status = Status.PENDING;
        this.spending = spending;
    }

    public void makePayment(double value) {
        this.value = this.value - value;
        if(this.value == 0){
            this.status = Status.PAID;
        }
        this.date_payment = LocalDateTime.now();
    }
}
