import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController

@Composable
fun BiometricAuthScreen(navController: NavController) {
    val context = LocalContext.current
    val biometricManager = BiometricManager.from(context)
    val canAuthenticate = remember { mutableStateOf(false) }

    // Check if the device supports biometric authentication
    LaunchedEffect(Unit) {
        canAuthenticate.value = when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    showBiometricPrompt(context as FragmentActivity,navController)
}

private fun showBiometricPrompt(activity: FragmentActivity,navController: NavController) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            // Handle authentication error
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            navController.navigate("mealScreen") {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
            }
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // Handle authentication failure
        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}