package com.pm.commands;

import com.pm.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for canceling existing orders in the system.
 * This command requires an order ID as an argument and attempts to cancel the corresponding order.
 */
@RequiredArgsConstructor
@Slf4j
public class CancelCommand implements Command {
    /** Service for accessing order-related operations */
    private final OrderService orderService;

    /**
     * Executes the cancel command with the provided order ID.
     *
     * @param args Command arguments where args[1] should contain the order ID
     * @throws IllegalArgumentException if the argument count is incorrect or if the cancellation fails
     * @throws Exception if an unexpected error occurs during cancellation
     */
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: cancel <ID>");
        }

        try {
            String orderId = args[1];
            boolean cancelled = orderService.cancelOrder(orderId);

            if (cancelled) {
                System.out.println("Order " + orderId + " cancelled successfully");
            } else {
                System.out.println("Order " + orderId + " not found");
            }
        } catch (Exception e) {
            log.error("Failed to cancel order", e);
            throw new IllegalArgumentException("Failed to cancel order: " + e.getMessage());
        }
    }
}