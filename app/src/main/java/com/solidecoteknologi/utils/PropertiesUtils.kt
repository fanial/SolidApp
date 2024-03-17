package com.solidecoteknologi.utils

import java.io.FileInputStream
import java.util.Properties

object PropertyReader {
    private const val PROPERTIES_FILE = "local.properties"

    fun getProperty(key: String): String? {
        val properties = Properties()
        val fileInputStream = FileInputStream(PROPERTIES_FILE)
        properties.load(fileInputStream)
        return properties.getProperty(key)
    }
}