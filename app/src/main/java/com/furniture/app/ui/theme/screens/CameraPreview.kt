package com.furniture.app.ui.theme.screens

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.furniture.app.R
import com.furniture.app.data.SupabaseActivity
import io.github.jan.supabase.SupabaseClient

@Composable
fun CameraPreview(supabase: SupabaseClient,context: Context) {
    val lifecycleOwner= LocalLifecycleOwner.current
    val context= LocalContext.current
    val cameraController= remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    LaunchedEffect(cameraController) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Take Photo")},
                icon = { Icon(painter = painterResource(R.drawable.photo_camera_icon), contentDescription = "Take Photo") },
                onClick = {
                    val mainExecutor= ContextCompat.getMainExecutor(context)
                    cameraController.takePicture(mainExecutor,object : ImageCapture.OnImageCapturedCallback(){

                        override fun onCaptureSuccess(image: ImageProxy) {
                            try {
                                val buffer = image.planes[0].buffer
                                val bytes = ByteArray(buffer.remaining())
                                buffer.get(bytes)

                                val fileName = "image_${System.currentTimeMillis()}.jpg"
                                val supabaseActivity= SupabaseActivity(supabase,context)
                                //supabaseActivity.imageUploadByCamera(bytes,fileName)
                                supabaseActivity.imageUploadByCameraWithCallback(
                                    bytes = bytes,
                                    fileName = fileName,
                                    callback = object : SupabaseActivity.UploadCallback {
                                        override fun onSuccess(url: String) {
                                            Log.d("Camera", "Upload successful: $url")
                                            // Handle success - maybe store URL somewhere
                                        }

                                        override fun onFailure(error: String) {
                                            Log.e("Camera", "Upload failed: $error")
                                            // Handle failure
                                        }
                                    }
                                )

                            } catch (e: Exception) {
                                Log.e("Camera", "Failed to process image", e)
                            } finally {
                                image.close()
                            }
                        }
                        override fun onError(exception: ImageCaptureException) {}
                    })
                }
            )

        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { context ->
                PreviewView(context).apply {
                    //layoutParams= LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
                    setBackgroundColor(android.graphics.Color.BLUE)
                    scaleType= PreviewView.ScaleType.FILL_CENTER
                }.also { previewView ->
                    previewView.controller=cameraController
                    //cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
    }
}