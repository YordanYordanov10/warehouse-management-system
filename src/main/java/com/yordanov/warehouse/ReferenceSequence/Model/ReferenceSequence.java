package com.yordanov.warehouse.ReferenceSequence.Model;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"type", "warehouse_id", "date"}
        )
)
public class ReferenceSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType type;

    @Column(nullable = true)
    private UUID warehouseId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private long currentValue;
}