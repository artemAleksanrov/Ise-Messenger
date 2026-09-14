package com.example.isemessenger.feature.chats

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
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Unarchive
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.VideocamOff
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.material3.rememberDrawerState
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
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.roundToInt

@Composable
internal fun ChatsScreen(controller: MessengerController, archived: Boolean) {
    val state = controller.state
    ChatsScreenContent(
        chats = state.chats,
        drafts = state.drafts,
        loading = state.loading,
        userName = state.userName,
        userEmail = state.email,
        userAvatar = state.userAvatar,
        userId = state.userId,
        availableUpdate = state.availableUpdate,
        updateDownloading = state.updateDownloading,
        archived = archived,
        openChat = controller::openChat,
        addChat = controller::showAddSheet,
        createGroup = controller::openCreateGroup,
        settings = controller::openSettings,
        openArchive = controller::openArchive,
        openSaved = controller::openSavedChat,
        archiveChat = controller::setChatArchived,
        deleteChat = controller::deleteChat,
        renameChat = controller::setPersonalChatName,
        installUpdate = controller::downloadAndInstallUpdate,
        back = controller::back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatsScreenContent(
    chats: List<ChatItem>,
    drafts: Map<Long, String>,
    loading: Boolean,
    userName: String,
    userEmail: String,
    userAvatar: String,
    userId: Long,
    availableUpdate: AppUpdateInfo?,
    updateDownloading: Boolean,
    archived: Boolean,
    openChat: (ChatItem) -> Unit,
    addChat: () -> Unit,
    createGroup: () -> Unit,
    settings: () -> Unit,
    openArchive: () -> Unit,
    openSaved: () -> Unit,
    archiveChat: (ChatItem, Boolean) -> Unit,
    deleteChat: (ChatItem) -> Unit,
    renameChat: (ChatItem, String) -> Unit,
    installUpdate: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    var searchVisible by remember(archived) { mutableStateOf(false) }
    var searchQuery by remember(archived) { mutableStateOf("") }
    var selectedChat by remember { mutableStateOf<ChatItem?>(null) }
    var deleteConfirmationTarget by remember { mutableStateOf<ChatItem?>(null) }
    var renameTarget by remember { mutableStateOf<ChatItem?>(null) }
    var activeSwipeChatId by remember { mutableStateOf<Long?>(null) }
    val displayedChats = remember(chats, archived, searchQuery) {
        val query = searchQuery.trim()
        chats.filter {
            it.archived == archived && !it.saved &&
                    (query.isEmpty() || it.name.contains(query, ignoreCase = true) ||
                            it.email.contains(query, ignoreCase = true))
        }
    }
    val showAppUpdate = !archived && searchQuery.isBlank() && availableUpdate != null
    val archivedUnreadCount = remember(chats) {
        chats.sumOf { if (it.archived) it.unread.toLong() else 0L }
    }
    val archivedUnreadLabel = if (archivedUnreadCount > 99L) "99+" else archivedUnreadCount.toString()
    val displayName = userName.ifBlank { "Пользователь" }
    val runDrawerAction: (() -> Unit) -> Unit = { action ->
        drawerScope.launch {
            drawerState.close()
            action()
        }
    }
    val closeSearch = {
        searchVisible = false
        searchQuery = ""
        focusManager.clearFocus()
    }
    BackHandler(enabled = searchVisible, onBack = closeSearch)
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !archived,
        scrimColor = Color.Black.copy(alpha = OverlayDimAlpha),
        drawerContent = {
            if (!archived) {
                ChatsSidePanel(
                    displayName = displayName,
                    email = userEmail,
                    avatar = userAvatar,
                    userId = userId,
                    archivedUnreadLabel = archivedUnreadLabel,
                    archivedUnread = archivedUnreadCount > 0L,
                    openSaved = { runDrawerAction(openSaved) },
                    openArchive = { runDrawerAction(openArchive) },
                    createGroup = { runDrawerAction(createGroup) },
                    addChat = { runDrawerAction(addChat) },
                    settings = { runDrawerAction(settings) }
                )
            }
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.fillMaxWidth().windowInsetsTopHeight(WindowInsets.statusBars).background(Canvas))
            Column(Modifier.fillMaxSize()) {
            CenteredTopBar(
                title = if (archived) "Архив" else "Чаты",
                modifier = Modifier.background(Canvas),
                startContent = {
                    if (archived) {
                        RoundAction(Icons.AutoMirrored.Rounded.ArrowBack, "Назад", back)
                    } else {
                    Box {
                        RoundAction(
                            Icons.Rounded.Menu,
                            "Открыть боковую панель",
                            onClick = { drawerScope.launch { drawerState.open() } }
                        )
                        if (archivedUnreadCount > 0L) {
                            RedStatusBadge(
                                archivedUnreadLabel,
                                Modifier.align(Alignment.TopEnd).offset(x = 3.dp, y = (-5).dp)
                            )
                        }
                    }
                    }
                },
                endContent = {
                    RoundAction(
                        Icons.Rounded.Search,
                        if (searchVisible) "Закрыть поиск" else "Поиск",
                        onClick = {
                            if (searchVisible) closeSearch() else searchVisible = true
                        },
                        tint = Ink
                    )
                }
            )
            AnimatedVisibility(
                visible = searchVisible,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(240, easing = FastOutSlowInEasing)
                ) + expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(240, easing = FastOutSlowInEasing),
                    clip = true
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(220, easing = FastOutSlowInEasing)
                ) + shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(220, easing = FastOutSlowInEasing),
                    clip = true
                )
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Поиск чатов", color = Muted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = SoftSurface,
                        unfocusedContainerColor = SoftSurface,
                        cursorColor = Forest
                    ),
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .height(54.dp)
                )
            }
            Box(Modifier.fillMaxSize()) {
                if (displayedChats.isEmpty() && !loading && !showAppUpdate) {
                    Text(
                        when {
                            searchQuery.isNotBlank() -> "Ничего не найдено"
                            archived -> "Архив пуст"
                            else -> "Чатов пока нет"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = Muted,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 10.dp,
                        bottom = if (showAppUpdate) 74.dp else 10.dp
                    ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedChats, key = { it.id }, contentType = { "chat" }) { chat ->
                        ChatRow(
                            chat = chat,
                            draft = drafts[chat.id].orEmpty(),
                            modifier = Modifier.animateItem(
                                fadeInSpec = null,
                                placementSpec = tween(280, easing = FastOutSlowInEasing),
                                fadeOutSpec = null
                            ),
                            onClick = { openChat(chat) },
                            onLongClick = { selectedChat = chat },
                            onArchive = { archiveChat(chat, !chat.archived) },
                            requestSwipe = {
                                if (activeSwipeChatId == null || activeSwipeChatId == chat.id) {
                                    activeSwipeChatId = chat.id
                                    true
                                } else {
                                    false
                                }
                            },
                            releaseSwipe = {
                                if (activeSwipeChatId == chat.id) activeSwipeChatId = null
                            }
                        )
                    }
                }
                if (showAppUpdate) {
                    AppUpdateBar(
                        downloading = updateDownloading,
                        install = installUpdate,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
            }
        }
    }
    selectedChat?.let { chat ->
        ChatActionsSheet(
            chat = chat,
            draft = drafts[chat.id].orEmpty(),
            notificationsEnabled = chatNotificationsEnabled(context, chat.id),
            archiveLabel = if (chat.archived) "Вернуть" else "В архив",
            archiveIcon = if (chat.archived) Icons.Rounded.Unarchive else Icons.Rounded.Archive,
            actionLabel = when {
                chat.group && chat.owner -> "Удалить для всех"
                chat.group -> "Выйти"
                else -> "Удалить"
            },
            actionIcon = if (chat.group && !chat.owner) Icons.AutoMirrored.Rounded.Logout else Icons.Rounded.Delete,
            dismiss = { selectedChat = null },
            rename = if (!chat.saved && !chat.group) {
                {
                    selectedChat = null
                    renameTarget = chat
                }
            } else null,
            toggleNotifications = {
                selectedChat = null
                val enabled = chatNotificationsEnabled(context, chat.id)
                setChatNotificationsEnabled(context, chat.id, !enabled)
            },
            archive = {
                selectedChat = null
                archiveChat(chat, !chat.archived)
            },
            delete = if (chat.saved) null else {
                {
                    selectedChat = null
                    deleteConfirmationTarget = chat
                }
            }
        )
    }
    deleteConfirmationTarget?.let { chat ->
        ConfirmationSheet(
            title = "",
            confirmText = when {
                chat.group && chat.owner -> "Удалить"
                chat.group -> "Выйти"
                else -> "Удалить"
            },
            prominentActions = true,
            dismiss = { deleteConfirmationTarget = null },
            confirm = {
                deleteConfirmationTarget = null
                deleteChat(chat)
            }
        )
    }
    renameTarget?.let { chat ->
        RenameChatSheet(
            chat = chat,
            dismiss = { renameTarget = null },
            save = { name ->
                renameTarget = null
                renameChat(chat, name)
            },
            reset = {
                renameTarget = null
                renameChat(chat, "")
            }
        )
    }
}

@Composable
private fun AppUpdateBar(
    downloading: Boolean,
    install: () -> Unit,
    modifier: Modifier = Modifier
) {
    var loadingDotCount by remember(downloading) { mutableIntStateOf(0) }
    LaunchedEffect(downloading) {
        if (!downloading) return@LaunchedEffect
        while (true) {
            delay(350)
            loadingDotCount = (loadingDotCount + 1) % 4
        }
    }
    Surface(
        color = Forest,
        modifier = modifier.fillMaxWidth().clickable(enabled = !downloading, onClick = install)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            if (downloading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Загрузка", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = ".".repeat(loadingDotCount),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.width(24.dp)
                    )
                }
            } else {
                Text(
                    text = "Обновить приложение",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ChatsSidePanel(
    displayName: String,
    email: String,
    avatar: String,
    userId: Long,
    archivedUnreadLabel: String,
    archivedUnread: Boolean,
    openSaved: () -> Unit,
    openArchive: () -> Unit,
    createGroup: () -> Unit,
    addChat: () -> Unit,
    settings: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(304.dp),
        drawerContainerColor = Paper,
        drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(vertical = 14.dp)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Avatar(displayName, 76.dp, avatar, userId)
                Spacer(Modifier.height(13.dp))
                Text(
                    displayName,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (email.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        email,
                        color = Muted,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            HorizontalDivider(color = Line, modifier = Modifier.padding(vertical = 6.dp))
            NavigationDrawerItem(
                label = { Text("Избранное", style = MaterialTheme.typography.labelLarge, color = Ink) },
                selected = false,
                onClick = openSaved,
                icon = { Icon(Icons.Rounded.Bookmark, contentDescription = null, tint = Forest) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            )
            NavigationDrawerItem(
                label = { Text("Архив", style = MaterialTheme.typography.labelLarge, color = Ink) },
                selected = false,
                onClick = openArchive,
                icon = { Icon(Icons.Rounded.Archive, contentDescription = null, tint = Forest) },
                badge = {
                    if (archivedUnread) RedStatusBadge(archivedUnreadLabel)
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            )
            NavigationDrawerItem(
                label = { Text("Создать группу", style = MaterialTheme.typography.labelLarge, color = Ink) },
                selected = false,
                onClick = createGroup,
                icon = { Icon(Icons.Rounded.Groups, contentDescription = null, tint = Forest) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            )
            NavigationDrawerItem(
                label = { Text("Добавить чат", style = MaterialTheme.typography.labelLarge, color = Ink) },
                selected = false,
                onClick = addChat,
                icon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = Forest) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            )
            NavigationDrawerItem(
                label = { Text("Настройки", style = MaterialTheme.typography.labelLarge, color = Ink) },
                selected = false,
                onClick = settings,
                icon = { Icon(Icons.Rounded.Settings, contentDescription = null, tint = Forest) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
