package com.aiden3630.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.aiden3630.domain.model.UserProject
import com.aiden3630.presentation.main.ProjectsViewModel

import com.aiden3630.presentation.components.ProjectCard
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@Composable
fun ProjectsScreen(
    onAddProjectClick: () -> Unit = {},
    onProjectClick: (String) -> Unit = {},
    viewModel: ProjectsViewModel = hiltViewModel()
) {
    val projects by viewModel.projects.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Хедер
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Проекты",
                    style = Title2,
                    color = MatuleBlack,
                    modifier = Modifier.align(Alignment.Center)
                )

                Icon(
                    painter = painterResource(id = UiKitR.drawable.ic_plus),
                    contentDescription = "Add Project",
                    tint = MatuleBlue,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd)
                        .clickable { onAddProjectClick() }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Список проектов
        if (projects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "У вас пока нет проектов",
                        style = BodyText,
                        color = MatuleTextGray
                    )
                }
            }
        } else {
            items(projects) { project ->
                ProjectCard(
                    title = project.name,
                    date = project.dateStart,
                    imageUri = project.imageUri,
                    onClick = {
                        onProjectClick(project.id)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}