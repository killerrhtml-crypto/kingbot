package com.kingbot.service

import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class CreateOfferParser {

    companion object {
        const val TAG = "KingBotCreateOffer"
    }

    /**
     * Analiza el panel BottomSheet de creación y envío de oferta (IDs clave: 
     * 7f0a049a para el botón principal de envío y 7f0a0499 para cancelar/secundario).
     */
    fun parseCreateOfferBottomSheet(node: AccessibilityNodeInfo?) {
        node ?: return

        try {
            val viewId = node.viewIdResourceName

            if (viewId != null) {
                when {
                    viewId.endsWith("7f0a049a") -> {
                        if (node.isClickable) {
                            Log.i(TAG, "Panel de oferta detectado. El bot puede disparar la oferta aquí.")
                            // Aquí se activará la pulsación automática una vez validado el precio por km:
                            // node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        }
                    }
                    viewId.endsWith("7f0a0499") -> {
                        Log.i(TAG, "Botón secundario del panel de oferta localizado.")
                    }
                }
            }

            for (i in 0 until node.childCount) {
                parseCreateOfferBottomSheet(node.getChild(i))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar el panel create_offer: ${e.message}")
        } finally {
            // Nota: Se omite el reciclaje recursivo directo para evitar excepciones de sincronización en el framework.
        }
    }
}
