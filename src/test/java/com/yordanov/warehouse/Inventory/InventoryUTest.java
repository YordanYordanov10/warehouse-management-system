package com.yordanov.warehouse.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yordanov.warehouse.Exception.ConflictException;
import com.yordanov.warehouse.Inventory.Model.Inventory;
import com.yordanov.warehouse.Product.Model.Product;
import com.yordanov.warehouse.Warehouse.Model.Warehouse;
import com.yordanov.warehouse.Warehouse.Model.WarehouseStatus;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
public class InventoryUTest{

    private Product product;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Test Product")
                .sku("SKU-001")
                .price(BigDecimal.TEN)
                .build();

        warehouse = Warehouse.builder()
                .name("Test Warehouse")
                .warehouseCode("WH-001")
                .address("Some street 1")
                .city("Sofia")
                .postalCode("1000")
                .country("Bulgaria")
                .maxPalletCapacity(100)
                .warehouseStatus(WarehouseStatus.ACTIVE)
                .build();
    }

   @Test
    void getAvailableQuantity_subtractsReservedFromQuantity() {

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(5);

        assertEquals(5, inventory.getAvailableQuantity());
    }

    @Test
    void reserve_shouldReserveQuantity_whenRequestedIsLessFromActual(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(6);

        assertEquals(6, inventory.getReservedQuantity());
    }

    @Test
    void reserve_shouldReserveQuantity_whenRequestedIsEqualWithActual(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(10);

        assertEquals(10, inventory.getReservedQuantity());
    }

    @Test
    void reserve_shouldThrowException_whenRequestedQuantityIsZero(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);
        inventory.receive(10);

        assertThrows(
            ConflictException.class,
                () -> inventory.reserve(0)
        );
    }

     @Test
    void reserve_shouldThrowException_whenRequestedQuantityIsNegative(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);
        inventory.receive(10);

        assertThrows(
            ConflictException.class,
                () -> inventory.reserve(-88)
        );
    }

    @Test
    void reserve_shouldThrowException_whenRequestedQuantityExceedsAvailable(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);
        inventory.receive(10);

        assertThrows(
            ConflictException.class,
                () -> inventory.reserve(15)
        );
    }

     @Test
    void reserve_shouldThrowException_whenRequestedQuantityToEmptyInventory(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        assertThrows(
            ConflictException.class,
                () -> inventory.reserve(15)
        );
    }

    @Test
    void receive_shouldIncreaseQuantitywithGivenQuantity(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);
        inventory.receive(10);
        inventory.receive(3);

        assertEquals(13, inventory.getQuantity());
    }


    @Test
    void receive_shouldThrowException_withNegtiveQuantity(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        assertThrows(
            ConflictException.class, () -> inventory.receive(-10));
    }

    @Test
    void receive_shouldThrowException_withZeroQuantity(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        assertThrows(
            ConflictException.class, () -> inventory.receive(0));
    }

    @Test
    void release_shouldDecreaseReservedQuantity_withGivenQuantity(){
         Inventory inventory = Inventory.createEmpty(product, warehouse);

          inventory.receive(10);
          inventory.reserve(6);
          inventory.release(6);

          assertEquals(0, inventory.getReservedQuantity());
          assertEquals(10, inventory.getQuantity());
          
    }

    @Test
    void release_shouldThrowException_withGivenQuantityBiggerFromReservedQuantity(){
         Inventory inventory = Inventory.createEmpty(product, warehouse);

          inventory.receive(10);
          inventory.reserve(6);
         
         assertThrows(
            ConflictException.class, () -> inventory.release(7));
          
    }

    @Test
    void release_shouldThrowException_withNegtiveQuantity(){
         Inventory inventory = Inventory.createEmpty(product, warehouse);

          inventory.receive(10);
          inventory.reserve(6);
         
         assertThrows(
            ConflictException.class, () -> inventory.release(-3));
          
    }

    @Test
    void release_shouldThrowException_withZeroQuantity(){
         Inventory inventory = Inventory.createEmpty(product, warehouse);

          inventory.receive(10);
          inventory.reserve(6);
         
         assertThrows(
            ConflictException.class, () -> inventory.release(0));
          
    }

    @Test
    void ship_shouldDecreaseQuantityAndReservedQuantity_withGivenQuantity(){
        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(6);
        inventory.ship(4);

        assertEquals(6, inventory.getQuantity());
        assertEquals(2, inventory.getReservedQuantity());
    }

    @Test
    void ship_shouldDecreaseQuantityAndReservedQuantity_whenShipQuantitiIsEqualWithReservedQuantity(){
        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(6);
        inventory.ship(6);

        assertEquals(4, inventory.getQuantity());
        assertEquals(0, inventory.getReservedQuantity());
    }

    @Test
    void ship_shouldThrownException_whenReservedQuantityIsLess(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(10);
        inventory.reserve(1);

        assertThrows(
            ConflictException.class, () ->  inventory.ship(4));
    }

    @Test
    void ship_shouldThrownException_whenReservedQuantityAntQuantiryIsLess(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(5);
        inventory.reserve(3);

        assertThrows(
            ConflictException.class, () ->  inventory.ship(6));
    }

    @Test
    void ship_shouldThrownException_whenShipQuantityIsZero(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(5);
        inventory.reserve(3);

        assertThrows(
            ConflictException.class, () ->  inventory.ship(0));
    }

    @Test
    void ship_shouldThrownException_whenShipQuantityIsNegative(){

        Inventory inventory = Inventory.createEmpty(product, warehouse);

        inventory.receive(5);
        inventory.reserve(3);

        assertThrows(
            ConflictException.class, () ->  inventory.ship(-5));
    }
   
}
