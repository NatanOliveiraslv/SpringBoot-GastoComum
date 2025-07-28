package com.br.gasto_comum.models;

import com.br.gasto_comum.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "ExpensesDividedAcconts")
@Table(name = "expensesdividedacconts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ExpensesDividedAcconts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Double value;

    private LocalDateTime date_payment;

    @ManyToOne
    @JoinColumn(name = "spending_id")
    private Spending spending;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public ExpensesDividedAcconts(User user, Spending spending) {
        this.user = user;
        this.status = Status.PENDING;
        this.spending = spending;
        this.value = 0.0;
    }

    public void makePayment(double value) {
        if (this.value < value) {
            throw new IllegalArgumentException("Payment value exceeds the remaining balance.");
        }
        this.value = this.value - value;
        if(this.value == 0){
            this.status = Status.PAID;
        }
        this.date_payment = LocalDateTime.now();
    }
}
