package com.foxminded.booking.model;

import com.foxminded.booking.model.dto.BookingDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    public Booking() {
    }

    public Booking(Tour tour, User user, Guide guide) {
        this.tour = tour;
        this.user = user;
        this.guide = guide;
    }

    public Booking(Long id, Tour tour, User user, Guide guide) {
        this.id = id;
        this.tour = tour;
        this.user = user;
        this.guide = guide;
    }

    public BookingDto toBookingDto() {
        BookingDto dto = new BookingDto();
        dto.setId(id);
        dto.setTourDto(tour.toTourDto());
        dto.setUserDto(user.toUserDto());
        dto.setGuideDto(guide.toGuideDto());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(tour, booking.tour) &&
                Objects.equals(user, booking.user) &&
                Objects.equals(guide, booking.guide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tour, user, guide);
    }
}
