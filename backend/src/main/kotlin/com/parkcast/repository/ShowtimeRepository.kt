// ShowtimeRepository.kt
package com.parkcast.repository

import com.parkcast.model.Showtime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShowtimeRepository : JpaRepository<Showtime, Int>{
    // Find all showtimes for a specific theatre
    fun findByTheatreId(theatreId: Int): List<Showtime>
}