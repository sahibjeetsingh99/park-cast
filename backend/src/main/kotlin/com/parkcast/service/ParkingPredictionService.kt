// backend/src/main/kotlin/com/parkcast/service/ParkingPredictionService.kt
package com.parkcast.service

import com.parkcast.repository.BookingRepository
import com.parkcast.repository.ShowtimeRepository
import org.springframework.stereotype.Service

// Data class to hold prediction result
data class ParkingPrediction(
    val showtimeId: Int,
    val movieName: String,
    val theatreName: String,
    val lotCapacity: Int,
    val ticketsSold: Int,
    val estimatedCars: Int,
    val occupancyPercent: Int,
    val status: String,
    val recommendation: String
)

@Service
class ParkingPredictionService(
    private val showtimeRepo: ShowtimeRepository,
    private val bookingRepo: BookingRepository
) {

    // Average people per car — real world statistic
    private val avgPeoplePerCar = 2.1

    fun predictOccupancy(showtimeId: Int): ParkingPrediction {

        // Step 1 — find the showtime
        val showtime = showtimeRepo.findById(showtimeId)
            .orElseThrow { RuntimeException("Showtime not found: $showtimeId") }

        // Step 2 — count total tickets sold for this showtime
        val ticketsSold = bookingRepo.countTotalSeatsByShowtime(showtimeId)

        // Step 3 — estimate how many cars will show up
        val estimatedCars = (ticketsSold / avgPeoplePerCar).toInt()

        // Step 4 — get lot capacity from theatre
        val lotCapacity = showtime.theatre.lotCapacity

        // Step 5 — calculate occupancy percentage
        val occupancyPercent = ((estimatedCars.toFloat() / lotCapacity) * 100)
            .toInt()
            .coerceIn(0, 100)  // clamp between 0-100

        // Step 6 — determine status and recommendation
        val (status, recommendation) = when {
            occupancyPercent >= 90 ->
                "FULL" to "Parking will be very limited. Arrive 45+ mins early or consider Uber/transit."

            occupancyPercent >= 70 ->
                "BUSY" to "Parking will be busy. We recommend arriving 30 mins early."

            occupancyPercent >= 40 ->
                "MODERATE" to "Parking should be available. Arriving 15 mins early is sufficient."

            else ->
                "AVAILABLE" to "Plenty of parking available. Normal arrival time is fine."
        }

        return ParkingPrediction(
            showtimeId = showtimeId,
            movieName = showtime.movieName,
            theatreName = showtime.theatre.name,
            lotCapacity = lotCapacity,
            ticketsSold = ticketsSold,
            estimatedCars = estimatedCars,
            occupancyPercent = occupancyPercent,
            status = status,
            recommendation = recommendation
        )
    }

    // Get predictions for all showtimes at a theatre
    fun predictAllShowtimes(theatreId: Int): List<ParkingPrediction> {
        return showtimeRepo.findByTheatreId(theatreId)
            .map { predictOccupancy(it.id) }
    }
}