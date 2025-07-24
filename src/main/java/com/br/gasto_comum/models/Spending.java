package com.br.gasto_comum.models;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "Spending")
@Table(name = "spending")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Spending {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double value;

    @OneToOne
    @JoinColumn(name = "voucher_id")
    private Document voucher;
    private String description;

    @Column(name = "date_spending")
    private LocalDate dateSpending;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "spending_id")
    private List<ExpensesDividedAcconts> expensesDividedAcconts;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public Spending(SpendingRequestDTO data) {
        this.title = data.title();
        this.type = data.type();
        this.value = data.value();
        this.description = data.description();
        this.dateSpending = data.dateSpending();
    }

    public void update(SpendingUpdateDTO data) {
        if (data.type() != null) {
            this.type = data.type();
        }
        if (data.title() != null) {
            this.title = data.title();
        }
        if (data.description() != null) {
            this.description = data.description();
        }
    }

    public boolean checkIfTheUserIsSpending(User user) {
        for (ExpensesDividedAcconts e : this.expensesDividedAcconts) {
            if (e.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void addExpensesDividedAcconts(ExpensesDividedAcconts expensesDividedAcconts) {
        if(checkIfTheUserIsSpending(expensesDividedAcconts.getUser())) {
            throw new RuntimeException("Usuário já está na lista de despesas divididas");
        }
        this.expensesDividedAcconts.add(expensesDividedAcconts);
    }
}
