@file:OptIn(ExperimentalPermissionsApi::class)

package com.furniture.app.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.furniture.app.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.jan.supabase.SupabaseClient

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(supabase: SupabaseClient) {
    val cameraPermissionState: PermissionState= rememberPermissionState(android.Manifest.permission.CAMERA)

    TempScreen1(
        cameraPermissionState.status.isGranted,
        cameraPermissionState,
        supabase
        )

}

@Composable
fun TempScreen1(
    permission: Boolean,
    onRequest: PermissionState,
    supabase: SupabaseClient
){
    var context= LocalContext.current
    if (permission) CameraPreview(supabase, context)
    else NoCameraPerm(onRequest)
}

@Composable
fun NoCameraPerm(permissionState: PermissionState){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Please Grant Camera Permission")
        Button(onClick = {permissionState.launchPermissionRequest()}) {
            Icon(painter = painterResource(R.drawable.photo_camera_icon), contentDescription = null)
            Text("Grant Permission")
        }
    }
}