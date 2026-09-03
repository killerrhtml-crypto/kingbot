package com.kingbot.service

import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class SkeletonLoaderParser {

    companion object {
        const val TAG = "KingBotSkeleton"
    }

    /**
     * Valida recursivamente si hay un layout de esqueleto activo en pantalla.
     * Si detecta SkeletonLinearLayout o SkeletonLayout, indica que la interfaz 
     * sigue cargando y el bot debe esperar para evitar errores de sincronización.
     */
    fun isSkeletonLoadingActive(node: AccessibilityNodeInfo?): Boolean {
        node ?: return false

        try {
            val className = node.className?.toString() ?: ""

            if (className.contains("SkeletonLinearLayout") || className.contains("SkeletonLayout")) {
                return true
            }

            for (i in 0 until node.childCount) {
                if (isSkeletonLoadingActive(node.getChild(i))) {
                    return true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al verificar estado de esqueleto: ${e.message}")
        } finally {
            // Nota: En exploraciones recursivas profundas de nodos, reciclar aquí 
            // puede causar fallos si el framework vuelve a usar el nodo. 
            // Lo manejamos con seguridad.
        }
        return false
    }
}
