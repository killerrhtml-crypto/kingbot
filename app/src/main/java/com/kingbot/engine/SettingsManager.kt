package com.kingbot.engine

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("kingbot_secure_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MIN_PRICE_KM = "min_price_km"
        private const val KEY_MIN_PRICE_TOTAL = "min_price_total"
        private const val KEY_MAX_PRICE = "max_price"
        private const val KEY_MAX_PICKUP = "max_pickup"
        private const val KEY_DYNAMIC_PICKUP = "dynamic_pickup"
        private const val KEY_MAX_TRIP = "max_trip"
    }

    var minPricePerKm: Double
        get() = prefs.getFloat(KEY_MIN_PRICE_KM, 1.50f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MIN_PRICE_KM, value.toFloat()).apply()

    var minPriceTotal: Double
        get() = prefs.getFloat(KEY_MIN_PRICE_TOTAL, 3.00f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MIN_PRICE_TOTAL, value.toFloat()).apply()

    var maxPriceAllowed: Double
        get() = prefs.getFloat(KEY_MAX_PRICE, 50.00f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MAX_PRICE, value.toFloat()).apply()

    var maxPickupKm: Double
        get() = prefs.getFloat(KEY_MAX_PICKUP, 2.5f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MAX_PICKUP, value.toFloat()).apply()

    var allowDynamicPickupExtension: Boolean
        get() = prefs.getBoolean(KEY_DYNAMIC_PICKUP, true)
        set(value) = prefs.edit().putBoolean(KEY_DYNAMIC_PICKUP, value).apply()

    var maxTripKm: Double
        get() = prefs.getFloat(KEY_MAX_TRIP, 20.0f).toDouble()
        set(value) = prefs.edit().putFloat(KEY_MAX_TRIP, value.toFloat()).apply()

    /**
     * Sincroniza los ajustes guardados directamente con una instancia del Orquestador.
     */
    fun applySettingsToOrchestrator(orchestrator: KingBotOrchestrator) {
        orchestrator.minPricePerKm = minPricePerKm
        orchestrator.minPriceTotal = minPriceTotal
        orchestrator.maxPriceAllowed = maxPriceAllowed
        orchestrator.maxPickupKm = maxPickupKm
        orchestrator.allowDynamicPickupExtension = allowDynamicPickupExtension
        orchestrator.maxTripKm = maxTripKm
    }
}
