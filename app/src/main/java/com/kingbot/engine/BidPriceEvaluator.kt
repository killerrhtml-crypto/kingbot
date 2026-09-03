package com.kingbot.engine

import android.util.Log

class BidPriceEvaluator {

    companion object {
        const val TAG = "KingBotBidEvaluator"
    }

    /**
     * Evalúa si el precio por kilómetro y los límites generales cumplen con los requisitos.
     */
    fun isPriceValid(price: Double, distanceKm: Double, minPricePerKm: Double, minPriceTotal: Double, maxPriceAllowed: Double): Boolean {
        if (distanceKm <= 0.0) return false

        val pricePerKm = price / distanceKm

        // Validar contra el mínimo por km, precio total mínimo y el límite máximo permitido
        val isValid = pricePerKm >= minPricePerKm && price >= minPriceTotal && price <= maxPriceAllowed

        Log.d(TAG, "Tarifa: $$price | Distancia: ${distanceKm}km | Rate: $$pricePerKm/km -> Aprobado: $isValid")
        return isValid
    }
}
