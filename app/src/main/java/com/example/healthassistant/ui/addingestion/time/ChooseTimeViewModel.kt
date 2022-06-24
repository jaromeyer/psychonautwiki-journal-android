package com.example.healthassistant.ui.addingestion.time

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val experienceId: Int?
    val substance: Substance?
    private val calendar: Calendar = Calendar.getInstance()
    val year = mutableStateOf(calendar.get(Calendar.YEAR))
    val month = mutableStateOf(calendar.get(Calendar.MONTH))
    val day = mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    val hour = mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    val minute = mutableStateOf(calendar.get(Calendar.MINUTE))
    private val currentlySelectedDate: Date
        get() {
            calendar.set(year.value, month.value, day.value, hour.value, minute.value)
            return calendar.time
        }
    val dateString: String
        get() {
            val formatter = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
            return formatter.format(currentlySelectedDate) ?: "Unknown"
        }
    val timeString: String
        get() {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatter.format(currentlySelectedDate) ?: "Unknown"
        }

    private val substanceName: String
    private val administrationRoute: AdministrationRoute
    private val dose: Double?
    private val units: String?
    private val isEstimate: Boolean
    private val color: IngestionColor

    init {
        substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
        substance = substanceRepo.getSubstance(substanceName)
        experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toIntOrNull()
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)!!
        administrationRoute = AdministrationRoute.valueOf(routeString)
        dose = state.get<String>(DOSE_KEY)?.toDoubleOrNull()
        units = state.get<String>(UNITS_KEY)?.let {
            if (it == "null") {
                null
            } else {
                it
            }
        }
        isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)!!
        val colorName = state.get<String>(COLOR_KEY)!!
        color = IngestionColor.valueOf(colorName)
        assert(substance != null)
    }

    fun onSubmitDate(newDay: Int, newMonth: Int, newYear: Int) {
        day.value = newDay
        month.value = newMonth
        year.value = newYear
    }

    fun onSubmitTime(newHour: Int, newMinute: Int) {
        hour.value = newHour
        minute.value = newMinute
    }

    fun createAndSaveIngestion() {
        viewModelScope.launch {
            val newIngestion = Ingestion(
                substanceName = substanceName,
                time = currentlySelectedDate,
                administrationRoute = administrationRoute,
                dose = dose,
                isDoseAnEstimate = isEstimate,
                units = units,
                color = color,
                experienceId = experienceId,
                notes = null
            )
            experienceRepo.addIngestion(newIngestion)
        }
    }
}