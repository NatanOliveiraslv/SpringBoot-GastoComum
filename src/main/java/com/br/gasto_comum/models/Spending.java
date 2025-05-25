package com.br.gasto_comum.models;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String title;
    private Double value;
    @OneToOne
    @JoinColumn(name = "voucher_id")
    private Document voucher;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "spending_id")
    private List<ExpensesDividedAcconts> expensesDividedAcconts;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private LocalDateTime registration_date;


    public Spending(SpendingRequestDTO data) {
        this.title = data.title();
        this.type = data.type();
        this.value = data.value();
        this.description = data.description();
        this.registration_date = LocalDateTime.now();
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
