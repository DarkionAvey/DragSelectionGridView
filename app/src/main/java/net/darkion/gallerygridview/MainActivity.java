package net.darkion.gallerygridview;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import net.darkion.gallerygridview.MultiSelectGridView.SelectionListener;

import java.util.ArrayList;

import static net.darkion.gallerygridview.FlyOutMenu.FlyOutMenus;
import static net.darkion.gallerygridview.R.id.delete;
import static net.darkion.gallerygridview.R.id.share;

/**
 * Created by DarkionAvey
 */
public class MainActivity extends Activity {
    FlyOutMenu flyOut;
    MultiSelectGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (MultiSelectGridView) findViewById(R.id.gridView);
        gridView.setAdapter(new GridAdapter(getApplicationContext(), 100));
        flyOut = (FlyOutMenu) findViewById(R.id.contextFlyOut);
        final int gridPaddingBottom = getResources().getDimensionPixelOffset(R.dimen.grid_padding_bottom);
        final TextView selectionCount = (TextView) findViewById(R.id.selectionCount);
        final View cancelSelection = findViewById(R.id.cancelSelection);
        cancelSelection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.cancelSelection();
            }
        });
        flyOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        flyOut.findViewById(delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flyOut.setFlyOutMenu(v.getId(), FlyOutMenus.DELETE);
            }
        });
        flyOut.findViewById(share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        flyOut.setTranslationY(200);
        gridView.setSelectionListener(new SelectionListener() {
            @Override
            public void onSelected(ArrayList<Integer> selection) {
                selectionCount.setText(String.valueOf(selection.size()));
            }

            @Override
            public void onStartedSelection() {
                flyOut.show();
                ValueAnimator animator = ValueAnimator.ofInt(0, gridPaddingBottom);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        gridView.setPadding(gridView.getPaddingLeft(), gridView.getPaddingTop(), gridView.getPaddingRight(), (int) animation.getAnimatedValue());
                    }
                });

                animator.setInterpolator(GridViewItem.interpolator);
                animator.start();
            }

            @Override
            public void onDoneSelection() {
                flyOut.reset();
                ValueAnimator animator = ValueAnimator.ofInt(gridPaddingBottom, 0);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        gridView.setPadding(gridView.getPaddingLeft(), gridView.getPaddingTop(), gridView.getPaddingRight(), (int) animation.getAnimatedValue());
                    }
                });

                animator.setInterpolator(GridViewItem.interpolator);
                animator.start();

            }
        });
    }
}
