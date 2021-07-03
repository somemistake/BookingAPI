package com.foxminded.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.Tour;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.dto.TourDto;
import com.foxminded.booking.service.TourService;
import com.foxminded.booking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(roles = {"ADMIN", "USER"})
class ToursControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private TourService tourService;

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void shouldGetAllTours() throws Exception {
        List<TourDto> expected = new ArrayList<>();
        when(tourService.findAll()).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/tours").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<TourDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<TourDto>>() {
        });
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTourById() throws Exception {
        Tour expected = new Tour(1l, 3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2));
        when(tourService.findById(1l)).thenReturn(Optional.of(expected));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/tours/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TourDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), TourDto.class);
        assertEquals(expected.toTourDto(), actual);
    }

    @Test
    void shouldThrowTourNotFoundExceptionInGetMethod() throws Exception {
        when(tourService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/tours/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("tour not found", actual.getMessage());
    }

    @Test
    void shouldCreateValidTour() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour(3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2)));
        Tour expected = new Tour(1l, 3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2));
        when(tourService.save(any(Tour.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/tours").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isCreated())
                .andReturn();
        TourDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), TourDto.class);
        assertEquals(expected.toTourDto(), actual);
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPostMethod() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour());
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/tours").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldEditTourById() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour(3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2)));
        Tour expected = new Tour(1l, 3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2));
        when(tourService.findById(1l)).thenReturn(Optional.of(new Tour(1l, 3l, "b", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2))));
        when(tourService.save(any(Tour.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/tours/1").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isOk())
                .andReturn();
        TourDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), TourDto.class);
        assertEquals(expected.toTourDto(), actual);
    }

    @Test
    void shouldThrowTourNotFoundExceptionInPutMethod() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour(3l, "a", LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2)));
        when(tourService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/tours/1").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("tour not found", actual.getMessage());
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPutMethod() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour());
        when(tourService.findById(1l)).thenReturn(Optional.of(new Tour()));
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/tours/1").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldDeleteTourById() throws Exception {
        mockMvc.perform(delete("/api/v1/tours/1"))
                .andExpect(status().isOk());
        verify(tourService, times(1)).deleteById(1l);
    }
}