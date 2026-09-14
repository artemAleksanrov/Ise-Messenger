package com.example.isemessenger.feature.profile

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
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
import androidx.compose.material.icons.rounded.MoreHoriz
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
import androidx.compose.foundation.gestures.Orientation
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
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.roundToInt

@Composable
internal fun ProfileScreenRoute(
    chat: ChatItem, messages: List<MessageItem>, groupMembers: List<GroupMember>, token: String, openMedia: (MessageItem) -> Unit,
    openAvatar: (String, String, Long) -> Unit,
    editGroup: () -> Unit,
    messagesHasMore: Boolean, loadingOlderMessages: Boolean, loadOlderMessages: () -> Unit,
    startCall: (Boolean) -> Unit, callPermissionError: () -> Unit,
    renameChat: (String) -> Unit, archiveChat: (Boolean) -> Unit, clearChat: () -> Unit, deleteChat: () -> Unit,
    errorState: SnackbarHostState, back: () -> Unit
) = ChatProfileScreenContent(
    chat, messages, groupMembers, token, openMedia, openAvatar, editGroup, messagesHasMore, loadingOlderMessages, loadOlderMessages,
    startCall, callPermissionError, renameChat, archiveChat, clearChat, deleteChat, errorState, back
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatProfileScreenContent(
    chat: ChatItem,
    messages: List<MessageItem>,
    groupMembers: List<GroupMember>,
    token: String,
    openMedia: (MessageItem) -> Unit,
    openAvatar: (String, String, Long) -> Unit,
    editGroup: () -> Unit,
    messagesHasMore: Boolean,
    loadingOlderMessages: Boolean,
    loadOlderMessages: () -> Unit,
    startCall: (Boolean) -> Unit,
    callPermissionError: () -> Unit,
    renameChat: (String) -> Unit,
    archiveChat: (Boolean) -> Unit,
    clearChat: () -> Unit,
    deleteChat: () -> Unit,
    errorState: SnackbarHostState,
    back: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { ProfileSection.entries.size }
    val activeSection = ProfileSection.entries[pagerState.currentPage]
    val messagesById = remember(messages) { messages.associateBy { it.id } }
    var notificationsEnabled by remember(chat.id) { mutableStateOf(chatNotificationsEnabled(context, chat.id)) }
    var moreMenuVisible by remember(chat.id) { mutableStateOf(false) }
    var clearConfirmationVisible by remember(chat.id) { mutableStateOf(false) }
    var deleteConfirmationVisible by remember(chat.id) { mutableStateOf(false) }
    var renameSheetVisible by remember(chat.id) { mutableStateOf(false) }
    var membersSheetVisible by remember(chat.id) { mutableStateOf(false) }
    var pendingVideoCall by remember(chat.id) { mutableStateOf<Boolean?>(null) }
    var sectionsExpansion by remember(chat.id) { mutableFloatStateOf(0f) }
    var sectionsDragging by remember(chat.id) { mutableStateOf(false) }
    var sectionButtonDragging by remember(chat.id) { mutableStateOf(false) }
    var draggedSectionPosition by remember(chat.id) { mutableStateOf<Float?>(null) }
    var draggedSectionTarget by remember(chat.id) { mutableIntStateOf(0) }
    var profileHeaderHeight by remember(chat.id) { mutableIntStateOf(0) }
    var sectionsSettleJob by remember { mutableStateOf<Job?>(null) }
    val sectionsMoving = sectionsDragging || sectionButtonDragging || sectionsExpansion in 0.001f..0.999f
    val density = LocalDensity.current
    val callPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val video = pendingVideoCall
        pendingVideoCall = null
        if (video != null && hasCallPermissions(context, video)) startCall(video) else callPermissionError()
    }
    val requestCall: (Boolean) -> Unit = { video ->
        if (hasCallPermissions(context, video)) {
            startCall(video)
        } else {
            pendingVideoCall = video
            callPermission.launch(requiredCallPermissions(video))
        }
    }
    val settleSections: (Float) -> Unit = { target ->
        sectionsSettleJob?.cancel()
        sectionsSettleJob = scope.launch {
            animate(
                initialValue = sectionsExpansion,
                targetValue = target,
                animationSpec = tween(240, easing = FastOutSlowInEasing)
            ) { value, _ -> sectionsExpansion = value }
        }
    }
    BackHandler(enabled = sectionsExpansion > 0f) { settleSections(0f) }
    BoxWithConstraints(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Column(
            Modifier.fillMaxWidth().onSizeChanged { size ->
                if (size.height > 0) profileHeaderHeight = size.height
            }
        ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundAction(Icons.AutoMirrored.Rounded.ArrowBack, "Назад", back)
                    Spacer(Modifier.weight(1f))
                    if (chat.group && chat.owner) {
                        RoundAction(Icons.Rounded.Edit, "Настройки группы", editGroup)
                    }
                }
                Column(
                    Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val avatarInteraction = remember(chat.id) { MutableInteractionSource() }
                    Box(
                        Modifier.clip(CircleShape).clickable(
                            interactionSource = avatarInteraction,
                            indication = null
                        ) {
                            if (!chat.saved) {
                                val previewId = if (chat.group) {
                                    chat.avatarGradientSeed.takeIf { it != 0L } ?: chat.id
                                } else {
                                    chat.userId
                                }
                                openAvatar(chat.name, chat.avatar, previewId)
                            }
                        }
                    ) {
                        ChatAvatar(chat, 112.dp)
                        if (!chat.saved && !chat.group && chat.online) {
                            OnlineIndicator(
                                avatarSize = 112.dp,
                                backgroundColor = Canvas,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            chat.name,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 280.dp)
                        )
                        if (!chat.saved && !notificationsEnabled) {
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                Icons.Rounded.NotificationsOff,
                                contentDescription = "Уведомления отключены",
                                tint = Muted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    if (!chat.saved) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            if (chat.group) "${chat.memberCount} участников" else chat.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    Spacer(Modifier.height(18.dp))
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (chat.saved) {
                            ChatProfileAction(
                                Icons.Rounded.Delete,
                                "Очистить чат",
                                Modifier.fillMaxWidth()
                            ) { clearConfirmationVisible = true }
                        } else {
                        if (!chat.group) {
                            ChatProfileAction(Icons.Rounded.Call, "Аудио", Modifier.weight(1f)) { requestCall(false) }
                            ChatProfileAction(Icons.Rounded.Videocam, "Видео", Modifier.weight(1f)) { requestCall(true) }
                            ChatProfileAction(Icons.Rounded.Delete, "Очистить чат", Modifier.weight(1f)) {
                                clearConfirmationVisible = true
                            }
                        } else {
                            ChatProfileAction(Icons.Rounded.Groups, "Участники", Modifier.weight(1f)) {
                                membersSheetVisible = true
                            }
                        }
                        Box(Modifier.weight(1f)) {
                            ChatProfileAction(Icons.Rounded.MoreHoriz, "Ещё", Modifier.fillMaxWidth()) { moreMenuVisible = true }
                            DropdownMenu(
                                expanded = moreMenuVisible,
                                onDismissRequest = { moreMenuVisible = false },
                                containerColor = Paper,
                                shape = RoundedCornerShape(18.dp),
                                shadowElevation = 3.dp
                            ) {
                                if (!chat.group) {
                                    DropdownMenuItem(
                                        text = { Text("Переименовать для себя") },
                                        leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null, tint = Forest) },
                                        onClick = {
                                            moreMenuVisible = false
                                            renameSheetVisible = true
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = {
                                        Text(if (notificationsEnabled) "Отключить уведомления" else "Включить уведомления")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            if (notificationsEnabled) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications,
                                            contentDescription = null,
                                            tint = Forest
                                        )
                                    },
                                    onClick = {
                                        moreMenuVisible = false
                                        notificationsEnabled = !notificationsEnabled
                                        setChatNotificationsEnabled(context, chat.id, notificationsEnabled)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(if (chat.archived) "Вернуть" else "В архив") },
                                    leadingIcon = {
                                        Icon(
                                            if (chat.archived) Icons.Rounded.Unarchive else Icons.Rounded.Archive,
                                            contentDescription = null,
                                            tint = Forest
                                        )
                                    },
                                    onClick = {
                                        moreMenuVisible = false
                                        archiveChat(!chat.archived)
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when {
                                                chat.group && chat.owner -> "Удалить для всех"
                                                chat.group -> "Выйти"
                                                else -> "Удалить"
                                            }
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            if (chat.group && !chat.owner) Icons.AutoMirrored.Rounded.Logout else Icons.Rounded.Delete,
                                            contentDescription = null,
                                            tint = Forest
                                        )
                                    },
                                    onClick = {
                                        moreMenuVisible = false
                                        deleteConfirmationVisible = true
                                    }
                                )
                                }
                            }
                        }
                        }
                    }
        }
        if (profileHeaderHeight > 0) {
            val panelOffsetPx = profileHeaderHeight * (1f - sectionsExpansion)
            val panelHeight = with(density) {
                (constraints.maxHeight.toFloat() - panelOffsetPx).coerceAtLeast(0f).toDp()
            }
            Column(
                Modifier.fillMaxWidth().height(panelHeight)
                    .offset { IntOffset(0, panelOffsetPx.roundToInt()) }
                    .background(Canvas)
            ) {
        BoxWithConstraints(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp).clip(RoundedCornerShape(15.dp))
                .background(SoftSurface)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        if (profileHeaderHeight > 0) {
                            sectionsSettleJob?.cancel()
                            sectionsExpansion = (sectionsExpansion - delta / profileHeaderHeight).coerceIn(0f, 1f)
                        }
                    },
                    onDragStarted = {
                        sectionsSettleJob?.cancel()
                        sectionsDragging = true
                    },
                    onDragStopped = { velocity ->
                        sectionsDragging = false
                        val target = when {
                            velocity < -1_200f -> 1f
                            velocity > 1_200f -> 0f
                            sectionsExpansion >= 0.5f -> 1f
                            else -> 0f
                        }
                        settleSections(target)
                    }
                )
                .padding(4.dp)
        ) {
            val spacing = 2.dp
            val sectionWidth = (maxWidth - spacing * (ProfileSection.entries.size - 1)) / ProfileSection.entries.size
            val sectionStepPx = with(density) { (sectionWidth + spacing).toPx() }.coerceAtLeast(1f)
            val maximumSectionPosition = (ProfileSection.entries.size - 1).toFloat()
            Box(
                Modifier.offset {
                    val position = draggedSectionPosition ?: (pagerState.currentPage + pagerState.currentPageOffsetFraction)
                        .coerceIn(0f, maximumSectionPosition)
                    IntOffset(((sectionWidth + spacing).toPx() * position).roundToInt(), 0)
                }.width(sectionWidth).height(40.dp).clip(RoundedCornerShape(12.dp)).background(Forest)
            )
            Row(
                Modifier.fillMaxWidth().draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val nextPosition = ((draggedSectionPosition
                            ?: (pagerState.currentPage + pagerState.currentPageOffsetFraction)) + delta / sectionStepPx)
                            .coerceIn(0f, maximumSectionPosition)
                        draggedSectionPosition = nextPosition
                        val target = nextPosition.roundToInt().coerceIn(ProfileSection.entries.indices)
                        if (target != draggedSectionTarget) {
                            draggedSectionTarget = target
                            scope.launch { pagerState.animateScrollToPage(target) }
                        }
                    },
                    onDragStarted = {
                        sectionButtonDragging = true
                        draggedSectionTarget = pagerState.currentPage
                        draggedSectionPosition = (pagerState.currentPage + pagerState.currentPageOffsetFraction)
                            .coerceIn(0f, maximumSectionPosition)
                    },
                    onDragStopped = {
                        val target = (draggedSectionPosition ?: pagerState.currentPage.toFloat())
                            .roundToInt()
                            .coerceIn(ProfileSection.entries.indices)
                        draggedSectionPosition = null
                        sectionButtonDragging = false
                        scope.launch { pagerState.animateScrollToPage(target) }
                    }
                ),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                ProfileSection.entries.forEach { section ->
                    val label = when (section) {
                        ProfileSection.Media -> "Медиа"
                        ProfileSection.Audio -> "Аудио"
                        ProfileSection.Files -> "Файлы"
                        ProfileSection.Links -> "Ссылки"
                    }
                    AttachmentSectionButton(
                        label = label,
                        selected = activeSection == section,
                        onClick = {
                            if (!sectionsMoving) {
                                scope.launch { pagerState.animateScrollToPage(section.ordinal) }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            key = { ProfileSection.entries[it] },
            userScrollEnabled = !sectionsMoving,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            val section = ProfileSection.entries[page]
            val sectionMessages = remember(messages, section) {
                messages.filter { message ->
                    when (section) {
                        ProfileSection.Media -> message.kind == "image" || message.kind == "video"
                        ProfileSection.Audio -> message.isPlayableAudio()
                        ProfileSection.Files -> message.kind == "file" && !message.isPlayableAudio()
                        ProfileSection.Links -> message.kind == "text" && messageLinks(message.text).isNotEmpty()
                    }
                }
            }
            val sectionEntries = remember(sectionMessages, section) {
                if (section == ProfileSection.Media) {
                    groupMediaMessages(sectionMessages)
                } else {
                    sectionMessages.map { ChatMessageEntry(listOf(it)) }
                }
            }
            val displayedSectionEntries = remember(sectionEntries) { sectionEntries.asReversed() }
            val sectionListState = rememberLazyListState()
            LaunchedEffect(section, sectionEntries.lastOrNull()?.key) {
                if (sectionEntries.isNotEmpty()) {
                    sectionListState.scrollToItem(0)
                }
            }
            LaunchedEffect(
                sectionListState,
                messagesHasMore,
                loadingOlderMessages,
                messages.isNotEmpty(),
                displayedSectionEntries.size,
                sectionsMoving
            ) {
                snapshotFlow {
                    !sectionsMoving && messages.isNotEmpty() && messagesHasMore && !loadingOlderMessages &&
                            sectionListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                ?.let { it >= displayedSectionEntries.lastIndex - 2 } == true
                }.distinctUntilChanged().collect { nearOldest ->
                    if (nearOldest) loadOlderMessages()
                }
            }
            Box(Modifier.fillMaxSize()) {
                if (sectionEntries.isEmpty()) {
                    Text("Здесь пока ничего нет", color = Muted, modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        state = sectionListState,
                        reverseLayout = true,
                        userScrollEnabled = !sectionsMoving,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.Bottom),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(displayedSectionEntries, key = { it.key }, contentType = { it.primary.kind }) { entry ->
                            val message = entry.primary
                            MessageBubble(
                                message = message,
                                mediaItems = entry.messages,
                                token = token,
                                replyMessage = messagesById[
                                    (entry.messages.firstOrNull { it.replyToId > 0L } ?: message).replyToId
                                ],
                                openMedia = openMedia,
                                openFile = {
                                    scope.launch {
                                        if (!openMessageFile(context, message, token)) {
                                            errorState.showSnackbar("Не удалось открыть файл")
                                        }
                                    }
                                },
                                interactive = !sectionsMoving
                            )
                        }
                    }
                }
            }
        }
            }
        }
    }
    if (clearConfirmationVisible) {
        ConfirmationSheet(
            title = "",
            confirmText = "Очистить",
            prominentActions = true,
            dismiss = { clearConfirmationVisible = false },
            confirm = {
                clearConfirmationVisible = false
                clearChat()
            }
        )
    }
    if (deleteConfirmationVisible) {
        ConfirmationSheet(
            title = "",
            confirmText = when {
                chat.group && chat.owner -> "Удалить"
                chat.group -> "Выйти"
                else -> "Удалить"
            },
            prominentActions = true,
            dismiss = { deleteConfirmationVisible = false },
            confirm = {
                deleteConfirmationVisible = false
                deleteChat()
            }
        )
    }
    if (renameSheetVisible && !chat.group && !chat.saved) {
        RenameChatSheet(
            chat = chat,
            dismiss = { renameSheetVisible = false },
            save = { name ->
                renameSheetVisible = false
                renameChat(name)
            },
            reset = {
                renameSheetVisible = false
                renameChat("")
            }
        )
    }
    if (membersSheetVisible && chat.group) {
        GroupMembersSheet(
            members = groupMembers,
            openAvatar = { member -> openAvatar(member.name, member.avatar, member.id) },
            dismiss = { membersSheetVisible = false }
        )
    }
}

@Composable
internal fun ChatProfileAction(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier.height(64.dp).clip(RoundedCornerShape(12.dp)).background(Mint).clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, tint = Forest, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(4.dp))
            Text(label, color = Ink, fontSize = 10.sp, maxLines = 1, textAlign = TextAlign.Center)
        }
    }
}
