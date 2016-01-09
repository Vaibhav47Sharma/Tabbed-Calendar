package vaibhav.android.com.recycledcalendar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vaibhavsharma on 09/01/16.
 */
public class AlphaInAnimatingAdapter extends AnimationAdapter {
  private static final float DEFAULT_ALPHA_FROM = 0f;
  private final float mFrom;

  public AlphaInAnimatingAdapter(RecyclerView.Adapter adapter) {
    this(adapter, DEFAULT_ALPHA_FROM);
  }

  public AlphaInAnimatingAdapter(RecyclerView.Adapter adapter, float from) {
    super(adapter);
    mFrom = from;
  }

  @Override
  protected Animator[] getAnimators(View view) {
    return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
  }
}
