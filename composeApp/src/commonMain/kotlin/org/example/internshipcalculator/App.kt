package org.example.internshipcalculator

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import androidx.compose.material3.TextField
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import org.example.internshipcalculator.themes.darkColorScheme
import org.example.internshipcalculator.themes.lightColorScheme
import org.example.internshipcalculator.utils.DateTimeController.Companion.calculateBusinessDays
import org.example.internshipcalculator.utils.DateTimeController.Companion.getDate
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme as MaterialTheme3

import org.example.internshipcalculator.utils.DateTimeController.Companion.getDayOfWeek
import org.example.internshipcalculator.utils.DateTimeController.Companion.getDaysDifference
import org.example.internshipcalculator.utils.DateTimeController.Companion.getHours


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val onlyBusinessDays by remember { mutableStateOf(true) }
    val selectedDates = remember { mutableStateOf<Pair<Long?, Long?>>(Pair(null, null)) }
    val timeText = remember { mutableStateOf("") }
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
                    println("Days until start(reverse)" + Instant.fromEpochMilliseconds(datePickerStateEnd.selectedDateMillis?: Clock.System.now().toEpochMilliseconds()).daysUntil(Instant.fromEpochMilliseconds(datePickerStateStart.selectedDateMillis?: Clock.System.now().toEpochMilliseconds()), TimeZone.UTC))
                }

                DatePickerComposable(
                    modifier = Modifier,
                    dateState = datePickerStateEnd,
                    title = "Fim"
                )
                val visible = remember { mutableStateOf(false) }
                var hourState by remember { mutableStateOf("") }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Start)
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Horas:",
                        color = MaterialTheme3.colorScheme.onSurfaceVariant,
                    )

                    TextField(
                        value = hourState,
                        onValueChange = { hourState = it },
                        placeholder = { Text("0", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        keyboardActions =
                            KeyboardActions(
                                onAny = {
                                    if (hourState.isEmpty() || hourState.toInt() < 0){
                                        hourState = "0"
                                    }
                                    else {
                                        if (hourState.toInt() > 24) {
                                            hourState = "24"
                                        }
                                    }
                                }
                            ),
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .padding(5.dp),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                }
                Text(text = "Se o valor digitado for maior que 24 ou menor que 0, será considerado o mais próximo", fontSize = 7.sp, color = MaterialTheme3.colorScheme.onSurfaceVariant, modifier = Modifier.padding(5.dp, 0.dp, 5.dp, 5.dp))


                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    onClick = {
                        if(selectedDates.value.first == null || selectedDates.value.second == null) {
                            return@ElevatedButton
                        } else {
                            val currentHour = if (hourState.isEmpty() || hourState.toInt() < 0){
                                0
                            } else {
                                if (hourState.toInt() > 24) 24 else hourState.toInt()
                            }
                            val days = calculateDays(selectedDates.value, onlyBusinessDays)
                            val hours = calculateHours(currentHour , days)
                            timeText.value = when(hours){
                                1 -> "Se passou $hours hora no decorrer de $days ${if (days == 1)  "dia" else "dias"}."
                                0 -> if (days == 1)  "Se passou $days dia." else "Se passaram $days dias."
                                else -> "Se passaram $hours horas no decorrer de $days ${if (days == 1)  "dia" else "dias"}."
                            }
                        }
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
                Text(
                    modifier = Modifier
                        .padding(10.dp, 5.dp),
                    text = if (timeText.value.isNotEmpty()) {
                            timeText.value
                        } else "",
                    fontSize = 12.sp,
                    color = MaterialTheme3.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
fun calculateDays(dates: Pair<Long?, Long?>, onlyBusinessDays: Boolean): Int  {
    val startDate = dates.first
    val endDate = dates.second
    if (startDate == null || endDate == null) {
        return 0
    }
    val startDateInstant = getDate(startDate)
    val endDateInstant = getDate(endDate)
    if (onlyBusinessDays) {
        val startDateDayOfWeek = getDayOfWeek(startDateInstant)
        val businessDays = calculateBusinessDays(startDateInstant, endDateInstant, startDateDayOfWeek)
        return businessDays
    }
    val days = getDaysDifference(startDateInstant, endDateInstant)
    return days
}

fun calculateHours(hours: Int, days: Int): Int {
    return getHours(hours, days)
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

