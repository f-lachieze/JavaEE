package org.example.ProjetJavaEE.Ex.modele;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "APP_ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // ... (Ajoutez les autres getters/setters et les méthodes equals/hashCode pour 'name')
}