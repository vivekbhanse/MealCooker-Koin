package com.example.mykoinapp.presentation.profile

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mykoinapp.R
import com.example.mykoinapp.presentation.login.LoginActivity
import com.example.mykoinapp.presentation.login.LoginViewModel
import com.example.mykoinapp.presentation.orders.OrdersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen() {
    val profileViewModel: ProfileViewModel = koinViewModel()
    val ordersViewModel: OrdersViewModel = koinViewModel()
    val context = LocalContext.current
    val username by profileViewModel.fullName.collectAsStateWithLifecycle(initialValue = "Loading...")
    val isLoggedIn by profileViewModel.isLoggedIn.collectAsStateWithLifecycle(initialValue = true)

    var showLogoutDialog by remember { mutableStateOf(false) } // State for dialog visibility

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.profile_pic),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = username, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Android Developer | Tech Enthusiast", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.White, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "About Me", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Passionate about creating amazing mobile experiences. Love to explore new technologies and share knowledge with the community.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button - Show dialog on click
        Button(onClick = { showLogoutDialog = true }) {
            Text("Logout")
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Confirm Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            profileViewModel.logoutUser() // Perform logout
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Redirect to LoginActivity if user is logged out
        LaunchedEffect(!isLoggedIn) {
            if (!isLoggedIn) {
                ordersViewModel.signout()
                ordersViewModel.clearAllOrders()
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as? Activity)?.finish()
            }
        }
    }
}
