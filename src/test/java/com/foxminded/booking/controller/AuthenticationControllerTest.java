package com.foxminded.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.Role;
import com.foxminded.booking.model.User;
import com.foxminded.booking.model.dto.AuthorizationDto;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.model.dto.RegistrationDto;
import com.foxminded.booking.model.dto.UserDto;
import com.foxminded.booking.security.jwt.JwtTokenProvider;
import com.foxminded.booking.service.RoleService;
import com.foxminded.booking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        User expected = new User(1l, "a", "a", "a", "a",
                new Role(1l, "ROLE_USER"));
        RegistrationDto dto = new RegistrationDto();
        dto.setFirstName("a");
        dto.setLastName("a");
        dto.setUsername("a");
        dto.setPassword("a");
        String registrationDtoJson = mapper.writeValueAsString(dto);
        when(roleService.findByName("ROLE_USER")).thenReturn(Optional.of(new Role(1l, "ROLE_USER")));
        when(userService.save(any(User.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content(registrationDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);
        assertEquals(expected.toUserDto(), actual);
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInRegisterMethod() throws Exception {
        String registrationDtoJson = mapper.writeValueAsString(new RegistrationDto());
        MvcResult mvcResult = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content(registrationDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @Test
    void shouldThrowRoleNotFoundExceptionInRegisterMethod() throws Exception {
        RegistrationDto dto = new RegistrationDto();
        dto.setFirstName("a");
        dto.setLastName("a");
        dto.setUsername("a");
        dto.setPassword("a");
        String registrationDtoJson = mapper.writeValueAsString(dto);
        when(roleService.findByName("ROLE_USER")).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
                .content(registrationDtoJson))
                .andExpect(status().isNoContent())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatus());
        assertEquals("role not found", actual.getMessage());
    }

    @Test
    void shouldAuthValidUserAndGetOkResponseStatus() throws Exception {
        AuthorizationDto dto = new AuthorizationDto();
        dto.setUsername("a");
        dto.setPassword("a");
        String authDtoJson = mapper.writeValueAsString(dto);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User(1l, "a", "a", "a", "a",
                new Role(1l, "a"))));
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(authDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAuthInvalidPrincipalAndGetNoContentResponseStatus() throws Exception {
        AuthorizationDto dto = new AuthorizationDto();
        dto.setUsername("a");
        dto.setPassword("a");
        String authDtoJson = mapper.writeValueAsString(dto);
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(authDtoJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAthUserWithBadCredentialsAndGetNoContentResponseStatus() throws Exception {
        AuthorizationDto dto = new AuthorizationDto();
        dto.setUsername("a");
        dto.setPassword("a");
        String authDtoJson = mapper.writeValueAsString(dto);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User(1l, "a", "a", "a", "b",
                new Role(1l, "a"))));
        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(authDtoJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldHandleArgumentNotValidExceptionInAuthMethod() throws Exception {
        String authDtoJson = mapper.writeValueAsString(new AuthorizationDto());
        MvcResult mvcResult = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
                .content(authDtoJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }
}