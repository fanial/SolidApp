package com.solidecoteknologi.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatDate(inputDateString: String): String {
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"
    val outputFormat = "dd MMM yyyy, HH:mm"
    val timeZone = TimeZone.getTimeZone("UTC")
    val tz = TimeZone.getTimeZone("UTC+7")
    val inputParser = SimpleDateFormat(inputFormat, Locale.getDefault())
    inputParser.timeZone = timeZone

    val date = inputParser.parse(inputDateString)

    val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
    outputFormatter.timeZone = tz

    return outputFormatter.format(date)
}