package com.foxminded.booking.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Booking Entity")
public class BookingDto {
    @Schema(description = "Identifier")
    private Long id;
    @Schema(description = "Tour Entity")
    private TourDto tourDto;
    @Schema(description = "User Entity")
    private UserDto userDto;
    @Schema(description = "Guide Entity")
    private GuideDto guideDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TourDto getTourDto() {
        return tourDto;
    }

    public void setTourDto(TourDto tourDto) {
        this.tourDto = tourDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public GuideDto getGuideDto() {
        return guideDto;
    }

    public void setGuideDto(GuideDto guideDto) {
        this.guideDto = guideDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(tourDto, that.tourDto) &&
                Objects.equals(userDto, that.userDto) &&
                Objects.equals(guideDto, that.guideDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourDto, userDto, guideDto);
    }
}
