// BookingRepository.kt
package com.parkcast.repository

import com.parkcast.model.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query

@Repository
interface BookingRepository : JpaRepository<Booking, Int> {

    // Custom query — count total seats booked for a showtime
    // this is what our parking prediction algorithm needs
    @Query("SELECT COALESCE(SUM(b.seats), 0) FROM Booking b WHERE b.showtime.id = :showtimeId")
    fun countTotalSeatsByShowtime(showtimeId: Int): Int
}