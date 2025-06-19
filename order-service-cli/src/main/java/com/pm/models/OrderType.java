package com.pm.models;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum OrderType {
    BUY("BUY"),
    SELL("SELL");

    private final String value;
}
