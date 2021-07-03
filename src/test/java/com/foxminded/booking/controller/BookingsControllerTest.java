package com.foxminded.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.booking.model.*;
import com.foxminded.booking.model.dto.BookingDto;
import com.foxminded.booking.model.dto.ErrorDto;
import com.foxminded.booking.service.BookingService;
import com.foxminded.booking.service.GuideService;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.foxminded.booking.security.SecurityConstants.ROLE_USER;
import static java.util.stream.Collectors.toList;
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
class BookingsControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private GuideService guideService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void shouldGetAllBookings() throws Exception {
        List<BookingDto> expected = new ArrayList<>();
        when(bookingService.findAll()).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<BookingDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BookingDto>>() {
        });
        assertEquals(expected, actual);
        verify(bookingService, times(1)).findAll();
    }

    @WithMockUser(username = "test")
    @Test
    void shouldGetAllBookingsByUserId() throws Exception {
        User user = new User(1l, "Alfred", "Einstain", "test", "123", new Role(ROLE_USER));

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        Tour tour = new Tour(1l, "easy", LocalDate.now(), LocalDate.now().plus(60, ChronoUnit.DAYS));
        Guide john = new Guide(1l, "John");
        Guide peter = new Guide(2l, "Peter");
        Booking booking1 = new Booking(tour, user, john);
        Booking booking2 = new Booking(tour, user, peter);
        List<Booking> allBookings = Arrays.asList(booking1, booking2);
        when(bookingService.findByUserId(1l)).thenReturn(allBookings);

        List<BookingDto> expected = allBookings.stream().map(it -> it.toBookingDto()).collect(toList());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        List<BookingDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BookingDto>>() {
        });
        assertEquals(expected, actual);
        verify(userService, times(1)).findByUsername("test");
        verify(bookingService, times(1)).findByUserId(1l);
    }

    @WithMockUser(username = "test", roles = {"ADMIN"})
    @Test
    void shouldGetAllBookingsByGuideId() throws Exception {
        User user = new User();
        user.setId(1l);
        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        List<BookingDto> expected = new ArrayList<>();
        when(bookingService.findByGuideId(1l)).thenReturn(new ArrayList<>());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings?guideId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
        List<BookingDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BookingDto>>() {
        });
        assertEquals(expected, actual);
        verify(bookingService, times(1)).findByGuideId(1l);
    }

    @WithMockUser(username = "test")
    @Test
    void shouldGetAllBookingsByGuideIdForUser() throws Exception {
        User user = new User(1l, "Alfred", "Einstain", "test", "123", new Role(ROLE_USER));

        when(userService.findByUsername("test")).thenReturn(Optional.of(user));

        Tour tour = new Tour(1l, "easy", LocalDate.now(), LocalDate.now().plus(60, ChronoUnit.DAYS));
        Guide john = new Guide(1l, "John");
        Guide peter = new Guide(2l, "Peter");
        Booking booking1 = new Booking(tour, user, john);
        Booking booking2 = new Booking(tour, user, peter);
        List<Booking> allBookings = Arrays.asList(booking1, booking2);
        when(bookingService.findByUserId(1l)).thenReturn(allBookings);

        List<BookingDto> expected = Arrays.asList(booking1.toBookingDto());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings?guideId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<BookingDto> actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BookingDto>>() {
        });
        assertEquals(expected, actual);
        verify(bookingService, times(1)).findByUserId(1l);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void shouldGetBookingById() throws Exception {
        Booking expected = new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        );
        when(bookingService.findById(1l)).thenReturn(Optional.of(expected));
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookingDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class);
        assertEquals(expected.toBookingDto(), actual);
    }

    @WithMockUser
    @Test
    void shouldThrowBookingNotFoundExceptionInGetMethod() throws Exception {
        when(bookingService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/bookings/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("booking not found", actual.getMessage());
    }

    @WithMockUser(username = "test")
    @Test
    void shouldCreateValidBooking() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                LocalDate.of(2021, 4, 2)));
        Booking expected = new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "test", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        );
        when(userService.findByUsername("test")).thenReturn(Optional.of(new User(2l, "Lewis", "Scott", "test", "user", new Role("ROLE_USER"))));
        when(guideService.findAll()).thenReturn(new ArrayList<Guide>() {{
            add(new Guide(1l, "Chris"));
        }});
        when(bookingService.save(any(Booking.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isCreated())
                .andReturn();
        BookingDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class);
        assertEquals(expected.toBookingDto(), actual);
        verify(userService, times(1)).findByUsername("test");
        verify(guideService, times(1)).findAll();
        verify(bookingService, times(1)).save(any(Booking.class));
    }

    @WithMockUser
    @Test
    void shouldHandleArgumentNotValidExceptionInPostMethod() throws Exception {
        String tourJson = mapper.writeValueAsString(new Tour());
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(tourJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
        assertTrue(actual.getMessage().startsWith("not valid due to validation error:"));
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void shouldEditBookingById() throws Exception {
        String bookingJson = mapper.writeValueAsString(new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "test", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        ));
        Booking expected = new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "test", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        );
        when(bookingService.findById(1l)).thenReturn(Optional.of(new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "lewis", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        )));
        when(bookingService.save(any(Booking.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/bookings/1").contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson))
                .andExpect(status().isOk())
                .andReturn();
        BookingDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class);
        assertEquals(expected.toBookingDto(), actual);
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void shouldThrowGuideNotFoundExceptionInPutMethod() throws Exception {
        String bookingJson = mapper.writeValueAsString(new Booking(
                1l,
                new Tour(1l, 3l, "easy", LocalDate.of(2021, 4, 1),
                        LocalDate.of(2021, 4, 2)),
                new User(2l, "Lewis", "Scott", "test", "user", new Role("ROLE_USER")),
                new Guide(1l, "Chris")
        ));
        when(bookingService.findById(1l)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/bookings/1").contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorDto actual = mapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatus());
        assertEquals("booking not found", actual.getMessage());
    }

    @WithMockUser(roles = {"ADMIN"})
    @Test
    void shouldDeleteGuideById() throws Exception {
        mockMvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isOk());
        verify(bookingService, times(1)).deleteById(1l);
    }
}