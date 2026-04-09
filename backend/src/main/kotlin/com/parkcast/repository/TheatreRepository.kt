package com.parkcast.repository

import com.parkcast.model.Theatre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TheatreRepository : JpaRepository<Theatre, Int>