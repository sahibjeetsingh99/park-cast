package com.parkcast

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ParkCastApplication

fun main(args: Array<String>){
    runApplication<ParkCastApplication>(*args)
}