package com.example.scanpiggy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashScreen : AppCompatActivity() {

    data class AdviceItem(val advice: String, val gifResId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Obtener consejo financiero aleatorio
        val adviceItem = getRandomFinancialAdvice()

        // Configurar texto del consejo financiero
        val adviceTextView = findViewById<TextView>(R.id.splash_advice)
        adviceTextView.text = adviceItem.advice

        // Configurar GIF del consejo financiero
        val adviceGifView = findViewById<ImageView>(R.id.splash_gif)
        Glide.with(this)
            .asGif()
            .load(adviceItem.gifResId)
            .into(adviceGifView)

        // Mostrar el splash screen durante 3 segundos y luego iniciar la actividad principal
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 milisegundos = 3 segundos
    }

    // Función para obtener un consejo financiero aleatorio con su GIF correspondiente
    private fun getRandomFinancialAdvice(): AdviceItem {
        val adviceList = listOf(
            AdviceItem("Ahorra al menos el 10% de tus ingresos.", R.drawable.consejouno),
            AdviceItem("Invierte en tu educación financiera.", R.drawable.consejodos),
            AdviceItem("Crea un presupuesto mensual y cúmplelo.", R.drawable.consejotres),
        )
        return adviceList.random()
    }
}

