package org.example.internshipcalculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme as MaterialTheme3
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.internshipcalculator.themes.*

import internshipcalculator.composeapp.generated.resources.Res
import internshipcalculator.composeapp.generated.resources.compose_multiplatform
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.util.Calendar
import kotlinx.datetime.Instant
import java.time.ZoneId
import java.time.Instant as InstantJava

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val onlyBusinessDays by remember { mutableStateOf(true) }
    val selectedDates = remember { mutableStateOf<Pair<Long?, Long?>>(Pair(null, null)) }
    val datePickerStateStart = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Input,
        selectableDates =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val dayOfWeek = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.UTC)
                    .dayOfWeek
                val endDate = selectedDates.value.second
                val isValidRange = if (endDate != null) {
                    utcTimeMillis <= endDate
                } else true

                if (onlyBusinessDays) {
                    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && isValidRange
                }
                return isValidRange
            }
        }
    )
    val datePickerStateEnd = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Input,
        selectableDates =
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val dayOfWeek = Instant.fromEpochMilliseconds(utcTimeMillis)
                    .toLocalDateTime(TimeZone.UTC)
                    .dayOfWeek
                val startDate = selectedDates.value.first
                val isValidRange = if (startDate != null) {
                    utcTimeMillis >= startDate
                } else true

                if (onlyBusinessDays) {
                    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && isValidRange
                }
                return isValidRange
            }
        }
    )

    LaunchedEffect(datePickerStateStart.selectedDateMillis, datePickerStateEnd.selectedDateMillis) {
        selectedDates.value = Pair(datePickerStateStart.selectedDateMillis, datePickerStateEnd.selectedDateMillis)
    }
    MaterialTheme3(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme else lightColorScheme,

    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                DatePickerComposable(
                    modifier = Modifier,
                    dateState = datePickerStateStart,
                    title = "Inicio"
                )
                if (datePickerStateStart.selectedDateMillis != null) {
                    println(Instant.fromEpochMilliseconds(datePickerStateEnd.selectedDateMillis?: Clock.System.now().toEpochMilliseconds()).daysUntil(Instant.fromEpochMilliseconds(datePickerStateStart.selectedDateMillis?: Clock.System.now().toEpochMilliseconds()), TimeZone.UTC))
                }

                DatePickerComposable(
                    modifier = Modifier,
                    dateState = datePickerStateEnd,
                    title = "Fim"
                )

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposable(modifier: Modifier, dateState: DatePickerState, title: String? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        DatePicker(
            headline =  {
                Text(
                    modifier = Modifier
                        .padding(20.dp, 5.dp, 0.dp, 0.dp),
                    color = MaterialTheme3.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    text = title?: "Select Date")
            },
            title = null,
            state = dateState,
            modifier = Modifier.padding(5.dp)
        )
    }
}

