package com.example.mykoinapp.presentation.login

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.mykoinapp.MainActivity
import com.example.mykoinapp.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenView(
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val activity = context as? Activity
    var isLoginPageVisible by remember { mutableStateOf(true) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_loginb))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .focusable(true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoginPageVisible) {
                LoginScreen(
                    auth = auth,
                    onSwitchToSignUp = { isLoginPageVisible = false },
                    onLoginSuccess = {
                        activity?.startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                )
            } else {
                SignUpScreen(
                    auth = auth,
                    onSwitchToLogin = { isLoginPageVisible = true }
                )
            }

            GoogleSignInButton {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }
    }
}



@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    onSwitchToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val loginViewModel: LoginViewModel = koinViewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isFormValid = email.isNotBlank() && password.isNotBlank() && emailError == null

    Box(
        modifier = Modifier.fillMaxSize()
        .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Log In Now", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Please login to continue using our app", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            InputField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (isValidEmail(it)) null else "Invalid email address"
                },
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    passwordError =
                        if (password.length < 6) "Password must be at least 6 characters" else null
                },
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    logInWithEmail(auth, email, password) { success, error ->
                        isLoading = false
                        if (success) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                            loginViewModel.saveUserToFirestore(email, password, "fullName")
                            loginViewModel.saveEmailPassword(email, password)
                        } else {
                            Toast.makeText(context, error ?: "Login failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onSwitchToSignUp, enabled = !isLoading) {
                Text("Don't have an account? Sign Up")
            }
        }

        if (isLoading) {
            ProgressBars()
        }
    }
}


@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    onSwitchToLogin: () -> Unit
) {
    val loginViewModel: LoginViewModel = koinViewModel()
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isFormValid = email.isNotBlank() && fullName.isNotBlank() && password.isNotBlank()
            && emailError == null && fullNameError == null && passwordError == null

    Box(
        modifier = Modifier.fillMaxSize()
        .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Sign Up Now", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Create a new account", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            InputField(
                label = "Full Name",
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError =
                        if (it.length >= 3) null else "Full name must be at least 3 characters"
                },
                isError = fullNameError != null,
                errorMessage = fullNameError
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (isValidEmail(it)) null else "Invalid email address"
                },
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                    passwordError =
                        if (password.length < 6) "Password must be at least 6 characters" else null
                },
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    signUpWithEmail(auth, email, password) { success, error ->
                        isLoading = false
                        if (success) {
                            Toast.makeText(context, "Sign-up successful!", Toast.LENGTH_SHORT)
                                .show()
                            loginViewModel.saveUserToFirestore(email, password, fullName)
                            onSwitchToLogin()
                        } else {
                            Toast.makeText(context, error ?: "Sign-up failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onSwitchToLogin, enabled = !isLoading) {
                Text("Already have an account? Log In")
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = errorMessage.orEmpty(), color = Color.Red, fontSize = 12.sp)
            }
        }
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        trailingIcon = {
            IconButton(onClick = onTogglePassword) {
                Icon(
                    painter = painterResource(
                        id = if (showPassword) R.drawable.eye_hidden else R.drawable.eye_show
                    ),
                    contentDescription = if (showPassword) "Hide Password" else "Show Password",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        ),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    )
}


fun signUpWithEmail(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful, task.exception?.message)
        }
}

fun logInWithEmail(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onResult(task.isSuccessful, task.exception?.message)

        }
}


@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    AndroidView(
        factory = { context ->
            SignInButton(context).apply {
                setSize(SignInButton.SIZE_WIDE) // Adjust size if needed
                setColorScheme(SignInButton.COLOR_DARK) // Use COLOR_LIGHT for a white button
                setOnClickListener { onClick() }
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun ProgressBars(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize() // Makes the Box take the full screen
            .then(modifier) // Allows for external modifiers
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

