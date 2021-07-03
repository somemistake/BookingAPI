package com.foxminded.booking.model;

import com.foxminded.booking.model.dto.GuideDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "guides")
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "guide")
    private List<Booking> bookings = new ArrayList<>();

    public Guide() {
    }

    public Guide(@NotEmpty String name) {
        this.name = name;
    }

    public Guide(Long id, @NotEmpty String name) {
        this.id = id;
        this.name = name;
    }

    public GuideDto toGuideDto() {
        GuideDto dto = new GuideDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guide guide = (Guide) o;
        return name.equals(guide.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
