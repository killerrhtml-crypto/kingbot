package com.kingbot.service

import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class BidEvaluator {

    companion object {
        const val TAG = "KingBotEvaluator"
        // Parámetro mínimo deseado por kilómetro (configurable)
        const val MIN_PRICE_PER_KM = 25.0 
    }

    /**
     * Analiza los nodos recursivamente buscando texto de precios y distancias,
     * evalúa la rentabilidad y ejecuta la acción si cumple con el filtro.
     */
    fun evaluateAndExecute(rootNode: AccessibilityNodeInfo): Boolean {
        val textList = mutableListOf<String>()
        collectTextNodes(rootNode, textList)

        var price: Double = 0.0
        var distance: Double = 0.0

        for (text in textList) {
            val cleanText = text.lowercase().trim()
            // Detectar patrón de precio (ej: "$150")
            if (cleanText.contains("$")) {
                price = cleanText.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
            }
            // Detectar patrón de distancia (ej: "3.5 km" o "700 m")
            if (cleanText.contains("km")) {
                distance = cleanText.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
            } else if (cleanText.contains("m") && distance == 0.0) {
                val meters = cleanText.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                if (meters > 0) distance = meters / 1000.0 // Convertir metros a km
            }
        }

        if (price > 0 && distance > 0) {
            val pricePerKm = price / distance
            Log.i(TAG, "Oferta detectada -> Precio: \$$price | Distancia: $distance km | Valor/km: \$$pricePerKm")

            if (pricePerKm >= MIN_PRICE_PER_KM) {
                Log.i(TAG, "¡Oferta rentable! Buscando botón de aceptación...")
                return clickActionNode(rootNode)
            } else {
                Log.w(TAG, "Oferta descartada: No cumple con el mínimo por kilómetro (\$$MIN_PRICE_PER_KM).")
            }
        }
        return false
    }

    private fun collectTextNodes(node: AccessibilityNodeInfo?, list: MutableList<String>) {
        node ?: return
        if (!node.text.isNullOrEmpty()) {
            list.add(node.text.toString())
        }
        for (i in 0 until node.childCount) {
            collectTextNodes(node.getChild(i), list)
        }
    }

    private fun clickActionNode(node: AccessibilityNodeInfo?): Boolean {
        node ?: return false
        val viewId = node.viewIdResourceName ?: ""
        val text = node.text?.toString()?.lowercase() ?: ""

        // Identificar botones de acción o aceptación rápida basados en IDs mapeados
        if (viewId.contains("accept") || text.contains("aceptar") || text.contains("ofrecer")) {
            if (node.isClickable) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                Log.i(TAG, "¡Acción ejecutada con éxito sobre el nodo objetivo!")
                return true
            }
        }

        for (i in 0 until node.childCount) {
            if (clickActionNode(node.getChild(i))) return true
        }
        return false
    }
}
