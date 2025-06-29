package com.furniture.app.ui.theme.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color scheme
private val DarkBackground = Color(0xFF121417)
private val SecondaryBackground = Color(0xFF1D2125)
private val BorderColor = Color(0xFF3F4750)
private val TextGray = Color(0xFFA1ABB5)
private val TextWhite = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var geminiToken by remember { mutableStateOf("") }
    var gptToken by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top bar
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Handle back */ }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBackground
            )
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // AI Tokens section
            SectionHeader(title = "AI Tokens")

            // Gemini Token input
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Gemini Token",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = geminiToken,
                    onValueChange = { geminiToken = it },
                    placeholder = {
                        Text(
                            text = "Enter your Gemini token",
                            color = TextGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SecondaryBackground,
                        unfocusedContainerColor = SecondaryBackground,
                        focusedBorderColor = BorderColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // GPT Token input
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "GPT Token",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = gptToken,
                    onValueChange = { gptToken = it },
                    placeholder = {
                        Text(
                            text = "Enter your GPT token",
                            color = TextGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedContainerColor = SecondaryBackground,
                        unfocusedContainerColor = SecondaryBackground,
                        focusedBorderColor = BorderColor,
                        unfocusedBorderColor = BorderColor,
                        cursorColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // User Details section
            SectionHeader(title = "User Details")

            UserDetailItem(
                title = "User ID",
                subtitle = "user12345"
            )

            UserDetailItem(
                title = "Email Address",
                subtitle = "sophia.anderson@email.com"
            )
        }

        // Bottom Navigation
        //BottomNavigationBar()
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = TextWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    onClick: () -> Unit,
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = TextWhite,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Arrow",
                tint = TextWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun UserDetailItem(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = TextWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}




@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen()
    }
}