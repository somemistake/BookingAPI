package com.foxminded.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.User;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.dto.UserDto;
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
class UsersControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<UserDto> expected = new ArrayList<>();
        when(userService.findAll()).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<UserDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<UserDto>>() {
        });
        assertEquals(expected, actual);

    }

    @Test
    void shouldGetUserById() throws Exception {
        User expected = new User(1l, "a", "a", "a", "a",
                new Role(1l, "a"));
        when(userService.findById(1l)).thenReturn(Optional.of(expected));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(expected.toUserDto(), actual);
    }

    @Test
    void shouldThrowUserNotFoundExceptionInGetMethod() throws Exception {
        when(userService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("user not found", actual.getMessage());
    }

    @Test
    void shouldCreateValidUser() throws Exception {
        String roleJson = mapper.writeValueAsString(new User("a", "a", "a", "a",
                new Role(1l, "a")));
        User expected = new User(1l, "a", "a", "a", "a",
                new Role(1l, "a"));
        when(userService.save(any(User.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isCreated())
                .andReturn();
        UserDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(expected.toUserDto(), actual);
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPostMethod() throws Exception {
        String userJson = mapper.writeValueAsString(new User());
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldEditUserById() throws Exception {
        String roleJson = mapper.writeValueAsString(new User("a", "a", "a", "a",
                new Role(1l, "a")));
        User expected = new User(1l, "a", "a", "a", "a",
                new Role(1l, "a"));
        when(userService.findById(1l)).thenReturn(Optional.of(new User(1l, "b", "a", "a", "a",
                new Role(1l, "a"))));
        when(userService.save(any(User.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(expected.toUserDto(), actual);
    }

    @Test
    void shouldThrowUserNotFoundExceptionInPutMethod() throws Exception {
        String userJson = mapper.writeValueAsString(new User("a", "a", "a", "a",
                new Role(1l, "a")));
        when(userService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("user not found", actual.getMessage());
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPutMethod() throws Exception {
        String userJson = mapper.writeValueAsString(new User());
        when(userService.findById(1l)).thenReturn(Optional.of(new User()));
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteById(1l);
    }
}