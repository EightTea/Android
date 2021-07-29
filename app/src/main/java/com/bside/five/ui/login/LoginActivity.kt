package com.bside.five.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityLoginBinding
import com.bside.five.util.ActivityUtil

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val tag = this::class.java.simpleName

    override val layoutResourceId: Int
        get() = R.layout.activity_login
    override val viewModelClass: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}