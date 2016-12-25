package net.darkion.gallerygridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by DarkionAvey
 */

public class FlyOutMenu extends LinearLayout {
    public FlyOutMenu(Context context) {
        super(context);
    }

    public FlyOutMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyOutMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public enum FlyOutMenus {
        DELETE, SHARE, ROTATE
    }

    OnClickListener parkedOnClickListener;
    ImageButtonWithListener icon;

    public void setFlyOutMenu(int icon, final FlyOutMenus menus) {
        AutoTransition transition = new AutoTransition();
        transition.setInterpolator(GridViewItem.interpolator);
        transition.excludeTarget(icon, true);
        transition.setDuration(100);
        transition.excludeChildren(icon, true);
        TransitionManager.beginDelayedTransition(this);

        View child;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            if (child.getId() != icon) {
                child.setVisibility(View.GONE);
            } else {
                if (child instanceof ImageButtonWithListener) {
                    this.icon = (ImageButtonWithListener) child;
                    parkedOnClickListener = this.icon.getOnClickListener();
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToMainFlyOutMenu();
                        }
                    });

                }
            }
        }
        final View attachedView = findViewById(net.darkion.gallerygridview.R.id.options);
        attachedView.setVisibility(View.VISIBLE);
        Button positive = (Button) attachedView.findViewById(net.darkion.gallerygridview.R.id.positive);
        Button negative = (Button) attachedView.findViewById(net.darkion.gallerygridview.R.id.negative);

        negative.setVisibility(View.VISIBLE);
        negative.setVisibility(View.VISIBLE);
        switch (menus) {
            case DELETE:
                positive.setText("Confirm");
                negative.setText("Cancel");
                break;


        }
        positive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (menus) {
                    case DELETE:
                        reset();
                        break;
                }
            }
        });

        negative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainFlyOutMenu();
            }
        });


    }

    public void goToMainFlyOutMenu() {

        TransitionManager.beginDelayedTransition(this);
        icon.setOnClickListener(parkedOnClickListener);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.VISIBLE);
        }
        findViewById(net.darkion.gallerygridview.R.id.options).setVisibility(View.GONE);


    }

    public void show() {
        final float verticalSpace = getResources().getDimension(net.darkion.gallerygridview.R.dimen.activity_vertical_margin);
if(getTranslationY()==-verticalSpace)return;
        animate().translationY(-verticalSpace).setInterpolator(GridViewItem.interpolator).start();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setAlpha(0f);
            getChildAt(i).setTranslationY(20f);
            getChildAt(i).animate().alpha(1f).translationY(0f).setStartDelay((i + 1) * 50).setInterpolator(GridViewItem.interpolator).start();
        }
    }

    public void reset() {
        animate().translationY(200).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View child;
                for (int i = 0; i < getChildCount(); i++) {
                    child = getChildAt(i);
                    if (net.darkion.gallerygridview.R.id.options == child.getId()) child.setVisibility(View.GONE);
                    else {
                        child.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).setInterpolator(GridViewItem.interpolator).start();
    }
}
