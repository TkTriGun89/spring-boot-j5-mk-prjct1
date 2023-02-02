package com.santk.springboot.controller;

import java.util.List;
import java.util.Optional;

import com.santk.springboot.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.santk.springboot.model.OrderEntity;

@RestController
@RequestMapping("/api")
public class OrderController {

    private static final Logger LOGGER = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrderService orderservice;

    /**
     * Create New Orders
     *
     * @param orderCrt - order details
     * @return
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity orderCrt) {
        try {
            LOGGER.info("createOrder controller");
            return new ResponseEntity<>(orderservice.createOrder(orderCrt), HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get Orders based on title
     *
     * @param title get orders for the particular title
     * @return
     */
    @GetMapping("/getorders")
    public ResponseEntity<List<OrderEntity>> getAllOrders(@RequestParam(required = false) String title) {
        try {
            LOGGER.info("getAllOrders controller");
            List<OrderEntity> ordersLst = orderservice.getAllOrders(title);
            if (ordersLst.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(ordersLst, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get Particular Order by Id
     *
     * @param id - order number
     * @return
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable("id") long id) {
        try {
            LOGGER.info("getOrderById controller");
            Optional<OrderEntity> orderData = orderservice.getOrderById(id);
            if (orderData.isPresent()) {
                return new ResponseEntity<>(orderData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get published orders
     *
     * @return
     */
    @GetMapping("/orders/published")
    public ResponseEntity<List<OrderEntity>> findOrderByPublished() {
        boolean isPublished = true;
        try {
            LOGGER.info("findOrderByPublished controller");
            List<OrderEntity> orders = orderservice.findOrderByPublished(isPublished);
            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update order by orderNumber
     *
     * @param id
     * @param order
     * @return
     */
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderEntity> updateOrder(@PathVariable("id") long id,
                                                   @RequestBody OrderEntity order) {
        try {
            LOGGER.info("updateOrder controller");
            Optional<OrderEntity> orderDataById = orderservice.getOrderById(id);
            if (orderDataById.isPresent()) {

                OrderEntity updateOrder = orderDataById.get();
                updateOrder.setTitle(order.getTitle());
                updateOrder.setDescription(order.getDescription());
                updateOrder.setPublished(order.isPublished());

                return new ResponseEntity<>(orderservice.updateOrder(updateOrder), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete order by orderNumber
     *
     * @param id - ordernumber
     * @return
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrders(@PathVariable("id") long id) {
        try {
            LOGGER.info("deleteOrders controller");
            orderservice.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
