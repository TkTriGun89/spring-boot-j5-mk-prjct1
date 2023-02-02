package com.santk.springboot.service;

import com.santk.springboot.model.OrderEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderEntity createOrder(OrderEntity orderEnt);

    List<OrderEntity> getAllOrders(String title);

    Optional<OrderEntity> getOrderById(long id);

    List<OrderEntity> findOrderByPublished(boolean isPublished);

    OrderEntity updateOrder(OrderEntity updatedEmployee);

    void deleteOrder(long id);
}
