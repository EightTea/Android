package com.bside.five.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object CommonUtil {

    fun shareGoogleLink(context: Context, uri: Uri, message: String?) {
        Intent(Intent.ACTION_SEND).run {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(this, null))
        }
    }

    fun checkStoragePermission(activity: AppCompatActivity): Boolean {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.OnRequestPermissionsResultCallback { requestCode, permissions, grantResults ->
                if (requestCode == 100) {
                    if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                        // Permission granted
                        Toast.makeText(activity, "Permission granted", Toast.LENGTH_LONG).show()
                    } else {
                        // Permission denied or canceled
                        Toast.makeText(activity, "Permission denied or canceled", Toast.LENGTH_LONG).show()
                    }
                }
            }

            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )

            return false
        }

        return true
    }

    fun convertFormat(covertData: String?, convertFormat: String): String {
        return try {
            covertData?.let {
                var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                format.parse(it)?.let {
                    format = SimpleDateFormat(convertFormat, Locale.getDefault())
                    format.format(it)
                }
            } ?: ""
        } catch (e: Exception) {
            covertData ?: ""
        }
    }

    fun convertDateFormat(
        formatDate: String?,
        oriFormat: String?,
        convertFormat: String?,
        locale: Locale?
    ): String? {
        if (formatDate.isNullOrEmpty()) {
            return ""
        }

        var result = ""
        try {
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)
            val date = format.parse(formatDate)
            format = SimpleDateFormat(convertFormat, locale)
            result = format.format(date)
        } catch (e: Exception) {
            return formatDate
        }
        return result
    }
}