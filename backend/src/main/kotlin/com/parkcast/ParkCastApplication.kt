package com.parkcast

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling 

@SpringBootApplication
@EnableScheduling
class ParkCastApplication

fun main(args: Array<String>){
    runApplication<ParkCastApplication>(*args)
}