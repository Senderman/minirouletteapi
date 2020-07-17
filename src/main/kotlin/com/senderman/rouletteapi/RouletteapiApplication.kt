package com.senderman.rouletteapi

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootApplication
class RouletteapiApplication

fun main(args: Array<String>) {
    runApplication<RouletteapiApplication>(*args)
}
