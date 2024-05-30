package com.example.scanpiggy

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.renderer.PieChartRenderer
import com.github.mikephil.charting.utils.MPPointF

class CustomPieChartRenderer(
    pieChart: PieChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: com.github.mikephil.charting.utils.ViewPortHandler,
    private val images: List<Bitmap?>
) : PieChartRenderer(pieChart, animator, viewPortHandler) {

    override fun drawValues(c: Canvas) {
        super.drawValues(c)

        val pieData = mChart.data
        val dataSets = pieData.dataSets

        val radius = mChart.radius

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val center = mChart.centerCircleBox

        var angle = mChart.rotationAngle

        val drawAngles = mChart.drawAngles
        val absoluteAngles = mChart.absoluteAngles

        val circleBox = mChart.circleBox

        for (i in dataSets.indices) {
            val dataSet = dataSets[i]

            val entryCount = dataSet.entryCount

            mValuePaint.typeface = dataSet.valueTypeface
            mValuePaint.textSize = dataSet.valueTextSize
            mValuePaint.color = dataSet.valueTextColor

            for (j in 0 until entryCount) {
                if (j == images.size) break
                val entry = dataSet.getEntryForIndex(j)

                if (entry.y == 0f) continue

                val angleOffset = drawAngles[j] / 2f

                val angle = absoluteAngles[j] - angleOffset + mChart.rotationAngle

                val x = center.x + radius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = center.y + radius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

                val image = images[j]
                if (image != null) {
                    val halfImageWidth = image.width / 2f
                    val halfImageHeight = image.height / 2f

                    c.drawBitmap(image, x - halfImageWidth, y - halfImageHeight, null)
                }
            }
        }
    }
}