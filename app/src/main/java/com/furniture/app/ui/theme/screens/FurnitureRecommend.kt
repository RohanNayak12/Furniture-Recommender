package com.furniture.app.ui.theme.screens

// Required imports:
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.furniture.app.data.FurnitureItem
import kotlinx.serialization.json.Json


@Composable
fun DesignPreviewScreen(
    onBackClick: () -> Unit = {},
    onViewItemClick: (String) -> Unit = {},
    json: String
) {
    val furnitureItems: List<FurnitureItem> = emptyList()
    try {
        val furnitureItems: List<FurnitureItem> = Json.decodeFromString<List<FurnitureItem>>(json)
    }
    catch (e: Exception){
        Log.d("EEE","$e")
    }

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
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCas5pKl4Fz4yKxtnW1XaJhjUnpPWH__5SuftwgBsrTB8cNIvf4sm0OAwGmrZ3iXQo0jyhkuqkrMfI4sBElNTTE-gP6QRVL7WMgNPyYAqxCKtV7PnRFJnz-s7K8e6SIF8b_8YxIyMAZl5SaxyCh_mThzVEfSgZOCVNxsygZ9132yPZdY1jHIPrTlLFCOzHVAsY2D-V5OBxHMD15rze3gsEuq4hNEzcFtLZ8LyIJ6eSs5hTkMOt-I9UDYILfHaT7du7RwGGeqEKK7Dk",
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
            items(furnitureItems) { item ->
                FurnitureItemRow(
                    item = item,
                    onViewClick = { onViewItemClick(item.name!!) }
                )
            }
        }

        // Bottom spacer
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun FurnitureItemRow(
    item: FurnitureItem,
    onViewClick: () -> Unit
) {
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
            /*
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            ) */

            Spacer(modifier = Modifier.width(16.dp))

            // Item Details
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name!!,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.description!!,
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
            onClick = onViewClick,
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