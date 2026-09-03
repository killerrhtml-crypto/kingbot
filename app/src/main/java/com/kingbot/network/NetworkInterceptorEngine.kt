package com.kingbot.network

import android.util.Log
import org.json.JSONObject

class NetworkInterceptorEngine {

    companion object {
        const val TAG = "KingBotNetwork"
    }

    /**
     * Procesa un payload JSON crudo interceptado desde la red de inDrive.
     * Extrae de forma ultra rápida los datos clave para alimentar los evaluadores.
     */
    fun parseTripJson(jsonString: String): TripPayload? {
        return try {
            val jsonObject = JSONObject(jsonString)

            // Las llaves varían según el esquema de la API interna, definimos campos estándar de ejemplo
            val tripId = jsonObject.optString("id", jsonObject.optString("trip_id", ""))
            val price = jsonObject.optDouble("price", jsonObject.optDouble("cost", 0.0))
            val pickupDistanceKm = jsonObject.optDouble("pickup_distance", 0.0)
            val totalDistanceKm = jsonObject.optDouble("total_distance", jsonObject.optDouble("distance", 0.0))
            val destinationName = jsonObject.optString("destination_name", jsonObject.optString("to_address", ""))

            if (tripId.isBlank()) {
                return null
            }

            Log.d(TAG, "JSON Interceptado con éxito - ID: $tripId | Precio: $$price | Destino: $destinationName")

            TripPayload(
                tripId = tripId,
                price = price,
                pickupDistanceKm = pickupDistanceKm,
                totalDistanceKm = totalDistanceKm,
                destinationName = destinationName,
                rawJson = jsonString
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error al deserializar paquete JSON del viaje: ${e.message}")
            null
        }
    }
}
