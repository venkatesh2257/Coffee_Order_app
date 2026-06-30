package com.example.test_1_project

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.test_1_project.data.Coffee
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

data class CartItem(
    val coffee: Coffee,
    val quantity: Int
)

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> = _cartItems

    fun addToCart(coffee: Coffee, quantity: Int) {
        val index = _cartItems.indexOfFirst { it.coffee.id == coffee.id }
        if (index != -1) {
            val existingItem = _cartItems[index]
            _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            _cartItems.add(CartItem(coffee, quantity))
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = _cartItems.indexOfFirst { it.coffee.id == item.coffee.id }
        if (index != -1) {
            if (newQuantity <= 0) {
                _cartItems.removeAt(index)
            } else {
                _cartItems[index] = _cartItems[index].copy(quantity = newQuantity)
            }
        }
    }

    fun removeFromCart(item: CartItem) {
        _cartItems.remove(item)
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun getTotalPrice(): Double {
        return _cartItems.sumOf { it.coffee.price * it.quantity }
    }

    suspend fun placeOrder(paymentMethod: String, onError: (String) -> Unit): String? {
        val db = Firebase.firestore
        val auth = Firebase.auth
        val userId = auth.currentUser?.uid ?: "anonymous"
        
        if (_cartItems.isEmpty()) {
            onError("Cart is empty")
            return null
        }

        try {
            // Sequential ID logic using a transaction
            val counterDoc = db.collection("metadata").document("order_counter")
            
            val nextId = db.runTransaction { transaction ->
                val snapshot = transaction.get(counterDoc)
                val currentCount = if (snapshot.exists()) snapshot.getLong("count") ?: 0 else 0
                val nextCount = currentCount + 1
                transaction.set(counterDoc, mapOf("count" to nextCount))
                
                // Format ID as coffee01, coffee02, etc.
                "coffee%02d".format(nextCount)
            }.await()

            val orderData = hashMapOf(
                "orderId" to nextId,
                "userId" to userId,
                "items" to _cartItems.map { 
                    mapOf(
                        "coffeeName" to it.coffee.name,
                        "quantity" to it.quantity,
                        "price" to it.coffee.price
                    )
                },
                "totalPrice" to getTotalPrice(),
                "paymentMethod" to paymentMethod,
                "timestamp" to com.google.firebase.Timestamp.now()
            )

            db.collection("orders").document(nextId).set(orderData).await()
            clearCart()
            return nextId
        } catch (e: Exception) {
            Log.e("CartViewModel", "Error placing order", e)
            val errorMsg = e.message ?: "Unknown error"
            
            // Helpful message for the user if it's a permission issue
            if (errorMsg.contains("permission-denied", ignoreCase = true)) {
                onError("Firebase Permission Denied. Check your Firestore Rules.")
            } else {
                onError(errorMsg)
            }
            return null
        }
    }
}
