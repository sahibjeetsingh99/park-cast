// User.kt
package com.parkcast.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    val email: String = "",

    val fcmToken: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now()
)