// android/app/src/main/java/com/parkcast/android/network/RetrofitClient.kt
package com.parkcast.android.network

import com.parkcast.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // debug → http://10.0.2.2:8080/ (local emulator)
    // release → https://your-app.up.railway.app/ (set in build.gradle.kts)
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}