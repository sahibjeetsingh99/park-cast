package com.parkcast.model

import jakarta.persistence.*
import com.parkcast.model.Theatre
import java.time.LocalDateTime

@Entity
@Table(name = "showtimes")
data class Showtime(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int =0,

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    val theatre: Theatre = Theatre(),

    @Column(nullable = false)
    val movieName: String = "",

    @Column(nullable = false)
    val startTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val totalSeats: Int = 0
)
