package org.example.internshipcalculator.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

class DateTimeController {

    enum class DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }


    companion object {
        val dayOfWeekList = listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )

        fun getDate(milliSeconds: Long): Instant {
            return Instant.fromEpochMilliseconds(milliSeconds)
        }

        fun getDays(instant1: Instant, instant2 : Instant): Int {
            println(instant1.toLocalDateTime(TimeZone.UTC))
            return instant1.daysUntil(instant2, TimeZone.UTC)
        }
        fun getDayOfWeek(instant: Instant): DayOfWeek {
            val dayOfWeek = instant.toLocalDateTime(TimeZone.UTC).dayOfWeek
            return DayOfWeek.valueOf(dayOfWeek.toString())
        }

        fun calculateBusinessDays(instant1: Instant, instant2: Instant, startDate: DayOfWeek, endDate: DayOfWeek): Int {
            val days = getDays(instant1, instant2)
            var index = dayOfWeekList.indexOf(startDate)
            var count = 0
            for (i in 0..days) {
                if(dayOfWeekList[index] == DayOfWeek.SUNDAY || dayOfWeekList[index] == DayOfWeek.SATURDAY) {
                    if(index == dayOfWeekList.size) {
                        index = 0
                    }
                    continue
                }
                count++
                index++
                if(index == dayOfWeekList.size) {
                    index = 0
                }
            }
            return count
        }
    }
}