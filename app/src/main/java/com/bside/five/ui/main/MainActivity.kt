package com.bside.five.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.textView.text = "Good"
    }
}