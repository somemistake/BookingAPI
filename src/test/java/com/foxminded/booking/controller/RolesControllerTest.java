package com.foxminded.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.dto.RoleDto;
import com.foxminded.booking.service.RoleService;
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
class RolesControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private RoleService roleService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void shouldGetAllRoles() throws Exception {
        List<RoleDto> expected = new ArrayList<>();
        when(roleService.findAll()).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/roles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<RoleDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<RoleDto>>() {
        });
        assertEquals(expected, actual);

    }

    @Test
    void shouldGetRoleById() throws Exception {
        Role expected = new Role(1l, "a");
        when(roleService.findById(1l)).thenReturn(Optional.of(expected));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        RoleDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), RoleDto.class);
        assertEquals(expected.toRoleDto(), actual);
    }

    @Test
    void shouldThrowRoleNotFoundExceptionInGetMethod() throws Exception {
        when(roleService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("role not found", actual.getMessage());
    }

    @Test
    void shouldCreateValidRole() throws Exception {
        String roleJson = mapper.writeValueAsString(new Role("a"));
        Role expected = new Role(1l, "a");
        when(roleService.save(any(Role.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/roles").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isCreated())
                .andReturn();
        RoleDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), RoleDto.class);
        assertEquals(expected.toRoleDto(), actual);
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPostMethod() throws Exception {
        String roleJson = mapper.writeValueAsString(new Role());
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/roles").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldEditRoleById() throws Exception {
        String roleJson = mapper.writeValueAsString(new Role("a"));
        Role expected = new Role(1l, "a");
        when(roleService.findById(1l)).thenReturn(Optional.of(new Role(1l, "b")));
        when(roleService.save(any(Role.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isOk())
                .andReturn();
        RoleDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), RoleDto.class);
        assertEquals(expected.toRoleDto(), actual);
    }

    @Test
    void shouldThrowRoleNotFoundExceptionInPutMethod() throws Exception {
        String roleJson = mapper.writeValueAsString(new Role(1l, "a"));
        when(roleService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("role not found", actual.getMessage());
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInPutMethod() throws Exception {
        String roleJson = mapper.writeValueAsString(new Role());
        when(roleService.findById(1l)).thenReturn(Optional.of(new Role()));
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON)
                .content(roleJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldDeleteRoleById() throws Exception {
        mockMvc.perform(delete("/api/v1/roles/1"))
                .andExpect(status().isOk());
        verify(roleService, times(1)).deleteById(1l);
    }
}