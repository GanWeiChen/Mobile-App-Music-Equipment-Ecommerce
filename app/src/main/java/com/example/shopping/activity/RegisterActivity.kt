package com.example.shopping.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopping.Database.UserAuth
import com.example.shopping.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userAuth: UserAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAuth = UserAuth(this)

        binding.signin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                userAuth.registerUser(email, password, object : UserAuth.OnAuthCompleteListener {
                    override fun onSuccess(user: FirebaseUser?) {
                        user?.let {
                            val database = FirebaseDatabase.getInstance().reference
                            val userMap = hashMapOf(
                                "name" to name,
                                "email" to email
                            )
                            database.child("users").child(it.uid).setValue(userMap)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("RegisterActivity", "Database write successful")
                                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_LONG).show()
                                        // Add a short delay before starting LoginActivity
                                        binding.registerBtn.postDelayed({
                                            startActivity(Intent(this@RegisterActivity,IntroActivity::class.java))
                                            finish() // Close the current activity
                                        }, 500) // Delay in milliseconds
                                    } else {
                                        Log.e("RegisterActivity", "Database write failed: ${task.exception?.message}")
                                        Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }

                    override fun onFailure(e: Exception?) {
                        Log.e("RegisterActivity", "Registration failed: ${e?.message}")
                        Toast.makeText(this@RegisterActivity, "Registration Failed: ${e?.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }


    }
}
