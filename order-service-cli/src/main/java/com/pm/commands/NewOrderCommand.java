package com.pm.commands;

import com.pm.models.Currency;
import com.pm.models.CurrencyPair;
import com.pm.models.Order;
import com.pm.models.OrderType;
import com.pm.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Command implementation for creating new orders in the system.
 * Handles validation of order parameters and supports a predefined set of currency pairs.
 */

@RequiredArgsConstructor
public class NewOrderCommand implements Command {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    // From /supportedCurrencyPairs
    private static final Set<CurrencyPair> SUPPORTED_PAIRS = new HashSet<>(Arrays.asList(
            new CurrencyPair(Currency.EUR, Currency.USD),
            new CurrencyPair(Currency.EUR, Currency.GBP),
            new CurrencyPair(Currency.EUR, Currency.SEK),
            new CurrencyPair(Currency.EUR, Currency.NOK),
            new CurrencyPair(Currency.USD, Currency.SEK),
            new CurrencyPair(Currency.USD, Currency.NOK),
            new CurrencyPair(Currency.USD, Currency.JPY),
            new CurrencyPair(Currency.USD, Currency.ZAR),
            new CurrencyPair(Currency.EUR, Currency.CHF),
            new CurrencyPair(Currency.USD, Currency.CHF)
    ));
    private final OrderService orderService;

    /**
     * Executes the new order command, creating a new order with the specified parameters.
     *
     * @param args Command arguments in the format: new [buy|sell] <investment ccy> <counter ccy> <limit> <validity>
     * @throws IllegalArgumentException if any of the parameters are invalid or the currency pair is not supported
     * @throws Exception if an unexpected error occurs during order creation
     */

    @Override
    public void execute(String[] args) throws Exception {

        if (args.length != 6) {
            throw new IllegalArgumentException("Usage: new [buy|sell] <investment ccy> <counter ccy> <limit> <validity>");
        }

        try {
            OrderType type = parseOrderType(args[1]);
            Currency investmentCcy = parseCurrency(args[2]);
            Currency counterCcy = parseCurrency(args[3]);
            validateCurrencyPair(investmentCcy, counterCcy);
            double limit = parseLimit(args[4]);
            LocalDate validity = parseValidity(args[5]);

            Order order = new Order(type, investmentCcy, counterCcy, limit, validity);
            Order createdOrder = orderService.createOrder(order);
            System.out.println("Order created: " + createdOrder.getId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create order: " + e.getMessage());
        }
    }

    /**
     * Parses the order type string to an OrderType enum value.
     *
     * @param typeStr The order type string ("buy" or "sell")
     * @return The corresponding OrderType enum value
     * @throws IllegalArgumentException if the order type is invalid
     */

    private OrderType parseOrderType(String typeStr) {
        try {
            return OrderType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order type must be 'buy' or 'sell'");
        }
    }

    /**
     * Parses a currency string to a Currency enum value.
     *
     * @param currencyStr The currency code string
     * @return The corresponding Currency enum value
     * @throws IllegalArgumentException if the currency code is invalid
     */
    private Currency parseCurrency(String currencyStr) {
        try {
            return Currency.fromString(currencyStr);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Validates that the given currency pair is supported for trading.
     *
     * @param ccy1 The first currency
     * @param ccy2 The second currency
     * @throws IllegalArgumentException if the currencies are the same or the pair is not supported
     */
    private void validateCurrencyPair(Currency ccy1, Currency ccy2) {
        if (ccy1 == ccy2) {
            throw new IllegalArgumentException("Investment and counter currency cannot be the same: " + ccy1);
        }

        CurrencyPair pair = new CurrencyPair(ccy1, ccy2);
        CurrencyPair reversePair = new CurrencyPair(ccy2, ccy1);

        if (!SUPPORTED_PAIRS.contains(pair) && !SUPPORTED_PAIRS.contains(reversePair)) {
            String supportedPairs = SUPPORTED_PAIRS.stream()
                    .map(p -> p.getCcy1() + "/" + p.getCcy2())
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException(
                    String.format("Unsupported currency pair: %s/%s. Supported pairs: %s",
                            ccy1, ccy2, supportedPairs));
        }
    }

    /**
     * Parses and validates the limit price string.
     *
     * @param limitStr The limit price as a string
     * @return The parsed limit price as a double
     * @throws IllegalArgumentException if the limit is invalid or not positive
     */
    private double parseLimit(String limitStr) {
        try {
            double limit = Double.parseDouble(limitStr);
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be greater than zero");
            }
            return limit;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid limit format: " + limitStr);
        }
    }

    /**
     * Parses and validates the validity date string.
     *
     * @param validityStr The validity date in dd.MM.yyyy format
     * @return The parsed LocalDate
     * @throws IllegalArgumentException if the date format is invalid or the date is in the past
     */

    private LocalDate parseValidity(String validityStr) {
        try {
            LocalDate validity = LocalDate.parse(validityStr, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            if (validity.isBefore(today)) {
                throw new IllegalArgumentException("Validity date must be in the future");
            }

            return validity;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: dd.MM.yyyy");
        }
    }
}