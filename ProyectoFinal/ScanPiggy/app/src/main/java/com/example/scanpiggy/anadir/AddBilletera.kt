package com.example.scanpiggy.anadir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.scanpiggy.R
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard

class AddBilletera : AppCompatActivity() {

    private lateinit var textViewTarjeta: TextView
    private lateinit var textViewFecha: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_billetera)

        textViewTarjeta = findViewById(R.id.textViewTarjeta)
        textViewFecha = findViewById(R.id.textViewFecha)
    }

    companion object {
        private const val SCAN_RESULT = 100
    }

    fun scanearTarjeta(view: View) {
        val intent = Intent(this, CardIOActivity::class.java)
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY,true)
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV,true)
        intent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE,true)
        startActivityForResult(intent, SCAN_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCAN_RESULT){
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                val scanResult: CreditCard? = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
                textViewTarjeta.text = scanResult?.redactedCardNumber

                if (scanResult != null) {
                    if (scanResult.isExpiryValid){
                        val mes = scanResult?.expiryMonth.toString()
                        val anyo = scanResult?.expiryYear.toString()
                        textViewFecha.text = "$mes/$anyo"

                    }
                }
            }
        }
    }
}