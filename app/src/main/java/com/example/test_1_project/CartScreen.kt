package com.example.test_1_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.test_1_project.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CartScreen(cartViewModel: CartViewModel, onNavigateToHome: () -> Unit = {}) {
    val cartItems = cartViewModel.cartItems
    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("Card") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var paymentError by remember { mutableStateOf("") }
    var placedOrderId by remember { mutableStateOf<String?>(null) }
    var isPlacingOrder by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()

    if (placedOrderId != null) {
        OrderSuccessScreen(orderId = placedOrderId!!) {
            placedOrderId = null
            onNavigateToHome()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Your Cart",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Cream,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Your cart is thirsty! ☕",
                        color = Cream,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Add some delicious coffee to your cart and treat yourself today.",
                        color = Cream.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(item, cartViewModel)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Payment Method", color = Cream, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentMethod == "Card",
                            onClick = { paymentMethod = "Card" },
                            colors = RadioButtonDefaults.colors(selectedColor = Cream, unselectedColor = Tan)
                        )
                        Text("Credit/Debit Card", color = Cream, modifier = Modifier.selectable(selected = paymentMethod == "Card", onClick = { paymentMethod = "Card" }))
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        RadioButton(
                            selected = paymentMethod == "COD",
                            onClick = { paymentMethod = "COD" },
                            colors = RadioButtonDefaults.colors(selectedColor = Cream, unselectedColor = Tan)
                        )
                        Text("Cash on Delivery", color = Cream, modifier = Modifier.selectable(selected = paymentMethod == "COD", onClick = { paymentMethod = "COD" }))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", color = Cream, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$${String.format("%.2f", cartViewModel.getTotalPrice())}",
                            color = Cream,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showPaymentDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Cream, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Place Order", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { if (!isPlacingOrder) showPaymentDialog = false },
            containerColor = DarkBrown,
            titleContentColor = Cream,
            textContentColor = Tan,
            title = { Text(if (paymentMethod == "Card") "Payment Details" else "Confirm Order") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isPlacingOrder) {
                        Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Cream)
                        }
                    } else {
                        if (paymentMethod == "Card") {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 16) cardNumber = it },
                                label = { Text("Card Number (16 digits)", color = Tan) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Cream,
                                    unfocusedTextColor = Tan,
                                    focusedBorderColor = Chocolate,
                                    unfocusedBorderColor = Brown
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = expiry,
                                    onValueChange = { expiry = it },
                                    label = { Text("MM/YY", color = Tan) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Cream,
                                        unfocusedTextColor = Tan,
                                        focusedBorderColor = Chocolate,
                                        unfocusedBorderColor = Brown
                                    )
                                )
                                OutlinedTextField(
                                    value = cvv,
                                    onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 3) cvv = it },
                                    label = { Text("CVV", color = Tan) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Cream,
                                        unfocusedTextColor = Tan,
                                        focusedBorderColor = Chocolate,
                                        unfocusedBorderColor = Brown
                                    )
                                )
                            }
                        } else {
                            Text("You have selected Cash on Delivery. Please confirm your order for $${String.format("%.2f", cartViewModel.getTotalPrice())}.", color = Tan)
                        }
                        
                        if (paymentError.isNotEmpty()) {
                            Text(paymentError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                if (!isPlacingOrder) {
                    Button(
                        onClick = {
                            if (paymentMethod == "Card" && (cardNumber.length != 16 || cvv.length != 3)) {
                                paymentError = "Please enter valid payment details"
                            } else {
                                scope.launch {
                                    isPlacingOrder = true
                                    paymentError = ""
                                    val orderId = cartViewModel.placeOrder(paymentMethod) { error ->
                                        paymentError = error
                                    }
                                    isPlacingOrder = false
                                    if (orderId != null) {
                                        placedOrderId = orderId
                                        showPaymentDialog = false
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Chocolate, contentColor = Cream)
                    ) {
                        Text(if (paymentMethod == "Card") "Pay Now" else "Confirm")
                    }
                }
            },
            dismissButton = {
                if (!isPlacingOrder) {
                    TextButton(onClick = { showPaymentDialog = false }) {
                        Text("Cancel", color = Tan)
                    }
                }
            }
        )
    }
}

@Composable
fun OrderSuccessScreen(orderId: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBrown)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(15.dp, CircleShape),
                shape = CircleShape,
                color = DarkBrown, // Blends perfectly with the background
                border = BorderStroke(2.dp, Brush.verticalGradient(listOf(Tan, Color(0xFFD4AF37))))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Order Placed Successfully!",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Cream,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "🎉☕",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Order Number: $orderId",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Tan
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Your coffee is being prepared with love! ❤️",
                fontSize = 15.sp,
                color = Cream.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Chocolate),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Back to Menu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.coffee.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(item.coffee.name, color = Cream, fontWeight = FontWeight.Bold)
                Text("$${String.format("%.2f", item.coffee.price)}", color = Cream.copy(alpha = 0.7f))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (item.quantity > 1) cartViewModel.updateQuantity(item, item.quantity - 1) }) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = Cream)
                }
                Text(item.quantity.toString(), color = Cream, fontWeight = FontWeight.Bold)
                IconButton(onClick = { cartViewModel.updateQuantity(item, item.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Cream)
                }
                IconButton(onClick = { cartViewModel.removeFromCart(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.7f))
                }
            }
        }
    }
}
