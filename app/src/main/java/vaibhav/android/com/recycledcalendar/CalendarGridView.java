package vaibhav.android.com.recycledcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by vaibhavsharma on 13/01/16.
 */
public class CalendarGridView extends GridView {
  public CalendarGridView(Context context) {
    super(context);
  }

  public CalendarGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CalendarGridView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
    int cellSize = widthMeasureSize / 7;
    // Remove any extra pixels since /7 is unlikely to give whole nums.
    widthMeasureSize = cellSize * 7;
    int totalHeight = 0;
    setSelection(0);
    final int rowWidthSpec = makeMeasureSpec(widthMeasureSize, EXACTLY);
    final int rowHeightSpec = makeMeasureSpec(cellSize, EXACTLY);
    for (int c = 0; c < getCount(); c++) {
      final View child = getSelectedView();
      measureChild(child, rowWidthSpec, rowHeightSpec);
      totalHeight += child.getMeasuredHeight();
    }
    final int measuredWidth = widthMeasureSize + 2; // Fudge factor to make the borders show up.
    setMeasuredDimension(measuredWidth, totalHeight);
  }
}
