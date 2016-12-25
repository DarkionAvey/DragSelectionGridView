package net.darkion.gallerygridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by DarkionAvey
 */

public class ImageButtonWithListener extends ImageButton {
    OnClickListener onClickListener;

    public void setDrawable(final Drawable img) {
        animate().scaleX(0f).scaleY(0f).setDuration(100).alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setImageDrawable(img);
                animate().alpha(1f).scaleX(1f).scaleY(1f).start();
            }
        }).start();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
    }

    public ImageButtonWithListener(Context context) {
        super(context);
    }

    public ImageButtonWithListener(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButtonWithListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
