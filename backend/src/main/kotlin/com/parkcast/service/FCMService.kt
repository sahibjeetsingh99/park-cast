// backend/src/main/kotlin/com/parkcast/service/FCMService.kt
package com.parkcast.service

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class FCMService {

    @PostConstruct
    fun initialize() {
        val serviceAccount = FileInputStream(
            "src/main/resources/firebase-service-account.json"
        )
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

    fun sendNotification(
        fcmToken: String,
        title: String,
        body: String
    ) {
        val message = Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .setToken(fcmToken)
            .build()

        try {
            val response = FirebaseMessaging.getInstance().send(message)
            println("✅ Notification sent: $response")
        } catch (e: Exception) {
            println("❌ Failed to send notification: ${e.message}")
        }
    }
}