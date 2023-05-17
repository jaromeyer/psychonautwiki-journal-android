package com.isaakhanimann.journal.ui.tabs.search.substance.category

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.data.substances.classes.Category

@Preview
@Composable
fun CategoryDialogPreview() {
    CategoryDialog(category = Category(
        name = "psychedelic",
        description = "Psychedelics are drugs which alter the perception, causing a number of mental effects which manifest in many forms including altered states of consciousness, visual or tactile effects.",
        url = "https://psychonautwiki.org/wiki/Psychedelics",
        color = Color.Red
    ), navigateToURL = {}, dismiss = {})
}

@Composable
fun CategoryDialog(
    category: Category?, navigateToURL: (url: String) -> Unit, dismiss: () -> Unit
) {
    if (category == null) {
        AlertDialog(onDismissRequest = dismiss, title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "Category Not Found", style = MaterialTheme.typography.headlineSmall
                )
            }
        }, text = { Text("An error happened, please navigate back.") }, dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text("Close")
            }
        }, confirmButton = {})
    } else {
        AlertDialog(onDismissRequest = dismiss, title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Information")
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = category.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }, text = {
            Text(text = category.description)
        }, confirmButton = {
            if (category.url != null) {
                TextButton(
                    onClick = { navigateToURL(category.url) },
                ) {
                    Text("More Info")
                }
            }
        }, dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text("Close")
            }
        })
    }
}