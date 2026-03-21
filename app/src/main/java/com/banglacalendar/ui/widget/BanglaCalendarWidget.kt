package com.banglacalendar.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.banglacalendar.domain.model.BanglaDateConverter
import java.time.LocalDate

class BanglaCalendarWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today      = LocalDate.now()
        val banglaDate = BanglaDateConverter.toBanglaDate(today)
        val dowIndex   = BanglaDateConverter.bdDayOfWeek(today)
        val dayName    = BanglaDateConverter.DAY_NAMES_FULL[dowIndex]
        val holiday    = BanglaDateConverter.getHoliday(today)

        provideContent {
            GlanceTheme {
                WidgetContent(
                    dayBn        = banglaDate.dayBn,
                    monthName    = banglaDate.monthName,
                    yearBn       = banglaDate.yearBn,
                    dayName      = dayName,
                    seasonLabel  = "${banglaDate.seasonEmoji} ${banglaDate.seasonName}",
                    gregDate     = "${today.dayOfMonth}/${today.monthValue}/${today.year}",
                    holidayName  = holiday
                )
            }
        }
    }
}

@Composable
private fun WidgetContent(
    dayBn:       String,
    monthName:   String,
    yearBn:      String,
    dayName:     String,
    seasonLabel: String,
    gregDate:    String,
    holidayName: String?
) {
    val red   = ColorProvider(Color(0xFFC0392B))
    val muted = ColorProvider(Color(0xFF7B6060))
    val dark  = ColorProvider(Color(0xFF1C1B1F))

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color.White))
            .padding(14.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = GlanceModifier.fillMaxSize()) {

            Text(
                seasonLabel,
                style = TextStyle(color = muted, fontSize = 10.sp)
            )

            Spacer(GlanceModifier.height(2.dp))

            Text(
                dayBn,
                style = TextStyle(color = red, fontSize = 52.sp, fontWeight = FontWeight.Bold)
            )

            Text(
                "$monthName $yearBn",
                style = TextStyle(color = red, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            )

            Spacer(GlanceModifier.height(4.dp))

            Text(dayName, style = TextStyle(color = dark, fontSize = 12.sp))

            Text(gregDate, style = TextStyle(color = muted, fontSize = 11.sp))

            if (holidayName != null) {
                Spacer(GlanceModifier.height(4.dp))
                Text(
                    "🎉 $holidayName",
                    style = TextStyle(color = red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

class BanglaCalendarWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BanglaCalendarWidget()
}
