package com.example.isemessenger.feature.chat

import com.example.isemessenger.*
import com.example.isemessenger.core.config.*

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
import android.graphics.Shader
import android.graphics.SurfaceTexture
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.widget.VideoView
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
import android.view.Gravity
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.stopScroll
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
import androidx.compose.foundation.layout.requiredHeight
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
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
import androidx.core.graphics.drawable.IconCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.roundToInt

private data class ChatViewportSnapshot(
    val nearOldest: Boolean,
    val newestVisibleMessageId: Long,
    val atBottom: Boolean
)

private sealed interface ChatTimelineItem {
    val key: String
    val contentType: String
}

private data class ChatTimelineMessage(val entry: ChatMessageEntry) : ChatTimelineItem {
    override val key: String = entry.key
    override val contentType: String = "${entry.primary.kind}_${entry.primary.mine}_${entry.messages.size > 1}"
}

private data class ChatTimelineDate(val day: String, val sourceDate: String) : ChatTimelineItem {
    override val key: String = "date_$day"
    override val contentType: String = "date"
}

@Composable
internal fun ChatScreenRoute(controller: MessengerController, errorState: SnackbarHostState) {
    val state = controller.state
    val chat = state.currentChat ?: return
    ChatScreenContent(
        chat, state.chats, state.groupMembers, state.messages, state.drafts[chat.id].orEmpty(),
        state.replyTo, state.editingMessage, state.editingText, state.reopenMediaSheet,
        state.typingChatId == chat.id, state.typingUserId, state.typingVoiceRecording, state.typingActivity,
        state.loading, state.messagesHasMore, state.loadingOlderMessages, state.mediaUploadProgress,
        state.selectedLocalMedia, state.token, controller::sendMessage, controller::sendMedia,
        controller::sendFile, controller::sendVoice, controller::cancelMediaUpload,
        controller::dismissMediaSheet, controller::clearLocalMediaSelection,
        controller::toggleLocalMediaSelection, controller::selectReply,
        controller::cancelReply, controller::startEditing, controller::cancelEditing,
        controller::editMessage, { value -> controller.updateDraft(chat.id, value) },
        controller::deleteMessage, controller::loadOlderMessages,
        controller::markMessageRead,
        controller::forwardMessage, controller::openLocalMediaPreview, controller::openRemoteMediaPreview,
        controller::openChatProfile, controller::showPermissionError,
        controller::showCallPermissionError, errorState, controller::typingChanged,
        controller::voiceRecordingChanged, controller::startCall, controller::back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatScreenContent(
    chat: ChatItem,
    chats: List<ChatItem>,
    groupMembers: List<GroupMember>,
    messages: List<MessageItem>,
    draft: String,
    replyTo: MessageItem?,
    editingMessage: MessageItem?,
    editingText: String,
    reopenMediaSheet: Boolean,
    typing: Boolean,
    typingUserId: Long?,
    voiceTyping: Boolean,
    typingActivity: String,
    loading: Boolean,
    messagesHasMore: Boolean,
    loadingOlderMessages: Boolean,
    uploadProgress: Float?,
    selectedMedia: List<DeviceMedia>,
    token: String,
    send: (String) -> Unit,
    sendMedia: (List<DeviceMedia>, () -> Unit) -> Unit,
    sendFile: (Uri) -> Unit,
    sendVoice: (Uri, Long, () -> Unit) -> Unit,
    cancelUpload: () -> Unit,
    dismissMediaSheet: () -> Unit,
    clearMediaSelection: () -> Unit,
    toggleMediaSelection: (DeviceMedia) -> Unit,
    selectReply: (MessageItem) -> Unit,
    cancelReply: () -> Unit,
    startEditing: (MessageItem) -> Unit,
    cancelEditing: () -> Unit,
    editMessage: (String) -> Unit,
    updateDraft: (String) -> Unit,
    deleteMessage: (MessageItem) -> Unit,
    loadOlderMessages: () -> Unit,
    markMessageRead: (Long, Long) -> Unit,
    forwardMessage: (MessageItem, ChatItem) -> Unit,
    openLocalMedia: (DeviceMedia, List<DeviceMedia>) -> Unit,
    openRemoteMedia: (MessageItem) -> Unit,
    openProfile: () -> Unit,
    permissionError: () -> Unit,
    callPermissionError: () -> Unit,
    errorState: SnackbarHostState,
    typingChanged: (Boolean) -> Unit,
    voiceRecordingChanged: (Boolean) -> Unit,
    startCall: (Boolean) -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val notificationsEnabled = chatNotificationsEnabledState(context, chat.id)
    val connectionStatusText = LocalConnectionStatusText.current
    val presenceStatusText = rememberPresenceStatusText(chat)
    val view = LocalView.current
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val navigationBarColor = Paper.toArgb()
    DisposableEffect(view, navigationBarColor) {
        val window = view.context.findMainActivity()?.window
        @Suppress("DEPRECATION")
        val previousNavigationBarColor = window?.navigationBarColor
        @Suppress("DEPRECATION")
        window?.navigationBarColor = navigationBarColor
        onDispose {
            @Suppress("DEPRECATION")
            if (previousNavigationBarColor != null) window.navigationBarColor = previousNavigationBarColor
        }
    }
    var mediaSheetVisible by remember(chat.id) { mutableStateOf(reopenMediaSheet) }
    var resetSelectionConfirmationVisible by remember(chat.id) { mutableStateOf(false) }
    var attachmentSection by remember(chat.id) { mutableStateOf(AttachmentSection.Media) }
    var pendingAttachmentSection by remember(chat.id) { mutableStateOf<AttachmentSection?>(null) }
    var animatedReply by remember(chat.id) { mutableStateOf(replyTo) }
    val replyPanelProgress = remember(chat.id) { Animatable(if (replyTo != null) 1f else 0f) }
    var fileAccessVersion by remember(chat.id) { mutableIntStateOf(0) }
    val mediaSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var actionMessage by remember(chat.id) { mutableStateOf<MessageItem?>(null) }
    var actionMediaItems by remember(chat.id) { mutableStateOf<List<MessageItem>>(emptyList()) }
    var forwardCandidate by remember(chat.id) { mutableStateOf<MessageItem?>(null) }
    var pendingVideoCall by remember(chat.id) { mutableStateOf<Boolean?>(null) }
    var mediaToSave by remember(chat.id) { mutableStateOf<MessageItem?>(null) }
    var voiceRecorder by remember(chat.id) { mutableStateOf<MediaRecorder?>(null) }
    var voiceRecordingFile by remember(chat.id) { mutableStateOf<File?>(null) }
    var voiceRecordingStartedAt by remember(chat.id) { mutableLongStateOf(0L) }
    var voiceRecordingDuration by remember(chat.id) { mutableLongStateOf(0L) }
    var startVoiceAfterPermission by remember(chat.id) { mutableStateOf(false) }
    LaunchedEffect(reopenMediaSheet) {
        mediaSheetVisible = reopenMediaSheet
        if (reopenMediaSheet) resetSelectionConfirmationVisible = false
    }
    LaunchedEffect(replyTo) {
        if (replyTo != null) {
            animatedReply = replyTo
        }
        replyPanelProgress.animateTo(
            targetValue = if (replyTo != null) 1f else 0f,
            animationSpec = tween(240, easing = FastOutSlowInEasing)
        )
        if (replyTo == null) animatedReply = null
    }
    val saveSelectedMedia: (MessageItem) -> Unit = { item ->
        scope.launch {
            if (saveMessageAttachment(context, item, token)) {
                errorState.showSnackbar(
                    when (item.kind) {
                        "file" -> "Файл сохранён в загрузки"
                        "audio" -> "Аудио сохранено в загрузки"
                        else -> "Сохранено в галерею"
                    }
                )
            } else {
                errorState.showSnackbar(
                    when (item.kind) {
                        "file" -> "Не удалось сохранить файл"
                        "audio" -> "Не удалось сохранить аудио"
                        else -> "Не удалось сохранить медиа"
                    }
                )
            }
        }
    }
    val savePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        val item = mediaToSave
        mediaToSave = null
        if (granted && item != null) saveSelectedMedia(item) else if (!granted) permissionError()
    }
    val legacyFilePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        fileAccessVersion += 1
        if (!granted) {
            scope.launch { errorState.showSnackbar("Разрешите доступ к файлам в настройках устройства") }
        }
    }
    val allFilesAccessPermission = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        fileAccessVersion += 1
        if (!hasInternalFileAccess(context)) {
            scope.launch { errorState.showSnackbar("Разрешите доступ ко всем файлам в настройках устройства") }
        }
    }
    val voicePermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        startVoiceAfterPermission = granted
        if (!granted) scope.launch { errorState.showSnackbar("Разрешите доступ к микрофону в настройках устройства") }
    }
    val callPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val video = pendingVideoCall
        pendingVideoCall = null
        if (video != null && hasCallPermissions(context, video)) startCall(video) else callPermissionError()
    }
    val openAttachmentSheet = {
        mediaSheetVisible = true
    }
    val requestFileAccess: () -> Unit = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val applicationSettings = Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            val settingsIntent = if (applicationSettings.resolveActivity(context.packageManager) != null) {
                applicationSettings
            } else {
                Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            }
            allFilesAccessPermission.launch(settingsIntent)
        } else {
            legacyFilePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    val requestCall: (Boolean) -> Unit = { video ->
        if (hasCallPermissions(context, video)) startCall(video)
        else {
            pendingVideoCall = video
            callPermission.launch(requiredCallPermissions(video))
        }
    }
    fun startVoiceRecording(): Boolean {
        if (voiceRecorder != null || uploadProgress != null || editingMessage != null || draft.isNotBlank()) return false
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            voicePermission.launch(Manifest.permission.RECORD_AUDIO)
            return false
        }
        val directory = File(context.cacheDir, "voice_messages").apply { mkdirs() }
        val recordingFile = File(directory, "voice_${System.currentTimeMillis()}.m4a")
        val recorder = createVoiceMessageRecorder(context)
        return runCatching {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setAudioChannels(1)
            recorder.setAudioSamplingRate(44_100)
            recorder.setAudioEncodingBitRate(64_000)
            recorder.setOutputFile(recordingFile.absolutePath)
            recorder.prepare()
            recorder.start()
            voiceRecordingFile = recordingFile
            voiceRecordingStartedAt = SystemClock.elapsedRealtime()
            voiceRecordingDuration = 0L
            voiceRecorder = recorder
            voiceRecordingChanged(true)
            true
        }.getOrElse {
            runCatching { recorder.release() }
            recordingFile.delete()
            scope.launch { errorState.showSnackbar("Не удалось начать запись") }
            false
        }
    }
    LaunchedEffect(startVoiceAfterPermission) {
        if (startVoiceAfterPermission) {
            startVoiceAfterPermission = false
            startVoiceRecording()
        }
    }
    fun finishVoiceRecording(sendRecording: Boolean) {
        val recorder = voiceRecorder ?: return
        voiceRecordingChanged(false)
        val recordingFile = voiceRecordingFile
        val duration = (SystemClock.elapsedRealtime() - voiceRecordingStartedAt).coerceAtLeast(0L)
        voiceRecorder = null
        voiceRecordingFile = null
        voiceRecordingStartedAt = 0L
        voiceRecordingDuration = 0L
        val stopped = runCatching { recorder.stop() }.isSuccess
        runCatching { recorder.release() }
        if (!sendRecording || !stopped || recordingFile == null || !recordingFile.isFile || recordingFile.length() < 1L) {
            recordingFile?.delete()
            return
        }
        val recordingUri = runCatching {
            FileProvider.getUriForFile(context, "${context.packageName}.files", recordingFile)
        }.getOrNull()
        if (recordingUri == null) {
            recordingFile.delete()
            scope.launch { errorState.showSnackbar("Не удалось подготовить голосовое сообщение") }
            return
        }
        sendVoice(recordingUri, duration) { recordingFile.delete() }
    }
    val voiceRecording = voiceRecorder != null
    LaunchedEffect(voiceRecording, voiceRecordingStartedAt) {
        while (voiceRecording) {
            voiceRecordingDuration = (SystemClock.elapsedRealtime() - voiceRecordingStartedAt).coerceAtLeast(0L)
            delay(100)
        }
    }
    val latestVoiceRecorder by rememberUpdatedState(voiceRecorder)
    val latestVoiceFile by rememberUpdatedState(voiceRecordingFile)
    val latestVoiceRecordingChanged by rememberUpdatedState(voiceRecordingChanged)
    DisposableEffect(chat.id) {
        onDispose {
            if (latestVoiceRecorder != null) latestVoiceRecordingChanged(false)
            latestVoiceRecorder?.let { recorder ->
                runCatching { recorder.stop() }
                runCatching { recorder.release() }
            }
            latestVoiceFile?.delete()
        }
    }
    val listState = rememberLazyListState()
    val pendingDraftUpdate = remember(chat.id) { AtomicReference<Job?>(null) }
    val latestDraftText = remember(chat.id) { AtomicReference(draft) }
    val lastDraftEditAt = remember(chat.id) { AtomicLong(0L) }
    val draftDirty = remember(chat.id) { AtomicBoolean(false) }
    val latestUpdateDraft by rememberUpdatedState(updateDraft)
    DisposableEffect(chat.id) {
        onDispose {
            pendingDraftUpdate.getAndSet(null)?.cancel()
            if (draftDirty.getAndSet(false)) latestUpdateDraft(latestDraftText.get())
        }
    }
    val scrollDownThreshold = with(LocalDensity.current) { 180.dp.roundToPx() }
    val showScrollDownButton by remember(listState, scrollDownThreshold) {
        derivedStateOf {
            listState.firstVisibleItemIndex >= 2 ||
                    listState.firstVisibleItemScrollOffset >= scrollDownThreshold
        }
    }
    var newMessagesBelow by remember(chat.id) { mutableIntStateOf(0) }
    var replySwipeActive by remember(chat.id) { mutableStateOf(false) }
    val updateReplySwipeActive: (Boolean) -> Unit = { active ->
        replySwipeActive = active
        if (active) scope.launch { listState.scroll(MutatePriority.PreventUserInput) {} }
    }
    val messageGroups = remember(messages) {
        groupChatMessagesByDay(messages)
    }
    val timeline = remember(messageGroups) {
        buildList {
            messageGroups.forEach { (day, dayEntries) ->
                dayEntries.asReversed().forEach { add(ChatTimelineMessage(it)) }
                add(ChatTimelineDate(day, dayEntries.first().primary.createdAt))
            }
        }
    }
    val groupMembersById = remember(groupMembers) { groupMembers.associateBy { it.id } }
    val messagesById = remember(messages) { messages.associateBy { it.id } }
    val minimumReadVisibility = with(LocalDensity.current) { 24.dp.roundToPx() }
    LaunchedEffect(
        listState,
        timeline,
        chat.id,
        messagesHasMore,
        loadingOlderMessages,
        messages.isNotEmpty()
    ) {
        snapshotFlow {
            val layout = listState.layoutInfo
            var newestVisibleMessageId = 0L
            var lastVisibleIndex = 0
            layout.visibleItemsInfo.forEach { item ->
                if (item.index > lastVisibleIndex) lastVisibleIndex = item.index
                val visibleStart = maxOf(item.offset, layout.viewportStartOffset)
                val visibleEnd = minOf(item.offset + item.size, layout.viewportEndOffset)
                val visibleSize = (visibleEnd - visibleStart).coerceAtLeast(0)
                if (visibleSize >= minOf(item.size, minimumReadVisibility)) {
                    (timeline.getOrNull(item.index) as? ChatTimelineMessage)?.entry?.messages?.forEach { message ->
                        if (!message.mine && message.kind != "call" && message.id > newestVisibleMessageId) {
                            newestVisibleMessageId = message.id
                        }
                    }
                }
            }
            ChatViewportSnapshot(
                nearOldest = messages.isNotEmpty() && messagesHasMore && !loadingOlderMessages &&
                        layout.totalItemsCount > 0 && lastVisibleIndex >= layout.totalItemsCount - 3,
                newestVisibleMessageId = newestVisibleMessageId,
                atBottom = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
            )
        }.distinctUntilChanged().collect { viewport ->
            if (viewport.nearOldest) loadOlderMessages()
            if (viewport.newestVisibleMessageId > 0L) {
                markMessageRead(chat.id, viewport.newestVisibleMessageId)
            }
            if (viewport.atBottom) newMessagesBelow = 0
        }
    }
    val scrollToReply: (Long) -> Unit = { messageId ->
        val index = timeline.indexOfFirst { item ->
            item is ChatTimelineMessage && item.entry.messages.any { it.id == messageId }
        }
        if (index >= 0) scope.launch { listState.animateScrollToItem(index) }
    }
    val newestMessage = messages.lastOrNull()
    var previousNewestMessageId by remember(chat.id) { mutableLongStateOf(newestMessage?.id ?: 0L) }
    LaunchedEffect(newestMessage?.id) {
        val message = newestMessage
        if (message != null && message.id != previousNewestMessageId) {
            val nearBottom = listState.firstVisibleItemIndex <= 1
            if (!replySwipeActive && (message.mine || nearBottom)) {
                listState.scrollToItem(0)
                newMessagesBelow = 0
            } else if (!message.mine && message.kind != "call") {
                val incomingCount = messages.count {
                    it.id > previousNewestMessageId && !it.mine && it.kind != "call"
                }
                newMessagesBelow += incomingCount
            }
            previousNewestMessageId = message.id
        }
    }
    Column(Modifier.fillMaxSize().statusBarsPadding().imePadding()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Canvas,
            shadowElevation = 0.dp
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            RoundAction(Icons.AutoMirrored.Rounded.ArrowBack, "Назад", back)
            Spacer(Modifier.width(9.dp))
            val avatarInteraction = remember(chat.id) { MutableInteractionSource() }
            Box(
                Modifier.clip(CircleShape).clickable(
                    interactionSource = avatarInteraction,
                    indication = null
                ) {
                    openProfile()
                }
            ) {
                ChatAvatar(chat, 42.dp)
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                val typingMember = typingUserId?.let { groupMembersById[it] }
                val typingName = typingMember?.name ?: chat.name
                val headerStatusText = when {
                    connectionStatusText != null -> connectionStatusText
                    typing && (voiceTyping || typingActivity == "voice_recording") && chat.group -> "$typingName голосовое сообщение"
                    typing && (voiceTyping || typingActivity == "voice_recording") -> "голосовое сообщение"
                    typing && typingActivity == "photo_upload" && chat.group -> "$typingName отправляет фото"
                    typing && typingActivity == "photo_upload" -> "отправляет фото"
                    typing && typingActivity == "video_upload" && chat.group -> "$typingName отправляет видео"
                    typing && typingActivity == "video_upload" -> "отправляет видео"
                    typing && typingActivity == "media_upload" && chat.group -> "$typingName отправляет медиа"
                    typing && typingActivity == "media_upload" -> "отправляет медиа"
                    typing && typingActivity == "audio_upload" && chat.group -> "$typingName отправляет аудио"
                    typing && typingActivity == "audio_upload" -> "отправляет аудио"
                    typing && typingActivity == "file_upload" && chat.group -> "$typingName отправляет файл"
                    typing && typingActivity == "file_upload" -> "отправляет файл"
                    typing && chat.group -> "$typingName печатает"
                    typing -> "печатает"
                    !chat.saved && chat.group -> "${chat.memberCount} участников"
                    !chat.saved -> presenceStatusText
                    else -> null
                }
                if (headerStatusText != null) {
                    val highlightedStatus = connectionStatusText != null || typing || chat.online
                    RollingStatusText(
                        text = headerStatusText,
                        animatedDots = connectionStatusText != null || typing,
                        style = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                        color = if (highlightedStatus) Forest else Muted,
                        modifier = Modifier.fillMaxWidth().height(18.dp).clipToBounds()
                    )
                }
            }
            if (!chat.saved && !chat.group) {
                RoundAction(
                    icon = Icons.Rounded.Call,
                    description = "Аудиозвонок",
                    onClick = { requestCall(false) },
                    tint = Ink
                )
                RoundAction(
                    icon = Icons.Rounded.Videocam,
                    description = "Видеозвонок",
                    onClick = { requestCall(true) },
                    tint = Ink
                )
            }
        }
        }
        Box(Modifier.weight(1f).fillMaxWidth().background(ChatWallpaperBrush)) {
            if (messages.isEmpty() && !loading) {
                Text(
                    when {
                        chat.saved -> "Сохраняйте здесь важные сообщения"
                        chat.group -> "Начните общение в ${chat.name}"
                        else -> "Начните разговор с ${chat.name}"
                    },
                    color = Muted,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                state = listState,
                userScrollEnabled = !replySwipeActive,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Bottom),
                reverseLayout = true,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    timeline,
                    key = { it.key },
                    contentType = { it.contentType }
                ) { timelineItem ->
                    when (timelineItem) {
                        is ChatTimelineMessage -> {
                            val entry = timelineItem.entry
                        val message = entry.primary
                        val contextMessage = entry.messages.firstOrNull { it.replyToId > 0L } ?: message
                        val sender = groupMembersById[message.senderId]
                        MessageBubble(
                            message = message,
                            mediaItems = entry.messages,
                            token = token,
                            replyMessage = messagesById[contextMessage.replyToId],
                            showSenderAvatar = chat.group && !message.mine && message.kind != "call",
                            senderAvatar = sender?.avatar.orEmpty(),
                            openMedia = openRemoteMedia,
                            openFile = {
                                scope.launch {
                                    if (!openMessageFile(context, message, token)) errorState.showSnackbar("Не удалось открыть файл")
                                }
                            },
                            onLongPress = { selectedMessage ->
                                scope.launch {
                                    listState.stopScroll()
                                    actionMediaItems = if (entry.messages.size > 1) entry.messages else listOf(selectedMessage)
                                    actionMessage = selectedMessage
                                }
                            },
                            onSwipeReply = { selectReply(message) },
                            onReplySwipeActiveChanged = updateReplySwipeActive,
                            onReplyReferenceClick = scrollToReply,
                            showReadStatus = !chat.saved
                        )
                        }
                        is ChatTimelineDate -> MessageDateBubble(formatMessageDate(timelineItem.sourceDate))
                    }
                }
                if (loadingOlderMessages) {
                    item(key = "older_messages_loading") {
                        Box(Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(Modifier.size(20.dp), color = Forest, strokeWidth = 2.dp)
                        }
                    }
                }
            }
            Box(
                Modifier.align(Alignment.TopCenter).fillMaxWidth().height(8.dp).background(
                    Brush.verticalGradient(
                        listOf(Ink.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
            )
            Box(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(8.dp).background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Ink.copy(alpha = 0.12f))
                    )
                )
            )
            val scrollDownVisible = showScrollDownButton || newMessagesBelow > 0
            val scrollDownVerticalOffset by animateDpAsState(
                targetValue = if (scrollDownVisible) 0.dp else 72.dp,
                animationSpec = tween(220, easing = FastOutSlowInEasing),
                label = "scrollDownVerticalOffset"
            )
            Box(
                Modifier.align(Alignment.BottomEnd)
                    .graphicsLayer { translationY = scrollDownVerticalOffset.toPx() }
                    .padding(end = 16.dp, bottom = 16.dp)
            ) {
                    Surface(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0, 0)
                                newMessagesBelow = 0
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        enabled = scrollDownVisible,
                        shape = CircleShape,
                        color = Paper,
                        shadowElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "К последнему сообщению",
                                tint = Forest,
                                modifier = Modifier.size(27.dp)
                            )
                        }
                    }
                    if (newMessagesBelow > 0) {
                        UnreadBadge(
                            newMessagesBelow,
                            Modifier.align(Alignment.TopEnd).offset(x = 7.dp, y = (-7).dp)
                        )
                    }
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Paper,
            shadowElevation = 0.dp
        ) {
        Column(Modifier.fillMaxWidth().background(Paper).navigationBarsPadding()) {
        val replyPanelHeight = 66.dp
        val replyPanelHeightPx = with(LocalDensity.current) { replyPanelHeight.toPx() }
        Box(
            Modifier.fillMaxWidth()
                .height(replyPanelHeight * replyPanelProgress.value)
                .clipToBounds()
        ) {
            animatedReply?.let { message ->
                ReplyComposerPanel(
                    message = message,
                    token = token,
                    cancel = cancelReply,
                    modifier = Modifier.requiredHeight(replyPanelHeight).graphicsLayer {
                        translationY = replyPanelHeightPx * (1f - replyPanelProgress.value)
                    }
                )
            }
        }
        MessageComposer(
            value = editingMessage?.let { editingText } ?: draft,
            editing = editingMessage != null,
            voiceRecording = voiceRecording,
            voiceRecordingDuration = voiceRecordingDuration,
            uploadProgress = uploadProgress,
            cancelUpload = cancelUpload,
            onValueChange = {
                if (it.codePointCount(0, it.length) <= 4000) {
                    if (editingMessage == null) {
                        latestDraftText.set(it)
                        lastDraftEditAt.set(SystemClock.elapsedRealtime())
                        draftDirty.set(true)
                        if (pendingDraftUpdate.get()?.isActive != true) {
                            pendingDraftUpdate.set(scope.launch {
                                while (true) {
                                    val remaining = 180L - (SystemClock.elapsedRealtime() - lastDraftEditAt.get())
                                    if (remaining <= 0L) break
                                    delay(remaining)
                                }
                                if (draftDirty.getAndSet(false)) updateDraft(latestDraftText.get())
                                pendingDraftUpdate.set(null)
                            })
                        }
                        typingChanged(it.isNotBlank())
                    }
                }
            },
            onAttach = { if (uploadProgress == null) openAttachmentSheet() },
            onCancelEditing = cancelEditing,
            onVoiceRecordingStart = ::startVoiceRecording,
            onVoiceRecordingCancel = { finishVoiceRecording(false) },
            onVoiceRecordingSend = { finishVoiceRecording(true) },
            onSend = { composerText ->
                if (uploadProgress != null) {
                    Unit
                } else if (editingMessage != null) {
                    editMessage(composerText)
                } else {
                    val value = composerText.trim()
                    if (value.isNotEmpty()) {
                        pendingDraftUpdate.getAndSet(null)?.cancel()
                        draftDirty.set(false)
                        latestDraftText.set("")
                        updateDraft("")
                        send(value)
                    }
                }
            }
        )
        }
        }
    }
    if (mediaSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                if (selectedMedia.isNotEmpty()) {
                    scope.launch {
                        mediaSheetState.hide()
                        mediaSheetVisible = false
                        pendingAttachmentSection = null
                        resetSelectionConfirmationVisible = true
                    }
                } else {
                    mediaSheetVisible = false
                    dismissMediaSheet()
                }
            },
            sheetState = mediaSheetState,
            sheetGesturesEnabled = false,
            containerColor = Paper,
            scrimColor = OverlayScrimColor,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            BottomSheetHandle()
            Box(Modifier.fillMaxWidth().bottomSheetPop()) {
                AttachmentSheet(
                    storageAccessVersion = fileAccessVersion,
                    storageAccessGranted = hasInternalFileAccess(context),
                    selectedMedia = selectedMedia,
                    initialSection = attachmentSection,
                    sectionChanged = { attachmentSection = it },
                    requestSectionChange = { target ->
                        scope.launch {
                            mediaSheetState.hide()
                            mediaSheetVisible = false
                            pendingAttachmentSection = target
                            resetSelectionConfirmationVisible = true
                        }
                    },
                    selectMedia = { media, items ->
                        scope.launch {
                            mediaSheetState.hide()
                            mediaSheetVisible = false
                            openLocalMedia(media, items)
                        }
                    },
                    toggleMedia = toggleMediaSelection,
                    sendSelected = {
                        val outgoing = selectedMedia
                        scope.launch {
                            mediaSheetState.hide()
                            mediaSheetVisible = false
                            sendMedia(outgoing) {}
                        }
                    },
                    selectFile = { file ->
                        scope.launch {
                            mediaSheetState.hide()
                            mediaSheetVisible = false
                            dismissMediaSheet()
                            sendFile(Uri.fromFile(file))
                        }
                    },
                    requestStorageAccess = requestFileAccess
                )
                AppErrorPopup(errorState)
            }
        }
    }
    if (resetSelectionConfirmationVisible) {
        val target = pendingAttachmentSection
        val reopenSelection: () -> Unit = {
            resetSelectionConfirmationVisible = false
            pendingAttachmentSection = null
            mediaSheetVisible = true
            scope.launch {
                delay(16)
                mediaSheetState.show()
            }
        }
        ConfirmationSheet(
            title = "",
            confirmText = "Сбросить",
            centeredTitle = true,
            prominentActions = true,
            dismiss = reopenSelection,
            confirm = {
                clearMediaSelection()
                resetSelectionConfirmationVisible = false
                pendingAttachmentSection = null
                if (target == null) {
                    dismissMediaSheet()
                } else {
                    attachmentSection = target
                    mediaSheetVisible = true
                    scope.launch {
                        delay(16)
                        mediaSheetState.show()
                    }
                }
            }
        )
    }
    actionMessage?.let { selected ->
        val previewMediaItems = actionMediaItems.ifEmpty { listOf(selected) }
        val previewMessage = previewMediaItems.last()
        val previewContextMessage = previewMediaItems.firstOrNull {
            it.replyToId > 0L || it.forwardedFromName.isNotBlank()
        } ?: previewMessage
        val previewSender = groupMembersById[previewMessage.senderId]
        MessageActionsSheet(
            message = selected,
            previewMessage = previewMessage,
            mediaItems = previewMediaItems,
            token = token,
            replyMessage = messagesById[previewContextMessage.replyToId],
            showSenderAvatar = chat.group && !previewMessage.mine && previewMessage.kind != "call",
            senderAvatar = previewSender?.avatar.orEmpty(),
            showReadStatus = !chat.saved,
            dismiss = { actionMessage = null },
            delete = {
                actionMessage = null
                deleteMessage(selected)
            },
            edit = {
                actionMessage = null
                startEditing(selected)
            },
            copy = {
                clipboard.setText(AnnotatedString(messagePreviewText(selected)))
                actionMessage = null
            },
            open = {
                openExternalLink(context, firstMessageLink(selected.text))
                actionMessage = null
            },
            save = {
                actionMessage = null
                if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    mediaToSave = selected
                    savePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    saveSelectedMedia(selected)
                }
            },
            reply = {
                selectReply(selected)
                actionMessage = null
            },
            forward = {
                actionMessage = null
                forwardCandidate = selected
            }
        )
    }
    forwardCandidate?.let { selected ->
        ForwardMessageSheet(
            chats = chats,
            dismiss = { forwardCandidate = null },
            select = { target ->
                forwardCandidate = null
                forwardMessage(selected, target)
            }
        )
    }
}
