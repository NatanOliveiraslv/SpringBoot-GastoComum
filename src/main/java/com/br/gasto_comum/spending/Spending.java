package com.br.gasto_comum.spending;

import com.br.gasto_comum.ExpensesDividedAcconts.ExpensesDividedAcconts;
import com.br.gasto_comum.users.User;
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
    //private Voucher voucher;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "spending_id")
    private List<ExpensesDividedAcconts> expensesDividedAcconts;

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

    public void addExpensesDividedAcconts(ExpensesDividedAcconts expensesDividedAcconts) {
        this.expensesDividedAcconts.add(expensesDividedAcconts);
    }
}
