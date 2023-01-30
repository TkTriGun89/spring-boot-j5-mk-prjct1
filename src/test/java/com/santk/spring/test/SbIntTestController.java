package com.santk.spring.test;

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

import com.santk.spring.test.controller.SbtController;
import com.santk.spring.test.model.SbtEntityH2;
import com.santk.spring.test.repository.SbtRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SbtController.class)
public class SbIntTestController {
  @MockBean
  private SbtRepository tutorialRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldCreateTutorial() throws Exception {
    SbtEntityH2 tutorial = new SbtEntityH2(1, "Spring Boot @WebMvcTest", "Description", true);

    mockMvc.perform(post("/api/tutorials").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tutorial)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  void shouldReturnTutorial() throws Exception {
    long id = 1L;
    SbtEntityH2 tutorial = new SbtEntityH2(id, "Spring Boot @WebMvcTest", "Description", true);

    when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
    mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.title").value(tutorial.getTitle()))
        .andExpect(jsonPath("$.description").value(tutorial.getDescription()))
        .andExpect(jsonPath("$.published").value(tutorial.isPublished()))
        .andDo(print());
  }

  @Test
  void shouldReturnNotFoundTutorial() throws Exception {
    long id = 1L;

    when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
    mockMvc.perform(get("/api/tutorials/{id}", id))
         .andExpect(status().isNotFound())
         .andDo(print());
  }

  @Test
  void shouldReturnListOfTutorials() throws Exception {
    List<SbtEntityH2> tutorials = new ArrayList<>(
        Arrays.asList(new SbtEntityH2(1, "Spring Boot @WebMvcTest 1", "Description 1", true),
            new SbtEntityH2(2, "Spring Boot @WebMvcTest 2", "Description 2", true),
            new SbtEntityH2(3, "Spring Boot @WebMvcTest 3", "Description 3", true)));

    when(tutorialRepository.findAll()).thenReturn(tutorials);
    mockMvc.perform(get("/api/tutorials"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(tutorials.size()))
        .andDo(print());
  }

  @Test
  void shouldReturnListOfTutorialsWithFilter() throws Exception {
    List<SbtEntityH2> tutorials = new ArrayList<>(
        Arrays.asList(new SbtEntityH2(1, "Spring Boot @WebMvcTest", "Description 1", true),
            new SbtEntityH2(3, "Spring Boot Web MVC", "Description 3", true)));

    String title = "Boot";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("title", title);

    when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorials);
    mockMvc.perform(get("/api/tutorials").params(paramsMap))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(tutorials.size()))
        .andDo(print());
  }
  
  @Test
  void shouldReturnNoContentWhenFilter() throws Exception {
    String title = "SanTek";
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    paramsMap.add("title", title);
    
    List<SbtEntityH2> tutorials = Collections.emptyList();

    when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorials);
    mockMvc.perform(get("/api/tutorials").params(paramsMap))
        .andExpect(status().isNoContent())
        .andDo(print());
  }

  @Test
  void shouldUpdateTutorial() throws Exception {
    long id = 1L;

    SbtEntityH2 tutorial = new SbtEntityH2(id, "Spring Boot @WebMvcTest", "Description", false);
    SbtEntityH2 updatedtutorial = new SbtEntityH2(id, "Updated", "Updated", true);

    when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
    when(tutorialRepository.save(any(SbtEntityH2.class))).thenReturn(updatedtutorial);

    mockMvc.perform(put("/api/tutorials/{id}", id).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedtutorial)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(updatedtutorial.getTitle()))
        .andExpect(jsonPath("$.description").value(updatedtutorial.getDescription()))
        .andExpect(jsonPath("$.published").value(updatedtutorial.isPublished()))
        .andDo(print());
  }
  
  @Test
  void shouldReturnNotFoundUpdateTutorial() throws Exception {
    long id = 1L;

    SbtEntityH2 updatedtutorial = new SbtEntityH2(id, "Updated", "Updated", true);

    when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
    when(tutorialRepository.save(any(SbtEntityH2.class))).thenReturn(updatedtutorial);

    mockMvc.perform(put("/api/tutorials/{id}", id).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedtutorial)))
        .andExpect(status().isNotFound())
        .andDo(print());
  }
  
  @Test
  void shouldDeleteTutorial() throws Exception {
    long id = 1L;

    doNothing().when(tutorialRepository).deleteById(id);
    mockMvc.perform(delete("/api/tutorials/{id}", id))
         .andExpect(status().isNoContent())
         .andDo(print());
  }
  
  @Test
  void shouldDeleteAllTutorials() throws Exception {
    doNothing().when(tutorialRepository).deleteAll();
    mockMvc.perform(delete("/api/tutorials"))
         .andExpect(status().isNoContent())
         .andDo(print());
  }
}
