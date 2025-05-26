package com.br.gasto_comum.models;

import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.exceptions.SpendingIsAlreadyInGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Group")
@Table(name = "groupspending")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "group_id")
    private List<Spending> spendings;
    private Double total_value;

    public Group(GroupRequestDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.total_value = 0.0;
    }

    public boolean checkIfTheSpendingIsGroup(Spending spending) {
        for (Spending e : this.spendings) {
            if (e.getId().equals(spending.getId())) {
                return true;
            }
        }
        return false;
    }

    public void addSpending(Spending spending) {
        if(checkIfTheSpendingIsGroup(spending)) {
            throw new SpendingIsAlreadyInGroup("Gasto já está na lista do grupo. ID: " + spending.getId());
        }
        this.spendings.add(spending);
        this.total_value += spending.getValue();
    }

}
