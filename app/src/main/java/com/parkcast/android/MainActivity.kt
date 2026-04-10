// MainActivity.kt
package com.parkcast.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.google.firebase.messaging.FirebaseMessaging
import com.parkcast.android.model.FcmTokenRequest
import com.parkcast.android.network.RetrofitClient
import com.parkcast.android.ui.ParkingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Device token: $token")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        RetrofitClient.api.registerFcmToken(
                            FcmTokenRequest(email = "sahib@test.com", token = token)
                        )
                    } catch (e: Exception) {
                        Log.e("FCM", "Token registration failed: ${e.message}")
                    }
                }
            }
        }

        setContent {
            MaterialTheme {
                Surface {
                    ParkingScreen()
                }
            }
        }
    }
}