package vaibhav.android.com.recycledcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private CalendarTabsPagerAdapter calendarTabsPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewPager = (ViewPager) findViewById(R.id.view_pager);
    tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.removeAllTabs();
    tabLayout.addTab(tabLayout.newTab().setText("Onward"));
    tabLayout.addTab(tabLayout.newTab().setText("Return"));
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

  public class CalendarTabsPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> registeredFragments = new ArrayList<>();

    public CalendarTabsPagerAdapter(final FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(final int pos) {
      Fragment fragment;
      fragment = CalendarDisplayFragment.newInstance();
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