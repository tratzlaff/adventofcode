package com.tratzlaff

import java.io.File

fun readText(fileName: String) = File("src/main/resources/$fileName").readText()

fun readLines(fileName: String) = File("src/main/resources/$fileName").readLines()