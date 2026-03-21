package com.banglacalendar.domain.model

import java.time.LocalDate

/**
 * Bangladesh Revised Bengali Calendar (Bangabda) converter.
 * Officially adopted by Bangla Academy in 1987.
 */
object BanglaDateConverter {

    val MONTH_NAMES = listOf(
        "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র",
        "আশ্বিন", "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ",
        "ফাল্গুন", "চৈত্র"
    )

    val SEASON_NAMES = listOf(
        "গ্রীষ্মকাল", "বর্ষাকাল", "শরৎকাল",
        "হেমন্তকাল", "শীতকাল", "বসন্তকাল"
    )

    val SEASON_EMOJIS = listOf("☀️", "🌧️", "⛅", "🍂", "❄️", "🌸")

    // Week starts Friday in Bangladesh
    val DAY_NAMES_SHORT = listOf("শুক্র", "শনি", "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ")
    val DAY_NAMES_FULL  = listOf(
        "শুক্রবার", "শনিবার", "রবিবার", "সোমবার",
        "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার"
    )

    private val BN_DIGITS = arrayOf("০","১","২","৩","৪","৫","৬","৭","৮","৯")

    // Gregorian start date of each Bengali month: (month 1-based, day)
    // Months 9-11 (Magh, Falgun, Chaitra) start in the next Gregorian year
    private val MONTH_STARTS = listOf(
        4  to 14,   // 0  Baishakh
        5  to 15,   // 1  Jaistha
        6  to 15,   // 2  Asharh
        7  to 16,   // 3  Srabon
        8  to 16,   // 4  Bhadra
        9  to 16,   // 5  Ashwin
        10 to 16,   // 6  Kartik
        11 to 15,   // 7  Agrahayan
        12 to 15,   // 8  Poush
        1  to 13,   // 9  Magh       (next Gregorian year)
        2  to 13,   // 10 Falgun     (next Gregorian year)
        3  to 14    // 11 Chaitra    (next Gregorian year)
    )

    fun getDaysInMonth(monthIndex: Int, banglaYear: Int): Int = when (monthIndex) {
        in 0..4  -> 31
        10       -> if (isBanglaLeapYear(banglaYear)) 31 else 30
        11       -> if (isBanglaLeapYear(banglaYear)) 31 else 30
        else     -> 30
    }

    fun isBanglaLeapYear(banglaYear: Int): Boolean {
        val gy = banglaYear + 593
        return (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
    }

    fun toBanglaDate(date: LocalDate): BanglaDate {
        val gYear  = date.year
        val gMonth = date.monthValue
        val gDay   = date.dayOfMonth

        val isAfterNewYear = gMonth > 4 || (gMonth == 4 && gDay >= 14)
        val banglaYear = if (isAfterNewYear) gYear - 593 else gYear - 594

        var banglaMonth = 0
        var banglaDay   = 0
        for (i in 11 downTo 0) {
            val start = monthStartGregorian(i, banglaYear)
            if (!date.isBefore(start)) {
                banglaMonth = i
                banglaDay   = (date.toEpochDay() - start.toEpochDay()).toInt() + 1
                break
            }
        }
        return BanglaDate(banglaDay, banglaMonth, banglaYear)
    }

    fun monthStartGregorian(monthIndex: Int, banglaYear: Int): LocalDate {
        val (m, d) = MONTH_STARTS[monthIndex]
        val gYear  = if (monthIndex >= 9) banglaYear + 594 else banglaYear + 593
        return LocalDate.of(gYear, m, d)
    }

    fun toGregorianDate(bd: BanglaDate): LocalDate =
        monthStartGregorian(bd.month, bd.year).plusDays((bd.day - 1).toLong())

    fun getMonthDates(banglaMonth: Int, banglaYear: Int): List<LocalDate> {
        val days  = getDaysInMonth(banglaMonth, banglaYear)
        val start = monthStartGregorian(banglaMonth, banglaYear)
        return (0 until days).map { start.plusDays(it.toLong()) }
    }

    fun getSeasonIndex(banglaMonthIndex: Int): Int = banglaMonthIndex / 2

    fun toBengaliNumeral(n: Int): String =
        n.toString().map { BN_DIGITS[it.digitToInt()] }.joinToString("")

    /** 0 = Friday … 6 = Thursday (Bangladesh week) */
    fun bdDayOfWeek(date: LocalDate): Int = when (date.dayOfWeek.value) {
        5    -> 0  // Friday
        6    -> 1  // Saturday
        7    -> 2  // Sunday
        1    -> 3  // Monday
        2    -> 4  // Tuesday
        3    -> 5  // Wednesday
        4    -> 6  // Thursday
        else -> 0
    }

    fun isFriday(date: LocalDate)   = date.dayOfWeek.value == 5
    fun isSaturday(date: LocalDate) = date.dayOfWeek.value == 6

    fun getHoliday(date: LocalDate): String? = when {
        date.monthValue == 2  && date.dayOfMonth == 21 -> "শহীদ দিবস"
        date.monthValue == 3  && date.dayOfMonth == 26 -> "স্বাধীনতা দিবস"
        date.monthValue == 4  && date.dayOfMonth == 14 -> "পহেলা বৈশাখ"
        date.monthValue == 8  && date.dayOfMonth == 15 -> "জাতীয় শোক দিবস"
        date.monthValue == 12 && date.dayOfMonth == 16 -> "বিজয় দিবস"
        else -> null
    }
}

data class BanglaDate(
    val day:   Int,
    val month: Int,   // 0-based
    val year:  Int
) {
    val monthName:   String get() = BanglaDateConverter.MONTH_NAMES[month]
    val seasonIndex: Int    get() = BanglaDateConverter.getSeasonIndex(month)
    val seasonName:  String get() = BanglaDateConverter.SEASON_NAMES[seasonIndex]
    val seasonEmoji: String get() = BanglaDateConverter.SEASON_EMOJIS[seasonIndex]
    val dayBn:       String get() = BanglaDateConverter.toBengaliNumeral(day)
    val yearBn:      String get() = BanglaDateConverter.toBengaliNumeral(year)
    override fun toString() = "$dayBn $monthName $yearBn"
}

data class CalendarDay(
    val gregorianDate: LocalDate,
    val banglaDate:    BanglaDate,
    val isToday:       Boolean,
    val isFriday:      Boolean,
    val isSaturday:    Boolean,
    val holidayName:   String? = null
)
