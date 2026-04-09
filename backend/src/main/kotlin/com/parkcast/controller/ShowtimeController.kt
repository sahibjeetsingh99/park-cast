// backend/src/main/kotlin/com/parkcast/controller/ShowtimeController.kt
package com.parkcast.controller

import com.parkcast.service.ParkingPredictionService
import com.parkcast.repository.ShowtimeRepository
import com.parkcast.repository.TheatreRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ShowtimeController(
    private val predictionService: ParkingPredictionService,
    private val showtimeRepo: ShowtimeRepository,
    private val theatreRepo: TheatreRepository
) {

    // GET /api/theatres
    // Returns all theatres
    @GetMapping("/theatres")
    fun getAllTheatres(): ResponseEntity<Any> {
        val theatres = theatreRepo.findAll()
        return ResponseEntity.ok(theatres)
    }

    // GET /api/showtimes
    // Returns all showtimes
    @GetMapping("/showtimes")
    fun getAllShowtimes(): ResponseEntity<Any> {
        val showtimes = showtimeRepo.findAll()
        return ResponseEntity.ok(showtimes)
    }

    // GET /api/showtimes/{id}/parking
    // Returns parking prediction for a specific showtime
    @GetMapping("/showtimes/{id}/parking")
    fun getParkingPrediction(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            val prediction = predictionService.predictOccupancy(id)
            ResponseEntity.ok(prediction)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    // GET /api/theatres/{id}/parking
    // Returns parking predictions for all showtimes at a theatre
    @GetMapping("/theatres/{id}/parking")
    fun getTheatreParkingPredictions(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            val predictions = predictionService.predictAllShowtimes(id)
            ResponseEntity.ok(predictions)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }
}