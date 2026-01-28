package com.aiden3630.presentation.main

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aiden3630.presentation.theme.*
import java.io.File

@Composable
fun ProjectDetailsScreen(
    projectId: String,
    onBackClick: () -> Unit,
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    // Загружаем проект при старте
    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    val project by viewModel.project.collectAsState()

    if (project == null) {
        // Показываем загрузку, пока ищем проект
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MatuleBlue)
        }
    } else {
        // Показываем данные
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleWhite)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Кнопка назад (можно добавить иконку как везде)
            TextButton(onClick = onBackClick) {
                Text("Назад", color = MatuleBlue)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Картинка
            if (project!!.imageUri != null) {
                AsyncImage(
                    model = File(project!!.imageUri!!), // Читаем из файла
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MatuleInputBg, RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = project!!.name, style = Title1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Категория: ${project!!.category}", style = BodyText, color = MatuleTextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Начало: ${project!!.dateStart}", style = BodyText)
        }
    }
}