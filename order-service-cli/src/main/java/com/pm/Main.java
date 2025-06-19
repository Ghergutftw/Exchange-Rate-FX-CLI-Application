package com.pm;


import com.pm.service.HttpService;
import com.pm.service.OrderService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            OrderService orderService = new HttpService("http://localhost:8888");
            CLI cli = new CLI(orderService);
            cli.start();
        } catch (Exception e) {
            log.error("Application failed to start", e);
            System.err.println("Failed to start application: " + e.getMessage());
            System.exit(1);
        }
    }
}