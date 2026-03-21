package com.banglacalendar

import com.banglacalendar.domain.model.BanglaDateConverter
import com.banglacalendar.domain.model.BanglaDate
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class BanglaDateConverterTest {

    // ── Month boundary tests ──────────────────────────────────────────────────

    @Test fun `1 Baishakh is April 14`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 4, 14))
        assertEquals(1, b.day); assertEquals(0, b.month); assertEquals(1432, b.year)
    }

    @Test fun `1 Jaistha is May 15`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 5, 15))
        assertEquals(1, b.day); assertEquals(1, b.month)
    }

    @Test fun `1 Asharh is June 15`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 6, 15))
        assertEquals(1, b.day); assertEquals(2, b.month)
    }

    @Test fun `1 Srabon is July 16`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 7, 16))
        assertEquals(1, b.day); assertEquals(3, b.month)
    }

    @Test fun `1 Bhadra is August 16`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 8, 16))
        assertEquals(1, b.day); assertEquals(4, b.month)
    }

    @Test fun `1 Ashwin is September 16`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 9, 16))
        assertEquals(1, b.day); assertEquals(5, b.month)
    }

    @Test fun `1 Kartik is October 16`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 10, 16))
        assertEquals(1, b.day); assertEquals(6, b.month)
    }

    @Test fun `1 Agrahayan is November 15`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 11, 15))
        assertEquals(1, b.day); assertEquals(7, b.month)
    }

    @Test fun `1 Poush is December 15`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 12, 15))
        assertEquals(1, b.day); assertEquals(8, b.month)
    }

    @Test fun `1 Magh is January 13 next Gregorian year`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2026, 1, 13))
        assertEquals(1, b.day); assertEquals(9, b.month); assertEquals(1432, b.year)
    }

    @Test fun `1 Falgun is February 13`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2026, 2, 13))
        assertEquals(1, b.day); assertEquals(10, b.month); assertEquals(1432, b.year)
    }

    @Test fun `1 Chaitra is March 14`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2026, 3, 14))
        assertEquals(1, b.day); assertEquals(11, b.month); assertEquals(1432, b.year)
    }

    // ── Year boundary ─────────────────────────────────────────────────────────

    @Test fun `April 13 is last day of previous Bangla year`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 4, 13))
        assertEquals(1431, b.year); assertEquals(11, b.month)
    }

    @Test fun `Year rolls over on April 14`() {
        val before = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 4, 13))
        val after  = BanglaDateConverter.toBanglaDate(LocalDate.of(2025, 4, 14))
        assertEquals(1431, before.year)
        assertEquals(1432, after.year)
    }

    @Test fun `Bangla year 1400 is April 14 1993`() {
        val b = BanglaDateConverter.toBanglaDate(LocalDate.of(1993, 4, 14))
        assertEquals(1400, b.year); assertEquals(0, b.month); assertEquals(1, b.day)
    }

    // ── Leap year ─────────────────────────────────────────────────────────────

    @Test fun `1407 is leap (Gregorian 2000 is leap)`() =
        assertTrue(BanglaDateConverter.isBanglaLeapYear(1407))

    @Test fun `1406 is not leap (Gregorian 1999)`() =
        assertFalse(BanglaDateConverter.isBanglaLeapYear(1406))

    @Test fun `Chaitra has 31 days in leap year 1407`() =
        assertEquals(31, BanglaDateConverter.getDaysInMonth(11, 1407))

    @Test fun `Chaitra has 30 days in non-leap year 1406`() =
        assertEquals(30, BanglaDateConverter.getDaysInMonth(11, 1406))

    @Test fun `First 5 months always 31 days`() {
        for (i in 0..4)
            assertEquals("Month $i", 31, BanglaDateConverter.getDaysInMonth(i, 1432))
    }

    @Test fun `Months 5-9 always 30 days`() {
        for (i in 5..9)
            assertEquals("Month $i", 30, BanglaDateConverter.getDaysInMonth(i, 1432))
    }

    // ── Round-trip ────────────────────────────────────────────────────────────

    @Test fun `Round-trip every day of 2025`() {
        var d = LocalDate.of(2025, 1, 1)
        val end = LocalDate.of(2025, 12, 31)
        while (!d.isAfter(end)) {
            val back = BanglaDateConverter.toGregorianDate(BanglaDateConverter.toBanglaDate(d))
            assertEquals("Failed for $d", d, back)
            d = d.plusDays(1)
        }
    }

    @Test fun `Round-trip every day of 2024 (leap year)`() {
        var d = LocalDate.of(2024, 1, 1)
        val end = LocalDate.of(2024, 12, 31)
        while (!d.isAfter(end)) {
            val back = BanglaDateConverter.toGregorianDate(BanglaDateConverter.toBanglaDate(d))
            assertEquals("Failed for $d", d, back)
            d = d.plusDays(1)
        }
    }

    // ── Holidays ──────────────────────────────────────────────────────────────

    @Test fun `Pahela Baishakh April 14`() =
        assertEquals("পহেলা বৈশাখ", BanglaDateConverter.getHoliday(LocalDate.of(2025, 4, 14)))

    @Test fun `Shahid Dibosh February 21`() =
        assertEquals("শহীদ দিবস", BanglaDateConverter.getHoliday(LocalDate.of(2025, 2, 21)))

    @Test fun `Independence Day March 26`() =
        assertEquals("স্বাধীনতা দিবস", BanglaDateConverter.getHoliday(LocalDate.of(2025, 3, 26)))

    @Test fun `Victory Day December 16`() =
        assertEquals("বিজয় দিবস", BanglaDateConverter.getHoliday(LocalDate.of(2025, 12, 16)))

    @Test fun `Mourning Day August 15`() =
        assertEquals("জাতীয় শোক দিবস", BanglaDateConverter.getHoliday(LocalDate.of(2025, 8, 15)))

    @Test fun `Normal day returns null`() =
        assertNull(BanglaDateConverter.getHoliday(LocalDate.of(2025, 6, 10)))

    // ── Day of week ───────────────────────────────────────────────────────────

    @Test fun `Friday is index 0`() {
        val friday = LocalDate.of(2025, 1, 3)
        assertEquals(0, BanglaDateConverter.bdDayOfWeek(friday))
        assertTrue(BanglaDateConverter.isFriday(friday))
    }

    @Test fun `Saturday is index 1`() {
        val saturday = LocalDate.of(2025, 1, 4)
        assertEquals(1, BanglaDateConverter.bdDayOfWeek(saturday))
        assertTrue(BanglaDateConverter.isSaturday(saturday))
    }

    // ── Bengali numerals ──────────────────────────────────────────────────────

    @Test fun `Bengali numerals correct`() {
        assertEquals("১৪৩২", BanglaDateConverter.toBengaliNumeral(1432))
        assertEquals("০",    BanglaDateConverter.toBengaliNumeral(0))
        assertEquals("৩১",   BanglaDateConverter.toBengaliNumeral(31))
    }
}
