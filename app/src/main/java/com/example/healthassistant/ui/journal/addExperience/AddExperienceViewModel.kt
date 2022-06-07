package com.example.healthassistant.ui.journal.addExperience

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Experience
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddExperienceViewModel @Inject constructor(private val repository: ExperienceRepository) :
    ViewModel() {

    var title by mutableStateOf("")
    var notes by mutableStateOf("")
    val isTitleOk get() = title.isNotEmpty()
    val dateString: String
        get() {
            val formatter = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
            return formatter.format(Date()) ?: "Unknown"
        }

    init {
        title = dateString
    }

    fun onConfirmTap(onSuccess: (Int) -> Unit) {
        if (title.isNotEmpty()) {
            viewModelScope.launch {
                val newExperience =
                    Experience(
                        title = title,
                        text = notes
                    )
                val experienceId = repository.addExperience(newExperience)
                onSuccess(experienceId.toInt())
            }
        }
    }

}