package com.santk.springboot.service;

import com.santk.springboot.model.OrderEntity;
import com.santk.springboot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class OrderServiceImpl implements OrderService {
    public OrderServiceImpl() {
    }
    @Autowired
    private OrderRepository ordersRepo;

    @Override
    public OrderEntity createOrder(OrderEntity orderEnt) {
        OrderEntity createOrder = ordersRepo.save(new OrderEntity(orderEnt.getTitle(),
                orderEnt.getDescription(), false));
        return createOrder;
    }

    @Override
    public List<OrderEntity> getAllOrders(String title) {
        List<OrderEntity> ordersLst = new ArrayList<OrderEntity>();
        if (title == null)
            ordersRepo.findAll().forEach(ordersLst::add);
        else
            ordersRepo.findByTitleContaining(title).forEach(ordersLst::add);
        return ordersLst;
    }

    @Override
    public Optional<OrderEntity> getOrderById(long id) {
        return ordersRepo.findById(id);
    }

    @Override
    public List<OrderEntity> findOrderByPublished(boolean isPublished) {
        return ordersRepo.findByPublished(isPublished);
    }

    @Override
    public OrderEntity updateOrder(OrderEntity updateOrder) {
        OrderEntity updateOrderResp = ordersRepo.save(updateOrder);
        return updateOrderResp;
    }

    @Override
    public void deleteOrder(long id) {

    }
}
