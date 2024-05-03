package com.solidecoteknologi.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.solidecoteknologi.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    // Get the application's internal storage directory
    val wrapper = ContextWrapper(context)
    var file = wrapper.getDir("images", Context.MODE_PRIVATE)
    file = File(file, "${System.currentTimeMillis()}.jpg")

    try {
        // Convert the Bitmap to a file
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        // Return the Uri for the file
        return Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

class UriToFile(context: Context) {
    private val applicationContext = context.applicationContext

    fun getImageBody(imageUri: Uri): File? {
        try {
            val parcelFileDescriptor = applicationContext.contentResolver.openFileDescriptor(
                imageUri,
                "r",
                null
            ) ?: return null // Return null if file descriptor couldn't be obtained

            val fileName = applicationContext.contentResolver.getFileName(imageUri)
            val file = File(applicationContext.cacheDir, fileName)

            parcelFileDescriptor.use { pfd ->
                FileInputStream(pfd.fileDescriptor).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
            return file
        } catch (e: Exception) {
            // Handle any exceptions, log them, or return null
            e.printStackTrace()
            return null
        }
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(
            uri, null, null,
            null, null
        )
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }
}

fun loadImage(context: Context, img: String, imageView: ImageView) {
    try {
        Glide.with(context).clear(imageView)
        Glide.with(context)
            .load("https://solidecoteknologi.com/$img")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.profile_icon)
            .skipMemoryCache(true)
            .into(imageView)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
