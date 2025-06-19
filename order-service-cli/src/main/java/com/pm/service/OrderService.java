package com.pm.service;

import com.pm.models.FXRate;
import com.pm.models.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order) throws Exception;

    boolean cancelOrder(String orderId) throws Exception;

    List<Order> getAllOrders() throws Exception;

    List<FXRate> getExchangeRates() throws Exception;
}
