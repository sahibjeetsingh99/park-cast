// ParkCastMessagingService.kt
package com.parkcast.android

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.parkcast.android.network.RetrofitClient
import com.parkcast.android.model.FcmTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ParkCastMessagingService : FirebaseMessagingService() {

    // Called when Firebase issues a new token (first launch, token refresh, or app reinstall)
    override fun onNewToken(token: String) {
        Log.d("FCM", "New token: $token")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.api.registerFcmToken(FcmTokenRequest(
                    email = "sahib@test.com", // hardcoded for now — replace with real auth later
                    token = token
                ))
                Log.d("FCM", "Token registered with backend")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to register token: ${e.message}")
            }
        }
    }

    // Called when a message arrives while the app is in the foreground
    // (When app is in background, the system shows the notification automatically)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Message received: ${remoteMessage.notification?.title}")
    }
}
