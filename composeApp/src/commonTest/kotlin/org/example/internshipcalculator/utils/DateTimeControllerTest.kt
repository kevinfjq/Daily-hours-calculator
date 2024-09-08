package org.example.internshipcalculator.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.example.internshipcalculator.utils.DateTimeController.Companion.calculateBusinessDays
import org.example.internshipcalculator.utils.DateTimeController.Companion.getDaysDifference
import org.junit.Test
import java.time.DayOfWeek
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals


class DateTimeControllerTest {
    @Test
    fun testGetDaysDifferenceEqualTenDays() {
        assertEquals(10, getDaysDifference(Instant.parse("2024-05-05T00:00:00.000Z"), Instant.parse("2024-05-15T00:00:00.000Z")))

    }
    @Test
    fun testGetDaysDifferenceAssertFailsWhenStartDateIsAfterEndDateOrEndDateIsBeforeStartDate() {
        assertFails("Invalid date range") {
            getDaysDifference(Clock.System.now(), Instant.parse("2024-05-15T00:00:00.000Z"))
        }
    }

    @Test
    fun testGetDaysDifferenceAssertNotEqualOneBetweenSpecificDateAndCurrentDate() {
        assertNotEquals(1, getDaysDifference(Instant.parse("2024-05-05T00:00:00.000Z"), Clock.System.now()))
    }

    @Test
    fun testCalculateBusinessDaysEqualsFiveForRangeStartingOnSundayInSevenDaysPeriod() {
        assertEquals(5, calculateBusinessDays(Instant.parse("2024-05-05T00:00:00.000Z"), Instant.parse("2024-05-12T00:00:00.000Z"), DayOfWeek.SUNDAY))
    }
}