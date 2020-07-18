package com.senderman.rouletteapi.model

import org.springframework.data.mongodb.repository.MongoRepository

interface UsersRepository : MongoRepository<User, String> {

    fun findByUserId(userId: Int): User?

}