package com.example.isemessenger.feature.group

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.isemessenger.AppErrorPopup
import com.example.isemessenger.AppState
import com.example.isemessenger.Avatar
import com.example.isemessenger.BottomSheetHandle
import com.example.isemessenger.BottomSheetWindowBehavior
import com.example.isemessenger.CenteredTopBar
import com.example.isemessenger.GallerySheet
import com.example.isemessenger.GroupEditorState
import com.example.isemessenger.GroupMember
import com.example.isemessenger.GroupMemberRow
import com.example.isemessenger.MembersSelectionSheet
import com.example.isemessenger.RoundAction
import com.example.isemessenger.bottomSheetPop
import com.example.isemessenger.hasImagePermission
import com.example.isemessenger.requiredImagePermissions
import com.example.isemessenger.core.config.*
import kotlinx.coroutines.launch

@Composable
internal fun GroupSettingsScreenRoute(
    state: AppState,
    editor: GroupEditorState,
    updateName: (String) -> Unit,
    saveName: () -> Unit,
    previewAvatar: (Uri) -> Unit,
    removeAvatar: () -> Unit,
    openAvatar: (String, String, Long) -> Unit,
    addMembers: (Set<Long>) -> Unit,
    removeMember: (GroupMember) -> Unit,
    permissionError: () -> Unit,
    errorState: SnackbarHostState,
    back: () -> Unit
) = GroupSettingsScreenContent(
    state = state,
    editor = editor,
    updateName = updateName,
    saveName = saveName,
    previewAvatar = previewAvatar,
    removeAvatar = removeAvatar,
    openAvatar = openAvatar,
    addMembers = addMembers,
    removeMember = removeMember,
    permissionError = permissionError,
    errorState = errorState,
    back = back
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GroupSettingsScreenContent(
    state: AppState,
    editor: GroupEditorState,
    updateName: (String) -> Unit,
    saveName: () -> Unit,
    previewAvatar: (Uri) -> Unit,
    removeAvatar: () -> Unit,
    openAvatar: (String, String, Long) -> Unit,
    addMembers: (Set<Long>) -> Unit,
    removeMember: (GroupMember) -> Unit,
    permissionError: () -> Unit,
    errorState: SnackbarHostState,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val candidates = remember(state.chats) {
        state.chats.filter { !it.saved && !it.group && it.userId > 0L }.distinctBy { it.userId }
    }
    var menuVisible by remember { mutableStateOf(false) }
    var galleryVisible by remember { mutableStateOf(false) }
    var memberPickerVisible by remember { mutableStateOf(false) }
    val gallerySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val galleryPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (hasImagePermission(context)) galleryVisible = true else permissionError()
    }
    val openGallery = {
        menuVisible = false
        if (hasImagePermission(context)) galleryVisible = true
        else galleryPermission.launch(requiredImagePermissions())
    }

    Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().imePadding()) {
        CenteredTopBar(
            title = "Настройки группы",
            startContent = { RoundAction(Icons.AutoMirrored.Rounded.ArrowBack, "Назад", back) },
            endContent = {
                Box {
                    RoundAction(
                        Icons.Rounded.MoreVert,
                        "Открыть меню",
                        onClick = { menuVisible = true }
                    )
                    DropdownMenu(
                        expanded = menuVisible,
                        onDismissRequest = { menuVisible = false },
                        containerColor = Paper,
                        shape = RoundedCornerShape(20.dp),
                        shadowElevation = 2.dp
                    ) {
                        DropdownMenuItem(
                            text = { Text("Сменить аватар") },
                            leadingIcon = { Icon(Icons.Rounded.CameraAlt, contentDescription = null, tint = Forest) },
                            onClick = openGallery
                        )
                        DropdownMenuItem(
                            text = { Text("Убрать аватар") },
                            leadingIcon = { Icon(Icons.Rounded.NoPhotography, contentDescription = null, tint = Forest) },
                            onClick = {
                                menuVisible = false
                                removeAvatar()
                            }
                        )
                        HorizontalDivider(color = Line, modifier = Modifier.padding(vertical = 6.dp))
                        DropdownMenuItem(
                            text = { Text("Добавить участников") },
                            leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = Forest) },
                            onClick = {
                                menuVisible = false
                                memberPickerVisible = true
                            }
                        )
                    }
                }
            }
        )

        Column(
            Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val previewId = editor.avatarGradientSeed.takeIf { it != 0L } ?: editor.chat?.id ?: 1L
            val avatarInteraction = remember { MutableInteractionSource() }
            Box(
                Modifier.clip(CircleShape).clickable(
                    interactionSource = avatarInteraction,
                    indication = null
                ) {
                    openAvatar(editor.name.ifBlank { "Группа" }, editor.avatar, previewId)
                }
            ) {
                Avatar(editor.name.ifBlank { "Группа" }, 104.dp, editor.avatar, previewId)
            }
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = editor.name,
                onValueChange = updateName,
                placeholder = { Text("Название группы", color = Muted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    saveName()
                }),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            saveName()
                        },
                        enabled = !state.loading
                    ) {
                        if (state.loading) {
                            CircularProgressIndicator(Modifier.size(20.dp), color = Forest, trackColor = Color.Transparent, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Rounded.Done, contentDescription = "Сохранить название", tint = Forest)
                        }
                    }
                },
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
                modifier = Modifier.fillMaxWidth().height(60.dp)
            )
        }

        LazyColumn(
            Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(editor.members, key = { it.id }, contentType = { "group_settings_member" }) { member ->
                GroupMemberRow(
                    member = member,
                    openAvatar = { openAvatar(member.name, member.avatar, member.id) },
                    remove = if (member.owner) null else {{ removeMember(member) }},
                    enabled = !state.loading
                )
            }
        }
    }

    if (galleryVisible) {
        ModalBottomSheet(
            onDismissRequest = { galleryVisible = false },
            sheetState = gallerySheetState,
            sheetGesturesEnabled = false,
            containerColor = Paper,
            scrimColor = OverlayScrimColor,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            BottomSheetWindowBehavior()
            BottomSheetHandle()
            Box(Modifier.fillMaxWidth().bottomSheetPop()) {
                GallerySheet { uri ->
                    scope.launch {
                        gallerySheetState.hide()
                        galleryVisible = false
                        previewAvatar(uri)
                    }
                }
                AppErrorPopup(errorState)
            }
        }
    }

    if (memberPickerVisible) {
        val existingIds = editor.members.mapTo(mutableSetOf()) { it.id }
        MembersSelectionSheet(
            chats = candidates.filter { it.userId !in existingIds },
            initialSelection = emptySet(),
            loading = state.loading,
            openAvatar = { chat -> openAvatar(chat.name, chat.avatar, chat.userId) },
            dismiss = { memberPickerVisible = false },
            confirm = { selected ->
                memberPickerVisible = false
                addMembers(selected)
            }
        )
    }
}
