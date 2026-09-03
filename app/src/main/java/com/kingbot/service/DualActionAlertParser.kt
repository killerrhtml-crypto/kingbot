package com.kingbot.service

import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class DualActionAlertParser {

    companion object {
        const val TAG = "KingBotDualAction"
    }

    /**
     * Identifica y procesa diálogos de alerta con botones duales (IDs: 7f0a0d59 y 7f0a0d5a)
     * para automatizar respuestas lógicas de confirmación o rechazo.
     */
    fun parseDualActionAlertNodes(node: AccessibilityNodeInfo?) {
        node ?: return

        try {
            val viewId = node.viewIdResourceName

            if (viewId != null) {
                when {
                    viewId.endsWith("7f0a0d59") -> {
                        if (node.isClickable) {
                            Log.i(TAG, "Botón izquierdo de alerta dual localizado.")
                            // Aquí se puede ejecutar node.performAction(AccessibilityNodeInfo.ACTION_CLICK) si se requiere confirmar
                        }
                    }
                    viewId.endsWith("7f0a0d5a") -> {
                        if (node.isClickable) {
                            Log.i(TAG, "Botón derecho de alerta dual localizado.")
                        }
                    }
                }
            }

            for (i in 0 until node.childCount) {
                parseDualActionAlertNodes(node.getChild(i))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar la alerta de acción dual: ${e.message}")
        } finally {
            // Nota: Reciclar nodos de forma recursiva profunda puede causar excepciones si el framework los reutiliza.
        }
    }
}
