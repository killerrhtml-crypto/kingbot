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
        traverseAndExtractRideData(rootNode)
        rootNode.recycle()
    }

    private fun traverseAndExtractRideData(node: AccessibilityNodeInfo) {
        val text = node.text?.toString() ?: ""
        
        if (text.isNotEmpty()) {
            if (isPriceNode(text)) {
                Log.d(TAG, "Precio detectado dinámicamente: $text")
            }
            
            if (text.contains("★") || (text.contains("(") && text.contains(")"))) {
                Log.d(TAG, "Reputación/Viajes detectados: $text")
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            traverseAndExtractRideData(child)
            child.recycle()
        }
    }

    private fun isPriceNode(text: String): Boolean {
        return text.any { it.isDigit() } && (text.contains("DOP") || text.contains("$") || text.length <= 6)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Servicio de accesibilidad interrumpido.")
    }
}
