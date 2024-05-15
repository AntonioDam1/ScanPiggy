package com.example.scanpiggy

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.developer.gbuttons.GoogleSignInButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class IniciarSesion : AppCompatActivity() {
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var googleBtn: GoogleSignInButton
    private lateinit var gOptions: GoogleSignInOptions
    private lateinit var gClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signUpRedirectText)
        forgotPassword = findViewById(R.id.forgot_password)
        googleBtn = findViewById(R.id.googleBtn)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString()
            val pass = loginPassword.text.toString()

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (pass.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener {
                            Toast.makeText(this@IniciarSesion, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@IniciarSesion, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@IniciarSesion, "Inicio de sesión fallido", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    loginPassword.setError("No se permiten campos vacíos")
                }
            } else if (email.isEmpty()) {
                loginEmail.setError("Los campos vacíos no están permitidos")
            } else {
                loginEmail.setError("Por favor, ingresa un correo electrónico correcto")
            }
        }

        signupRedirectText.setOnClickListener {
            startActivity(Intent(this@IniciarSesion, Registrar::class.java))
        }

        forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this@IniciarSesion)
            val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)

            builder.setView(dialogView)
            val dialog = builder.create()

            dialogView.findViewById<View>(R.id.btnReset).setOnClickListener {
                val userEmail = emailBox.text.toString()

                if (userEmail.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(this@IniciarSesion, "Ingrese su correo electrónico registrado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@IniciarSesion, "Revise su correo electrónico", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this@IniciarSesion, "No se pudo enviar, error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialogView.findViewById<View>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.show()
        }

        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gClient = GoogleSignIn.getClient(this, gOptions)

        val gAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (gAccount != null) {
            finish()
            val intent = Intent(this@IniciarSesion, MainActivity::class.java)
            startActivity(intent)
        }
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        task.getResult(ApiException::class.java)
                        finish()
                        val intent = Intent(this@IniciarSesion, MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: ApiException) {
                        Toast.makeText(this@IniciarSesion, "Algo salió mal", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        googleBtn.setOnClickListener {
            val signInIntent = gClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
    }
}