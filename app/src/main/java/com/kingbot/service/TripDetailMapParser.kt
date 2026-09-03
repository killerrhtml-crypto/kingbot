package com.kingbot.service

import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class TripDetailMapParser {

    companion object {
        const val TAG = "KingBotTripDetail"
    }

    /**
     * Analiza el panel de detalles de ruta y mapa (IDs clave: 7f0a0cfc para el botón de progreso 
     * de aceptación y 7f0a0d0c para el mapa de ruta activo).
     */
    fun parseTripDetailBottomSheet(node: AccessibilityNodeInfo?) {
        node ?: return

        try {
            val viewId = node.viewIdResourceName

            if (viewId != null) {
                when {
                    viewId.endsWith("7f0a0cfc") -> {
                        if (node.isClickable) {
                            Log.i(TAG, "Botón de confirmación de ruta localizado en detalle de mapa.")
                            // Aquí se activará la pulsación automática una vez aprobado por los filtros financieros y de zona:
                            // node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        }
                    }
                    viewId.endsWith("7f0a0d0c") -> {
                        Log.i(TAG, "Mapa de ruta activo en pantalla de detalles.")
                    }
                }
            }

            for (i in 0 until node.childCount) {
                parseTripDetailBottomSheet(node.getChild(i))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar el detalle de ruta y mapa: ${e.message}")
        } finally {
            // Nota: Se evita reciclar nodos recursivos de forma agresiva para prevenir excepciones del framework.
        }
    }
}
