package com.senderman.rouletteapi.telegram

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TelegramService {

    @GET("sendMessage")
    fun sendMessage(
            @Query("chat_id") chatId: Long,
            @Query("text") text: String,
            @Query("parse_mode") parseMode: String = "HTML"
    ): Call<Unit>

}