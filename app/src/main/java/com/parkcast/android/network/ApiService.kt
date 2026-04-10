// android/app/src/main/java/com/parkcast/android/network/ApiService.kt
package com.parkcast.android.network

import com.parkcast.android.model.FcmTokenRequest
import com.parkcast.android.model.ParkingPrediction
import com.parkcast.android.model.Showtime
import com.parkcast.android.model.Theatre
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("api/theatres")
    suspend fun getTheatres(): List<Theatre>

    @GET("api/showtimes")
    suspend fun getShowtimes(): List<Showtime>

    @GET("api/showtimes/{id}/parking")
    suspend fun getParkingPrediction(@Path("id") id: Int): ParkingPrediction

    @GET("api/theatres/{id}/parking")
    suspend fun getTheatrePredictions(@Path("id") id: Int): List<ParkingPrediction>

    @POST("api/users/fcm-token")
    suspend fun registerFcmToken(@Body request: FcmTokenRequest): Response<Unit>
}