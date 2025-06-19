package com.pm.models;

public enum Currency {
    /** Euro currency */
    EUR,
    /** US Dollar currency */
    USD,
    /** British Pound currency */
    GBP,
    /** Swedish Krona currency */
    SEK,
    /** Norwegian Krone currency */
    NOK,
    /** Japanese Yen currency */
    JPY,
    /** South African Rand currency */
    ZAR,
    /** Swiss Franc currency */
    CHF;


    /**
     * Converts a string representation to a Currency enum value.
     *
     * @param currency The currency code as a string
     * @return The corresponding Currency enum value
     * @throws IllegalArgumentException if the currency code is not supported
     */

    public static Currency fromString(String currency) {
        try {
            return Currency.valueOf(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency: " + currency + ". Supported currencies: " +
                    String.join(", ", java.util.Arrays.stream(Currency.values())
                            .map(Currency::name)
                            .toList()));
        }
    }
}