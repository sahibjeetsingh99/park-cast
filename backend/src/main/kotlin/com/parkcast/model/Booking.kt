// Booking.kt
package com.parkcast.model

import jakarta.persistence.*
import java.time.LocalDateTime
//import com.parkcast.model.Showtime
//import com.parkcast.model.User

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    val showtime: Showtime = Showtime(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User = User(),

    @Column(nullable = false)
    val seats: Int = 0,

    val createdAt: LocalDateTime = LocalDateTime.now()
)