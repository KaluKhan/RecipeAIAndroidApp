package com.sankhlatraders.recipeai.ui.screens.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sankhlatraders.recipeai.ui.theme.AccentBlue
import com.sankhlatraders.recipeai.ui.theme.AccentGreen
import com.sankhlatraders.recipeai.ui.theme.BrandGold
import com.sankhlatraders.recipeai.ui.theme.BrandOrange
import com.sankhlatraders.recipeai.ui.theme.DarkBackground
import com.sankhlatraders.recipeai.ui.theme.DarkSurfaceVariant
import com.sankhlatraders.recipeai.ui.theme.Divider
import com.sankhlatraders.recipeai.ui.theme.TextHint
import com.sankhlatraders.recipeai.ui.theme.TextSecondary

@Composable
fun ContactUsScreen(onBackClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        //Header
        ContactHeader(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            //Hero illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    BrandOrange.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Envelop emoji", fontSize = 52.sp)
                }
            }
            Text(
                text = "Get in Touch",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "We'd love to hear from you. Send us a message and we'll get back to you as soon as possible.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            //Contact info cards
            ContactInfoRow()

            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = Divider)
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Send us a message",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (submitted) {
                SuccessBanner()
            } else {
                ContactForm(
                    name = name,
                    email = email,
                    message = message,
                    onNameChange = { name = it },
                    onEmailChange = { email = it },
                    onMessageChange = { message = it },
                    onSubmit = {
                        if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                            submitted = true
                        }
                    })
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ContactHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF8E2DE2),
                        Color(0xFF1A0800),
                        DarkBackground
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Contact Us",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ContactInfoRow() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ContactInfoCard(
            icon = Icons.Rounded.Email,
            title = "Email",
            value = "hello@recipeai.com",
            iconColor = BrandOrange
        )
        ContactInfoCard(
            icon = Icons.Rounded.Phone,
            title = "Phone",
            value = "+1 (800) RECIPE-1",
            iconColor = AccentBlue
        )
        ContactInfoCard(
            icon = Icons.Rounded.LocationOn,
            title = "Office",
            value = "123 Culinary Street, San Francisco, CA",
            iconColor = AccentGreen
        )
    }
}

@Composable
private fun ContactInfoCard(icon: ImageVector, title: String, value: String, iconColor: Color) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ContactForm(
    name: String,
    email: String,
    message: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandOrange,
        unfocusedBorderColor = Color(0xFF333333),
        focusedContainerColor = DarkSurfaceVariant,
        unfocusedContainerColor = DarkSurfaceVariant,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = BrandOrange,
        unfocusedLabelColor = TextHint
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(text = "Your Name") },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            singleLine = true
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(text = "Email Address") },
            leadingIcon = {
                Icon(
                    Icons.Rounded.Email,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            singleLine = true
        )
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text(text = "Message") },
            leadingIcon = {
                Icon(
                    Icons.Rounded.ChatBubbleOutline,
                    contentDescription = null,
                    tint = BrandOrange
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            maxLines = 6,
            minLines = 4
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                BrandOrange,
                                BrandGold
                            )
                        ), shape = RoundedCornerShape(14.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Send Message",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SuccessBanner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AccentGreen.copy(alpha = 0.12f))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "celebration emoji", fontSize = 48.sp)
        Text(
            text = "Message Sent!",
            style = MaterialTheme.typography.titleLarge,
            color = AccentGreen,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Thank you for reaching out. We'll get back to you within 24 hours.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            lineHeight = 22.sp
        )
    }
}