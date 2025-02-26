package com.example.mykoinapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mykoinapp.data.local.store.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val dataStoreManager: DataStoreManager,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _fullName = MutableSharedFlow<String>(replay = 1) // SharedFlow with replay
    val fullName = _fullName.asSharedFlow() // Use asSharedFlow()
    private var _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
//        getUserName() // Fetch username on ViewModel initialization
        getUsernameFromFirestore()
    }

    private fun getUserName() {
        viewModelScope.launch {
            dataStoreManager.userName.collect {
                if (it != null) {
                    _fullName.emit(it)
                }
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            dataStoreManager.clearUserData()
            _isLoggedIn.value = false // Update state correctly
        }
    }

    private fun getUsernameFromFirestore() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val username = document.getString("fullName")

                viewModelScope.launch {
                    username?.let { _fullName.emit(it) }
                }
            }
            .addOnFailureListener {

            }
    }


}