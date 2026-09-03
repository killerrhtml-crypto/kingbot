package com.kingbot.service

accessibility.accessibilitynodeinfo.AccessibilityNodeInfo
android.view.accessibility.AccessibilityEvent
android.util.Log
android.widget.Toast
com.kingbot.engine.KingBotOrchestrator
com.kingbot.engine.SettingsManager
com.kingbot.network.TripPayload

class KingBotAccessibilityService : AccessibilityService() {

    companion object {
        const val TAG = "KingBotAccessibility"
        // ID objetivo estándar del botón de progreso o acción en inDrive
        const val TARGET_ACTION_VIEW_ID = "com.InDriver:id/7f0a0cfc"
    }

    private lateinit var orchestrator: KingBotOrchestrator
    private lateinit var settingsManager: SettingsManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "¡Servicio de Accesibilidad de KingBot conectado y activo!")
        
        settingsManager = SettingsManager(this)
        orchestrator = KingBotOrchestrator(this)
        
        // Sincronizar configuraciones guardadas con el orquestador
        settingsManager.applySettingsToOrchestrator(orchestrator)
        
        Toast.makeText(this, "KingBot: Accesibilidad Iniciada", Toast.LENGTH_SHORT).show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return

        // Recorremos el árbol de nodos buscando los elementos clave de la oferta de inDrive
        try {
            processNodeTree(rootNode)
        } catch (e: Exception) {
            Log.e(TAG, "Error procesando nodos de accesibilidad: ${e.message}")
        } finally {
            rootNode.recycle()
        }
    }

    private fun processNodeTree(node: AccessibilityNodeInfo) {
        // Lógica de escaneo de nodos para extraer texto, precios y coordenadas de la pantalla
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue

            // Aquí podemos identificar los textos de precios y destinos visuales
            val text = child.text?.toString() ?: ""
            val viewId = child.viewIdResourceName ?: ""

            // Si detectamos el botón objetivo y el orquestador da luz verde, simulamos el clic
            if (viewId == TARGET_ACTION_VIEW_ID && child.isClickable) {
                // Ejemplo de simulación de clic en el nodo real
                // child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }

            processNodeTree(child)
            child.recycle()
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Servicio de Accesibilidad interrumpido.")
    }
}
