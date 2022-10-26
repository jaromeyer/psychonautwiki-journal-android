package com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.timelines

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.AllTimelinesModel
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.drawables.TimelineDrawable


data class OnsetTimeline(
    val onset: FullDurationRange,
) : TimelineDrawable {

    override fun getPeakDurationRangeInSeconds(startDurationInSeconds: Float): ClosedRange<Float>? {
        return null
    }

    override val widthInSeconds: Float =
        onset.maxInSeconds

    override fun drawTimeLine(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        val weight = 0.5f
        val onsetEndX =
            startX + (onset.interpolateAtValueInSeconds(weight) * pixelsPerSec)
        drawScope.drawPath(
            path = Path().apply {
                moveTo(x = startX, y = height)
                lineTo(x = onsetEndX, y = height)
            },
            color = color,
            style = AllTimelinesModel.normalStroke
        )
    }

    override fun drawTimeLineShape(
        drawScope: DrawScope,
        height: Float,
        startX: Float,
        pixelsPerSec: Float,
        color: Color,
    ) {
        drawScope.drawPath(
            path = Path().apply {
                val onsetEndMinX = startX + (onset.minInSeconds * pixelsPerSec)
                val onsetEndMaxX = startX + (onset.maxInSeconds * pixelsPerSec)
                moveTo(onsetEndMinX, height)
                lineTo(x = onsetEndMaxX, y = height)
                lineTo(x = onsetEndMaxX, y = height-AllTimelinesModel.shapeWidth)
                lineTo(x = onsetEndMinX, y = height-AllTimelinesModel.shapeWidth)
                close()
            },
            color = color.copy(alpha = AllTimelinesModel.shapeAlpha)
        )
    }
}

fun RoaDuration.toOnsetTimeline(): OnsetTimeline? {
    val fullOnset = onset?.toFullDurationRange()
    return if (fullOnset != null) {
        OnsetTimeline(
            onset = fullOnset,
        )
    } else {
        null
    }
}