package com.bside.five.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.bside.five.R
import com.bside.five.adapter.SurveyAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initRecyclerView()
        viewModel.requestMainApi()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        }
    }

    private fun initRecyclerView() {
        val adapter = SurveyAdapter()

        val itemDivider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_white)?.let {
            itemDivider.setDrawable(it)
        }

        binding.mainRecyclerView.addItemDecoration(itemDivider)
        binding.mainRecyclerView.adapter = adapter
    }
}