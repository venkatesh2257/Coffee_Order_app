package com.example.test_1_project

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.test_1_project.data.Coffee
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FavoritesViewModel(
    private val auth: FirebaseAuth? = null,
    private val db: FirebaseFirestore? = null
) : ViewModel() {
    private val _favorites = mutableStateListOf<Coffee>()
    val favorites: List<Coffee> = _favorites

    init {
        try {
            loadFavorites()
        } catch (e: Exception) {
            // Safe catch for Unit Tests where Firebase is not initialized
        }
    }

    fun loadFavorites() {
        val authInstance = auth ?: try {
            Firebase.auth
        } catch (e: Exception) {
            return
        }
        
        val dbInstance = db ?: try {
            Firebase.firestore
        } catch (e: Exception) {
            return
        }
        
        val userId = authInstance.currentUser?.uid ?: return

        dbInstance.collection("favorites")
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
        val authInstance = auth ?: try {
            Firebase.auth
        } catch (e: Exception) {
            onResult(false)
            return
        }
        
        val dbInstance = db ?: try {
            Firebase.firestore
        } catch (e: Exception) {
            onResult(false)
            return
        }
        
        val userId = authInstance.currentUser?.uid
        if (userId == null) {
            onResult(false)
            return
        }

        val isFavorite = _favorites.any { it.id == coffee.id }

        if (isFavorite) {
            // Remove from favorites
            dbInstance.collection("favorites")
                .whereEqualTo("userId", userId)
                .whereEqualTo("coffeeId", coffee.id)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.documents.forEach { doc ->
                        dbInstance.collection("favorites").document(doc.id).delete()
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
            dbInstance.collection("favorites")
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
