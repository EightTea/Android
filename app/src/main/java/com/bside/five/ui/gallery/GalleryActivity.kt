package com.bside.five.ui.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bside.five.R
import com.bside.five.adapter.GalleryAdapter
import com.bside.five.databinding.ActivityGalleryBinding
import com.bside.five.model.GalleryInfo

class GalleryActivity : AppCompatActivity() {

    private val binding: ActivityGalleryBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_gallery) as ActivityGalleryBinding
    }
    private var adapter: GalleryAdapter? = null
    private val items = ArrayList<GalleryInfo>()
    private var imagePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initRecyclerView()
        searchImage()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_done -> {
                val intent = Intent()
                intent.putExtra("key", imagePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gallery, menu)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(binding.galleryToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initRecyclerView() {
        adapter = GalleryAdapter()
        adapter?.setActionListener(object : GalleryAdapter.ActionListener {
            override fun clickItem(position: Int, item: GalleryInfo) {
                imagePath = item.uri
            }
        })

        binding.galleryRecyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.galleryRecyclerView.adapter = adapter
    }

    /**
     * 갤러리 이미지 가져오기
     */
    private fun searchImage() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val sortType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        } else {
            null
        }

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortType
        )

        cursor?.let {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            items.clear()

            while (it.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
                items.add(GalleryInfo(contentUri, it.getString(1)))
            }

            adapter?.replaceAll(items)

            it.close()
        }
    }
}