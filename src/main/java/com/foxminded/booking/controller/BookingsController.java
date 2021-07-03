package com.foxminded.booking.controller;

import com.foxminded.booking.model.Booking;
import com.foxminded.booking.model.Guide;
import com.foxminded.booking.model.Tour;
import com.foxminded.booking.model.User;
import com.foxminded.booking.model.dto.BookingDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.service.BookingService;
import com.foxminded.booking.service.GuideService;
import com.foxminded.booking.service.UserService;
import com.foxminded.booking.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.foxminded.booking.security.SecurityConstants.*;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Bookings Controller", description = "Working with bookings")
public class BookingsController {
    private final BookingService bookingService;
    private final UserService userService;
    private final GuideService guideService;

    public BookingsController(BookingService bookingService, UserService userService, GuideService guideService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.guideService = guideService;
    }

    @PreAuthorize(isAuthenticated)
    @GetMapping
    @Operation(summary = "Get all bookings", security = @SecurityRequirement(name = "bearerAuth"))
    public List<BookingDto> getBookings(@Parameter(description = "guide id")
                                        @RequestParam(name = "guideId") Optional<Long> guideId) {
        Stream<Booking> bookings = fetchBookings(guideId);

        return bookings
                .map(Booking::toBookingDto)
                .collect(Collectors.toList());
    }

    private Stream<Booking> fetchBookings(Optional<Long> guideId) {
        Stream<Booking> bookings;
        if (SecurityUtils.userHasRole(ROLE_ADMIN)) {
            if (guideId.isPresent()) {
                bookings = bookingService.findByGuideId(guideId.get()).stream();
            } else {
                bookings = bookingService.findAll().stream();
            }
        } else {
            User user = userService
                    .findByUsername(SecurityUtils.currentUserName())
                    .orElseThrow(() -> new NotFoundException("user not found", HttpStatus.NOT_FOUND));
            bookings = bookingService.findByUserId(user.getId()).stream();
            if (guideId.isPresent()) {
                bookings = bookings.filter(it -> it.getGuide() != null && guideId.get().equals(it.getGuide().getId()));
            }
        }
        return bookings;
    }

    @PreAuthorize(isAuthenticated)
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by id", security = @SecurityRequirement(name = "bearerAuth"))
    public BookingDto getBookingById(@Parameter(description = "guide id", required = true)
                                     @PathVariable("id") long id) {
        return bookingService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("booking not found", HttpStatus.NOT_FOUND))
                .toBookingDto();
    }

    @PreAuthorize(isUser)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create booking", security = @SecurityRequirement(name = "bearerAuth"))
    public BookingDto createBooking(@RequestBody(
            description = "Tour for booking",
            required = true,
            content = @Content(schema = @Schema(implementation = Tour.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid Tour tour) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new NotFoundException("user not found", HttpStatus.NOT_FOUND));
        List<Guide> guides = guideService.findAll();
        Random random = new Random();
        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setUser(user);
        booking.setGuide(guides.get(random.nextInt(guides.size())));
        return bookingService.save(booking).toBookingDto();
    }

    @PreAuthorize(isAdmin)
    @PutMapping("/{id}")
    @Operation(summary = "Edit booking by id", security = @SecurityRequirement(name = "bearerAuth"))
    public BookingDto editBooking(@Parameter(description = "guide id", required = true)
                                  @PathVariable("id") long id,
                                  @RequestBody(
                                          description = "Booking to edit",
                                          content = @Content(schema = @Schema(implementation = Booking.class))
                                  ) @org.springframework.web.bind.annotation.RequestBody @Valid Booking actual) {
        return bookingService
                .findById(id)
                .map(booking -> {
                    booking.setTour(actual.getTour());
                    booking.setUser(actual.getUser());
                    booking.setGuide(actual.getGuide());
                    return bookingService.save(booking);
                })
                .orElseThrow(() -> new NotFoundException("booking not found", HttpStatus.NOT_FOUND))
                .toBookingDto();
    }

    @PreAuthorize(isAuthenticated)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete booking by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteBooking(@Parameter(description = "guide id", required = true)
                              @PathVariable("id") long id) {
        bookingService.deleteById(id);
    }
}
