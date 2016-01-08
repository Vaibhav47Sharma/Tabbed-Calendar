package vaibhav.android.com.recycledcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by vaibhavsharma on 08/01/16.
 */
public class CalendarView extends LinearLayout {
  // for logging
  private static final String LOGTAG = "Calendar View";

  // how many days to show, defaults to six weeks, 42 days
  private static final int DAYS_COUNT = 42;

  private View lowestFareDate;
  // default date format
  private static final String DATE_FORMAT = "MMM yyyy";

  // date format
  private String dateFormat;

  private Calendar currentDate = Calendar.getInstance(Locale.getDefault());

  //event handling
  private ClickHandler clickHandler;
  HashSet<Date> events;

  // internal components
  private LinearLayout header;
  private ImageView btnPrev;
  private ImageView btnNext;
  private TextView txtDate;
  private GridView grid;

  public CalendarView(Context context) {
    super(context);
  }

  public CalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initControl(context, attrs);
  }

  public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initControl(context, attrs);
  }

  /**
   * Load control xml layout
   */
  private void initControl(Context context, AttributeSet attrs) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.control_calendar, this);

//    loadDateFormat(attrs);
    assignUiElements();
    assignClickHandlers();

    updateCalendar();
  }

  private void loadDateFormat(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

    try {
      // try to load provided date format, and fallback to default otherwise
      dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
      if (dateFormat == null)
        dateFormat = DATE_FORMAT;
    } finally {
      ta.recycle();
    }
  }

  private void assignUiElements() {
    // layout is inflated, assign local variables to components
    header = (LinearLayout) findViewById(R.id.calendar_header);
    btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
    btnNext = (ImageView) findViewById(R.id.calendar_next_button);
    txtDate = (TextView) findViewById(R.id.calendar_date_display);
    grid = (GridView) findViewById(R.id.calendar_grid);
  }

  private void assignClickHandlers() {
    // add one month and refresh UI
    btnNext.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        currentDate.add(Calendar.MONTH, 1);
        updateCalendar();
      }
    });

    // subtract one month and refresh UI
    btnPrev.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar todaysCal = Calendar.getInstance(Locale.getDefault());
        Calendar tempCal = Calendar.getInstance(Locale.getDefault());
        tempCal.setTime(currentDate.getTime());
        tempCal.add(Calendar.MONTH, -1);
        if (todaysCal.get(Calendar.MONTH) <= tempCal.get(Calendar.MONTH) && todaysCal.get(Calendar.YEAR) == tempCal.get(Calendar.YEAR) || tempCal.get(Calendar.YEAR) > todaysCal.get(Calendar.YEAR)) {
          currentDate.add(Calendar.MONTH, -1);
          updateCalendar();
        }
      }
    });

    // long-pressing a day
    grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lowestFareDate != null) {
          lowestFareDate.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        lowestFareDate = view;
        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        clickHandler.onDayPress((Date) parent.getItemAtPosition(position));
      }
    });
  }

  public HashSet<Date> getEvents() {
    return events;
  }

  public void addEvents(Date eventDate) {
    if (events == null) {
      events = new HashSet<>();
    }
    events.add(eventDate);
    updateCalendar();
  }

  /**
   * Display dates correctly in grid
   */
  public void updateCalendar() {
    ArrayList<Date> cells = new ArrayList<>();
    Calendar calendar = (Calendar) currentDate.clone();

    // determine the cell for current month's beginning
    calendar.set(Calendar.DATE, 1);
    int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

    // move calendar backwards to the beginning of the week
    calendar.add(Calendar.DATE, -monthBeginningCell);

    // fill cells
    while (cells.size() < DAYS_COUNT) {
      cells.add(calendar.getTime());
      calendar.add(Calendar.DATE, 1);
    }

    // update grid
    grid.setAdapter(new CalendarAdapter(getContext(), cells));

    // update title
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    txtDate.setText(sdf.format(currentDate.getTime()));

    // set header color according to current season
//    int month = currentDate.get(Calendar.MONTH);
//    int season = monthSeason[month];
//    int color = rainbow[season];

//    header.setBackgroundColor(getResources().getColor(color));
  }


  private class CalendarAdapter extends ArrayAdapter<Date> {
    // days with events
    private HashSet<Date> eventDays;

    // for view inflation
    private LayoutInflater inflater;

    public CalendarAdapter(Context context, ArrayList<Date> days) {
      super(context, R.layout.control_calendar_day, days);
      this.eventDays = getEvents();
      inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      // day in question
      Date date = getItem(position);
      Calendar currentPositionCalandar = Calendar.getInstance(Locale.getDefault());
      currentPositionCalandar.setTime(date);
      int day = currentPositionCalandar.get(Calendar.DATE);
      int month = currentPositionCalandar.get(Calendar.MONTH);
      int year = currentPositionCalandar.get(Calendar.YEAR);

      // today
      Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());

      // inflate item if it does not exist yet
      if (view == null) {
        view = inflater.inflate(R.layout.control_calendar_day, parent, false);
      }

      Calendar tempCalandar = Calendar.getInstance(Locale.getDefault());
      // if this day has an event, specify event image
      view.setBackgroundResource(0);
      if (eventDays != null) {
        for (Date eventDate : eventDays) {
          tempCalandar.setTime(eventDate);
          if (tempCalandar.get(Calendar.DATE) == day && tempCalandar.get(Calendar.MONTH) == month && tempCalandar.get(Calendar.YEAR) == year) {
            // mark this day for event
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            break;
          }
        }
      }

      // clear styling
      ((TextView) view).setTypeface(null, Typeface.NORMAL);
      ((TextView) view).setTextColor(Color.BLACK);

      if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR)) {
        // if this day is outside current month, grey it out
        ((TextView) view).setTextColor(getResources().getColor(R.color.greyed_out));
      } else if (day == todaysCalendar.get(Calendar.DATE) && month == todaysCalendar.get(Calendar.MONTH) && day == todaysCalendar.get(Calendar.YEAR)) {
        // if it is today, set it to blue/bold
        ((TextView) view).setTypeface(null, Typeface.BOLD);
        ((TextView) view).setTextColor(getResources().getColor(R.color.today));
      }

//       set text
      if (month == currentDate.get(Calendar.MONTH)) {
        ((TextView) view).setText(String.valueOf(currentPositionCalandar.get(Calendar.DATE)));
      } else {
        view.setVisibility(GONE);
      }

      return view;
    }
  }

  /**
   * Assign event handler to be passed needed events
   */
  public void setClickHandler(ClickHandler clickHandler) {
    this.clickHandler = clickHandler;
  }

  /**
   * This interface defines what events to be reported to
   * the outside world
   */
  public interface ClickHandler {
    void onDayPress(Date date);
  }
}

