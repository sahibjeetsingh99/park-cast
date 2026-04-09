// backend/src/main/kotlin/com/parkcast/DataSeeder.kt
package com.parkcast

import com.parkcast.model.Booking
import com.parkcast.model.Showtime
import com.parkcast.model.Theatre
import com.parkcast.model.User
import com.parkcast.repository.BookingRepository
import com.parkcast.repository.ShowtimeRepository
import com.parkcast.repository.TheatreRepository
import com.parkcast.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DataSeeder(
    private val theatreRepo: TheatreRepository,
    private val showtimeRepo: ShowtimeRepository,
    private val userRepo: UserRepository,
    private val bookingRepo: BookingRepository
) : CommandLineRunner {

    override fun run(vararg args: String) {

        // Only seed if database is empty
        if (theatreRepo.count() > 0) {
            println("Database already seeded — skipping")
            return
        }

        println("Seeding database...")

        // ── Theatres ──────────────────────────────────────────────
        val vaughan = theatreRepo.save(
            Theatre(
                name = "Cineplex Vaughan",
                address = "3555 Highway 7 West, Vaughan, ON",
                lotCapacity = 150
            )
        )

        val whitby = theatreRepo.save(
            Theatre(
                name = "Landmark Cinemas Whitby",
                address = "75 Consumers Dr, Whitby, ON",
                lotCapacity = 800
            )
        )

        println("✅ Theatres created")

        // ── Showtimes ─────────────────────────────────────────────
        // Busy show — nearly sold out
        val busyShow = showtimeRepo.save(
            Showtime(
                theatre = vaughan,
                movieName = "Project Hail Mary IMAX",
                startTime = LocalDateTime.now().plusHours(2),
                totalSeats = 350
            )
        )

        // Medium show — moderately busy
        val mediumShow = showtimeRepo.save(
            Showtime(
                theatre = vaughan,
                movieName = "A Minecraft Movie",
                startTime = LocalDateTime.now().plusHours(3),
                totalSeats = 200
            )
        )

        // Quiet show — plenty of parking
        val quietShow = showtimeRepo.save(
            Showtime(
                theatre = whitby,
                movieName = "The Alto Knights",
                startTime = LocalDateTime.now().plusHours(4),
                totalSeats = 150
            )
        )

        println("✅ Showtimes created")

        // ── Users ─────────────────────────────────────────────────
        val user1 = userRepo.save(
            User(email = "sahib@test.com", fcmToken = "fake_fcm_token_1")
        )
        val user2 = userRepo.save(
            User(email = "john@test.com", fcmToken = "fake_fcm_token_2")
        )
        val user3 = userRepo.save(
            User(email = "jane@test.com", fcmToken = "fake_fcm_token_3")
        )

        println("✅ Users created")

        // ── Bookings ──────────────────────────────────────────────
        // Busy show — 300 tickets sold (very full lot)
        repeat(150) { i ->
            bookingRepo.save(
                Booking(
                    showtime = busyShow,
                    user = if (i % 2 == 0) user1 else user2,
                    seats = 2
                )
            )
        }

        // Medium show — 140 tickets sold (moderately full)
        repeat(70) { i ->
            bookingRepo.save(
                Booking(
                    showtime = mediumShow,
                    user = if (i % 2 == 0) user2 else user3,
                    seats = 2
                )
            )
        }

        // Quiet show — 60 tickets sold (plenty of parking)
        repeat(30) { i ->
            bookingRepo.save(
                Booking(
                    showtime = quietShow,
                    user = user3,
                    seats = 2
                )
            )
        }

        println("✅ Bookings created")
        println("✅ Database seeding complete!")
        println("   Busy show:  300 tickets → ~143 cars → 86% full lot")
        println("   Medium show: 140 tickets → ~67 cars  → 13% full lot")
        println("   Quiet show:  60 tickets  → ~29 cars  → 6% full lot")
    }
}