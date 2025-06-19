package com.pm.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pm.models.FXRate;
import com.pm.models.Order;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class HttpService implements OrderService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public HttpService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public Order createOrder(Order order) throws Exception {
        String json = objectMapper.writeValueAsString(order);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/createOrder"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to create order. Status: " + response.statusCode() + ", Body: " + response.body());
        }

        return objectMapper.readValue(response.body(), Order.class);
    }

    @Override
    public boolean cancelOrder(String orderId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/cancelOrder"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(orderId))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to cancel order. Status: " + response.statusCode() + ", Body: " + response.body());
        }

        return Boolean.parseBoolean(response.body());
    }

    @Override
    public List<Order> getAllOrders() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/retrieveOrders"))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get orders. Status: " + response.statusCode() + ", Body: " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<Order>>() {
        });
    }

    @Override
    public List<FXRate> getExchangeRates() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rateSnapshot"))
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to get exchange rates. Status: " + response.statusCode() + ", Body: " + response.body());
        }

        return objectMapper.readValue(response.body(), new TypeReference<List<FXRate>>() {
        });
    }
}
