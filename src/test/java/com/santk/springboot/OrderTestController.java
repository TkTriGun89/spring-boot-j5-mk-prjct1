package com.santk.springboot;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.santk.springboot.controller.OrderController;
import com.santk.springboot.model.OrderEntity;
import com.santk.springboot.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
public class OrderTestController {
    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        OrderEntity orderTestData = new OrderEntity(1, "Store Orders",
                "Bulk Store Orders Are Placed Every Week", true);
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderTestData)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void testReturnOrderById() throws Exception {
        long id = 1L;
        OrderEntity orderTestData = new OrderEntity(id, "findById(id) Test", "Get Order By Id Description", true);
        when(orderRepository.findById(id)).thenReturn(Optional.of(orderTestData));
        mockMvc.perform(get("/api/order/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(orderTestData.getTitle()))
                .andExpect(jsonPath("$.description").value(orderTestData.getDescription()))
                .andExpect(jsonPath("$.published").value(orderTestData.isPublished()))
                .andDo(print());
    }

    @Test
    void testReturnListOfOrders() throws Exception {
        List<OrderEntity> ordersList = new ArrayList<>(
                Arrays.asList(new OrderEntity(1, "Consumer Order 1", "Bananas 1", true),
                        new OrderEntity(2, "Consumer Order 2", "Tomatoes 2", false),
                        new OrderEntity(3, "Consumer Order 3", "Deli 3", true)));
        when(orderRepository.findAll()).thenReturn(ordersList);
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(ordersList.size()))
                .andDo(print());
    }

    @Test
    void testReturnListOfOrdersWithFilter() throws Exception {
        List<OrderEntity> orderList = new ArrayList<>(
                Arrays.asList(new OrderEntity(1, "Store Order Banana 1", "Store Order Banana 1", true),
                        new OrderEntity(3, "Store Order Cucumber 2", "Store Order Cucumber 2", true)));
        String title = "Store Order Banana 1";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", title);
        when(orderRepository.findByTitleContaining(title)).thenReturn(orderList);
        mockMvc.perform(get("/api/orders").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(orderList.size()))
                .andDo(print());
    }

    @Test
    void testReturnOrderNotFound() throws Exception {
        long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/order/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testReturnNoContentWhenFilter() throws Exception {
        String title = "Store";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", title);
        List<OrderEntity> orders = Collections.emptyList();
        when(orderRepository.findByTitleContaining(title)).thenReturn(orders);
        mockMvc.perform(get("/api/orders").params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testUpdatedOrder() throws Exception {
        long id = 1L;
        OrderEntity order = new OrderEntity(id, "Store Order Fish 1", "Mackerels", false);
        OrderEntity updatedOrder = new OrderEntity(id, "Store Order Fish 1 Updated", "Mackerels Updated", true);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(updatedOrder);
        mockMvc.perform(put("/api/orders/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedOrder.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedOrder.getDescription()))
                .andExpect(jsonPath("$.published").value(updatedOrder.isPublished()))
                .andDo(print());
    }

    @Test
    void testUpdatedOrderNotFound() throws Exception {
        long id = 1L;
        OrderEntity updatedOrder = new OrderEntity(id, "Store Order Fish 2 Updated", "Pompano Updated", true);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(updatedOrder);
        mockMvc.perform(put("/api/order/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testDeleteOrder() throws Exception {
        long id = 1L;
        doNothing().when(orderRepository).deleteById(id);
        mockMvc.perform(delete("/api/orders/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}