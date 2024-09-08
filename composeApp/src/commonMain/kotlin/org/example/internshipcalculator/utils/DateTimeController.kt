package org.example.internshipcalculator.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

class DateTimeController {

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

        fun getDaysDifference(instant1: Instant, instant2 : Instant): Int {
            if(instant1.daysUntil(instant2, TimeZone.UTC) >= 0) {
                return instant1.daysUntil(instant2, TimeZone.UTC)
            } else throw Exception("Invalid date range")
        }
        fun getDayOfWeek(instant: Instant): DayOfWeek {
            val dayOfWeek = instant.toLocalDateTime(TimeZone.UTC).dayOfWeek
            return DayOfWeek.valueOf(dayOfWeek.toString())
        }

        fun calculateBusinessDays(instant1: Instant, instant2: Instant, startDate: DayOfWeek): Int {
            val days = getDaysDifference(instant1, instant2)
            var index = (dayOfWeekList.indexOf(startDate)) -1
            var count = 0
            for (i in 0..days) {
                index++
                if(index >= dayOfWeekList.size) {
                    index = 0
                }
                if(dayOfWeekList[index] == DayOfWeek.SUNDAY || dayOfWeekList[index] == DayOfWeek.SATURDAY) {
                    if(index == dayOfWeekList.size) {
                        println("Index reset " + index)
                        index = 0
                    }
                    continue
                }
                count++

            }
            return count
        }
    }
}