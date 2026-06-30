package com.example.test_1_project

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.test_1_project.ui.theme.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen() {
    val auth = Firebase.auth
    val db = Firebase.firestore
    val user = auth.currentUser
    
    var orderHistory by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(user?.uid) {
        val listener = if (user != null) {
            db.collection("orders")
                .whereEqualTo("userId", user.uid)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("ProfileScreen", "Error listening to orders", e)
                        errorMessage = e.message
                        isLoading = false
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        orderHistory = snapshot.documents.mapNotNull { it.data }
                            .sortedByDescending { it["timestamp"] as? com.google.firebase.Timestamp }
                        Log.d("ProfileScreen", "Updated: Found ${orderHistory.size} orders")
                    }
                    isLoading = false
                }
        } else {
            isLoading = false
            null
        }
        
        onDispose {
            listener?.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User Profile Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Chocolate.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left column - User info with modern design
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Avatar with glow effect
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                androidx.compose.ui.graphics.Brush.radialGradient(
                                    colors = listOf(
                                        Cream,
                                        Cream.copy(alpha = 0.8f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(45.dp),
                            tint = DarkBrown
                        )
                    }

                    // User name with gradient effect
                    Text(
                        text = user?.displayName ?: "Coffee Lover",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Cream,
                        letterSpacing = 0.5.sp
                    )

                    // Email with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = Tan,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = (user?.email ?: "No email available").take(20) + if ((user?.email?.length ?: 0) > 20) "..." else "",
                            fontSize = 11.sp,
                            color = Tan.copy(alpha = 0.8f)
                        )
                    }

                    // Modern logout button with icon
                    Button(
                        onClick = {
                            auth.signOut()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFFF6B6B)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFFF6B6B).copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Logout", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }

                // Right column - Order stats with modern design
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Main Stats Card with gradient-like effect
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Cream.copy(alpha = 0.25f),
                                        Cream.copy(alpha = 0.1f)
                                    )
                                ),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Animated-like icon container
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        androidx.compose.ui.graphics.Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFFFF6B6B),
                                                Color(0xFFEE5A5A)
                                            )
                                        ),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Large order count
                            Text(
                                text = "${orderHistory.size}",
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Cream,
                                letterSpacing = (-0.5).sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text = if (orderHistory.size == 1) "Order Placed" else "Orders Placed",
                                fontSize = 13.sp,
                                color = Tan,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // Achievement Badge
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                orderHistory.size >= 10 -> Color(0xFFFFD700).copy(alpha = 0.3f) // Gold
                                orderHistory.size >= 8 -> Color(0xFFE5E4E2).copy(alpha = 0.3f) // Platinum
                                orderHistory.size >= 5 -> Color(0xFFC0C0C0).copy(alpha = 0.3f) // Silver
                                orderHistory.size >= 3 -> Color(0xFFCD7F32).copy(alpha = 0.3f) // Bronze
                                orderHistory.size >= 1 -> Tan.copy(alpha = 0.2f)
                                else -> Color.White.copy(alpha = 0.1f)
                            }
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            when {
                                orderHistory.size >= 10 -> Color(0xFFFFD700).copy(alpha = 0.5f)
                                orderHistory.size >= 8 -> Color(0xFFE5E4E2).copy(alpha = 0.5f)
                                orderHistory.size >= 5 -> Color(0xFFC0C0C0).copy(alpha = 0.5f)
                                orderHistory.size >= 3 -> Color(0xFFCD7F32).copy(alpha = 0.5f)
                                orderHistory.size >= 1 -> Tan.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                when {
                                    orderHistory.size >= 10 -> Icons.Default.Star
                                    orderHistory.size >= 8 -> Icons.Default.WorkspacePremium
                                    orderHistory.size >= 5 -> Icons.Default.EmojiEvents
                                    orderHistory.size >= 3 -> Icons.Default.MilitaryTech
                                    orderHistory.size >= 1 -> Icons.Default.Coffee
                                    else -> Icons.Default.Lock
                                },
                                contentDescription = null,
                                tint = when {
                                    orderHistory.size >= 10 -> Color(0xFFFFD700)
                                    orderHistory.size >= 8 -> Color(0xFFE5E4E2)
                                    orderHistory.size >= 5 -> Color(0xFFC0C0C0)
                                    orderHistory.size >= 3 -> Color(0xFFCD7F32)
                                    orderHistory.size >= 1 -> Tan
                                    else -> Tan.copy(alpha = 0.5f)
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    orderHistory.size >= 10 -> "Coffee Master ☕"
                                    orderHistory.size >= 8 -> "Connoisseur"
                                    orderHistory.size >= 5 -> "Enthusiast"
                                    orderHistory.size >= 3 -> "Coffee Explorer"
                                    orderHistory.size >= 1 -> "Coffee Novice"
                                    else -> "Start Journey"
                                },
                                fontSize = 11.sp,
                                color = Cream,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Progress to next level
                    if (orderHistory.size < 10) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${10 - orderHistory.size} more to Coffee Master",
                                fontSize = 10.sp,
                                color = Tan.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { orderHistory.size.toFloat() / 10f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = Cream,
                                trackColor = Tan.copy(alpha = 0.3f),
                                strokeCap = StrokeCap.Round,
                                drawStopIndicator = {}
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Order History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Cream,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Cream)
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text("Error: $errorMessage", color = Color.Red, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else if (orderHistory.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Tan, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No orders placed yet", color = Tan)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orderHistory) { order ->
                    OrderHistoryItem(order)
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(order: Map<String, Any>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${order["orderId"] ?: "N/A"}",
                    fontWeight = FontWeight.Bold,
                    color = Cream
                )
                Text(
                    text = "$${String.format("%.2f", order["totalPrice"] ?: 0.0)}",
                    fontWeight = FontWeight.Bold,
                    color = Cream,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            
            val itemsList = order["items"] as? List<Map<String, Any>>
            val itemsDescription = itemsList?.joinToString { 
                "${it["coffeeName"]} x${it["quantity"]}" 
            } ?: "Items details not available"
            
            Text(
                text = itemsDescription,
                color = Tan,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val timestamp = order["timestamp"] as? com.google.firebase.Timestamp
                val dateStr = timestamp?.toDate()?.toString()?.substring(0, 16) ?: "Recently"
                
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = Tan.copy(alpha = 0.6f)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle, 
                        contentDescription = null, 
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Confirmed (${order["paymentMethod"]})",
                        fontSize = 12.sp,
                        color = Tan.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
