package vaibhav.android.com.recycledcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarDisplayFragment.OnDayClickInterface {
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private CalendarTabsPagerAdapter calendarTabsPagerAdapter;
  private int tabHeight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewPager = (ViewPager) findViewById(R.id.view_pager);
    tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.removeAllTabs();
    //Onward
    TabLayout.Tab tab = tabLayout.newTab();
    RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tab_custom_view, tabLayout, false);
    TextView tabTitleView = (TextView) relativeLayout.findViewById(R.id.tab_text);
    tabTitleView.setText("Onward");
//    ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
//    tabHeight = params.height;
//    relativeLayout.setMinimumHeight(tabHeight);

    tab.setCustomView(relativeLayout);
    tabLayout.addTab(tab);

    //Return
    tab = tabLayout.newTab();
    relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tab_custom_view, tabLayout, false);
    tabTitleView = (TextView) relativeLayout.findViewById(R.id.tab_text);
    tabTitleView.setText("Return");
//    relativeLayout.setMinimumHeight(tabHeight);
    tab.setCustomView(relativeLayout);
    tabLayout.addTab(tab);

    calendarTabsPagerAdapter = new CalendarTabsPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(calendarTabsPagerAdapter);
    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    viewPager.setCurrentItem(0, true);
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(final TabLayout.Tab tab) {
        final int currentItem = tab.getPosition();
        viewPager.setCurrentItem(currentItem, true);
      }

      @Override
      public void onTabUnselected(final TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(final TabLayout.Tab tab) {
      }
    });
  }

  @Override
  public void onBackPressed() {
    if (viewPager.getCurrentItem() > 0) {
      viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }
  }

  @Override
  public void changeTabTitle(String dateString, int fragmentPosition) {
    TabLayout.Tab tab = tabLayout.getTabAt(fragmentPosition);
    RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
    TextView tabDateView = (TextView) relativeLayout.findViewById(R.id.date_text);
    TextView tabMonthView = (TextView) relativeLayout.findViewById(R.id.month_text);
    tabDateView.setText(dateString.split(" ", 2)[0]);
    tabMonthView.setText(dateString.split(" ", 2)[1]);
    tab.setCustomView(relativeLayout);
  }

  public class CalendarTabsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> registeredFragments = new ArrayList<>();

    public CalendarTabsPagerAdapter(final FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(final int pos) {
      Fragment fragment;
      fragment = CalendarDisplayFragment.newInstance(pos);
      registeredFragments.add(pos, fragment);
      return fragment;
    }

    @Override
    public int getCount() {
      return tabLayout.getTabCount();
    }

    public List<Fragment> getRegisteredFragmentsList() {
      return registeredFragments;
    }

    @Nullable
    public Fragment getRegisteredFragment(final int position) {
      final Fragment wr = registeredFragments.get(position);
      if (wr != null) {
        return wr;
      } else {
        return null;
      }
    }

    @Override
    public void notifyDataSetChanged() {
      super.notifyDataSetChanged();
    }
  }
}