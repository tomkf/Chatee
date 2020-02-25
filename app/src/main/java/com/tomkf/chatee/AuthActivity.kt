package com.tomkf.chatee

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity: AppCompatActivity() {
    private lateinit var authMode: AuthMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authMode = intent?.extras?.getParcelable(AuthModeExtraName)!!

        when(authMode) {
            is AuthMode.Login -> { activity_auth.text = getString(R.string.login)}
            is AuthMode.Register -> { activity_auth.text = getString(R.string.register) }
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