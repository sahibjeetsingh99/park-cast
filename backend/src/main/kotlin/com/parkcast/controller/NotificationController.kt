// backend/src/main/kotlin/com/parkcast/controller/NotificationController.kt
package com.parkcast.controller

import com.parkcast.repository.ShowtimeRepository
import com.parkcast.repository.UserRepository
import com.parkcast.service.FCMService
import com.parkcast.service.ParkingPredictionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notify")
class NotificationController(
    private val showtimeRepo: ShowtimeRepository,
    private val userRepo: UserRepository,
    private val predictionService: ParkingPredictionService,
    private val fcmService: FCMService
) {

    // POST /api/notify/test/{showtimeId}
    // Manually triggers a parking notification for a showtime — bypasses time window.
    // Use this to test without waiting for the scheduler.
    @PostMapping("/test/{showtimeId}")
    fun testNotification(@PathVariable showtimeId: Int): ResponseEntity<Any> {
        val showtime = showtimeRepo.findById(showtimeId).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val prediction = predictionService.predictOccupancy(showtimeId)
        val emoji = if (prediction.status == "FULL") "🔴" else "🟡"

        val users = userRepo.findAll().filter { it.fcmToken != null }
        users.forEach { user ->
            fcmService.sendNotification(
                fcmToken = user.fcmToken!!,
                title = "$emoji Parking Alert — ${showtime.movieName}",
                body = prediction.recommendation
            )
        }

        return ResponseEntity.ok(mapOf(
            "sent" to users.size,
            "showtime" to showtime.movieName,
            "status" to prediction.status,
            "recommendation" to prediction.recommendation
        ))
    }
}
