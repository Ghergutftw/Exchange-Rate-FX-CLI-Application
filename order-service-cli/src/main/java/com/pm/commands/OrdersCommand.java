package com.pm.commands;

import com.google.common.collect.Ordering;
import com.pm.models.FXRate;
import com.pm.models.Order;
import com.pm.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command implementation for displaying all orders in the order book.
 * Orders are displayed sorted by currency pair and their distance to current market rates.
 */
@RequiredArgsConstructor
public class OrdersCommand implements Command {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private final OrderService orderService;

    /**
     * Executes the orders command, displaying all orders sorted by currency pair and distance to market rate.
     *
     * @param args Command arguments (not used for this command)
     * @throws Exception If an error occurs while fetching or processing orders
     */
    @Override
    public void execute(String[] args) throws Exception {
        List<Order> orders = orderService.getAllOrders();
        List<FXRate> rates = orderService.getExchangeRates();

        if (orders.isEmpty()) {
            System.out.println("No orders to display");
            return;
        }

        Map<String, FXRate> rateMap = createRateMap(rates);
        List<OrderWithDistance> ordersWithDistance = calculateDistances(orders, rateMap);
        printOrders(ordersWithDistance);
    }

    /**
     * Creates a map of currency pairs to their corresponding FX rates.
     *
     * @param rates List of FX rates to map
     * @return Map of currency pair strings to their FX rate objects
     */
    private Map<String, FXRate> createRateMap(List<FXRate> rates) {
        return rates.stream()
                .collect(Collectors.toMap(
                        rate -> rate.getCcyPair().getCcy1().toString() + rate.getCcyPair().getCcy2().toString(),
                        rate -> rate
                ));
    }

    /**
     * Calculates distances between order limits and current market rates.
     *
     * @param orders List of orders to process
     * @param rateMap Map of currency pairs to their FX rates
     * @return List of orders with their calculated distances to market rates
     */
    private List<OrderWithDistance> calculateDistances(List<Order> orders, Map<String, FXRate> rateMap) {
        return orders.stream()
                .map(order -> new OrderWithDistance(order, calculateDistance(order, rateMap)))
                .sorted(createOrderingRule())
                .toList();
    }

    /**
     * Calculates the distance between an order's limit price and the current market rate.
     *
     * @param order The order to calculate distance for
     * @param rateMap Map of currency pairs to their FX rates
     * @return The calculated distance, or 0.0 if no rate is found
     */
    private double calculateDistance(Order order, Map<String, FXRate> rateMap) {
        String pair = order.getInvestmentCcy() + order.getCounterCcy();
        FXRate rate = rateMap.get(pair);

        if (rate != null) {
            return Math.abs(rate.getAsk() - order.getLimit());
        }

        String reversePair = order.getCounterCcy() + order.getInvestmentCcy();
        FXRate reverseRate = rateMap.get(reversePair);

        if (reverseRate != null) {
            return Math.abs(reverseRate.getAsk() - order.getLimit());
        }

        return 0.0;
    }

    private Ordering<OrderWithDistance> createOrderingRule() {
        return Ordering.from(
                Comparator.<OrderWithDistance, String>comparing(o -> o.order().getInvestmentCcy() + o.order().getCounterCcy())
                        .thenComparingDouble(OrderWithDistance::distance)
        );
    }

    private void printOrders(List<OrderWithDistance> ordersWithDistance) {
        System.out.printf("%-4s %-4s %-4s %-4s %8s %12s %8s%n",
                "ID", "TYPE", "INV", "CTR", "LIMIT", "VALIDITY", "DISTANCE");
        System.out.println("-----------------------------------------------------");

        ordersWithDistance.forEach(this::printOrder);
    }


    private void printOrder(OrderWithDistance orderWithDistance) {
        Order order = orderWithDistance.order();
        String orderType = order.isBuy() ? "buy" : "sell";
        String formattedValidUntil = formatDate(order.getValidUntil());

        System.out.printf("%-4s %-4s %-4s %-4s %8.2f %12s %8.3f%n",
                order.getId(),
                orderType,
                order.getInvestmentCcy(),
                order.getCounterCcy(),
                order.getLimit(),
                formattedValidUntil,
                orderWithDistance.distance());
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "N/A";
    }

    /**
     * Record representing an order and its distance to the current market rate.
     *
     * @param order The order
     * @param distance The calculated distance to current market rate
     */
    private record OrderWithDistance(Order order, double distance) {
    }
}