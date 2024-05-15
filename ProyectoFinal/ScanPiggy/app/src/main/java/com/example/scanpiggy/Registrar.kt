package com.example.scanpiggy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class Registrar : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)


        auth = FirebaseAuth.getInstance()
        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        signupButton.setOnClickListener {
            val user = signupEmail.text.toString().trim()
            val pass = signupPassword.text.toString().trim()

            if (user.isEmpty()) {
                signupEmail.setError("El correo electrónico no puede estar vacío")
            }
            if (pass.isEmpty()) {
                    signupPassword.setError("La contraseña no puede estar vacía")
            } else {
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@Registrar, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Registrar, IniciarSesion::class.java))
                    } else {
                        Toast.makeText(this@Registrar, "SignUp Failed" + task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this@Registrar, IniciarSesion::class.java))
        }

    }
}