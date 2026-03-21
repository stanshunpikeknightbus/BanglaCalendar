package com.banglacalendar.domain.usecase

import com.banglacalendar.domain.model.BanglaDate
import com.banglacalendar.domain.model.BanglaDateConverter
import com.banglacalendar.domain.model.CalendarDay
import java.time.LocalDate

class GetCalendarMonthUseCase {

    data class CalendarMonth(
        val banglaMonth:    Int,
        val banglaYear:     Int,
        val days:           List<CalendarDay?>,
        val leadingEmpties: Int,
        val totalDays:      Int
    )

    operator fun invoke(banglaMonth: Int, banglaYear: Int): CalendarMonth {
        val today    = LocalDate.now()
        val allDates = BanglaDateConverter.getMonthDates(banglaMonth, banglaYear)
        val leading  = BanglaDateConverter.bdDayOfWeek(allDates.first())

        val days = mutableListOf<CalendarDay?>()
        repeat(leading) { days.add(null) }
        allDates.forEach { date ->
            days.add(
                CalendarDay(
                    gregorianDate = date,
                    banglaDate    = BanglaDateConverter.toBanglaDate(date),
                    isToday       = date == today,
                    isFriday      = BanglaDateConverter.isFriday(date),
                    isSaturday    = BanglaDateConverter.isSaturday(date),
                    holidayName   = BanglaDateConverter.getHoliday(date)
                )
            )
        }
        return CalendarMonth(banglaMonth, banglaYear, days, leading, allDates.size)
    }
}

class GetTodayBanglaDateUseCase {
    operator fun invoke(): BanglaDate = BanglaDateConverter.toBanglaDate(LocalDate.now())
}

class NavigateMonthUseCase {
    fun previous(month: Int, year: Int) =
        if (month == 0) 11 to year - 1 else month - 1 to year

    fun next(month: Int, year: Int) =
        if (month == 11) 0 to year + 1 else month + 1 to year
}
