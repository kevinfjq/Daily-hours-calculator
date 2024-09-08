package org.example.internshipcalculator

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.example.internshipcalculator.themes.darkColorScheme
import org.example.internshipcalculator.themes.lightColorScheme
import org.example.internshipcalculator.utils.DateTimeController.Companion.calculateBusinessDays
import org.example.internshipcalculator.utils.DateTimeController.Companion.getDate
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme as MaterialTheme3

import org.example.internshipcalculator.utils.DateTimeController.Companion.getDayOfWeek

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
                val dayOfWeek = getDayOfWeek(getDate(utcTimeMillis))
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
                val dayOfWeek = getDayOfWeek(getDate(utcTimeMillis))
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
                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    onClick = {
                        println(selectedDates.value)
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme3.colorScheme.secondary),
                ) {
                     Text(
                         modifier = Modifier
                             .padding(10.dp, 5.dp),
                         text = "Calcular Dias",
                         fontSize = 15.sp,
                         color =  Color(0xFFDDDDDD),
                         fontWeight = FontWeight.SemiBold
                     )
                }
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
                    fontSize = 20.sp,
                    text = title?: "Select Date")
            },
            title = null,
            state = dateState,
            modifier = Modifier.padding(5.dp)
        )
    }
}

