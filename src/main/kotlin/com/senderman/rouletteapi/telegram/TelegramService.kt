package com.senderman.rouletteapi.telegram

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity

class TelegramService(token: String) {

    private val template = RestTemplateBuilder().rootUri("https://api.telegram.org/bot$token").build()

    fun sendMessage(chatId: Long, text: String): ResponseEntity<Unit> =
            template.getForEntity(
                    "/sendMessage?chat_id=$chatId&text=$text&parse_mode=HTML",
                    Unit::class.java
            )

    companion object {
        private var cachedToken: String? = null
        private var cachedService: TelegramService? = null

        fun getService(token: String): TelegramService {
            if (token == cachedToken) return cachedService!!

            cachedToken = token
            cachedService = TelegramService(token)
            return cachedService!!
        }

    }
}