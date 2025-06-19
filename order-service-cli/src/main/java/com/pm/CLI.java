package com.pm;

import com.pm.commands.*;
import com.pm.service.HttpService;
import com.pm.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class CLI {
    private final HttpService httpService = new HttpService("http://localhost:8888");

    private final Map<String, Command> commands;
    private final BufferedReader reader;
    private volatile boolean running = true;

    public CLI(OrderService orderService) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.commands = initializeCommands(orderService);
    }

    private Map<String, Command> initializeCommands(OrderService orderService) {
        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("new", new NewOrderCommand(orderService));
        commandMap.put("cancel", new CancelCommand(orderService));
        commandMap.put("rates", new RatesCommand(orderService));
        commandMap.put("orders", new OrdersCommand(orderService));
        commandMap.put("summary", new SummaryCommand(orderService));
        commandMap.put("help", new HelpCommand());
        commandMap.put("exit", new ExitCommand(() -> running = false));
        return commandMap;
    }

    public void start() throws Exception {
        System.out.println("FX OrderBook CLI Application");
        System.out.println("Type 'help' for available commands or 'exit' to quit.");

        // Test connection to the service
        try {
            httpService.getExchangeRates();
            System.out.println("Successfully connected to FX service");
        } catch (Exception e) {
            System.err.println("\nError: Could not connect to the Order Service!");
            System.err.println("Please ensure the Order Service is running on http://localhost:8888");
            System.err.println("The application cannot function without the Order Service.\n");
            running = false;
            return;
        }

        while (running) {
            try {
                System.out.print("> ");
                String input = reader.readLine();

                if (input == null) {
                    break;
                }

                processCommand(input.trim());
            } catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
                break;
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error closing input reader: " + e.getMessage());
        }
    }

    private void processCommand(String input) {
        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.split("\\s+");
        String commandName = parts[0].toLowerCase();

        Command command = commands.get(commandName);
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            System.out.println("Type 'help' for available commands.");
            return;
        }

        try {
            command.execute(parts);
        } catch (Exception e) {
            System.err.println("Command failed: " + e.getMessage());
        }
    }
}