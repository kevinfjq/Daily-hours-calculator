package org.example.internshipcalculator

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "InternshipCalculator",
        state = rememberWindowState(width = 500.dp, height = 800.dp, placement = WindowPlacement.Floating),
        resizable = false,
    ) {
        App()
    }
}