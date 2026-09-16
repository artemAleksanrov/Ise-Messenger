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

internal val BrandPrimaryColor = Color(0xFF2B7A5B)
internal val BrandDarkColor = Color(0xFF19543E)
internal val BrandMediumColor = Color(0xFF3E9672)
internal val BrandSoftColor = Color(0xFFE0F1E8)
internal val AppCanvasColor = Color(0xFFF4F8F6)
internal val AppPaperColor = Color(0xFFFFFFFF)
internal val AppInkColor = Color(0xFF172A22)
internal val AppMutedColor = Color(0xFF687A71)
internal val AppLineColor = Color(0xFFDCE8E2)
internal val AppSoftSurfaceColor = Color(0xFFEBF3EF)
internal val AppOnlineColor = Color(0xFF2FAE72)
internal val AppDangerColor = Color(0xFFD65C5C)
internal val AppDangerSoftColor = Color(0xFFF8E7E7)
internal val CallBackgroundTopColor = Color(0xFF12261D)
internal val CallBackgroundBottomColor = Color(0xFF08130F)
internal val CallBackgroundGlowColor = Color(0xFF28513F)

internal val ChatWallpaperBrush = Brush.linearGradient(
    listOf(Color(0xFFE0F1E8), AppCanvasColor, Color(0xFFE8F3ED))
)
internal val OutgoingMessageBrush = Brush.linearGradient(
    listOf(Color(0xFF4AAA82), Color(0xFF2F8A68), Color(0xFF21684F))
)
internal val IncomingMessageBrush = Brush.linearGradient(
    listOf(AppPaperColor, AppSoftSurfaceColor)
)

internal const val NotificationAccentColor = -13927845
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
    BrandPrimaryColor, BrandDarkColor, BrandSoftColor, AppCanvasColor, AppPaperColor,
    AppInkColor, AppMutedColor, AppLineColor, AppSoftSurfaceColor, AppOnlineColor
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
    Brush.linearGradient(listOf(Color(0xFFBFE8D3), Color(0xFF63B68E))),
    Brush.linearGradient(listOf(Color(0xFFD8E8CC), Color(0xFF8CB47A))),
    Brush.linearGradient(listOf(Color(0xFFC4E8E3), Color(0xFF62ABA2))),
    Brush.linearGradient(listOf(Color(0xFFD5E2EC), Color(0xFF7E9EB5))),
    Brush.linearGradient(listOf(Color(0xFFEEE0C8), Color(0xFFC7A26D)))
)
