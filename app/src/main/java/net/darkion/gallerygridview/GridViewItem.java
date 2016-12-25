package net.darkion.gallerygridview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by DarkionAvey
 */
public class GridViewItem extends FrameLayout {
    public GridViewItem(Context context) {
        super(context);

    }

    public GridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GridViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean init = false;
    ImageView checkBox;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        if (init) return;
        init = true;
        setBackground(new ColorDrawable(getResources().getColor(R.color.gridCheckedBackground)));

        checkBox = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item_checkbox, this, false);
        addView(checkBox);
        checkBox.setScaleX(0f);
        checkBox.setScaleY(0f);
        checkBox.setTranslationY(UNCHECKED_TRANSLATION);
        checkBox.setTranslationX(UNCHECKED_TRANSLATION);

    }

    public boolean isChecked() {
        return isChecked;
    }

    private boolean isChecked = false;
    private float CHECKED_SCALE = 0.85f;
    private int ANIMATION_DURATION = 200;
    private final float UNCHECKED_TRANSLATION = -50f;
    public static final LogDecelerateInterpolator interpolator = new LogDecelerateInterpolator(60, 0);

    public void toggle() {
        setChecked(!isChecked(), true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) return;
        View image = getChildAt(0);

        if (checked) {
            if(animated) {
                image.animate().setInterpolator(interpolator).scaleY(CHECKED_SCALE).scaleX(CHECKED_SCALE).setDuration(ANIMATION_DURATION).start();
                checkBox.animate().scaleX(1f).setInterpolator(interpolator).setDuration(ANIMATION_DURATION).scaleY(1f).translationY(1f).translationX(1f).start();
            }
            else {
                image.setScaleX(CHECKED_SCALE);
                image.setScaleY(CHECKED_SCALE);
                checkBox.setScaleY(1f);
                checkBox.setScaleX(1f);
                checkBox.setTranslationY(1f);
                checkBox.setTranslationX(1f);
            }
        } else {
            if(animated) {
                image.animate().scaleY(1f).setInterpolator(interpolator).scaleX(1f).setDuration(ANIMATION_DURATION).start();
                checkBox.animate().scaleX(0f).scaleY(0f).setInterpolator(interpolator).setDuration(ANIMATION_DURATION).translationY(UNCHECKED_TRANSLATION).translationX(UNCHECKED_TRANSLATION).start();
            }
            else {
                image.setScaleX(1f);
                image.setScaleY(1f);
                checkBox.setScaleY(0f);
                checkBox.setScaleX(0f);
                checkBox.setTranslationY(UNCHECKED_TRANSLATION);
                checkBox.setTranslationX(UNCHECKED_TRANSLATION);
            }
        }
        //checkBox.setChecked(checked);

        isChecked = checked;

    }
}
