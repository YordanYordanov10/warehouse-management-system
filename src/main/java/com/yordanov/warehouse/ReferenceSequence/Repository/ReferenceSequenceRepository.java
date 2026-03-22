package com.yordanov.warehouse.ReferenceSequence.Repository;

import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.ReferenceSequence.Model.ReferenceSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReferenceSequenceRepository extends JpaRepository<ReferenceSequence, UUID> {
    Optional<ReferenceSequence> findByTypeAndWarehouseIdAndDate(ReferenceType type, UUID warehouseId, LocalDate date);
}
