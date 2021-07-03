package com.foxminded.booking.repository;

import com.foxminded.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByTourId(Long tourId);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByGuideId(Long guideId);
}
