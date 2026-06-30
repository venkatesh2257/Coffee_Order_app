package com.example.test_1_project

import com.example.test_1_project.data.Coffee
import com.example.test_1_project.data.coffeeList
import org.junit.Assert.*
import org.junit.Test

class CoffeeTest {

    @Test
    fun `Coffee data class has correct default values`() {
        val coffee = Coffee()
        
        assertEquals("", coffee.id)
        assertEquals("", coffee.name)
        assertEquals("", coffee.type)
        assertEquals(0.0, coffee.price, 0.001)
        assertEquals("", coffee.description)
        assertEquals("", coffee.imageBase64)
        assertEquals("", coffee.imageUrl)
    }

    @Test
    fun `Coffee data class stores custom values correctly`() {
        val coffee = Coffee(
            id = "test-123",
            name = "Test Coffee",
            type = "Test Type",
            price = 5.99,
            description = "Test description",
            imageBase64 = "base64string",
            imageUrl = "https://example.com/image.jpg"
        )
        
        assertEquals("test-123", coffee.id)
        assertEquals("Test Coffee", coffee.name)
        assertEquals("Test Type", coffee.type)
        assertEquals(5.99, coffee.price, 0.001)
        assertEquals("Test description", coffee.description)
        assertEquals("base64string", coffee.imageBase64)
        assertEquals("https://example.com/image.jpg", coffee.imageUrl)
    }

    @Test
    fun `Coffee data class supports copy with modifications`() {
        val originalCoffee = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50,
            description = "Strong coffee"
        )
        
        val modifiedCoffee = originalCoffee.copy(
            price = 4.50,
            name = "Espresso Large"
        )
        
        assertEquals(originalCoffee.id, modifiedCoffee.id)
        assertEquals(originalCoffee.type, modifiedCoffee.type)
        assertEquals("Espresso Large", modifiedCoffee.name)
        assertEquals(4.50, modifiedCoffee.price, 0.001)
        assertEquals(originalCoffee.description, modifiedCoffee.description)
    }

    @Test
    fun `coffeeList contains all expected coffees`() {
        assertTrue(coffeeList.isNotEmpty())
        assertEquals(10, coffeeList.size)
    }

    @Test
    fun `coffeeList contains unique ids`() {
        val ids = coffeeList.map { it.id }
        val uniqueIds = ids.toSet()
        assertEquals(ids.size, uniqueIds.size)
    }

    @Test
    fun `coffeeList contains unique names`() {
        val names = coffeeList.map { it.name }
        val uniqueNames = names.toSet()
        assertEquals(names.size, uniqueNames.size)
    }

    @Test
    fun `all coffees in coffeeList have valid prices`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.price > 0)
        }
    }

    @Test
    fun `all coffees in coffeeList have non-empty names`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.name.isNotEmpty())
        }
    }

    @Test
    fun `all coffees in coffeeList have non-empty types`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.type.isNotEmpty())
        }
    }

    @Test
    fun `all coffees in coffeeList have valid image URLs`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.imageUrl.isNotEmpty())
            assertTrue(coffee.imageUrl.startsWith("https://"))
        }
    }

    @Test
    fun `coffeeList contains Espresso with correct properties`() {
        val espresso = coffeeList.find { it.name == "Espresso" }
        
        assertNotNull(espresso)
        assertEquals("1", espresso!!.id)
        assertEquals("Strong & Bold", espresso.type)
        assertEquals(3.50, espresso.price, 0.001)
        assertEquals("Pure and intense coffee experience.", espresso.description)
    }

    @Test
    fun `coffeeList contains Cappuccino with correct properties`() {
        val cappuccino = coffeeList.find { it.name == "Cappuccino" }
        
        assertNotNull(cappuccino)
        assertEquals("4", cappuccino!!.id)
        assertEquals("Creamy Foam", cappuccino.type)
        assertEquals(4.50, cappuccino.price, 0.001)
    }

    @Test
    fun `coffeeList contains Matcha Latte with correct properties`() {
        val matchaLatte = coffeeList.find { it.name == "Matcha Latte" }
        
        assertNotNull(matchaLatte)
        assertEquals("6", matchaLatte!!.id)
        assertEquals("Zen Green", matchaLatte.type)
        assertEquals(5.25, matchaLatte.price, 0.001)
    }

    @Test
    fun `coffeeList prices are within reasonable range`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.price >= 3.0)
            assertTrue(coffee.price <= 10.0)
        }
    }

    @Test
    fun `Coffee data class equality works correctly`() {
        val coffee1 = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50
        )
        
        val coffee2 = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50
        )
        
        val coffee3 = Coffee(
            id = "2",
            name = "Cappuccino",
            type = "Creamy",
            price = 4.50
        )
        
        assertEquals(coffee1, coffee2)
        assertNotEquals(coffee1, coffee3)
    }

    @Test
    fun `Coffee data class hashCode is consistent with equality`() {
        val coffee1 = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50
        )
        
        val coffee2 = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50
        )
        
        assertEquals(coffee1.hashCode(), coffee2.hashCode())
    }

    @Test
    fun `Coffee data class toString contains name`() {
        val coffee = Coffee(
            id = "1",
            name = "Espresso",
            type = "Strong",
            price = 3.50
        )
        
        val toString = coffee.toString()
        assertTrue(toString.contains("Espresso"))
    }

    @Test
    fun `coffeeList is ordered by id`() {
        val ids = coffeeList.map { it.id.toInt() }
        val sortedIds = ids.sorted()
        
        assertEquals(sortedIds, ids)
    }

    @Test
    fun `coffeeList contains variety of coffee types`() {
        val types = coffeeList.map { it.type }
        
        assertTrue(types.contains("Strong & Bold"))
        assertTrue(types.contains("Fresh & Fruity"))
        assertTrue(types.contains("Smooth Balance"))
        assertTrue(types.contains("Creamy Foam"))
        assertTrue(types.contains("Chilled Refreshment"))
        assertTrue(types.contains("Zen Green"))
        assertTrue(types.contains("Sweet Drizzle"))
        assertTrue(types.contains("Berry Blast"))
        assertTrue(types.contains("Sweet & Icy"))
        assertTrue(types.contains("Choco Intensity"))
    }

    @Test
    fun `coffeeList contains coffee with price 3_50`() {
        assertTrue(coffeeList.any { it.price == 3.50 })
    }

    @Test
    fun `coffeeList contains coffee with price 6_00`() {
        assertTrue(coffeeList.any { it.price == 6.00 })
    }

    @Test
    fun `Coffee with zero price is valid`() {
        val freeCoffee = Coffee(
            id = "free",
            name = "Free Coffee",
            type = "Promotional",
            price = 0.0
        )
        
        assertEquals(0.0, freeCoffee.price, 0.001)
    }

    @Test
    fun `Coffee with negative price is technically valid but unusual`() {
        // While unusual, the data class doesn't enforce price constraints
        val negativePriceCoffee = Coffee(
            id = "error",
            name = "Error Coffee",
            type = "Pricing Error",
            price = -1.0
        )
        
        assertEquals(-1.0, negativePriceCoffee.price, 0.001)
    }

    @Test
    fun `coffeeList descriptions are not empty`() {
        coffeeList.forEach { coffee ->
            assertTrue(coffee.description.isNotEmpty())
        }
    }

    @Test
    fun `Coffee data class component1 returns id`() {
        val coffee = Coffee(id = "test-id")
        assertEquals("test-id", coffee.component1())
    }

    @Test
    fun `Coffee data class component2 returns name`() {
        val coffee = Coffee(name = "test-name")
        assertEquals("test-name", coffee.component2())
    }
}
