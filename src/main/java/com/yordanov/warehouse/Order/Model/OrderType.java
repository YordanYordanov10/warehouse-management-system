package com.yordanov.warehouse.Order.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

;


public enum OrderType {

    ONLINE, MANUAL, IMPORT
}