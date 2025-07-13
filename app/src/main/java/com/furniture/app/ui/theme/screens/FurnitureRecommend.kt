package com.furniture.app.ui.theme.screens

// Required imports:
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.furniture.app.data.FurnitureItemFromLLM
import com.furniture.app.data.IkeaAPI
import com.furniture.app.database.HelperClass1
import com.furniture.app.database.IkeaApiSerialization
import com.furniture.app.database.Routes
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@Composable
fun DesignPreviewScreen(
    onBackClick: () -> Unit = {},
    onViewItemClick: (String) -> Unit = {},
    json: String,
    imgUrl: String,
    supabase: SupabaseClient,
    navcontroller: NavHostController,
    helperClass1: HelperClass1,
    ) {

    val ikeaAPI= IkeaAPI()
    var eee by remember { mutableStateOf(false) }
    Log.d("FurnitureRecommendScreen",json)
    var furnitureItems by remember { mutableStateOf<List<FurnitureItemFromLLM>>(emptyList()) }
    try {
        furnitureItems = Json.decodeFromString<List<FurnitureItemFromLLM>>(json)
        Log.d("tetetete",furnitureItems[0].name.toString())
    }
    catch (e: Exception){
        eee=true
        Log.d("EEE","$e")
    }
    if(eee){
        eee=false
        val routes=Routes()
        navcontroller.navigate(routes.designAssistant)
    }
    Log.d("from furniture recommend screen","${furnitureItems.get(0).name}")

    val coroutineScope= rememberCoroutineScope()
    var finalList:List<IkeaApiSerialization> by remember { mutableStateOf(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(furnitureItems) {
        if(furnitureItems.isNotEmpty()){
            isLoading=true
            val results = mutableListOf<IkeaApiSerialization>()

            for (item in furnitureItems) {
                try {
                    val res = ikeaAPI.apiCall(keyword = item.name!!)
                    if (res.isNotEmpty()) {
                        results.add(res[0])
                        Log.d("NNNN", res[0].name)
                    }
                } catch (e: Exception) {
                    Log.d("AAAA", "$e")
                }
            }
            finalList = results
            isLoading = false
            Log.d("GGG", "Final list size: ${finalList.size}")
        }
    }
    val n=finalList.size
    Log.d("GGG","$n")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111418))
            .systemBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Design Preview",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Spacer to balance the back button
            Spacer(modifier = Modifier.width(48.dp))
        }

        // Hero Image
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = "Design Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Recommended Furniture Title
        Text(
            text = "Recommended Furniture",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // Furniture List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(finalList) { item ->
                FurnitureItemRow(
                    item = item,
                    onViewClick = { onViewItemClick(item.name) }
                )
            }
        }

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun FurnitureItemRow(
    item: IkeaApiSerialization,
    onViewClick: () -> Unit
) {
    val context=LocalContext.current
    Log.d("JJJJ",item.name)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item Image

            AsyncImage(
                model = item.image,
                contentDescription = item.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Item Details
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.typeName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val s= item.price.currentPrice.toString()
                Text(
                    text = s,
                    color = Color(0xFF9CAABA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // View Button
        Button(
            onClick = {
                Log.d("OOOO","${item.url}")
                val uriIntent= Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(item.url)
                )
                context.startActivity(uriIntent)
            },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF283039)
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
        ) {
            Text(
                text = "View",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun FurnitureItemRowPreview(){}



// Usage example:
/*
@Preview
@Composable
fun DesignPreviewScreenPreview() {
    DesignPreviewScreen(
        onBackClick = { /* Handle back navigation */ },
        onViewItemClick = { itemId ->
            /* Handle view item click with itemId */
        }
    )
}

 */

fun ikeaNetworkCall(coroutineScope: CoroutineScope,ikeaAPI: IkeaAPI,keyword: String):IkeaApiSerialization?{
    var list:IkeaApiSerialization? by mutableStateOf(null)
    coroutineScope.launch {
        try {
            var res:List<IkeaApiSerialization> =ikeaAPI.apiCall(keyword = keyword)
            list=res.get(0)
            Log.d("NNNN",list!!.name)
        }
        catch (e: Exception){
            Log.d("AAAA","$e")
        }
    }
    //list.
    //Log.d("NNNN",list!!.name)
    return list
}