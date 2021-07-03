package com.foxminded.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.Guide;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.dto.GuideDto;
import com.foxminded.booking.service.GuideService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithMockUser(roles = {"ADMIN"})
class GuidesControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private GuideService guideService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void shouldGetAllGuides() throws Exception {
        List<GuideDto> expected = new ArrayList<>();
        when(guideService.findAll()).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/guides").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<GuideDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<GuideDto>>() {
        });
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetGuideById() throws Exception {
        Guide expected = new Guide(1l, "a");
        when(guideService.findById(1l)).thenReturn(Optional.of(expected));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/guides/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        GuideDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuideDto.class);
        assertEquals(expected.toGuideDto(), actual);
    }

    @Test
    void shouldThrowGuideNotFoundExceptionInGetMethod() throws Exception {
        when(guideService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/guides/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("guide not found", actual.getMessage());
    }

    @Test
    void shouldCreateValidGuide() throws Exception {
        String guideJson = mapper.writeValueAsString(new Guide("a"));
        Guide expected = new Guide(1l, "a");
        when(guideService.save(any(Guide.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/guides").contentType(MediaType.APPLICATION_JSON)
                .content(guideJson))
                .andExpect(status().isCreated())
                .andReturn();
        GuideDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuideDto.class);
        assertEquals(expected.toGuideDto(), actual);
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPostMethod() throws Exception {
        String guideJson = mapper.writeValueAsString(new Guide());
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/guides").contentType(MediaType.APPLICATION_JSON)
                .content(guideJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldEditGuideById() throws Exception {
        String guideJson = mapper.writeValueAsString(new Guide("a"));
        Guide expected = new Guide(1l, "a");
        when(guideService.findById(1l)).thenReturn(Optional.of(new Guide(1l, "b")));
        when(guideService.save(any(Guide.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/guides/1").contentType(MediaType.APPLICATION_JSON)
                .content(guideJson))
                .andExpect(status().isOk())
                .andReturn();
        GuideDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), GuideDto.class);
        assertEquals(expected.toGuideDto(), actual);
    }

    @Test
    void shouldThrowGuideNotFoundExceptionInPutMethod() throws Exception {
        String guideJson = mapper.writeValueAsString(new Guide(1l, "a"));
        when(guideService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/guides/1").contentType(MediaType.APPLICATION_JSON)
                .content(guideJson))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("guide not found", actual.getMessage());
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPutMethod() throws Exception {
        String guideJson = mapper.writeValueAsString(new Guide());
        when(guideService.findById(1l)).thenReturn(Optional.of(new Guide()));
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/guides/1").contentType(MediaType.APPLICATION_JSON)
                .content(guideJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldDeleteGuideById() throws Exception {
        mockMvc.perform(delete("/api/v1/guides/1"))
                .andExpect(status().isOk());
        verify(guideService, times(1)).deleteById(1l);
    }
}