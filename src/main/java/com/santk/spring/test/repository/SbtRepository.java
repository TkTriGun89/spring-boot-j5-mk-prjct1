package com.santk.spring.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.santk.spring.test.model.SbtEntityH2;

public interface SbtRepository extends JpaRepository<SbtEntityH2, Long> {
  List<SbtEntityH2> findByPublished(boolean published);

  List<SbtEntityH2> findByTitleContaining(String title);
}
