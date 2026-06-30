package com.example.test_1_project

import com.example.test_1_project.data.Coffee
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoritesViewModelTest {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var testCoffee1: Coffee
    private lateinit var testCoffee2: Coffee

    @Before
    fun setup() {
        // FavoritesViewModel now has a try-catch in init to handle 
        // Firebase not being initialized during unit tests.
        favoritesViewModel = FavoritesViewModel()
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
    fun `favorites list is initially empty`() {
        assertEquals(0, favoritesViewModel.favorites.size)
    }

    @Test
    fun `isFavorite returns false for non-favorite coffee`() {
        assertFalse(favoritesViewModel.isFavorite(testCoffee1.id))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `isFavorite returns true for favorite coffee after manual add`() {
        // Manually add to favorites list for testing (bypassing Firebase)
        val favorites = favoritesViewModel.favorites as MutableList<Coffee>
        favorites.add(testCoffee1)
        
        assertTrue(favoritesViewModel.isFavorite(testCoffee1.id))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `isFavorite returns false for different coffee id`() {
        val favorites = favoritesViewModel.favorites as MutableList<Coffee>
        favorites.add(testCoffee1)
        
        assertFalse(favoritesViewModel.isFavorite(testCoffee2.id))
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `favorites list contains correct coffee after manual add`() {
        val favorites = favoritesViewModel.favorites as MutableList<Coffee>
        favorites.add(testCoffee1)
        
        assertEquals(1, favoritesViewModel.favorites.size)
        assertEquals(testCoffee1.id, favoritesViewModel.favorites[0].id)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `favorites list can contain multiple coffees`() {
        val favorites = favoritesViewModel.favorites as MutableList<Coffee>
        favorites.add(testCoffee1)
        favorites.add(testCoffee2)
        
        assertEquals(2, favoritesViewModel.favorites.size)
    }

    @Test
    fun `loadFavorites does nothing when user is not authenticated`() {
        val initialSize = favoritesViewModel.favorites.size
        // This will return early because auth.currentUser is null (or throws and is caught)
        favoritesViewModel.loadFavorites()
        assertEquals(initialSize, favoritesViewModel.favorites.size)
    }

    @Test
    fun `isFavorite handles null or empty coffee id gracefully`() {
        assertFalse(favoritesViewModel.isFavorite(""))
        assertFalse(favoritesViewModel.isFavorite("non-existent-id"))
    }
}
