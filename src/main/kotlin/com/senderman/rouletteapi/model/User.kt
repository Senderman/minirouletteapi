package com.senderman.rouletteapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
        @Id
        val id: String,

        val userId: Int,

        val coins: Int,

        val lastReqDate: Int,

        val last10requestDate: Int
)