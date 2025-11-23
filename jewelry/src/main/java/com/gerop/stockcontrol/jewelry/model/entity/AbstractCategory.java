package com.gerop.stockcontrol.jewelry.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="category_abstract")
@Getter
@Setter
@NoArgsConstructor
public class AbstractCategory {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max=30)
    private String name;

    private boolean isGlobal = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public AbstractCategory(String name, boolean isGlobal, User owner) {
        this.name = name;
        this.isGlobal = isGlobal;
        this.owner = owner;
    }
}
