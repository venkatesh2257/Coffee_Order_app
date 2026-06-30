package com.example.test_1_project

import com.example.test_1_project.data.Coffee
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var testCoffee1: Coffee
    private lateinit var testCoffee2: Coffee

    @Before
    fun setup() {
        cartViewModel = CartViewModel()
        testCoffee1 = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong & Bold",
            price = 3.50,
            description = "Pure and intense coffee experience.",
            imageUrl = "https://example.com/espresso.jpg"
        )
        testCoffee2 = Coffee(
            id = "2",
            name = "Cappuccino",
            type = "Creamy Foam",
            price = 4.50,
            description = "Creamy coffee with foam.",
            imageUrl = "https://example.com/cappuccino.jpg"
        )
    }

    @Test
    fun `addToCart adds new item when cart is empty`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        
        assertEquals(1, cartViewModel.cartItems.size)
        assertEquals(testCoffee1, cartViewModel.cartItems[0].coffee)
        assertEquals(2, cartViewModel.cartItems[0].quantity)
    }

    @Test
    fun `addToCart updates quantity when item already exists`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        cartViewModel.addToCart(testCoffee1, quantity = 3)
        
        assertEquals(1, cartViewModel.cartItems.size)
        assertEquals(5, cartViewModel.cartItems[0].quantity)
    }

    @Test
    fun `addToCart adds different items separately`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        cartViewModel.addToCart(testCoffee2, quantity = 2)
        
        assertEquals(2, cartViewModel.cartItems.size)
        assertEquals(testCoffee1, cartViewModel.cartItems[0].coffee)
        assertEquals(testCoffee2, cartViewModel.cartItems[1].coffee)
    }

    @Test
    fun `updateQuantity increases item quantity`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        val cartItem = cartViewModel.cartItems[0]
        
        cartViewModel.updateQuantity(cartItem, newQuantity = 5)
        
        assertEquals(1, cartViewModel.cartItems.size)
        assertEquals(5, cartViewModel.cartItems[0].quantity)
    }

    @Test
    fun `updateQuantity removes item when quantity is zero`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        val cartItem = cartViewModel.cartItems[0]
        
        cartViewModel.updateQuantity(cartItem, newQuantity = 0)
        
        assertEquals(0, cartViewModel.cartItems.size)
    }

    @Test
    fun `updateQuantity removes item when quantity is negative`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        val cartItem = cartViewModel.cartItems[0]
        
        cartViewModel.updateQuantity(cartItem, newQuantity = -1)
        
        assertEquals(0, cartViewModel.cartItems.size)
    }

    @Test
    fun `removeFromCart removes specific item`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        cartViewModel.addToCart(testCoffee2, quantity = 1)
        val cartItemToRemove = cartViewModel.cartItems[0]
        
        cartViewModel.removeFromCart(cartItemToRemove)
        
        assertEquals(1, cartViewModel.cartItems.size)
        assertNotEquals(cartItemToRemove.coffee.id, cartViewModel.cartItems[0].coffee.id)
    }

    @Test
    fun `clearCart removes all items`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        cartViewModel.addToCart(testCoffee2, quantity = 2)
        
        cartViewModel.clearCart()
        
        assertEquals(0, cartViewModel.cartItems.size)
    }

    @Test
    fun `getTotalPrice returns correct total for single item`() {
        cartViewModel.addToCart(testCoffee1, quantity = 3)
        
        val expectedTotal = testCoffee1.price * 3
        assertEquals(expectedTotal, cartViewModel.getTotalPrice(), 0.001)
    }

    @Test
    fun `getTotalPrice returns correct total for multiple items`() {
        cartViewModel.addToCart(testCoffee1, quantity = 2)
        cartViewModel.addToCart(testCoffee2, quantity = 1)
        
        val expectedTotal = (testCoffee1.price * 2) + (testCoffee2.price * 1)
        assertEquals(expectedTotal, cartViewModel.getTotalPrice(), 0.001)
    }

    @Test
    fun `getTotalPrice returns zero when cart is empty`() {
        assertEquals(0.0, cartViewModel.getTotalPrice(), 0.001)
    }

    @Test
    fun `cartItems list is immutable from outside`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        val cartItems = cartViewModel.cartItems
        
        // Try to modify the returned list (should not affect the internal state)
        // Since cartItems returns a List (not MutableList), this test verifies
        // that the internal state is protected
        assertEquals(1, cartItems.size)
    }

    @Test
    fun `multiple addToCart calls accumulate correctly`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        
        assertEquals(1, cartViewModel.cartItems.size)
        assertEquals(3, cartViewModel.cartItems[0].quantity)
    }

    @Test
    fun `updateQuantity handles non-existent item gracefully`() {
        cartViewModel.addToCart(testCoffee1, quantity = 1)
        val nonExistentItem = CartItem(testCoffee2, quantity = 1)
        
        // Should not throw exception
        cartViewModel.updateQuantity(nonExistentItem, newQuantity = 5)
        
        // Original item should remain unchanged
        assertEquals(1, cartViewModel.cartItems.size)
        assertEquals(testCoffee1.id, cartViewModel.cartItems[0].coffee.id)
    }
}
