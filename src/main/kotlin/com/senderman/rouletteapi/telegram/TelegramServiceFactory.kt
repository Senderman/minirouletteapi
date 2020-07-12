package com.senderman.rouletteapi.telegram

import retrofit2.Retrofit

class TelegramServiceFactory {
    companion object {

        private var cachedToken: String? = null
        private var cachedService: TelegramService? = null

        fun getService(token: String): TelegramService {

            if (token == cachedToken) return cachedService!!

            return Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot$token/")
                    .build()
                    .create(TelegramService::class.java)
        }
    }
}