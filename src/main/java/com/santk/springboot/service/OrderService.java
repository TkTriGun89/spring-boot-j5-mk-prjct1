package com.santk.springboot.service;

import com.santk.springboot.model.OrderEntity;
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
