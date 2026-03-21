package com.banglacalendar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.banglacalendar.domain.model.BanglaDateConverter
import com.banglacalendar.domain.model.CalendarDay
import com.banglacalendar.ui.theme.BanglaRed
import com.banglacalendar.ui.theme.BanglaRedContainer
import com.banglacalendar.ui.theme.SaturdayBlue
import com.banglacalendar.ui.theme.SaturdayBlueDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(vm: CalendarViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    val isDark = isSystemInDarkTheme()
    val fridayColor   = if (isDark) Color(0xFFFFB4AB) else BanglaRed
    val saturdayColor = if (isDark) SaturdayBlueDark  else SaturdayBlue

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "বাংলা ক্যালেন্ডার",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        state.todayDate?.let {
                            Text(
                                "আজ: $it",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BanglaRed),
                actions = {
                    TextButton(onClick = { vm.goToToday() }) {
                        Text("আজকে", color = Color.White, fontSize = 14.sp)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Season badge
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        state.seasonLabel,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            // Month navigation
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { vm.previousMonth() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "আগের মাস",
                        tint = BanglaRed,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text(
                    state.monthTitle,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BanglaRed
                )
                IconButton(onClick = { vm.nextMonth() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "পরের মাস",
                        tint = BanglaRed,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Weekday header row
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BanglaDateConverter.DAY_NAMES_SHORT.forEachIndexed { idx, name ->
                    Text(
                        name,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (idx) {
                            0    -> fridayColor
                            1    -> saturdayColor
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Divider(
                Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            // Calendar grid
            state.calendarMonth?.let { month ->
                val rows = (month.days.size + 6) / 7
                Column(Modifier.padding(horizontal = 8.dp)) {
                    for (row in 0 until rows) {
                        Row(Modifier.fillMaxWidth()) {
                            for (col in 0 until 7) {
                                val idx = row * 7 + col
                                val day = month.days.getOrNull(idx)
                                Box(Modifier.weight(1f)) {
                                    if (day == null) {
                                        Spacer(Modifier.fillMaxWidth().height(70.dp))
                                    } else {
                                        DayCell(
                                            day           = day,
                                            fridayColor   = fridayColor,
                                            saturdayColor = saturdayColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Legend card
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        "রঙের অর্থ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(6.dp))
                    LegendRow(color = fridayColor,   label = "শুক্রবার — সরকারি ছুটি")
                    LegendRow(color = saturdayColor, label = "শনিবার")
                    LegendRow(color = BanglaRed, label = "আজকের তারিখ", bordered = true)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DayCell(day: CalendarDay, fridayColor: Color, saturdayColor: Color) {
    val textColor = when {
        day.isFriday   -> fridayColor
        day.isSaturday -> saturdayColor
        else           -> MaterialTheme.colorScheme.onSurface
    }
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (day.isToday) BanglaRedContainer else Color.Transparent)
            .border(
                width = if (day.isToday) 1.5.dp else 0.5.dp,
                color = if (day.isToday) BanglaRed else MaterialTheme.colorScheme.outline.copy(0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(vertical = 3.dp, horizontal = 1.dp)
            .height(66.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Bengali date — large
            Text(
                day.banglaDate.dayBn,
                fontSize = 18.sp,
                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.SemiBold,
                color = if (day.isToday) BanglaRed else textColor,
                textAlign = TextAlign.Center
            )
            // Gregorian date — small
            Text(
                day.gregorianDate.dayOfMonth.toString(),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            // Holiday label
            if (day.holidayName != null) {
                Text(
                    day.holidayName,
                    fontSize = 7.sp,
                    color = BanglaRed,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 8.sp
                )
            }
            // Today dot
            if (day.isToday) {
                Spacer(Modifier.height(2.dp))
                Box(
                    Modifier.size(4.dp).clip(CircleShape).background(BanglaRed)
                )
            }
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String, bordered: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Box(
            Modifier
                .size(11.dp)
                .clip(CircleShape)
                .background(if (bordered) BanglaRedContainer else color)
                .then(if (bordered) Modifier.border(1.5.dp, color, CircleShape) else Modifier)
        )
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Divider(modifier: Modifier = Modifier, color: Color = Color.Gray, thickness: Dp = 1.dp) {
    Box(modifier.fillMaxWidth().height(thickness).background(color))
}
