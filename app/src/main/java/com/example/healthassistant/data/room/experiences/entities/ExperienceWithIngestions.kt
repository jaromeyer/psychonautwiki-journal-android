package com.example.healthassistant.data.room.experiences.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ExperienceWithIngestions(
    @Embedded val experience: Experience,
    @Relation(
        parentColumn = "id",
        entityColumn = "experienceId"
    ) val ingestions: List<Ingestion>
)