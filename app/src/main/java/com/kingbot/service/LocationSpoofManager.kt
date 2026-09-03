package com.kingbot.service

import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import android.util.Log

class LocationSpoofManager {

    companion object {
        const val TAG = "KingBotLocation"
    }

    /**
     * Inyecta coordenadas simuladas para proteger el estado del dispositivo
     * y mantener la consistencia en el radio de búsqueda de ofertas.
     */
    fun spoofLocation(locationManager: LocationManager, latitude: Double, longitude: Double) {
        try {
            val provider = LocationManager.GPS_PROVIDER
            
            // Asegurarnos de que el proveedor simulado esté habilitado
            try {
                locationManager.addTestProvider(
                    provider,
                    false, false, false, false,
                    true, true, true,
                    android.location.Criteria.POWER_LOW,
                    android.location.Criteria.ACCURACY_FINE
                )
            } catch (e: Exception) {
                // El proveedor ya podría estar añadido
            }

            locationManager.setTestProviderEnabled(provider, true)

            val mockLocation = Location(provider).apply {
                this.latitude = latitude
                this.longitude = longitude
                this.altitude = 10.0
                this.accuracy = 3.0f
                this.time = System.currentTimeMillis()
                this.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            }

            locationManager.setTestProviderLocation(provider, mockLocation)
            Log.d(TAG, "Ubicación simulada inyectada con éxito: $latitude, $longitude")

        } catch (e: SecurityException) {
            Log.e(TAG, "Permiso de ubicación simulada (Mock Locations) no concedido: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Error al inyectar ubicación fake: ${e.message}")
        }
    }
}
