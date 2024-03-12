package com.solidecoteknologi.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class PercentValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${(value * 100).toInt()}%"
    }
}