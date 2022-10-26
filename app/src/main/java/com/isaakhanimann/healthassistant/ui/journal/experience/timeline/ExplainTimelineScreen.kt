package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.healthassistant.ui.journal.experience.CardWithTitle
import com.isaakhanimann.healthassistant.ui.search.substance.BulletPoints
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.search.substance.VerticalSpace
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExplainTimelineScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(
                title = "Timeline Info",
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            VerticalSpace()
            BulletPoints(
                points = listOf(
                    "The vertical trajectory of dotted lines is unknown",
                    "A full stomach delays the onset of an orally consumed substance by approximately 3 hours",
                    "Durations are given only for common dosages. Heavy doses can have much longer durations",
                ),
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
            VerticalSpace()
            CardWithTitle(title = "Incomplete Timelines") {
                SectionText(text = "If there is no timeline or part of the timeline is missing that means that the duration is not defined in PsychonautWiki. If you add the missing durations in PsychonautWiki, the full timeline will be shown in the next update. ")
            }
            CardWithTitle(title = "PsychonautWiki Durations") {
                Column(Modifier.padding(horizontal = horizontalPadding)) {
                    SectionText(
                        text = "Duration refers to the length of time over which the subjective effects of a psychoactive substance manifest themselves.\n" +
                                "Duration can be broken down into 6 parts: (1) total duration (2) onset (3) come up (4) peak (5) offset and (6) after effects. Depending upon the substance consumed, each of these occurs in a separate and continuous fashion."
                    )
                    val titleStyle = MaterialTheme.typography.titleMedium
                    Text(text = "Total", style = titleStyle)
                    SectionText(text = "The total duration of a substance can be defined as the amount of time it takes for the effects of a substance to completely wear off into sobriety, starting from the moment the substance is first administered.")
                    Text(text = "Onset", style = titleStyle)
                    SectionText(text = "The onset phase can be defined as the period until the very first changes in perception (i.e. \"first alerts\") are able to be detected.")
                    Text(text = "Come up", style = titleStyle)
                    SectionText(text = "The \"come up\" phase can be defined as the period between the first noticeable changes in perception and the point of highest subjective intensity. This is colloquially known as \"coming up.\"")
                    Text(text = "Peak", style = titleStyle)
                    SectionText(text = "The peak phase can be defined as period of time in which the intensity of the substance's effects are at its height.")
                    Text(text = "Offset", style = titleStyle)
                    SectionText(text = "The offset phase can be defined as the amount of time in between the conclusion of the peak and shifting into a sober state. This is colloquially referred to as \"coming down.\"")
                    Text(text = "After Effects", style = titleStyle)
                    SectionText(text = "The after effects can be defined as any residual effects which may remain after the experience has reached its conclusion. This is colloquially known as a \"hangover\" or an \"afterglow\" depending on the substance and usage.\n" +
                            "The after effects are not included as part of the total duration.")
                }
            }
        }
    }
}
