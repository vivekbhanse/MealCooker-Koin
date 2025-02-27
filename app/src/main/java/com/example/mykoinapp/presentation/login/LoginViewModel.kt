package com.example.mykoinapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.store.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
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

    fun saveUserToFirestore(email: String, password: String, fullName: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "uid" to userId,
                        "email" to email,
                        "fullName" to fullName
                    )

                    FirebaseFirestore.getInstance().collection("users").document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            Log.d("Firestore", "User data successfully saved")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error saving user data", e)
                        }
                } else {
                    Log.e("Auth", "User registration failed", task.exception)
                }
            }
    }


    fun saveUserName(userName: String) {
        viewModelScope.launch {
            dataStoreManager.saveUserName(userName)
        }
    }

}

