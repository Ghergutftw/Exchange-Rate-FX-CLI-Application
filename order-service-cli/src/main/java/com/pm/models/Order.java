package com.pm.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private static int nextId = 1;
    private String id;
    @JsonProperty("investmentCcy")
    private String investmentCcy;

    @JsonProperty("buy")
    private boolean buy;

    @JsonProperty("counterCcy")
    private String counterCcy;

    @JsonProperty("limit")
    private double limit;

    @JsonProperty("validUntil")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate validUntil;

    public Order(OrderType type, Currency investmentCcyEnum, Currency counterCcyEnum, double limit, LocalDate validity) {
        this.id = String.valueOf(nextId++);
        this.investmentCcy = investmentCcyEnum.name();
        this.buy = (type == OrderType.BUY);
        this.counterCcy = counterCcyEnum.name();
        this.limit = limit;
        this.validUntil = validity;
    }
}