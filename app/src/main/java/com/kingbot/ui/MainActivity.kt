package com.kingbot.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kingbot.R
import com.kingbot.engine.SettingsManager
import com.kingbot.service.FloatingControlService

class MainActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsManager = SettingsManager(this)

        val etMinPriceKm = findViewById<EditText>(R.id.etMinPriceKm)
        val etMinPriceTotal = findViewById<EditText>(R.id.etMinPriceTotal)
        val etMaxPrice = findViewById<EditText>(R.id.etMaxPrice)
        val etMaxPickup = findViewById<EditText>(R.id.etMaxPickup)
        val cbDynamicPickup = findViewById<CheckBox>(R.id.cbDynamicPickup)
        val etMaxTrip = findViewById<EditText>(R.id.etMaxTrip)
        
        val btnSave = findViewById<Button>(R.id.btnSaveSettings)
        val btnStartFloating = findViewById<Button>(R.id.btnStartFloating)

        // Cargar los valores actuales guardados en las preferencias
        etMinPriceKm.setText(settingsManager.minPricePerKm.toString())
        etMinPriceTotal.setText(settingsManager.minPriceTotal.toString())
        etMaxPrice.setText(settingsManager.maxPriceAllowed.toString())
        etMaxPickup.setText(settingsManager.maxPickupKm.toString())
        cbDynamicPickup.isChecked = settingsManager.allowDynamicPickupExtension
        etMaxTrip.setText(settingsManager.maxTripKm.toString())

        // Guardar cambios desde la interfaz y sincronizar el motor
        btnSave.setOnClickListener {
            try {
                settingsManager.minPricePerKm = etMinPriceKm.text.toString().toDouble()
                settingsManager.minPriceTotal = etMinPriceTotal.text.toString().toDouble()
                settingsManager.maxPriceAllowed = etMaxPrice.text.toString().toDouble()
                settingsManager.maxPickupKm = etMaxPickup.text.toString().toDouble()
                settingsManager.allowDynamicPickupExtension = cbDynamicPickup.isChecked
                settingsManager.maxTripKm = etMaxTrip.text.toString().toDouble()

                Toast.makeText(this, "¡Parámetros actualizados en la interfaz!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: Revisa que los campos numéricos sean válidos", Toast.LENGTH_SHORT).show()
            }
        }

        // Lanzar el servicio flotante de control
        btnStartFloating.setOnClickListener {
            val intent = Intent(this, FloatingControlService::class.java)
            startService(intent)
            Toast.makeText(this, "Panel flotante activado", Toast.LENGTH_SHORT).show()
        }
    }
}
