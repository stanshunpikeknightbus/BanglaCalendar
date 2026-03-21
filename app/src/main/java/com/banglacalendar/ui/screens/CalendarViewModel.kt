package com.banglacalendar.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banglacalendar.domain.model.BanglaDate
import com.banglacalendar.domain.model.BanglaDateConverter
import com.banglacalendar.domain.usecase.GetCalendarMonthUseCase
import com.banglacalendar.domain.usecase.GetTodayBanglaDateUseCase
import com.banglacalendar.domain.usecase.NavigateMonthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarUiState(
    val currentMonth:  Int  = 0,
    val currentYear:   Int  = 1432,
    val calendarMonth: GetCalendarMonthUseCase.CalendarMonth? = null,
    val todayDate:     BanglaDate? = null,
    val isLoading:     Boolean = true
) {
    val monthTitle: String get() = calendarMonth?.let {
        "${BanglaDateConverter.MONTH_NAMES[it.banglaMonth]} ${BanglaDateConverter.toBengaliNumeral(it.banglaYear)}"
    } ?: ""

    val seasonLabel: String get() = calendarMonth?.let {
        val i = BanglaDateConverter.getSeasonIndex(it.banglaMonth)
        "${BanglaDateConverter.SEASON_EMOJIS[i]}  ${BanglaDateConverter.SEASON_NAMES[i]}"
    } ?: ""
}

class CalendarViewModel : ViewModel() {

    private val getMonth   = GetCalendarMonthUseCase()
    private val getToday   = GetTodayBanglaDateUseCase()
    private val navigate   = NavigateMonthUseCase()

    private val _state = MutableStateFlow(CalendarUiState())
    val state: StateFlow<CalendarUiState> = _state.asStateFlow()

    init { goToToday() }

    fun goToToday() {
        viewModelScope.launch {
            val today = getToday()
            _state.update {
                it.copy(
                    currentMonth  = today.month,
                    currentYear   = today.year,
                    calendarMonth = getMonth(today.month, today.year),
                    todayDate     = today,
                    isLoading     = false
                )
            }
        }
    }

    fun previousMonth() {
        viewModelScope.launch {
            val s = _state.value
            val (m, y) = navigate.previous(s.currentMonth, s.currentYear)
            _state.update { it.copy(currentMonth = m, currentYear = y, calendarMonth = getMonth(m, y)) }
        }
    }

    fun nextMonth() {
        viewModelScope.launch {
            val s = _state.value
            val (m, y) = navigate.next(s.currentMonth, s.currentYear)
            _state.update { it.copy(currentMonth = m, currentYear = y, calendarMonth = getMonth(m, y)) }
        }
    }
}
