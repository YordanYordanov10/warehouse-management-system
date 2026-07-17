package com.yordanov.warehouse.Inventory.Model;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private int quantity;

    private int reservedQuantity;

    @Version
    private int version;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
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

    private void checkQuantity(int quantity){
        if(quantity <= 0){
            throw new ConflictException("The Quantity must be greater than zero");
        }
    }

    public void receive(int quantity){
        
        checkQuantity(quantity);
        this.quantity += quantity; 
    }

    public void reserve(int quantity){

        checkQuantity(quantity);
        if(getAvailableQuantity() < quantity){
            throw new ConflictException("Not enough quantity to reserve, available is %s".formatted(getAvailableQuantity()));
        }

        this.reservedQuantity += quantity;
    }

    public void release(int quantity){
        
        checkQuantity(quantity);
        if(reservedQuantity < quantity){
            throw new ConflictException("Not enough stock to release, wanted - %s, reserved - %s".formatted(quantity,reservedQuantity ));
        }

        this.reservedQuantity -= quantity;
    }

    public void ship(int quantity){

         checkQuantity(quantity);
        if(reservedQuantity < quantity){
             throw new ConflictException("Not enough reserved stock to ship,  wanted - %s, reserved - %s".formatted(quantity,reservedQuantity));
        }

        this.reservedQuantity -= quantity;
        this.quantity -= quantity;
    }

    public static Inventory createEmpty(Product product, Warehouse warehouse){
        
        Inventory inventory = new Inventory();
      
        inventory.quantity = 0;
        inventory.reservedQuantity = 0;
        inventory.product = product;
        inventory.warehouse = warehouse;

        return inventory;
    }

}
