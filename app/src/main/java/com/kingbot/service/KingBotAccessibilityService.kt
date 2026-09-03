package com.kingbot.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class KingBotAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "KingBotAccessibility"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode: AccessibilityNodeInfo = rootInActiveWindow ?: return
        evaluateInDriveCards(rootNode)
        rootNode.recycle()
    }

    private fun evaluateInDriveCards(node: AccessibilityNodeInfo) {
        val text = node.text?.toString() ?: ""

        if (text.isNotEmpty()) {
            // Detección específica para tarifas en República Dominicana (ej: DOP 165)
            if (text.contains("DOP") || text.contains("$")) {
                Log.d(TAG, "Tarifa detectada en DOP: $text")
                // Aquí aplicamos la lectura de los SharedPreferences para evaluar si cumple el precio mínimo por KM
            }

            // Detección de reputación y viajes (ej: 5.0 (1) o 4.78 (126))
            if (text.contains("★") || (text.contains("(") && text.contains(")"))) {
                Log.d(TAG, "Estadísticas del pasajero/conductor detectadas: $text")
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            evaluateInDriveCards(child)
            child.recycle()
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Servicio de accesibilidad interrumpido.")
    }
}
