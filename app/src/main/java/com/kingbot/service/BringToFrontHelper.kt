package com.kingbot.service

import android.content.Context
import android.content.Intent
import android.util.Log

object BringToFrontHelper {

    private const val TAG = "KingBotForeground"
    private const val INDRIVE_PACKAGE = "sinet.startup.inDriver"

    fun bringInDriveToForeground(context: Context) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(INDRIVE_PACKAGE)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            }

            if (intent != null) {
                context.startActivity(intent)
                Log.i(TAG, "¡Viaje asegurado! Trayendo inDrive al primer plano de inmediato.")
            } else {
                Log.w(TAG, "No se encontró el Intent de lanzamiento para el paquete: $INDRIVE_PACKAGE")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al intentar traer la app al frente: ${e.message}")
        }
    }
}
