package com.example.isemessenger.core.config

import com.example.isemessenger.BuildConfig

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val ServerUrl: String = BuildConfig.SERVER_URL.trimEnd('/')
internal const val NotificationChannelId = "messages"
internal const val CallNotificationChannelId = "calls"
internal const val CallNotificationId = 9042
internal const val IncomingCallNotificationChannelId = "incoming_calls"
internal const val CallOpenAction = "com.example.isemessenger.CALL_OPEN"
internal const val CallAcceptAction = "com.example.isemessenger.CALL_ACCEPT"
internal const val CallDeclineAction = "com.example.isemessenger.CALL_DECLINE"
internal const val NotificationOpenAction = "com.example.isemessenger.NOTIFICATION_OPEN"
internal const val NotificationReplyAction = "com.example.isemessenger.REPLY"
internal const val NotificationReadAction = "com.example.isemessenger.READ"
internal const val NotificationReplyKey = "notification_reply"

internal val NotificationAccentColor = android.graphics.Color.rgb(31, 122, 85)
internal const val OverlayDimAlpha = 0.14f
internal val OverlayScrimColor = Color.Transparent

@Immutable
internal data class IsePalette(
    val accent: Color,
    val accentDark: Color,
    val accentSoft: Color,
    val canvas: Color,
    val paper: Color,
    val ink: Color,
    val muted: Color,
    val line: Color,
    val softSurface: Color,
    val online: Color
)

internal val LightPalette = IsePalette(
    Color(0xFF1F7A55), Color(0xFF115137), Color(0xFFDFF4EA), Color(0xFFF4F7F5), Color(0xFFFFFFFF),
    Color(0xFF14211A), Color(0xFF6D7B73), Color(0xFFDDE7E1), Color(0xFFEAF1ED), Color(0xFF25B96B)
)

internal val LocalIsePalette = staticCompositionLocalOf { LightPalette }
internal val LocalConnectionStatusText = staticCompositionLocalOf<String?> { null }
internal val Forest: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.accent
internal val ForestDark: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.accentDark
internal val Mint: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.accentSoft
internal val Canvas: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.canvas
internal val Paper: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.paper
internal val Ink: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.ink
internal val Muted: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.muted
internal val Line: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.line
internal val SoftSurface: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.softSurface
internal val OnlineGreen: Color @Composable @ReadOnlyComposable get() = LocalIsePalette.current.online

internal val AvatarGradients = listOf(
    Brush.linearGradient(listOf(Color(0xFFBEEBD5), Color(0xFF58B889))),
    Brush.linearGradient(listOf(Color(0xFFFFDFC0), Color(0xFFF19B54))),
    Brush.linearGradient(listOf(Color(0xFFD8E1FF), Color(0xFF7793E8))),
    Brush.linearGradient(listOf(Color(0xFFF5D4E7), Color(0xFFD978AD))),
    Brush.linearGradient(listOf(Color(0xFFCDEDEC), Color(0xFF55B8B2)))
)
