package advent.of.kotlin.utils

import java.io.File

fun readInputFileByLines(fileName: String): List<String> {
    val filePath = "app/src/main/resources/inputs/${fileName}"
    return File(filePath).readLines()
}