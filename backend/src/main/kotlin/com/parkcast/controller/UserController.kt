// backend/src/main/kotlin/com/parkcast/controller/UserController.kt
package com.parkcast.controller

import com.parkcast.model.User
import com.parkcast.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepo: UserRepository) {

    data class FcmTokenRequest(val email: String, val token: String)

    // POST /api/users/fcm-token
    // Registers or updates the FCM token for a user (upsert by email)
    @PostMapping("/fcm-token")
    fun registerFcmToken(@RequestBody request: FcmTokenRequest): ResponseEntity<Any> {
        val existing = userRepo.findByEmail(request.email)
        if (existing != null) {
            userRepo.save(existing.copy(fcmToken = request.token))
        } else {
            userRepo.save(User(email = request.email, fcmToken = request.token))
        }
        return ResponseEntity.ok(mapOf("status" to "registered"))
    }
}
