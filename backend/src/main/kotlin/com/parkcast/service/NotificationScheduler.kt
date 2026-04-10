// backend/src/main/kotlin/com/parkcast/service/NotificationScheduler.kt
package com.parkcast.service

import com.parkcast.repository.ShowtimeRepository
import com.parkcast.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotificationScheduler(
    private val showtimeRepo: ShowtimeRepository,
    private val userRepo: UserRepository,
    private val predictionService: ParkingPredictionService,
    private val fcmService: FCMService
) {

    // Runs every 5 minutes to check upcoming showtimes
    @Scheduled(fixedRate = 300000)
    fun checkUpcomingShowtimes() {
        val now = LocalDateTime.now()
        val notifyWindow = now.plusMinutes(90) // 90 mins before show

        showtimeRepo.findAll()
            .filter { showtime ->
                // Find showtimes starting in next 85-95 mins
                // (5 min window to avoid sending twice)
                showtime.startTime.isAfter(now.plusMinutes(85)) &&
                showtime.startTime.isBefore(now.plusMinutes(95))
            }
            .forEach { showtime ->
                val prediction = predictionService.predictOccupancy(showtime.id)

                // Only notify if parking will be busy
                if (prediction.status == "FULL" || prediction.status == "BUSY") {
                    notifyAllUsers(
                        movieName = showtime.movieName,
                        recommendation = prediction.recommendation,
                        status = prediction.status
                    )
                }
            }
    }

    private fun notifyAllUsers(
        movieName: String,
        recommendation: String,
        status: String
    ) {
        val emoji = if (status == "FULL") "🔴" else "🟡"

        userRepo.findAll()
            .filter { it.fcmToken != null }
            .forEach { user ->
                fcmService.sendNotification(
                    fcmToken = user.fcmToken!!,
                    title = "$emoji Parking Alert — $movieName",
                    body = recommendation
                )
            }
    }
}