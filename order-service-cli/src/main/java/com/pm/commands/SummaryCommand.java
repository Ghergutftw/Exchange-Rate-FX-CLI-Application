package com.pm.commands;

import com.pm.models.Order;
import com.pm.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command implementation for displaying a summary of orders grouped by currency pair and type.
 * Shows the count and average limit price for each group.
 */

@RequiredArgsConstructor
public class SummaryCommand implements Command {
    private final OrderService orderService;

    /**
     * Executes the summary command, displaying grouped statistics about current orders.
     *
     * @param args Command arguments (not used for this command)
     * @throws Exception If an error occurs while fetching or processing orders
     */

    @Override
    public void execute(String[] args) throws Exception {
        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders to summarize");
            return;
        }

//        Collects data
        Map<SummaryKey, List<Order>> groupedOrders = orders.stream()
                .collect(Collectors.groupingBy(order ->
                        new SummaryKey(order.isBuy() ? "buy" : "sell",
                                order.getInvestmentCcy(),
                                order.getCounterCcy())));

        System.out.printf("%-7s %-7s %-7s %4s %7s%n", "TYPE", "INV", "CTR", "COUNT", "AVERAGE");
        System.out.println("====================================");

        groupedOrders.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    SummaryKey key = entry.getKey();
                    List<Order> orderList = entry.getValue();

                    double avgLimit = orderList.stream()
                            .mapToDouble(Order::getLimit)
                            .average()
                            .orElse(0.0);

                    System.out.printf("%-7s %-7s %-7s %3d %7.2f%n",
                            key.type, key.investmentCcy, key.counterCcy,
                            orderList.size(), avgLimit);
                });
    }
    /**
     * Record representing a key for grouping orders by type and currency pair.
     */

    private record SummaryKey(String type, String investmentCcy, String counterCcy) implements Comparable<SummaryKey> {

        /**
         * Compares this key with another for sorting purposes.
         *
         * @param other The other key to compare with
         * @return negative if this key is less than other, positive if greater, zero if equal
         */

        @Override
        public int compareTo(SummaryKey other) {
            if (other == null) {
                return 1;
            }

            int result = this.investmentCcy.compareTo(other.investmentCcy);
            if (result != 0) {
                return result;
            }

            result = this.counterCcy.compareTo(other.counterCcy);
            if (result != 0) {
                return result;
            }
            return this.type.compareTo(other.type);
        }
    }
}