package com.pm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a pair of currencies used in FX trading.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyPair {
    /** The base currency of the pair */
    @JsonProperty("ccy1")
    private Currency ccy1;

    /** The quote currency of the pair */
    @JsonProperty("ccy2")
    private Currency ccy2;
}