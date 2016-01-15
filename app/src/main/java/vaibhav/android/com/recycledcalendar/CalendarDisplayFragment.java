package vaibhav.android.com.recycledcalendar;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
  private SnappyRecyclerView calendarRecyclerView;
  private int position;
  private OnDayClickInterface onDayClickInterface;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      onDayClickInterface = (OnDayClickInterface) context;
    } catch (ClassCastException e) {

    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      onDayClickInterface = (OnDayClickInterface) activity;
    } catch (ClassCastException e) {

    }
  }

  public CalendarDisplayFragment() {
    // Required empty public constructor
  }

  public static CalendarDisplayFragment newInstance(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt("position", position);
    CalendarDisplayFragment calendarDisplayFragment = new CalendarDisplayFragment();
    calendarDisplayFragment.setArguments(bundle);
    return calendarDisplayFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    position = getArguments().getInt("position");
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.calendar_display_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    calendarRecyclerView = (SnappyRecyclerView) view.findViewById(R.id.recycler_view);
    HashSet<Date> events = new HashSet<>();
    Calendar todaysCalendar = Calendar.getInstance(Locale.getDefault());
    events.add(todaysCalendar.getTime());
    List<CalendarDateBean> calendarDateBeanList = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      CalendarDateBean calendarDateBean = new CalendarDateBean(todaysCalendar);
      calendarDateBeanList.add(calendarDateBean);
      todaysCalendar.add(Calendar.MONTH, 1);
    }

//    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//    calendarRecyclerView.setLayoutManager(linearLayoutManager);
//    CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), calendarDateBeanList, this, events);
//    AlphaInAnimatingAdapter alphaInAnimatingAdapter = new AlphaInAnimatingAdapter(calendarAdapter);
//    alphaInAnimatingAdapter.setDuration(500);
//    alphaInAnimatingAdapter.setInterpolator(new OvershootInterpolator());
////    ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimatingAdapter);
//    ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(calendarAdapter);
//    scaleInAnimationAdapter.setDuration(500);
//    scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
//    calendarRecyclerView.setAdapter(alphaInAnimatingAdapter);

    SnappyLinearLayoutManager snappyLinearLayoutManager = new SnappyLinearLayoutManager(getActivity());
    snappyLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    calendarRecyclerView.setLayoutManager(snappyLinearLayoutManager);
    CalendarAdapter calendarAdapter = new CalendarAdapter(getActivity(), calendarDateBeanList, this, events);
    calendarRecyclerView.setAdapter(calendarAdapter);
  }

  @Override
  public void onDayPress(Date date) {
    DateFormat df = SimpleDateFormat.getDateInstance();
    SimpleDateFormat tabTextDateFormat = new SimpleDateFormat("dd MMM yyyy");
    onDayClickInterface.changeTabTitle(tabTextDateFormat.format(date), position);
    Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
  }

  public interface OnDayClickInterface {
    void changeTabTitle(String dateString, int fragmentPosition);
  }
}
