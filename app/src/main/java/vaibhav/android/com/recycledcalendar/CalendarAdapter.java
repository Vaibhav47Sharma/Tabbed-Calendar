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
  private static final int FOUR_ROW_ITEM = 4;
  private static final int FIVE_ROW_ITEM = 5;
  private static final int SIX_ROW_ITEM = 6;

  public CalendarAdapter(Context context, List<CalendarDateBean> calendarDateBeanList, ClickHandler clickHandler, HashSet<Date> events) {
    this.context = context;
    this.calendarDateBeanList = calendarDateBeanList;
    this.clickHandler = clickHandler;
    this.events = events;
  }

  @Override
  public int getItemViewType(int position) {
    int numberOfRows = (int) Math.ceil((double) calendarDateBeanList.get(position).getCells().size()/ 7);
    int viewType;
    switch (numberOfRows) {
      case 4: viewType = FOUR_ROW_ITEM;
        break;
      case 5: viewType = FIVE_ROW_ITEM;
        break;
      case 6: viewType = SIX_ROW_ITEM;
        break;
      default: viewType = FOUR_ROW_ITEM;
        break;
    }
    return viewType;
  }

  @Override
  public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.calendar_row_item, parent, false);
    GridView grid = (GridView) view.findViewById(R.id.calendar_grid);
    ViewGroup.LayoutParams params = grid.getLayoutParams();
    int rowHeight = context.getResources().getDimensionPixelSize(R.dimen.calendar_date_text_view_height);
    int totalGridHeight;
    switch (viewType) {
      case FOUR_ROW_ITEM: totalGridHeight = FOUR_ROW_ITEM * rowHeight;
        break;
      case FIVE_ROW_ITEM: totalGridHeight = FIVE_ROW_ITEM * rowHeight;
        break;
      case SIX_ROW_ITEM: totalGridHeight = SIX_ROW_ITEM * rowHeight;
        break;
      default: totalGridHeight = FOUR_ROW_ITEM * rowHeight;
        break;
    }
    params.height = totalGridHeight;
    grid.setLayoutParams(params);
    view.setMinimumHeight(totalGridHeight);
    CalendarViewHolder calendarViewHolder = new CalendarViewHolder(view);
    return calendarViewHolder;
  }

  @Override
  public void onBindViewHolder(final CalendarViewHolder holder, final int position) {
    currentDate = calendarDateBeanList.get(position).getCalendar();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    holder.txtDate.setText(sdf.format(currentDate.getTime()));
    final ArrayList<Date> monthsdDates = calendarDateBeanList.get(position).getCells();
    CalendarMonthAdapter calendarMonthAdapter = new CalendarMonthAdapter(context, monthsdDates);
    holder.grid.setAdapter(calendarMonthAdapter);
    holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int insidePosition, long id) {
        Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());
        Calendar tempCalendar = Calendar.getInstance();
        Date clickedDate = monthsdDates.get(insidePosition);
        if (clickedDate != null) {
          tempCalendar.setTime(clickedDate);
          if (tempCalendar.get(Calendar.DATE) == todaysCalendar.get(Calendar.DATE) || clickedDate.compareTo(todaysCalendar.getTime()) == 1) {
            clickHandler.onDayPress(clickedDate);
            clickedFareDate = clickedDate;
            notifyDataSetChanged();
          }
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
    private int rowHeight;

    public CalendarMonthAdapter(Context context, ArrayList<Date> days) {
      this.eventDays = getEvents();
      inflater = LayoutInflater.from(context);
      this.days = days;
    }

    public int getRowHeight() {
      return rowHeight;
    }

    private void setRowHeight(int rowHeight) {
      if (rowHeight > 0) {
        this.rowHeight = rowHeight;
      }
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
      setRowHeight(view.getMeasuredHeight());
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
