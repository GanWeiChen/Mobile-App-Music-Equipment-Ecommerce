package com.example.shopping.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopping.Database.UserAuth
import com.example.shopping.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userAuth: UserAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAuth = UserAuth(this)
        binding.signup.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEnterText.text.toString()
            val password = binding.passwordEnterText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userAuth.loginUser(email, password, object : UserAuth.OnAuthCompleteListener {
                    override fun onSuccess(user: FirebaseUser?) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userId", user?.uid)
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(e: Exception?) {
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
