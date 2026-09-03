package com.kingbot.service

android.app.Service
android.content.Intent
android.graphics.PixelFormat
android.os.IBinder
android.view.Gravity
android.view.LayoutInflater
android.view.MotionEvent
android.view.View
android.view.WindowManager
android.widget.ImageView
android.widget.LinearLayout
android.widget.TextView
android.widget.Toast
com.kingbot.R

class FloatingControlService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var collapsedView: View
    private lateinit var expandedView: View
    private lateinit var statusIndicator: View

    private var isBotRunning: Boolean = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Inflar la vista flotante principal
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        collapsedView = floatingView.findViewById(R.id.collapsed_container)
        expandedView = floatingView.findViewById(R.id.expanded_container)
        statusIndicator = floatingView.findViewById(R.id.status_indicator)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 200

        windowManager.addView(floatingView, params)

        // Configurar gestos táctiles de arrastre y expansión
        setupTouchAndClickListeners(params)
    }

    private fun setupTouchAndClickListeners(params: WindowManager.LayoutParams) {
        val rootLayout = floatingView.findViewById<View>(R.id.root_floating_layout)
        val btnToggle = floatingView.findViewById<TextView>(R.id.btn_toggle_bot)

        // Control táctil para mover la burbuja por la pantalla
        rootLayout.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var touchX = 0f
            private var touchY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        touchX = event.rawX
                        touchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - touchX).toInt()
                        params.y = initialY + (event.rawY - touchY).toInt()
                        windowManager.updateViewLayout(floatingView, params)
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        // Si el movimiento fue corto, se interpreta como un toque para expandir/colapsar
                        val xDiff = Math.abs(event.rawX - touchX)
                        val yDiff = Math.abs(event.rawY - touchY)
                        if (xDiff < 10 && yDiff < 10) {
                            toggleExpandedPanel()
                        }
                        return true
                    }
                }
                return false
            }
        })

        // Botón de encendido/apagado rápido del bot dentro del panel desplegado
        btnToggle.setOnClickListener {
            isBotRunning = !isBotRunning
            if (isBotRunning) {
                statusIndicator.setBackgroundColor(android.graphics.Color.parseColor("#00FF66")) // Verde Neón
                btnToggle.text = "DETENER BOT"
                Toast.makeText(this, "KingBot Activado", Toast.LENGTH_SHORT).show()
            } else {
                statusIndicator.setBackgroundColor(android.graphics.Color.parseColor("#FF0033")) // Rojo Láser
                btnToggle.text = "INICIAR BOT"
                Toast.makeText(this, "KingBot en Pausa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleExpandedPanel() {
        if (expandedView.visibility == View.GONE) {
            collapsedView.visibility = View.GONE
            expandedView.visibility = View.VISIBLE
        } else {
            expandedView.visibility = View.GONE
            collapsedView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }
}
