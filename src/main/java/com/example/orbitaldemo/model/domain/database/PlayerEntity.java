package com.example.orbitaldemo.model.domain.database;

import com.example.orbitaldemo.model.enums.PlayerPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private PlayerPosition position;

    @Column(name = "market_value", nullable = false)
    private BigDecimal marketValue;

}