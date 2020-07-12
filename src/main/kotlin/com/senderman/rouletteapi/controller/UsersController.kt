package com.senderman.rouletteapi.controller

import com.senderman.rouletteapi.model.TransactionResult
import com.senderman.rouletteapi.model.User
import com.senderman.rouletteapi.model.UserBalance
import com.senderman.rouletteapi.telegram.TelegramServiceFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UsersController(
        @Autowired val mongoOps: MongoTemplate,
        @Value("\${channelId}") val channelId: Long
) {

    val coinsKey: String = "coins"

    fun findUserQuery(userId: Int): Query = Query(Criteria.where("userId").isEqualTo(userId))

    @GetMapping("/getBalance")
    fun getBalance(
            @RequestParam(value = "user_id") userId: Int
    ): UserBalance {
        val result = mongoOps.findOne(findUserQuery(userId), User::class.java)
        return if (result != null) {
            UserBalance(result)
        } else UserBalance(-1)
    }


    @PutMapping("/updateCoins")
    fun updateCoins(
            @RequestParam(value = "user_id") userId: Int,
            @RequestParam(value = "coins") coins: Int,
            @RequestParam(value = "token") token: String
    ): TransactionResult {
        val user = mongoOps.findOne(findUserQuery(userId), User::class.java)
                ?: return TransactionResult(false, "User does not exist")

        if (user.coins + coins < 0) return TransactionResult(false, "Not enough coins")

        val telegram = TelegramServiceFactory.getService(token)
        val code = telegram.sendMessage(channelId, "<b>Новая транкзация выполняется...</b>").execute().code()
        if (code != 200) return TransactionResult(false, "Telegram verification error")

        return try {
            mongoOps.updateFirst(findUserQuery(userId), Update().inc(coinsKey, coins), User::class.java)
            telegram.sendMessage(channelId,
                    "Транкзация на перевод $coins монеток пользователю $userId успешна!\n" +
                            "Теперь у пользователя ${user.coins + coins} монеток!")
                    .execute()
            TransactionResult(true, "OK")
        } catch (e: Exception) {
            TransactionResult(false, "Error: $e")
        }
    }

}