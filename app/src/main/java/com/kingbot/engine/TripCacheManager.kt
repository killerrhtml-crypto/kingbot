package com.kingbot.engine

import android.util.Log

class TripCacheManager(private val maxCacheSize: Int = 100) {

    companion object {
        const val TAG = "KingBotCache"
    }

    // Usamos una lista enlazada como caché de tamaño limitado para retener IDs recientes
    private val processedTripIds = object : LinkedHashSet<String>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            return size > maxCacheSize
        }
    }

    /**
     * Verifica si un viaje ya fue procesado anteriormente.
     */
    @Synchronized
    fun hasBeenProcessed(tripId: String): Boolean {
        return processedTripIds.contains(tripId)
    }

    /**
     * Marca un ID de viaje como procesado para ignorarlo en futuros ciclos.
     */
    @Synchronized
    fun markAsProcessed(tripId: String) {
        if (tripId.isNotBlank()) {
            processedTripIds.add(tripId)
            Log.d(TAG, "Viaje con ID '$tripId' guardado en caché.")
        }
    }

    /**
     * Limpia completamente la caché de viajes.
     */
    @Synchronized
    fun clearCache() {
        processedTripIds.clear()
        Log.d(TAG, "Caché de viajes limpiada.")
    }
}
