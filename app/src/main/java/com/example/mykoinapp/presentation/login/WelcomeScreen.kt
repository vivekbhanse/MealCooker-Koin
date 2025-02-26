package com.example.mykoinapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mykoinapp.R


@Composable
fun WelcomeScreen(onButtonClicked: () -> Unit) { // Corrected function signature
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_delivery_orange))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever, // Keeps looping infinitely
        restartOnPlay = true
    )
    val caveatFont = FontFamily(Font(R.font.caveat_variablefont_wght))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = caveatFont
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Illustration
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Welcome Text
        Text(
            text = "Welcome",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = caveatFont
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create an account and access our awesome services",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Button
        Button(
            onClick = onButtonClicked, // Correctly pass the function reference
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE))
        ) {
            Text(text = "Getting Started", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log In Text
        TextButton(onClick = onButtonClicked) { // Correctly pass the function reference
            Text(text = "Already have an account? Log In")
        }
    }
}

@Composable
@Preview
fun PreviewScreen(modifier: Modifier = Modifier) {

}