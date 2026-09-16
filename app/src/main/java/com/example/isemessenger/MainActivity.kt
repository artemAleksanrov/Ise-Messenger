package com.example.isemessenger

import com.example.isemessenger.core.config.*
import com.example.isemessenger.feature.auth.*
import com.example.isemessenger.feature.avatar.*
import com.example.isemessenger.feature.call.*
import com.example.isemessenger.feature.chat.*
import com.example.isemessenger.feature.chats.*
import com.example.isemessenger.feature.group.*
import com.example.isemessenger.feature.media.*
import com.example.isemessenger.feature.profile.*
import com.example.isemessenger.feature.settings.*

import android.annotation.SuppressLint
import android.animation.ValueAnimator
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SurfaceTexture
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.Ringtone
import android.media.RingtoneManager
import android.widget.VideoView
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Environment
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Base64
import android.util.LruCache
import android.util.Patterns
import android.util.Size
import android.webkit.MimeTypeMap
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.view.TextureView
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.automirrored.rounded.Reply
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Audiotrack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallEnd
import androidx.compose.material.icons.rounded.Cameraswitch
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.edit
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.ServiceCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import org.webrtc.AudioTrack
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DataChannel
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.EglRenderer
import org.webrtc.GlRectDrawer
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.RendererCommon
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoSource
import org.webrtc.VideoSink
import org.webrtc.VideoTrack
import org.webrtc.audio.JavaAudioDeviceModule
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import okio.BufferedSink
import java.text.SimpleDateFormat
import java.security.MessageDigest
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.WeakHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import java.nio.ByteOrder
import kotlin.math.roundToInt

internal val NotificationSequence = AtomicInteger(1000)
internal val BitmapMemoryCache = object : LruCache<String, Bitmap>(
    (Runtime.getRuntime().maxMemory() / 1024L / 10L).coerceIn(8L * 1024L, 48L * 1024L).toInt()
) {
    override fun sizeOf(key: String, value: Bitmap): Int = (value.allocationByteCount / 1024).coerceAtLeast(1)
}
internal val VideoDurationCache = LruCache<String, Long>(256)
internal val AudioWaveformCache = LruCache<String, List<Float>>(128)
internal val StableFileKeyCache = Collections.synchronizedMap(WeakHashMap<String, String>())
internal val StableFileDigest = ThreadLocal.withInitial { MessageDigest.getInstance("SHA-256") }
internal var ActiveAudioMessageId by mutableLongStateOf(0L)
internal val UnifiedMessageBubbleBrush = OutgoingMessageBrush
internal val IncomingMessageBubbleBrush = IncomingMessageBrush

private data class BlurSnapshot(
    val bitmap: Bitmap,
    val sourceWidth: Int,
    val sourceHeight: Int,
    val createdAt: Long
)

private val BlurSnapshotLock = Any()
private var CachedBlurSnapshot: BlurSnapshot? = null
private val BlurWorkBufferLock = Any()
private var BlurPixelBuffer = IntArray(0)
private var BlurTemporaryBuffer = IntArray(0)
private val BlurDivisionLookup = IntArray(256 * 17) { it / 17 }
class IseFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        getSharedPreferences("ise_session", Context.MODE_PRIVATE).edit { putString("fcm_token", token) }
        PushTokenRegistrar.register(this, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"].orEmpty()
        if (type == "call_ended") {
            cancelIncomingCallNotification(this, message.data["call_id"].orEmpty())
            return
        }
        if (type == "incoming_call") {
            val callId = message.data["call_id"].orEmpty()
            val chatId = message.data["chat_id"]?.toLongOrNull() ?: 0L
            val callerId = message.data["caller_id"]?.toLongOrNull() ?: 0L
            val callerName = message.data["caller_name"].orEmpty().ifBlank { "Ise Messenger" }
            val video = message.data["video"].toBoolean()
            val token = getSharedPreferences("ise_session", Context.MODE_PRIVATE).getString("token", "").orEmpty()
            if (callId.isBlank() || chatId < 1L || callerId < 1L || token.isBlank()) return
            showIncomingCallNotification(this, callId, chatId, callerId, callerName, video)
            return
        }
        val chatId = message.data["chat_id"]?.toLongOrNull() ?: 0L
        val messageId = message.data["message_id"]?.toLongOrNull() ?: 0L
        if (type in listOf("read", "message_deleted", "chat_cleared")) {
            if (chatId > 0L) {
                if (type == "message_deleted") recordDeletedNotification(this, chatId, messageId)
                else recordNotificationRead(this, chatId, messageId)
                cancelChatNotification(this, chatId)
            }
            return
        }
        if (notificationSuppressed(this, chatId, messageId)) return
        val title = message.notification?.title ?: message.data["title"] ?: "Ise Messenger"
        val body = message.notification?.body ?: message.data["body"] ?: return
        showNotification(this, title, body, message.data["chat_id"], message.data["message_id"], type == "call_history")
    }
}

class CallForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CallNotificationChannelId, "Звонки", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val peerName = intent?.getStringExtra("peer_name").orEmpty().ifBlank { "Ise Messenger" }
        val video = intent?.getBooleanExtra("video", false) == true
        val microphoneGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        val cameraGranted = !video || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (!microphoneGranted || !cameraGranted) {
            stopSelf(startId)
            return START_NOT_STICKY
        }
        val openIntent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            CallNotificationId,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CallNotificationChannelId)
            .setSmallIcon(R.drawable.ic_notification_call)
            .setColor(NotificationAccentColor)
            .setContentTitle(peerName)
            .setContentText(if (video) "Видеозвонок" else "Аудиозвонок")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .build()
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or
                    if (video) ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA else 0
        } else {
            0
        }
        if (runCatching { ServiceCompat.startForeground(this, CallNotificationId, notification, type) }.isFailure) stopSelf(startId)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val chatId = intent.getLongExtra("chat_id", 0L)
        val messageId = intent.getLongExtra("message_id", 0L)
        val notificationId = intent.getIntExtra("notification_id", 0)
        val token = context.getSharedPreferences("ise_session", Context.MODE_PRIVATE).getString("token", "").orEmpty()
        if (chatId < 1L || messageId < 1L || token.isBlank()) return
        val reply = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(NotificationReplyKey)?.toString()?.trim().orEmpty()
        if (intent.action == NotificationReplyAction && reply.isBlank()) return
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                var completed = false
                repeat(3) { attempt ->
                    if (!completed) {
                        completed = runCatching {
                            val api = MessengerApi(ServerUrl, 12L)
                            if (intent.action == NotificationReplyAction) {
                                api.post("/chats/$chatId/messages", JSONObject().put("text", reply).put("reply_to_id", messageId), token)
                            } else if (intent.action == NotificationReadAction) {
                                api.post("/chats/$chatId/read", JSONObject().put("message_id", messageId), token)
                            } else {
                                return@runCatching false
                            }
                            true
                        }.getOrDefault(false)
                        if (!completed && attempt < 2) delay(750L shl attempt)
                    }
                }
                if (completed) {
                    recordNotificationRead(context, chatId, messageId)
                    if (notificationId > 0) NotificationManagerCompat.from(context).cancel(notificationId)
                } else if (intent.action == NotificationReplyAction) {
                    showNotification(
                        context,
                        "Ответ не отправлен",
                        "Откройте чат и попробуйте отправить сообщение ещё раз",
                        null,
                        null,
                        false
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

class CallActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != CallDeclineAction) return
        val callId = intent.getStringExtra("call_id").orEmpty()
        val token = context.getSharedPreferences("ise_session", Context.MODE_PRIVATE).getString("token", "").orEmpty()
        if (callId.isBlank()) return
        cancelIncomingCallNotification(context, callId)
        if (token.isBlank()) return
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                runCatching {
                    MessengerApi(ServerUrl, 12L).post(
                        "/calls/action",
                        JSONObject().put("call_id", callId).put("action", "decline"),
                        token
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

internal object PushTokenRegistrar {
    private val client = OkHttpClient.Builder().connectTimeout(12, TimeUnit.SECONDS).build()
    private val jsonType = "application/json; charset=utf-8".toMediaType()
    private val registering = AtomicBoolean(false)

    fun register(context: Context, fcmToken: String) {
        val session = context.getSharedPreferences("ise_session", Context.MODE_PRIVATE)
        val authToken = session.getString("token", "").orEmpty()
        if (authToken.isBlank() || fcmToken.isBlank()) return
        if (!registering.compareAndSet(false, true)) return
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                repeat(4) { attempt ->
                    val request = Request.Builder()
                        .url("$ServerUrl/devices/register")
                        .header("Authorization", "Bearer $authToken")
                        .post(JSONObject().put("token", fcmToken).toString().toRequestBody(jsonType))
                        .build()
                    val success = runCatching { client.newCall(request).execute().use { it.isSuccessful } }.getOrDefault(false)
                    if (success) return@launch
                    delay(1_000L shl attempt)
                }
            } finally {
                registering.set(false)
            }
        }
    }
}


internal fun ensureNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= 26) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(NotificationChannelId, "Сообщения", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Новые чаты и сообщения"
                enableVibration(true)
            }
        )
    }
}

internal fun ensureIncomingCallNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < 26) return
    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    context.getSystemService(NotificationManager::class.java).createNotificationChannel(
        NotificationChannel(IncomingCallNotificationChannelId, "Входящие звонки", NotificationManager.IMPORTANCE_HIGH).apply {
            setSound(ringtone, attributes)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 700, 450, 700, 450)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
    )
}

internal fun incomingCallNotificationId(callId: String): Int {
    return 100_000 + ((callId.hashCode() and Int.MAX_VALUE) % 800_000)
}

internal fun cancelIncomingCallNotification(context: Context, callId: String) {
    if (callId.isNotBlank()) NotificationManagerCompat.from(context).cancel(incomingCallNotificationId(callId))
}

internal fun showIncomingCallNotification(
    context: Context,
    callId: String,
    chatId: Long,
    callerId: Long,
    callerName: String,
    video: Boolean
) {
    if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
    ensureIncomingCallNotificationChannel(context)
    val notificationId = incomingCallNotificationId(callId)
    val baseIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        putExtra("call_id", callId)
        putExtra("chat_id", chatId)
        putExtra("caller_id", callerId)
        putExtra("caller_name", callerName)
        putExtra("video", video)
        putExtra("opened_over_lock_screen", context.getSystemService(KeyguardManager::class.java).isKeyguardLocked)
    }
    val openIntent = Intent(baseIntent).apply { action = CallOpenAction }
    val acceptIntent = Intent(baseIntent).apply { action = CallAcceptAction }
    val declineIntent = Intent(context, CallActionReceiver::class.java).apply {
        action = CallDeclineAction
        putExtra("call_id", callId)
    }
    val openPendingIntent = PendingIntent.getActivity(
        context,
        notificationId * 3,
        openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val acceptPendingIntent = PendingIntent.getActivity(
        context,
        notificationId * 3 + 1,
        acceptIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val declinePendingIntent = PendingIntent.getBroadcast(
        context,
        notificationId * 3 + 2,
        declineIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val caller = Person.Builder()
        .setName(callerName)
        .setImportant(true)
        .build()
    val callText = if (video) "Входящий видеозвонок" else "Входящий аудиозвонок"
    val callStyle = NotificationCompat.CallStyle
        .forIncomingCall(caller, declinePendingIntent, acceptPendingIntent)
    val builder = NotificationCompat.Builder(context, IncomingCallNotificationChannelId)
        .setSmallIcon(R.drawable.ic_notification_call)
        .setColor(NotificationAccentColor)
        .setContentTitle(callerName)
        .setContentText(callText)
        .setStyle(callStyle)
        .setCategory(NotificationCompat.CATEGORY_CALL)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOngoing(true)
        .setAutoCancel(false)
        .setOnlyAlertOnce(true)
        .setSound(ringtone)
        .setVibrate(longArrayOf(0, 700, 450, 700, 450))
        .setTimeoutAfter(45_000)
        .setContentIntent(openPendingIntent)
        .setFullScreenIntent(openPendingIntent, true)
        .addPerson(caller)
    val notification = builder.build().apply {
        flags = flags or android.app.Notification.FLAG_INSISTENT
    }
    NotificationManagerCompat.from(context).notify(notificationId, notification)
}

internal fun notificationIdForChat(chatId: Long): Int {
    return ((chatId xor (chatId ushr 32)).toInt() and Int.MAX_VALUE).coerceAtLeast(1)
}

internal fun cancelChatNotification(context: Context, chatId: Long) {
    NotificationManagerCompat.from(context).cancel(notificationIdForChat(chatId))
}

internal var ChatNotificationsVersion by mutableIntStateOf(0)

@Composable
internal fun chatNotificationsEnabledState(context: Context, chatId: Long): Boolean {
    val version = ChatNotificationsVersion
    return remember(context, chatId, version) { chatNotificationsEnabled(context, chatId) }
}

internal fun chatNotificationsEnabled(context: Context, chatId: Long): Boolean {
    if (chatId < 1L) return true
    return !context.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE)
        .getBoolean("muted_$chatId", false)
}

internal fun setChatNotificationsEnabled(context: Context, chatId: Long, enabled: Boolean) {
    if (chatId < 1L) return
    context.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE).edit {
        putBoolean("muted_$chatId", !enabled)
    }
    ChatNotificationsVersion++
    if (!enabled) cancelChatNotification(context, chatId)
}

internal fun recordNotificationRead(context: Context, chatId: Long, messageId: Long) {
    if (chatId < 1L || messageId < 1L) return
    val preferences = context.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE)
    val key = "read_$chatId"
    if (messageId > preferences.getLong(key, 0L)) preferences.edit { putLong(key, messageId) }
}

internal fun recordDeletedNotification(context: Context, chatId: Long, messageId: Long) {
    if (chatId > 0L && messageId > 0L) {
        context.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE).edit { putLong("deleted_$chatId", messageId) }
    }
}

internal fun notificationSuppressed(context: Context, chatId: Long, messageId: Long): Boolean {
    if (chatId < 1L || messageId < 1L) return false
    if (!chatNotificationsEnabled(context, chatId)) return true
    val preferences = context.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE)
    return preferences.getLong("read_$chatId", 0L) >= messageId || preferences.getLong("deleted_$chatId", 0L) == messageId
}

internal fun showNotification(
    context: Context,
    title: String,
    body: String,
    chatId: String?,
    messageId: String?,
    callEvent: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
    ensureNotificationChannel(context)
    val parsedChatId = chatId?.toLongOrNull() ?: 0L
    val parsedMessageId = messageId?.toLongOrNull() ?: 0L
    val notificationId = if (parsedChatId > 0L) {
        notificationIdForChat(parsedChatId)
    } else {
        NotificationSequence.incrementAndGet()
    }
    val intent = Intent(context, MainActivity::class.java).apply {
        action = NotificationOpenAction
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        chatId?.let { putExtra("chat_id", it) }
    }
    val pendingIntent = PendingIntent.getActivity(
        context,
        NotificationSequence.incrementAndGet(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val builder = NotificationCompat.Builder(context, NotificationChannelId)
        .setSmallIcon(if (callEvent) R.drawable.ic_notification_call else R.drawable.ic_notification_message)
        .setColor(NotificationAccentColor)
        .setContentTitle(title)
        .setContentText(body)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        .setCategory(if (callEvent) NotificationCompat.CATEGORY_EVENT else NotificationCompat.CATEGORY_MESSAGE)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
    if (!callEvent && parsedChatId > 0L && parsedMessageId > 0L) {
        val actionExtras = Intent(context, NotificationActionReceiver::class.java).apply {
            putExtra("chat_id", parsedChatId)
            putExtra("message_id", parsedMessageId)
            putExtra("notification_id", notificationId)
        }
        val replyIntent = Intent(actionExtras).apply { action = NotificationReplyAction }
        val readIntent = Intent(actionExtras).apply { action = NotificationReadAction }
        val mutableFlag = if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_MUTABLE else 0
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId * 2,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or mutableFlag
        )
        val readPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId * 2 + 1,
            readIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val remoteInput = RemoteInput.Builder(NotificationReplyKey).setLabel("Ответить").build()
        builder.addAction(
            NotificationCompat.Action.Builder(0, "Ответить", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
                .build()
        )
        builder.addAction(
            NotificationCompat.Action.Builder(android.R.drawable.checkbox_on_background, "Прочитано", readPendingIntent)
                .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MARK_AS_READ)
                .build()
        )
    }
    NotificationManagerCompat.from(context).notify(notificationId, builder.build())
}

internal fun loadNotificationAvatar(context: Context, senderId: Long, token: String): Bitmap? {
    if (senderId < 1L || token.isBlank()) return null
    val directory = File(context.filesDir, "media").apply { mkdirs() }
    val file = File(directory, "avatar_notification_$senderId.img")
    val legacyFile = File(File(context.filesDir, "avatars"), "notification_$senderId.img")
    if (!file.exists() && legacyFile.isFile) runCatching { legacyFile.copyTo(file, overwrite = false) }
    return synchronized(persistentMediaLock(file)) {
        val fresh = file.isFile && file.length() > 0L && System.currentTimeMillis() - file.lastModified() < TimeUnit.MINUTES.toMillis(15)
        if (fresh) {
            runCatching { decodeNotificationAvatar(file.readBytes()) }.getOrNull()?.let { return@synchronized it }
        }
        runCatching {
            val request = Request.Builder().url("$ServerUrl/avatars/$senderId").header("Authorization", "Bearer $token").build()
            val call = MediaLoadClient.newCall(request)
            call.timeout().timeout(5L, TimeUnit.SECONDS)
            call.execute().use { response ->
                if (response.code == 404) {
                    if (file.exists()) file.delete()
                    return@use null
                }
                if (!response.isSuccessful) return@use if (file.isFile) decodeNotificationAvatar(file.readBytes()) else null
                val bytes = response.body?.bytes()?.takeIf { it.isNotEmpty() && it.size <= 512 * 1024 } ?: return@use null
                val partial = File(directory, file.name + ".part")
                partial.outputStream().use { it.write(bytes) }
                if (!partial.renameTo(file)) {
                    partial.copyTo(file, overwrite = true)
                    partial.delete()
                }
                decodeNotificationAvatar(bytes)
            }
        }.getOrNull() ?: if (file.isFile) runCatching { decodeNotificationAvatar(file.readBytes()) }.getOrNull() else null
    }
}

internal fun decodeNotificationAvatar(bytes: ByteArray): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
    var sample = 1
    while (bounds.outWidth / sample > 256 || bounds.outHeight / sample > 256) sample *= 2
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options().apply { inSampleSize = sample })
}

internal fun notificationAvatarFallback(name: String, senderId: Long): Bitmap {
    val colors = arrayOf(
        intArrayOf(0xFFBEEBD5.toInt(), 0xFF58B889.toInt()),
        intArrayOf(0xFFFFDFC0.toInt(), 0xFFF19B54.toInt()),
        intArrayOf(0xFFD8E1FF.toInt(), 0xFF7793E8.toInt()),
        intArrayOf(0xFFF5D4E7.toInt(), 0xFFD978AD.toInt()),
        intArrayOf(0xFFCDEDEC.toInt(), 0xFF55B8B2.toInt())
    )
    val pair = colors[avatarGradientIndex(senderId, name)]
    val size = 144
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(0f, 0f, size.toFloat(), size.toFloat(), pair[0], pair[1], Shader.TileMode.CLAMP)
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    val initial = name.trim().firstOrNull()?.uppercase() ?: "?"
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 62f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    val baseline = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
    canvas.drawText(initial, size / 2f, baseline, textPaint)
    return bitmap
}

class MainActivity : ComponentActivity() {
    private lateinit var controller: MessengerController
    private lateinit var connectivityManager: ConnectivityManager
    private var connectivityCallbackRegistered = false
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = updateInternetAvailability()

        override fun onLost(network: Network) = updateInternetAvailability()

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            updateInternetAvailability()
        }
    }
    private var statusBarAnimator: ValueAnimator? = null
    private var systemBarsPreview: Boolean? = null
    private var statusBarUsesDarkIcons: Boolean? = null
    private var appDarkTheme = false
    private var incomingCallOverLockScreen by mutableStateOf(false)
    private var incomingCallLaunchCover: View? = null
    internal var overlayBlurBitmap by mutableStateOf<Bitmap?>(null)
        private set
    internal var overlayBlurRequests by mutableIntStateOf(0)
        private set
    private var overlayBlurJob: Job? = null

    internal fun acquireOverlayBlur() {
        val firstRequest = overlayBlurRequests == 0
        overlayBlurRequests += 1
        if (!firstRequest) return

        cachedBlurBackground(this)?.let {
            overlayBlurBitmap = it
            return
        }
        if (overlayBlurJob?.isActive == true) return
        val source = captureBlurSource(this) ?: return
        overlayBlurJob = lifecycleScope.launch {
            val snapshot = withContext(Dispatchers.Default) {
                smoothBoxBlur(source.bitmap)
                source
            }
            synchronized(BlurSnapshotLock) { CachedBlurSnapshot = snapshot }
            if (overlayBlurRequests > 0) overlayBlurBitmap = snapshot.bitmap
            overlayBlurJob = null
        }
    }

    internal fun releaseOverlayBlur() {
        overlayBlurRequests = (overlayBlurRequests - 1).coerceAtLeast(0)
    }

    internal fun clearOverlayBlurIfHidden() {
        if (overlayBlurRequests == 0) overlayBlurBitmap = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = MessengerController(this, ::finishLockedCall)
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        controller.updateInternetAvailability(hasInternetConnection())
        runCatching {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
            connectivityCallbackRegistered = true
        }
        handleCallIntent(intent)
        handleNotificationIntent(intent)
        updateSystemBars(false)
        ensureNotificationChannel(this)
        ensureIncomingCallNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1401)
        }
        // Firebase is configured only when app/google-services.json is present.
        // Keep local and CI builds usable without project-specific credentials.
        runCatching { FirebaseMessaging.getInstance() }
            .getOrNull()
            ?.token
            ?.addOnSuccessListener(controller::registerPushToken)
        setContent {
            IseTheme {
                MessengerApp(controller, this@MainActivity)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleCallIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleCallIntent(intent: Intent?) {
        val action = intent?.action ?: return
        if (action != CallOpenAction && action != CallAcceptAction) return
        val callId = intent.getStringExtra("call_id").orEmpty()
        if (callId.isBlank()) return
        val openedOverLockScreen = intent.getBooleanExtra("opened_over_lock_screen", false) ||
                getSystemService(KeyguardManager::class.java).isKeyguardLocked
        if (openedOverLockScreen && controller.state.screen != Screen.Call) showIncomingCallLaunchCover()
        updateIncomingCallWindow(true, openedOverLockScreen)
        cancelIncomingCallNotification(this, callId)
        controller.requestNotificationCall(
            callId = callId,
            accept = action == CallAcceptAction,
            chatId = intent.getLongExtra("chat_id", 0L),
            callerId = intent.getLongExtra("caller_id", 0L),
            callerName = intent.getStringExtra("caller_name").orEmpty(),
            video = intent.getBooleanExtra("video", false)
        )
        intent.action = null
    }

    fun updateIncomingCallWindow(active: Boolean, openedOverLockScreen: Boolean = false) {
        if (active) {
            incomingCallOverLockScreen = incomingCallOverLockScreen || openedOverLockScreen ||
                    getSystemService(KeyguardManager::class.java).isKeyguardLocked
        } else {
            incomingCallOverLockScreen = false
        }
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(active)
            setTurnScreenOn(active)
        } else if (active) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        if (incomingCallOverLockScreen) {
            WindowCompat.getInsetsController(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.statusBars())
                show(WindowInsetsCompat.Type.navigationBars())
            }
        } else {
            updateSystemBars(controller.state.screen in listOf(Screen.Media, Screen.AvatarSelection, Screen.Avatar, Screen.Call))
        }
    }

    fun isIncomingCallOverLockScreen(): Boolean = incomingCallOverLockScreen

    fun revealIncomingCallWindow() {
        val cover = incomingCallLaunchCover ?: return
        cover.postOnAnimation {
            if (incomingCallLaunchCover === cover) {
                (cover.parent as? android.view.ViewGroup)?.removeView(cover)
                incomingCallLaunchCover = null
            }
        }
    }

    private fun showIncomingCallLaunchCover() {
        if (incomingCallLaunchCover != null) return
        val cover = View(this).apply {
            setBackgroundColor(android.graphics.Color.BLACK)
            isClickable = true
            elevation = 1000f
        }
        incomingCallLaunchCover = cover
        addContentView(
            cover,
            android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun finishLockedCall() {
        if (!incomingCallOverLockScreen || isFinishing || isDestroyed) return
        incomingCallOverLockScreen = false
        incomingCallLaunchCover?.let { (it.parent as? android.view.ViewGroup)?.removeView(it) }
        incomingCallLaunchCover = null
        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        finishAndRemoveTask()
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.action != NotificationOpenAction) return
        val chatId = intent.getStringExtra("chat_id")?.toLongOrNull() ?: intent.getLongExtra("chat_id", 0L)
        if (chatId > 0L) controller.openChatFromNotification(chatId)
        intent.action = null
    }

    override fun onDestroy() {
        statusBarAnimator?.cancel()
        if (connectivityCallbackRegistered) {
            runCatching { connectivityManager.unregisterNetworkCallback(networkCallback) }
            connectivityCallbackRegistered = false
        }
        controller.close()
        super.onDestroy()
    }

    private fun hasInternetConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun updateInternetAvailability() {
        runOnUiThread {
            if (!isFinishing && !isDestroyed) {
                controller.updateInternetAvailability(hasInternetConnection())
            }
        }
    }

    internal fun retryInternetConnection() {
        updateInternetAvailability()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_RUNNING_LOW) {
            BitmapMemoryCache.trimToSize((BitmapMemoryCache.maxSize() / 2).coerceAtLeast(1))
        }
        if (level >= TRIM_MEMORY_UI_HIDDEN && overlayBlurRequests == 0) {
            overlayBlurBitmap = null
            synchronized(BlurSnapshotLock) { CachedBlurSnapshot = null }
            synchronized(BlurWorkBufferLock) {
                BlurPixelBuffer = IntArray(0)
                BlurTemporaryBuffer = IntArray(0)
            }
        }
        if (level >= TRIM_MEMORY_BACKGROUND) {
            StableFileKeyCache.clear()
            ParsedServerDateCache.evictAll()
            MessageDayKeyCache.evictAll()
            MessageTimeCache.evictAll()
        }
    }

    override fun onStart() {
        super.onStart()
        controller.onForeground()
    }

    override fun onStop() {
        controller.onBackground()
        super.onStop()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) updateSystemBars(controller.state.screen in listOf(Screen.Media, Screen.AvatarSelection, Screen.Avatar, Screen.Call))
    }

    @Suppress("DEPRECATION")
    fun updateSystemBars(mediaPreview: Boolean, force: Boolean = false) {
        val previousMode = systemBarsPreview
        systemBarsPreview = mediaPreview
        val targetDarkIcons = !mediaPreview && !appDarkTheme
        if (previousMode == null) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
            )
        }
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= 29) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        val insetsController = WindowCompat.getInsetsController(window, window.decorView).apply {
            if (incomingCallOverLockScreen) hide(WindowInsetsCompat.Type.statusBars()) else show(WindowInsetsCompat.Type.statusBars())
            show(WindowInsetsCompat.Type.navigationBars())
            isAppearanceLightStatusBars = statusBarUsesDarkIcons ?: targetDarkIcons
            isAppearanceLightNavigationBars = targetDarkIcons
        }
        if (incomingCallOverLockScreen) return
        if (!force && previousMode == mediaPreview) return
        statusBarAnimator?.cancel()
        if (previousMode == null || force) {
            insetsController.isAppearanceLightStatusBars = targetDarkIcons
            statusBarUsesDarkIcons = targetDarkIcons
            return
        }
        insetsController.isAppearanceLightStatusBars = statusBarUsesDarkIcons ?: (!previousMode && !appDarkTheme)
        var appearanceSwitched = false
        val switchPoint = if (mediaPreview) 0.44f else 0.56f
        statusBarAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 320L
            addUpdateListener { animator ->
                if (!appearanceSwitched && animator.animatedFraction >= switchPoint) {
                    appearanceSwitched = true
                    insetsController.isAppearanceLightStatusBars = targetDarkIcons
                    statusBarUsesDarkIcons = targetDarkIcons
                }
            }
            start()
        }
    }

    fun updateThemeSystemBars(darkTheme: Boolean) {
        if (appDarkTheme == darkTheme && statusBarUsesDarkIcons != null) return
        appDarkTheme = darkTheme
        updateSystemBars(systemBarsPreview == true, true)
    }
}

internal enum class Screen {
    Splash, Email, Code, Name, Chats, Archive, Settings, Chat, Profile, Group, GroupSettings, Media, AvatarSelection, Avatar, Call
}

internal enum class ProfileSection { Media, Audio, Files, Links }

internal enum class CallPhase {
    Incoming, Calling, Connecting, Active
}

internal enum class ConfirmAction {
    Logout, Delete
}

@Immutable
internal data class ChatItem(
    val id: Long,
    val userId: Long,
    val name: String,
    val defaultName: String = name,
    val customName: String = "",
    val email: String,
    val lastMessage: String,
    val updatedAt: String,
    val unread: Int,
    val lastMine: Boolean,
    val lastRead: Boolean,
    val online: Boolean,
    val lastSeenAt: String,
    val avatar: String,
    val saved: Boolean,
    val archived: Boolean = false,
    val group: Boolean = false,
    val owner: Boolean = false,
    val memberCount: Int = 0,
    val lastKind: String = "",
    val lastMessageId: Long = 0L,
    val avatarGradientSeed: Long = 0L
)

@Immutable
internal data class GroupMember(
    val id: Long,
    val name: String,
    val email: String,
    val avatar: String,
    val owner: Boolean,
    val online: Boolean,
    val lastSeenAt: String
)

@Immutable
internal data class GroupEditorState(
    val chat: ChatItem? = null,
    val name: String = "",
    val avatar: String = "",
    val members: List<GroupMember> = emptyList(),
    val selectedMemberIds: Set<Long> = emptySet(),
    val avatarGradientSeed: Long = 0L
)

@Immutable
internal data class AvatarPreviewItem(
    val name: String,
    val avatar: String,
    val userId: Long,
    val returnScreen: Screen
)

@Immutable
internal data class DevicePhoto(val id: Long, val uri: Uri)

@Immutable
internal data class DeviceMedia(
    val id: Long,
    val uri: Uri,
    val kind: String,
    val duration: Long,
    val dateAdded: Long,
    val width: Int,
    val height: Int,
    val name: String = "",
    val artist: String = "",
    val size: Long = 0L
)

internal data class MediaBatchUpload(
    val media: DeviceMedia,
    val width: Int,
    val height: Int,
    val groupId: String
)

@Immutable
internal data class DeviceStorageEntry(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val size: Long
)

@Immutable
internal data class DeviceStorageListing(
    val entries: List<DeviceStorageEntry>,
    val readable: Boolean
)

internal fun DeviceMedia.selectionKey(): String = "${kind}_$id"

internal enum class AttachmentSection { Media, Audio, Files }

internal enum class UploadContentType { Photo, Video, Media, Audio, File }

internal val MessageLinkPattern = Regex(
    """(?<![\p{L}\p{N}_@])(?:[a-z][a-z0-9+.-]*://[^\s<>\"']+|mailto:[^\s<>\"']+|[a-z0-9.!#$%&'*+/=?^_`{|}~-]+@(?:[\p{L}\p{N}](?:[\p{L}\p{N}-]{0,61}[\p{L}\p{N}])?\.)+(?:[\p{L}]{2,63}|xn--[a-z0-9-]{2,59})|(?:localhost|(?:\d{1,3}\.){3}\d{1,3})(?::\d{1,5})?(?:[/?#][^\s<>\"']*)?|(?:www\.)?(?:[\p{L}\p{N}](?:[\p{L}\p{N}-]{0,61}[\p{L}\p{N}])?\.)+(?:[\p{L}]{2,63}|xn--[a-z0-9-]{2,59})(?::\d{1,5})?(?:[/?#][^\s<>\"']*)?)""",
    RegexOption.IGNORE_CASE
)

@Immutable
internal data class MessageItem(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val senderName: String,
    val text: String,
    val createdAt: String,
    val mine: Boolean,
    val read: Boolean,
    val kind: String,
    val mediaUrl: String,
    val mediaMime: String,
    val mediaName: String,
    val mediaSize: Long,
    val mediaWidth: Int,
    val mediaHeight: Int,
    val mediaDuration: Long,
    val mediaGroupId: String,
    val replyToId: Long,
    val replySenderName: String,
    val replyText: String,
    val replyKind: String,
    val forwardedFromName: String,
    val forwardedFromId: Long,
    val edited: Boolean
)

@Immutable
internal data class ChatMessageEntry(val messages: List<MessageItem>) {
    val primary: MessageItem get() = messages.last()
    val key: String get() = if (messages.size > 1) "album_${primary.mediaGroupId}_${messages.first().id}" else primary.id.toString()
}

internal fun groupMediaMessages(messages: List<MessageItem>): List<ChatMessageEntry> {
    val grouped = ArrayList<ChatMessageEntry>(messages.size)
    messages.forEach { message ->
        val previous = grouped.lastOrNull()
        val belongsToPrevious = previous != null &&
                previous.messages.size < 4 &&
                message.mediaGroupId.isNotBlank() &&
                (message.kind == "image" || message.kind == "video") &&
                previous.primary.mediaGroupId == message.mediaGroupId &&
                previous.primary.senderId == message.senderId &&
                (previous.primary.kind == "image" || previous.primary.kind == "video")
        if (belongsToPrevious) {
            grouped[grouped.lastIndex] = previous.copy(messages = previous.messages + message)
        } else {
            grouped += ChatMessageEntry(listOf(message))
        }
    }
    return grouped
}

internal fun groupChatMessagesByDay(messages: List<MessageItem>): List<Pair<String, List<ChatMessageEntry>>> {
    if (messages.isEmpty()) return emptyList()
    val groupedDays = ArrayList<Pair<String, List<ChatMessageEntry>>>()
    var start = 0
    var day = messageDayKey(messages[0].createdAt)
    for (index in 1..messages.size) {
        val nextDay = if (index < messages.size) messageDayKey(messages[index].createdAt) else null
        if (nextDay != day) {
            groupedDays += day to groupMediaMessages(messages.subList(start, index))
            start = index
            if (nextDay != null) day = nextDay
        }
    }
    groupedDays.reverse()
    return groupedDays
}

@Immutable
internal data class CallUiState(
    val id: String,
    val chatId: Long,
    val peerId: Long,
    val peerName: String,
    val peerAvatar: String,
    val video: Boolean,
    val incoming: Boolean,
    val phase: CallPhase,
    val acceptRequested: Boolean = false,
    val offerSdp: String = "",
    val muted: Boolean = false,
    val speaker: Boolean = false,
    val cameraEnabled: Boolean = true,  
    val cameraFront: Boolean = true,
    val remoteVideo: Boolean = false,
    val remoteCameraEnabled: Boolean = true,
    val renderVersion: Int = 0,
    val startedAt: Long = 0L,
    val returnScreen: Screen = Screen.Chat
)

@Immutable
internal data class IceServerConfig(
    val urls: List<String>,
    val username: String = "",
    val credential: String = ""
)

@Immutable
internal data class AppUpdateInfo(
    val versionName: String,
    val downloadUrl: String
)

@Immutable
internal data class AppState(
    val screen: Screen = Screen.Splash,
    val email: String = "",
    val token: String = "",
    val userId: Long = 0,
    val userName: String = "",
    val userAvatar: String = "",
    val chats: List<ChatItem> = emptyList(),
    val drafts: Map<Long, String> = emptyMap(),
    val messages: List<MessageItem> = emptyList(),
    val messagesHasMore: Boolean = true,
    val loadingOlderMessages: Boolean = false,
    val currentChat: ChatItem? = null,
    val groupEditor: GroupEditorState? = null,
    val groupMembers: List<GroupMember> = emptyList(),
    val replyTo: MessageItem? = null,
    val editingMessage: MessageItem? = null,
    val editingText: String = "",
    val localMediaPreview: DeviceMedia? = null,
    val localMediaItems: List<DeviceMedia> = emptyList(),
    val selectedLocalMedia: List<DeviceMedia> = emptyList(),
    val pendingMediaGroupIds: Map<String, String> = emptyMap(),
    val reopenMediaSheet: Boolean = false,
    val avatarSelectionUri: Uri? = null,
    val avatarSelectionReturnScreen: Screen = Screen.Settings,
    val remoteMediaPreview: MessageItem? = null,
    val avatarPreview: AvatarPreviewItem? = null,
    val call: CallUiState? = null,
    val typingChatId: Long? = null,
    val typingUserId: Long? = null,
    val typingVoiceRecording: Boolean = false,
    val typingActivity: String = "",
    val loading: Boolean = false,
    val sending: Boolean = false,
    val mediaUploadProgress: Float? = null,
    val mediaUploadType: UploadContentType? = null,
    val internetAvailable: Boolean = true,
    val online: Boolean = false,
    val listsUpdating: Boolean = false,
    val availableUpdate: AppUpdateInfo? = null,
    val updateDownloading: Boolean = false,
    val addSheet: Boolean = false,
    val error: String? = null
)

@Stable
internal class MessengerController(context: Context, private val onCallFinished: () -> Unit) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val api = MessengerApi(ServerUrl)
    private val appContext = context.applicationContext
    private val preferences = context.getSharedPreferences("ise_session", Context.MODE_PRIVATE)
    private val cachePreferences = context.getSharedPreferences("ise_cache", Context.MODE_PRIVATE)
    private var socket: WebSocket? = null
    private var socketEnabled = false
    private var appForeground = false
    private var socketGeneration = 0L
    private var typingSent = false
    private var lastTypingSentAt = 0L
    private var lastTypingActivityAt = 0L
    private var typingStopJob: Job? = null
    private var remoteTypingJob: Job? = null
    private var voiceTypingJob: Job? = null
    private var voiceTypingChatId: Long? = null
    private var uploadActivityJob: Job? = null
    private var callEngine: CallEngine? = null
    private var callTimeoutJob: Job? = null
    private var incomingRingtone: Ringtone? = null
    private var incomingVibrator: Vibrator? = null
    private val pendingCallIce = ArrayList<IceCandidate>()
    private val pendingLocalCallIce = ArrayList<IceCandidate>()
    private var callSignalReady = false
    private var cachedIceServers: List<IceServerConfig>? = null
    private var notificationCallId = ""
    private var notificationCallAccept = false
    private var chatsLoading = false
    private var chatsRefreshPending = false
    private var chatsRefreshNeedsLoading = false
    private var chatsRefreshShowsUpdating = false
    private var chatsRefreshJob: Job? = null
    private var updateCheckJob: Job? = null
    private var updateDownloadJob: Job? = null
    private var pendingUpdateFile: File? = null
    private var mediaUploadJob: Job? = null
    private var mediaReturnScreen = Screen.Chat
    private var notificationChatId = 0L
    private var openSavedChatWhenLoaded = false
    private var messagesRequestId = 0L
    private var olderMessagesLoading = false
    private val pendingReadMessageIds = HashMap<Long, Long>()
    private val readJobs = HashMap<Long, Job>()
    private var socketReconnectAttempt = 0
    private var listUpdatesInFlight = 0
    private var chatReturnScreen = Screen.Chats
    private var groupReturnScreen = Screen.Chats
    var state by mutableStateOf(AppState())
        private set

    init {
        scope.launch(Dispatchers.IO) { cleanupStaleTemporaryFiles(appContext) }
        val token = preferences.getString("token", "").orEmpty()
        val userId = preferences.getLong("user_id", 0)
        val name = preferences.getString("name", "").orEmpty()
        val avatar = preferences.getString("avatar", "").orEmpty()
        val email = preferences.getString("email", "").orEmpty()
        if (token.isBlank() || userId == 0L) {
            state = state.copy(screen = Screen.Email)
        } else {
            val cachedChats = readCachedChats(userId)
            state = state.copy(
                screen = if (name.isBlank()) Screen.Name else Screen.Chats,
                token = token,
                userId = userId,
                userName = name,
                userAvatar = avatar,
                email = email,
                chats = cachedChats
            )
            restoreSession()
        }
    }

    fun requestCode(value: String) {
        val email = value.trim().lowercase(Locale.ROOT)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректную почту")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/auth/request-code", JSONObject().put("email", email)) }
                .onSuccess { state = state.copy(screen = Screen.Code, email = email, loading = false) }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun verifyCode(value: String) {
        if (value.length != 6 || value.any { it !in '0'..'9' }) {
            showError("Введите код из шести цифр")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                api.post(
                    "/auth/verify-code",
                    JSONObject().put("email", state.email).put("code", value)
                )
            }.onSuccess { response ->
                val user = response.getJSONObject("user")
                val token = response.getString("token")
                val userId = user.getLong("id")
                val name = user.optString("name")
                val avatar = user.optString("avatar")
                preferences.edit {
                    putString("token", token)
                    putLong("user_id", userId)
                    putString("name", name)
                    putString("avatar", avatar)
                    putString("email", state.email)
                }
                val needsName = response.getBoolean("needs_name")
                state = state.copy(
                    screen = if (needsName) Screen.Name else Screen.Chats,
                    token = token,
                    userId = userId,
                    userName = name,
                    userAvatar = avatar,
                    loading = false
                )
                if (!needsName) {
                    registerStoredPushToken()
                    connectSocket()
                    loadChats()
                }
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun saveName(value: String) {
        val name = value.trim()
        if (name.codePointCount(0, name.length) !in 2..40) {
            showError("Имя должно содержать от 2 до 40 символов")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/profile", JSONObject().put("name", name), state.token) }
                .onSuccess {
                    preferences.edit { putString("name", name) }
                    state = state.copy(screen = Screen.Chats, userName = name, loading = false)
                    registerStoredPushToken()
                    connectSocket()
                    loadChats()
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun openSettings() {
        state = state.copy(screen = Screen.Settings, error = null)
    }

    fun openArchive() {
        state = state.copy(screen = Screen.Archive, error = null)
    }

    fun openSavedChat() {
        state.chats.firstOrNull { it.saved }?.let {
            openSavedChatWhenLoaded = false
            openChat(it)
            return
        }
        openSavedChatWhenLoaded = true
        loadChats(silent = true)
    }

    fun updateName(value: String) {
        val name = value.trim()
        if (name.codePointCount(0, name.length) !in 2..40) {
            showError("Имя должно содержать от 2 до 40 символов")
            return
        }
        if (name == state.userName) return
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/profile", JSONObject().put("name", name), state.token) }
                .onSuccess { response ->
                    val savedName = response.optString("name", name)
                    preferences.edit { putString("name", savedName) }
                    state = state.copy(userName = savedName, loading = false)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun requestEmailChange(value: String, codeSent: () -> Unit) {
        val email = value.trim().lowercase(Locale.ROOT)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректную почту")
            return
        }
        if (email == state.email.lowercase(Locale.ROOT)) {
            showError("Это ваша текущая почта")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                api.post("/profile/email/request-code", JSONObject().put("email", email), state.token)
            }.onSuccess {
                state = state.copy(loading = false)
                codeSent()
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun confirmEmailChange(value: String, code: String, changed: () -> Unit) {
        val email = value.trim().lowercase(Locale.ROOT)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректную почту")
            return
        }
        if (code.length != 6 || code.any { it !in '0'..'9' }) {
            showError("Введите код из шести цифр")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                api.post(
                    "/profile/email/verify-code",
                    JSONObject().put("email", email).put("code", code),
                    state.token
                )
            }.onSuccess { response ->
                val savedEmail = response.optString("email", email)
                preferences.edit { putString("email", savedEmail) }
                state = state.copy(email = savedEmail, loading = false)
                changed()
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun saveAvatar(uri: Uri) {
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                val avatar = withContext(Dispatchers.IO) { encodeAvatar(appContext, uri) }
                api.post("/profile", JSONObject().put("avatar", avatar), state.token)
            }.onSuccess { response ->
                val avatar = response.optString("avatar")
                preferences.edit { putString("avatar", avatar) }
                state = state.copy(userAvatar = avatar, loading = false)
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun removeAvatar() {
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/profile", JSONObject().put("remove_avatar", true), state.token) }
                .onSuccess {
                    preferences.edit { remove("avatar") }
                    state = state.copy(userAvatar = "", loading = false)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun registerPushToken(value: String) {
        val token = value.trim()
        if (token.isBlank()) return
        preferences.edit { putString("fcm_token", token) }
        PushTokenRegistrar.register(appContext, token)
    }

    fun logoutUser() {
        val deviceToken = preferences.getString("fcm_token", "").orEmpty()
        val authToken = state.token
        if (authToken.isBlank()) {
            logout()
            return
        }
        scope.launch {
            if (deviceToken.isNotBlank()) {
                runCatching { api.post("/devices/unregister", JSONObject().put("token", deviceToken), authToken) }
            }
            runCatching { api.post("/auth/logout", JSONObject(), authToken) }
            logout()
        }
    }

    fun deleteAccount() {
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/account/delete", JSONObject(), state.token) }
                .onSuccess { logout() }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun showPermissionError() {
        showError("Разрешите доступ к фото и видео в настройках устройства")
    }

    fun showCallPermissionError() {
        showError("Разрешите доступ к микрофону и камере в настройках устройства")
    }


    fun loadChats(silent: Boolean = false, showUpdating: Boolean = false) {
        val token = state.token
        val userId = state.userId
        if (token.isBlank()) return
        if (chatsLoading) {
            chatsRefreshPending = true
            if (showUpdating) chatsRefreshShowsUpdating = true
            if (!silent) {
                chatsRefreshNeedsLoading = true
                if (!state.loading) state = state.copy(loading = true, error = null)
            }
            return
        }
        chatsLoading = true
        if (showUpdating) beginListUpdate()
        scope.launch {
            if (!silent) state = state.copy(loading = true, error = null)
            runCatching {
                val chatsJson = api.get("/chats", token).getJSONArray("chats")
                val chats = withContext(Dispatchers.Default) { parseChats(chatsJson) }
                cacheChats(userId, chats)
                chats
            }
                .onSuccess { chats ->
                    val current = state.currentChat
                    val refreshedCurrent = current?.let { active -> chats.firstOrNull { it.id == active.id } }
                    val currentMissing = current != null && refreshedCurrent == null
                    val nextScreen = if (currentMissing && state.screen in listOf(Screen.Chat, Screen.Profile, Screen.Media)) {
                        chatReturnScreen
                    } else state.screen
                    val nextMessages = if (currentMissing) emptyList() else state.messages
                    val nextMembers = if (currentMissing) emptyList() else state.groupMembers
                    val nextLoading = if (silent) state.loading else false
                    if (state.screen != nextScreen || state.chats != chats || state.currentChat != refreshedCurrent ||
                        state.messages !== nextMessages || state.groupMembers !== nextMembers || state.loading != nextLoading
                    ) {
                        state = state.copy(
                            screen = nextScreen,
                            chats = chats,
                            currentChat = refreshedCurrent,
                            messages = nextMessages,
                            groupMembers = nextMembers,
                            loading = nextLoading
                        )
                    }
                    val requestedChatId = notificationChatId
                    if (requestedChatId > 0L) {
                        notificationChatId = 0L
                        chats.firstOrNull { it.id == requestedChatId }?.let(::openChat)
                    }
                    if (openSavedChatWhenLoaded) {
                        chats.firstOrNull { it.saved }?.let {
                            openSavedChatWhenLoaded = false
                            openChat(it)
                        }
                    }
                }
                .onFailureActive { error ->
                    if (error is ApiException && error.status == 401) logout()
                    else state = state.copy(
                        loading = if (silent) state.loading else false,
                        error = if (state.internetAvailable) errorText(error) else null
                    )
                }
            chatsLoading = false
            val refreshAgain = chatsRefreshPending
            val showLoading = chatsRefreshNeedsLoading
            val showRefreshUpdating = chatsRefreshShowsUpdating
            chatsRefreshPending = false
            chatsRefreshNeedsLoading = false
            chatsRefreshShowsUpdating = false
            if (showUpdating) finishListUpdate()
            if (refreshAgain && state.token == token) loadChats(!showLoading, showRefreshUpdating)
        }
    }

    private fun scheduleChatsRefresh() {
        if (chatsRefreshJob?.isActive == true) return
        chatsRefreshJob = scope.launch {
            chatsRefreshJob = null
            loadChats(true)
        }
    }

    fun showAddSheet() {
        state = state.copy(addSheet = true)
    }

    fun hideAddSheet() {
        state = state.copy(addSheet = false)
    }

    fun createChat(value: String) {
        val email = value.trim().lowercase(Locale.ROOT)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Введите корректную почту")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.post("/chats", JSONObject().put("email", email), state.token) }
                .onSuccess { response ->
                    val chat = parseChat(response.getJSONObject("chat"))
                    state = state.copy(addSheet = false, loading = false)
                    loadChats(true)
                    openChat(chat)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun openCreateGroup() {
        groupReturnScreen = Screen.Chats
        val gradientSeed = (UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE).coerceAtLeast(1L)
        state = state.copy(
            screen = Screen.Group,
            groupEditor = GroupEditorState(avatarGradientSeed = gradientSeed),
            groupMembers = emptyList(),
            error = null
        )
    }

    fun openGroupSettings() {
        val chat = state.currentChat?.takeIf { it.group && it.owner } ?: return
        groupReturnScreen = state.screen
        state = state.copy(
            screen = Screen.GroupSettings,
            groupEditor = GroupEditorState(
                chat = chat,
                name = chat.name,
                avatar = chat.avatar,
                members = state.groupMembers,
                avatarGradientSeed = chat.avatarGradientSeed.takeIf { it != 0L } ?: chat.id
            ),
            error = null
        )
        loadGroupMembers(chat.id, true)
    }

    fun updateGroupDraftName(value: String) {
        if (value.codePointCount(0, value.length) <= 80) state.groupEditor?.let { state = state.copy(groupEditor = it.copy(name = value)) }
    }

    fun updateGroupSelection(memberIds: Set<Long>) {
        state.groupEditor?.takeIf { it.chat == null }?.let {
            state = state.copy(groupEditor = it.copy(selectedMemberIds = memberIds))
        }
    }

    fun saveGroupAvatar(uri: Uri) {
        val editor = state.groupEditor ?: return
        val chat = editor.chat
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                val avatar = withContext(Dispatchers.IO) { encodeAvatar(appContext, uri) }
                val response = chat?.let {
                    api.post(
                        "/chats/${it.id}/group",
                        JSONObject().put("name", it.name).put("avatar", avatar),
                        state.token
                    )
                }
                avatar to response
            }.onSuccess { (avatar, response) ->
                if (chat == null) {
                    state.groupEditor?.let { current ->
                        state = state.copy(groupEditor = current.copy(avatar = avatar), loading = false)
                    }
                } else {
                    applySavedGroupAvatar(chat, response?.optString("avatar", avatar).orEmpty())
                }
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun removeGroupAvatar() {
        val editor = state.groupEditor ?: return
        val chat = editor.chat
        if (chat == null) {
            state = state.copy(groupEditor = editor.copy(avatar = ""))
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                api.post(
                    "/chats/${chat.id}/group",
                    JSONObject().put("name", chat.name).put("remove_avatar", true),
                    state.token
                )
            }.onSuccess { response ->
                applySavedGroupAvatar(chat, response.optString("avatar"))
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    private fun applySavedGroupAvatar(chat: ChatItem, avatar: String) {
        val updated = chat.copy(avatar = avatar)
        state = state.copy(
            currentChat = state.currentChat?.let { if (it.id == updated.id) updated else it },
            chats = state.chats.map { if (it.id == updated.id) updated else it },
            groupEditor = state.groupEditor?.let { editor ->
                if (editor.chat?.id == updated.id) editor.copy(chat = updated, avatar = avatar) else editor
            },
            loading = false
        )
        loadChats(true)
    }

    fun createGroup() {
        val editor = state.groupEditor?.takeIf { it.chat == null } ?: return
        val name = editor.name.trim()
        if (name.codePointCount(0, name.length) !in 2..80) {
            showError("Название должно содержать от 2 до 80 символов")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            val memberIds = JSONArray().apply { editor.selectedMemberIds.forEach { put(it) } }
            val body = JSONObject().put("name", name).put("member_ids", memberIds)
                .put("avatar_gradient_seed", editor.avatarGradientSeed)
            if (editor.avatar.isNotBlank()) body.put("avatar", editor.avatar)
            runCatching { api.post("/groups", body, state.token) }
                .onSuccess { response ->
                    val chat = parseChat(response.getJSONObject("chat"))
                    state = state.copy(groupEditor = null, loading = false)
                    loadChats(true)
                    openChat(chat)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun applyGroupName() {
        val editor = state.groupEditor ?: return
        val name = editor.name.trim()
        if (name.codePointCount(0, name.length) !in 2..80) {
            showError("Название должно содержать от 2 до 80 символов")
            return
        }
        if (editor.chat == null) state = state.copy(groupEditor = editor.copy(name = name)) else saveGroup()
    }

    fun saveGroup() {
        val editor = state.groupEditor ?: return
        val chat = editor.chat ?: return
        val name = editor.name.trim()
        if (name.codePointCount(0, name.length) !in 2..80) {
            showError("Название должно содержать от 2 до 80 символов")
            return
        }
        scope.launch {
            state = state.copy(loading = true, error = null)
            val body = JSONObject().put("name", name)
            runCatching { api.post("/chats/${chat.id}/group", body, state.token) }
                .onSuccess { response ->
                    val updated = chat.copy(
                        name = response.optString("name", name),
                        avatar = response.optString("avatar"),
                        memberCount = response.optInt("member_count", chat.memberCount)
                    )
                    state = state.copy(
                        currentChat = updated,
                        chats = state.chats.map { if (it.id == updated.id) updated else it },
                        groupEditor = state.groupEditor?.copy(chat = updated, name = updated.name, avatar = updated.avatar),
                        loading = false
                    )
                    loadChats(true)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun addGroupMembers(memberIds: Set<Long>) {
        val editor = state.groupEditor ?: return
        val chat = editor.chat ?: return
        if (memberIds.isEmpty()) return
        scope.launch {
            state = state.copy(loading = true, error = null)
            val ids = JSONArray().apply { memberIds.forEach { put(it) } }
            runCatching { api.post("/chats/${chat.id}/members", JSONObject().put("member_ids", ids), state.token) }
                .onSuccess {
                    state = state.copy(loading = false)
                    loadGroupMembers(chat.id, true)
                    loadChats(true)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun removeGroupMember(member: GroupMember) {
        val editor = state.groupEditor ?: return
        val chat = editor.chat ?: return
        if (member.owner || member.id == state.userId) return
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching {
                api.post("/chats/${chat.id}/member-remove", JSONObject().put("member_id", member.id), state.token)
            }.onSuccess {
                val members = editor.members.filterNot { it.id == member.id }
                val updatedChat = chat.copy(memberCount = members.size)
                state = state.copy(
                    loading = false,
                    groupMembers = if (state.currentChat?.id == chat.id) members else state.groupMembers,
                    currentChat = state.currentChat?.let { if (it.id == chat.id) updatedChat else it },
                    chats = state.chats.map { if (it.id == chat.id) updatedChat else it },
                    groupEditor = editor.copy(chat = updatedChat, members = members)
                )
                loadChats(true)
            }.onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun loadCurrentGroupMembers() {
        state.currentChat?.takeIf { it.group }?.let { loadGroupMembers(it.id, true) }
    }

    private fun loadGroupMembers(chatId: Long, updateEditor: Boolean = false, showUpdating: Boolean = false) {
        val token = state.token
        if (showUpdating) beginListUpdate()
        scope.launch {
            try {
                runCatching {
                    val members = api.get("/chats/$chatId/members", token).getJSONArray("members")
                    withContext(Dispatchers.Default) { parseGroupMembers(members) }
                }
                    .onSuccess { members ->
                        val editor = state.groupEditor
                        state = state.copy(
                            groupMembers = if (state.currentChat?.id == chatId || editor?.chat?.id == chatId) members else state.groupMembers,
                            groupEditor = if (updateEditor && editor?.chat?.id == chatId) editor.copy(members = members) else editor
                        )
                    }
                    .onFailureActive { state = state.copy(error = errorText(it)) }
            } finally {
                if (showUpdating) finishListUpdate()
            }
        }
    }

    fun openChat(chat: ChatItem) {
        remoteTypingJob?.cancel()
        cancelChatNotification(appContext, chat.id)
        chatReturnScreen = if (state.screen == Screen.Archive) Screen.Archive else Screen.Chats
        val cachedMessages = if (state.currentChat?.id == chat.id && state.messages.isNotEmpty()) {
            state.messages
        } else {
            readCachedMessages(state.userId, chat.id)
        }
        state = state.copy(
            screen = Screen.Chat,
            currentChat = chat,
            chats = state.chats,
            messages = cachedMessages,
            messagesHasMore = cachedMessages.size >= 100,
            loadingOlderMessages = false,
            replyTo = null,
            editingMessage = null,
            editingText = "",
            selectedLocalMedia = emptyList(),
            pendingMediaGroupIds = emptyMap(),
            reopenMediaSheet = false,
            typingChatId = null,
            typingUserId = null,
            typingVoiceRecording = false,
            typingActivity = "",
            groupMembers = if (chat.group) state.groupMembers.takeIf { state.currentChat?.id == chat.id }.orEmpty() else emptyList(),
            error = null
        )
        if (state.internetAvailable) {
            loadMessages(chat.id)
            if (chat.group) loadGroupMembers(chat.id)
        }
    }

    fun openChatFromNotification(chatId: Long) {
        if (chatId < 1L) return
        cancelChatNotification(appContext, chatId)
        state.chats.firstOrNull { it.id == chatId }?.let {
            notificationChatId = 0L
            openChat(it)
            return
        }
        notificationChatId = chatId
        loadChats(true)
    }

    fun startCall(video: Boolean) {
        val chat = state.currentChat ?: return
        if (chat.saved || chat.group || state.call != null) return
        if (socket == null || !state.online) {
            showError("Нет соединения с сервером")
            return
        }
        val call = CallUiState(
            id = UUID.randomUUID().toString(),
            chatId = chat.id,
            peerId = chat.userId,
            peerName = chat.name,
            peerAvatar = chat.avatar,
            video = video,
            incoming = false,
            phase = CallPhase.Calling,
            speaker = true,
            returnScreen = Screen.Chat
        )
        state = state.copy(screen = Screen.Call, call = call, error = null)
        if (!startCallService(call)) {
            finishCall("Не удалось запустить звонок в фоне")
            return
        }
        prepareCallEngine(call, true)
        scheduleCallTimeout(call.id)
    }

    fun requestNotificationCall(
        callId: String,
        accept: Boolean,
        chatId: Long,
        callerId: Long,
        callerName: String,
        video: Boolean
    ) {
        if (callId.isBlank()) return
        notificationCallId = callId
        notificationCallAccept = accept
        val call = state.call
        if (call?.id == callId && call.incoming && accept) {
            if (call.offerSdp.isNotBlank()) {
                state = state.copy(call = call.copy(acceptRequested = true))
                notificationCallId = ""
                notificationCallAccept = false
            }
        } else if (call == null && chatId > 0L && callerId > 0L) {
            val chat = state.chats.firstOrNull { it.id == chatId || it.userId == callerId }
            val returnScreen = when {
                state.screen == Screen.Chat && state.currentChat?.id == chatId -> Screen.Chat
                state.screen == Screen.Archive -> Screen.Archive
                else -> Screen.Chats
            }
            val pendingCall = CallUiState(
                id = callId,
                chatId = chatId,
                peerId = callerId,
                peerName = callerName.ifBlank { chat?.name.orEmpty().ifBlank { "Ise Messenger" } },
                peerAvatar = chat?.avatar.orEmpty(),
                video = video,
                incoming = true,
                phase = CallPhase.Incoming,
                returnScreen = returnScreen
            )
            state = state.copy(screen = Screen.Call, call = pendingCall, error = null)
            startIncomingAlert()
            scheduleCallTimeout(callId)
        }
        connectSocket()
    }

    fun acceptCall() {
        val call = state.call?.takeIf { it.incoming && it.phase == CallPhase.Incoming } ?: return
        if (call.offerSdp.isBlank()) {
            state = state.copy(call = call.copy(acceptRequested = true))
            return
        }
        stopIncomingAlert()
        state = state.copy(call = call.copy(phase = CallPhase.Connecting, speaker = true))
        if (!startCallService(call)) {
            finishCall("Не удалось запустить звонок в фоне")
            return
        }
        prepareCallEngine(call, false)
    }

    fun declineCall() {
        val call = state.call ?: return
        val sent = sendCallSignal("call_decline", call, reason = "declined")
        if (!sent && call.incoming) {
            val token = state.token
            scope.launch {
                runCatching {
                    api.post(
                        "/calls/action",
                        JSONObject().put("call_id", call.id).put("action", "decline"),
                        token
                    )
                }
            }
        }
        finishCall()
    }

    fun endCall() {
        val call = state.call ?: return
        sendCallSignal("call_end", call, reason = "ended")
        finishCall()
    }

    fun toggleCallMute() {
        val call = state.call ?: return
        val muted = !call.muted
        callEngine?.setMuted(muted)
        state = state.copy(call = call.copy(muted = muted))
    }

    fun toggleCallSpeaker() {
        val call = state.call ?: return
        val speaker = !call.speaker
        callEngine?.setSpeaker(speaker)
        state = state.copy(call = call.copy(speaker = speaker))
    }

    fun toggleCallCamera() {
        val call = state.call?.takeIf { it.video } ?: return
        val enabled = !call.cameraEnabled
        callEngine?.setCameraEnabled(enabled)
        state = state.copy(call = call.copy(cameraEnabled = enabled))
        sendCallCameraState(call, enabled)
    }

    fun switchCallCamera() {
        callEngine?.switchCamera()
    }

    fun callVideoTrack(local: Boolean): VideoTrack? {
        return if (local) callEngine?.localVideoTrack else callEngine?.remoteVideoTrack
    }

    fun callEglContext(): EglBase.Context? = callEngine?.eglContext

    private fun prepareCallEngine(call: CallUiState, caller: Boolean) {
        scope.launch {
            val servers = loadIceServers()
            if (state.call?.id != call.id) return@launch
            val engine = runCatching {
                CallEngine(
                    context = appContext,
                    video = call.video,
                    iceServers = servers,
                    onIceCandidate = { candidate -> scope.launch { sendOrQueueLocalIce(call.id, candidate) } },
                    onConnected = { scope.launch { setCallConnected(call.id) } },
                    onRemoteVideo = { scope.launch { setRemoteCallVideo(call.id) } },
                    onCameraSwitched = { front -> scope.launch { setLocalCameraFacing(call.id, front) } },
                    onFailure = { scope.launch { failCall(call.id) } }
                )
            }.getOrElse {
                finishCall("Не удалось запустить звонок")
                return@launch
            }
            if (state.call?.id != call.id) {
                runCatching { engine.close() }
                return@launch
            }
            callEngine = engine
            state.call?.takeIf { it.id == call.id }?.let {
                state = state.copy(call = it.copy(renderVersion = it.renderVersion + 1))
            }
            val started = runCatching {
                pendingCallIce.forEach(engine::addRemoteIce)
                pendingCallIce.clear()
                if (caller) {
                    engine.createOffer { description -> scope.launch { sendLocalDescription(call.id, "call_offer", description) } }
                } else {
                    engine.createAnswer(call.offerSdp) { description -> scope.launch { sendLocalDescription(call.id, "call_answer", description) } }
                }
            }.isSuccess
            if (!started && state.call?.id == call.id) {
                if (callEngine === engine) callEngine = null
                runCatching { engine.close() }
                finishCall("Не удалось установить соединение")
            }
        }
    }

    private suspend fun loadIceServers(): List<IceServerConfig> {
        cachedIceServers?.let { return it }
        val loaded = runCatching {
            val array = api.get("/calls/config", state.token).getJSONArray("ice_servers")
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    val urlsValue = item.opt("urls")
                    val urls = when (urlsValue) {
                        is JSONArray -> buildList { for (urlIndex in 0 until urlsValue.length()) add(urlsValue.getString(urlIndex)) }
                        is String -> listOf(urlsValue)
                        else -> emptyList()
                    }.map(String::trim)
                        .filter { url ->
                            url.isNotEmpty() && !url.any(Char::isWhitespace) &&
                                    (url.startsWith("stun:", true) || url.startsWith("turn:", true) || url.startsWith("turns:", true))
                        }
                        .distinct()
                    if (urls.isNotEmpty()) add(IceServerConfig(urls, item.optString("username"), item.optString("credential")))
                }
            }
        }.getOrDefault(emptyList())
        val result = loaded.ifEmpty { listOf(IceServerConfig(listOf("stun:stun.l.google.com:19302"))) }
        if (loaded.isNotEmpty()) cachedIceServers = result
        return result
    }

    private fun sendLocalDescription(callId: String, type: String, description: SessionDescription) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        if (!sendCallSignal(type, call, sdp = description.description)) {
            finishCall("Не удалось установить соединение")
            return
        }
        callSignalReady = true
        val unsent = pendingLocalCallIce.filterNot { sendCallIce(call, it) }
        pendingLocalCallIce.clear()
        pendingLocalCallIce.addAll(unsent)
    }

    private fun sendOrQueueLocalIce(callId: String, candidate: IceCandidate) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        if (!callSignalReady || !sendCallIce(call, candidate)) pendingLocalCallIce.add(candidate)
    }

    private fun sendCallIce(call: CallUiState, candidate: IceCandidate): Boolean {
        val event = JSONObject()
            .put("type", "call_ice")
            .put("chat_id", call.chatId)
            .put("call_id", call.id)
            .put("candidate", candidate.sdp)
            .put("sdp_mid", candidate.sdpMid.orEmpty())
            .put("sdp_mline_index", candidate.sdpMLineIndex)
        return socket?.send(event.toString()) == true
    }

    private fun sendCallCameraState(call: CallUiState, enabled: Boolean) {
        val event = JSONObject()
            .put("type", "call_video_state")
            .put("chat_id", call.chatId)
            .put("call_id", call.id)
            .put("camera_enabled", enabled)
        socket?.send(event.toString())
    }

    private fun sendCallSignal(type: String, call: CallUiState, sdp: String = "", reason: String = ""): Boolean {
        val event = JSONObject().put("type", type).put("chat_id", call.chatId).put("call_id", call.id)
        if (sdp.isNotEmpty()) event.put("sdp", sdp)
        if (type == "call_offer") event.put("video", call.video)
        if (reason.isNotEmpty()) event.put("reason", reason)
        return socket?.send(event.toString()) == true
    }

    private fun handleCallSignal(event: JSONObject) {
        val type = event.optString("type")
        val callId = event.optString("call_id")
        val chatId = event.optLong("chat_id")
        if (callId.isBlank() || chatId < 1L) return
        if (type == "call_offer") {
            val active = state.call
            if (active != null) {
                if (active.id == callId && active.incoming && active.offerSdp.isBlank()) {
                    val peerId = event.optLong("from_user_id", active.peerId)
                    val chat = state.chats.firstOrNull { it.id == chatId || it.userId == peerId }
                    val acceptRequested = notificationCallId == callId && notificationCallAccept
                    val updated = active.copy(
                        chatId = chatId,
                        peerId = peerId,
                        peerName = chat?.name ?: event.optString("from_name").ifBlank { active.peerName },
                        peerAvatar = chat?.avatar ?: active.peerAvatar,
                        video = event.optBoolean("video", active.video),
                        acceptRequested = acceptRequested,
                        offerSdp = event.optString("sdp")
                    )
                    notificationCallId = ""
                    notificationCallAccept = false
                    cancelIncomingCallNotification(appContext, callId)
                    state = state.copy(screen = Screen.Call, call = updated, error = null)
                    return
                }
                val busy = CallUiState(
                    id = callId,
                    chatId = chatId,
                    peerId = event.optLong("from_user_id"),
                    peerName = "",
                    peerAvatar = "",
                    video = event.optBoolean("video"),
                    incoming = true,
                    phase = CallPhase.Incoming
                )
                sendCallSignal("call_busy", busy, reason = "busy")
                cancelIncomingCallNotification(appContext, callId)
                return
            }
            val peerId = event.optLong("from_user_id")
            val chat = state.currentChat?.takeIf { it.id == chatId } ?: state.chats.firstOrNull { it.id == chatId } ?: ChatItem(
                id = chatId,
                userId = peerId,
                name = event.optString("from_name").ifBlank { "Ise Messenger" },
                email = "",
                lastMessage = "",
                updatedAt = "",
                unread = 0,
                lastMine = false,
                lastRead = false,
                online = false,
                lastSeenAt = "",
                avatar = "",
                saved = false
            )
            if (chat.saved || chat.group) return
            val returnScreen = when {
                state.screen == Screen.Chat && state.currentChat?.id == chatId -> Screen.Chat
                state.screen == Screen.Archive -> Screen.Archive
                else -> Screen.Chats
            }
            val acceptRequested = notificationCallId == callId && notificationCallAccept
            val call = CallUiState(
                id = callId,
                chatId = chatId,
                peerId = event.optLong("from_user_id", chat.userId),
                peerName = chat.name,
                peerAvatar = chat.avatar,
                video = event.optBoolean("video"),
                incoming = true,
                phase = CallPhase.Incoming,
                acceptRequested = acceptRequested,
                offerSdp = event.optString("sdp"),
                returnScreen = returnScreen
            )
            pendingCallIce.clear()
            pendingLocalCallIce.clear()
            callSignalReady = false
            if (notificationCallId == callId) {
                notificationCallId = ""
                notificationCallAccept = false
            }
            cancelIncomingCallNotification(appContext, callId)
            state = state.copy(screen = Screen.Call, call = call, error = null)
            startIncomingAlert()
            scheduleCallTimeout(call.id)
            return
        }
        val call = state.call?.takeIf { it.id == callId && it.chatId == chatId } ?: return
        when (type) {
            "call_answer" -> {
                state = state.copy(call = call.copy(phase = CallPhase.Connecting))
                callEngine?.setRemoteAnswer(event.optString("sdp"))
            }
            "call_ice" -> {
                val candidate = IceCandidate(
                    event.optString("sdp_mid").ifBlank { null },
                    event.optInt("sdp_mline_index"),
                    event.optString("candidate")
                )
                callEngine?.addRemoteIce(candidate) ?: pendingCallIce.add(candidate)
            }
            "call_decline" -> finishCall()
            "call_busy" -> finishCall()
            "call_unavailable" -> finishCall()
            "call_video_state" -> state = state.copy(call = call.copy(remoteCameraEnabled = event.optBoolean("camera_enabled")))
            "call_end" -> finishCall()
        }
    }

    private fun setCallConnected(callId: String) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        callTimeoutJob?.cancel()
        state = state.copy(call = call.copy(phase = CallPhase.Active, startedAt = System.currentTimeMillis()))
    }

    private fun setRemoteCallVideo(callId: String) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        state = state.copy(call = call.copy(remoteVideo = true, renderVersion = call.renderVersion + 1))
    }

    private fun setLocalCameraFacing(callId: String, front: Boolean) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        state = state.copy(call = call.copy(cameraFront = front, renderVersion = call.renderVersion + 1))
    }

    private fun failCall(callId: String) {
        val call = state.call?.takeIf { it.id == callId } ?: return
        sendCallSignal("call_end", call, reason = "failed")
        finishCall("Соединение прервано")
    }

    private fun scheduleCallTimeout(callId: String) {
        callTimeoutJob?.cancel()
        callTimeoutJob = scope.launch {
            delay(45_000)
            val call = state.call?.takeIf { it.id == callId && it.phase != CallPhase.Active } ?: return@launch
            sendCallSignal(if (call.incoming) "call_decline" else "call_end", call, reason = "timeout")
            finishCall()
        }
    }

    @Suppress("DEPRECATION")
    private fun startIncomingAlert() {
        stopIncomingAlert()
        incomingRingtone = runCatching {
            RingtoneManager.getRingtone(appContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))?.apply { play() }
        }.getOrNull()
        incomingVibrator = runCatching {
            val vibrator = if (Build.VERSION.SDK_INT >= 31) {
                appContext.getSystemService(VibratorManager::class.java).defaultVibrator
            } else {
                appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (vibrator.hasVibrator()) {
                val pattern = longArrayOf(0, 700, 450, 700, 450)
                if (Build.VERSION.SDK_INT >= 26) {
                    val attributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0), attributes)
                } else {
                    vibrator.vibrate(pattern, 0)
                }
                vibrator
            } else null
        }.getOrNull()
    }

    private fun stopIncomingAlert() {
        runCatching { incomingRingtone?.stop() }
        incomingRingtone = null
        runCatching { incomingVibrator?.cancel() }
        incomingVibrator = null
    }

    private fun startCallService(call: CallUiState): Boolean {
        if (!hasCallPermissions(appContext, call.video)) return false
        val intent = Intent(appContext, CallForegroundService::class.java)
            .putExtra("peer_name", call.peerName)
            .putExtra("video", call.video)
        return runCatching { ContextCompat.startForegroundService(appContext, intent) }.isSuccess
    }

    private fun finishCall(message: String? = null) {
        val call = state.call ?: return
        callTimeoutJob?.cancel()
        callTimeoutJob = null
        stopIncomingAlert()
        appContext.stopService(Intent(appContext, CallForegroundService::class.java))
        val engine = callEngine
        callEngine = null
        runCatching { engine?.close() }
        pendingCallIce.clear()
        pendingLocalCallIce.clear()
        callSignalReady = false
        notificationCallId = ""
        notificationCallAccept = false
        cancelIncomingCallNotification(appContext, call.id)
        onCallFinished()
        val destination = when {
            call.returnScreen == Screen.Chat && state.currentChat != null -> Screen.Chat
            call.returnScreen == Screen.Archive -> Screen.Archive
            else -> Screen.Chats
        }
        state = state.copy(screen = destination, call = null, error = message ?: state.error)
        if (!appForeground) {
            socketEnabled = false
            socketGeneration++
            val activeSocket = socket
            socket = null
            state = state.copy(online = false)
            activeSocket?.cancel()
        }
    }

    fun selectReply(message: MessageItem) {
        state = state.copy(replyTo = message, editingMessage = null, editingText = "")
    }

    fun cancelReply() {
        state = state.copy(replyTo = null)
    }

    fun startEditing(message: MessageItem) {
        if (!message.mine || message.kind != "text") return
        stopTyping()
        state = state.copy(replyTo = null, editingMessage = message, editingText = message.text)
    }

    fun cancelEditing() {
        state = state.copy(editingMessage = null, editingText = "")
    }

    fun updateEditingText(value: String) {
        if (state.editingMessage != null) state = state.copy(editingText = value)
    }

    fun editMessage(value: String) {
        val editing = state.editingMessage ?: return
        val text = value.trim()
        if (text.isBlank() || state.sending) return
        if (text == editing.text) {
            cancelEditing()
            return
        }
        scope.launch {
            state = state.copy(sending = true, error = null)
            runCatching {
                api.patch("/messages/${editing.id}", JSONObject().put("text", text), state.token)
            }.onSuccess { response ->
                val updated = parseMessage(response.getJSONObject("message")).copy(mine = true)
                state = state.copy(
                    sending = false,
                    editingMessage = null,
                    editingText = "",
                    messages = state.messages.map { if (it.id == updated.id) updated else it }
                )
                cacheMessages(state.userId, updated.chatId, state.messages)
                loadChats(true)
            }.onFailureActive {
                state = state.copy(sending = false, error = errorText(it))
            }
        }
    }

    fun updateDraft(chatId: Long, value: String) {
        if (chatId < 1L) return
        val drafts = if (value.isBlank()) state.drafts - chatId else state.drafts + (chatId to value)
        if (drafts == state.drafts) return
        state = state.copy(drafts = drafts)
    }

    fun typingChanged(hasText: Boolean) {
        val chat = state.currentChat ?: return
        if (state.mediaUploadProgress != null) return
        if (!hasText) {
            stopTyping()
            return
        }
        val now = SystemClock.elapsedRealtime()
        lastTypingActivityAt = now
        if (!typingSent || now - lastTypingSentAt >= 1200) {
            sendTyping(chat.id, true)
            typingSent = true
            lastTypingSentAt = now
        }
        if (typingStopJob?.isActive == true) return
        typingStopJob = scope.launch {
            while (true) {
                val remaining = 1800L - (SystemClock.elapsedRealtime() - lastTypingActivityAt)
                if (remaining <= 0L) break
                delay(remaining)
            }
            typingStopJob = null
            if (typingSent && state.currentChat?.id == chat.id) {
                sendTyping(chat.id, false)
                typingSent = false
            }
        }
    }

    fun voiceRecordingChanged(recording: Boolean) {
        if (!recording) {
            stopVoiceTyping()
            return
        }
        val chatId = state.currentChat?.id ?: return
        stopTyping()
        stopVoiceTyping()
        voiceTypingChatId = chatId
        sendTyping(chatId, true, activity = "voice_recording")
        voiceTypingJob = scope.launch {
            while (voiceTypingChatId == chatId && state.currentChat?.id == chatId) {
                delay(1500)
                if (voiceTypingChatId == chatId) sendTyping(chatId, true, activity = "voice_recording")
            }
        }
    }

    fun sendMessage(value: String) {
        val chat = state.currentChat ?: return
        val text = value.trim()
        if (text.isBlank() || state.sending) return
        if (!state.internetAvailable) {
            updateDraft(chat.id, text)
            return
        }
        val replyTo = state.replyTo
        stopTyping()
        scope.launch {
            state = state.copy(sending = true, replyTo = null, drafts = state.drafts - chat.id, error = null)
            runCatching {
                api.post(
                    "/chats/${chat.id}/messages",
                    JSONObject().put("text", text).put("reply_to_id", replyTo?.id ?: 0L),
                    state.token
                )
            }.onSuccess { response ->
                appendMessage(parseMessage(response.getJSONObject("message")))
                state = state.copy(sending = false)
                loadChats(true)
            }.onFailureActive {
                state = state.copy(
                    sending = false,
                    replyTo = state.replyTo ?: replyTo,
                    drafts = state.drafts + (chat.id to text),
                    error = if (state.internetAvailable) errorText(it) else null
                )
            }
        }
    }

    fun deleteMessage(message: MessageItem) {
        scope.launch {
            runCatching { api.delete("/messages/${message.id}", state.token) }
                .onSuccess {
                    state = state.copy(
                        messages = state.messages.filterNot { it.id == message.id },
                        replyTo = state.replyTo?.takeUnless { it.id == message.id },
                        editingMessage = state.editingMessage?.takeUnless { it.id == message.id },
                        editingText = if (state.editingMessage?.id == message.id) "" else state.editingText
                    )
                    cacheMessages(state.userId, message.chatId, state.messages)
                    loadChats(true)
                }
                .onFailureActive { state = state.copy(error = errorText(it)) }
        }
    }

    fun deleteChat(chat: ChatItem) {
        scope.launch {
            runCatching { api.delete("/chats/${chat.id}", state.token) }
                .onSuccess {
                    state = state.copy(
                        chats = state.chats.filterNot { it.id == chat.id },
                        drafts = state.drafts - chat.id
                    )
                }
                .onFailureActive { state = state.copy(error = errorText(it)) }
        }
    }

    fun setPersonalChatName(chat: ChatItem, value: String) {
        if (chat.saved || chat.group) return
        val customName = value.trim()
        if (customName.codePointCount(0, customName.length) > 80) {
            showError("Имя должно содержать не более 80 символов")
            return
        }
        val previous = state.chats.firstOrNull { it.id == chat.id } ?: chat
        val updated = previous.copy(
            name = customName.ifBlank { previous.defaultName },
            customName = customName
        )
        state = state.copy(
            chats = state.chats.map { if (it.id == chat.id) updated else it },
            currentChat = state.currentChat?.let { if (it.id == chat.id) updated else it },
            error = null
        )
        scope.launch {
            runCatching {
                api.post(
                    "/chats/${chat.id}/name",
                    JSONObject().put("name", customName),
                    state.token
                )
            }.onFailureActive { error ->
                state = state.copy(
                    chats = state.chats.map {
                        if (it.id == chat.id && it.customName == customName) previous else it
                    },
                    currentChat = state.currentChat?.let {
                        if (it.id == chat.id && it.customName == customName) previous else it
                    },
                    error = errorText(error)
                )
            }
        }
    }

    fun setChatArchived(chat: ChatItem, archived: Boolean) {
        if (chat.archived == archived) return
        state = state.copy(
            chats = state.chats.map { if (it.id == chat.id) it.copy(archived = archived) else it },
            currentChat = state.currentChat?.let { if (it.id == chat.id) it.copy(archived = archived) else it }
        )
        scope.launch {
            runCatching {
                api.post(
                    "/chats/${chat.id}/archive",
                    JSONObject().put("archived", archived),
                    state.token
                )
            }.onSuccess { response ->
                val saved = response.optBoolean("archived", archived)
                state = state.copy(
                    chats = state.chats.map { if (it.id == chat.id) it.copy(archived = saved) else it },
                    currentChat = state.currentChat?.let { if (it.id == chat.id) it.copy(archived = saved) else it }
                )
            }.onFailureActive { error ->
                state = state.copy(
                    chats = state.chats.map { if (it.id == chat.id && it.archived == archived) it.copy(archived = chat.archived) else it },
                    currentChat = state.currentChat?.let {
                        if (it.id == chat.id && it.archived == archived) it.copy(archived = chat.archived) else it
                    },
                    error = errorText(error)
                )
            }
        }
    }

    fun clearChat(chat: ChatItem) {
        scope.launch {
            state = state.copy(loading = true, error = null)
            runCatching { api.delete("/chats/${chat.id}/messages", state.token) }
                .onSuccess {
                    state = state.copy(
                        loading = false,
                        messages = if (state.currentChat?.id == chat.id) emptyList() else state.messages,
                        replyTo = if (state.currentChat?.id == chat.id) null else state.replyTo,
                        editingMessage = if (state.currentChat?.id == chat.id) null else state.editingMessage,
                        editingText = if (state.currentChat?.id == chat.id) "" else state.editingText,
                        chats = state.chats.map {
                            if (it.id == chat.id) it.copy(lastMessage = "", unread = 0, lastMine = false, lastRead = false) else it
                        }
                    )
                    cacheMessages(state.userId, chat.id, emptyList())
                    cacheChats(state.userId, state.chats)
                    loadChats(true)
                }
                .onFailureActive { state = state.copy(loading = false, error = errorText(it)) }
        }
    }

    fun forwardMessage(message: MessageItem, chat: ChatItem) {
        stopTyping()
        scope.launch {
            runCatching {
                api.post("/messages/${message.id}/forward", JSONObject().put("chat_id", chat.id), state.token)
            }.onSuccess { response ->
                val forwarded = parseMessage(response.getJSONObject("message"))
                if (state.currentChat?.id == forwarded.chatId) {
                    appendMessage(forwarded)
                } else {
                    remoteTypingJob?.cancel()
                    cancelChatNotification(appContext, chat.id)
                    state = state.copy(
                        screen = Screen.Chat,
                        currentChat = chat.copy(lastMessage = messagePreviewText(forwarded), unread = 0, lastMine = true, lastRead = false),
                        messages = listOf(forwarded.copy(mine = true)),
                        groupMembers = emptyList(),
                        replyTo = null,
                        typingChatId = null,
                        typingUserId = null,
                        typingVoiceRecording = false,
                        typingActivity = "",
                        error = null
                    )
                    loadMessages(chat.id)
                    if (chat.group) loadGroupMembers(chat.id)
                }
                loadChats(true)
            }.onFailureActive { state = state.copy(error = errorText(it)) }
        }
    }

    fun sendMedia(media: List<DeviceMedia>, sent: () -> Unit) {
        val chat = state.currentChat ?: return
        val outgoing = media.filter { it.kind == "image" || it.kind == "video" || it.kind == "audio" }
            .distinctBy { it.selectionKey() }
        if (outgoing.isEmpty() || state.sending || mediaUploadJob?.isActive == true) return
        val existingGroupIds = state.pendingMediaGroupIds
        val mediaGroupIds = if (outgoing.all { existingGroupIds.containsKey(it.selectionKey()) }) {
            existingGroupIds
        } else {
            buildMap {
                outgoing.filter { it.kind == "audio" }.forEach { item -> put(item.selectionKey(), "") }
                outgoing.filter { it.kind == "image" || it.kind == "video" }.chunked(4).forEach { group ->
                    val groupId = if (outgoing.size == 1) "" else UUID.randomUUID().toString()
                    group.forEach { item -> put(item.selectionKey(), groupId) }
                }
            }
        }
        val replyTo = state.replyTo
        stopTyping()
        mediaUploadJob = scope.launch {
            try {
                val currentUploadType = when {
                    outgoing.all { it.kind == "audio" } -> UploadContentType.Audio
                    outgoing.all { it.kind == "image" } -> UploadContentType.Photo
                    outgoing.all { it.kind == "video" } -> UploadContentType.Video
                    else -> UploadContentType.Media
                }
                state = state.copy(
                    sending = true,
                    replyTo = null,
                    pendingMediaGroupIds = mediaGroupIds,
                    reopenMediaSheet = false,
                    mediaUploadProgress = 0f,
                    mediaUploadType = currentUploadType,
                    error = null
                )
                startUploadActivity(chat.id, currentUploadType)
                val uploads = outgoing.map { item ->
                    val measured = if (item.kind == "audio") {
                        0 to 0
                    } else if (item.kind == "video" || item.width < 1 || item.height < 1) {
                        withContext(Dispatchers.IO) { resolveMediaDimensions(appContext, item.uri, item.kind) }
                    } else item.width to item.height
                    val dimensions = if (measured.first > 0 && measured.second > 0) measured else item.width to item.height
                    MediaBatchUpload(
                        media = item,
                        width = dimensions.first,
                        height = dimensions.second,
                        groupId = mediaGroupIds[item.selectionKey()].orEmpty()
                    )
                }
                val messages = if (uploads.size == 1) {
                    val upload = uploads.first()
                    val response = api.postMedia(
                        "/chats/${chat.id}/media",
                        appContext,
                        upload.media.uri,
                        upload.media.kind,
                        upload.width,
                        upload.height,
                        upload.media.duration,
                        replyTo?.id ?: 0L,
                        upload.groupId,
                        state.token
                    ) { progress ->
                        scope.launch {
                            if (state.mediaUploadProgress != null) {
                                state = state.copy(mediaUploadProgress = progress)
                            }
                        }
                    }
                    JSONArray().put(response.getJSONObject("message"))
                } else {
                    api.postMediaBatch(
                        "/chats/${chat.id}/media-batch",
                        appContext,
                        uploads,
                        replyTo?.id ?: 0L,
                        state.token
                    ) { progress ->
                        scope.launch {
                            if (state.mediaUploadProgress != null) {
                                state = state.copy(mediaUploadProgress = progress)
                            }
                        }
                    }.getJSONArray("messages")
                }
                if (messages.length() != uploads.size) throw Exception("Не удалось отправить медиа")
                val parsedMessages = mutableListOf<MessageItem>()
                uploads.forEachIndexed { index, upload ->
                    val message = messages.getJSONObject(index)
                    parsedMessages += parseMessage(message)
                    withContext(Dispatchers.IO) {
                        runCatching {
                            storeLocalMedia(
                                appContext,
                                upload.media.uri,
                                message.optString("media_url"),
                                upload.media.kind,
                                message.optString("media_name")
                            )
                        }
                    }
                }
                parsedMessages.forEach { appendMessage(it) }
                state = state.copy(
                    sending = false,
                    selectedLocalMedia = emptyList(),
                    pendingMediaGroupIds = emptyMap(),
                    mediaUploadProgress = null,
                    mediaUploadType = null
                )
                sent()
                loadChats(true)
            } catch (error: Throwable) {
                state = state.copy(
                    sending = false,
                    replyTo = state.replyTo ?: replyTo,
                    mediaUploadProgress = null,
                    mediaUploadType = null,
                    error = if (error is CancellationException) null else errorText(error)
                )
            } finally {
                stopUploadActivity(chat.id)
                if (mediaUploadJob === coroutineContext[Job]) mediaUploadJob = null
            }
        }
    }

    fun sendFile(uri: Uri) {
        val name = mediaDisplayName(appContext, uri)
        val mime = appContext.contentResolver.getType(uri).orEmpty().ifBlank { mimeTypeFromFileName(name).orEmpty() }
        sendSingleAttachment(uri, if (isAudioMedia(mime, name)) "audio" else "file", 0L)
    }

    fun sendVoice(uri: Uri, duration: Long, finished: () -> Unit) {
        sendSingleAttachment(uri, "audio", duration.coerceAtLeast(0L), finished)
    }

    private fun sendSingleAttachment(
        uri: Uri,
        selectedKind: String,
        selectedDuration: Long,
        finished: () -> Unit = {}
    ) {
        val chat = state.currentChat ?: run {
            finished()
            return
        }
        if (state.sending || mediaUploadJob?.isActive == true) {
            finished()
            return
        }
        val replyTo = state.replyTo
        stopTyping()
        mediaUploadJob = scope.launch {
            try {
                val currentUploadType = if (selectedKind == "audio") UploadContentType.Audio else UploadContentType.File
                state = state.copy(
                    sending = true,
                    replyTo = null,
                    mediaUploadProgress = 0f,
                    mediaUploadType = currentUploadType,
                    error = null
                )
                startUploadActivity(chat.id, currentUploadType)
                val duration = if (selectedKind == "audio" && selectedDuration < 1L) {
                    withContext(Dispatchers.IO) { resolveMediaDuration(appContext, uri) }
                } else {
                    selectedDuration
                }
                var lastReportedProgress = -1f
                runCatching {
                    val response = api.postMedia(
                        "/chats/${chat.id}/media",
                        appContext,
                        uri,
                        selectedKind,
                        0,
                        0,
                        duration,
                        replyTo?.id ?: 0L,
                        "",
                        state.token
                    ) { progress ->
                        if ((progress >= 1f && lastReportedProgress < 1f) || progress - lastReportedProgress >= 0.05f) {
                            lastReportedProgress = progress
                            scope.launch {
                                if (state.mediaUploadProgress != null) state = state.copy(mediaUploadProgress = progress)
                            }
                        }
                    }
                    val message = response.getJSONObject("message")
                    withContext(Dispatchers.IO) {
                        runCatching {
                            storeLocalMedia(
                                appContext,
                                uri,
                                message.optString("media_url"),
                                selectedKind,
                                message.optString("media_name")
                            )
                        }
                    }
                    response
                }.onSuccess { response ->
                    appendMessage(parseMessage(response.getJSONObject("message")))
                    state = state.copy(sending = false, mediaUploadProgress = null, mediaUploadType = null)
                    loadChats(true)
                }.onFailure { error ->
                    state = state.copy(
                        sending = false,
                        replyTo = state.replyTo ?: replyTo,
                        mediaUploadProgress = null,
                        mediaUploadType = null,
                        error = if (error is CancellationException) null else errorText(error)
                    )
                }
            } finally {
                stopUploadActivity(chat.id)
                finished()
                if (mediaUploadJob === coroutineContext[Job]) mediaUploadJob = null
            }
        }
    }

    fun cancelMediaUpload() {
        val chatId = state.currentChat?.id
        mediaUploadJob?.cancel()
        api.cancelMediaUpload()
        state = state.copy(
            sending = false,
            mediaUploadProgress = null,
            mediaUploadType = null,
            selectedLocalMedia = emptyList(),
            pendingMediaGroupIds = emptyMap(),
            reopenMediaSheet = false
        )
        if (chatId != null) stopUploadActivity(chatId)
    }

    fun openLocalMediaPreview(media: DeviceMedia, items: List<DeviceMedia>) {
        state = state.copy(
            screen = Screen.Media,
            localMediaPreview = media,
            localMediaItems = items.ifEmpty { listOf(media) },
            reopenMediaSheet = false,
            remoteMediaPreview = null,
            error = null
        )
    }

    fun openRemoteMediaPreview(message: MessageItem) {
        mediaReturnScreen = if (state.screen == Screen.Profile) Screen.Profile else Screen.Chat
        state = state.copy(
            screen = Screen.Media,
            localMediaPreview = null,
            localMediaItems = emptyList(),
            selectedLocalMedia = emptyList(),
            pendingMediaGroupIds = emptyMap(),
            reopenMediaSheet = false,
            remoteMediaPreview = message,
            error = null
        )
    }

    fun openAvatarPreview(name: String, avatar: String, userId: Long) {
        val returnScreen = state.screen.takeIf {
            it == Screen.Settings || it == Screen.Group || it == Screen.GroupSettings || it == Screen.Profile || it == Screen.Chat
        } ?: return
        state = state.copy(
            screen = Screen.Avatar,
            avatarPreview = AvatarPreviewItem(
                name = name.ifBlank { "Пользователь" },
                avatar = avatar,
                userId = userId,
                returnScreen = returnScreen
            ),
            error = null
        )
    }

    fun openAvatarSelection(uri: Uri) {
        val returnScreen = state.screen.takeIf {
            it == Screen.Settings || it == Screen.Group || it == Screen.GroupSettings
        } ?: return
        state = state.copy(
            screen = Screen.AvatarSelection,
            avatarSelectionUri = uri,
            avatarSelectionReturnScreen = returnScreen,
            error = null
        )
    }

    fun cancelAvatarSelection() {
        val returnScreen = state.avatarSelectionReturnScreen
        state = state.copy(screen = returnScreen)
        scope.launch {
            delay(340)
            if (state.screen == returnScreen) state = state.copy(avatarSelectionUri = null)
        }
    }

    fun confirmAvatarSelection() {
        val uri = state.avatarSelectionUri ?: return
        val returnScreen = state.avatarSelectionReturnScreen
        state = state.copy(screen = returnScreen, avatarSelectionUri = null)
        if (returnScreen == Screen.Group || returnScreen == Screen.GroupSettings) saveGroupAvatar(uri) else saveAvatar(uri)
    }

    fun closeAvatarPreview() {
        val returnScreen = state.avatarPreview?.returnScreen ?: Screen.Chats
        state = state.copy(screen = returnScreen)
        scope.launch {
            delay(340)
            if (state.screen == returnScreen) state = state.copy(avatarPreview = null)
        }
    }

    fun closeMediaPreview() {
        closeMediaPreview(state.localMediaPreview != null)
    }

    fun closeMediaPreviewAfterSend() {
        closeMediaPreview(false)
    }

    fun dismissMediaSheet() {
        state = state.copy(reopenMediaSheet = false, selectedLocalMedia = emptyList(), pendingMediaGroupIds = emptyMap())
    }

    fun clearLocalMediaSelection() {
        if (state.mediaUploadProgress != null) return
        state = state.copy(selectedLocalMedia = emptyList(), pendingMediaGroupIds = emptyMap())
    }

    fun toggleLocalMediaSelection(media: DeviceMedia) {
        if (state.mediaUploadProgress != null) return
        val selected = state.selectedLocalMedia
        val index = selected.indexOfFirst { it.id == media.id && it.kind == media.kind }
        if (index < 0 && selected.size >= 20) {
            state = state.copy(error = "Можно выбрать не более 20 файлов")
            return
        }
        state = state.copy(
            selectedLocalMedia = if (index >= 0) selected.filterIndexed { itemIndex, _ -> itemIndex != index } else selected + media,
            pendingMediaGroupIds = emptyMap()
        )
    }

    private fun closeMediaPreview(reopenMediaSheet: Boolean) {
        val returnScreen = if (state.localMediaPreview != null) Screen.Chat else mediaReturnScreen
        state = state.copy(screen = returnScreen, reopenMediaSheet = false)
        scope.launch {
            delay(340)
            if (state.screen == returnScreen) {
                state = state.copy(
                    localMediaPreview = null,
                    localMediaItems = emptyList(),
                    remoteMediaPreview = null,
                    selectedLocalMedia = if (reopenMediaSheet) state.selectedLocalMedia else emptyList(),
                    pendingMediaGroupIds = if (reopenMediaSheet) state.pendingMediaGroupIds else emptyMap(),
                    reopenMediaSheet = reopenMediaSheet
                )
            }
        }
    }

    fun back() {
        when (state.screen) {
            Screen.Code -> state = state.copy(screen = Screen.Email)
            Screen.Name -> logout()
            Screen.Archive -> state = state.copy(screen = Screen.Chats)
            Screen.Settings -> state = state.copy(screen = Screen.Chats)
            Screen.Profile -> state = state.copy(screen = Screen.Chat)
            Screen.Group -> {
                state = state.copy(screen = Screen.Chats, groupEditor = null)
            }
            Screen.GroupSettings -> {
                state = state.copy(screen = groupReturnScreen, groupEditor = null)
            }
            Screen.Chat -> {
                stopTyping()
                remoteTypingJob?.cancel()
                val returnScreen = chatReturnScreen
                state = state.copy(
                    screen = returnScreen,
                    replyTo = null,
                    editingMessage = null,
                    editingText = "",
                    selectedLocalMedia = emptyList(),
                    pendingMediaGroupIds = emptyMap(),
                    typingChatId = null,
                    typingUserId = null,
                    typingVoiceRecording = false,
                    typingActivity = ""
                )
                loadChats(true)
                scope.launch {
                    delay(340)
                    if (state.screen == returnScreen) state = state.copy(currentChat = null, messages = emptyList())
                }
            }
            Screen.Media -> if (state.mediaUploadProgress == null) closeMediaPreview()
            Screen.AvatarSelection -> cancelAvatarSelection()
            Screen.Avatar -> closeAvatarPreview()
            Screen.Call -> endCall()
            else -> Unit
        }
    }

    fun openChatProfile() {
        state.currentChat ?: return
        state = state.copy(screen = Screen.Profile, error = null)
    }

    fun clearError() {
        state = state.copy(error = null)
    }

    fun updateInternetAvailability(available: Boolean) {
        if (state.internetAvailable == available) return
        if (!available) {
            socketGeneration++
            val activeSocket = socket
            socket = null
            state = state.copy(
                internetAvailable = false,
                online = false,
                listsUpdating = false,
                typingChatId = null,
                typingUserId = null,
                typingVoiceRecording = false,
                typingActivity = ""
            )
            activeSocket?.cancel()
            return
        }

        state = state.copy(
            internetAvailable = true,
            online = false,
            listsUpdating = listUpdatesInFlight > 0,
            error = null
        )
        if (appForeground) {
            updateCheckJob?.cancel()
            updateCheckJob = null
            startAppUpdateChecks()
        }
        socketReconnectAttempt = 0
        if (!appForeground || state.token.isBlank() || state.userName.isBlank()) return
        registerStoredPushToken()
        connectSocket()
    }

    fun onForeground() {
        appForeground = true
        socketReconnectAttempt = 0
        maybeInstallPendingUpdate()
        startAppUpdateChecks()
        if (state.token.isBlank() || state.userName.isBlank()) return
        if (!state.internetAvailable) return
        registerStoredPushToken()
        connectSocket()
    }

    fun onBackground() {
        appForeground = false
        updateCheckJob?.cancel()
        updateCheckJob = null
        stopVoiceTyping()
        if (state.call != null) {
            stopTyping()
            remoteTypingJob?.cancel()
            state = state.copy(typingChatId = null, typingUserId = null, typingVoiceRecording = false, typingActivity = "")
            return
        }
        socketEnabled = false
        stopTyping()
        remoteTypingJob?.cancel()
        socketGeneration++
        val activeSocket = socket
        socket = null
        state = state.copy(online = false, typingChatId = null, typingUserId = null, typingVoiceRecording = false, typingActivity = "")
        activeSocket?.cancel()
    }

    fun close() {
        state.call?.let { sendCallSignal("call_end", it, reason = "closed") }
        runCatching { callEngine?.close() }
        callEngine = null
        stopIncomingAlert()
        appContext.stopService(Intent(appContext, CallForegroundService::class.java))
        onBackground()
        api.cancelAll()
        scope.cancel()
    }

    private fun startAppUpdateChecks() {
        if (updateCheckJob?.isActive == true) return
        updateCheckJob = scope.launch {
            while (appForeground) {
                if (state.internetAvailable) {
                    runCatching {
                        withContext(Dispatchers.IO) { fetchLatestAppUpdate() }
                    }.onSuccess { update ->
                        state = state.copy(availableUpdate = update)
                    }
                }
                delay(AppUpdateCheckIntervalMillis)
            }
        }
    }

    fun downloadAndInstallUpdate() {
        pendingUpdateFile?.takeIf(File::isFile)?.let {
            launchUpdateInstaller(it)
            return
        }
        val update = state.availableUpdate ?: return
        if (updateDownloadJob?.isActive == true) return
        updateDownloadJob = scope.launch {
            state = state.copy(updateDownloading = true, error = null)
            runCatching {
                withContext(Dispatchers.IO) { downloadAppUpdate(appContext, update) }
            }.onSuccess { file ->
                state = state.copy(updateDownloading = false)
                launchUpdateInstaller(file)
            }.onFailureActive { error ->
                state = state.copy(updateDownloading = false, error = errorText(error))
            }
        }
    }

    private fun maybeInstallPendingUpdate() {
        val file = pendingUpdateFile?.takeIf(File::isFile) ?: return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || appContext.packageManager.canRequestPackageInstalls()) {
            launchUpdateInstaller(file)
        }
    }

    private fun launchUpdateInstaller(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !appContext.packageManager.canRequestPackageInstalls()) {
            pendingUpdateFile = file
            runCatching {
                appContext.startActivity(
                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${appContext.packageName}"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }.onFailure {
                pendingUpdateFile = null
                state = state.copy(error = "Разрешите установку обновлений для Ise Messenger")
            }
            return
        }
        pendingUpdateFile = null
        runCatching {
            val uri = FileProvider.getUriForFile(appContext, "${appContext.packageName}.files", file)
            appContext.startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri, AppUpdateMimeType)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            )
        }.onFailure {
            state = state.copy(error = "Не удалось открыть установщик обновления")
        }
    }

    private fun restoreSession() {
        scope.launch {
            runCatching { api.get("/me", state.token) }
                .onSuccess { response ->
                    val userId = response.getLong("id")
                    val name = response.optString("name")
                    val avatar = response.optString("avatar")
                    val email = response.getString("email")
                    val needsName = response.getBoolean("needs_name")
                    preferences.edit {
                        putLong("user_id", userId)
                        putString("name", name)
                        putString("avatar", avatar)
                        putString("email", email)
                    }
                    state = state.copy(
                        screen = when {
                            state.call != null -> Screen.Call
                            needsName -> Screen.Name
                            state.screen in listOf(Screen.Chat, Screen.Profile, Screen.Media, Screen.Avatar) -> state.screen
                            else -> Screen.Chats
                        },
                        userId = userId,
                        userName = name,
                        userAvatar = avatar,
                        email = email
                    )
                    if (!needsName) {
                        registerStoredPushToken()
                        connectSocket()
                        loadChats()
                    }
                }
                .onFailureActive { error ->
                    if (error is ApiException && error.status == 401) logout()
                    else {
                        val restoredScreen = if (state.call != null) Screen.Call else if (state.screen == Screen.Chat) Screen.Chat else Screen.Chats
                        state = state.copy(
                            screen = restoredScreen,
                            error = if (state.internetAvailable) errorText(error) else null
                        )
                        connectSocket()
                    }
                }
        }
    }

    private fun readCachedChats(userId: Long): List<ChatItem> {
        if (userId < 1L) return emptyList()
        return runCatching {
            val value = cachePreferences.getString("chats_$userId", null) ?: return@runCatching emptyList()
            parseChats(JSONArray(value))
        }.getOrDefault(emptyList())
    }

    private fun readCachedMessages(userId: Long, chatId: Long): List<MessageItem> {
        if (userId < 1L || chatId < 1L) return emptyList()
        return runCatching {
            val value = cachePreferences.getString("messages_${userId}_$chatId", null)
                ?: return@runCatching emptyList()
            parseMessages(JSONArray(value))
        }.getOrDefault(emptyList())
    }

    private fun cacheChats(userId: Long, chats: List<ChatItem>) {
        if (userId < 1L) return
        cachePreferences.edit { putString("chats_$userId", chatsToJson(chats).toString()) }
    }

    private fun cacheMessages(userId: Long, chatId: Long, messages: List<MessageItem>) {
        if (userId < 1L || chatId < 1L) return
        val recentMessages = messages.takeLast(500)
        cachePreferences.edit {
            putString("messages_${userId}_$chatId", messagesToJson(recentMessages).toString())
        }
    }

    private fun loadMessages(chatId: Long, catchUp: Boolean = false, showUpdating: Boolean = false) {
        val requestId = ++messagesRequestId
        if (showUpdating) beginListUpdate()
        scope.launch {
            try {
                if (!catchUp) state = state.copy(loading = true)
                runCatching {
                    val after = if (catchUp) state.messages.maxOfOrNull { it.id } ?: 0L else 0L
                    val path = if (after > 0L) "/chats/$chatId/messages?after=$after" else "/chats/$chatId/messages"
                    val messages = api.get(path, state.token).getJSONArray("messages")
                    withContext(Dispatchers.Default) { parseMessages(messages) }
                }
                    .onSuccess { messages ->
                        if (requestId == messagesRequestId && state.currentChat?.id == chatId) {
                            val merged = if (catchUp) {
                                (state.messages + messages).distinctBy { it.id }.sortedBy { it.id }
                            } else messages
                            cacheMessages(state.userId, chatId, merged)
                            state = state.copy(
                                messages = merged,
                                messagesHasMore = if (catchUp) state.messagesHasMore else messages.size >= 100,
                                loading = if (catchUp) state.loading else false
                            )
                            if (!showUpdating) loadChats(true)
                        }
                    }
                    .onFailureActive {
                        if (requestId == messagesRequestId && state.currentChat?.id == chatId) {
                            state = state.copy(
                                loading = if (catchUp) state.loading else false,
                                error = if (state.internetAvailable) errorText(it) else null
                            )
                        }
                    }
            } finally {
                if (showUpdating) finishListUpdate()
            }
        }
    }

    fun loadOlderMessages() {
        val chatId = state.currentChat?.id ?: return
        val before = state.messages.minOfOrNull { it.id } ?: return
        if (olderMessagesLoading || !state.messagesHasMore) return
        olderMessagesLoading = true
        state = state.copy(loadingOlderMessages = true)
        scope.launch {
            runCatching {
                val array = api.get("/chats/$chatId/messages?before=$before", state.token).getJSONArray("messages")
                withContext(Dispatchers.Default) { parseMessages(array) }
            }.onSuccess { older ->
                if (state.currentChat?.id == chatId) {
                    val merged = (older + state.messages).distinctBy { it.id }.sortedBy { it.id }
                    cacheMessages(state.userId, chatId, merged)
                    state = state.copy(
                        messages = merged,
                        messagesHasMore = older.size >= 100,
                        loadingOlderMessages = false
                    )
                }
            }.onFailureActive { error ->
                if (state.currentChat?.id == chatId) {
                    state = state.copy(loadingOlderMessages = false, error = errorText(error))
                }
            }
            olderMessagesLoading = false
        }
    }

    private fun beginListUpdate() {
        listUpdatesInFlight++
        if (!state.listsUpdating) state = state.copy(listsUpdating = true)
    }

    private fun finishListUpdate() {
        listUpdatesInFlight = (listUpdatesInFlight - 1).coerceAtLeast(0)
        if (listUpdatesInFlight == 0 && state.listsUpdating) state = state.copy(listsUpdating = false)
    }

    private fun connectSocket() {
        if (!state.internetAvailable || (!appForeground && state.call == null) || state.token.isBlank() || socket != null) return
        socketEnabled = true
        val token = state.token
        val generation = ++socketGeneration
        socket = api.openSocket(
            token = token,
            onOpen = {
                scope.launch {
                    if (generation == socketGeneration && (appForeground || state.call != null)) {
                        socketReconnectAttempt = 0
                        state = state.copy(online = true, listsUpdating = listUpdatesInFlight > 0)
                        state.call?.takeIf { callSignalReady }?.let { activeCall ->
                            val unsent = pendingLocalCallIce.filterNot { sendCallIce(activeCall, it) }
                            pendingLocalCallIce.clear()
                            pendingLocalCallIce.addAll(unsent)
                        }
                        state.currentChat?.let { chat ->
                            loadMessages(chat.id, catchUp = true, showUpdating = true)
                            if (chat.group) loadGroupMembers(chat.id, showUpdating = true)
                        }
                        loadChats(silent = true, showUpdating = true)
                    }
                }
            },
            onMessage = socketMessage@{ payload ->
                val event = runCatching { JSONObject(payload) }.getOrNull() ?: return@socketMessage
                scope.launch {
                    if (generation != socketGeneration || (!appForeground && state.call == null)) return@launch
                    runCatching {
                        when (event.optString("type")) {
                            "message" -> {
                                val parsed = parseMessage(event.getJSONObject("message"))
                                val message = parsed.copy(mine = parsed.senderId == state.userId)
                                if (state.currentChat?.id == message.chatId) {
                                    applyTyping(message.chatId, message.senderId, false)
                                    appendMessage(message)
                                }
                                scheduleChatsRefresh()
                            }
                            "message_deleted" -> {
                                val chatId = event.getLong("chat_id")
                                val messageId = event.getLong("message_id")
                                recordDeletedNotification(appContext, chatId, messageId)
                                cancelChatNotification(appContext, chatId)
                                if (state.currentChat?.id == chatId) {
                                    state = state.copy(
                                        messages = state.messages.filterNot { it.id == messageId },
                                        replyTo = state.replyTo?.takeUnless { it.id == messageId },
                                        editingMessage = state.editingMessage?.takeUnless { it.id == messageId },
                                        editingText = if (state.editingMessage?.id == messageId) "" else state.editingText
                                    )
                                    cacheMessages(state.userId, chatId, state.messages)
                                }
                                scheduleChatsRefresh()
                            }
                            "message_edited" -> {
                                val parsed = parseMessage(event.getJSONObject("message"))
                                val message = parsed.copy(mine = parsed.senderId == state.userId)
                                if (state.currentChat?.id == message.chatId) {
                                    state = state.copy(
                                        messages = state.messages.map { if (it.id == message.id) message else it },
                                        editingMessage = state.editingMessage?.let { if (it.id == message.id) message else it }
                                    )
                                    cacheMessages(state.userId, message.chatId, state.messages)
                                }
                                scheduleChatsRefresh()
                            }
                            "chat_cleared" -> {
                                val chatId = event.getLong("chat_id")
                                recordNotificationRead(appContext, chatId, event.optLong("message_id"))
                                cancelChatNotification(appContext, chatId)
                                if (state.currentChat?.id == chatId) {
                                    state = state.copy(
                                        messages = emptyList(),
                                        replyTo = null,
                                        editingMessage = null,
                                        editingText = "",
                                        typingChatId = null,
                                        typingUserId = null,
                                        typingVoiceRecording = false,
                                        typingActivity = ""
                                    )
                                    cacheMessages(state.userId, chatId, emptyList())
                                }
                                scheduleChatsRefresh()
                            }
                            "chat" -> {
                                val chat = parseChat(event.getJSONObject("chat"))
                                state = state.copy(
                                    chats = listOf(chat) + state.chats.filterNot { it.id == chat.id }
                                )
                            }
                            "chat_changed" -> {
                                val chatId = event.getLong("chat_id")
                                scheduleChatsRefresh()
                                if (state.currentChat?.id == chatId && state.currentChat?.group == true) loadGroupMembers(chatId)
                                if (state.groupEditor?.chat?.id == chatId) loadGroupMembers(chatId, true)
                            }
                            "chat_archived" -> {
                                val chatId = event.getLong("chat_id")
                                val archived = event.optBoolean("archived")
                                state = state.copy(
                                    chats = state.chats.map { if (it.id == chatId) it.copy(archived = archived) else it }
                                )
                            }
                            "chat_deleted" -> {
                                val chatId = event.getLong("chat_id")
                                if (state.call?.chatId == chatId) finishCall()
                                val currentDeleted = state.currentChat?.id == chatId
                                if (currentDeleted) {
                                    remoteTypingJob?.cancel()
                                    typingStopJob?.cancel()
                                    typingSent = false
                                }
                                state = state.copy(
                                    screen = if (currentDeleted) chatReturnScreen else state.screen,
                                    chats = state.chats.filterNot { it.id == chatId },
                                    drafts = state.drafts - chatId,
                                    currentChat = if (currentDeleted) null else state.currentChat,
                                    messages = if (currentDeleted) emptyList() else state.messages,
                                    replyTo = if (currentDeleted) null else state.replyTo,
                                    editingMessage = if (currentDeleted) null else state.editingMessage,
                                    editingText = if (currentDeleted) "" else state.editingText,
                                    typingChatId = if (currentDeleted) null else state.typingChatId
                                )
                            }
                            "read" -> {
                                applyRead(
                                    chatId = event.getLong("chat_id"),
                                    readerId = event.getLong("reader_id"),
                                    lastReadMessageId = event.getLong("last_read_message_id")
                                )
                                scheduleChatsRefresh()
                            }
                            "presence" -> applyPresence(
                                userId = event.getLong("user_id"),
                                online = event.getBoolean("online"),
                                lastSeenAt = event.optString("last_seen_at")
                            )
                            "typing" -> applyTyping(
                                chatId = event.getLong("chat_id"),
                                userId = event.getLong("user_id"),
                                typing = event.getBoolean("typing"),
                                recordingVoice = event.optBoolean("recording_voice") ||
                                        event.optString("activity") == "voice_recording",
                                activity = event.optString("activity", "typing")
                            )
                            "profile" -> applyProfile(
                                userId = event.getLong("user_id"),
                                name = event.optString("name"),
                                avatar = event.optString("avatar")
                            )
                            "call_offer", "call_answer", "call_ice", "call_end", "call_decline", "call_busy", "call_unavailable", "call_video_state" -> handleCallSignal(event)
                            "account_deleted" -> {
                                applyAccountDeleted(event.getLong("user_id"))
                                scheduleChatsRefresh()
                            }
                        }
                    }
                }
            },
            onClosed = {
                scope.launch {
                    if (generation != socketGeneration) return@launch
                    socket = null
                    typingStopJob?.cancel()
                    remoteTypingJob?.cancel()
                    typingSent = false
                    state = state.copy(
                        online = false,
                        typingChatId = null,
                        typingUserId = null,
                        typingVoiceRecording = false,
                        typingActivity = ""
                    )
                    if (socketEnabled && (appForeground || state.call != null) && state.token == token) {
                        val reconnectDelay = (500L * (1L shl socketReconnectAttempt.coerceAtMost(4))).coerceAtMost(8_000L)
                        socketReconnectAttempt = (socketReconnectAttempt + 1).coerceAtMost(6)
                        delay(reconnectDelay)
                        if (generation == socketGeneration && (appForeground || state.call != null)) connectSocket()
                    }
                }
            }
        )
    }

    private fun appendMessage(message: MessageItem) {
        if (state.messages.none { it.id == message.id }) {
            val messages = state.messages + message.copy(mine = message.senderId == state.userId)
            state = state.copy(messages = messages)
            cacheMessages(state.userId, message.chatId, messages)
        }
    }

    private fun markRead(chatId: Long, messageId: Long) {
        pendingReadMessageIds[chatId] = maxOf(pendingReadMessageIds[chatId] ?: 0L, messageId)
        if (readJobs[chatId]?.isActive == true) return
        val token = state.token
        readJobs[chatId] = scope.launch {
            delay(75)
            var retryDelay = 500L
            try {
                while (true) {
                    val targetMessageId = pendingReadMessageIds.remove(chatId) ?: break
                    val success = runCatching {
                        api.post("/chats/$chatId/read", JSONObject().put("message_id", targetMessageId), token)
                    }.isSuccess
                    if (!success) {
                        pendingReadMessageIds[chatId] = maxOf(pendingReadMessageIds[chatId] ?: 0L, targetMessageId)
                        if (!appForeground || state.token != token) break
                        delay(retryDelay)
                        retryDelay = (retryDelay * 2).coerceAtMost(8_000L)
                        continue
                    }
                    retryDelay = 500L
                    recordNotificationRead(appContext, chatId, targetMessageId)
                    cancelChatNotification(appContext, chatId)
                    scheduleChatsRefresh()
                }
            } finally {
                readJobs.remove(chatId)
            }
        }
    }

    fun markMessageRead(chatId: Long, messageId: Long) {
        if (state.currentChat?.id == chatId && messageId > 0L) markRead(chatId, messageId)
    }

    private fun sendTyping(chatId: Long, typing: Boolean, activity: String = "typing") {
        val recordingVoice = activity == "voice_recording"
        socket?.send(
            JSONObject()
                .put("type", "typing")
                .put("chat_id", chatId)
                .put("typing", typing)
                .put("recording_voice", recordingVoice)
                .put("activity", activity)
                .toString()
        )
    }

    private fun startUploadActivity(chatId: Long, type: UploadContentType) {
        val activity = when (type) {
            UploadContentType.Photo -> "photo_upload"
            UploadContentType.Video -> "video_upload"
            UploadContentType.Media -> "media_upload"
            UploadContentType.Audio -> "audio_upload"
            UploadContentType.File -> "file_upload"
        }
        uploadActivityJob?.cancel()
        sendTyping(chatId, true, activity)
        uploadActivityJob = scope.launch {
            while (state.mediaUploadProgress != null && state.currentChat?.id == chatId) {
                delay(1_500L)
                if (state.mediaUploadProgress != null) sendTyping(chatId, true, activity)
            }
        }
    }

    private fun stopUploadActivity(chatId: Long) {
        uploadActivityJob?.cancel()
        uploadActivityJob = null
        sendTyping(chatId, false)
    }

    private fun stopTyping() {
        typingStopJob?.cancel()
        typingStopJob = null
        val chatId = state.currentChat?.id
        if (typingSent && chatId != null) sendTyping(chatId, false)
        typingSent = false
    }

    private fun stopVoiceTyping() {
        voiceTypingJob?.cancel()
        voiceTypingJob = null
        voiceTypingChatId?.let { sendTyping(it, false) }
        voiceTypingChatId = null
    }

    private fun applyTyping(
        chatId: Long,
        userId: Long,
        typing: Boolean,
        recordingVoice: Boolean = false,
        activity: String = "typing"
    ) {
        if (userId == state.userId || state.currentChat?.id != chatId) return
        remoteTypingJob?.cancel()
        if (!typing) {
            if (state.typingChatId == null && state.typingUserId == null && !state.typingVoiceRecording && state.typingActivity.isBlank()) return
            state = state.copy(typingChatId = null, typingUserId = null, typingVoiceRecording = false, typingActivity = "")
            return
        }
        val nextActivity = when {
            recordingVoice || activity == "voice_recording" -> "voice_recording"
            activity in setOf("photo_upload", "video_upload", "media_upload", "audio_upload", "file_upload") -> activity
            else -> "typing"
        }
        val nextVoiceRecording = nextActivity == "voice_recording"
        if (state.typingChatId == chatId && state.typingUserId == userId &&
            state.typingVoiceRecording == nextVoiceRecording && state.typingActivity == nextActivity
        ) {
            remoteTypingJob = scope.launch {
                delay(3500)
                if (state.typingChatId == chatId && state.typingUserId == userId) {
                    state = state.copy(typingChatId = null, typingUserId = null, typingVoiceRecording = false, typingActivity = "")
                }
            }
            return
        }
        state = state.copy(
            typingChatId = chatId,
            typingUserId = userId,
            typingVoiceRecording = nextVoiceRecording,
            typingActivity = nextActivity
        )
        remoteTypingJob = scope.launch {
            delay(3500)
            if (state.typingChatId == chatId && state.typingUserId == userId) {
                state = state.copy(typingChatId = null, typingUserId = null, typingVoiceRecording = false, typingActivity = "")
            }
        }
    }

    private fun applyRead(chatId: Long, readerId: Long, lastReadMessageId: Long) {
        if (readerId == state.userId) {
            recordNotificationRead(appContext, chatId, lastReadMessageId)
            cancelChatNotification(appContext, chatId)
            scheduleChatsRefresh()
            return
        }
        if (state.currentChat?.id != chatId) return
        if (state.messages.none { it.mine && !it.read && it.id <= lastReadMessageId }) return
        state = state.copy(
            messages = state.messages.map { message ->
                if (message.mine && message.id <= lastReadMessageId) message.copy(read = true) else message
            }
        )
        cacheMessages(state.userId, chatId, state.messages)
    }

    private fun applyPresence(userId: Long, online: Boolean, lastSeenAt: String) {
        val clearTyping = !online && state.currentChat?.userId == userId && state.typingUserId == userId
        val chatsAffected = state.chats.any {
            !it.saved && it.userId == userId && (it.online != online || it.lastSeenAt != lastSeenAt)
        }
        val currentAffected = state.currentChat?.let {
            !it.saved && it.userId == userId && (it.online != online || it.lastSeenAt != lastSeenAt)
        } == true
        val membersAffected = state.groupMembers.any {
            it.id == userId && (it.online != online || it.lastSeenAt != lastSeenAt)
        }
        val editorAffected = state.groupEditor?.members?.any {
            it.id == userId && (it.online != online || it.lastSeenAt != lastSeenAt)
        } == true
        if (!clearTyping && !chatsAffected && !currentAffected && !membersAffected && !editorAffected) return
        if (clearTyping) remoteTypingJob?.cancel()
        state = state.copy(
            chats = if (chatsAffected) state.chats.map { chat ->
                if (!chat.saved && chat.userId == userId) chat.copy(online = online, lastSeenAt = lastSeenAt) else chat
            } else state.chats,
            currentChat = if (currentAffected) state.currentChat?.let { chat ->
                if (!chat.saved && chat.userId == userId) chat.copy(online = online, lastSeenAt = lastSeenAt) else chat
            } else state.currentChat,
            groupMembers = if (membersAffected) state.groupMembers.map { member ->
                if (member.id == userId) member.copy(online = online, lastSeenAt = lastSeenAt) else member
            } else state.groupMembers,
            groupEditor = if (editorAffected) state.groupEditor?.let { editor ->
                editor.copy(members = editor.members.map { member ->
                    if (member.id == userId) member.copy(online = online, lastSeenAt = lastSeenAt) else member
                })
            } else state.groupEditor,
            typingChatId = if (clearTyping) null else state.typingChatId
        )
    }

    private fun applyProfile(userId: Long, name: String, avatar: String) {
        scope.launch(Dispatchers.IO) {
            val file = File(File(appContext.filesDir, "avatars"), "notification_$userId.img")
            synchronized(persistentMediaLock(file)) { if (file.exists()) file.delete() }
        }
        val chatsAffected = state.chats.any { !it.saved && it.userId == userId }
        val currentAffected = state.currentChat?.let { !it.saved && it.userId == userId } == true
        val membersAffected = state.groupMembers.any { it.id == userId }
        val editorAffected = state.groupEditor?.members?.any { it.id == userId } == true
        if (!chatsAffected && !currentAffected && !membersAffected && !editorAffected) return
        state = state.copy(
            chats = if (chatsAffected) state.chats.map { chat ->
                if (!chat.saved && chat.userId == userId) {
                    val defaultName = name.ifBlank { chat.defaultName }
                    chat.copy(
                        name = if (chat.customName.isBlank()) defaultName else chat.name,
                        defaultName = defaultName,
                        avatar = avatar
                    )
                } else chat
            } else state.chats,
            currentChat = if (currentAffected) state.currentChat?.let { chat ->
                if (!chat.saved && chat.userId == userId) {
                    val defaultName = name.ifBlank { chat.defaultName }
                    chat.copy(
                        name = if (chat.customName.isBlank()) defaultName else chat.name,
                        defaultName = defaultName,
                        avatar = avatar
                    )
                } else chat
            } else state.currentChat,
            groupMembers = if (membersAffected) state.groupMembers.map { member ->
                if (member.id == userId) member.copy(name = name.ifBlank { member.name }, avatar = avatar) else member
            } else state.groupMembers,
            groupEditor = if (editorAffected) state.groupEditor?.let { editor ->
                editor.copy(members = editor.members.map { member ->
                    if (member.id == userId) member.copy(name = name.ifBlank { member.name }, avatar = avatar) else member
                })
            } else state.groupEditor
        )
    }

    private fun applyAccountDeleted(userId: Long) {
        if (state.call?.peerId == userId) finishCall()
        val currentDeleted = state.currentChat?.userId == userId
        state = state.copy(
            screen = if (currentDeleted) Screen.Chats else state.screen,
            chats = state.chats.filterNot { it.userId == userId },
            groupMembers = state.groupMembers.filterNot { it.id == userId },
            groupEditor = state.groupEditor?.let { it.copy(members = it.members.filterNot { member -> member.id == userId }) },
            currentChat = if (currentDeleted) null else state.currentChat,
            messages = if (currentDeleted) emptyList() else state.messages,
            replyTo = if (currentDeleted) null else state.replyTo,
            typingChatId = if (currentDeleted) null else state.typingChatId
        )
    }

    private fun logout() {
        state.call?.let { sendCallSignal("call_end", it, reason = "logout") }
        stopVoiceTyping()
        scope.coroutineContext.cancelChildren()
        runCatching { callEngine?.close() }
        callEngine = null
        callTimeoutJob?.cancel()
        callTimeoutJob = null
        stopIncomingAlert()
        appContext.stopService(Intent(appContext, CallForegroundService::class.java))
        socketEnabled = false
        socketGeneration++
        stopTyping()
        remoteTypingJob?.cancel()
        readJobs.values.forEach { it.cancel() }
        readJobs.clear()
        pendingReadMessageIds.clear()
        socket?.close(1000, null)
        socket = null
        api.cancelAll()
        chatsLoading = false
        chatsRefreshPending = false
        chatsRefreshNeedsLoading = false
        mediaUploadJob = null
        pendingCallIce.clear()
        pendingLocalCallIce.clear()
        callSignalReady = false
        val fcmToken = preferences.getString("fcm_token", "").orEmpty()
        preferences.edit {
            clear()
            if (fcmToken.isNotBlank()) putString("fcm_token", fcmToken)
        }
        cachePreferences.edit { clear() }
        appContext.getSharedPreferences("ise_notifications", Context.MODE_PRIVATE).edit { clear() }
        NotificationManagerCompat.from(appContext).cancelAll()
        state = AppState(screen = Screen.Email)
    }

    private fun registerStoredPushToken() {
        preferences.getString("fcm_token", "")?.takeIf { it.isNotBlank() }?.let { PushTokenRegistrar.register(appContext, it) }
    }

    private fun showError(message: String) {
        state = state.copy(error = message)
    }

    private fun errorText(error: Throwable): String {
        return error.message?.takeIf { it.isNotBlank() } ?: "Что-то пошло не так"
    }
}

@Suppress("DEPRECATION")
internal class CallEngine(
    context: Context,
    private val video: Boolean,
    iceServers: List<IceServerConfig>,
    private val onIceCandidate: (IceCandidate) -> Unit,
    private val onConnected: () -> Unit,
    private val onRemoteVideo: () -> Unit,
    private val onCameraSwitched: (Boolean) -> Unit,
    private val onFailure: () -> Unit
) {
    companion object {
        private val initialized = AtomicBoolean(false)
    }

    private val appContext = context.applicationContext
    val eglContext: EglBase.Context
    private val eglBase: EglBase
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val previousAudioMode = audioManager.mode
    private val previousSpeaker = audioManager.isSpeakerphoneOn
    private val audioModule: JavaAudioDeviceModule
    private val factory: PeerConnectionFactory
    private val audioSource: org.webrtc.AudioSource
    private val audioTrack: AudioTrack
    private var videoSource: VideoSource? = null
    private var surfaceTextureHelper: SurfaceTextureHelper? = null
    private var cameraCapturer: CameraVideoCapturer? = null
    private var frontCameraName: String? = null
    private var backCameraName: String? = null
    private var activeCameraName: String? = null
    private var cameraSwitching = false
    var localVideoTrack: VideoTrack? = null
        private set
    var remoteVideoTrack: VideoTrack? = null
        private set
    private val queuedRemoteIce = ArrayList<IceCandidate>()
    private var remoteDescriptionReady = false
    private var closed = false
    private val peerConnection: PeerConnection

    init {
        if (!initialized.get()) {
            synchronized(initialized) {
                if (!initialized.get()) {
                    PeerConnectionFactory.initialize(
                        PeerConnectionFactory.InitializationOptions.builder(appContext).createInitializationOptions()
                    )
                    initialized.set(true)
                }
            }
        }
        eglBase = EglBase.create()
        eglContext = eglBase.eglBaseContext
        audioModule = JavaAudioDeviceModule.builder(appContext).createAudioDeviceModule()
        factory = PeerConnectionFactory.builder()
            .setAudioDeviceModule(audioModule)
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglContext))
            .createPeerConnectionFactory()
        audioSource = factory.createAudioSource(MediaConstraints())
        audioTrack = factory.createAudioTrack("ise_audio", audioSource)
        if (video && !createLocalVideo()) throw IllegalStateException("camera unavailable")
        val rtcServers = iceServers.flatMap { config ->
            if (config.urls.isEmpty()) emptyList() else listOf(
                PeerConnection.IceServer.builder(config.urls).apply {
                    if (config.username.isNotEmpty()) setUsername(config.username)
                    if (config.credential.isNotEmpty()) setPassword(config.credential)
                }.createIceServer()
            )
        }
        val rtcConfig = PeerConnection.RTCConfiguration(rtcServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
            rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
        }
        peerConnection = factory.createPeerConnection(rtcConfig, peerObserver())
            ?: throw IllegalStateException("peer connection unavailable")
        peerConnection.addTrack(audioTrack, listOf("ise_stream"))
        localVideoTrack?.let { peerConnection.addTrack(it, listOf("ise_stream")) }
        runCatching { audioManager.mode = AudioManager.MODE_IN_COMMUNICATION }
        setSpeaker(true)
    }

    private fun createLocalVideo(): Boolean {
        val enumerator = if (Camera2Enumerator.isSupported(appContext)) Camera2Enumerator(appContext) else Camera1Enumerator(true)
        val names = enumerator.deviceNames
        frontCameraName = names.firstOrNull(enumerator::isFrontFacing)
        backCameraName = names.firstOrNull(enumerator::isBackFacing)
        val selected = frontCameraName ?: backCameraName ?: names.firstOrNull() ?: return false
        val capturer = enumerator.createCapturer(selected, null) ?: return false
        val source = factory.createVideoSource(false)
        val helper = runCatching { SurfaceTextureHelper.create("IseCamera", eglContext) }.getOrNull()
        if (helper == null) {
            runCatching { capturer.dispose() }
            runCatching { source.dispose() }
            return false
        }
        if (runCatching {
                capturer.initialize(helper, appContext, source.capturerObserver)
                capturer.startCapture(720, 1280, 30)
            }.isFailure
        ) {
            runCatching { capturer.dispose() }
            runCatching { helper.dispose() }
            runCatching { source.dispose() }
            return false
        }
        cameraCapturer = capturer
        activeCameraName = selected
        videoSource = source
        surfaceTextureHelper = helper
        localVideoTrack = factory.createVideoTrack("ise_video", source)
        onCameraSwitched(enumerator.isFrontFacing(selected))
        return localVideoTrack != null
    }

    private fun peerObserver() = object : PeerConnection.Observer {
        override fun onSignalingChange(state: PeerConnection.SignalingState) = Unit
        override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
            if (state == PeerConnection.IceConnectionState.FAILED || state == PeerConnection.IceConnectionState.CLOSED) onFailure()
        }
        override fun onStandardizedIceConnectionChange(state: PeerConnection.IceConnectionState) = Unit
        override fun onConnectionChange(state: PeerConnection.PeerConnectionState) {
            when (state) {
                PeerConnection.PeerConnectionState.CONNECTED -> onConnected()
                PeerConnection.PeerConnectionState.FAILED, PeerConnection.PeerConnectionState.CLOSED -> onFailure()
                else -> Unit
            }
        }
        override fun onIceConnectionReceivingChange(receiving: Boolean) = Unit
        override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) = Unit
        override fun onIceCandidate(candidate: IceCandidate) = onIceCandidate.invoke(candidate)
        override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) = Unit
        override fun onAddStream(stream: MediaStream) {
            stream.videoTracks.firstOrNull()?.let(::setRemoteVideoTrack)
        }
        override fun onRemoveStream(stream: MediaStream) = Unit
        override fun onDataChannel(channel: DataChannel) = Unit
        override fun onRenegotiationNeeded() = Unit
        override fun onAddTrack(receiver: RtpReceiver, streams: Array<out MediaStream>) {
            (receiver.track() as? VideoTrack)?.let(::setRemoteVideoTrack)
        }
        override fun onTrack(transceiver: RtpTransceiver) {
            (transceiver.receiver.track() as? VideoTrack)?.let(::setRemoteVideoTrack)
        }
    }

    private fun setRemoteVideoTrack(track: VideoTrack) {
        if (remoteVideoTrack === track) return
        remoteVideoTrack = track
        track.setEnabled(true)
        onRemoteVideo()
    }

    fun createOffer(onDescription: (SessionDescription) -> Unit) {
        runCatching {
            peerConnection.createOffer(object : SimpleSdpObserver() {
                override fun onCreateSuccess(description: SessionDescription) {
                    setLocalDescription(description, onDescription)
                }
                override fun onCreateFailure(error: String) = onFailure()
            }, mediaConstraints())
        }.onFailure { onFailure() }
    }

    fun createAnswer(offer: String, onDescription: (SessionDescription) -> Unit) {
        if (offer.isBlank()) {
            onFailure()
            return
        }
        runCatching {
            peerConnection.setRemoteDescription(object : SimpleSdpObserver() {
                override fun onSetSuccess() {
                    runCatching {
                        markRemoteDescriptionReady()
                        peerConnection.createAnswer(object : SimpleSdpObserver() {
                            override fun onCreateSuccess(description: SessionDescription) {
                                setLocalDescription(description, onDescription)
                            }
                            override fun onCreateFailure(error: String) = onFailure()
                        }, mediaConstraints())
                    }.onFailure { onFailure() }
                }
                override fun onSetFailure(error: String) = onFailure()
            }, SessionDescription(SessionDescription.Type.OFFER, offer))
        }.onFailure { onFailure() }
    }

    fun setRemoteAnswer(answer: String) {
        if (answer.isBlank()) {
            onFailure()
            return
        }
        runCatching {
            peerConnection.setRemoteDescription(object : SimpleSdpObserver() {
                override fun onSetSuccess() {
                    runCatching { markRemoteDescriptionReady() }.onFailure { onFailure() }
                }
                override fun onSetFailure(error: String) = onFailure()
            }, SessionDescription(SessionDescription.Type.ANSWER, answer))
        }.onFailure { onFailure() }
    }

    private fun setLocalDescription(description: SessionDescription, completed: (SessionDescription) -> Unit) {
        runCatching {
            peerConnection.setLocalDescription(object : SimpleSdpObserver() {
                override fun onSetSuccess() = completed(description)
                override fun onSetFailure(error: String) = onFailure()
            }, description)
        }.onFailure { onFailure() }
    }

    private fun mediaConstraints() = MediaConstraints().apply {
        mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", video.toString()))
    }

    @Synchronized
    fun addRemoteIce(candidate: IceCandidate) {
        if (closed) return
        if (remoteDescriptionReady) {
            runCatching { peerConnection.addIceCandidate(candidate) }.onFailure { onFailure() }
        } else {
            queuedRemoteIce.add(candidate)
        }
    }

    @Synchronized
    private fun markRemoteDescriptionReady() {
        remoteDescriptionReady = true
        queuedRemoteIce.forEach { candidate ->
            runCatching { peerConnection.addIceCandidate(candidate) }.onFailure { onFailure() }
        }
        queuedRemoteIce.clear()
    }

    fun setMuted(muted: Boolean) {
        audioTrack.setEnabled(!muted)
    }

    fun setCameraEnabled(enabled: Boolean) {
        localVideoTrack?.setEnabled(enabled)
    }

    fun switchCamera() {
        val capturer = cameraCapturer ?: return
        val target = synchronized(this) {
            if (cameraSwitching) return
            val next = if (activeCameraName == frontCameraName) backCameraName else frontCameraName
            if (next.isNullOrBlank() || next == activeCameraName) return
            cameraSwitching = true
            next
        }
        runCatching {
            capturer.switchCamera(object : CameraVideoCapturer.CameraSwitchHandler {
                override fun onCameraSwitchDone(isFrontCamera: Boolean) {
                    synchronized(this@CallEngine) {
                        activeCameraName = target
                        cameraSwitching = false
                    }
                    onCameraSwitched(isFrontCamera)
                }

                override fun onCameraSwitchError(errorDescription: String) {
                    synchronized(this@CallEngine) { cameraSwitching = false }
                }
            }, target)
        }.onFailure {
            synchronized(this) { cameraSwitching = false }
        }
    }

    fun setSpeaker(enabled: Boolean) {
        runCatching { audioManager.isSpeakerphoneOn = enabled }
    }

    @Synchronized
    fun close() {
        if (closed) return
        closed = true
        runCatching { cameraCapturer?.stopCapture() }
        runCatching { cameraCapturer?.dispose() }
        runCatching { surfaceTextureHelper?.dispose() }
        runCatching { peerConnection.close() }
        runCatching { peerConnection.dispose() }
        runCatching { localVideoTrack?.dispose() }
        runCatching { videoSource?.dispose() }
        runCatching { audioTrack.dispose() }
        runCatching { audioSource.dispose() }
        runCatching { factory.dispose() }
        runCatching { audioModule.release() }
        runCatching { eglBase.release() }
        runCatching { audioManager.mode = previousAudioMode }
        runCatching { audioManager.isSpeakerphoneOn = previousSpeaker }
    }
}

internal open class SimpleSdpObserver : SdpObserver {
    override fun onCreateSuccess(description: SessionDescription) = Unit
    override fun onSetSuccess() = Unit
    override fun onCreateFailure(error: String) = Unit
    override fun onSetFailure(error: String) = Unit
}

internal class ApiException(val status: Int, message: String) : Exception(message)

internal inline fun <T> Result<T>.onFailureActive(action: (Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.takeUnless { it is CancellationException }?.let(action)
    return this
}

internal class MessengerApi(private val baseUrl: String, private val requestTimeoutSeconds: Long = 30L) {
    private val jsonType = "application/json; charset=utf-8".toMediaType()
    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(0, TimeUnit.MILLISECONDS)
        .pingInterval(25, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    private val activeMediaUploadCall = AtomicReference<Call?>(null)

    fun cancelMediaUpload() {
        activeMediaUploadCall.getAndSet(null)?.cancel()
    }

    suspend fun get(path: String, token: String = ""): JSONObject {
        return execute(path, "GET", null, token)
    }

    suspend fun post(path: String, body: JSONObject, token: String = ""): JSONObject {
        return execute(path, "POST", body, token)
    }

    suspend fun patch(path: String, body: JSONObject, token: String = ""): JSONObject {
        return execute(path, "PATCH", body, token)
    }

    suspend fun delete(path: String, token: String = ""): JSONObject {
        return execute(path, "DELETE", null, token)
    }

    suspend fun postMedia(
        path: String,
        context: Context,
        uri: Uri,
        kind: String,
        width: Int,
        height: Int,
        duration: Long,
        replyToId: Long,
        mediaGroupId: String,
        token: String,
        onProgress: (Float) -> Unit
    ): JSONObject {
        return withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val fileName = mediaDisplayName(context, uri).ifBlank {
                when (kind) {
                    "image" -> "photo.jpg"
                    "video" -> "video.mp4"
                    "audio" -> "audio.mp3"
                    else -> "file"
                }
            }
            val mimeType = resolver.getType(uri)?.substringBefore(';')?.trim()?.takeIf { it.isNotBlank() }
                ?: mimeTypeFromFileName(fileName)
                ?: when (kind) {
                    "image" -> "image/jpeg"
                    "video" -> "video/mp4"
                    "audio" -> "audio/mpeg"
                    else -> "application/octet-stream"
                }
            var preparedFile: File? = null
            var uploadCall: Call? = null
            try {
                val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaType()
                val uriBody = UriRequestBody(context, uri, mediaType, onProgress)
                val requestBody = if ((kind == "file" || kind == "audio") && uriBody.contentLength() < 1L) {
                    val staged = prepareFileUpload(context, uri)
                    preparedFile = staged
                    LocalFileRequestBody(staged, mediaType, onProgress)
                } else {
                    uriBody
                }
                val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("kind", kind)
                    .addFormDataPart("width", width.coerceAtLeast(0).toString())
                    .addFormDataPart("height", height.coerceAtLeast(0).toString())
                    .addFormDataPart("duration", duration.coerceAtLeast(0L).toString())
                    .addFormDataPart("reply_to_id", replyToId.coerceAtLeast(0L).toString())
                    .addFormDataPart("media_group_id", mediaGroupId)
                    .addFormDataPart("file", fileName, requestBody)
                    .build()
                val request = Request.Builder().url(baseUrl + path)
                    .header("Authorization", "Bearer $token")
                    .post(multipart)
                    .build()
                uploadCall = client.newCall(request)
                activeMediaUploadCall.set(uploadCall)
                uploadCall.execute().use { response ->
                    val text = response.body?.string().orEmpty()
                    val json = if (text.isBlank()) JSONObject() else JSONObject(text)
                    if (!response.isSuccessful) throw ApiException(response.code, json.optString("error", "Ошибка сервера"))
                    json
                }
            } catch (error: ApiException) {
                throw error
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                currentCoroutineContext().ensureActive()
                if (uploadCall?.isCanceled() == true) throw CancellationException("Загрузка отменена")
                throw Exception(if (kind == "file") "Не удалось загрузить файл" else "Не удалось загрузить медиа")
            } finally {
                uploadCall?.let { activeMediaUploadCall.compareAndSet(it, null) }
                preparedFile?.delete()
            }
        }
    }

    suspend fun postMediaBatch(
        path: String,
        context: Context,
        items: List<MediaBatchUpload>,
        replyToId: Long,
        token: String,
        onProgress: (Float) -> Unit
    ): JSONObject {
        return withContext(Dispatchers.IO) {
            require(items.isNotEmpty())
            val preparedFiles = mutableListOf<File>()
            var uploadCall: Call? = null
            try {
                val itemProgress = FloatArray(items.size)
                var lastReportedProgress = -1f
                val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("count", items.size.toString())
                    .addFormDataPart("reply_to_id", replyToId.coerceAtLeast(0L).toString())
                items.forEachIndexed { index, upload ->
                    val item = upload.media
                    val fileName = mediaDisplayName(context, item.uri).ifBlank {
                        when (item.kind) {
                            "image" -> "photo.jpg"
                            "video" -> "video.mp4"
                            "audio" -> "audio.mp3"
                            else -> "file"
                        }
                    }
                    val mimeType = context.contentResolver.getType(item.uri)?.substringBefore(';')?.trim()?.takeIf { it.isNotBlank() }
                        ?: mimeTypeFromFileName(fileName)
                        ?: when (item.kind) {
                            "image" -> "image/jpeg"
                            "video" -> "video/mp4"
                            "audio" -> "audio/mpeg"
                            else -> "application/octet-stream"
                        }
                    val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaType()
                    val reportProgress: (Float) -> Unit = { progress ->
                        itemProgress[index] = progress.coerceIn(0f, 1f)
                        val totalProgress = itemProgress.sum() / itemProgress.size.toFloat()
                        if ((totalProgress >= 1f && lastReportedProgress < 1f) || totalProgress - lastReportedProgress >= 0.01f) {
                            lastReportedProgress = totalProgress
                            onProgress(totalProgress)
                        }
                    }
                    val uriBody = UriRequestBody(context, item.uri, mediaType, reportProgress)
                    val requestBody = if (item.kind == "audio" && uriBody.contentLength() < 1L) {
                        prepareFileUpload(context, item.uri).also { preparedFiles += it }
                            .let { LocalFileRequestBody(it, mediaType, reportProgress) }
                    } else {
                        uriBody
                    }
                    multipart
                        .addFormDataPart("kind_$index", item.kind)
                        .addFormDataPart("width_$index", upload.width.coerceAtLeast(0).toString())
                        .addFormDataPart("height_$index", upload.height.coerceAtLeast(0).toString())
                        .addFormDataPart("duration_$index", item.duration.coerceAtLeast(0L).toString())
                        .addFormDataPart("media_group_id_$index", upload.groupId)
                        .addFormDataPart("file_$index", fileName, requestBody)
                }
                val request = Request.Builder().url(baseUrl + path)
                    .header("Authorization", "Bearer $token")
                    .post(multipart.build())
                    .build()
                uploadCall = client.newCall(request)
                activeMediaUploadCall.set(uploadCall)
                uploadCall.execute().use { response ->
                    val text = response.body?.string().orEmpty()
                    val json = if (text.isBlank()) JSONObject() else JSONObject(text)
                    if (!response.isSuccessful) throw ApiException(response.code, json.optString("error", "Ошибка сервера"))
                    json
                }
            } catch (error: ApiException) {
                throw error
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                currentCoroutineContext().ensureActive()
                if (uploadCall?.isCanceled() == true) throw CancellationException("Загрузка отменена")
                throw Exception("Не удалось загрузить медиа")
            } finally {
                uploadCall?.let { activeMediaUploadCall.compareAndSet(it, null) }
                preparedFiles.forEach { it.delete() }
            }
        }
    }

    fun openSocket(
        token: String,
        onOpen: () -> Unit,
        onMessage: (String) -> Unit,
        onClosed: () -> Unit
    ): WebSocket {
        val socketUrl = baseUrl.replaceFirst("http://", "ws://").replaceFirst("https://", "wss://")
        val request = Request.Builder()
            .url("$socketUrl/ws")
            .header("Authorization", "Bearer $token")
            .build()
        return client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                onOpen()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                onMessage(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                onClosed()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onClosed()
            }
        })
    }

    fun cancelAll() {
        client.dispatcher.cancelAll()
        client.connectionPool.evictAll()
    }

    private suspend fun execute(path: String, method: String, body: JSONObject?, token: String): JSONObject {
        return withContext(Dispatchers.IO) {
            val builder = Request.Builder().url(baseUrl + path)
            if (token.isNotBlank()) builder.header("Authorization", "Bearer $token")
            when (method) {
                "POST" -> builder.post((body ?: JSONObject()).toString().toRequestBody(jsonType))
                "PATCH" -> builder.patch((body ?: JSONObject()).toString().toRequestBody(jsonType))
                "DELETE" -> builder.delete()
                else -> builder.get()
            }
            try {
                val call = client.newCall(builder.build())
                call.timeout().timeout(requestTimeoutSeconds, TimeUnit.SECONDS)
                call.execute().use { response ->
                    val text = response.body?.string().orEmpty()
                    val json = if (text.isBlank()) JSONObject() else JSONObject(text)
                    if (!response.isSuccessful) {
                        throw ApiException(response.code, json.optString("error", "Ошибка сервера"))
                    }
                    json
                }
            } catch (error: ApiException) {
                throw error
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                currentCoroutineContext().ensureActive()
                throw Exception("Сервер недоступен. Проверьте подключение")
            }
        }
    }
}

internal class UriRequestBody(
    private val context: Context,
    private val uri: Uri,
    private val type: okhttp3.MediaType,
    private val onProgress: (Float) -> Unit
) : RequestBody() {
    private val resolvedLength by lazy {
        runCatching {
            val descriptorLength = context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { it.length } ?: -1L
            if (descriptorLength > 0) descriptorLength
            else context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst() && !cursor.isNull(0)) cursor.getLong(0) else -1L
            } ?: -1L
        }.getOrDefault(-1L)
    }

    override fun contentType() = type

    override fun contentLength(): Long = resolvedLength

    override fun writeTo(sink: BufferedSink) {
        val stream = context.contentResolver.openInputStream(uri) ?: throw IOException("media unavailable")
        val total = resolvedLength
        var uploaded = 0L
        var lastPercent = -2
        stream.use {
            val buffer = ByteArray(32 * 1024)
            while (true) {
                val count = it.read(buffer)
                if (count < 0) break
                sink.write(buffer, 0, count)
                uploaded += count
                if (total > 0) {
                    val percent = ((uploaded * 100L) / total).toInt().coerceIn(0, 100)
                    if (percent == 100 || percent - lastPercent >= 5) {
                        lastPercent = percent
                        onProgress(percent / 100f)
                    }
                }
            }
        }
        onProgress(1f)
    }
}

internal fun prepareFileUpload(context: Context, uri: Uri): File {
    val directory = File(context.cacheDir, "uploads").apply { mkdirs() }
    val target = File.createTempFile("upload_", ".tmp", directory)
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: throw ApiException(400, "Не удалось открыть файл")
        input.use { source ->
            target.outputStream().use { output ->
                source.copyTo(output, 64 * 1024)
            }
        }
        target
    } catch (error: Exception) {
        target.delete()
        throw error
    }
}

internal fun cleanupStaleTemporaryFiles(context: Context) {
    val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
    val directories = listOf(
        File(context.cacheDir, "uploads"),
        File(context.cacheDir, "voice_messages"),
        File(context.filesDir, "media"),
        File(context.filesDir, "avatars")
    )
    directories.forEach { directory ->
        directory.listFiles()?.forEach { file ->
            val temporary = directory.name == "uploads" || directory.name == "voice_messages" ||
                    file.name.endsWith(".part")
            if (temporary && file.isFile && file.lastModified() in 1 until cutoff) file.delete()
        }
    }
    val persistentDirectories = listOf(File(context.filesDir, "media"), File(context.filesDir, "avatars"))
    val persistentFiles = persistentDirectories.flatMap { it.listFiles()?.filter(File::isFile).orEmpty() }
        .filterNot { it.name.endsWith(".part") }
        .sortedByDescending(File::lastModified)
    val maximumCacheBytes = 256L shl 20
    val maximumAge = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
    var retainedBytes = 0L
    persistentFiles.forEach { file ->
        retainedBytes += file.length()
        if (file.lastModified() < maximumAge || retainedBytes > maximumCacheBytes) file.delete()
    }
}

internal class LocalFileRequestBody(
    private val file: File,
    private val type: okhttp3.MediaType,
    private val onProgress: (Float) -> Unit
) : RequestBody() {
    override fun contentType() = type

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val total = file.length().coerceAtLeast(1L)
        var uploaded = 0L
        var lastPercent = -2
        file.inputStream().use { input ->
            val buffer = ByteArray(64 * 1024)
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                sink.write(buffer, 0, count)
                uploaded += count
                val percent = ((uploaded * 100L) / total).toInt().coerceIn(0, 100)
                if (percent == 100 || percent - lastPercent >= 5) {
                    lastPercent = percent
                    onProgress(percent / 100f)
                }
            }
        }
        onProgress(1f)
    }
}

internal fun parseChats(array: JSONArray): List<ChatItem> {
    return buildList {
        for (index in 0 until array.length()) add(parseChat(array.getJSONObject(index)))
    }
}

internal fun parseChat(json: JSONObject): ChatItem {
    val lastMessage = json.optString("last_message").let {
        if (it.startsWith("Файл:", ignoreCase = true)) "Файл" else it
    }
    return ChatItem(
        id = json.getLong("id"),
        userId = json.getLong("user_id"),
        name = json.optString("name").ifBlank { json.getString("email").substringBefore('@') },
        defaultName = json.optString("default_name").ifBlank {
            json.optString("name").ifBlank { json.optString("email").substringBefore('@') }
        },
        customName = json.optString("custom_name"),
        email = json.getString("email"),
        lastMessage = lastMessage,
        updatedAt = json.optString("updated_at"),
        unread = json.optInt("unread"),
        lastMine = json.optBoolean("last_mine"),
        lastRead = json.optBoolean("last_read"),
        online = json.optBoolean("online"),
        lastSeenAt = json.optString("last_seen_at"),
        avatar = json.optString("avatar"),
        saved = json.optBoolean("saved"),
        archived = json.optBoolean("archived"),
        group = json.optBoolean("group"),
        owner = json.optBoolean("owner"),
        memberCount = json.optInt("member_count"),
        lastKind = json.optString("last_kind"),
        lastMessageId = json.optLong("last_message_id"),
        avatarGradientSeed = json.optLong("avatar_gradient_seed")
    )
}

internal fun chatsToJson(chats: List<ChatItem>): JSONArray = JSONArray().apply {
    chats.forEach { chat ->
        put(
            JSONObject()
                .put("id", chat.id)
                .put("user_id", chat.userId)
                .put("name", chat.name)
                .put("default_name", chat.defaultName)
                .put("custom_name", chat.customName)
                .put("email", chat.email)
                .put("last_message", chat.lastMessage)
                .put("updated_at", chat.updatedAt)
                .put("unread", chat.unread)
                .put("last_mine", chat.lastMine)
                .put("last_read", chat.lastRead)
                .put("online", chat.online)
                .put("last_seen_at", chat.lastSeenAt)
                .put("avatar", chat.avatar)
                .put("saved", chat.saved)
                .put("archived", chat.archived)
                .put("group", chat.group)
                .put("owner", chat.owner)
                .put("member_count", chat.memberCount)
                .put("last_kind", chat.lastKind)
                .put("last_message_id", chat.lastMessageId)
                .put("avatar_gradient_seed", chat.avatarGradientSeed)
        )
    }
}

internal fun parseGroupMembers(array: JSONArray): List<GroupMember> {
    return buildList {
        for (index in 0 until array.length()) {
            val json = array.getJSONObject(index)
            add(
                GroupMember(
                    id = json.getLong("id"),
                    name = json.optString("name").ifBlank { json.optString("email").substringBefore('@') },
                    email = json.optString("email"),
                    avatar = json.optString("avatar"),
                    owner = json.optBoolean("owner"),
                    online = json.optBoolean("online"),
                    lastSeenAt = json.optString("last_seen_at")
                )
            )
        }
    }
}

internal fun parseMessages(array: JSONArray): List<MessageItem> {
    return buildList {
        for (index in 0 until array.length()) add(parseMessage(array.getJSONObject(index)))
    }
}

internal fun parseMessage(json: JSONObject): MessageItem {
    val rawKind = json.optString("kind", "text").ifBlank { "text" }
    val rawReplyKind = json.optString("reply_kind")
    return MessageItem(
        id = json.getLong("id"),
        chatId = json.getLong("chat_id"),
        senderId = json.getLong("sender_id"),
        senderName = json.optString("sender_name"),
        text = json.getString("text"),
        createdAt = json.getString("created_at"),
        mine = json.optBoolean("mine"),
        read = json.optBoolean("read"),
        kind = if (rawKind == "video_note") "video" else rawKind,
        mediaUrl = json.optString("media_url"),
        mediaMime = json.optString("media_mime"),
        mediaName = json.optString("media_name"),
        mediaSize = json.optLong("media_size"),
        mediaWidth = json.optInt("media_width"),
        mediaHeight = json.optInt("media_height"),
        mediaDuration = json.optLong("media_duration"),
        mediaGroupId = json.optString("media_group_id"),
        replyToId = json.optLong("reply_to_id"),
        replySenderName = json.optString("reply_sender_name"),
        replyText = json.optString("reply_text"),
        replyKind = if (rawReplyKind == "video_note") "video" else rawReplyKind,
        forwardedFromName = json.optString("forwarded_from_name"),
        forwardedFromId = json.optLong("forwarded_from_id"),
        edited = json.optBoolean("edited")
    )
}

internal fun messagesToJson(messages: List<MessageItem>): JSONArray = JSONArray().apply {
    messages.forEach { message ->
        put(
            JSONObject()
                .put("id", message.id)
                .put("chat_id", message.chatId)
                .put("sender_id", message.senderId)
                .put("sender_name", message.senderName)
                .put("text", message.text)
                .put("created_at", message.createdAt)
                .put("mine", message.mine)
                .put("read", message.read)
                .put("kind", message.kind)
                .put("media_url", message.mediaUrl)
                .put("media_mime", message.mediaMime)
                .put("media_name", message.mediaName)
                .put("media_size", message.mediaSize)
                .put("media_width", message.mediaWidth)
                .put("media_height", message.mediaHeight)
                .put("media_duration", message.mediaDuration)
                .put("media_group_id", message.mediaGroupId)
                .put("reply_to_id", message.replyToId)
                .put("reply_sender_name", message.replySenderName)
                .put("reply_text", message.replyText)
                .put("reply_kind", message.replyKind)
                .put("forwarded_from_name", message.forwardedFromName)
                .put("forwarded_from_id", message.forwardedFromId)
                .put("edited", message.edited)
        )
    }
}

internal data class MessageLink(val value: String, val start: Int, val end: Int)
internal val MessageLinkCache = LruCache<String, List<MessageLink>>(512)

internal fun messageLinks(text: String): List<MessageLink> {
    MessageLinkCache.get(text)?.let { return it }
    return MessageLinkPattern.findAll(text).mapNotNull { match ->
        val value = match.value.trimEnd('.', ',', '!', '?', ':', ';', ')', ']', '}', '»')
        value.takeIf { it.isNotBlank() }?.let { MessageLink(it, match.range.first, match.range.first + it.length) }
    }.toList().also { MessageLinkCache.put(text, it) }
}

internal fun firstMessageLink(text: String): String = messageLinks(text).firstOrNull()?.value.orEmpty()

internal fun openExternalLink(context: Context, url: String) {
    if (url.isBlank()) return
    val normalizedUrl = when {
        url.startsWith("mailto:", true) -> url
        url.contains('@') && !url.contains("://") -> "mailto:$url"
        Uri.parse(url).scheme?.isNotBlank() == true -> url
        else -> "https://$url"
    }
    runCatching {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(normalizedUrl)).apply {
            if (context !is android.app.Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}

@Composable
internal fun IseTheme(content: @Composable () -> Unit) {
    val palette = LightPalette
    val systemDensity = LocalDensity.current
    val stableDensity = remember(systemDensity.density) {
        Density(systemDensity.density, fontScale = 1f)
    }
    val colorScheme = lightColorScheme(
        primary = palette.accent,
        onPrimary = Color.White,
        primaryContainer = palette.accentSoft,
        onPrimaryContainer = palette.accentDark,
        background = palette.canvas,
        onBackground = palette.ink,
        surface = palette.paper,
        onSurface = palette.ink,
        surfaceVariant = palette.softSurface,
        onSurfaceVariant = palette.muted,
        outline = palette.line,
        error = AppDangerColor,
        onError = Color.White,
        errorContainer = AppDangerSoftColor,
        onErrorContainer = AppInkColor
    )
    CompositionLocalProvider(
        LocalIsePalette provides palette,
        LocalDensity provides stableDensity,
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(
                headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold, color = palette.ink),
                headlineSmall = TextStyle(fontSize = 23.sp, lineHeight = 29.sp, fontWeight = FontWeight.Bold, color = palette.ink),
                titleLarge = TextStyle(fontSize = 18.sp, lineHeight = 23.sp, fontWeight = FontWeight.SemiBold, color = palette.ink),
                bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 23.sp, color = palette.ink),
                bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, color = palette.ink),
                labelLarge = TextStyle(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Bold)
            ),
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessengerApp(controller: MessengerController, activity: MainActivity) {
    val state = controller.state
    val snackbar = remember { SnackbarHostState() }
    val previewVisible = state.screen in listOf(Screen.Media, Screen.AvatarSelection, Screen.Avatar, Screen.Call)
    val connectionStatusText = when {
        state.token.isBlank() || state.userName.isBlank() -> null
        !state.internetAvailable -> "Ожидание сети"
        !state.online -> "Соединение"
        state.listsUpdating -> "Обновление"
        else -> null
    }
    PreviewSystemBars(previewVisible)
    IncomingCallWindowBehavior(state.screen == Screen.Call && state.call?.incoming == true)
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbar.showSnackbar(it)
            controller.clearError()
        }
    }
    BackHandler(enabled = state.screen in listOf(Screen.Code, Screen.Name, Screen.Archive, Screen.Settings, Screen.Group, Screen.GroupSettings, Screen.Chat, Screen.Profile, Screen.AvatarSelection, Screen.Avatar, Screen.Call)) {
        controller.back()
    }
    CompositionLocalProvider(LocalConnectionStatusText provides connectionStatusText) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Canvas,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AnimatedContent(
                targetState = state.screen,
                transitionSpec = {
                    val movement = tween<IntOffset>(280, easing = FastOutSlowInEasing)
                    if (initialState == Screen.Splash && targetState == Screen.Chats) {
                        EnterTransition.None togetherWith ExitTransition.None
                    } else if (isOpeningNewScreen(initialState, targetState)) {
                        slideInHorizontally(movement) { fullWidth -> fullWidth } togetherWith
                                slideOutHorizontally(movement) { fullWidth -> -fullWidth }
                    } else {
                        slideInHorizontally(movement) { fullWidth -> -fullWidth } togetherWith
                                slideOutHorizontally(movement) { fullWidth -> fullWidth }
                    }
                },
                label = "screen"
            ) { screen ->
                Surface(
                    Modifier.fillMaxSize(),
                    color = if (screen in listOf(Screen.Media, Screen.AvatarSelection, Screen.Avatar, Screen.Call)) Color.Black else Canvas,
                    shadowElevation = 0.dp
                ) {
                    when (screen) {
                        Screen.Splash -> SplashScreen()
                        Screen.Email -> EmailScreen(state.email, state.loading, controller::requestCode)
                        Screen.Code -> CodeScreen(state.loading, controller::verifyCode, { controller.requestCode(state.email) }, controller::back)
                        Screen.Name -> NameScreen(state.loading, controller::saveName)
                        Screen.Chats -> ChatsScreen(controller, archived = false)
                        Screen.Archive -> ChatsScreen(controller, archived = true)
                        Screen.Settings -> SettingsScreenRoute(
                            state = state,
                            errorState = snackbar,
                            saveName = controller::updateName,
                            requestEmailChange = controller::requestEmailChange,
                            confirmEmailChange = controller::confirmEmailChange,
                            previewAvatar = controller::openAvatarSelection,
                            removeAvatar = controller::removeAvatar,
                            openAvatar = controller::openAvatarPreview,
                            logout = controller::logoutUser,
                            deleteAccount = controller::deleteAccount,
                            permissionError = controller::showPermissionError,
                            back = controller::back
                        )
                        Screen.Group -> state.groupEditor?.let { editor ->
                            GroupScreenRoute(
                                state = state,
                                editor = editor,
                                updateName = controller::updateGroupDraftName,
                                previewAvatar = controller::openAvatarSelection,
                                removeAvatar = controller::removeGroupAvatar,
                                openAvatar = controller::openAvatarPreview,
                                updateSelection = controller::updateGroupSelection,
                                createGroup = controller::createGroup,
                                permissionError = controller::showPermissionError,
                                errorState = snackbar,
                                back = controller::back
                            )
                        }
                        Screen.GroupSettings -> state.groupEditor?.let { editor ->
                            GroupSettingsScreenRoute(
                                state = state,
                                editor = editor,
                                updateName = controller::updateGroupDraftName,
                                saveName = controller::applyGroupName,
                                previewAvatar = controller::openAvatarSelection,
                                removeAvatar = controller::removeGroupAvatar,
                                openAvatar = controller::openAvatarPreview,
                                addMembers = controller::addGroupMembers,
                                removeMember = controller::removeGroupMember,
                                permissionError = controller::showPermissionError,
                                errorState = snackbar,
                                back = controller::back
                            )
                        }
                        Screen.Chat -> ChatScreenRoute(controller, snackbar)
                        Screen.Profile -> state.currentChat?.let { chat ->
                            ProfileScreenRoute(
                                chat = chat,
                                messages = state.messages,
                                groupMembers = state.groupMembers,
                                token = state.token,
                                openMedia = controller::openRemoteMediaPreview,
                                openAvatar = controller::openAvatarPreview,
                                editGroup = controller::openGroupSettings,
                                messagesHasMore = state.messagesHasMore,
                                loadingOlderMessages = state.loadingOlderMessages,
                                loadOlderMessages = controller::loadOlderMessages,
                                startCall = controller::startCall,
                                callPermissionError = controller::showCallPermissionError,
                                renameChat = { name -> controller.setPersonalChatName(chat, name) },
                                archiveChat = { archived -> controller.setChatArchived(chat, archived) },
                                clearChat = { controller.clearChat(chat) },
                                deleteChat = {
                                    controller.deleteChat(chat)
                                    controller.back()
                                },
                                errorState = snackbar,
                                back = controller::back
                            )
                        }
                        Screen.Media -> {
                            MediaScreenRoute(controller)
                        }
                        Screen.AvatarSelection -> AvatarSelectionScreenRoute(controller)
                        Screen.Avatar -> AvatarScreenRoute(controller)
                        Screen.Call -> state.call?.let { call ->
                            CallScreenRoute(
                                call = call,
                                controller = controller,
                                accept = controller::acceptCall,
                                decline = controller::declineCall,
                                end = controller::endCall,
                                toggleMute = controller::toggleCallMute,
                                toggleSpeaker = controller::toggleCallSpeaker,
                                toggleCamera = controller::toggleCallCamera,
                                switchCamera = controller::switchCallCamera,
                                permissionError = controller::showCallPermissionError
                            )
                        }
                    }
                }
            }
            val overlayBlurAlpha by animateFloatAsState(
                targetValue = if (activity.overlayBlurRequests > 0 && activity.overlayBlurBitmap != null) 1f else 0f,
                animationSpec = tween(
                    durationMillis = if (activity.overlayBlurRequests > 0) 220 else 300,
                    easing = FastOutSlowInEasing
                ),
                label = "overlayBlur",
                finishedListener = { alpha ->
                    if (alpha == 0f) activity.clearOverlayBlurIfHidden()
                }
            )
            activity.overlayBlurBitmap?.let { bitmap ->
                if (overlayBlurAlpha > 0f) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = overlayBlurAlpha }
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = OverlayDimAlpha))
                        )
                    }
                }
            }
            if (!state.addSheet) {
                AppErrorPopup(snackbar)
            }
        }
    }
    }
    if (state.addSheet) {
        ModalBottomSheet(
            onDismissRequest = controller::hideAddSheet,
            sheetGesturesEnabled = false,
            containerColor = Paper,
            scrimColor = OverlayScrimColor,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            BottomSheetHandle()
            Box(Modifier.fillMaxWidth().bottomSheetPop()) {
                AddChatSheet(state.loading, controller::createChat)
                AppErrorPopup(snackbar)
            }
        }
    }
}

internal fun isOpeningNewScreen(from: Screen, to: Screen): Boolean = when {
    from == to -> false
    from == Screen.Splash -> true
    from == Screen.Email -> to in listOf(Screen.Code, Screen.Name, Screen.Chats)
    from == Screen.Code -> to in listOf(Screen.Name, Screen.Chats)
    from == Screen.Name -> to == Screen.Chats
    from == Screen.Chats -> to in listOf(Screen.Archive, Screen.Settings, Screen.Group, Screen.Chat, Screen.Call)
    from == Screen.Archive -> to in listOf(Screen.Chat, Screen.Call)
    from == Screen.Chat -> to in listOf(Screen.Profile, Screen.Media, Screen.Group, Screen.Avatar, Screen.Call)
    from == Screen.Profile -> to in listOf(Screen.GroupSettings, Screen.Media, Screen.Avatar, Screen.Call)
    from == Screen.Settings || from == Screen.Group || from == Screen.GroupSettings -> to in listOf(Screen.AvatarSelection, Screen.Avatar, Screen.Call)
    from == Screen.AvatarSelection -> to == Screen.Avatar
    else -> false
}

@Composable
internal fun AppErrorPopup(state: SnackbarHostState) {
    val data = state.currentSnackbarData ?: return
    val accessibilityManager = LocalAccessibilityManager.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var dismissing by remember(data) { mutableStateOf(false) }
    var dragOffset by remember(data) { mutableFloatStateOf(0f) }
    var toastHeight by remember(data) { mutableIntStateOf(0) }
    var dragResetJob by remember(data) { mutableStateOf<Job?>(null) }
    val hide: () -> Unit = {
        if (!dismissing) {
            dismissing = true
            dragResetJob?.cancel()
            data.dismiss()
        }
    }
    val dragState = rememberDraggableState { delta ->
        dragResetJob?.cancel()
        dragOffset = (dragOffset + delta).coerceAtMost(0f)
    }
    LaunchedEffect(data) {
        dragOffset = 0f
        val baseDuration = when (data.visuals.duration) {
            SnackbarDuration.Short -> 4_000L
            SnackbarDuration.Long -> 10_000L
            SnackbarDuration.Indefinite -> Long.MAX_VALUE
        }
        val duration = accessibilityManager?.calculateRecommendedTimeoutMillis(
            originalTimeoutMillis = baseDuration,
            containsIcons = true,
            containsText = true,
            containsControls = data.visuals.actionLabel != null
        ) ?: baseDuration
        delay(duration)
        hide()
    }
    Popup(
        popupPositionProvider = TopErrorPositionProvider,
        properties = PopupProperties(focusable = false, clippingEnabled = false)
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            AppErrorHost(
                data,
                hide,
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                    .padding(top = 8.dp)
                    .onSizeChanged { toastHeight = it.height }
                    .graphicsLayer { translationY = dragOffset }
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Vertical,
                        enabled = !dismissing,
                        onDragStopped = { velocity ->
                            val distanceThreshold = maxOf(
                                toastHeight * 0.3f,
                                with(density) { 32.dp.toPx() }
                            )
                            if (dragOffset <= -distanceThreshold || velocity < -1_000f) {
                                hide()
                            } else {
                                dragResetJob = scope.launch {
                                    animate(
                                        initialValue = dragOffset,
                                        targetValue = 0f,
                                        animationSpec = tween(180, easing = FastOutSlowInEasing)
                                    ) { value, _ -> dragOffset = value }
                                }
                            }
                        }
                    )
            )
        }
    }
}

internal object TopErrorPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(0, 0)
    }
}

@Composable
internal fun AppErrorHost(data: SnackbarData, dismissAnimated: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            dismiss {
                dismissAnimated()
                true
            }
            paneTitle = "Уведомление"
        }
    ) {
        Snackbar(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            containerColor = ForestDark,
            contentColor = Color.White
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Warning, contentDescription = null, tint = Mint, modifier = Modifier.size(21.dp))
                Spacer(Modifier.width(11.dp))
                Text(
                    data.visuals.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}




@Composable
internal fun GroupMemberRow(
    member: GroupMember,
    openAvatar: (() -> Unit)? = null,
    remove: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (openAvatar != null) {
            val avatarInteraction = remember(member.id) { MutableInteractionSource() }
            Box(
                Modifier.clip(CircleShape).clickable(
                    interactionSource = avatarInteraction,
                    indication = null,
                    enabled = enabled,
                    onClick = openAvatar
                )
            ) {
                Avatar(member.name, 48.dp, member.avatar, member.id)
            }
        } else {
            Avatar(member.name, 48.dp, member.avatar, member.id)
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    member.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (member.owner) {
                    Spacer(Modifier.width(7.dp))
                    Text(
                        "Создатель",
                        color = ForestDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Mint)
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
            Text(member.email, color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        remove?.let {
            IconButton(onClick = it, enabled = enabled) {
                Icon(Icons.Rounded.Delete, contentDescription = "Удалить участника", tint = Forest, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MembersSelectionSheet(
    chats: List<ChatItem>,
    initialSelection: Set<Long>,
    loading: Boolean,
    openAvatar: (ChatItem) -> Unit,
    dismiss: () -> Unit,
    confirm: (Set<Long>) -> Unit
) {
    var selected by remember(chats, initialSelection) { mutableStateOf(initialSelection.intersect(chats.mapTo(mutableSetOf()) { it.userId })) }
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        sheetGesturesEnabled = false,
        containerColor = Paper,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        BottomSheetWindowBehavior()
        BottomSheetHandle()
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.78f).bottomSheetPop()) {
            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (chats.isEmpty()) {
                    Text("Нет доступных чатов", color = Muted, modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 4.dp)) {
                        items(chats, key = { it.userId }, contentType = { "member_candidate" }) { chat ->
                            val checked = chat.userId in selected
                            val notificationsEnabled = chatNotificationsEnabledState(LocalContext.current, chat.id)
                            val interactionSource = remember(chat.userId) { MutableInteractionSource() }
                            Row(
                                Modifier.fillMaxWidth().clickable(interactionSource = interactionSource, indication = null) {
                                    selected = if (checked) selected - chat.userId else selected + chat.userId
                                }.padding(horizontal = 20.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val avatarInteraction = remember(chat.userId) { MutableInteractionSource() }
                                Box(
                                    Modifier.clip(CircleShape).clickable(
                                        interactionSource = avatarInteraction,
                                        indication = null,
                                        onClick = { openAvatar(chat) }
                                    )
                                ) {
                                    Avatar(chat.name, 48.dp, chat.avatar, chat.userId)
                                }
                                Spacer(Modifier.width(13.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            chat.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        if (!notificationsEnabled) {
                                            Spacer(Modifier.width(5.dp))
                                            Icon(
                                                Icons.Rounded.NotificationsOff,
                                                contentDescription = "Уведомления отключены",
                                                tint = Muted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(chat.email, color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Box(
                                    Modifier.size(24.dp).clip(CircleShape).background(if (checked) Forest else Line),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (checked) Icon(Icons.Rounded.Done, contentDescription = null, tint = Color.White, modifier = Modifier.size(17.dp))
                                }
                            }
                        }
                    }
                }
            }
            Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                PrimaryButton("Добавить", loading, onClick = { confirm(selected) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroupMembersSheet(members: List<GroupMember>, openAvatar: (GroupMember) -> Unit, dismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        sheetGesturesEnabled = false,
        containerColor = Paper,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        BottomSheetWindowBehavior()
        BottomSheetHandle()
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.72f).bottomSheetPop()) {
            if (members.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(28.dp), color = Forest, strokeWidth = 2.5.dp)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(members, key = { it.id }, contentType = { "group_member_sheet" }) { member ->
                        GroupMemberRow(member, openAvatar = { openAvatar(member) })
                    }
                }
            }
        }
    }
}

@Composable
internal fun GallerySheet(select: (Uri) -> Unit) {
    BottomSheetWindowBehavior()
    val context = LocalContext.current
    var photos by remember { mutableStateOf<List<DevicePhoto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        photos = loadDevicePhotos(context)
        loading = false
    }
    Column(Modifier.fillMaxWidth().fillMaxHeight(0.86f)) {
        Box(Modifier.fillMaxSize()) {
            if (photos.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    gridItems(photos, key = { it.id }, contentType = { "photo" }) { photo ->
                        DevicePhotoItem(photo) { select(photo.uri) }
                    }
                }
            } else if (!loading) {
                Text("Фотографии не найдены", color = Muted, modifier = Modifier.align(Alignment.Center))
            }
            if (loading) CircularProgressIndicator(Modifier.size(30.dp).align(Alignment.Center), color = Forest, strokeWidth = 2.5.dp)
        }
    }
}

@Composable
internal fun DevicePhotoItem(photo: DevicePhoto, select: () -> Unit) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val cacheKey = remember(photo.id) { "device_photo_${photo.id}" }
    var bitmap by remember(photo.id) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    LaunchedEffect(photo.id) {
        if (bitmap == null) bitmap = loadPhotoThumbnail(context, photo)?.asImageBitmap()
    }
    Box(
        Modifier.fillMaxWidth().aspectRatio(1f).background(Line)
            .clickable(interactionSource = interactionSource, indication = null, onClick = select),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(it, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
internal fun AttachmentSheet(
    storageAccessVersion: Int,
    storageAccessGranted: Boolean,
    selectedMedia: List<DeviceMedia>,
    initialSection: AttachmentSection,
    sectionChanged: (AttachmentSection) -> Unit,
    requestSectionChange: (AttachmentSection) -> Unit,
    selectMedia: (DeviceMedia, List<DeviceMedia>) -> Unit,
    toggleMedia: (DeviceMedia) -> Unit,
    sendSelected: () -> Unit,
    selectFile: (File) -> Unit,
    requestStorageAccess: () -> Unit
) {
    BottomSheetWindowBehavior()
    val sectionPagerState = rememberPagerState(initialPage = initialSection.ordinal) { AttachmentSection.entries.size }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var sectionButtonDragging by remember { mutableStateOf(false) }
    var draggedSectionPosition by remember { mutableStateOf<Float?>(null) }
    var draggedSectionTarget by remember { mutableIntStateOf(initialSection.ordinal) }
    val section = AttachmentSection.entries[sectionPagerState.currentPage]
    val selectedMediaCount = selectedMedia.count { it.kind == "image" || it.kind == "video" }
    val selectedAudioCount = selectedMedia.count { it.kind == "audio" }
    val sendButtonText = when {
        selectedAudioCount > 0 -> "Отправить $selectedAudioCount аудио"
        selectedMediaCount > 0 -> "Отправить $selectedMediaCount медиа"
        else -> ""
    }
    val openSection: (AttachmentSection) -> Unit = { target ->
        if (target != section) {
            if (selectedMedia.isEmpty()) {
                scope.launch { sectionPagerState.animateScrollToPage(target.ordinal) }
            } else {
                requestSectionChange(target)
            }
        }
    }
    LaunchedEffect(sectionPagerState) {
        snapshotFlow { sectionPagerState.settledPage }
            .distinctUntilChanged()
            .collect { sectionChanged(AttachmentSection.entries[it]) }
    }
    Column(Modifier.fillMaxWidth().fillMaxHeight(0.86f)) {
        BoxWithConstraints(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(15.dp)).background(SoftSurface).padding(4.dp)
        ) {
            val sectionSpacing = 4.dp
            val sectionCount = AttachmentSection.entries.size
            val sectionWidth = (maxWidth - sectionSpacing * (sectionCount - 1)) / sectionCount
            val sectionStepPx = with(density) { (sectionWidth + sectionSpacing).toPx() }.coerceAtLeast(1f)
            val maximumSectionPosition = (sectionCount - 1).toFloat()
            Box(
                Modifier.offset {
                    val position = draggedSectionPosition
                        ?: (sectionPagerState.currentPage + sectionPagerState.currentPageOffsetFraction)
                            .coerceIn(0f, maximumSectionPosition)
                    IntOffset(((sectionWidth + sectionSpacing).roundToPx() * position).toInt(), 0)
                }.width(sectionWidth).height(40.dp)
                    .clip(RoundedCornerShape(12.dp)).background(Forest)
            )
            Row(
                Modifier.fillMaxWidth().draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val nextPosition = ((draggedSectionPosition
                            ?: (sectionPagerState.currentPage + sectionPagerState.currentPageOffsetFraction)) +
                            delta / sectionStepPx).coerceIn(0f, maximumSectionPosition)
                        draggedSectionPosition = nextPosition
                        val target = nextPosition.roundToInt().coerceIn(AttachmentSection.entries.indices)
                        if (selectedMedia.isEmpty() && target != draggedSectionTarget) {
                            draggedSectionTarget = target
                            scope.launch { sectionPagerState.animateScrollToPage(target) }
                        }
                    },
                    onDragStarted = {
                        sectionButtonDragging = true
                        draggedSectionTarget = sectionPagerState.currentPage
                        draggedSectionPosition =
                            (sectionPagerState.currentPage + sectionPagerState.currentPageOffsetFraction)
                                .coerceIn(0f, maximumSectionPosition)
                    },
                    onDragStopped = {
                        val target = (draggedSectionPosition ?: sectionPagerState.currentPage.toFloat())
                            .roundToInt()
                            .coerceIn(AttachmentSection.entries.indices)
                        draggedSectionPosition = null
                        sectionButtonDragging = false
                        openSection(AttachmentSection.entries[target])
                    }
                ),
                horizontalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                AttachmentSectionButton(
                    label = "Медиа",
                    selected = section == AttachmentSection.Media,
                    onClick = { openSection(AttachmentSection.Media) },
                    modifier = Modifier.weight(1f)
                )
                AttachmentSectionButton(
                    label = "Аудио",
                    selected = section == AttachmentSection.Audio,
                    onClick = { openSection(AttachmentSection.Audio) },
                    modifier = Modifier.weight(1f)
                )
                AttachmentSectionButton(
                    label = "Файлы",
                    selected = section == AttachmentSection.Files,
                    onClick = { openSection(AttachmentSection.Files) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        HorizontalPager(
            state = sectionPagerState,
            key = { AttachmentSection.entries[it] },
            userScrollEnabled = selectedMedia.isEmpty() && !sectionButtonDragging,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            val activeSection = AttachmentSection.entries[page]
            when (activeSection) {
                AttachmentSection.Media -> DeviceMediaGrid(
                    mediaAccessVersion = storageAccessVersion,
                    mediaAccessGranted = storageAccessGranted,
                    selectedMedia = selectedMedia,
                    select = selectMedia,
                    toggle = toggleMedia,
                    requestAccess = requestStorageAccess,
                    modifier = Modifier.fillMaxSize()
                )
                AttachmentSection.Audio -> DeviceAudioList(
                    mediaAccessVersion = storageAccessVersion,
                    audioAccessGranted = storageAccessGranted,
                    selectedMedia = selectedMedia,
                    toggle = toggleMedia,
                    requestAccess = requestStorageAccess,
                    modifier = Modifier.fillMaxSize()
                )
                AttachmentSection.Files -> DeviceFileBrowser(
                    fileAccessVersion = storageAccessVersion,
                    fileAccessGranted = storageAccessGranted,
                    active = section == AttachmentSection.Files,
                    selectFile = selectFile,
                    requestAccess = requestStorageAccess,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        if (selectedMedia.isNotEmpty()) {
            Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                PrimaryButton(text = sendButtonText, loading = false, onClick = sendSelected)
            }
        }
    }
}

@Composable
internal fun AttachmentSectionButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val contentColor by animateColorAsState(
        targetValue = if (selected) Color.White else Ink,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "attachmentSectionContent"
    )
    Box(
        modifier.height(40.dp).clip(RoundedCornerShape(12.dp))
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun AttachmentAccessButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Разрешить доступ"
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Forest),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp)
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
internal fun DeviceFileBrowser(
    fileAccessVersion: Int,
    fileAccessGranted: Boolean,
    active: Boolean,
    selectFile: (File) -> Unit,
    requestAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val storageRoot = remember { primarySharedStorageRoot() }
    var currentFolderPath by remember(storageRoot.absolutePath) { mutableStateOf(storageRoot.absolutePath) }
    val currentFolder = remember(currentFolderPath) { File(currentFolderPath) }
    var entries by remember { mutableStateOf<List<DeviceStorageEntry>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var readable by remember { mutableStateOf(true) }
    var previewPath by remember { mutableStateOf<String?>(null) }
    var previewPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var previewPreparing by remember { mutableStateOf(false) }
    var previewPlaying by remember { mutableStateOf(false) }
    val atStorageRoot = sameStoragePath(currentFolder, storageRoot)

    fun releasePreview() {
        previewPlayer?.release()
        previewPlayer = null
        previewPreparing = false
        previewPlaying = false
        previewPath?.let { path ->
            if (ActiveAudioMessageId == filePreviewAudioId(path)) ActiveAudioMessageId = 0L
        }
    }

    fun togglePreview(file: File) {
        val activeId = filePreviewAudioId(file.absolutePath)
        val currentPlayer = previewPlayer
        if (previewPath == file.absolutePath && currentPlayer != null) {
            if (currentPlayer.isPlaying) {
                currentPlayer.pause()
                previewPlaying = false
                if (ActiveAudioMessageId == activeId) ActiveAudioMessageId = 0L
            } else {
                ActiveAudioMessageId = activeId
                currentPlayer.start()
                previewPlaying = true
            }
            return
        }
        releasePreview()
        previewPath = file.absolutePath
        previewPreparing = true
        runCatching {
            MediaPlayer().also { player ->
                previewPlayer = player
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                player.setDataSource(file.absolutePath)
                player.setOnPreparedListener {
                    previewPreparing = false
                    ActiveAudioMessageId = activeId
                    it.start()
                    previewPlaying = true
                }
                player.setOnCompletionListener {
                    it.seekTo(0)
                    previewPlaying = false
                    if (ActiveAudioMessageId == activeId) ActiveAudioMessageId = 0L
                }
                player.setOnErrorListener { _, _, _ ->
                    releasePreview()
                    true
                }
                player.prepareAsync()
            }
        }.onFailure { releasePreview() }
    }
    val goToParent: () -> Unit = {
        currentFolder.parentFile
            ?.takeIf { isInsideStorageRoot(storageRoot, it) }
            ?.let { currentFolderPath = it.absolutePath }
    }

    BackHandler(enabled = active && fileAccessGranted && !atStorageRoot, onBack = goToParent)

    LaunchedEffect(fileAccessGranted, fileAccessVersion, currentFolderPath) {
        releasePreview()
        if (!fileAccessGranted) {
            entries = emptyList()
            loading = false
            readable = true
            return@LaunchedEffect
        }
        loading = true
        val listing = loadDeviceStorageEntries(currentFolder)
        entries = listing.entries
        readable = listing.readable
        loading = false
    }

    LaunchedEffect(ActiveAudioMessageId) {
        val path = previewPath ?: return@LaunchedEffect
        if (ActiveAudioMessageId != filePreviewAudioId(path) && previewPlaying) {
            previewPlayer?.takeIf { it.isPlaying }?.pause()
            previewPlaying = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { releasePreview() }
    }

    Box(modifier) {
        if (!fileAccessGranted) {
            AttachmentAccessButton(onClick = requestAccess, modifier = Modifier.fillMaxSize())
            return@Box
        }

        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().height(52.dp)) {
                if (!atStorageRoot) {
                    IconButton(onClick = goToParent, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Назад", tint = Ink)
                    }
                }
                Text(
                    text = if (atStorageRoot) "Внутреннее хранилище" else currentFolder.name.ifBlank { "Внутреннее хранилище" },
                    color = Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 56.dp)
                )
            }
            HorizontalDivider(color = Line)
            when {
                loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(30.dp), color = Forest, strokeWidth = 2.5.dp)
                }
                !readable -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Не удалось открыть папку", color = Muted)
                        if (!atStorageRoot) {
                            TextButton(onClick = goToParent) {
                                Text("Вернуться назад", color = Forest)
                            }
                        }
                    }
                }
                entries.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("В папке нет файлов", color = Muted)
                }
                else -> LazyColumn(Modifier.fillMaxSize()) {
                    items(entries, key = { it.path }) { entry ->
                        val audio = !entry.isDirectory && isAudioMedia(mimeTypeFromFileName(entry.name).orEmpty(), entry.name)
                        DeviceStorageRow(
                            entry = entry,
                            audio = audio,
                            previewPreparing = audio && previewPath == entry.path && previewPreparing,
                            previewPlaying = audio && previewPath == entry.path && previewPlaying,
                            togglePreview = { togglePreview(File(entry.path)) },
                            open = {
                                if (entry.isDirectory) currentFolderPath = entry.path
                                else selectFile(File(entry.path))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun DeviceStorageRow(
    entry: DeviceStorageEntry,
    audio: Boolean,
    previewPreparing: Boolean,
    previewPlaying: Boolean,
    togglePreview: () -> Unit,
    open: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = open).padding(horizontal = 16.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when {
                entry.isDirectory -> Icons.Rounded.Folder
                audio -> Icons.Rounded.Audiotrack
                else -> Icons.AutoMirrored.Rounded.InsertDriveFile
            },
            contentDescription = null,
            tint = if (entry.isDirectory) Forest else Muted,
            modifier = Modifier.size(30.dp)
        )
        Column(Modifier.weight(1f).padding(start = 18.dp)) {
            Text(
                entry.name,
                color = Ink,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!entry.isDirectory) {
                Text(formatFileSize(entry.size), color = Muted, fontSize = 12.sp)
            }
        }
        if (audio) {
            Box(
                Modifier.size(42.dp).clip(CircleShape).background(Mint)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = togglePreview
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (previewPreparing) {
                    CircularProgressIndicator(Modifier.size(22.dp), color = Forest, strokeWidth = 2.5.dp)
                } else {
                    Icon(
                        if (previewPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (previewPlaying) "Пауза" else "Прослушать",
                        tint = Forest,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

internal fun filePreviewAudioId(path: String): Long {
    return Long.MIN_VALUE + (path.hashCode().toLong() and 0xffffffffL)
}

@Composable
internal fun DeviceAudioList(
    mediaAccessVersion: Int,
    audioAccessGranted: Boolean,
    selectedMedia: List<DeviceMedia>,
    toggle: (DeviceMedia) -> Unit,
    requestAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var audio by remember { mutableStateOf<List<DeviceMedia>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var previewAudio by remember { mutableStateOf<DeviceMedia?>(null) }
    var previewPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var previewPreparing by remember { mutableStateOf(false) }
    var previewPlaying by remember { mutableStateOf(false) }
    var previewPosition by remember { mutableLongStateOf(0L) }
    var previewDuration by remember { mutableLongStateOf(0L) }

    fun releasePreview() {
        previewPlayer?.release()
        previewPlayer = null
        previewPreparing = false
        previewPlaying = false
        previewPosition = 0L
        previewAudio?.let { item ->
            if (ActiveAudioMessageId == -item.id.coerceAtLeast(1L)) ActiveAudioMessageId = 0L
        }
    }

    fun togglePreview(item: DeviceMedia) {
        val activeId = -item.id.coerceAtLeast(1L)
        val currentPlayer = previewPlayer
        if (previewAudio?.selectionKey() == item.selectionKey() && currentPlayer != null) {
            if (currentPlayer.isPlaying) {
                currentPlayer.pause()
                previewPlaying = false
                if (ActiveAudioMessageId == activeId) ActiveAudioMessageId = 0L
            } else {
                ActiveAudioMessageId = activeId
                currentPlayer.start()
                previewPlaying = true
            }
            return
        }

        releasePreview()
        previewAudio = item
        previewDuration = item.duration.coerceAtLeast(0L)
        previewPreparing = true
        runCatching {
            MediaPlayer().also { player ->
                previewPlayer = player
                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                player.setDataSource(context, item.uri)
                player.setOnPreparedListener {
                    previewPreparing = false
                    previewDuration = it.duration.toLong().coerceAtLeast(item.duration)
                    ActiveAudioMessageId = activeId
                    it.start()
                    previewPlaying = true
                }
                player.setOnCompletionListener {
                    previewPosition = 0L
                    previewPlaying = false
                    it.seekTo(0)
                    if (ActiveAudioMessageId == activeId) ActiveAudioMessageId = 0L
                }
                player.setOnErrorListener { _, _, _ ->
                    releasePreview()
                    true
                }
                player.prepareAsync()
            }
        }.onFailure { releasePreview() }
    }

    LaunchedEffect(mediaAccessVersion, audioAccessGranted) {
        if (!audioAccessGranted) {
            audio = emptyList()
            loading = false
        } else {
            loading = true
            audio = loadDeviceAudio(context)
            loading = false
        }
    }
    LaunchedEffect(previewPlaying) {
        while (previewPlaying) {
            previewPosition = previewPlayer?.currentPosition?.toLong() ?: previewPosition
            delay(250)
        }
    }
    LaunchedEffect(ActiveAudioMessageId) {
        val item = previewAudio ?: return@LaunchedEffect
        if (ActiveAudioMessageId != -item.id.coerceAtLeast(1L) && previewPlaying) {
            previewPlayer?.takeIf { it.isPlaying }?.pause()
            previewPlaying = false
        }
    }
    DisposableEffect(Unit) {
        onDispose { releasePreview() }
    }
    Box(modifier.fillMaxWidth()) {
        when {
            !audioAccessGranted -> AttachmentAccessButton(onClick = requestAccess, modifier = Modifier.fillMaxSize())
            audio.isNotEmpty() -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(audio, key = { it.selectionKey() }, contentType = { "device_audio" }) { item ->
                    val selectionIndex = selectedMedia.indexOfFirst { it.selectionKey() == item.selectionKey() }
                        .takeIf { it >= 0 }?.plus(1)
                    val isPreview = previewAudio?.selectionKey() == item.selectionKey()
                    DeviceAudioItem(
                        audio = item,
                        selectionIndex = selectionIndex,
                        previewPreparing = isPreview && previewPreparing,
                        previewPlaying = isPreview && previewPlaying,
                        previewPosition = if (isPreview) previewPosition else 0L,
                        previewDuration = if (isPreview) previewDuration else item.duration,
                        togglePreview = { togglePreview(item) },
                        toggle = { toggle(item) }
                    )
                }
            }
            loading -> CircularProgressIndicator(
                Modifier.size(30.dp).align(Alignment.Center),
                color = Forest,
                strokeWidth = 2.5.dp
            )
            else -> Text("Аудио не найдено", color = Muted, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
internal fun DeviceAudioItem(
    audio: DeviceMedia,
    selectionIndex: Int?,
    previewPreparing: Boolean,
    previewPlaying: Boolean,
    previewPosition: Long,
    previewDuration: Long,
    togglePreview: () -> Unit,
    toggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth().heightIn(min = 66.dp)
            .clickable(onClick = toggle)
            .padding(start = 20.dp, top = 7.dp, bottom = 7.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(Mint)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = togglePreview
                ),
            contentAlignment = Alignment.Center
        ) {
            if (previewPreparing) {
                CircularProgressIndicator(Modifier.size(23.dp), color = Forest, strokeWidth = 2.5.dp)
            } else {
                Icon(
                    if (previewPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (previewPlaying) "Пауза" else "Прослушать",
                    tint = Forest,
                    modifier = Modifier.size(27.dp)
                )
            }
        }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(
                audio.name.ifBlank { "Аудио" },
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val details = buildList {
                if (audio.artist.isNotBlank()) add(audio.artist)
                if (previewPlaying || previewPosition > 0L) {
                    add("${formatDuration(previewPosition)} / ${formatDuration(previewDuration)}")
                } else {
                    add(formatDuration(previewDuration))
                }
                if (audio.size > 0L) add(formatFileSize(audio.size))
            }.joinToString(" · ")
            Text(details, color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        MediaSelectionButton(selectionIndex, toggle)
    }
}

@Composable
internal fun DeviceMediaGrid(
    mediaAccessVersion: Int,
    mediaAccessGranted: Boolean,
    selectedMedia: List<DeviceMedia>,
    select: (DeviceMedia, List<DeviceMedia>) -> Unit,
    toggle: (DeviceMedia) -> Unit,
    requestAccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var media by remember { mutableStateOf<List<DeviceMedia>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(mediaAccessVersion, mediaAccessGranted) {
        if (mediaAccessGranted) {
            loading = true
            media = loadDeviceMedia(context)
            loading = false
        } else {
            media = emptyList()
            loading = false
        }
    }
    Box(modifier.fillMaxWidth()) {
        if (!mediaAccessGranted) {
            AttachmentAccessButton(onClick = requestAccess, modifier = Modifier.fillMaxSize())
        } else if (media.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                gridItems(media, key = { "${it.kind}_${it.id}" }, contentType = { it.kind }) { item ->
                    val selectionIndex = selectedMedia.indexOfFirst { it.id == item.id && it.kind == item.kind }
                        .takeIf { it >= 0 }?.plus(1)
                    DeviceMediaItem(
                        media = item,
                        selectionIndex = selectionIndex,
                        select = { select(item, media) },
                        toggle = { toggle(item) }
                    )
                }
            }
        } else if (!loading) {
            Text("Фото и видео не найдены", color = Muted, modifier = Modifier.align(Alignment.Center))
        }
        if (loading) CircularProgressIndicator(Modifier.size(30.dp).align(Alignment.Center), color = Forest, strokeWidth = 2.5.dp)
    }
}

@Composable
internal fun DeviceMediaItem(
    media: DeviceMedia,
    selectionIndex: Int?,
    select: () -> Unit,
    toggle: () -> Unit
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val cacheKey = remember(media.id, media.kind) { "device_${media.kind}_${media.id}" }
    var bitmap by remember(media.id, media.kind) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    LaunchedEffect(media.id, media.kind) {
        if (bitmap == null) bitmap = loadDeviceMediaThumbnail(context, media)?.asImageBitmap()
    }
    Box(
        Modifier.fillMaxWidth().aspectRatio(1f).background(ForestDark)
            .clickable(interactionSource = interactionSource, indication = null, onClick = select),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let { Image(it, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()) }
        if (media.kind == "video") {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.16f)))
            Text(
                formatDuration(media.duration),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomStart).padding(7.dp).clip(RoundedCornerShape(7.dp))
                    .background(Color.Black.copy(alpha = 0.55f)).padding(horizontal = 5.dp, vertical = 2.dp)
            )
        }
        if (bitmap == null) MediaLoadingIndicator()
        MediaSelectionButton(
            selectedIndex = selectionIndex,
            onClick = toggle,
            modifier = Modifier.align(Alignment.TopEnd).padding(2.dp)
        )
    }
}

@Composable
internal fun MediaSelectionButton(
    selectedIndex: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier.size(40.dp).clip(CircleShape).clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier.size(28.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier.size(24.dp).clip(CircleShape)
                    .background(if (selectedIndex == null) Color.Black.copy(alpha = 0.42f) else Forest),
                contentAlignment = Alignment.Center
            ) {
                selectedIndex?.let {
                    Text(
                        it.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
internal fun ChatRow(
    chat: ChatItem,
    modifier: Modifier = Modifier,
    draft: String = "",
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onArchive: (() -> Unit)? = null,
    requestSwipe: () -> Boolean = { true },
    releaseSwipe: () -> Unit = {},
    interactive: Boolean = true,
    containerColor: Color = Color.Unspecified
) {
    val context = LocalContext.current
    val notificationsEnabled = chatNotificationsEnabledState(context, chat.id)
    val rowColor = if (containerColor == Color.Unspecified) Canvas else containerColor
    val updatedTime = remember(chat.updatedAt) { formatTime(chat.updatedAt) }
    var dragOffset by remember(chat.id, chat.archived) { mutableFloatStateOf(0f) }
    var rowWidth by remember(chat.id) { mutableIntStateOf(0) }
    var completingSwipe by remember(chat.id, chat.archived) { mutableStateOf(false) }
    var swipeAccepted by remember(chat.id, chat.archived) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    DisposableEffect(chat.id) {
        onDispose { if (swipeAccepted) releaseSwipe() }
    }
    val density = LocalDensity.current
    val swipeThreshold = with(density) { 72.dp.toPx() }
    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = tween(
            durationMillis = if (completingSwipe) 240 else if (dragOffset == 0f) 210 else 0,
            easing = FastOutSlowInEasing
        ),
        label = "chat_archive_swipe"
    )
    val action = if (interactive && !completingSwipe) {
        Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick)
    } else Modifier
    val swipe = if (interactive && onArchive != null) {
        Modifier.pointerInput(chat.id, chat.archived) {
            detectHorizontalDragGestures(
                onDragStart = { swipeAccepted = requestSwipe() },
                onDragEnd = {
                    if (!swipeAccepted) {
                        dragOffset = 0f
                        return@detectHorizontalDragGestures
                    }
                    val commit = dragOffset <= -swipeThreshold
                    if (commit && rowWidth > 0) {
                        completingSwipe = true
                        dragOffset = minOf(dragOffset, -rowWidth.toFloat())
                        scope.launch {
                            delay(250)
                            swipeAccepted = false
                            releaseSwipe()
                            onArchive()
                        }
                    } else {
                        dragOffset = 0f
                        swipeAccepted = false
                        releaseSwipe()
                    }
                },
                onDragCancel = {
                    dragOffset = 0f
                    if (swipeAccepted) releaseSwipe()
                    swipeAccepted = false
                }
            ) { change, amount ->
                if (!swipeAccepted) return@detectHorizontalDragGestures
                val next = (dragOffset + amount).coerceAtMost(0f)
                if (next != dragOffset) {
                    change.consume()
                    dragOffset = next
                }
            }
        }
    } else Modifier
    Box(modifier.fillMaxWidth().height(78.dp).onSizeChanged { rowWidth = it.width }.background(Forest)) {
        if (interactive && onArchive != null) {
            Icon(
                if (chat.archived) Icons.Rounded.Unarchive else Icons.Rounded.Archive,
                contentDescription = if (chat.archived) "Вернуть" else "В архив",
                tint = Color.White,
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 30.dp).size(25.dp)
            )
        }
        Row(
            Modifier.fillMaxSize().offset { IntOffset(animatedOffset.toInt(), 0) }.background(rowColor)
                .then(swipe).then(action).padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                ChatAvatar(chat, 54.dp)
                if (chat.online) {
                    OnlineIndicator(
                        avatarSize = 54.dp,
                        backgroundColor = rowColor,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f).fillMaxHeight()) {
                Row(Modifier.height(28.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            chat.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (!notificationsEnabled) {
                            Spacer(Modifier.width(5.dp))
                            Icon(
                                Icons.Rounded.NotificationsOff,
                                contentDescription = "Уведомления отключены",
                                tint = Muted,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    if (chat.lastMessage.isNotBlank()) {
                        Spacer(Modifier.width(8.dp))
                        if (chat.lastMine && !chat.saved) {
                            MessageReadStatus(chat.lastRead, if (chat.lastRead) Forest else Muted)
                            Spacer(Modifier.width(3.dp))
                        }
                        Text(updatedTime, fontSize = 12.sp, color = Muted)
                    }
                }
                Row(Modifier.height(30.dp), verticalAlignment = Alignment.Top) {
                    if (draft.isNotBlank()) {
                        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Text("Черновик:", style = MaterialTheme.typography.bodyMedium, color = AppDangerColor, maxLines = 1)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                draft,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Muted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Text(
                            chat.lastMessage.ifBlank { "Нет сообщений" },
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 15.sp),
                            color = Muted,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (chat.unread > 0) {
                        Spacer(Modifier.width(8.dp))
                        UnreadBadge(chat.unread, Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
        }
    }
}

@Composable
internal fun AddChatSheet(loading: Boolean, create: (String) -> Unit) {
    BottomSheetWindowBehavior()
    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Column(Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 22.dp)) {
        AppTextField(
            title = "Почта собеседника",
            value = email,
            onValueChange = { email = it },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            onDone = { focusManager.clearFocus(); create(email) }
        )
        Spacer(Modifier.height(14.dp))
        PrimaryButton("Открыть чат", loading) { focusManager.clearFocus(); create(email) }
    }
}

@Composable
internal fun Modifier.bottomSheetPop(): Modifier {
    return this
}

internal fun Context.findMainActivity(): MainActivity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is MainActivity) return current
        current = current.baseContext
    }
    return current as? MainActivity
}

private fun smoothBoxBlur(bitmap: Bitmap) {
    val width = bitmap.width
    val height = bitmap.height
    if (width < 2 || height < 2) return

    synchronized(BlurWorkBufferLock) {
        val radius = 8
        val passes = 3
        val pixelCount = width * height
        if (BlurPixelBuffer.size < pixelCount) BlurPixelBuffer = IntArray(pixelCount)
        if (BlurTemporaryBuffer.size < pixelCount) BlurTemporaryBuffer = IntArray(pixelCount)
        val pixels = BlurPixelBuffer
        val buffer = BlurTemporaryBuffer
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        repeat(passes) {
            for (y in 0 until height) {
                val row = y * width
                var red = 0
                var green = 0
                var blue = 0
                for (offset in -radius..radius) {
                    val color = pixels[row + offset.coerceIn(0, width - 1)]
                    red += color shr 16 and 0xFF
                    green += color shr 8 and 0xFF
                    blue += color and 0xFF
                }
                for (x in 0 until width) {
                    buffer[row + x] = 0xFF000000.toInt() or
                        (BlurDivisionLookup[red] shl 16) or
                        (BlurDivisionLookup[green] shl 8) or
                        BlurDivisionLookup[blue]
                    val removed = pixels[row + (x - radius).coerceIn(0, width - 1)]
                    val added = pixels[row + (x + radius + 1).coerceIn(0, width - 1)]
                    red += (added shr 16 and 0xFF) - (removed shr 16 and 0xFF)
                    green += (added shr 8 and 0xFF) - (removed shr 8 and 0xFF)
                    blue += (added and 0xFF) - (removed and 0xFF)
                }
            }

            for (x in 0 until width) {
                var red = 0
                var green = 0
                var blue = 0
                for (offset in -radius..radius) {
                    val color = buffer[offset.coerceIn(0, height - 1) * width + x]
                    red += color shr 16 and 0xFF
                    green += color shr 8 and 0xFF
                    blue += color and 0xFF
                }
                for (y in 0 until height) {
                    pixels[y * width + x] = 0xFF000000.toInt() or
                        (BlurDivisionLookup[red] shl 16) or
                        (BlurDivisionLookup[green] shl 8) or
                        BlurDivisionLookup[blue]
                    val removed = buffer[(y - radius).coerceIn(0, height - 1) * width + x]
                    val added = buffer[(y + radius + 1).coerceIn(0, height - 1) * width + x]
                    red += (added shr 16 and 0xFF) - (removed shr 16 and 0xFF)
                    green += (added shr 8 and 0xFF) - (removed shr 8 and 0xFF)
                    blue += (added and 0xFF) - (removed and 0xFF)
                }
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }
}

private fun cachedBlurBackground(activity: MainActivity): Bitmap? {
    val root = activity.window.decorView
    val sourceWidth = root.width
    val sourceHeight = root.height
    if (sourceWidth < 1 || sourceHeight < 1) return null
    val now = SystemClock.elapsedRealtime()
    synchronized(BlurSnapshotLock) {
        CachedBlurSnapshot?.takeIf {
            !it.bitmap.isRecycled &&
                it.sourceWidth == sourceWidth &&
                it.sourceHeight == sourceHeight &&
                now - it.createdAt < 750L
        }?.let { return it.bitmap }
    }
    return null
}

private fun captureBlurSource(activity: MainActivity): BlurSnapshot? {
    val root = activity.window.decorView
    val sourceWidth = root.width
    val sourceHeight = root.height
    if (sourceWidth < 1 || sourceHeight < 1) return null

    val scale = 0.28f
    val bitmap = runCatching {
        Bitmap.createBitmap(
            (sourceWidth * scale).roundToInt().coerceAtLeast(1),
            (sourceHeight * scale).roundToInt().coerceAtLeast(1),
            Bitmap.Config.ARGB_8888
        ).also { bitmap ->
            Canvas(bitmap).apply {
                scale(scale, scale)
                root.draw(this)
            }
        }
    }.getOrNull() ?: return null
    return BlurSnapshot(bitmap, sourceWidth, sourceHeight, SystemClock.elapsedRealtime())
}

@Composable
internal fun BottomSheetWindowBehavior(
    hideStatusBar: Boolean = false,
    disableWindowAnimations: Boolean = true,
    blurVisible: Boolean = true
) {
    val view = LocalView.current
    val navigationAreaColor = LocalIsePalette.current.paper.toArgb()
    DisposableEffect(view, hideStatusBar, navigationAreaColor, disableWindowAnimations, blurVisible) {
        val window = (view.parent as? DialogWindowProvider)?.window ?: return@DisposableEffect onDispose {}
        val decor = window.decorView
        val originalWindowAnimations = window.attributes.windowAnimations
        val activity = view.context.findMainActivity()
        if (blurVisible) activity?.acquireOverlayBlur()
        if (disableWindowAnimations) window.setWindowAnimations(0)
        window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.navigationBarColor = navigationAreaColor
        if (Build.VERSION.SDK_INT >= 29) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0f)
        WindowCompat.getInsetsController(window, decor).apply {
            if (hideStatusBar) hide(WindowInsetsCompat.Type.statusBars()) else show(WindowInsetsCompat.Type.statusBars())
            show(WindowInsetsCompat.Type.navigationBars())
            isAppearanceLightNavigationBars = true
        }
        onDispose {
            if (blurVisible) activity?.releaseOverlayBlur()
            if (disableWindowAnimations) window.setWindowAnimations(originalWindowAnimations)
            window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConfirmationSheet(
    title: String,
    confirmText: String,
    loading: Boolean = false,
    errorState: SnackbarHostState? = null,
    centeredTitle: Boolean = false,
    prominentActions: Boolean = false,
    dismiss: () -> Unit,
    confirm: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var closing by remember { mutableStateOf(false) }
    val close: (() -> Unit) -> Unit = { afterClose ->
        if (!closing && !loading) {
            closing = true
            scope.launch {
                sheetState.hide()
                afterClose()
            }
        }
    }
    ModalBottomSheet(
        onDismissRequest = { close(dismiss) },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = Paper,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        BottomSheetWindowBehavior(blurVisible = !closing)
        BottomSheetHandle()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 22.dp)
        ) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = if (centeredTitle) TextAlign.Center else TextAlign.Start,
                    modifier = if (centeredTitle) Modifier.fillMaxWidth() else Modifier
                )
                Spacer(Modifier.height(18.dp))
            }
            if (prominentActions) {
                PrimaryButton(
                    text = confirmText,
                    loading = loading,
                    enabled = !closing,
                    onClick = { close(confirm) }
                )
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { close(dismiss) },
                    enabled = !loading && !closing,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Отмена", color = Forest)
                }
            } else {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { close(dismiss) }, enabled = !loading && !closing) {
                        Text("Отмена", color = Forest)
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = { close(confirm) }, enabled = !loading && !closing) {
                        if (loading) {
                            CircularProgressIndicator(Modifier.size(18.dp), color = Forest, strokeWidth = 2.dp)
                        } else {
                            Text(confirmText, color = Forest)
                        }
                    }
                }
            }
        }
        errorState?.let { AppErrorPopup(it) }
    }
}

@Composable
internal fun PreviewSystemBars(active: Boolean) {
    val view = LocalView.current
    LaunchedEffect(view, active) {
        val activity = view.context as? MainActivity ?: return@LaunchedEffect
        activity.updateThemeSystemBars(false)
        activity.updateSystemBars(active)
    }
}

@Composable
internal fun IncomingCallWindowBehavior(active: Boolean) {
    val view = LocalView.current
    DisposableEffect(view, active) {
        val activity = view.context as? MainActivity ?: return@DisposableEffect onDispose {}
        activity.updateIncomingCallWindow(active)
        if (active) activity.revealIncomingCallWindow()
        onDispose { if (active) activity.updateIncomingCallWindow(false) }
    }
}

internal fun callStatusText(call: CallUiState, now: Long): String {
    return when (call.phase) {
        CallPhase.Incoming -> if (call.video) "Видеозвонок" else "Аудиозвонок"
        CallPhase.Calling -> "Вызов…"
        CallPhase.Connecting -> "Соединение…"
        CallPhase.Active -> {
            val seconds = ((now - call.startedAt).coerceAtLeast(0L) / 1000L)
            "%02d:%02d".format(Locale.ROOT, seconds / 60, seconds % 60)
        }
    }
}

@Composable
internal fun CallActionButton(
    icon: ImageVector,
    description: String,
    background: Color,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 58.dp,
    iconTint: Color = Color.White
) {
    Box(
        Modifier.size(size).clip(CircleShape).background(background).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, description, tint = iconTint, modifier = Modifier.size(size * 0.43f))
    }
}

@Composable
internal fun CallVideoRenderer(
    controller: MessengerController,
    local: Boolean,
    modifier: Modifier
) {
    val eglContext = controller.callEglContext() ?: return
    val track = controller.callVideoTrack(local) ?: return
    if (local) {
        CallLocalVideoRenderer(track, eglContext, modifier)
    } else {
        CallRemoteVideoRenderer(track, eglContext, modifier)
    }
}

@Composable
internal fun CallLocalVideoRenderer(
    track: VideoTrack,
    eglContext: EglBase.Context,
    modifier: Modifier
) {
    key(track, eglContext) {
        AndroidView(
            factory = { context ->
                RoundedCallTextureRenderer(context).apply {
                    initialize(eglContext)
                    track.addSink(this)
                }
            },
            modifier = modifier,
            onRelease = { renderer ->
                track.removeSink(renderer)
                renderer.releaseRenderer()
            }
        )
    }
}

@Composable
internal fun CallRemoteVideoRenderer(
    track: VideoTrack,
    eglContext: EglBase.Context,
    modifier: Modifier
) {
    key(track, eglContext) {
        AndroidView(
            factory = { context ->
                SurfaceViewRenderer(context).apply {
                    init(eglContext, null)
                    setEnableHardwareScaler(true)
                    setMirror(false)
                    setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
                    setZOrderMediaOverlay(false)
                    track.addSink(this)
                }
            },
            modifier = modifier,
            update = { renderer ->
                renderer.setMirror(false)
                renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
            },
            onRelease = { renderer ->
                track.removeSink(renderer)
                renderer.release()
            }
        )
    }
}

internal class RoundedCallTextureRenderer(context: Context) : TextureView(context), TextureView.SurfaceTextureListener, VideoSink {
    private val renderer = EglRenderer("IseCallTexture")
    private var initialized = false
    private var surfaceAttached = false

    init {
        isOpaque = false
        surfaceTextureListener = this
        val radius = 18f * context.resources.displayMetrics.density
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        clipToOutline = true
    }

    fun initialize(sharedContext: EglBase.Context) {
        if (initialized) return
        renderer.init(sharedContext, EglBase.CONFIG_PLAIN, GlRectDrawer())
        initialized = true
        renderer.setMirror(false)
        if (width > 0 && height > 0) renderer.setLayoutAspectRatio(width.toFloat() / height.toFloat())
        if (isAvailable) surfaceTexture?.let(::attachSurface)
    }

    private fun attachSurface(texture: SurfaceTexture) {
        if (!initialized || surfaceAttached) return
        surfaceAttached = true
        renderer.createEglSurface(texture)
    }

    override fun onFrame(frame: org.webrtc.VideoFrame) {
        if (initialized) renderer.onFrame(frame)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (initialized && width > 0 && height > 0) renderer.setLayoutAspectRatio(width.toFloat() / height.toFloat())
    }

    override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
        if (initialized && width > 0 && height > 0) renderer.setLayoutAspectRatio(width.toFloat() / height.toFloat())
        attachSurface(texture)
    }

    override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
        if (initialized && width > 0 && height > 0) renderer.setLayoutAspectRatio(width.toFloat() / height.toFloat())
    }

    override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
        surfaceAttached = false
        if (initialized) {
            val released = CountDownLatch(1)
            renderer.releaseEglSurface(released::countDown)
            runCatching { released.await(1, TimeUnit.SECONDS) }
        }
        return true
    }

    override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit

    fun releaseRenderer() {
        if (!initialized) return
        initialized = false
        surfaceAttached = false
        renderer.release()
    }
}


@Composable
internal fun ReplyComposerPanel(
    message: MessageItem,
    token: String,
    cancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth().background(Paper).padding(start = 16.dp, end = 8.dp, top = 9.dp, bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.width(3.dp).height(36.dp).clip(CircleShape).background(Forest))
        Spacer(Modifier.width(8.dp))
        if ((message.kind == "image" || message.kind == "video") && message.mediaUrl.isNotBlank()) {
            ReplyMediaThumbnail(message, token, 36.dp)
            Spacer(Modifier.width(8.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(message.senderName, color = Forest, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(messagePreviewText(message), color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = cancel) {
            Icon(Icons.Rounded.Close, contentDescription = "Отменить ответ", tint = Forest, modifier = Modifier.size(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageActionsSheet(
    message: MessageItem,
    previewMessage: MessageItem = message,
    mediaItems: List<MessageItem>,
    token: String,
    replyMessage: MessageItem? = null,
    showSenderAvatar: Boolean = false,
    senderAvatar: String = "",
    showReadStatus: Boolean = true,
    dismiss: () -> Unit,
    delete: () -> Unit,
    edit: () -> Unit,
    copy: () -> Unit,
    open: () -> Unit,
    save: () -> Unit,
    reply: () -> Unit,
    forward: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val closeThen: (() -> Unit) -> Unit = { action ->
        scope.launch {
            sheetState.hide()
            action()
        }
    }
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = Color.Transparent,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        BottomSheetWindowBehavior()
        Column(Modifier.fillMaxWidth().bottomSheetPop()) {
            Box(Modifier.fillMaxWidth().padding(horizontal = 14.dp)) {
                MessageBubble(
                    message = previewMessage,
                    mediaItems = mediaItems,
                    token = token,
                    replyMessage = replyMessage,
                    showSenderAvatar = showSenderAvatar,
                    senderAvatar = senderAvatar,
                    openMedia = { _ -> },
                    showReadStatus = showReadStatus,
                    showMetadata = true,
                    interactive = false
                )
            }
            Spacer(Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Paper,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                shadowElevation = 2.dp
            ) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 8.dp)) {
                    BottomSheetHandle()
                    if (message.mine) MessageActionRow(Icons.Rounded.Delete, "Удалить", action = { closeThen(delete) })
                    if (message.mine && message.kind == "text") MessageActionRow(Icons.Rounded.Edit, "Изменить", action = { closeThen(edit) })
                    if (message.kind == "text" && firstMessageLink(message.text).isNotBlank()) {
                        MessageActionRow(Icons.AutoMirrored.Rounded.OpenInNew, "Открыть", action = { closeThen(open) })
                    }
                    when (message.kind) {
                        "file", "audio" -> MessageActionRow(Icons.Rounded.Download, "Сохранить в загрузки", action = { closeThen(save) })
                        "text" -> MessageActionRow(Icons.Rounded.ContentCopy, "Копировать", action = { closeThen(copy) })
                        else -> MessageActionRow(Icons.Rounded.Download, "Сохранить в галерею", action = { closeThen(save) })
                    }
                    MessageActionRow(Icons.AutoMirrored.Rounded.Reply, "Ответить", action = { closeThen(reply) })
                    MessageActionRow(
                        Icons.AutoMirrored.Rounded.Reply,
                        "Переслать",
                        action = { closeThen(forward) },
                        mirrorIcon = true
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatActionsSheet(
    chat: ChatItem,
    draft: String,
    notificationsEnabled: Boolean,
    archiveLabel: String,
    archiveIcon: ImageVector,
    actionLabel: String,
    actionIcon: ImageVector,
    dismiss: () -> Unit,
    rename: (() -> Unit)?,
    toggleNotifications: () -> Unit,
    archive: () -> Unit,
    delete: (() -> Unit)?
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val closeThen: (() -> Unit) -> Unit = { action ->
        scope.launch {
            sheetState.hide()
            action()
        }
    }
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = Color.Transparent,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(0.dp),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        BottomSheetWindowBehavior()
        Column(Modifier.fillMaxWidth().bottomSheetPop()) {
            Box(Modifier.fillMaxWidth().background(Canvas)) {
                ChatRow(chat = chat, draft = draft, interactive = false)
            }
            Spacer(Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Paper,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                shadowElevation = 2.dp
            ) {
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 8.dp)) {
                    BottomSheetHandle()
                    rename?.let { action ->
                        MessageActionRow(Icons.Rounded.Edit, "Переименовать для себя", action = { closeThen(action) })
                    }
                    MessageActionRow(
                        if (notificationsEnabled) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,
                        if (notificationsEnabled) "Отключить уведомления" else "Включить уведомления",
                        action = { closeThen(toggleNotifications) }
                    )
                    MessageActionRow(archiveIcon, archiveLabel, action = { closeThen(archive) })
                    delete?.let { action ->
                        MessageActionRow(actionIcon, actionLabel, action = { closeThen(action) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RenameChatSheet(
    chat: ChatItem,
    dismiss: () -> Unit,
    save: (String) -> Unit,
    reset: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var closing by remember(chat.id) { mutableStateOf(false) }
    var name by remember(chat.id, chat.name) {
        mutableStateOf(TextFieldValue(chat.name, selection = TextRange(chat.name.length)))
    }
    val canSave = name.text.trim().isNotEmpty()
    val closeThen: (() -> Unit) -> Unit = { action ->
        if (!closing) {
            closing = true
            focusManager.clearFocus()
            scope.launch {
                sheetState.hide()
                action()
            }
        }
    }
    LaunchedEffect(chat.id) {
        name = name.copy(selection = TextRange(name.text.length))
    }
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        containerColor = Paper,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        BottomSheetWindowBehavior()
        BottomSheetHandle()
        Column(
            Modifier.fillMaxWidth().bottomSheetPop().navigationBarsPadding().imePadding()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    if (it.text.codePointCount(0, it.text.length) <= 80) name = it
                },
                placeholder = { Text("Новое имя", color = Muted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (canSave) {
                        closeThen { save(name.text) }
                    }
                }),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = SoftSurface,
                    unfocusedContainerColor = SoftSurface,
                    cursorColor = Forest
                ),
                modifier = Modifier.fillMaxWidth().height(60.dp)
            )
            Spacer(Modifier.height(16.dp))
            PrimaryButton("Сохранить", loading = closing, enabled = canSave && !closing) {
                closeThen { save(name.text) }
            }
            TextButton(
                onClick = {
                    closeThen(reset)
                },
                enabled = !closing,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("По умолчанию", color = Forest)
            }
        }
    }
}

@Composable
internal fun BottomSheetHandle() {
    Box(
        Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.width(36.dp).height(4.dp).clip(CircleShape).background(Line))
    }
}

@Composable
internal fun MessageActionRow(icon: ImageVector, label: String, action: () -> Unit, mirrorIcon: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = action).padding(horizontal = 22.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Forest,
            modifier = Modifier.size(23.dp).graphicsLayer { scaleX = if (mirrorIcon) -1f else 1f }
        )
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Ink)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForwardMessageSheet(chats: List<ChatItem>, dismiss: () -> Unit, select: (ChatItem) -> Unit) {
    val displayedChats = remember(chats) { chats.sortedByDescending { it.saved } }
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        sheetGesturesEnabled = false,
        containerColor = Paper,
        scrimColor = OverlayScrimColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        BottomSheetWindowBehavior()
        BottomSheetHandle()
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.86f).bottomSheetPop()) {
            if (displayedChats.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Чатов пока нет", color = Muted, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 10.dp)) {
                    items(displayedChats, key = { it.id }, contentType = { "forward_chat" }) { chat ->
                        ChatRow(chat = chat, onClick = { select(chat) }, containerColor = Paper)
                    }
                }
            }
        }
    }
}

internal fun isVoiceMessageName(name: String): Boolean {
    return name.startsWith("voice_", ignoreCase = true)
}

internal val AudioFileExtensions = setOf(
    "aac", "amr", "flac", "m4a", "mid", "midi", "mp3", "mp4", "oga", "ogg", "opus", "wav", "wave", "webm", "wma"
)

internal fun isAudioMedia(mime: String, name: String): Boolean {
    val normalizedMime = mime.substringBefore(';').trim().lowercase(Locale.US)
    val extension = name.substringAfterLast('.', "").lowercase(Locale.US)
    return normalizedMime.startsWith("audio/") ||
        normalizedMime == "application/ogg" ||
        normalizedMime == "application/x-ogg" ||
        extension in AudioFileExtensions
}

internal fun MessageItem.isPlayableAudio(): Boolean {
    return kind == "audio" || kind == "file" && isAudioMedia(mediaMime, mediaName)
}

internal fun messagePreviewText(message: MessageItem): String {
    return when (message.kind) {
        "image" -> "Фото"
        "video" -> "Видео"
        "audio" -> if (isVoiceMessageName(message.mediaName)) "Голосовое сообщение" else "Аудио"
        "file" -> if (message.isPlayableAudio()) "Аудио" else "Файл"
        else -> message.text
    }
}

@Composable
internal fun MessageDateBubble(label: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            label,
            color = Ink,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Mint).padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
internal fun UnreadBadge(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier.size(23.dp).clip(CircleShape).background(Forest),
        contentAlignment = Alignment.Center
    ) {
        Text(
            count.coerceAtMost(99).toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
    }
}

@Composable
internal fun OnlineIndicator(
    avatarSize: Dp,
    backgroundColor: Color = Canvas,
    modifier: Modifier = Modifier
) {
    val indicatorSize = avatarSize * (16f / 54f)
    val borderSize = avatarSize * (2f / 54f)
    Box(
        modifier.size(indicatorSize).clip(CircleShape).background(backgroundColor)
            .padding(borderSize).clip(CircleShape).background(OnlineGreen)
    )
}

@Composable
internal fun PresenceStatus(chat: ChatItem) {
    Text(
        rememberPresenceStatusText(chat),
        fontSize = 12.sp,
        color = Muted,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun rememberPresenceStatusText(chat: ChatItem): String {
    var now by remember(chat.userId) { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(chat.online, chat.lastSeenAt) {
        now = System.currentTimeMillis()
        while (!chat.online) {
            delay(30_000)
            now = System.currentTimeMillis()
        }
    }
    return formatPresence(chat.online, chat.lastSeenAt, now)
}

internal fun chatMediaPreviewSize(width: Int, height: Int): Pair<Dp, Dp> {
    if (width < 1 || height < 1) return 250.dp to 210.dp
    val sourceWidth = width.toFloat()
    val sourceHeight = height.toFloat()
    val scale = minOf(250f / sourceWidth, 320f / sourceHeight)
    return (sourceWidth * scale).dp to (sourceHeight * scale).dp
}

@Composable
internal fun MediaMessageThumbnail(
    message: MessageItem,
    token: String,
    modifier: Modifier = Modifier,
    fillBounds: Boolean = false
) {
    val context = LocalContext.current
    val cacheKey = remember(message.kind, message.mediaUrl) {
        if (message.kind == "image") "remote_image_700_${message.mediaUrl}" else "remote_video_${message.mediaUrl}"
    }
    var bitmap by remember(message.id, message.mediaUrl) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    var duration by remember(message.id, message.mediaUrl) {
        mutableLongStateOf(message.mediaDuration.takeIf { it > 0L } ?: VideoDurationCache.get(message.mediaUrl) ?: 0L)
    }
    var loadProgress by remember(message.id) { mutableFloatStateOf(0f) }
    val previewSize = remember(message.mediaWidth, message.mediaHeight) {
        chatMediaPreviewSize(message.mediaWidth, message.mediaHeight)
    }
    val thumbnailShape = if (fillBounds) RoundedCornerShape(0.dp) else RoundedCornerShape(18.dp)
    val thumbnailSize = if (fillBounds) {
        modifier.fillMaxSize()
    } else {
        modifier.width(previewSize.first).height(previewSize.second)
    }
    val thumbnailFrame = thumbnailSize.clip(thumbnailShape).background(ForestDark)
    LaunchedEffect(message.id, message.mediaUrl) {
        if (bitmap == null) {
            bitmap = loadRemoteMediaThumbnail(context, message, token) { loadProgress = it }?.asImageBitmap()
        }
        if (message.kind == "video" && duration <= 0L) {
            duration = loadRemoteVideoDuration(context, message.mediaUrl, token)
        }
    }
    Box(
        thumbnailFrame,
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let { Image(it, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) }
        if (bitmap == null) MediaLoadingIndicator(progress = loadProgress)
        if (message.kind == "video" && duration > 0L) {
            Text(
                formatDuration(duration),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp).clip(RoundedCornerShape(7.dp))
                    .background(Color.Black.copy(alpha = 0.55f)).padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
internal fun MediaAlbumTile(
    message: MessageItem,
    token: String,
    modifier: Modifier,
    extraCount: Int,
    interactive: Boolean,
    openMedia: (MessageItem) -> Unit,
    onLongPress: (MessageItem) -> Unit
) {
    val interactionSource = remember(message.id) { MutableInteractionSource() }
    val interaction = if (interactive) {
        Modifier.combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { openMedia(message) },
            onLongClick = { onLongPress(message) }
        )
    } else Modifier
    Box(modifier.then(interaction), contentAlignment = Alignment.Center) {
        MediaMessageThumbnail(message, token, Modifier.fillMaxSize(), fillBounds = true)
        if (extraCount > 0) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.52f)))
            Text(
                "+$extraCount",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
internal fun MediaAlbum(
    messages: List<MessageItem>,
    token: String,
    modifier: Modifier = Modifier,
    interactive: Boolean = true,
    openMedia: (MessageItem) -> Unit,
    onLongPress: (MessageItem) -> Unit
) {
    val visible = messages.take(4)
    val albumShape = RoundedCornerShape(18.dp)
    Box(
        modifier.width(250.dp)
            .clip(albumShape)
            .background(ForestDark)
    ) {
        when (visible.size) {
            2 -> Row(Modifier.fillMaxWidth().height(180.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                visible.forEach { item ->
                    MediaAlbumTile(item, token, Modifier.weight(1f).fillMaxHeight(), 0, interactive, openMedia, onLongPress)
                }
            }
            3 -> Row(Modifier.fillMaxWidth().height(250.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                MediaAlbumTile(visible[0], token, Modifier.weight(1f).fillMaxHeight(), 0, interactive, openMedia, onLongPress)
                Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    MediaAlbumTile(visible[1], token, Modifier.fillMaxWidth().weight(1f), 0, interactive, openMedia, onLongPress)
                    MediaAlbumTile(visible[2], token, Modifier.fillMaxWidth().weight(1f), 0, interactive, openMedia, onLongPress)
                }
            }
            else -> Column(Modifier.fillMaxWidth().height(250.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                visible.chunked(2).forEachIndexed { rowIndex, row ->
                    Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        row.forEachIndexed { columnIndex, item ->
                            val isLastVisible = rowIndex == 1 && columnIndex == 1
                            MediaAlbumTile(
                                message = item,
                                token = token,
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                extraCount = if (isLastVisible) (messages.size - visible.size).coerceAtLeast(0) else 0,
                                interactive = interactive,
                                openMedia = openMedia,
                                onLongPress = onLongPress
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
internal fun MediaLoadingIndicator(modifier: Modifier = Modifier, progress: Float? = null, color: Color = Color.White) {
    if (progress == null) {
        CircularProgressIndicator(
            modifier = modifier.size(30.dp),
            color = color,
            trackColor = color.copy(alpha = 0.22f),
            strokeWidth = 3.dp
        )
    } else {
        val animatedProgress by animateFloatAsState(progress.coerceIn(0f, 1f), tween(120), label = "mediaLoadProgress")
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = modifier.size(30.dp),
            color = color,
            trackColor = Color.Transparent,
            strokeWidth = 3.dp
        )
    }
}

@Composable
internal fun CancellableLoadingIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    color: Color = Color.White,
    cancel: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier.size(36.dp).clip(CircleShape).clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = cancel
        ),
        contentAlignment = Alignment.Center
    ) {
        MediaLoadingIndicator(Modifier.size(30.dp), progress, color)
        Icon(
            Icons.Rounded.Close,
            contentDescription = "Отменить загрузку",
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
internal fun LocalPreviewImage(uri: Uri, onTap: () -> Unit, onZoomChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val cacheKey = remember(uri) { "local_preview_$uri" }
    var bitmap by remember(uri) { mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap()) }
    LaunchedEffect(uri) { if (bitmap == null) bitmap = loadLocalPreviewBitmap(context, uri)?.asImageBitmap() }
    bitmap?.let { PreviewImage(it, onTap, onZoomChanged) }
        ?: Box(
            Modifier.fillMaxSize().pointerInput(onTap) { detectTapGestures(onTap = { onTap() }) },
            contentAlignment = Alignment.Center
        ) { MediaLoadingIndicator() }
}

@Composable
internal fun RemotePreviewImage(
    message: MessageItem,
    token: String,
    onTap: () -> Unit,
    onZoomChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val cacheKey = remember(message.mediaUrl) { "remote_image_1800_${message.mediaUrl}" }
    var bitmap by remember(message.id, message.mediaUrl) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    var loadProgress by remember(message.id) { mutableFloatStateOf(0f) }
    LaunchedEffect(message.id) {
        if (bitmap == null) {
            bitmap = loadRemoteBitmap(context, message.mediaUrl, token, 1800, message.mediaSize) { loadProgress = it }?.asImageBitmap()
        }
    }
    bitmap?.let { PreviewImage(it, onTap, onZoomChanged) }
        ?: Box(
            Modifier.fillMaxSize().pointerInput(onTap) { detectTapGestures(onTap = { onTap() }) },
            contentAlignment = Alignment.Center
        ) { MediaLoadingIndicator(progress = loadProgress) }
}

@Composable
internal fun PreviewImage(
    image: androidx.compose.ui.graphics.ImageBitmap,
    onTap: () -> Unit,
    onZoomChanged: (Boolean) -> Unit
) {
    var scale by remember(image) { mutableFloatStateOf(1f) }
    var offset by remember(image) { mutableStateOf(Offset.Zero) }
    var containerSize by remember(image) { mutableStateOf(IntSize.Zero) }
    var zoomAnimation by remember(image) { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val latestOnTap by rememberUpdatedState(onTap)
    val latestOnZoomChanged by rememberUpdatedState(onZoomChanged)

    fun boundedOffset(candidate: Offset, targetScale: Float): Offset {
        if (
            containerSize.width <= 0 || containerSize.height <= 0 || image.width <= 0 || image.height <= 0 ||
            !candidate.x.isFinite() || !candidate.y.isFinite() || !targetScale.isFinite()
        ) return Offset.Zero
        val fittedScale = minOf(
            containerSize.width.toFloat() / image.width,
            containerSize.height.toFloat() / image.height
        )
        val maxX = ((image.width * fittedScale * targetScale - containerSize.width) / 2f).coerceAtLeast(0f)
        val maxY = ((image.height * fittedScale * targetScale - containerSize.height) / 2f).coerceAtLeast(0f)
        return Offset(candidate.x.coerceIn(-maxX, maxX), candidate.y.coerceIn(-maxY, maxY))
    }

    fun animateZoom(targetScale: Float, targetOffset: Offset) {
        zoomAnimation?.cancel()
        zoomAnimation = scope.launch {
            val startScale = scale
            val startOffset = offset
            animate(0f, 1f, animationSpec = tween(220, easing = FastOutSlowInEasing)) { progress, _ ->
                scale = startScale + (targetScale - startScale) * progress
                offset = boundedOffset(
                    Offset(
                        startOffset.x + (targetOffset.x - startOffset.x) * progress,
                        startOffset.y + (targetOffset.y - startOffset.y) * progress
                    ),
                    scale
                )
            }
            scale = targetScale
            offset = boundedOffset(targetOffset, targetScale)
        }
    }

    val zoomed = scale > 1.01f
    LaunchedEffect(zoomed) { latestOnZoomChanged(zoomed) }

    Box(
        Modifier.fillMaxSize().clipToBounds().onSizeChanged { containerSize = it }
            .pointerInput(image, containerSize) {
                awaitEachGesture {
                    var transforming = scale > 1.01f
                    var pointersDown: Boolean
                    do {
                        val event = awaitPointerEvent()
                        pointersDown = event.changes.any { it.pressed }
                        if (!transforming && event.changes.count { it.pressed } >= 2) {
                            zoomAnimation?.cancel()
                            transforming = true
                        }
                        if (transforming && pointersDown) {
                            val centroid = event.calculateCentroid()
                            val zoomChange = event.calculateZoom()
                            val pan = event.calculatePan()
                            if (
                                centroid.x.isFinite() && centroid.y.isFinite() && zoomChange.isFinite() && zoomChange > 0f &&
                                pan.x.isFinite() && pan.y.isFinite()
                            ) {
                                val oldScale = scale.takeIf { it.isFinite() && it > 0f } ?: 1f
                                val newScale = (oldScale * zoomChange).coerceIn(1f, 4f)
                                val center = Offset(containerSize.width / 2f, containerSize.height / 2f)
                                val focus = centroid - center
                                val scaleRatio = newScale / oldScale
                                val transformedOffset = offset * scaleRatio + focus * (1f - scaleRatio) + pan
                                scale = newScale
                                offset = boundedOffset(transformedOffset, newScale)
                            }
                            event.changes.forEach { it.consume() }
                        }
                    } while (pointersDown)

                    if (scale <= 1.03f) animateZoom(1f, Offset.Zero)
                }
            }
            .pointerInput(image, containerSize) {
                detectTapGestures(onTap = { latestOnTap() })
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
        )
    }
}

@Composable
internal fun LocalVideoPoster(media: DeviceMedia) {
    val context = LocalContext.current
    val cacheKey = remember(media.id, media.kind) { "device_${media.kind}_${media.id}" }
    var bitmap by remember(media.id, media.kind) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    LaunchedEffect(media.id) { if (bitmap == null) bitmap = loadDeviceMediaThumbnail(context, media)?.asImageBitmap() }
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        bitmap?.let { Image(it, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) }
            ?: MediaLoadingIndicator()
    }
}

@Composable
internal fun RemoteVideoPoster(message: MessageItem, token: String) {
    val context = LocalContext.current
    val cacheKey = remember(message.mediaUrl) { "remote_video_${message.mediaUrl}" }
    var bitmap by remember(message.id, message.mediaUrl) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    var loadProgress by remember(message.id) { mutableFloatStateOf(0f) }
    LaunchedEffect(message.id) {
        if (bitmap == null) bitmap = loadRemoteMediaThumbnail(context, message, token) { loadProgress = it }?.asImageBitmap()
    }
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        bitmap?.let { Image(it, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize()) }
            ?: MediaLoadingIndicator(progress = loadProgress)
    }
}

@Composable
internal fun PersistentRemoteVideo(message: MessageItem, token: String, controlsChanged: (Boolean) -> Unit = {}) {
    val context = LocalContext.current
    var file by remember(message.id) { mutableStateOf<File?>(null) }
    var loadProgress by remember(message.id) { mutableFloatStateOf(0f) }
    LaunchedEffect(message.id) {
        file = ensurePersistentMediaFile(context, message.mediaUrl, token, "video", message.mediaSize) { loadProgress = it }
    }
    file?.let { VideoPlayer(Uri.fromFile(it), controlsChanged = controlsChanged) }
        ?: MediaLoadingIndicator(progress = loadProgress)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VideoPlayer(
    uri: Uri,
    headers: Map<String, String> = emptyMap(),
    controlsChanged: (Boolean) -> Unit = {}
) {
    var videoView by remember(uri) { mutableStateOf<VideoView?>(null) }
    var ready by remember(uri) { mutableStateOf(false) }
    var playing by remember(uri) { mutableStateOf(false) }
    var position by remember(uri) { mutableIntStateOf(0) }
    var duration by remember(uri) { mutableIntStateOf(0) }
    var seeking by remember(uri) { mutableStateOf(false) }
    var controlsVisible by remember(uri) { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(videoView, ready, seeking) {
        while (ready) {
            if (!seeking) videoView?.let { view ->
                position = runCatching { view.currentPosition }.getOrDefault(position)
                playing = runCatching { view.isPlaying }.getOrDefault(false)
            }
            delay(250)
        }
    }
    LaunchedEffect(controlsVisible, playing) {
        if (controlsVisible && playing) {
            delay(2500)
            controlsVisible = false
        }
    }
    LaunchedEffect(controlsVisible) { controlsChanged(controlsVisible) }
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        key(uri) {
            AndroidView(
                factory = { context ->
                    VideoView(context).also { view ->
                        videoView = view
                        if (headers.isEmpty()) view.setVideoURI(uri) else view.setVideoURI(uri, headers)
                        view.setOnPreparedListener { player ->
                            player.isLooping = false
                            duration = player.duration.coerceAtLeast(0)
                            ready = true
                            playing = true
                            view.start()
                        }
                        view.setOnCompletionListener {
                            position = duration
                            playing = false
                            controlsVisible = true
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                onRelease = { view -> view.stopPlayback() }
            )
        }
        Box(
            Modifier.fillMaxSize().clickable(
                interactionSource = interactionSource,
                indication = null
            ) { controlsVisible = !controlsVisible }
        )
        if (!ready) {
            MediaLoadingIndicator()
        }
        AnimatedVisibility(
            visible = ready && controlsVisible,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(180)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            IconButton(
                onClick = {
                    videoView?.let { view ->
                        if (playing) {
                            view.pause()
                            playing = false
                        } else {
                            if (duration > 0 && position >= duration - 250) {
                                view.seekTo(0)
                                position = 0
                            }
                            view.start()
                            playing = true
                        }
                        controlsVisible = true
                    }
                },
                modifier = Modifier.size(56.dp)
            ) {
                PlaybackGlyph(playing)
            }
        }
        AnimatedVisibility(
            visible = ready && controlsVisible,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(180)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.48f))
                    .padding(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 10.dp)
            ) {
                PlaybackProgressBar(
                    position = position.toLong(),
                    duration = duration.toLong(),
                    seeking = seeking,
                    activeColor = Forest,
                    inactiveColor = Color.White.copy(alpha = 0.34f),
                    onValueChange = { value ->
                        seeking = true
                        position = value.toInt()
                        videoView?.seekTo(position)
                    },
                    onValueChangeFinished = {
                        videoView?.seekTo(position)
                        seeking = false
                        controlsVisible = true
                    }
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(formatDuration(position.toLong()), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    Text(formatDuration(duration.toLong()), color = Color.White.copy(alpha = 0.72f), fontSize = 12.sp)
                }
            }
        }
    }
    DisposableEffect(uri) {
        onDispose { videoView?.stopPlayback() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaybackProgressBar(
    position: Long,
    duration: Long,
    seeking: Boolean = false,
    activeColor: Color,
    inactiveColor: Color,
    onValueChange: (Long) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    touchHeight: Dp = 48.dp
) {
    val safeDuration = duration.coerceAtLeast(1L)
    val safePosition = position.coerceIn(0L, safeDuration)
    val progressFraction = when {
        duration <= 0L -> 0f
        position >= duration - 250L -> 1f
        else -> position.toFloat().div(duration).coerceIn(0f, 1f)
    }
    val animatedProgressFraction by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(280, easing = LinearEasing),
        label = "video_progress"
    )
    val displayedProgress = if (seeking) progressFraction else animatedProgressFraction
    Box(modifier.fillMaxWidth().height(touchHeight), contentAlignment = Alignment.Center) {
        Slider(
            value = safePosition.toFloat(),
            onValueChange = { onValueChange(it.toLong()) },
            onValueChangeFinished = onValueChangeFinished,
            valueRange = 0f..safeDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = { Box(Modifier.size(0.dp)) },
            track = { Box(Modifier.fillMaxWidth().height(4.dp)) },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(inactiveColor)
        ) {
            Box(
                Modifier.fillMaxWidth(displayedProgress).fillMaxHeight().clip(CircleShape).background(activeColor)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AudioWaveformProgressBar(
    position: Long,
    duration: Long,
    amplitudes: List<Float>,
    seeking: Boolean = false,
    activeColor: Color,
    inactiveColor: Color,
    onValueChange: (Long) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
    touchHeight: Dp = 36.dp,
    enabled: Boolean = true
) {
    val safeDuration = duration.coerceAtLeast(1L)
    val safePosition = position.coerceIn(0L, safeDuration)
    val progressFraction = when {
        duration <= 0L -> 0f
        position >= duration - 250L -> 1f
        else -> position.toFloat().div(duration).coerceIn(0f, 1f)
    }
    val waveformReveal by animateFloatAsState(
        targetValue = if (amplitudes.isEmpty()) 0f else 1f,
        animationSpec = tween(260, easing = FastOutSlowInEasing),
        label = "waveform_reveal"
    )
    val animatedProgressFraction by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(280, easing = LinearEasing),
        label = "audio_progress"
    )
    val displayedProgress = if (seeking) progressFraction else animatedProgressFraction
    Box(modifier.fillMaxWidth().height(touchHeight), contentAlignment = Alignment.Center) {
        ComposeCanvas(Modifier.fillMaxWidth().height(25.dp)) {
            val barWidth = 2.dp.toPx()
            val gap = 2.dp.toPx()
            val step = barWidth + gap
            val barCount = (size.width / step).toInt().coerceAtLeast(1)
            val waveformWidth = (barCount - 1) * step + barWidth
            val startX = (size.width - waveformWidth) / 2f
            val activeEdge = size.width * displayedProgress
            val minimumHeight = 4.dp.toPx()
            val drawBars: (Color) -> Unit = { color ->
                repeat(barCount) { index ->
                    val amplitude = if (amplitudes.isEmpty()) {
                        0.18f
                    } else {
                        val amplitudeIndex = (index * amplitudes.size / barCount).coerceAtMost(amplitudes.lastIndex)
                        0.18f + (amplitudes[amplitudeIndex] - 0.18f) * waveformReveal
                    }
                    val barHeight = (size.height * amplitude).coerceAtLeast(minimumHeight)
                    val x = startX + index * step
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, (size.height - barHeight) / 2f),
                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                    )
                }
            }
            drawBars(inactiveColor)
            if (activeEdge > 0f) clipRect(right = activeEdge) { drawBars(activeColor) }
        }
        Slider(
            value = safePosition.toFloat(),
            onValueChange = { onValueChange(it.toLong()) },
            onValueChangeFinished = onValueChangeFinished,
            enabled = enabled,
            valueRange = 0f..safeDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = { Box(Modifier.size(0.dp)) },
            track = { Box(Modifier.fillMaxWidth().height(25.dp)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun PlaybackGlyph(playing: Boolean) {
    Icon(
        if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
        contentDescription = if (playing) "Пауза" else "Воспроизвести",
        tint = Color.White,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
internal fun LinkifiedMessageText(
    text: String,
    color: Color,
    linkColor: Color,
    style: TextStyle,
    interactive: Boolean,
    onLongPress: () -> Unit
) {
    val context = LocalContext.current
    val links = remember(text) { messageLinks(text) }
    val annotated = remember(text, links, linkColor) {
        AnnotatedString.Builder(text).apply {
            links.forEach { link ->
                addStyle(
                    SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                    link.start,
                    link.end
                )
            }
        }.toAnnotatedString()
    }
    var layoutResult by remember(text) { mutableStateOf<TextLayoutResult?>(null) }
    val linkInteraction = if (interactive && links.isNotEmpty()) {
        Modifier.pointerInput(text, links) {
            detectTapGestures(
                onTap = { position ->
                    val offset = layoutResult?.getOffsetForPosition(position) ?: return@detectTapGestures
                    links.firstOrNull { offset >= it.start && offset < it.end }?.let { openExternalLink(context, it.value) }
                },
                onLongPress = { onLongPress() }
            )
        }
    } else Modifier
    Text(annotated, color = color, style = style, modifier = linkInteraction, onTextLayout = { layoutResult = it })
}

@Composable
internal fun MessageBubble(
    message: MessageItem,
    token: String,
    replyMessage: MessageItem? = null,
    modifier: Modifier = Modifier,
    mediaItems: List<MessageItem> = emptyList(),
    showSenderAvatar: Boolean = false,
    senderAvatar: String = "",
    openMedia: (MessageItem) -> Unit,
    openFile: () -> Unit = {},
    onLongPress: (MessageItem) -> Unit = {},
    onSwipeReply: (() -> Unit)? = null,
    onReplySwipeActiveChanged: (Boolean) -> Unit = {},
    onReplyReferenceClick: (Long) -> Unit = {},
    showReadStatus: Boolean = true,
    showMetadata: Boolean = true,
    interactive: Boolean = true
) {
    var dragOffset by remember(message.id) { mutableFloatStateOf(0f) }
    val replyThreshold = 68.dp
    val animatedOffset by animateFloatAsState(
        dragOffset,
        tween(if (dragOffset == 0f) 180 else 55, easing = FastOutSlowInEasing),
        label = "message_swipe"
    )
    val bubbleBrush = if (message.mine) UnifiedMessageBubbleBrush else IncomingMessageBubbleBrush
    val bubbleContentColor = if (message.mine) Color.White else Ink
    val bubbleLinkColor = if (message.mine) Color.White.copy(alpha = 0.92f) else ForestDark
    val bubbleDarkContent = !message.mine
    val pressInteraction = remember(message.id) { MutableInteractionSource() }
    val canInteract = interactive && message.kind != "call"
    val canSwipeReply = canInteract && onSwipeReply != null
    var replySwipeInProgress by remember(message.id) { mutableStateOf(false) }
    DisposableEffect(message.id, canSwipeReply) {
        onDispose {
            if (replySwipeInProgress) onReplySwipeActiveChanged(false)
        }
    }
    val displayedMedia = remember(message, mediaItems) { if (mediaItems.isEmpty()) listOf(message) else mediaItems }
    val interaction = if (canInteract) {
        Modifier
            .combinedClickable(
                interactionSource = pressInteraction,
                indication = null,
                onClick = {
                    when (message.kind) {
                        "image", "video" -> openMedia(message)
                        "file" -> openFile()
                    }
                },
                onLongClick = { onLongPress(message) }
            )
            .then(if (canSwipeReply) Modifier.pointerInput(message.id) {
                val threshold = replyThreshold.toPx()
                val limit = 108.dp.toPx()
                val direction = if (message.mine) -1f else 1f
                detectHorizontalDragGestures(
                    onDragStart = {
                        replySwipeInProgress = true
                        onReplySwipeActiveChanged(true)
                    },
                    onHorizontalDrag = { change, amount ->
                        if (amount * direction > 0f || dragOffset * direction > 0f) {
                            change.consume()
                            dragOffset = if (message.mine) {
                                (dragOffset + amount).coerceIn(-limit, 0f)
                            } else {
                                (dragOffset + amount).coerceIn(0f, limit)
                            }
                        }
                    },
                    onDragEnd = {
                        val shouldReply = dragOffset * direction >= threshold
                        dragOffset = 0f
                        replySwipeInProgress = false
                        onReplySwipeActiveChanged(false)
                        if (shouldReply) onSwipeReply?.invoke()
                    },
                    onDragCancel = {
                        dragOffset = 0f
                        replySwipeInProgress = false
                        onReplySwipeActiveChanged(false)
                    }
                )
            } else Modifier)
    } else Modifier
    Box(modifier.fillMaxWidth()) {
        val directedOffset = animatedOffset * if (message.mine) -1f else 1f
        if (canSwipeReply && directedOffset > 8f) {
            Icon(
                Icons.AutoMirrored.Rounded.Reply,
                contentDescription = null,
                tint = Forest,
                modifier = Modifier.align(if (message.mine) Alignment.CenterEnd else Alignment.CenterStart)
                    .padding(start = if (message.mine) 0.dp else 12.dp, end = if (message.mine) 12.dp else 0.dp)
                    .size(25.dp).graphicsLayer {
                        val threshold = replyThreshold.toPx()
                        alpha = (directedOffset / threshold).coerceIn(0f, 1f)
                        translationX = if (message.mine) {
                            (threshold - directedOffset).coerceAtLeast(0f)
                        } else {
                            (directedOffset - threshold).coerceAtMost(0f)
                        }
                    }
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = when {
                message.kind == "call" -> Arrangement.Center
                message.mine -> Arrangement.End
                else -> Arrangement.Start
            }
        ) {
            Row(
                (if (animatedOffset == 0f) Modifier else Modifier.graphicsLayer {
                    translationX = animatedOffset
                }).then(interaction),
                verticalAlignment = Alignment.Bottom
            ) {
                if (showSenderAvatar) {
                    Column {
                        Avatar(message.senderName.ifBlank { "Участник" }, 32.dp, senderAvatar, message.senderId)
                        if (showMetadata && message.kind != "call") Spacer(Modifier.height(17.dp))
                    }
                    Spacer(Modifier.width(7.dp))
                }
                Column(horizontalAlignment = if (message.mine) Alignment.End else Alignment.Start) {
                    if (message.kind == "call") {
                        CallMessageBubble(message = message)
                    } else if (message.kind == "image" || message.kind == "video") {
                        val contextMessage = displayedMedia.firstOrNull {
                            it.replyToId > 0L || it.forwardedFromName.isNotBlank()
                        } ?: message
                        val hasMessageContext = contextMessage.replyToId > 0L || contextMessage.forwardedFromName.isNotBlank()
                        val mediaBubbleShape = RoundedCornerShape(18.dp)
                        val mediaContentWidth = if (displayedMedia.size > 1) {
                            250.dp
                        } else {
                            chatMediaPreviewSize(message.mediaWidth, message.mediaHeight).first
                        }
                        Column(
                            Modifier.width(mediaContentWidth + 4.dp)
                                .clip(mediaBubbleShape)
                                .background(bubbleBrush),
                            horizontalAlignment = if (message.mine) Alignment.End else Alignment.Start
                        ) {
                            if (hasMessageContext) {
                                MessageContext(
                                    contextMessage,
                                    replyMessage = replyMessage,
                                    token = token,
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(start = 9.dp, end = 9.dp, top = 7.dp),
                                    coloredBubble = true,
                                    darkContent = bubbleDarkContent,
                                    onReplyClick = onReplyReferenceClick.takeIf { interactive }
                                )
                                Spacer(Modifier.height(4.dp))
                            }
                            Box(
                                Modifier.padding(
                                    start = 2.dp,
                                    end = 2.dp,
                                    top = if (hasMessageContext) 0.dp else 2.dp,
                                    bottom = 2.dp
                                )
                            ) {
                                if (displayedMedia.size > 1) {
                                    MediaAlbum(
                                        messages = displayedMedia,
                                        token = token,
                                        interactive = interactive,
                                        openMedia = openMedia,
                                        onLongPress = onLongPress
                                    )
                                } else {
                                    MediaMessageThumbnail(
                                        message,
                                        token
                                    )
                                }
                                if (showMetadata) {
                                    Surface(
                                        modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp),
                                        color = Color.Black.copy(alpha = 0.42f),
                                        shape = RoundedCornerShape(10.dp),
                                        tonalElevation = 0.dp,
                                        shadowElevation = 0.dp
                                    ) {
                                        Row(
                                            Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            MessageTimeAndStatus(message, Color.White, showReadStatus)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (message.isPlayableAudio()) {
                        AudioMessageBubble(
                            message = message,
                            token = token,
                            replyMessage = replyMessage,
                            bubbleBrush = bubbleBrush,
                            contentColor = bubbleContentColor,
                            showMetadata = showMetadata,
                            showReadStatus = showReadStatus,
                            interactive = interactive,
                            onReplyReferenceClick = onReplyReferenceClick.takeIf { interactive }
                        )
                    } else if (message.kind == "file") {
                        FileMessageBubble(
                            message = message,
                            token = token,
                            replyMessage = replyMessage,
                            bubbleBrush = bubbleBrush,
                            contentColor = bubbleContentColor,
                            showMetadata = showMetadata,
                            showReadStatus = showReadStatus,
                            onReplyReferenceClick = onReplyReferenceClick.takeIf { interactive }
                        )
                    } else {
                        Surface(
                            modifier = Modifier.widthIn(max = 320.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = Color.Transparent,
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp
                        ) {
                            Column(
                                Modifier.background(bubbleBrush)
                                    .padding(horizontal = 15.dp, vertical = 10.dp)
                            ) {
                                MessageContext(
                                    message,
                                    replyMessage = replyMessage,
                                    token = token,
                                    coloredBubble = true,
                                    darkContent = bubbleDarkContent,
                                    onReplyClick = onReplyReferenceClick.takeIf { interactive }
                                )
                                if (message.replyToId > 0L || message.forwardedFromName.isNotBlank()) Spacer(Modifier.height(7.dp))
                                LinkifiedMessageText(
                                    text = message.text,
                                    color = bubbleContentColor,
                                    linkColor = bubbleLinkColor,
                                    style = MaterialTheme.typography.bodyLarge,
                                    interactive = interactive,
                                    onLongPress = { onLongPress(message) }
                                )
                                if (showMetadata) {
                                    Spacer(Modifier.height(3.dp))
                                    Row(
                                        Modifier.align(Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        MessageTimeAndStatus(message, bubbleContentColor, showReadStatus)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun AudioMessageBubble(
    message: MessageItem,
    token: String,
    replyMessage: MessageItem? = null,
    bubbleBrush: Brush,
    contentColor: Color,
    modifier: Modifier = Modifier,
    showMetadata: Boolean = true,
    showReadStatus: Boolean = true,
    interactive: Boolean = true,
    onReplyReferenceClick: ((Long) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val audioManager = remember { context.getSystemService(AudioManager::class.java) }
    val playerState = remember(message.id) { mutableStateOf<MediaPlayer?>(null) }
    var preparing by remember(message.id) { mutableStateOf(false) }
    var downloadProgress by remember(message.id) { mutableFloatStateOf(0f) }
    var playing by remember(message.id) { mutableStateOf(false) }
    var position by remember(message.id) { mutableLongStateOf(0L) }
    var duration by remember(message.id) { mutableLongStateOf(message.mediaDuration.coerceAtLeast(0L)) }
    var seeking by remember(message.id) { mutableStateOf(false) }
    val waveformCacheKey = remember(message.id, message.mediaUrl, message.mediaSize) {
        "${message.id}:${message.mediaUrl}:${message.mediaSize}"
    }
    var waveform by remember(waveformCacheKey) {
        mutableStateOf(AudioWaveformCache.get(waveformCacheKey).orEmpty())
    }
    val focusListener = remember(message.id) {
        AudioManager.OnAudioFocusChangeListener { change ->
            if (change == AudioManager.AUDIOFOCUS_LOSS || change == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                scope.launch {
                    playerState.value?.takeIf { it.isPlaying }?.pause()
                    playing = false
                    if (ActiveAudioMessageId == message.id) ActiveAudioMessageId = 0L
                }
            }
        }
    }
    @Suppress("DEPRECATION")
    fun abandonFocus() {
        audioManager.abandonAudioFocus(focusListener)
    }
    @Suppress("DEPRECATION")
    fun requestFocus(): Boolean {
        return audioManager.requestAudioFocus(
            focusListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }
    fun pause() {
        playerState.value?.takeIf { it.isPlaying }?.pause()
        playing = false
        if (ActiveAudioMessageId == message.id) ActiveAudioMessageId = 0L
        abandonFocus()
    }
    fun play() {
        val existing = playerState.value
        if (existing != null) {
            if (requestFocus()) {
                ActiveAudioMessageId = message.id
                existing.start()
                playing = true
            }
            return
        }
        if (preparing) return
        scope.launch {
            preparing = true
            downloadProgress = 0f
            var preparedPlayer: MediaPlayer? = null
            try {
                val file = ensurePersistentMediaFile(
                    context,
                    message.mediaUrl,
                    token,
                    "audio",
                    message.mediaSize,
                    message.mediaName
                ) { progress ->
                    scope.launch { downloadProgress = progress }
                } ?: return@launch
                scope.launch {
                    val decoded = loadAudioWaveform(file, waveformCacheKey)
                    if (decoded.isNotEmpty()) waveform = decoded
                }
                preparedPlayer = withContext(Dispatchers.IO) {
                    MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        setDataSource(file.absolutePath)
                        prepare()
                    }
                }
                val readyPlayer = preparedPlayer
                duration = readyPlayer.duration.toLong().coerceAtLeast(message.mediaDuration)
                readyPlayer.setOnCompletionListener {
                    scope.launch {
                        position = duration
                        playing = false
                        if (ActiveAudioMessageId == message.id) ActiveAudioMessageId = 0L
                        abandonFocus()
                    }
                }
                playerState.value = readyPlayer
                preparedPlayer = null
                position = readyPlayer.currentPosition.toLong()
                if (requestFocus()) {
                    ActiveAudioMessageId = message.id
                    readyPlayer.start()
                    playing = true
                }
            } finally {
                preparedPlayer?.release()
                preparing = false
            }
        }
    }
    LaunchedEffect(waveformCacheKey) {
        if (waveform.isEmpty()) {
            val file = ensurePersistentMediaFile(
                context,
                message.mediaUrl,
                token,
                "audio",
                message.mediaSize,
                message.mediaName
            )
            if (file != null) {
                waveform = loadAudioWaveform(file, waveformCacheKey)
            }
        }
    }
    LaunchedEffect(ActiveAudioMessageId) {
        if (ActiveAudioMessageId != message.id && playing) {
            playerState.value?.takeIf { it.isPlaying }?.pause()
            playing = false
            abandonFocus()
        }
    }
    LaunchedEffect(playing) {
        while (playing) {
            if (!seeking) position = playerState.value?.currentPosition?.toLong() ?: position
            delay(250)
        }
    }
    DisposableEffect(message.id) {
        onDispose {
            playerState.value?.release()
            playerState.value = null
            abandonFocus()
            if (ActiveAudioMessageId == message.id) ActiveAudioMessageId = 0L
        }
    }
    Surface(
        modifier = modifier.widthIn(min = 218.dp, max = 286.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            Modifier.background(bubbleBrush)
                .padding(horizontal = 11.dp, vertical = 8.dp)
        ) {
            MessageContext(
                message,
                replyMessage = replyMessage,
                token = token,
                coloredBubble = true,
                darkContent = !message.mine,
                onReplyClick = onReplyReferenceClick
            )
            if (message.replyToId > 0L || message.forwardedFromName.isNotBlank()) Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val interactionSource = remember { MutableInteractionSource() }
                val playbackInteraction = if (interactive) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { if (playing) pause() else play() }
                    )
                } else Modifier
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(contentColor.copy(alpha = 0.14f))
                        .then(playbackInteraction),
                    contentAlignment = Alignment.Center
                ) {
                    if (preparing) {
                        CircularProgressIndicator(
                            progress = { downloadProgress.coerceIn(0f, 1f) },
                            modifier = Modifier.size(22.dp),
                            color = contentColor,
                            trackColor = contentColor.copy(alpha = 0.2f),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Icon(
                            if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (playing) "Пауза" else "Воспроизвести",
                            tint = contentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    AudioWaveformProgressBar(
                        position = position,
                        duration = duration,
                        amplitudes = waveform,
                        seeking = seeking,
                        activeColor = contentColor,
                        inactiveColor = contentColor.copy(alpha = 0.25f),
                        onValueChange = { value ->
                            seeking = true
                            position = value
                            playerState.value?.seekTo(value.coerceIn(0L, Int.MAX_VALUE.toLong()).toInt())
                        },
                        onValueChangeFinished = {
                            playerState.value?.seekTo(position.coerceIn(0L, Int.MAX_VALUE.toLong()).toInt())
                            seeking = false
                        },
                        touchHeight = 30.dp,
                        enabled = interactive
                    )
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(formatDuration(position), color = contentColor.copy(alpha = 0.72f), fontSize = 10.sp)
                        Spacer(Modifier.weight(1f))
                        Text(formatDuration(duration), color = contentColor.copy(alpha = 0.72f), fontSize = 10.sp)
                    }
                }
            }
            if (showMetadata) {
                Spacer(Modifier.height(2.dp))
                Row(Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                    MessageTimeAndStatus(message, contentColor, showReadStatus)
                }
            }
        }
    }
}

@Composable
internal fun FileMessageBubble(
    message: MessageItem,
    token: String,
    replyMessage: MessageItem? = null,
    bubbleBrush: Brush,
    contentColor: Color,
    modifier: Modifier = Modifier,
    showMetadata: Boolean = true,
    showReadStatus: Boolean = true,
    onReplyReferenceClick: ((Long) -> Unit)? = null
) {
    Surface(
        modifier = modifier.widthIn(min = 210.dp, max = 320.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Column(
            Modifier.background(bubbleBrush)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            MessageContext(
                message,
                replyMessage = replyMessage,
                token = token,
                coloredBubble = true,
                darkContent = !message.mine,
                onReplyClick = onReplyReferenceClick
            )
            if (message.replyToId > 0L || message.forwardedFromName.isNotBlank()) Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(42.dp).clip(CircleShape).background(contentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (message.mediaMime.startsWith("audio/")) Icons.Rounded.Audiotrack else Icons.AutoMirrored.Rounded.InsertDriveFile,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(23.dp)
                    )
                }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        message.mediaName.ifBlank { "Файл" },
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(formatFileSize(message.mediaSize), color = contentColor.copy(alpha = 0.72f), fontSize = 12.sp)
                }
            }
            if (showMetadata) {
                Spacer(Modifier.height(3.dp))
                Row(Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
                    MessageTimeAndStatus(message, contentColor, showReadStatus)
                }
            }
        }
    }
}

@Composable
internal fun CallMessageBubble(
    message: MessageItem,
    modifier: Modifier = Modifier
) {
    MessageDateBubble(callMessageLabel(message), modifier)
}

internal fun callMessageLabel(message: MessageItem): String {
    val kind = if (message.mediaMime == "video") "видеозвонок" else "аудиозвонок"
    val direction = if (message.mine) "Исходящий" else "Входящий"
    return when (message.mediaName) {
        "completed" -> if (message.mediaSize > 0L) "$direction $kind · ${formatCallSeconds(message.mediaSize)}" else "$direction $kind · завершён"
        "declined" -> "$direction $kind · отклонён"
        "busy" -> "$direction $kind · занято"
        "failed" -> "$direction $kind · ошибка соединения"
        "cancelled" -> if (message.mine) "Исходящий $kind · отменён" else "Пропущенный $kind"
        "missed" -> if (message.mine) "Исходящий $kind · нет ответа" else "Пропущенный $kind"
        else -> "$direction $kind"
    }
}

@Composable
internal fun MessageContext(
    message: MessageItem,
    replyMessage: MessageItem? = null,
    token: String = "",
    modifier: Modifier = Modifier,
    coloredBubble: Boolean = false,
    darkContent: Boolean = false,
    onReplyClick: ((Long) -> Unit)? = null
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (message.forwardedFromName.isNotBlank()) {
            MessageReferencePanel(
                title = "Переслано от:",
                preview = message.forwardedFromName,
                avatarName = message.forwardedFromName,
                avatarUserId = message.forwardedFromId,
                token = token,
                avatarBesidePreview = true,
                coloredBubble = coloredBubble,
                darkContent = darkContent
            )
        }
        if (message.replyToId > 0L) {
            MessageReferencePanel(
                title = message.replySenderName,
                preview = replyPreviewText(message),
                avatarName = message.replySenderName,
                media = replyMessage?.takeIf {
                    it.id == message.replyToId && (it.kind == "image" || it.kind == "video")
                },
                token = token,
                coloredBubble = coloredBubble,
                darkContent = darkContent,
                onClick = onReplyClick?.let { action -> { action(message.replyToId) } }
            )
        }
    }
}

@Composable
internal fun MessageReferencePanel(
    title: String,
    preview: String,
    avatarName: String,
    avatarUserId: Long = 0L,
    media: MessageItem? = null,
    token: String = "",
    avatarBesidePreview: Boolean = false,
    coloredBubble: Boolean = false,
    darkContent: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val action = if (onClick == null) Modifier else Modifier.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
    val accentColor = when {
        !coloredBubble -> ForestDark
        darkContent -> Forest
        else -> Color.White.copy(alpha = 0.9f)
    }
    val previewColor = when {
        !coloredBubble -> Muted
        darkContent -> Ink.copy(alpha = 0.78f)
        else -> Color.White.copy(alpha = 0.88f)
    }
    Column(
        modifier = action.padding(vertical = 2.dp),
    ) {
        if (avatarBesidePreview) {
            Text(title, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                ForwardedUserAvatar(avatarName, avatarUserId, token, 16.dp)
                Spacer(Modifier.width(5.dp))
                Text(preview, color = previewColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.widthIn(max = 199.dp))
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(3.dp).height(34.dp).clip(CircleShape).background(accentColor))
                Spacer(Modifier.width(7.dp))
                if (media != null && media.mediaUrl.isNotBlank()) {
                    ReplyMediaThumbnail(media, token, 34.dp)
                    Spacer(Modifier.width(7.dp))
                }
                Column(Modifier.widthIn(max = if (media == null) 210.dp else 169.dp)) {
                    Text(title, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(preview, color = previewColor, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
internal fun ForwardedUserAvatar(name: String, userId: Long, token: String, size: Dp) {
    val context = LocalContext.current
    val cacheKey = remember(userId) { "forwarded_avatar_$userId" }
    var bitmap by remember(userId) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    LaunchedEffect(userId, token) {
        if (bitmap == null && userId > 0L && token.isNotBlank()) {
            val loaded = loadRemoteBitmap(context, "/avatars/$userId", token, 96)
            if (loaded != null) {
                BitmapMemoryCache.put(cacheKey, loaded)
                bitmap = loaded.asImageBitmap()
            }
        }
    }
    Box(Modifier.size(size).clip(CircleShape), contentAlignment = Alignment.Center) {
        Avatar(name, size, "", userId.takeIf { it > 0L } ?: name.hashCode().toLong())
        bitmap?.let {
            Image(it, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
internal fun ReplyMediaThumbnail(message: MessageItem, token: String, size: Dp) {
    val context = LocalContext.current
    val thumbnailSizePx = with(LocalDensity.current) { size.roundToPx() }.coerceAtLeast(1)
    val cacheKey = remember(message.kind, message.mediaUrl, thumbnailSizePx) {
        "reply_${thumbnailSizePx}_${message.kind}_${message.mediaUrl}"
    }
    var bitmap by remember(message.id, message.mediaUrl, thumbnailSizePx) {
        mutableStateOf(BitmapMemoryCache.get(cacheKey)?.asImageBitmap())
    }
    LaunchedEffect(message.id, message.mediaUrl, thumbnailSizePx, token) {
        if (bitmap == null) {
            val loaded = loadRemoteMediaThumbnail(context, message, token)
            if (loaded != null) {
                val cropped = centerCropSquareBitmap(loaded, thumbnailSizePx)
                BitmapMemoryCache.put(cacheKey, cropped)
                bitmap = cropped.asImageBitmap()
            }
        }
    }
    Box(
        Modifier.requiredSize(size).clip(RoundedCornerShape(7.dp)).background(ForestDark),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
        }
        if (message.kind == "video") {
            Icon(
                Icons.Rounded.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}

internal fun centerCropSquareBitmap(source: Bitmap, targetSize: Int): Bitmap {
    if (source.width < 1 || source.height < 1 || targetSize < 1) return source
    val scale = maxOf(
        targetSize.toFloat() / source.width.toFloat(),
        targetSize.toFloat() / source.height.toFloat()
    )
    val scaledWidth = source.width * scale
    val scaledHeight = source.height * scale
    val left = (targetSize - scaledWidth) / 2f
    val top = (targetSize - scaledHeight) / 2f
    return Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888).also { result ->
        Canvas(result).drawBitmap(
            source,
            null,
            RectF(left, top, left + scaledWidth, top + scaledHeight),
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        )
    }
}

internal fun replyPreviewText(message: MessageItem): String {
    return when (message.replyKind) {
        "image" -> "Фото"
        "video" -> "Видео"
        "audio" -> "Аудио"
        "file" -> "Файл"
        else -> message.replyText
    }
}

@Composable
internal fun MessageTimeAndStatus(message: MessageItem, contentColor: Color = Color.White, showReadStatus: Boolean = true) {
    if (message.edited) {
        Text("изменено", fontSize = 11.sp, color = contentColor.copy(alpha = 0.72f))
        Spacer(Modifier.width(4.dp))
    }
    Text(formatTime(message.createdAt), fontSize = 11.sp, color = contentColor.copy(alpha = 0.72f))
    if (message.mine && showReadStatus) {
        Spacer(Modifier.width(3.dp))
        MessageReadStatus(message.read, contentColor.copy(alpha = 0.84f))
    }
}

@Composable
internal fun MessageReadStatus(read: Boolean, tint: Color) {
    Box(Modifier.width(if (read) 19.dp else 14.dp).height(14.dp)) {
        Icon(
            Icons.Rounded.Done,
            contentDescription = if (read) "Прочитано" else "Отправлено",
            tint = tint,
            modifier = Modifier.size(14.dp).align(Alignment.CenterStart)
        )
        if (read) {
            Icon(
                Icons.Rounded.Done,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(14.dp).align(Alignment.CenterStart).offset(x = 5.dp)
            )
        }
    }
}

@Composable
internal fun MessageComposer(
    value: String,
    editing: Boolean,
    voiceRecording: Boolean,
    voiceRecordingDuration: Long,
    uploadProgress: Float?,
    cancelUpload: () -> Unit,
    onValueChange: (String) -> Unit,
    onAttach: () -> Unit,
    onCancelEditing: () -> Unit,
    onVoiceRecordingStart: () -> Boolean,
    onVoiceRecordingCancel: () -> Unit,
    onVoiceRecordingSend: () -> Unit,
    onSend: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }
    var wasEditing by remember { mutableStateOf(editing) }
    LaunchedEffect(editing, value) {
        val startedEditing = editing && !wasEditing
        wasEditing = editing
        if (startedEditing) {
            fieldValue = TextFieldValue(value, selection = TextRange(value.length))
        } else if (fieldValue.text != value) {
            fieldValue = TextFieldValue(value, selection = TextRange(value.length))
        }
    }
    OutlinedTextField(
            value = fieldValue,
            onValueChange = { updated ->
                if (updated.text.codePointCount(0, updated.text.length) <= 4000) {
                    fieldValue = updated
                    onValueChange(updated.text)
                }
            },
            readOnly = voiceRecording,
            placeholder = {
                if (voiceRecording) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            formatDuration(voiceRecordingDuration),
                            color = Forest,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Text("Сообщение", color = Muted)
                }
            },
            leadingIcon = when {
                voiceRecording || editing -> {
                    {
                    IconButton(onClick = if (voiceRecording) {
                        onVoiceRecordingCancel
                    } else {
                        {
                            focusManager.clearFocus()
                            onCancelEditing()
                        }
                    }) {
                    Icon(
                        if (voiceRecording) Icons.Rounded.Delete else Icons.Rounded.Close,
                        contentDescription = if (voiceRecording) "Отменить запись" else "Отменить изменение",
                        tint = Forest,
                        modifier = Modifier.size(23.dp)
                    )
                }
                    }
                }
                else -> null
            },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (uploadProgress != null) {
                        val animatedProgress by animateFloatAsState(
                            targetValue = uploadProgress.coerceIn(0f, 1f),
                            animationSpec = tween(180, easing = LinearEasing),
                            label = "file_upload"
                        )
                        Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                            CancellableLoadingIndicator(
                                progress = animatedProgress,
                                color = Forest,
                                cancel = cancelUpload
                            )
                        }
                    } else if (editing || fieldValue.text.isNotBlank()) {
                        IconButton(onClick = { onSend(fieldValue.text) }) {
                            Icon(
                                if (editing) Icons.Rounded.Done else Icons.AutoMirrored.Rounded.Send,
                                contentDescription = if (editing) "Сохранить" else "Отправить",
                                tint = Forest,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    } else if (voiceRecording) {
                        VoiceRecordButton(recording = true, start = { false }, send = onVoiceRecordingSend)
                    } else {
                        VoiceRecordButton(
                            recording = false,
                            start = {
                                focusManager.clearFocus()
                                onVoiceRecordingStart()
                            },
                            send = onVoiceRecordingSend
                        )
                        IconButton(onClick = {
                            focusManager.clearFocus()
                            onAttach()
                        }) {
                            Icon(
                                Icons.Rounded.AttachFile,
                                contentDescription = "Вложения",
                                tint = Forest,
                                modifier = Modifier.size(23.dp)
                            )
                        }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend(fieldValue.text) }),
            minLines = 1,
            maxLines = 5,
            shape = RectangleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Paper,
                unfocusedContainerColor = Paper,
                cursorColor = Forest,
                focusedLabelColor = Forest,
                unfocusedLabelColor = Muted
            ),
            modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp, max = 140.dp)
    )
}

@Composable
internal fun VoiceRecordButton(
    recording: Boolean,
    start: () -> Boolean,
    send: () -> Unit
) {
    IconButton(onClick = { if (recording) send() else start() }) {
        Icon(
            if (recording) Icons.AutoMirrored.Rounded.Send else Icons.Rounded.Mic,
            contentDescription = if (recording) "Отправить голосовое сообщение" else "Записать голосовое сообщение",
            tint = Forest,
            modifier = Modifier.size(if (recording) 23.dp else 24.dp)
        )
    }
}


@Composable
internal fun ChatAvatar(chat: ChatItem, size: androidx.compose.ui.unit.Dp) {
    if (chat.saved) {
        Box(
            Modifier.size(size).clip(CircleShape).background(Brush.linearGradient(listOf(Forest, ForestDark))),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Bookmark, contentDescription = null, tint = Color.White, modifier = Modifier.size(size * 0.46f))
        }
    } else if (chat.group) {
        Avatar(chat.name, size, chat.avatar, chat.avatarGradientSeed.takeIf { it != 0L } ?: chat.id)
    } else {
        Avatar(chat.name, size, chat.avatar, chat.userId)
    }
}

@Composable
internal fun Avatar(name: String, size: androidx.compose.ui.unit.Dp, avatar: String = "", userId: Long = 0) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val background = AvatarGradients[avatarGradientIndex(userId, name)]
    val cacheKey = remember(avatar) { if (avatar.isBlank()) "" else avatarBitmapCacheKey(avatar) }
    val initial = remember(name) { name.trim().firstOrNull()?.uppercase() ?: "?" }
    val initialTextSize = with(density) { (size.value * 0.38f).sp.toPx() }
    val initialPaint = remember(initialTextSize) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = initialTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
    var image by remember(avatar) {
        mutableStateOf(cacheKey.takeIf(String::isNotEmpty)?.let(BitmapMemoryCache::get)?.asImageBitmap())
    }
    LaunchedEffect(avatar) {
        if (avatar.isBlank()) image = null
        else if (image == null) image = loadPersistentAvatar(context, avatar)?.asImageBitmap()
    }
    Box(Modifier.size(size).clip(CircleShape).background(background), contentAlignment = Alignment.Center) {
        if (image != null) {
            Image(image!!, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        } else {
            ComposeCanvas(Modifier.fillMaxSize()) {
                val baseline = this.size.height / 2f - (initialPaint.ascent() + initialPaint.descent()) / 2f
                drawContext.canvas.nativeCanvas.drawText(initial, this.size.width / 2f, baseline, initialPaint)
            }
        }
    }
}

internal fun avatarGradientIndex(userId: Long, name: String): Int {
    var value = (if (userId != 0L) userId else name.hashCode().toLong()) - 7046029254386353131L
    value = (value xor (value ushr 30)) * -4658895280553007687L
    value = (value xor (value ushr 27)) * -7723592293110705685L
    value = value xor (value ushr 31)
    return Math.floorMod(value.toInt(), AvatarGradients.size)
}

@Composable
internal fun CenteredTopBar(
    title: String,
    modifier: Modifier = Modifier,
    startContent: @Composable () -> Unit,
    endContent: @Composable () -> Unit
) {
    val connectionStatus = LocalConnectionStatusText.current
    Box(modifier.fillMaxWidth().height(64.dp).padding(horizontal = 12.dp)) {
        Box(Modifier.align(Alignment.CenterStart), contentAlignment = Alignment.Center) {
            startContent()
        }
        RollingStatusText(
            text = connectionStatus ?: title,
            animatedDots = connectionStatus != null,
            style = MaterialTheme.typography.headlineSmall,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .padding(horizontal = 56.dp)
                .clipToBounds()
                .align(Alignment.Center)
        )
        Box(Modifier.align(Alignment.CenterEnd), contentAlignment = Alignment.Center) {
            endContent()
        }
    }
}

@Immutable
internal data class RollingStatusValue(
    val text: String,
    val animatedDots: Boolean,
    val color: Color
)

@Composable
internal fun RollingStatusText(
    text: String,
    animatedDots: Boolean,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    val value = remember(text, animatedDots, color) {
        RollingStatusValue(text, animatedDots, color)
    }
    AnimatedContent(
        targetState = value,
        modifier = modifier,
        contentAlignment = Alignment.Center,
        contentKey = { it.text },
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(280, easing = FastOutSlowInEasing),
                initialOffsetY = { it }
            ) togetherWith slideOutVertically(
                animationSpec = tween(280, easing = FastOutSlowInEasing),
                targetOffsetY = { -it }
            )
        },
        label = "rolling_status"
    ) { current ->
        var dots by remember(current.text, current.animatedDots) { mutableIntStateOf(0) }
        LaunchedEffect(current.text, current.animatedDots) {
            dots = 0
            if (current.animatedDots) {
                while (true) {
                    delay(360)
                    dots = (dots + 1) % 4
                }
            }
        }
        val displayedText = remember(current.text, current.animatedDots, current.color, dots) {
            if (!current.animatedDots) {
                AnnotatedString(current.text)
            } else {
                buildAnnotatedString {
                    append(current.text)
                    append(".".repeat(dots))
                    withStyle(SpanStyle(color = current.color.copy(alpha = 0f))) {
                        append(".".repeat(3 - dots))
                    }
                }
            }
        }
        Text(
            text = displayedText,
            style = style,
            color = current.color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun RoundAction(icon: ImageVector, description: String, onClick: () -> Unit, tint: Color = Ink) {
    Box(
        Modifier.size(44.dp).clip(CircleShape).background(Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(23.dp)
        )
    }
}

@Composable
internal fun RedStatusBadge(text: String, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val height = 16.dp
    val heightPx = with(density) { height.toPx() }
    val horizontalPaddingPx = with(density) { 4.dp.toPx() }
    val textSizePx = with(density) { 10.sp.toPx() }
    val paint = remember(textSizePx) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = textSizePx
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }
    val widthPx = maxOf(heightPx, kotlin.math.ceil(paint.measureText(text) + horizontalPaddingPx * 2f).toFloat())
    val width = with(density) { widthPx.toDp() }
    ComposeCanvas(modifier.width(width).height(height)) {
        drawRoundRect(
            color = AppDangerColor,
            cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
        )
        val baseline = size.height / 2f - (paint.ascent() + paint.descent()) / 2f
        drawContext.canvas.nativeCanvas.drawText(text, size.width / 2f, baseline, paint)
    }
}

internal fun requiredImagePermissions(): Array<String> {
    return when {
        Build.VERSION.SDK_INT >= 34 -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        Build.VERSION.SDK_INT >= 33 -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}

internal fun requiredCallPermissions(video: Boolean): Array<String> {
    return if (video) arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
    else arrayOf(Manifest.permission.RECORD_AUDIO)
}

@Suppress("DEPRECATION")
internal fun createVoiceMessageRecorder(context: Context): MediaRecorder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()
}

internal fun hasCallPermissions(context: Context, video: Boolean): Boolean {
    val microphone = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    val camera = !video || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    return microphone && camera
}

internal fun hasInternalFileAccess(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}

@Suppress("DEPRECATION")
internal fun primarySharedStorageRoot(): File = Environment.getExternalStorageDirectory()

internal fun sameStoragePath(first: File, second: File): Boolean {
    return runCatching { first.canonicalPath == second.canonicalPath }
        .getOrElse { first.absolutePath == second.absolutePath }
}

internal fun isInsideStorageRoot(root: File, candidate: File): Boolean {
    return runCatching {
        val rootPath = root.canonicalPath.trimEnd(File.separatorChar)
        val candidatePath = candidate.canonicalPath
        candidatePath == rootPath || candidatePath.startsWith(rootPath + File.separator)
    }.getOrDefault(false)
}

internal suspend fun loadDeviceStorageEntries(folder: File): DeviceStorageListing = withContext(Dispatchers.IO) {
    runCatching {
        val children = folder.listFiles() ?: return@runCatching DeviceStorageListing(emptyList(), false)
        val entries = children.asSequence()
            .filter { !it.isHidden && (it.isDirectory || it.isFile) }
            .map {
                DeviceStorageEntry(
                    path = it.absolutePath,
                    name = it.name.ifBlank { it.absolutePath },
                    isDirectory = it.isDirectory,
                    size = if (it.isFile) it.length().coerceAtLeast(0L) else 0L
                )
            }
            .sortedWith(compareByDescending<DeviceStorageEntry> { it.isDirectory }.thenBy { it.name.lowercase(Locale.getDefault()) })
            .toList()
        DeviceStorageListing(entries, true)
    }.getOrDefault(DeviceStorageListing(emptyList(), false))
}

internal fun hasImagePermission(context: Context): Boolean {
    return when {
        Build.VERSION.SDK_INT >= 34 -> {
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED
        }
        Build.VERSION.SDK_INT >= 33 -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        else -> ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}

internal suspend fun loadDevicePhotos(context: Context): List<DevicePhoto> = withContext(Dispatchers.IO) {
    runCatching {
        val photos = mutableListOf<DevicePhoto>()
        val collection = if (Build.VERSION.SDK_INT >= 29) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        context.contentResolver.query(
            collection,
            arrayOf(MediaStore.Images.Media._ID),
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                photos += DevicePhoto(id, ContentUris.withAppendedId(collection, id))
            }
        }
        photos
    }.getOrDefault(emptyList())
}

internal suspend fun loadPhotoThumbnail(context: Context, photo: DevicePhoto): Bitmap? = withContext(Dispatchers.IO) {
    val cacheKey = "device_photo_${photo.id}"
    BitmapMemoryCache.get(cacheKey)?.let { return@withContext it }
    DeviceThumbnailSemaphore.withPermit {
        BitmapMemoryCache.get(cacheKey)?.let { return@withPermit it }
        runCatching {
            if (Build.VERSION.SDK_INT >= 29) {
                context.contentResolver.loadThumbnail(photo.uri, Size(240, 240), null)
            } else {
                MediaStore.Images.Thumbnails.getThumbnail(
                    context.contentResolver,
                    photo.id,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null
                )
            }
        }.getOrNull()?.also { BitmapMemoryCache.put(cacheKey, it) }
    }
}

internal suspend fun loadDeviceMedia(context: Context): List<DeviceMedia> = coroutineScope {
    val images = async(Dispatchers.IO) { queryDeviceMediaType(context, "image") }
    val videos = async(Dispatchers.IO) { queryDeviceMediaType(context, "video") }
    mergeDeviceMedia(images.await(), videos.await())
}

internal suspend fun loadDeviceAudio(context: Context): List<DeviceMedia> = withContext(Dispatchers.IO) {
    runCatching {
        val collection = if (Build.VERSION.SDK_INT >= 29) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.SIZE
        )
        val items = mutableListOf<DeviceMedia>()
        context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val displayNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val dateColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            val sizeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = if (displayNameColumn >= 0 && !cursor.isNull(displayNameColumn)) cursor.getString(displayNameColumn).orEmpty() else ""
                val title = if (titleColumn >= 0 && !cursor.isNull(titleColumn)) cursor.getString(titleColumn).orEmpty() else ""
                val artist = if (artistColumn >= 0 && !cursor.isNull(artistColumn)) cursor.getString(artistColumn).orEmpty() else ""
                val duration = if (durationColumn >= 0 && !cursor.isNull(durationColumn)) cursor.getLong(durationColumn) else 0L
                val dateAdded = if (dateColumn >= 0 && !cursor.isNull(dateColumn)) cursor.getLong(dateColumn) else 0L
                val size = if (sizeColumn >= 0 && !cursor.isNull(sizeColumn)) cursor.getLong(sizeColumn) else 0L
                items += DeviceMedia(
                    id = id,
                    uri = ContentUris.withAppendedId(collection, id),
                    kind = "audio",
                    duration = duration,
                    dateAdded = dateAdded,
                    width = 0,
                    height = 0,
                    name = title.ifBlank { displayName.substringBeforeLast('.').ifBlank { "Аудио" } },
                    artist = artist.takeUnless { it.isBlank() || it == "<unknown>" }.orEmpty(),
                    size = size
                )
            }
        }
        items
    }.getOrDefault(emptyList())
}

internal fun mergeDeviceMedia(images: List<DeviceMedia>, videos: List<DeviceMedia>): List<DeviceMedia> {
    val result = ArrayList<DeviceMedia>(images.size + videos.size)
    var imageIndex = 0
    var videoIndex = 0
    while (imageIndex < images.size && videoIndex < videos.size) {
        if (images[imageIndex].dateAdded >= videos[videoIndex].dateAdded) {
            result.add(images[imageIndex++])
        } else {
            result.add(videos[videoIndex++])
        }
    }
    while (imageIndex < images.size) result.add(images[imageIndex++])
    while (videoIndex < videos.size) result.add(videos[videoIndex++])
    return result
}

internal fun queryDeviceMediaType(context: Context, kind: String): List<DeviceMedia> {
    return runCatching {
        val collection = when {
            kind == "video" && Build.VERSION.SDK_INT >= 29 -> MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            kind == "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            Build.VERSION.SDK_INT >= 29 -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val projection = if (kind == "video") {
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
            )
        } else {
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
            )
        }
        val items = mutableListOf<DeviceMedia>()
        context.contentResolver.query(collection, projection, null, null, "${MediaStore.MediaColumns.DATE_ADDED} DESC")?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
            val durationColumn = if (kind == "video") cursor.getColumnIndex(MediaStore.Video.Media.DURATION) else -1
            val widthColumn = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)
            val heightColumn = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val duration = if (durationColumn >= 0 && !cursor.isNull(durationColumn)) cursor.getLong(durationColumn) else 0L
                val width = if (widthColumn >= 0 && !cursor.isNull(widthColumn)) cursor.getInt(widthColumn) else 0
                val height = if (heightColumn >= 0 && !cursor.isNull(heightColumn)) cursor.getInt(heightColumn) else 0
                items += DeviceMedia(
                    id,
                    ContentUris.withAppendedId(collection, id),
                    kind,
                    duration,
                    cursor.getLong(dateColumn),
                    width,
                    height
                )
            }
        }
        items
    }.getOrDefault(emptyList())
}

internal suspend fun loadDeviceMediaThumbnail(context: Context, media: DeviceMedia): Bitmap? = withContext(Dispatchers.IO) {
    val cacheKey = "device_${media.kind}_${media.id}"
    BitmapMemoryCache.get(cacheKey)?.let { return@withContext it }
    DeviceThumbnailSemaphore.withPermit {
        BitmapMemoryCache.get(cacheKey)?.let { return@withPermit it }
        runCatching {
            if (Build.VERSION.SDK_INT >= 29) {
                context.contentResolver.loadThumbnail(media.uri, Size(240, 240), null)
            } else if (media.kind == "video") {
                MediaStore.Video.Thumbnails.getThumbnail(context.contentResolver, media.id, MediaStore.Video.Thumbnails.MINI_KIND, null)
            } else {
                MediaStore.Images.Thumbnails.getThumbnail(context.contentResolver, media.id, MediaStore.Images.Thumbnails.MINI_KIND, null)
            }
        }.getOrNull()?.also { BitmapMemoryCache.put(cacheKey, it) }
    }
}

internal fun mediaDisplayName(context: Context, uri: Uri): String {
    if (uri.scheme.equals("file", ignoreCase = true)) {
        return uri.path?.let(::File)?.name.orEmpty()
    }
    return runCatching {
        context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0).orEmpty() else ""
        }.orEmpty()
    }.getOrDefault("")
}

internal fun mimeTypeFromFileName(fileName: String): String? {
    val extension = fileName.substringAfterLast('.', "").lowercase(Locale.US)
    return extension.takeIf { it.isNotBlank() }?.let { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }
}

internal fun resolveMediaDimensions(context: Context, uri: Uri, kind: String): Pair<Int, Int> {
    return runCatching {
        if (kind == "video") {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                var width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
                var height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
                val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)?.toIntOrNull() ?: 0
                if (rotation == 90 || rotation == 270) {
                    val originalWidth = width
                    width = height
                    height = originalWidth
                }
                width to height
            } finally {
                retriever.release()
            }
        } else {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
            options.outWidth.coerceAtLeast(0) to options.outHeight.coerceAtLeast(0)
        }
    }.getOrDefault(0 to 0)
}

internal fun resolveMediaDuration(context: Context, uri: Uri): Long {
    return runCatching {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
        } finally {
            retriever.release()
        }
    }.getOrDefault(0L)
}

internal fun formatDuration(milliseconds: Long): String {
    val totalSeconds = (milliseconds / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(Locale.US, minutes, seconds)
}

internal fun formatCallSeconds(totalSeconds: Long): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val hours = safeSeconds / 3600
    val minutes = safeSeconds % 3600 / 60
    val seconds = safeSeconds % 60
    return if (hours > 0) "%d:%02d:%02d".format(Locale.US, hours, minutes, seconds)
    else "%d:%02d".format(Locale.US, minutes, seconds)
}

internal fun formatFileSize(bytes: Long): String {
    val size = bytes.coerceAtLeast(0L)
    return when {
        size >= (1L shl 30) -> "%.1f ГБ".format(Locale.US, size.toDouble() / (1L shl 30))
        size >= (1L shl 20) -> "%.1f МБ".format(Locale.US, size.toDouble() / (1L shl 20))
        size >= (1L shl 10) -> "%.1f КБ".format(Locale.US, size.toDouble() / (1L shl 10))
        else -> "$size Б"
    }
}

internal val MediaLoadClient = OkHttpClient.Builder()
    .dispatcher(okhttp3.Dispatcher().apply {
        maxRequests = 8
        maxRequestsPerHost = 6
    })
    .connectTimeout(12, TimeUnit.SECONDS)
    .readTimeout(2, TimeUnit.MINUTES)
    .build()

internal const val AppUpdateRepositoryUrl = "https://github.com/artemAleksanrov/Ise-Messenger"
internal val AppUpdateReleaseApiUrl =
    "https://api.github.com/repos/${AppUpdateRepositoryUrl.substringAfter("github.com/")}/releases/latest"
internal const val AppUpdateMimeType = "application/vnd.android.package-archive"
internal val AppUpdateCheckIntervalMillis = TimeUnit.MINUTES.toMillis(30)
internal const val MaximumAppUpdateBytes = 250L * 1024L * 1024L
private val AppVersionNumberPattern = Regex("\\d+")

internal fun isVersionNewer(candidate: String, installed: String): Boolean {
    val candidateParts = AppVersionNumberPattern.findAll(candidate)
        .mapNotNull { it.value.toLongOrNull() }
        .toList()
    val installedParts = AppVersionNumberPattern.findAll(installed)
        .mapNotNull { it.value.toLongOrNull() }
        .toList()
    if (candidateParts.isEmpty() || installedParts.isEmpty()) return false
    val size = maxOf(candidateParts.size, installedParts.size)
    repeat(size) { index ->
        val candidatePart = candidateParts.getOrElse(index) { 0L }
        val installedPart = installedParts.getOrElse(index) { 0L }
        if (candidatePart != installedPart) return candidatePart > installedPart
    }
    return false
}

private fun trustedAppUpdateUrl(value: String): Boolean {
    val uri = Uri.parse(value)
    val host = uri.host.orEmpty().lowercase(Locale.US)
    return uri.scheme.equals("https", ignoreCase = true) &&
        (host == "github.com" || host.endsWith(".githubusercontent.com"))
}

internal fun fetchLatestAppUpdate(): AppUpdateInfo? {
    val request = Request.Builder()
        .url(AppUpdateReleaseApiUrl)
        .header("Accept", "application/vnd.github+json")
        .header("X-GitHub-Api-Version", "2022-11-28")
        .header("User-Agent", "Ise-Messenger-Android/${BuildConfig.VERSION_NAME}")
        .build()
    return MediaLoadClient.newCall(request).execute().use { response ->
        if (response.code == 404) return@use null
        if (!response.isSuccessful) throw IOException("Не удалось проверить обновление")
        val payload = response.body?.string().orEmpty()
        if (payload.isBlank()) throw IOException("GitHub вернул пустой ответ")
        val release = JSONObject(payload)
        val tag = release.optString("tag_name").trim()
        if (!isVersionNewer(tag, BuildConfig.VERSION_NAME)) return@use null
        val assets = release.optJSONArray("assets") ?: return@use null
        val candidates = buildList {
            for (index in 0 until assets.length()) {
                val asset = assets.optJSONObject(index) ?: continue
                val name = asset.optString("name").trim()
                val url = asset.optString("browser_download_url").trim()
                if (!name.endsWith(".apk", ignoreCase = true) || !trustedAppUpdateUrl(url)) continue
                val lowerName = name.lowercase(Locale.US)
                val score = (if ("universal" in lowerName) 4 else 0) +
                    (if ("release" in lowerName) 2 else 0) -
                    (if ("debug" in lowerName) 8 else 0)
                add(score to url)
            }
        }
        val downloadUrl = candidates.maxByOrNull { it.first }?.second ?: return@use null
        AppUpdateInfo(tag.removePrefix("v").removePrefix("V"), downloadUrl)
    }
}

internal fun downloadAppUpdate(context: Context, update: AppUpdateInfo): File {
    if (!trustedAppUpdateUrl(update.downloadUrl)) throw IOException("Недопустимая ссылка обновления")
    val directory = File(context.cacheDir, "updates").apply { mkdirs() }
    val safeVersion = update.versionName.replace(Regex("[^0-9A-Za-z._-]"), "_").take(48)
    val target = File(directory, "ise-messenger-${safeVersion.ifBlank { "update" }}.apk")
    val partial = File(directory, "${target.name}.part")
    partial.delete()
    val request = Request.Builder()
        .url(update.downloadUrl)
        .header("Accept", AppUpdateMimeType)
        .header("User-Agent", "Ise-Messenger-Android/${BuildConfig.VERSION_NAME}")
        .build()
    try {
        MediaLoadClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Не удалось загрузить обновление")
            val body = response.body ?: throw IOException("GitHub вернул пустой файл")
            val declaredSize = body.contentLength()
            if (declaredSize > MaximumAppUpdateBytes) throw IOException("Файл обновления слишком большой")
            body.byteStream().use { input ->
                partial.outputStream().use { output ->
                    val buffer = ByteArray(64 * 1024)
                    var downloaded = 0L
                    while (true) {
                        val count = input.read(buffer)
                        if (count < 0) break
                        downloaded += count
                        if (downloaded > MaximumAppUpdateBytes) throw IOException("Файл обновления слишком большой")
                        output.write(buffer, 0, count)
                    }
                }
            }
        }
        val signature = partial.inputStream().use { input -> ByteArray(2).also { input.read(it) } }
        if (signature[0] != 'P'.code.toByte() || signature[1] != 'K'.code.toByte()) {
            throw IOException("Загружен некорректный APK")
        }
        target.delete()
        if (!partial.renameTo(target)) {
            partial.copyTo(target, overwrite = true)
            partial.delete()
        }
        directory.listFiles()?.filter { it.isFile && it != target }?.forEach(File::delete)
        return target
    } catch (error: Throwable) {
        partial.delete()
        throw error
    }
}
internal val DeviceThumbnailSemaphore = Semaphore(3)
internal val BitmapDecodeSemaphore = Semaphore(2)
internal val RemoteVideoThumbnailSemaphore = Semaphore(2)
internal val PersistentMediaLocks = Array(32) { Any() }

internal fun persistentMediaLock(file: File): Any {
    return PersistentMediaLocks[Math.floorMod(file.absolutePath.hashCode(), PersistentMediaLocks.size)]
}

internal fun remoteMediaLock(path: String): Any {
    return PersistentMediaLocks[Math.floorMod(path.hashCode(), PersistentMediaLocks.size)]
}

internal fun absoluteMediaUrl(path: String): String {
    return if (path.startsWith("http://") || path.startsWith("https://")) path else ServerUrl + path
}

internal suspend fun loadRemoteMediaThumbnail(
    context: Context,
    message: MessageItem,
    token: String,
    onProgress: (Float) -> Unit = {}
): Bitmap? = loadRemoteMediaThumbnail(
    context = context,
    path = message.mediaUrl,
    kind = message.kind,
    token = token,
    expectedSize = message.mediaSize,
    onProgress = onProgress
)

internal suspend fun loadRemoteMediaThumbnail(
    context: Context,
    path: String,
    kind: String,
    token: String,
    expectedSize: Long = 0L,
    maxSize: Int = 700,
    onProgress: (Float) -> Unit = {}
): Bitmap? = withContext(Dispatchers.IO) {
    if (kind == "image") return@withContext loadRemoteBitmap(context, path, token, maxSize, expectedSize, onProgress)
    val cacheKey = if (maxSize == 700) "remote_video_$path" else "remote_video_${maxSize}_$path"
    BitmapMemoryCache.get(cacheKey)?.let {
        onProgress(1f)
        return@withContext it
    }
    RemoteVideoThumbnailSemaphore.withPermit {
        synchronized(remoteMediaLock(path)) {
            BitmapMemoryCache.get(cacheKey) ?: runCatching {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(absoluteMediaUrl(path), mapOf("Authorization" to "Bearer $token"))
                    val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                        ?.let { scaleBitmapDown(it, maxSize) }
                    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                    if (duration > 0L) VideoDurationCache.put(path, duration)
                    frame
                } finally {
                    retriever.release()
                }
            }.getOrNull()?.also { BitmapMemoryCache.put(cacheKey, it) }
        }
    }
}

internal suspend fun loadRemoteVideoDuration(context: Context, path: String, token: String): Long = withContext(Dispatchers.IO) {
    VideoDurationCache.get(path)?.let { return@withContext it }
    RemoteVideoThumbnailSemaphore.withPermit {
        synchronized(remoteMediaLock(path)) {
            VideoDurationCache.get(path) ?: runCatching {
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(absoluteMediaUrl(path), mapOf("Authorization" to "Bearer $token"))
                    (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L).also {
                        if (it > 0L) VideoDurationCache.put(path, it)
                    }
                } finally {
                    retriever.release()
                }
            }.getOrDefault(0L)
        }
    }
}

internal suspend fun loadRemoteBitmap(
    context: Context,
    path: String,
    token: String,
    maxSize: Int,
    expectedSize: Long = 0L,
    onProgress: (Float) -> Unit = {}
): Bitmap? = withContext(Dispatchers.IO) {
    val cacheKey = "remote_image_${maxSize}_$path"
    BitmapMemoryCache.get(cacheKey)?.let {
        onProgress(1f)
        return@withContext it
    }
    runCatching {
        val file = ensurePersistentMediaFile(context, path, token, "image", expectedSize, onProgress = onProgress) ?: return@runCatching null
        BitmapMemoryCache.get(cacheKey) ?: BitmapDecodeSemaphore.withPermit {
            BitmapMemoryCache.get(cacheKey) ?: decodePersistentBitmap(file, maxSize)
        }
    }.getOrNull()?.also { BitmapMemoryCache.put(cacheKey, it) }
}

internal suspend fun ensurePersistentMediaFile(
    context: Context,
    path: String,
    token: String,
    kind: String,
    expectedSize: Long = 0L,
    preferredName: String = "",
    onProgress: (Float) -> Unit = {}
): File? = withContext(Dispatchers.IO) {
    if (path.isBlank()) return@withContext null
    onProgress(0f)
    val target = persistentMediaFile(context, path, kind, preferredName)
    if (target.isFile && target.length() > 0 && (expectedSize < 1L || target.length() == expectedSize)) {
        target.setLastModified(System.currentTimeMillis())
        onProgress(1f)
        return@withContext target
    }
    synchronized(persistentMediaLock(target)) {
        if (target.isFile && target.length() > 0 && (expectedSize < 1L || target.length() == expectedSize)) {
            target.setLastModified(System.currentTimeMillis())
            onProgress(1f)
            return@synchronized target
        }
        target.parentFile?.mkdirs()
        val partial = File(target.parentFile, target.name + ".part")
        if (partial.exists()) partial.delete()
        runCatching {
            val request = Request.Builder().url(absoluteMediaUrl(path)).header("Authorization", "Bearer $token").build()
            MediaLoadClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@use null
                val body = response.body ?: return@use null
                val total = body.contentLength().takeIf { it > 0L } ?: expectedSize.takeIf { it > 0L } ?: 0L
                partial.outputStream().use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(32 * 1024)
                        var downloaded = 0L
                        var lastPercent = -5
                        while (true) {
                            val count = input.read(buffer)
                            if (count < 0) break
                            output.write(buffer, 0, count)
                            downloaded += count
                            if (total > 0L) {
                                val percent = ((downloaded * 100L) / total).toInt().coerceIn(0, 100)
                                if (percent == 100 || percent - lastPercent >= 5) {
                                    lastPercent = percent
                                    onProgress(percent / 100f)
                                }
                            }
                        }
                    }
                }
            } ?: return@runCatching null
            if (!partial.isFile || partial.length() == 0L || (expectedSize > 0L && partial.length() != expectedSize)) {
                return@runCatching null
            }
            if (target.exists() && !target.delete()) return@runCatching null
            if (!partial.renameTo(target)) {
                partial.copyTo(target, overwrite = true)
                partial.delete()
            }
            onProgress(1f)
            target
        }.getOrNull().also { if (it == null) partial.delete() }
    }
}

internal suspend fun loadAudioWaveform(file: File, cacheKey: String, bucketCount: Int = 72): List<Float> =
    withContext(Dispatchers.IO) {
        AudioWaveformCache.get(cacheKey)?.let { return@withContext it }
        synchronized(remoteMediaLock("waveform_$cacheKey")) {
            AudioWaveformCache.get(cacheKey) ?: decodeAudioWaveform(file, bucketCount).also { waveform ->
                if (waveform.isNotEmpty()) AudioWaveformCache.put(cacheKey, waveform)
            }
        }
    }

internal fun decodeAudioWaveform(file: File, bucketCount: Int): List<Float> {
    if (!file.isFile || file.length() < 1L || bucketCount < 1) return emptyList()
    val extractor = MediaExtractor()
    var decoder: MediaCodec? = null
    return try {
        extractor.setDataSource(file.absolutePath)
        val trackIndex = (0 until extractor.trackCount).firstOrNull { index ->
            extractor.getTrackFormat(index).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
        } ?: return emptyList()
        val inputFormat = extractor.getTrackFormat(trackIndex)
        val mime = inputFormat.getString(MediaFormat.KEY_MIME) ?: return emptyList()
        val formatDurationUs = runCatching {
            inputFormat.takeIf { it.containsKey(MediaFormat.KEY_DURATION) }
                ?.getLong(MediaFormat.KEY_DURATION)
        }.getOrNull()?.takeIf { it > 0L }
        val durationUs = formatDurationUs ?: runCatching {
            MediaMetadataRetriever().let { retriever ->
                try {
                    retriever.setDataSource(file.absolutePath)
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull()?.times(1_000L)
                } finally {
                    retriever.release()
                }
            }
        }.getOrNull()?.takeIf { it > 0L } ?: 1L
        extractor.selectTrack(trackIndex)
        val activeDecoder = MediaCodec.createDecoderByType(mime).apply {
            configure(inputFormat, null, null, 0)
            start()
        }
        decoder = activeDecoder
        val peaks = FloatArray(bucketCount)
        val bufferInfo = MediaCodec.BufferInfo()
        var outputFormat = inputFormat
        var inputEnded = false
        var outputEnded = false
        val deadline = SystemClock.elapsedRealtime() + 45_000L
        while (!outputEnded && SystemClock.elapsedRealtime() < deadline) {
            if (!inputEnded) {
                val inputIndex = activeDecoder.dequeueInputBuffer(10_000L)
                if (inputIndex >= 0) {
                    val inputBuffer = activeDecoder.getInputBuffer(inputIndex)
                    val sampleSize = if (inputBuffer == null) -1 else {
                        inputBuffer.clear()
                        extractor.readSampleData(inputBuffer, 0)
                    }
                    if (sampleSize < 0) {
                        activeDecoder.queueInputBuffer(inputIndex, 0, 0, 0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        inputEnded = true
                    } else {
                        activeDecoder.queueInputBuffer(
                            inputIndex,
                            0,
                            sampleSize,
                            extractor.sampleTime.coerceAtLeast(0L),
                            extractor.sampleFlags
                        )
                        extractor.advance()
                    }
                }
            }
            when (val outputIndex = activeDecoder.dequeueOutputBuffer(bufferInfo, 10_000L)) {
                MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> outputFormat = activeDecoder.outputFormat
                MediaCodec.INFO_TRY_AGAIN_LATER -> Unit
                else -> if (outputIndex >= 0) {
                    activeDecoder.getOutputBuffer(outputIndex)?.let { source ->
                        if (bufferInfo.size > 0) {
                            val end = (bufferInfo.offset + bufferInfo.size).coerceAtMost(source.capacity())
                            if (bufferInfo.offset in 0 until end) {
                                val pcm = source.duplicate().apply {
                                    position(bufferInfo.offset)
                                    limit(end)
                                }.slice().order(ByteOrder.nativeOrder())
                                val channels = outputFormat.takeIf { it.containsKey(MediaFormat.KEY_CHANNEL_COUNT) }
                                    ?.getInteger(MediaFormat.KEY_CHANNEL_COUNT)?.coerceAtLeast(1) ?: 1
                                val sampleRate = outputFormat.takeIf { it.containsKey(MediaFormat.KEY_SAMPLE_RATE) }
                                    ?.getInteger(MediaFormat.KEY_SAMPLE_RATE)?.coerceAtLeast(1) ?: 44_100
                                val encoding = outputFormat.takeIf { it.containsKey(MediaFormat.KEY_PCM_ENCODING) }
                                    ?.getInteger(MediaFormat.KEY_PCM_ENCODING) ?: AudioFormat.ENCODING_PCM_16BIT
                                val bytesPerSample = when (encoding) {
                                    AudioFormat.ENCODING_PCM_FLOAT -> 4
                                    AudioFormat.ENCODING_PCM_8BIT -> 1
                                    else -> 2
                                }
                                val frameSize = bytesPerSample * channels
                                val frameCount = pcm.remaining() / frameSize
                                repeat(frameCount) { frameIndex ->
                                    var peak = 0f
                                    repeat(channels) {
                                        val sample = when (encoding) {
                                            AudioFormat.ENCODING_PCM_FLOAT -> kotlin.math.abs(pcm.float).coerceAtMost(1f)
                                            AudioFormat.ENCODING_PCM_8BIT -> kotlin.math.abs((pcm.get().toInt() and 0xff) - 128) / 128f
                                            else -> kotlin.math.abs(pcm.short.toInt()) / 32768f
                                        }
                                        if (sample > peak) peak = sample
                                    }
                                    val sampleTimeUs = bufferInfo.presentationTimeUs.coerceAtLeast(0L) +
                                            frameIndex * 1_000_000L / sampleRate
                                    val bucket = (sampleTimeUs * bucketCount / durationUs)
                                        .toInt().coerceIn(0, bucketCount - 1)
                                    if (peak > peaks[bucket]) peaks[bucket] = peak
                                }
                            }
                        }
                    }
                    outputEnded = bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0
                    activeDecoder.releaseOutputBuffer(outputIndex, false)
                }
            }
        }
        normalizeAudioWaveform(peaks)
    } catch (_: Exception) {
        emptyList()
    } finally {
        runCatching { decoder?.stop() }
        runCatching { decoder?.release() }
        extractor.release()
    }
}

internal fun normalizeAudioWaveform(peaks: FloatArray): List<Float> {
    val maximum = peaks.maxOrNull()?.takeIf { it > 0.0001f } ?: return emptyList()
    return peaks.mapIndexed { index, peak ->
        val left = peaks.getOrElse(index - 1) { peak }
        val right = peaks.getOrElse(index + 1) { peak }
        val smoothed = (left + peak * 2f + right) / 4f
        val normalized = (smoothed / maximum).coerceIn(0f, 1f)
        (0.18f + kotlin.math.sqrt(normalized) * 0.82f).coerceIn(0.18f, 1f)
    }
}

internal fun storeLocalMedia(context: Context, uri: Uri, path: String, kind: String, preferredName: String = ""): File? {
    if (path.isBlank()) return null
    val target = persistentMediaFile(context, path, kind, preferredName)
    if (target.isFile && target.length() > 0) return target
    return synchronized(persistentMediaLock(target)) {
        if (target.isFile && target.length() > 0) return@synchronized target
        runCatching {
            target.parentFile?.mkdirs()
            val partial = File(target.parentFile, target.name + ".part")
            if (partial.exists()) partial.delete()
            context.contentResolver.openInputStream(uri)?.use { input ->
                partial.outputStream().use { output -> input.copyTo(output) }
            } ?: return@runCatching null
            if (!partial.renameTo(target)) {
                partial.copyTo(target, overwrite = true)
                partial.delete()
            }
            target
        }.getOrNull()
    }
}

internal suspend fun openMessageFile(context: Context, message: MessageItem, token: String): Boolean {
    val file = ensurePersistentMediaFile(context, message.mediaUrl, token, "file", message.mediaSize, message.mediaName) ?: return false
    return withContext(Dispatchers.Main) {
        runCatching {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.files", file)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, message.mediaMime.ifBlank { "application/octet-stream" })
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Открыть файл").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        }.getOrDefault(false)
    }
}

internal suspend fun saveMessageAttachment(context: Context, message: MessageItem, token: String): Boolean {
    return if (message.kind == "file" || message.kind == "audio") saveMessageFileToDownloads(context, message, token)
    else saveMessageMediaToGallery(context, message, token)
}

internal suspend fun saveMessageFileToDownloads(context: Context, message: MessageItem, token: String): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val file = ensurePersistentMediaFile(context, message.mediaUrl, token, message.kind, message.mediaSize, message.mediaName) ?: return@runCatching false
        val fallbackExtension = if (message.kind == "audio") "mp3" else "bin"
        val cleanName = message.mediaName.trim().replace('\\', '/').substringAfterLast('/').take(180)
            .ifBlank { "Ise_${message.id}.$fallbackExtension" }
        if (Build.VERSION.SDK_INT >= 29) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, cleanName)
                put(MediaStore.MediaColumns.MIME_TYPE, message.mediaMime.ifBlank { "application/octet-stream" })
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/Ise Messenger")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return@runCatching false
            try {
                context.contentResolver.openOutputStream(uri)?.use { output -> file.inputStream().use { it.copyTo(output) } }
                    ?: return@runCatching false
                context.contentResolver.update(uri, ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }, null, null)
                true
            } catch (error: Exception) {
                context.contentResolver.delete(uri, null, null)
                throw error
            }
        } else {
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Ise Messenger").apply { mkdirs() }
            val target = File(directory, cleanName)
            file.copyTo(if (target.exists()) File(directory, "${message.id}_$cleanName") else target, overwrite = true)
            true
        }
    }.getOrDefault(false)
}

internal suspend fun saveMessageMediaToGallery(context: Context, message: MessageItem, token: String): Boolean = withContext(Dispatchers.IO) {
    runCatching {
        val file = ensurePersistentMediaFile(context, message.mediaUrl, token, message.kind, message.mediaSize) ?: return@runCatching false
        val video = message.kind == "video"
        val mime = message.mediaMime.ifBlank { if (video) "video/mp4" else "image/jpeg" }
        val extension = message.mediaName.substringAfterLast('.', "").takeIf { it.length in 2..5 }
            ?: if (video) "mp4" else "jpg"
        val displayName = message.mediaName.ifBlank { "Ise_${message.id}.$extension" }
        val collection = if (video) MediaStore.Video.Media.EXTERNAL_CONTENT_URI else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mime)
            if (Build.VERSION.SDK_INT >= 29) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    if (video) "${Environment.DIRECTORY_MOVIES}/Ise Messenger" else "${Environment.DIRECTORY_PICTURES}/Ise Messenger"
                )
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }
        val uri = context.contentResolver.insert(collection, values) ?: return@runCatching false
        try {
            val output = context.contentResolver.openOutputStream(uri)
            if (output == null) {
                context.contentResolver.delete(uri, null, null)
                return@runCatching false
            }
            output.use { stream -> file.inputStream().use { it.copyTo(stream) } }
            if (Build.VERSION.SDK_INT >= 29) {
                context.contentResolver.update(uri, ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }, null, null)
            }
            true
        } catch (error: Exception) {
            context.contentResolver.delete(uri, null, null)
            throw error
        }
    }.getOrDefault(false)
}

internal fun persistentMediaFile(context: Context, path: String, kind: String, preferredName: String = ""): File {
    val extensionSource = if ((kind == "audio" || kind == "file") && preferredName.isNotBlank()) preferredName else path.substringBefore('?')
    val extension = extensionSource.substringAfterLast('.', "").lowercase(Locale.ROOT)
        .takeIf { it.length in 2..5 && it.all(Char::isLetterOrDigit) }
        ?: when (kind) {
            "video" -> "mp4"
            "image" -> "jpg"
            "audio" -> "mp3"
            else -> "bin"
        }
    return File(File(context.filesDir, "media").apply { mkdirs() }, "${stableFileKey(path)}.$extension")
}

internal fun avatarBitmapCacheKey(avatar: String): String = "avatar_${stableFileKey(avatar)}"

internal suspend fun loadPersistentAvatar(context: Context, avatar: String): Bitmap? = withContext(Dispatchers.IO) {
    val cacheKey = avatarBitmapCacheKey(avatar)
    BitmapMemoryCache.get(cacheKey)?.let { return@withContext it }
    runCatching {
        val directory = File(context.filesDir, "media").apply { mkdirs() }
        val file = File(directory, "$cacheKey.jpg")
        val legacyFile = File(File(context.filesDir, "avatars"), "${cacheKey.removePrefix("avatar_")}.jpg")
        if (!file.exists() && legacyFile.isFile) runCatching { legacyFile.copyTo(file, overwrite = false) }
        BitmapDecodeSemaphore.withPermit {
            synchronized(persistentMediaLock(file)) {
                BitmapMemoryCache.get(cacheKey)?.let { return@synchronized it }
                if (!file.isFile || file.length() == 0L) {
                    val bytes = Base64.decode(avatar, Base64.DEFAULT)
                    val partial = File(directory, file.name + ".part")
                    partial.outputStream().use { it.write(bytes) }
                    if (!partial.renameTo(file)) {
                        partial.copyTo(file, overwrite = true)
                        partial.delete()
                    }
                }
                decodePersistentBitmap(file, 512)?.also { BitmapMemoryCache.put(cacheKey, it) }
            }
        }
    }.getOrNull()
}

internal fun scaleBitmapDown(bitmap: Bitmap, maximumSide: Int): Bitmap {
    val largestSide = maxOf(bitmap.width, bitmap.height)
    if (largestSide <= maximumSide || largestSide < 1) return bitmap
    val scale = maximumSide.toFloat() / largestSide
    val scaled = Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width * scale).roundToInt().coerceAtLeast(1),
        (bitmap.height * scale).roundToInt().coerceAtLeast(1),
        true
    )
    if (scaled !== bitmap) bitmap.recycle()
    return scaled
}

internal fun stableFileKey(value: String): String {
    StableFileKeyCache[value]?.let { return it }
    val digest = StableFileDigest.get()!!.apply { reset() }
    val bytes = digest.digest(value.toByteArray(Charsets.UTF_8))
    val hex = "0123456789abcdef"
    return CharArray(bytes.size * 2).also { result ->
        bytes.forEachIndexed { index, byte ->
            val valueByte = byte.toInt() and 0xff
            result[index * 2] = hex[valueByte ushr 4]
            result[index * 2 + 1] = hex[valueByte and 0x0f]
        }
    }.concatToString().also { StableFileKeyCache[value] = it }
}

internal suspend fun loadLocalPreviewBitmap(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    val cacheKey = "local_preview_$uri"
    BitmapMemoryCache.get(cacheKey)?.let { return@withContext it }
    BitmapDecodeSemaphore.withPermit {
        runCatching {
            if (Build.VERSION.SDK_INT >= 28) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri)) { decoder, info, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    val largest = maxOf(info.size.width, info.size.height)
                    if (largest > 1800) {
                        val scale = 1800f / largest
                        decoder.setTargetSize((info.size.width * scale).toInt().coerceAtLeast(1), (info.size.height * scale).toInt().coerceAtLeast(1))
                    }
                }
            } else {
                decodeSampledBitmap(context, uri, 1800)
            }
        }.getOrNull()?.also { BitmapMemoryCache.put(cacheKey, it) }
    }
}

internal fun decodeSampledBitmap(bytes: ByteArray, maxSize: Int): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
    if (bounds.outWidth < 1 || bounds.outHeight < 1) return null
    var sample = 1
    while (maxOf(bounds.outWidth, bounds.outHeight) / sample > maxSize) sample *= 2
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options().apply { inSampleSize = sample })
}

internal fun decodeSampledBitmap(context: Context, uri: Uri, maxSize: Int): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    if (bounds.outWidth < 1 || bounds.outHeight < 1) return null
    var sample = 1
    while (maxOf(bounds.outWidth, bounds.outHeight) / sample > maxSize) sample *= 2
    return context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
    }
}

internal fun decodeSampledBitmap(file: File, maxSize: Int): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, bounds)
    if (bounds.outWidth < 1 || bounds.outHeight < 1) return null
    var sample = 1
    while (maxOf(bounds.outWidth, bounds.outHeight) / sample > maxSize) sample *= 2
    return BitmapFactory.decodeFile(file.absolutePath, BitmapFactory.Options().apply { inSampleSize = sample })
}

internal fun decodePersistentBitmap(file: File, maxSize: Int): Bitmap? {
    if (Build.VERSION.SDK_INT < 28) return decodeSampledBitmap(file, maxSize)
    return ImageDecoder.decodeBitmap(ImageDecoder.createSource(file)) { decoder, info, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        val largest = maxOf(info.size.width, info.size.height)
        if (largest > maxSize) {
            val scale = maxSize.toFloat() / largest
            decoder.setTargetSize(
                (info.size.width * scale).toInt().coerceAtLeast(1),
                (info.size.height * scale).toInt().coerceAtLeast(1)
            )
        }
    }
}

internal fun encodeAvatar(context: Context, uri: Uri): String {
    val resolver = context.contentResolver
    val source = if (Build.VERSION.SDK_INT >= 28) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri)) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            val width = info.size.width
            val height = info.size.height
            val largest = maxOf(width, height)
            if (largest > 1280) {
                val scale = 1280f / largest
                decoder.setTargetSize((width * scale).toInt().coerceAtLeast(1), (height * scale).toInt().coerceAtLeast(1))
            }
        }
    } else {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth < 1 || bounds.outHeight < 1) throw Exception("Не удалось открыть фотографию")
        var sample = 1
        while (maxOf(bounds.outWidth, bounds.outHeight) / sample > 1280) sample *= 2
        resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
        } ?: throw Exception("Не удалось открыть фотографию")
    }
    val side = minOf(source.width, source.height)
    val cropped = Bitmap.createBitmap(source, (source.width - side) / 2, (source.height - side) / 2, side, side)
    val scaled = Bitmap.createScaledBitmap(cropped, 512, 512, true)
    val output = ByteArrayOutputStream()
    return try {
        if (!scaled.compress(Bitmap.CompressFormat.JPEG, 84, output)) throw Exception("Не удалось обработать фотографию")
        Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
    } finally {
        if (scaled !== cropped) scaled.recycle()
        if (cropped !== source) cropped.recycle()
        source.recycle()
    }
}

internal val ServerSecondDateParser = ThreadLocal.withInitial {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
}
internal val ServerMillisDateParser = ThreadLocal.withInitial {
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }
}
internal val MessageDayFormatter = ThreadLocal.withInitial { SimpleDateFormat("yyyyMMdd", Locale.US) }
internal val MessageTimeFormatter = ThreadLocal.withInitial { SimpleDateFormat("HH:mm", Locale.US) }
internal val MessageDateFormatter = ThreadLocal.withInitial { SimpleDateFormat("d MMMM", Locale.forLanguageTag("ru")) }
internal val MessageDateYearFormatter = ThreadLocal.withInitial { SimpleDateFormat("d MMMM yyyy", Locale.forLanguageTag("ru")) }
internal val ParsedServerDateCache = LruCache<String, Date>(1024)
internal val MessageDayKeyCache = LruCache<String, String>(1024)
internal val MessageTimeCache = LruCache<String, String>(1024)

internal fun formatLocalDate(formatter: ThreadLocal<SimpleDateFormat>, date: Date): String {
    val value = formatter.get()!!
    value.timeZone = TimeZone.getDefault()
    return value.format(date)
}

internal fun parseServerDate(value: String): Date? {
    if (value.isBlank()) return null
    ParsedServerDateCache.get(value)?.let { return it }
    return runCatching {
        val dot = value.indexOf('.')
        val normalized = if (dot >= 0) {
            val zoneIndex = value.indexOfAny(charArrayOf('Z', '+', '-'), dot)
            val zone = if (zoneIndex >= 0) value.substring(zoneIndex) else "Z"
            val fraction = value.substring(dot + 1, if (zoneIndex >= 0) zoneIndex else value.length).padEnd(3, '0').take(3)
            value.substring(0, dot) + "." + fraction + zone
        } else value
        if (normalized.contains('.')) ServerMillisDateParser.get()!!.parse(normalized) else ServerSecondDateParser.get()!!.parse(normalized)
    }.getOrNull()?.also { ParsedServerDateCache.put(value, it) }
}

internal fun messageDayKey(value: String): String {
    MessageDayKeyCache.get(value)?.let { return it }
    val date = parseServerDate(value) ?: return value.substringBefore('T')
    return formatLocalDate(MessageDayFormatter, date).also { MessageDayKeyCache.put(value, it) }
}

internal fun formatMessageDate(value: String): String {
    val date = parseServerDate(value) ?: return value.substringBefore('T')
    val current = Calendar.getInstance()
    val message = Calendar.getInstance().apply { time = date }
    if (sameCalendarDay(message, current)) return "Сегодня"
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    if (sameCalendarDay(message, yesterday)) return "Вчера"
    return if (message.get(Calendar.YEAR) == current.get(Calendar.YEAR)) {
        formatLocalDate(MessageDateFormatter, date)
    } else {
        formatLocalDate(MessageDateYearFormatter, date)
    }
}

internal fun formatMediaSentAt(value: String): String {
    val date = formatMessageDate(value)
    val time = formatTime(value)
    return when {
        date.isNotBlank() && time.isNotBlank() -> "$date в $time"
        date.isNotBlank() -> date
        else -> time
    }
}

internal fun sameCalendarDay(first: Calendar, second: Calendar): Boolean {
    return first.get(Calendar.ERA) == second.get(Calendar.ERA) &&
            first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
            first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}

internal fun formatPresence(online: Boolean, value: String, now: Long): String {
    if (online) return "в сети"
    val lastSeen = parseServerDate(value)?.time ?: return "в сети недавно"
    val seconds = ((now - lastSeen) / 1000).coerceAtLeast(0)
    if (seconds < 60) return "в сети только что"
    val minutes = seconds / 60
    if (minutes < 60) return "в сети $minutes мин назад"
    val hours = minutes / 60
    if (hours < 24) return "в сети $hours ${russianPlural(hours, "час", "часа", "часов")} назад"
    val days = hours / 24
    return "в сети $days ${russianPlural(days, "день", "дня", "дней")} назад"
}

internal fun russianPlural(value: Long, one: String, few: String, many: String): String {
    val lastHundred = value % 100
    val lastTen = value % 10
    return when {
        lastHundred in 11L..14L -> many
        lastTen == 1L -> one
        lastTen in 2L..4L -> few
        else -> many
    }
}

internal fun formatTime(value: String): String {
    MessageTimeCache.get(value)?.let { return it }
    val date = parseServerDate(value) ?: return ""
    return formatLocalDate(MessageTimeFormatter, date).also { MessageTimeCache.put(value, it) }
}
