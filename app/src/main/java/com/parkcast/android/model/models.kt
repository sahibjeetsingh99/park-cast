// android/app/src/main/java/com/parkcast/android/model/Models.kt
package com.parkcast.android.model

data class Theatre(
    val id: Int,
    val name: String,
    val address: String,
    val lotCapacity: Int
)

data class Showtime(
    val id: Int,
    val movieName: String,
    val startTime: String,
    val totalSeats: Int,
    val theatre: Theatre
)

data class FcmTokenRequest(val email: String, val token: String)

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