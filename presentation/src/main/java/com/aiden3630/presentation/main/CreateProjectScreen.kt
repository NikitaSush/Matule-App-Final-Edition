package com.aiden3630.presentation.main

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aiden3630.presentation.components.MatuleButton
import com.aiden3630.presentation.components.MatuleDatePicker
import com.aiden3630.presentation.components.MatuleTextField
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.utils.convertMillisToDate
import com.aiden3630.presentation.utils.createImageFile
import com.aiden3630.presentation.R as UiKitR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onBackClick: () -> Unit = {},
    viewModel: CreateProjectViewModel = hiltViewModel()
) {
    // 👇 1. ВОТ ЭТОЙ СТРОКИ НЕ ХВАТАЛО
    val context = LocalContext.current

    // --- Состояния полей ---
    var type by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var dateStart by remember { mutableStateOf("") }
    var dateEnd by remember { mutableStateOf("") }
    var toWhom by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    // Для календаря
    var dateFieldType by remember { mutableStateOf<String?>(null) }

    // Состояние картинки
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Состояние шторки
    var showPhotoSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Списки
    val typeOptions = listOf("Вязание спицами", "Вязание крючком", "Шитье", "Вышивка")
    val whoOptions = listOf("Себе", "В подарок", "На продажу", "Детям")
    val categoryOptions = listOf("Одежда", "Игрушки", "Аксессуары", "Дом")

    // --- ЛАУНЧЕРЫ ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            selectedImageUri = tempCameraUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = context.createImageFile()
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Нужен доступ к камере", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // --- 1. Хедер ---
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_chevron_left),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart)
            )
            Text(
                text = "Создать проект",
                style = Title2,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        // --- 2. Поля ---
        InputLabel("Тип")
        ProjectDropdown(value = type, onValueChange = { type = it }, placeholder = "Выберите тип", options = typeOptions)
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Название проекта")
        MatuleTextField(value = name, onValueChange = { name = it }, placeholder = "Введите имя")
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Дата начала")
        MatuleTextField(
            value = dateStart,
            onValueChange = {},
            placeholder = "ДД.ММ.ГГГГ",
            readOnly = true,
            onClick = { dateFieldType = "start" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Дата Окончания")
        MatuleTextField(
            value = dateEnd,
            onValueChange = {},
            placeholder = "ДД.ММ.ГГГГ",
            readOnly = true,
            onClick = { dateFieldType = "end" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Кому")
        ProjectDropdown(value = toWhom, onValueChange = { toWhom = it }, placeholder = "Выберите кому", options = whoOptions)
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Источник описания")
        MatuleTextField(value = source, onValueChange = { source = it }, placeholder = "example.com")
        Spacer(modifier = Modifier.height(16.dp))

        InputLabel("Категория")
        ProjectDropdown(value = category, onValueChange = { category = it }, placeholder = "Выберите категорию", options = categoryOptions)
        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Фото ---
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MatuleInputBg)
                .clickable { showPhotoSheet = true }
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Project Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = UiKitR.drawable.ic_plus),
                    contentDescription = "Add Photo",
                    tint = MatuleTextGray,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- 4. Кнопка Подтвердить ---
        MatuleButton(
            text = "Подтвердить",
            onClick = {
                viewModel.createProject(
                    context = context, // 👈 Передаем контекст
                    name = name,
                    type = type,
                    dateStart = dateStart,
                    imageUri = selectedImageUri // 👈 Передаем Uri (не toString)
                ) {
                    Toast.makeText(context, "Проект создан!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
            },
            enabled = name.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(40.dp))
    }

    // --- Календарь ---
    if (dateFieldType != null) {
        MatuleDatePicker(
            onDateSelected = { millis ->
                if (millis != null) {
                    val formattedDate = convertMillisToDate(millis)
                    if (dateFieldType == "start") dateStart = formattedDate else dateEnd = formattedDate
                }
            },
            onDismiss = { dateFieldType = null }
        )
    }

    // --- Шторка Фото ---
    if (showPhotoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSheet = false },
            sheetState = sheetState,
            containerColor = MatuleWhite
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Загрузить фото", style = Title2)
                Spacer(modifier = Modifier.height(20.dp))
                MatuleButton(text = "Сделать фото", onClick = {
                    showPhotoSheet = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                })
                Spacer(modifier = Modifier.height(12.dp))
                MatuleButton(text = "Выбрать из галереи", onClick = {
                    showPhotoSheet = false
                    galleryLauncher.launch("image/*")
                })
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// Вспомогательные функции
@Composable
fun InputLabel(text: String) {
    Text(
        text = text,
        style = Caption,
        color = MatuleTextGray,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
fun ProjectDropdown(value: String, onValueChange: (String) -> Unit, placeholder: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        MatuleTextField(
            value = value,
            onValueChange = {},
            placeholder = placeholder,
            readOnly = true,
            onClick = { expanded = true },
            trailingIcon = {
                Icon(painter = painterResource(id = UiKitR.drawable.ic_chevron_down), contentDescription = null, tint = MatuleTextGray)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MatuleWhite).fillMaxWidth(0.9f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = BodyText) },
                    onClick = { onValueChange(option); expanded = false }
                )
            }
        }
    }
}