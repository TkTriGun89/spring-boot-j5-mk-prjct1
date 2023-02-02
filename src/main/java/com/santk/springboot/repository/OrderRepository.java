package com.santk.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santk.springboot.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findByPublished(boolean published);

  List<OrderEntity> findByTitleContaining(String title);
}
