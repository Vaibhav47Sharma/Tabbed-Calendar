package vaibhav.android.com.recycledcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by vaibhavsharma on 08/01/16.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
  private final Context context;
  private final List<CalendarDateBean> calendarDateBeanList;
  private final ClickHandler clickHandler;
  private final HashSet<Date> events;
  private static final String DATE_FORMAT = "MMM yyyy";
  private Date clickedFareDate;
  private Calendar currentDate;

  public CalendarAdapter(Context context, List<CalendarDateBean> calendarDateBeanList, ClickHandler clickHandler, HashSet<Date> events) {
    this.context = context;
    this.calendarDateBeanList = calendarDateBeanList;
    this.clickHandler = clickHandler;
    this.events = events;
  }

  @Override
  public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.calendar_row_item, parent, false);
    CalendarViewHolder calendarViewHolder = new CalendarViewHolder(view);
    return calendarViewHolder;
  }

  @Override
  public void onBindViewHolder(final CalendarViewHolder holder, final int position) {
    currentDate = calendarDateBeanList.get(position).getCalendar();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    holder.txtDate.setText(sdf.format(currentDate.getTime()));
    CalendarMonthAdapter calendarMonthAdapter = new CalendarMonthAdapter(context, calendarDateBeanList.get(position).getCells());
    holder.grid.setAdapter(calendarMonthAdapter);

    holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int insidePosition, long id) {
        Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());
        Calendar tempCalendar = Calendar.getInstance();
        Date clickedDate = calendarDateBeanList.get(position).getCells().get(insidePosition);
        tempCalendar.setTime(clickedDate);
        if (clickedDate != null && (tempCalendar.get(Calendar.DATE) == todaysCalendar.get(Calendar.DATE) || clickedDate.compareTo(todaysCalendar.getTime()) == 1)) {
          clickHandler.onDayPress(clickedDate);
          clickedFareDate = clickedDate;
          notifyDataSetChanged();
        }
      }
    });
  }

  public HashSet<Date> getEvents() {
    return events;
  }

  private class CalendarMonthAdapter extends BaseAdapter {
    // days with events
    private HashSet<Date> eventDays;
    // for view inflation
    private LayoutInflater inflater;
    private final ArrayList<Date> days;

    public CalendarMonthAdapter(Context context, ArrayList<Date> days) {
      this.eventDays = getEvents();
      inflater = LayoutInflater.from(context);
      this.days = days;
    }

    @Override
    public View getView(final int insidePosition, View view, ViewGroup parent) {
      DateHolder dateHolder;

      if (view == null) {
        view = inflater.inflate(R.layout.control_calendar_day, parent, false);
        dateHolder = new DateHolder();
        dateHolder.date = (TextView) view.findViewById(R.id.date);
        view.setTag(dateHolder);
      } else {
        dateHolder = (DateHolder) view.getTag();
      }
      if (days.get(insidePosition) != null) {
        // day in question
        final Date date = days.get(insidePosition);
        final Calendar currentPositionCalandar = Calendar.getInstance(Locale.getDefault());
        currentPositionCalandar.setTime(date);
        int day = currentPositionCalandar.get(Calendar.DATE);
        int month = currentPositionCalandar.get(Calendar.MONTH);
        int year = currentPositionCalandar.get(Calendar.YEAR);

        // today
        Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());

        Calendar tempCalandar = Calendar.getInstance(Locale.getDefault());
        view.setBackgroundResource(0);

        dateHolder.date.setTypeface(null, Typeface.NORMAL);
        dateHolder.date.setTextColor(Color.BLACK);

//      if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR)) {
//        // if this day is outside current month, grey it out
//        dateHolder.date.setTextColor(context.getResources().getColor(R.color.greyed_out));
//      } else
        if (currentPositionCalandar.compareTo(todaysCalendar) == 0) {
          // if it is today, set it to blue/bold
          dateHolder.date.setTypeface(null, Typeface.BOLD);
          dateHolder.date.setTextColor(context.getResources().getColor(R.color.today));
        }

        if (month == currentDate.get(Calendar.MONTH)) {
          dateHolder.date.setText(String.valueOf(currentPositionCalandar.get(Calendar.DATE)));
        }
        if (currentPositionCalandar.getTime().compareTo(todaysCalendar.getTime()) == -1 && todaysCalendar.get(Calendar.DATE) != day) {
          dateHolder.date.setTextColor(context.getResources().getColor(R.color.greyed_out));
        }

        if (clickedFareDate != null && clickedFareDate.compareTo(currentPositionCalandar.getTime()) == 0) {
          view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
          view.setBackgroundColor(0);
          if (eventDays != null) {
            for (Date eventDate : eventDays) {
              tempCalandar.setTime(eventDate);
              if (tempCalandar.getTime().compareTo(currentPositionCalandar.getTime()) == 0) {
                // mark this day for event
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                break;
              }
            }
          }
        }

      }
      return view;
    }

    @Override
    public int getCount() {
      return days.size();
    }

    @Override
    public Object getItem(int position) {
      return position;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    class DateHolder {
      TextView date;
    }
  }

  @Override
  public int getItemCount() {
    return calendarDateBeanList.size();
  }

  public interface ClickHandler {
    void onDayPress(Date date);
  }

  static class CalendarViewHolder extends RecyclerView.ViewHolder {
    private final TextView txtDate;
    private final GridView grid;

    public CalendarViewHolder(View itemView) {
      super(itemView);
      txtDate = (TextView) itemView.findViewById(R.id.calendar_date_display);
      grid = (GridView) itemView.findViewById(R.id.calendar_grid);
    }
  }
}
