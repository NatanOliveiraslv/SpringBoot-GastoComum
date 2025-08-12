package com.br.gasto_comum.models;

import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.exceptions.SpendingIsAlreadyInGroup;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Group")
@Table(name = "groupspending")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double total_value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<Spending> spendings;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public Group(GroupRequestDTO data) {
        this.name = data.name();
        this.description = data.description();
        this.total_value = 0.0;
        this.spendings = new ArrayList<>();
    }

    public boolean checkIfTheSpendingIsGroup(Spending spending) {

        for (Spending e : this.spendings) {
            if (e.getId().equals(spending.getId())) {
                return true;
            }
        }

        if (spending.getId() != null) {
            throw new SpendingIsAlreadyInGroup("Gasto já está em outro grupo. ID: " + spending.getGroup().getId());
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
