package com.yordanov.warehouse.ReferenceSequence.Service;

import com.yordanov.warehouse.Exception.ResourceNotFoundException;
import com.yordanov.warehouse.InventoryMovement.Model.ReferenceType;
import com.yordanov.warehouse.ReferenceSequence.Model.ReferenceSequence;
import com.yordanov.warehouse.ReferenceSequence.Repository.ReferenceSequenceRepository;
import com.yordanov.warehouse.Warehouse.Repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReferenceSequenceService {

    private final ReferenceSequenceRepository referenceSequenceRepository;
    private final WarehouseRepository warehouseRepository;

    public ReferenceSequenceService(ReferenceSequenceRepository referenceSequenceRepository, WarehouseRepository warehouseRepository) {
        this.referenceSequenceRepository = referenceSequenceRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public String generateReference(ReferenceType type, UUID warehouseId) {

        LocalDate date = LocalDate.now();

        ReferenceSequence referenceSequence = referenceSequenceRepository
                .findByTypeAndWarehouseIdAndDate(type, warehouseId, date)
                .orElseGet(() -> ReferenceSequence.builder()
                        .type(type)
                        .warehouseId(warehouseId)
                        .date(date)
                        .currentValue(0L)
                        .build());

        referenceSequence.setCurrentValue(referenceSequence.getCurrentValue() + 1);

        referenceSequenceRepository.save(referenceSequence);

        String warehouseCode = null;

        if (warehouseId != null) {
            warehouseCode = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new ResourceNotFoundException(""))
                .getWarehouseCode();
        }

        return buildReference(type, warehouseCode, date, referenceSequence.getCurrentValue());
    }

    private String buildReference(ReferenceType type, String warehouseCode, LocalDate date, long currentValue) {
        String reference  = null;

        String formattedSequence = String.format("%06d", currentValue);

        if(warehouseCode == null) {
            reference = type + "-" + date + "-" +  currentValue;
        } else {
          reference = type + "-" + warehouseCode + "-" + date + "-" + currentValue;
        }

        return reference;
    }
}
