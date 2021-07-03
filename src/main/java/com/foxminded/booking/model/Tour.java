package com.foxminded.booking.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.foxminded.booking.model.dto.TourDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tours")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "price")
    private Long price;
    @NotEmpty
    @Column(name = "difficulty")
    private String difficulty;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "start")
    private LocalDate start;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "finish")
    private LocalDate finish;
    @OneToMany(mappedBy = "tour")
    private List<Booking> bookings = new ArrayList<>();

    public Tour() {
    }

    public Tour(@NotNull Long price, @NotEmpty String difficulty, LocalDate start, LocalDate finish) {
        this.price = price;
        this.difficulty = difficulty;
        this.start = start;
        this.finish = finish;
    }

    public Tour(Long id, @NotNull Long price, @NotEmpty String difficulty, LocalDate start, LocalDate finish) {
        this.id = id;
        this.price = price;
        this.difficulty = difficulty;
        this.start = start;
        this.finish = finish;
    }

    public TourDto toTourDto() {
        TourDto dto = new TourDto();
        dto.setId(id);
        dto.setPrice(price);
        dto.setDifficulty(difficulty);
        dto.setStart(start);
        dto.setFinish(finish);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
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
        Tour tour = (Tour) o;
        return price.equals(tour.price) &&
                difficulty.equals(tour.difficulty) &&
                start.equals(tour.start) &&
                finish.equals(tour.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, difficulty, start, finish);
    }
}
