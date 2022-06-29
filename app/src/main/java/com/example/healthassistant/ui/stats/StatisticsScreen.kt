package com.example.healthassistant.ui.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit,
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    StatisticsScreen(
        substancesLastUsed = viewModel.substanceStats.collectAsState().value,
        navigateToSettings = navigateToSettings,
        navigateToSubstanceCompanion = navigateToSubstanceCompanion
    )
}

@Preview
@Composable
fun StatisticsPreview(
    @PreviewParameter(
        StatisticsPreviewProvider::class,
    ) substancesLastUsed: List<SubstanceStat>
) {
    StatisticsScreen(
        substancesLastUsed = substancesLastUsed,
        navigateToSettings = {},
        navigateToSubstanceCompanion = {}
    )
}

@Composable
fun StatisticsScreen(
    substancesLastUsed: List<SubstanceStat>,
    navigateToSettings: () -> Unit,
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Statistics") },
                actions = {
                    IconButton(
                        onClick = navigateToSettings,
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Navigate to Settings"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(substancesLastUsed.size) { i ->
                val subStat = substancesLastUsed[i]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToSubstanceCompanion(subStat.substanceName)
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    val isDarkTheme = isSystemInDarkTheme()
                    Surface(
                        shape = CircleShape,
                        color = subStat.color.getComposeColor(isDarkTheme),
                        modifier = Modifier.size(25.dp)
                    ) {}
                    Column {
                        Text(text = subStat.substanceName, style = MaterialTheme.typography.h6)
                        Text(text = "Last used ${subStat.lastUsedText} ago")
                    }
                }
                if (i < substancesLastUsed.size) {
                    Divider()
                }
            }
        }
    }
}