package com.tomkf.chatee
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity: AppCompatActivity() {
    // lateinit is 'lazy'
    private lateinit var auth: FirebaseAuth
    private lateinit var authMode: AuthMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authMode = intent?.extras?.getParcelable(AuthModeExtraName)!!
        auth = FirebaseAuth.getInstance()

        when(authMode) {
            is AuthMode.Login -> { auth_button.text = getString(R.string.login)}
            is AuthMode.Register -> { auth_button.text = getString(R.string.register) }
        }

        auth_button.setOnClickListener {
            when(authMode) {
                is AuthMode.Login -> {
                    auth.signInWithEmailAndPassword(email_input.text.toString(), password_input.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                startActivity(Intent(this, ChatActivity::class.java))
                            }

                            // Handle error here
                        }
                }

                is AuthMode.Register -> {
                    auth.createUserWithEmailAndPassword(email_input.text.toString(), password_input.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                startActivity(Intent(this, ChatActivity::class.java))
                            }

                            // Handle error here
                        }
                }
            }
        }
    }

    companion object {
        const val AuthModeExtraName = "AUTH_MODE"

        fun newIntent(authMode: AuthMode, context: Context): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(AuthModeExtraName, authMode)
            return intent
        }
    }
}