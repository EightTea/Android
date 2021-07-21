package com.bside.five.util

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream

object ImageUtil {

    const val TEMP_FILE_NAME = "temp_qr.jpg"
    const val DELETE_PERMISSION_REQUEST = 0x1033

    fun insertImage(context: Context, bitmap: Bitmap, filename: String, fileType: String): Uri {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, fileType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val item = context.contentResolver.insert(collection, values)!!

        context.contentResolver.openFileDescriptor(item, "w").use {
            FileOutputStream(it!!.fileDescriptor).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            }
        }

        values.clear()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(item, values, null, null)
        }

        return item
    }

    fun deleteImage(context: Context, fileName: String) {
        val deleteList: ArrayList<Uri> = ArrayList()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )

        val selection = "${MediaStore.Video.Media.DISPLAY_NAME} LIKE ?"

        val selectionArgs = arrayOf(
            "%${fileName}%"
        )

        context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                deleteList.add(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString()))
            }
        }

        deleteList.forEach {
            deleteImage(context, it)
        }
    }

    fun deleteImage(context: Context, uri: Uri) {
        if (Uri.EMPTY == uri) {
            Log.d("ImageUtil()", "deleteImage Uri.EMPTY")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                context.contentResolver.delete(uri, null, null)
            } catch (e: RecoverableSecurityException) {
                /**
                 * Android Q 이상에서 파일 삭제시에만 권한 요청과 함께 삭제 다이얼로그를 띄우는 정책이 있어 해당작업을 수행해야함
                 * DELETE_PERMISSION_REQUEST 권한을 받으면 onActivityResult() 호출되며, 다시 삭제 요청해야함
                 **/
                val intentSender = e.userAction.actionIntent.intentSender
                intentSender?.let {
                    (context as AppCompatActivity).startIntentSenderForResult(
                        intentSender,
                        DELETE_PERMISSION_REQUEST,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                }
            }
        } else {
            context.contentResolver.delete(uri, null, null)
        }
    }
}