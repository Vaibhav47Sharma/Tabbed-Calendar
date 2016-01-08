package vaibhav.android.com.recycledcalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by vaibhavsharma on 08/01/16.
 */
public class CalendarDateBean {
  Calendar currentPositionCalendar = Calendar.getInstance(Locale.getDefault());
  Calendar calendar = Calendar.getInstance(Locale.getDefault());
  private ArrayList<Date> cells = new ArrayList<>();
  private static final int DAYS_COUNT = 42;
  private HashSet<Date> events;


  public CalendarDateBean(Calendar currentDate) {
    currentPositionCalendar.setTime(currentDate.getTime());
    calendar.setTime(currentDate.getTime());
    // determine the cell for current month's beginning
    currentPositionCalendar.set(Calendar.DATE, 1);
    int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

    // move calendar backwards to the beginning of the week
    currentPositionCalendar.add(Calendar.DATE, -monthBeginningCell);

    // fill cells
    while (cells.size() < DAYS_COUNT) {
      cells.add(currentPositionCalendar.getTime());
      currentPositionCalendar.add(Calendar.DATE, 1);
    }
  }

  public ArrayList<Date> getCells() {
    return cells;
  }

  public Calendar getCalendar() {
    return calendar;
  }
}
