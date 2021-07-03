package com.foxminded.booking.controller;

import com.foxminded.booking.model.Guide;
import com.foxminded.booking.model.dto.GuideDto;
import com.foxminded.booking.model.exception.NotFoundException;
import com.foxminded.booking.service.GuideService;
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

@RestController
@PreAuthorize(isAdmin)
@RequestMapping("/api/v1/guides")
@Tag(name = "Guides Controller", description = "Working with guides")
public class GuidesController {
    private final GuideService guideService;

    public GuidesController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    @Operation(summary = "Get all guides", security = @SecurityRequirement(name = "bearerAuth"))
    public List<GuideDto> getGuides() {
        return guideService
                .findAll()
                .stream()
                .map(Guide::toGuideDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guide by id", security = @SecurityRequirement(name = "bearerAuth"))
    public GuideDto getGuideById(@Parameter(description = "guide id", required = true)
                                 @PathVariable("id") long id) {
        return guideService
                .findById(id)
                .orElseThrow(() -> new NotFoundException("guide not found", HttpStatus.NOT_FOUND))
                .toGuideDto();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create guide", security = @SecurityRequirement(name = "bearerAuth"))
    public GuideDto createGuide(@RequestBody(
            description = "Guide to add",
            required = true,
            content = @Content(schema = @Schema(implementation = Guide.class))
    ) @org.springframework.web.bind.annotation.RequestBody @Valid Guide guide) {
        return guideService.save(guide).toGuideDto();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit guide by id", security = @SecurityRequirement(name = "bearerAuth"))
    public GuideDto editGuide(@Parameter(description = "guide id", required = true)
                              @PathVariable("id") long id,
                              @RequestBody(
                                      description = "Guide to edit",
                                      required = true,
                                      content = @Content(schema = @Schema(implementation = Guide.class))
                              ) @org.springframework.web.bind.annotation.RequestBody @Valid Guide actual) {
        return guideService.findById(id)
                .map(guide -> {
                    guide.setName(actual.getName());
                    guide.setBookings(actual.getBookings());
                    return guideService.save(guide);
                })
                .orElseThrow(() -> new NotFoundException("guide not found", HttpStatus.NOT_FOUND))
                .toGuideDto();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete guide by id", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteGuide(@Parameter(description = "guide id", required = true)
                            @PathVariable("id") long id) {
        guideService.deleteById(id);
    }
}
