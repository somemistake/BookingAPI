package com.foxminded.booking.controller;

import com.foxminded.booking.model.Tour;
import com.foxminded.booking.model.dto.TourDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.foxminded.booking.security.SecurityConstants.isAdmin;
import static com.foxminded.booking.security.SecurityConstants.isAuthenticated;

@RestController
@RequestMapping("/api/v1/tours")
@Tag(name = "Tours Controller", description = "Working with tours")
public class ToursController {
    private final TourService tourService;

    public ToursController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    @PreAuthorize(isAuthenticated)
    @Operation(summary = "Get all tours", security = @SecurityRequirement(name = "bearerAuth"))
    public List<TourDto> getTours() {
        return tourService
                .findAll()
                .stream()
                .map(Tour::toTourDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize(isAdmin)
    @Operation(summary = "Get tour by id", security = @SecurityRequirement(name = "bearerAuth"))
    public TourDto getTourById(@Parameter(description = "tour id", required = true)
                               @PathVariable("id") long id) {
        return tourService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("tour not found", HttpStatus.NOT_FOUND))
                .toTourDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(isAdmin)
    @Operation(summary = "Create tour", security = @SecurityRequirement(name = "bearerAuth"))
    public TourDto createTour(@RequestBody(
            description = "Tour to add",
            required = true,
            content = @Content(schema = @Schema(implementation = Tour.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid Tour tour) {
        return tourService.save(tour).toTourDto();
    }

    @PutMapping("/{id}")
    @PreAuthorize(isAuthenticated)
    @Operation(summary = "Edit tour by id", security = @SecurityRequirement(name = "bearerAuth"))
    public TourDto editTour(@Parameter(description = "tour id", required = true)
                            @PathVariable("id") long id,
                            @RequestBody(
                                    description = "Tour to edit",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = Tour.class))
                            ) @org.springframework.web.bind.annotation.RequestBody @Valid Tour actual) {
        return tourService.findById(id)
                .map(tour -> {
                    tour.setPrice(actual.getPrice());
                    tour.setDifficulty(actual.getDifficulty());
                    tour.setStart(actual.getStart());
                    tour.setFinish(actual.getFinish());
                    tour.setBookings(actual.getBookings());
                    return tourService.save(tour);
                })
                .orElseThrow(() -> new NotFoundException("tour not found", HttpStatus.NOT_FOUND))
                .toTourDto();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(isAdmin)
    @Operation(summary = "Delete tour by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteTour(@Parameter(description = "tour id", required = true)
                           @PathVariable("id") long id) {
        tourService.deleteById(id);
    }
}
