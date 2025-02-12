package com.example.mykoinapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mykoinapp.R

@Composable
fun ProfileScreen() {
    val profileImage = painterResource(id = R.drawable.profile_pic) // Replace with your image resource
    val backgroundColor = Color(0xFFBBDEFB) // Light blue background

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Profile") },
//                backgroundColor = backgroundColor,
//                contentColor = Color.White
//            )
//        },
//        backgroundColor = backgroundColor
//    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = profileImage,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "John Doe",
                style = MaterialTheme.typography.headlineMedium,
//                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Android Developer | Tech Enthusiast",
                style = MaterialTheme.typography.bodyMedium,
//                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { /* TODO: Handle Follow */ }) {
                    Text("Follow")
                }
                Button(onClick = { /* TODO: Handle Message */ }) {
                    Text("Message")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.White, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About Me",
                style = MaterialTheme.typography.headlineLarge,
//                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Passionate about creating amazing mobile experiences. Love to explore new technologies and share knowledge with the community.",
                style = MaterialTheme.typography.bodyMedium,
//                color = Color.White
            )
        }

}