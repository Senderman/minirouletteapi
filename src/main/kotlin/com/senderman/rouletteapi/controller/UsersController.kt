package com.senderman.rouletteapi.controller

import com.senderman.rouletteapi.model.UserBalance
import com.senderman.rouletteapi.model.UserStatus
import com.senderman.rouletteapi.model.UsersRepository
import com.senderman.rouletteapi.telegram.TelegramService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UsersController(
        @Value("\${channelId}")
        private val channelId: Long,

        @Autowired
        private val db: UsersRepository
) {

    @GetMapping("/getBalance")
    fun getBalance(@RequestParam(value = "user_id") userId: Int): UserBalance {
        val result = db.findByUserId(userId) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cannot find user with id $userId"
        )
        return UserBalance(result.coins)
    }


    @PutMapping("/updateCoins")
    fun updateCoins(
            @RequestParam(value = "user_id") userId: Int,
            @RequestParam(value = "coins") coins: Int,
            @RequestParam(value = "token") token: String
    ): UserStatus {
        val user = db.findByUserId(userId) ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cannot find user with id $userId"
        )

        if (coins < 0 && user.coins + coins < 400)
            throw ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Not enough coins will remain on balance (must be at least 400)"
            )

        val telegram = TelegramService.getService(token)
        val code = telegram.sendMessage(channelId, "<b>Новая транзакция выполняется...</b>").statusCode
        if (code != HttpStatus.OK) throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Telegram Verification error"
        )

        try {
            user.coins += coins
            db.save(user)
            telegram.sendMessage(channelId,
                    "Транзакция на перевод $coins монеток пользователю $userId успешна!\n" +
                            "Теперь у пользователя ${user.coins} монеток!")
            return UserStatus(user.userId, user.coins)
        } catch (e: Exception) {
            throw ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Unknown error",
                    e
            )
        }
    }

}