package com.furniture.app.ui.theme.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.furniture.app.R
import com.furniture.app.data.LlmActivity
import com.furniture.app.data.SupabaseActivity
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val DarkBackground = Color(0xFF111418)
private val SecondaryBackground = Color(0xFF1B2127)
private val TertiaryBackground = Color(0xFF283039)
private val PrimaryBlue = Color(0xFF3490F3)
private val LightBlue = Color(0xFF5BA7F7) // Lighter blue shade for selected state
private val TextWhite = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignAssistantScreen(supabse: SupabaseClient) {

    val context=LocalContext.current
    var imageurl by remember { mutableStateOf<String>("") }
    var choice by remember { mutableStateOf<String>("") }
    var choiceClicked by remember { mutableStateOf<Boolean>(false) }
    val supabaseActivity=SupabaseActivity(supabse, context)
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var galleryClicked by remember {mutableStateOf(false)}
    var camerClicked by remember { mutableStateOf(false) }
    var isImageUploading by remember { mutableStateOf(false) }
    var isImageUploaded by remember { mutableStateOf(false) }
    var llmResponse by remember { mutableStateOf("") }
    var furnitureRecommendScreen by remember { mutableStateOf(true) }
    val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected image URI: $uri")
            isImageUploading = true
            isImageUploaded = false
            CoroutineScope(Dispatchers.IO).launch {
                supabaseActivity.imageUploadByPhotoPicker(uri,context)
                // Poll for URL availability instead of fixed delay
                var attempts = 0
                while (attempts < 30) { // Try for up to 30 seconds
                    kotlinx.coroutines.delay(1000) // Check every second
                    val url = supabaseActivity.getUrl()
                    if (url != null) {
                        Log.d("PhotoPicker", "Upload completed: $url")
                        withContext(Dispatchers.Main) {
                            isImageUploading = false
                            isImageUploaded = true
                        }
                        break
                    }
                    attempts++
                }
                if (attempts >= 30) {
                    Log.d("PhotoPicker", "Upload timeout")
                    withContext(Dispatchers.Main) {
                        isImageUploading = false
                        Toast.makeText(context, "Upload timeout. Please try again.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Log.d("PhotoPicker", "No image selected")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Design Assistant",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            actions = {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextWhite
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBackground
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Capture your space to get personalized design suggestions.",
                color = TextWhite,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        Log.d("BB","BBBBB")
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.size(40.dp),
                    containerColor = SecondaryBackground,
                    contentColor = TextWhite
                ) {
                    Icon(
                        painter = painterResource(R.drawable.photo_library_icon),
                        contentDescription = "Gallery",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                FloatingActionButton(
                    onClick = { camerClicked=true },
                    modifier = Modifier.size(64.dp),
                    containerColor = SecondaryBackground,
                    contentColor = TextWhite
                ) {
                    Icon(
                        painter = painterResource(R.drawable.photo_camera_icon),
                        contentDescription = "Camera",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                FloatingActionButton(
                    onClick = { /* Handle options */ },
                    modifier = Modifier.size(40.dp),
                    containerColor = SecondaryBackground,
                    contentColor = TextWhite
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "Choose your style",
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        choice = "Modern"
                    },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (choice == "Modern") LightBlue else TertiaryBackground,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Modern",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {
                        choice = "Cozy"
                    },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (choice == "Cozy") LightBlue else TertiaryBackground,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Cozy",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {
                        choice = "Minimal"
                    },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (choice == "Minimal") LightBlue else TertiaryBackground,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Minimal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Text(
                text = "Previous Designs",
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            //Previous Designs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SecondaryBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.image_icon),
                        contentDescription = "No designs",
                        tint = TextWhite.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "No previous designs yet.",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Start a new design to see your projects here.",
                        color = TextWhite,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TertiaryBackground,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "New Design",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column {
            Button(
                onClick = {
                    if (isImageUploading) {
                        Toast.makeText(context, "Image is still uploading, please wait...", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (!isImageUploaded) {
                        Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    imageurl = supabaseActivity.getUrl()
                    Log.d("imgURL",imageurl)
                    if (imageurl == "") {
                        Log.d("Sug BB", "Image URL is null")
                        Toast.makeText(context, "Image upload failed, please try again", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("Sug BB", "Image URL: $imageurl")
                        val llmActivity = LlmActivity()
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = llmActivity.furnitureRecommendation(imageurl, choice)
                            Log.d("llm resp", response.toString())
                            llmResponse=response.toString()
                            if(llmResponse !=""){
                                furnitureRecommendScreen=true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = if (isImageUploading) "Uploading..." else "Get Suggestions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(DarkBackground)
            )
        }
    }

    if (camerClicked){
        CameraScreen(supabse)
        camerClicked!=camerClicked
    }
    if (galleryClicked){
        //supabaseActivity.imageUploadByPhotoPicker(photoUri!!,context)
        galleryClicked=false
    }
    if(furnitureRecommendScreen){
        DesignPreviewScreen(json = llmResponse)
        furnitureRecommendScreen=false
    }
}

@Preview(showBackground = true)
@Composable
fun DesignAssistantScreenPreview() {
    MaterialTheme {
        //DesignAssistantScreen(supabse)
    }
}