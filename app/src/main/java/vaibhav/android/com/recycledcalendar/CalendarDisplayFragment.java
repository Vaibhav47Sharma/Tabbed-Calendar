package vaibhav.android.com.recycledcalendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarDisplayFragment extends Fragment implements CalendarAdapter.ClickHandler {
  private RecyclerView calendarRecyclerView;

  public CalendarDisplayFragment() {
    // Required empty public constructor
  }

  public static CalendarDisplayFragment newInstance() {
    return new CalendarDisplayFragment();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.calendar_display_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    calendarRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    HashSet<Date> events = new HashSet<>();
    Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());
    events.add(todaysCalendar.getTime());
    List<CalendarDateBean> calendarDateBeanList = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      CalendarDateBean calendarDateBean = new CalendarDateBean(todaysCalendar);
      calendarDateBeanList.add(calendarDateBean);
      todaysCalendar.add(Calendar.MONTH, 1);
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    calendarRecyclerView.setLayoutManager(linearLayoutManager);
    CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), calendarDateBeanList, this, events);
    calendarRecyclerView.setAdapter(calendarAdapter);
  }

  @Override
  public void onDayPress(Date date) {
    DateFormat df = SimpleDateFormat.getDateInstance();
    Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
  }
}
