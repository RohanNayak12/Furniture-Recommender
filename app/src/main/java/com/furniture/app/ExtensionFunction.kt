package com.furniture.app

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File

fun Uri.toByteArray(context: Context): ByteArray? {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            inputStream.readBytes()
        }
    } catch (e: Exception) {
        Log.e("URI_CONVERSION", "Failed to convert URI to ByteArray", e)
        null
    }
}

// Get the actual file from content URI
fun getFileFromContentUri(context: Context, contentUri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(contentUri)
        val tempFile = File.createTempFile("upload", ".tmp", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        tempFile
    } catch (e: Exception) {
        null
    }
}
