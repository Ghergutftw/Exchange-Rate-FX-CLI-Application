package com.pm.commands;

import com.pm.models.FXRate;
import com.pm.service.OrderService;

import java.util.List;

/**
 * Command implementation for displaying current exchange rates.
 * Shows bid and ask prices for all available currency pairs.
 */
public class RatesCommand implements Command {
    /** Service for accessing exchange rate operations */
    private final OrderService orderService;

    /**
     * Constructs a new RatesCommand with the specified order service.
     *
     * @param orderService The service to use for retrieving exchange rates
     */
    public RatesCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Executes the rates command, displaying current exchange rates for all currency pairs.
     *
     * @param args Command arguments (not used for this command)
     * @throws Exception If an error occurs while fetching exchange rates
     */
    @Override
    public void execute(String[] args) throws Exception {
        List<FXRate> rates = orderService.getExchangeRates();

        if (rates.isEmpty()) {
            System.out.println("No exchange rates available");
            return;
        }

        System.out.printf("%-8s %-8s %10s %10s%n", "FROM", "TO", "BID", "ASK");
        System.out.println("-------------------------------------");

        for (FXRate rate : rates) {
            System.out.printf("%-8s %-8s %10.4f %10.4f%n",
                    rate.getCcyPair().getCcy1(),
                    rate.getCcyPair().getCcy2(),
                    rate.getBid(),
                    rate.getAsk());
        }
    }
}