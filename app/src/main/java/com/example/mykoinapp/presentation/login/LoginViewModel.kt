package com.example.mykoinapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.store.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val dataStoreManager: DataStoreManager, private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.isLoggedIn.collect { status ->
                _isLoggedIn.value = status
            }
        }
    }
    fun getUser() = firebaseAuth.currentUser

    fun saveEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            dataStoreManager.saveUser(email, password, isLoggedIn = true)
        }
    }

    fun saveUserToFirestore(email: String,password: String, fullName: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return

        val userMap = hashMapOf(
            "uid" to userId,
            "email" to email,
            "password" to password,
            "fullName" to fullName // Removed password for security reasons
        )

        viewModelScope.launch {
            try {
                firestore.collection("users").document(userId).set(userMap).await()
                Log.d("Firestore", "User data successfully saved")
            } catch (e: Exception) {
                Log.e("Firestore", "Error saving user data", e)
            }
        }
    }




    fun saveUserName(userName: String) {
        viewModelScope.launch {
            dataStoreManager.saveUserName(userName)
        }
    }

}

