package com.senderman.rouletteapi.model

class UserBalance(val coins: Int) {
    constructor(user: User) : this(user.coins)
}