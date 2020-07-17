package com.senderman.rouletteapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document

class UserStatus(
        @JsonProperty("user_id")
        val userId: Int,
        val coins: Int
)

class UserBalance(val coins: Int)

@Document("users")
@TypeAlias("user")
data class User(
        @Id
        val id: String,

        val userId: Int,

        var coins: Int,

        val lastReqDate: Int,

        val last10requestDate: Int
)