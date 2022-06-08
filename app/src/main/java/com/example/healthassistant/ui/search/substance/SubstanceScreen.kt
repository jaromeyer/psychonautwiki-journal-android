package com.example.healthassistant.ui.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.Effect
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider
import com.example.healthassistant.ui.search.substance.roa.RoaView
import com.example.healthassistant.ui.theme.HealthAssistantTheme
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SubstanceScreen(
    navigateToAddIngestion: (substanceName: String) -> Unit,
    viewModel: SubstanceViewModel = hiltViewModel()
) {
    SubstanceScreenContent(
        substance = viewModel.substance,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        onIngestTapped = {
            navigateToAddIngestion(viewModel.substance.name)
        }
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun SubstanceScreenPreview(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance
) {
    HealthAssistantTheme {
        SubstanceScreenContent(
            substance = substance,
            isSearchingForInteractions = true,
            dangerousInteractions = substance.dangerousInteractions,
            unsafeInteractions = substance.unsafeInteractions,
            uncertainInteractions = substance.uncertainInteractions,
            onIngestTapped = {}
        )
    }
}

@Composable
fun SubstanceScreenContent(
    substance: Substance,
    isSearchingForInteractions: Boolean,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
    onIngestTapped: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(substance.name) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onIngestTapped) {
                Icon(Icons.Default.Add, "Ingest ${substance.name}")
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val uriHandler = LocalUriHandler.current
            Button(onClick = {
                uriHandler.openUri(substance.url)
            }) {
                Text("Read Article")
            }
            val maxTotalDuration = remember(substance.roas) {
                substance.roas.mapNotNull { it.roaDuration?.total?.max }.maxOrNull()
            }
            substance.roas.forEach { roa ->
                RoaView(
                    roa = roa,
                    maxTotalDuration = maxTotalDuration
                )
            }
            InteractionsView(
                isSearchingForInteractions = isSearchingForInteractions,
                dangerousInteractions = dangerousInteractions,
                unsafeInteractions = unsafeInteractions,
                uncertainInteractions = uncertainInteractions
            )
            val tolerance = substance.tolerance
            if (tolerance != null) {
                SubstanceInfoCard(title = "Tolerance") {
                    Column {
                        Text(text = "zero: ${tolerance.zero}")
                        Text(text = "half: ${tolerance.half}")
                        Text(text = "full: ${tolerance.full}")
                    }
                }
            }
            if (substance.crossTolerances.isNotEmpty()) {
                SubstanceInfoCard(title = "Cross Tolerance") {
                    Column {
                        substance.crossTolerances.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.psychoactiveClasses.isNotEmpty()) {
                SubstanceInfoCard(title = "Psychoactive Class") {
                    Column {
                        substance.psychoactiveClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.chemicalClasses.isNotEmpty()) {
                SubstanceInfoCard(title = "Chemical Class") {
                    Column {
                        substance.chemicalClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.toxicity != null) {
                SubstanceInfoCard(title = "Toxicity") {
                    Text(text = substance.toxicity)
                }
            }
            if (substance.addictionPotential != null) {
                SubstanceInfoCard(title = "Addiction Potential") {
                    Text(substance.addictionPotential)
                }
            }
            if (substance.effects.isNotEmpty()) {
                SubstanceInfoCard(title = "Subjective Effects") {
                    FlowRow {
                        substance.effects.forEach {
                            EffectChip(effect = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EffectChip(effect: Effect) {
    val uriHandler = LocalUriHandler.current
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clickable {
                uriHandler.openUri(effect.url)
            },
        shape = RoundedCornerShape(20.dp),
        color = Color.Blue
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "link",
                tint = Color.White, modifier = Modifier.size(12.dp)
            )
            Text(
                effect.name,
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }

    }
}