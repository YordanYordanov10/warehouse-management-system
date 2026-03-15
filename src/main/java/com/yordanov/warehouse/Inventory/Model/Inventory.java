package com.yordanov.warehouse.Inventory.Model;

import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
        }
)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private int quantity;

    private int reservedQuantity;

    @Version
    private int version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;


    public int getAvailableQuantity(){
        return getQuantity() - getReservedQuantity();
    }

}
