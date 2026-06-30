package com.example.test_1_project

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.test_1_project.data.Coffee
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FavoritesViewModel : ViewModel() {
    private val _favorites = mutableStateListOf<Coffee>()
    val favorites: List<Coffee> = _favorites

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("favorites")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val favoriteIds = snapshot.documents.mapNotNull { it.getString("coffeeId") }
                _favorites.clear()
                _favorites.addAll(com.example.test_1_project.data.coffeeList.filter { it.id in favoriteIds })
                Log.d("FavoritesViewModel", "Loaded ${_favorites.size} favorites")
            }
            .addOnFailureListener { e ->
                Log.e("FavoritesViewModel", "Error loading favorites", e)
            }
    }

    fun toggleFavorite(coffee: Coffee, onResult: (Boolean) -> Unit = {}) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(false)
            return
        }

        val isFavorite = _favorites.any { it.id == coffee.id }

        if (isFavorite) {
            // Remove from favorites
            db.collection("favorites")
                .whereEqualTo("userId", userId)
                .whereEqualTo("coffeeId", coffee.id)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.documents.forEach { doc ->
                        db.collection("favorites").document(doc.id).delete()
                    }
                    _favorites.removeIf { it.id == coffee.id }
                    onResult(false)
                    Log.d("FavoritesViewModel", "Removed ${coffee.name} from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("FavoritesViewModel", "Error removing favorite", e)
                    onResult(false)
                }
        } else {
            // Add to favorites
            val favoriteData = hashMapOf(
                "userId" to userId,
                "coffeeId" to coffee.id,
                "timestamp" to com.google.firebase.Timestamp.now()
            )
            db.collection("favorites")
                .add(favoriteData)
                .addOnSuccessListener {
                    _favorites.add(coffee)
                    onResult(true)
                    Log.d("FavoritesViewModel", "Added ${coffee.name} to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("FavoritesViewModel", "Error adding favorite", e)
                    onResult(false)
                }
        }
    }

    fun isFavorite(coffeeId: String): Boolean {
        return _favorites.any { it.id == coffeeId }
    }
}
