package com.bside.five.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.ActivityLoginBinding
import com.bside.five.util.ActivityUtil

class LoginActivity : AppCompatActivity() {

    private val tag = this::class.java.simpleName
    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.kakaoLoginBtn.setOnClickListener {
            ActivityUtil.startMainActivity(this)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}