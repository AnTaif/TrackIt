package com.example.trackit.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackit.ui.theme.TrackItTheme
import io.github.boguszpawlowski.composecalendar.*
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import io.github.boguszpawlowski.composecalendar.week.Week
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ExpandableCalendar(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }

    val selectionState = remember {
        DynamicSelectionState(
            selection = emptyList(), selectionMode = SelectionMode.Single
        )
    }

    if (!expanded){
        val calendarState = rememberSelectableWeekCalendarState(
            selectionState = selectionState,
            initialWeek = getWeekFromDate(selectionState.selection)
        )

        CalendarCard(modifier.clickable { expanded = !expanded },
            cardContent = {
                SelectableWeekCalendar( calendarState = calendarState, weekHeader = {})
            },
            expandIcon = { ExpandIcon(expanded) }
        )

        if (calendarState.selectionState.selection.isNotEmpty()){
            onDateSelected(calendarState.selectionState.selection[0])
        }
    }
    else {
        val calendarState = rememberSelectableCalendarState(
            selectionState = selectionState,
            initialMonth = getMonthFromDate(selectionState.selection)
        )

        CalendarCard(modifier.clickable { expanded = !expanded },
            cardContent = {
                SelectableCalendar(calendarState = calendarState)
            },
            expandIcon = { ExpandIcon(expanded) }
        )

        if (calendarState.selectionState.selection.isNotEmpty()){
            onDateSelected(calendarState.selectionState.selection[0])
        }
    }
}

@Composable
private fun CalendarCard(
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit,
    expandIcon: @Composable () -> Unit
    ){
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            expandIcon()
            cardContent()
        }
    }
}

@Composable
private fun ExpandIcon(expanded: Boolean){
    val icon = if(!expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown
    Icon(icon, contentDescription = null)
}

private fun getMonthFromDate(dateList: List<LocalDate>): YearMonth {
    if (dateList.isNotEmpty()){
        val date = dateList[0]
        return YearMonth.from(date)

    }
    return YearMonth.now()
}

private fun getWeekFromDate(dateList: List<LocalDate>): Week {
    if (dateList.isNotEmpty()){
        val date = dateList[0]
        val monday = date.minusDays((date.dayOfWeek.value - 1).toLong())
        val weekList = List<LocalDate> (7) { monday.plusDays(it.toLong()) }

        return Week(weekList)
    }
    return Week.now()
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview(){
    TrackItTheme {
        CalendarCard(Modifier.clickable {  },
            cardContent = { SelectableCalendar() },
            expandIcon = { ExpandIcon(true) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarWeekPreview(){
    TrackItTheme {
        CalendarCard(Modifier.clickable {  },
            cardContent = { SelectableWeekCalendar( weekHeader = {}) },
            expandIcon = { ExpandIcon(false) }
        )
    }
}