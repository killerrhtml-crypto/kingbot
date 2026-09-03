package com.kingbot.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kingbot.R
import com.kingbot.service.FloatingControlService

class MainActivity : AppCompatActivity() {

    private lateinit var etMinPricePerKm: EditText
    private lateinit var etMinTotalPrice: EditText
    private lateinit var etMaxPrice: EditText
    private lateinit var etMaxPickupDist: EditText
    private lateinit var cbDynamicPickup: CheckBox
    private lateinit var etMaxTotalDist: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular vistas de la interfaz de configuración
        etMinPricePerKm = findViewById(R.id.etMinPricePerKm)
        etMinTotalPrice = findViewById(R.id.etMinTotalPrice)
        etMaxPrice = findViewById(R.id.etMaxPrice)
        etMaxPickupDist = findViewById(R.id.etMaxPickupDist)
        cbDynamicPickup = findViewById(R.id.cbDynamicPickup)
        etMaxTotalDist = findViewById(R.id.etMaxTotalDist)

        loadSavedPreferences()

        val btnSave = findViewById<Button>(R.id.btnSaveConfig)
        val btnStartService = findViewById<Button>(R.id.btnStartBubble)

        btnSave.setOnClickListener {
            savePreferences()
            Toast.Config("Configuración guardada correctamente", Toast.LENGTH_SHORT).show()
        }

        btnStartService.setOnClickListener {
            checkAndRequestPermissions()
        }
    }

    private fun savePreferences() {
        val prefs = getSharedPreferences("KingBotPrefs", MODE_PRIVATE)
        prefs.edit().apply {
            putFloat("min_price_km", etMinPricePerKm.text.toString().toFloatOrNull() ?: 1.5f)
            putFloat("min_total_price", etMinTotalPrice.text.toString().toFloatOrNull() ?: 3.0f)
            putFloat("max_price", etMaxPrice.text.toString().toFloatOrNull() ?: 50.0f)
            putFloat("max_pickup", etMaxPickupDist.text.toString().toFloatOrNull() ?: 2.5f)
            putBoolean("dynamic_pickup", cbDynamicPickup.isChecked)
            putFloat("max_total_dist", etMaxTotalDist.text.toString().toFloatOrNull() ?: 20.0f)
            apply()
        }
    }

    private fun loadSavedPreferences() {
        val prefs = getSharedPreferences("KingBotPrefs", MODE_PRIVATE)
        etMinPricePerKm.setText(prefs.getFloat("min_price_km", 1.5f).toString())
        etMinTotalPrice.setText(prefs.getFloat("min_total_price", 3.0f).toString())
        etMaxPrice.setText(prefs.getFloat("max_price", 50.0f).toString())
        etMaxPickupDist.setText(prefs.getFloat("max_pickup", 2.5f).toString())
        cbDynamicPickup.isChecked = prefs.getBoolean("dynamic_pickup", true)
        etMaxTotalDist.setText(prefs.getFloat("max_total_dist", 20.0f).toString())
    }

    private fun checkAndRequestPermissions() {
        // 1. Verificar permiso de ventana flotante (SYSTEM_ALERT_WINDOW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1001)
            Toast.makeText(this, "Concede el permiso de superposición para la burbuja", Toast.LENGTH_LONG).show()
            return
        }

        // 2. Iniciar el servicio flotante si los permisos están listos
        val serviceIntent = Intent(this, FloatingControlService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        
        Toast.makeText(this, "Burbuja flotante iniciada", Toast.LENGTH_SHORT).show()
        
        // Redirigir a Accesibilidad para que el usuario la encienda si no está activa
        val accessibilityIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(accessibilityIntent)
    }
}
