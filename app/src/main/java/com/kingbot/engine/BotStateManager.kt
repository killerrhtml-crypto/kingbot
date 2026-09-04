package com.kingbot.engine

object BotStateManager {
    var isBotActive: Boolean = false
        private set

    fun toggleBotState(): Boolean {
        isBotActive = !isBotActive
        return isBotActive
    }

    fun setBotActive(active: Boolean) {
        isBotActive = active
    }
}
