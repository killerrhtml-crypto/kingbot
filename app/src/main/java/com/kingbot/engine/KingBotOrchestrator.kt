package com.kingbot.engine

import android.content.Context
import android.util.Log
import com.kingbot.network.TripPayload
import com.kingbot.service.BringToFrontHelper

class KingBotOrchestrator(private val context: Context) {

    companion object {
        const val TAG = "KingBotOrchestrator"
    }

    // Instancias de nuestros motores independientes
    private val bidEvaluator = BidPriceEvaluator()
    private val pickupEvaluator = PickupEvaluator()
    private val dropoffEvaluator = DropoffEvaluator()
    private val zoneEvaluator = ZoneEvaluator()
    private val cacheManager = TripCacheManager(150)

    // Configuración de umbrales del usuario (pueden ser inyectados o leídos de SharedPreferences)
    var minPricePerKm: Double = 1.50
    var minPriceTotal: Double = 3.00
    var maxPriceAllowed: Double = 50.00
    
    var maxPickupKm: Double = 2.5
    var allowDynamicPickupExtension: Boolean = true

    var maxTripKm: Double = 20.0

    /**
     * Procesa una oferta entrante (proveniente de Red JSON o Parser Visual)
     * a través del pipeline completo de filtrado de alta velocidad.
     */
    fun evaluateAndExecuteTrip(trip: TripPayload): Boolean {
        try {
            // 1. Verificar si ya fue procesado en caché para evitar bucles
            if (cacheManager.hasBeenProcessed(trip.tripId)) {
                Log.d(TAG, "Viaje ${trip.tripId} ignorado: ya existe en caché.")
                return false
            }

            // Marcar de inmediato en caché para evitar reintentos concurrentes
            cacheManager.markAsProcessed(trip.tripId)

            // 2. Evaluar Zona (Filtro de Destino / Zona Bloqueada)
            if (!zoneEvaluator.isZoneAllowed(trip.destinationName)) {
                Log.i(TAG, "Viaje rechazado: Destino en zona bloqueada -> ${trip.destinationName}")
                return false
            }

            // 3. Evaluar Distancia de Recogida (Punto A con flexibilidad opcional)
            if (!pickupEvaluator.isPickupValid(trip.pickupDistanceKm, maxPickupKm, allowDynamicPickupExtension)) {
                Log.i(TAG, "Viaje rechazado: Distancia de recogida ${trip.pickupDistanceKm}km fuera de rango.")
                return false
            }

            // 4. Evaluar Distancia Total del Viaje (Punto B)
            if (!dropoffEvaluator.isDropoffValid(trip.totalDistanceKm, maxTripKm)) {
                Log.i(TAG, "Viaje rechazado: Distancia total ${trip.totalDistanceKm}km excede el límite.")
                return false
            }

            // 5. Evaluar Precio y Tasa por Kilómetro ($/km)
            if (!bidEvaluator.isPriceValid(trip.price, trip.totalDistanceKm, minPricePerKm, minPriceTotal, maxPriceAllowed)) {
                Log.i(TAG, "Viaje rechazado: Tarifa no rentable (Precio: $$| ${trip.totalDistanceKm}km).")
                return false
            }

            // ==========================================
            // ¡VIAJE APROBADO! DISPARANDO ACCIÓN DE ÉXITO
            // ==========================================
            Log.i(TAG, "¡VICTORIA! Viaje ${trip.tripId} aprobado por todos los filtros. Ejecutando aceptación...")

            // Forzar a inDrive al primer plano para concretar la interfaz visual de confirmación
            BringToFrontHelper.bringInDriveToForeground(context)

            return true

        } catch (e: Exception) {
            Log.e(TAG, "Error crítico en el pipeline del orquestador: ${e.message}")
            return false
        }
    }
}
