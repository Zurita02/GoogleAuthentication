package com.dgz.logintoolbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dgz.logintoolbar.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        this.onSignInResult(result)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?) {
        if (result != null) {
            if (result.resultCode == RESULT_OK) {
                val usuario = FirebaseAuth.getInstance().currentUser
                if (usuario != null) {
                    println("Bienvenido: ${usuario.displayName}")
                    println("Correo: ${usuario.email}")
                    println("Token: ${usuario.uid}")
                    val intMenu = Intent(this, MainActivity::class.java)
                    startActivity(intMenu)
                    finish()    //Quita de la pila esta actividad
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarEventos()
        verificarUsuarioFirmado()
    }

    private fun configurarEventos() {
        binding.btnLogin.setOnClickListener {
            autenticar()
        }

        binding.btnLogout.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun verificarUsuarioFirmado() {
        val usuario = FirebaseAuth.getInstance().currentUser
        if (usuario != null){
            //Ya estaba firmado
            println("Bienvenido NUEVAMENTE: ${usuario.displayName}")
            val intMenu = Intent(this, MainActivity::class.java)
            startActivity(intMenu)
            //finish()
        }
    }

    private fun cerrarSesion() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener{
            //SEGURO que cerró la sesión después de cierto tiempo
            val intLogin = Intent(this, LoginActivity::class.java)
            startActivity(intLogin)
            //startActivity
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun autenticar() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }
}