package com.example.healthassistant.ui.search.substance.roa.duration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration
) {
    RoaDurationView(
        roaDuration = roaDuration,
        maxDurationInSeconds = 13.toDuration(DurationUnit.HOURS).inWholeSeconds.toFloat(),
        isOralRoute = true
    )
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    maxDurationInSeconds: Float?,
    isOralRoute: Boolean
) {
    Column {
        val total = roaDuration.total
        val colorTimeLine = MaterialTheme.colors.primary
        val colorTransparent = colorTimeLine.copy(alpha = 0.1f)
        val strokeWidth = 8f
        val strokeWidthThick = 40f
        val ingestionDotRadius = 10f
        val onset = roaDuration.onset
        val comeup = roaDuration.comeup
        val peak = roaDuration.peak
        val offset = roaDuration.offset
        val onsetInterpol = onset?.interpolateAtValueInSeconds(0.5f)
        val comeupInterpol = comeup?.interpolateAtValueInSeconds(0.5f)
        val peakInterpol = peak?.interpolateAtValueInSeconds(0.5f)
        val offsetInterpol = offset?.interpolateAtValueInSeconds(0.5f)
        val allDurations = listOf(onsetInterpol, comeupInterpol, peakInterpol, offsetInterpol)
        val undefinedCount = allDurations.count { it == null }
        if (maxDurationInSeconds != null && undefinedCount < 4) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                inset(left = ingestionDotRadius, top = 0f, right = 0f, bottom = 0f) {
                    val canvasWidth = size.width
                    val pixelsPerSec = canvasWidth.div(maxDurationInSeconds)
                    val sumDurations = allDurations.filterNotNull().reduce { acc, duration ->
                        acc + duration
                    }
                    val offsetDiff =
                        if (offset?.maxInSec != null && offset.minInSec != null) offset.maxInSec - offset.minInSec else null
                    val wholeDuration = total?.interpolateAtValueInSeconds(0.5f)
                        ?: if (offsetDiff != null) maxDurationInSeconds.minus(offsetDiff.div(2)) else maxDurationInSeconds
                    val restDuration = wholeDuration - sumDurations
                    val divider = if (undefinedCount == 0) 1 else undefinedCount
                    val dottedLineWidths = restDuration.div(divider) * pixelsPerSec
                    inset(vertical = strokeWidthThick / 2) {
                        val canvasHeight = size.height
                        drawCircle(
                            color = colorTimeLine,
                            radius = ingestionDotRadius,
                            center = Offset(x = 0f, y = canvasHeight)
                        )
                        val start1 =
                            onsetInterpol?.times(pixelsPerSec) ?: dottedLineWidths
                        val pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 30f)
                        )
                        drawLine(
                            start = Offset(x = 0f, y = canvasHeight),
                            end = Offset(x = start1, y = canvasHeight),
                            color = colorTimeLine,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                            pathEffect = if (onsetInterpol == null) pathEffect else null
                        )
                        if (onset?.maxInSec != null && onset.minInSec != null) {
                            val diff =
                                (onset.maxInSec - onset.minInSec).times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start1 - diff, y = canvasHeight),
                                end = Offset(x = start1 + diff, y = canvasHeight),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
                        }
                        val diff1 =
                            comeupInterpol?.times(pixelsPerSec) ?: dottedLineWidths
                        val start2 = start1 + diff1
                        drawLine(
                            start = Offset(x = start1, y = canvasHeight),
                            end = Offset(x = start2, y = 0f),
                            color = colorTimeLine,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                            pathEffect = if (comeupInterpol == null) pathEffect else null
                        )
                        if (comeup?.maxInSec != null && comeup.minInSec != null) {
                            val diff =
                                (comeup.maxInSec - comeup.minInSec).times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start2 - diff, y = 0f),
                                end = Offset(x = start2 + diff, y = 0f),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
                        }

                        val diff2 =
                            peakInterpol?.times(pixelsPerSec) ?: dottedLineWidths
                        val start3 = start2 + diff2
                        drawLine(
                            start = Offset(x = start2, y = 0f),
                            end = Offset(x = start3, y = 0f),
                            color = colorTimeLine,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                            pathEffect = if (peakInterpol == null) pathEffect else null
                        )
                        if (peak?.maxInSec != null && peak.minInSec != null) {
                            val diff = (peak.maxInSec - peak.minInSec).times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start3 - diff, y = 0f),
                                end = Offset(x = start3 + diff, y = 0f),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
                        }
                        val diff3 =
                            offsetInterpol?.times(pixelsPerSec) ?: dottedLineWidths
                        val start4 = start3 + diff3
                        drawLine(
                            start = Offset(x = start3, y = 0f),
                            end = Offset(x = start4, y = canvasHeight),
                            color = colorTimeLine,
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round,
                            pathEffect = if (offsetInterpol == null) pathEffect else null
                        )
                        if (offset?.maxInSec != null && offset.minInSec != null) {
                            val diff =
                                (offset.maxInSec - offset.minInSec).times(pixelsPerSec) / 2
                            drawLine(
                                start = Offset(x = start4 - diff, y = canvasHeight),
                                end = Offset(x = start4 + diff, y = canvasHeight),
                                color = colorTransparent,
                                strokeWidth = strokeWidthThick,
                                cap = StrokeCap.Round,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
        if ((total?.minInSec != null) && (total.maxInSec != null)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) { strokeWidthThick.toDp() })
            ) {
                inset(left = ingestionDotRadius, top = 0f, right = 0f, bottom = 0f) {
                    val canvasWidth = size.width
                    val midHeight = size.height / 2
                    drawCircle(
                        color = colorTimeLine,
                        radius = ingestionDotRadius,
                        center = Offset(x = 0f, y = midHeight)
                    )
                    val max = maxDurationInSeconds ?: total.maxInSec
                    val minX = total.minInSec.div(max) * canvasWidth
                    val maxX = total.maxInSec.div(max) * canvasWidth
                    val midX = (minX + maxX) / 2
                    drawLine(
                        start = Offset(x = 0f, y = midHeight),
                        end = Offset(x = midX, y = midHeight),
                        color = colorTimeLine,
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        start = Offset(x = minX, y = midHeight),
                        end = Offset(x = maxX, y = midHeight),
                        color = colorTransparent,
                        strokeWidth = strokeWidthThick,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        val durationTextStyle = MaterialTheme.typography.caption
        val rowArrangement = Arrangement.spacedBy(5.dp)
        if (total != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    text = "total",
                    style = durationTextStyle
                )
                Text(
                    text = total.text,
                    style = durationTextStyle
                )
            }
        }
        if (onset != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    "onset",
                    style = durationTextStyle
                )
                Text(
                    roaDuration.onset.text,
                    style = durationTextStyle
                )
                if (isOralRoute) {
                    Text(
                        text = " * a full stomach can delay the onset by hours",
                        style = durationTextStyle
                    )
                }
            }
        }
        if (comeup != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    "comeup",
                    style = durationTextStyle
                )
                Text(
                    roaDuration.comeup.text,
                    style = durationTextStyle
                )
            }
        }
        if (peak != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    "peak",
                    style = durationTextStyle
                )
                Text(
                    roaDuration.peak.text,
                    style = durationTextStyle
                )
            }
        }
        if (offset != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    "offset",
                    style = durationTextStyle
                )
                Text(
                    roaDuration.offset.text,
                    style = durationTextStyle
                )
            }
        }
        if (roaDuration.afterglow != null) {
            Row(horizontalArrangement = rowArrangement) {
                Text(
                    "after effects",
                    style = durationTextStyle
                )
                Text(
                    roaDuration.afterglow.text,
                    style = durationTextStyle
                )
            }
        }
    }

}