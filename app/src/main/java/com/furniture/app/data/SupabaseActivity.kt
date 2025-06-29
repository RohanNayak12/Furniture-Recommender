package com.furniture.app.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.furniture.app.getFileFromContentUri
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupabaseActivity(val supabase: SupabaseClient, context: Context) {
    val llmActivity = LlmActivity()
    var context: Context = context
    var img_url by mutableStateOf<String>("")

    // Callback interface for upload results
    interface UploadCallback {
        fun onSuccess(url: String)
        fun onFailure(error: String)
    }

    suspend fun imageUploadByCamera(
        bytes: ByteArray,
        fileName: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = supabase.storage.from("room-image").upload(
                    path = fileName,
                    data = bytes
                ) {
                    upsert = false
                }

                val publicUrl = supabase.storage.from("room-image").publicUrl(fileName)

                withContext(Dispatchers.Main) {
                    img_url = publicUrl
                    Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                }

                Result.success(publicUrl)
            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                }
                Result.failure(e)
            }
        }
    }

    // Callback-based version for camera upload (alternative approach)
    fun imageUploadByCameraWithCallback(
        bytes: ByteArray,
        fileName: String,
        callback: UploadCallback
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = supabase.storage.from("room-image").upload(
                    path = fileName,
                    data = bytes
                ) {
                    upsert = false
                }

                val publicUrl = supabase.storage.from("room-image").publicUrl(fileName)

                withContext(Dispatchers.Main) {
                    img_url = publicUrl
                    Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                    callback.onSuccess(publicUrl)
                }
            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                    callback.onFailure(e.message ?: "Unknown error")
                }
            }
        }
    }

    suspend fun imageUploadByPhotoPicker(
        uri: Uri,
        context: Context
    ): Result<String> {
        val filename = "image_${System.currentTimeMillis()}.jpg"
        val file = getFileFromContentUri(context, uri)

        return withContext(Dispatchers.IO) {
            try {
                file?.let {
                    supabase.storage
                        .from("room-image")
                        .upload(
                            path = filename,
                            data = it.readBytes()
                        ) {
                            contentType = ContentType.Image.Any
                        }

                    val publicUrl = supabase.storage.from("room-image").publicUrl(filename)
                    Log.d("pubUrl", publicUrl)

                    withContext(Dispatchers.Main) {
                        img_url = publicUrl
                        Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                    }

                    Result.success(publicUrl)
                } ?: run {
                    val errorMsg = "Failed to read file from URI"
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                    }
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                }
                Result.failure(e)
            }
        }
    }

    // Callback-based version for photo picker (alternative approach)
    fun imageUploadByPhotoPickerWithCallback(
        uri: Uri,
        context: Context,
        callback: UploadCallback
    ) {
        val filename = "image_${System.currentTimeMillis()}.jpg"
        val file = getFileFromContentUri(context, uri)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                file?.let {
                    supabase.storage
                        .from("room-image")
                        .upload(
                            path = filename,
                            data = it.readBytes()
                        ) {
                            contentType = ContentType.Image.Any
                        }

                    val publicUrl = supabase.storage.from("room-image").publicUrl(filename)
                    Log.d("pubUrl", publicUrl)

                    withContext(Dispatchers.Main) {
                        img_url = publicUrl
                        Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                        callback.onSuccess(publicUrl)
                    }
                } ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                        callback.onFailure("Failed to read file from URI")
                    }
                }
            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_LONG).show()
                    callback.onFailure(e.message ?: "Unknown error")
                }
            }
        }
    }

    fun getUrl(): String {
        return img_url
    }
}