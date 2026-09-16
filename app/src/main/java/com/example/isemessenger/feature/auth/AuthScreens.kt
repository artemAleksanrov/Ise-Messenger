package com.example.isemessenger.feature.auth

import com.example.isemessenger.*
import com.example.isemessenger.core.config.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SplashScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { BrandMark(72.dp) }
}

@Composable
internal fun EmailScreen(initialEmail: String, loading: Boolean, submit: (String) -> Unit) {
    var email by rememberSaveable(initialEmail) { mutableStateOf(initialEmail) }
    val focusManager = LocalFocusManager.current
    AuthLayout("Введите почту") {
        AppTextField("Почта", email, { email = it }, KeyboardType.Email, ImeAction.Done,
            onDone = { focusManager.clearFocus(); submit(email) })
        Spacer(Modifier.height(14.dp))
        AuthContinueButton("Получить код", loading) { focusManager.clearFocus(); submit(email) }
    }
}

@Composable
internal fun CodeScreen(loading: Boolean, submit: (String) -> Unit, resend: () -> Unit, back: () -> Unit) {
    var code by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    AuthLayout("Введите код") {
        AppTextField(
            "Код", code,
            { code = it.filter { character -> character in '0'..'9' }.take(6) },
            KeyboardType.NumberPassword, ImeAction.Done,
            onDone = { focusManager.clearFocus(); submit(code) }
        )
        Spacer(Modifier.height(14.dp))
        AuthContinueButton("Продолжить", loading) { focusManager.clearFocus(); submit(code) }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = resend, enabled = !loading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Отправить код ещё раз", color = Forest)
        }
        TextButton(onClick = back, enabled = !loading, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Изменить почту", color = Forest)
        }
    }
}

@Composable
internal fun NameScreen(loading: Boolean, submit: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    AuthLayout("Введите имя") {
        AppTextField(
            "Имя", name,
            { if (it.codePointCount(0, it.length) <= 40) name = it },
            KeyboardType.Text, ImeAction.Done,
            onDone = { focusManager.clearFocus(); submit(name) }
        )
        Spacer(Modifier.height(14.dp))
        AuthContinueButton("Начать общение", loading) { focusManager.clearFocus(); submit(name) }
    }
}

@Composable
private fun AuthLayout(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
        CenteredTopBar(
            title = title,
            startContent = {},
            endContent = {}
        )
        Column(
            Modifier.fillMaxWidth().weight(1f).padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Composable
private fun AuthContinueButton(description: String, loading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !loading,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Forest,
            disabledContainerColor = Forest.copy(alpha = 0.38f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
        } else {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = description,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun BrandMark(size: Dp) {
    Box(
        Modifier.size(size).clip(RoundedCornerShape(size * 0.34f))
            .background(Brush.linearGradient(listOf(BrandMediumColor, ForestDark))),
        contentAlignment = Alignment.Center
    ) {
        Text("i", color = Color.White, fontWeight = FontWeight.Black, fontSize = (size.value * 0.55f).sp)
        Box(Modifier.size(size * 0.13f).align(Alignment.TopEnd).padding(top = size * 0.04f, end = size * 0.04f).clip(CircleShape).background(Color.White.copy(alpha = 0.84f)))
    }
}

@Composable
internal fun AppTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(title, color = Muted) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Forest,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = SoftSurface,
            unfocusedContainerColor = SoftSurface,
            cursorColor = Forest,
            focusedLabelColor = Forest,
            unfocusedLabelColor = Muted
        ),
        modifier = modifier.fillMaxWidth().height(60.dp)
    )
}

@Composable
internal fun PrimaryButton(text: String, loading: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Forest, disabledContainerColor = Forest.copy(alpha = 0.38f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
    ) {
        if (loading) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
        else Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
