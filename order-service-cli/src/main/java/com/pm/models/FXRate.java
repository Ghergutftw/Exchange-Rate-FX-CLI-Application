package com.pm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FXRate {
    @JsonProperty("ccyPair")
    private CurrencyPair ccyPair;

    @JsonProperty("bid")
    private double bid;

    @JsonProperty("ask")
    private double ask;
}
