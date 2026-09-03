package com.kingbot.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class KingBotService : AccessibilityService() {

    companion object {
        const val TAG = "KingBotEngine"
    }

    override fun onAccessibilityEvent(event: android.view.accessibility.AccessibilityEvent?) {
        event ?: return
        val rootNode = rootInActiveWindow ?: return

        try {
            if (isSkeletonLoadingActive(rootNode) || parseSumsubVerificationNodes(rootNode)) {
                return
            }
            parseNodeTree(rootNode)
        } catch (e: Exception) {
            Log.e(TAG, "Error en el ciclo de accesibilidad: ${e.message}")
        } finally {
            rootNode.recycle()
        }
    }

    private fun parseNodeTree(node: AccessibilityNodeInfo?) {
        node ?: return
        val viewId = node.viewIdResourceName
        if (viewId != null) {
            when {
                viewId.contains("7f0a0cfc") -> {
                    Log.i(TAG, "¡Botón de acción/progreso detectado!")
                }
            }
        }
        for (i in 0 until node.childCount) {
            parseNodeTree(node.getChild(i))
        }
    }

    private fun isSkeletonLoadingActive(node: AccessibilityNodeInfo?): Boolean {
        node ?: return false
        val className = node.className?.toString() ?: ""
        if (className.contains("SkeletonLinearLayout") || className.contains("SkeletonLayout")) {
            return true
        }
        for (i in 0 until node.childCount) {
            if (isSkeletonLoadingActive(node.getChild(i))) return true
        }
        return false
    }

    private fun parseSumsubVerificationNodes(node: AccessibilityNodeInfo?): Boolean {
        node ?: return false
        val packageName = node.packageName?.toString() ?: ""
        if (packageName.contains("sumsub")) {
            Log.w(TAG, "KYC Sumsub activo. Bot en pausa de seguridad.")
            return true
        }
        for (i in 0 until node.childCount) {
            if (parseSumsubVerificationNodes(node.getChild(i))) return true
        }
        return false
    }

    override fun onInterrupt() {
        Log.w(TAG, "Servicio de accesibilidad King Bot interrumpido.")
    }
}
