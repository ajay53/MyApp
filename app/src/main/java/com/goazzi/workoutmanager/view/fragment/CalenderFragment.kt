package com.goazzi.workoutmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import com.goazzi.workoutmanager.R
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan

import com.prolificinteractive.materialcalendarview.DayViewFacade

import com.prolificinteractive.materialcalendarview.DayViewDecorator


class CalenderFragment : Fragment() {

    companion object {
        private const val TAG = "CalenderFragment"
    }

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentActivity = requireActivity()
        applicationContext = fragmentActivity.applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViews(inflater.inflate(R.layout.fragment_calender, container, false))
        return inflater.inflate(R.layout.fragment_calender, container, false)
    }

    private fun initViews(root: View) {
        val calenderView = root.findViewById<MaterialCalendarView>(R.id.calender_view)

        val calender = Calendar.getInstance()
        val date: LocalDate = calender.time.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        val s = CalendarDay.from(2021, 10, 10)
        val today = CalendarDay.today()
        calenderView.setDateSelected(today, true)
        calenderView.currentDate = today

        val days = List<CalendarDay>(1) { today }
        calenderView.addDecorator(EventDecorator(applicationContext, R.color.button_color, days))
        /*val calender = root.findViewById<CalendarView>(R.id.calender)

        calender.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            // In this Listener have one method
            // and in this method we will
            // get the value of DAYS, MONTH, YEARS
            // Store the value of date with
            // format in String type Variable
            // Add 1 in month because month
            // index is start with 0
            val date = dayOfMonth.toString() + "-" + (month + 1) + "-" + year

            // set this date in TextView for Display
            Log.d(TAG, "initViews: date: $date")
        })*/
    }

    class EventDecorator(private val applicationContext: Context, private val color: Int, dates: Collection<CalendarDay?>?) :
        DayViewDecorator {
        private val dates: HashSet<CalendarDay> = HashSet(dates)
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return true
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(5F, color))
            view.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.current_session_background)!!)
        }

    }
}