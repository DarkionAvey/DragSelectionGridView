package net.darkion.gallerygridview;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by DarkionAvey
 */

public class MultiSelectGridView extends GridView {

    public void cancelSelection() {
        for (int i = 0; i < getAdapter().getCount(); i++) {
            if (findViewWithTag(i) != null)
                ((GridViewItem) findViewWithTag(i)).setChecked(false, true);
            notifyCheckedItem(i, false);
            dispatchListeners();

        }
    }

    private void dispatchListeners() {
        selectedItemsCount = getCheckedItemsNumber();
        selectionMode = (selectedItemsCount > 0);

        if (selectionListener != null) {

            if (selectedItemsCount == 0 && !touchDown) {
                selectionListener.onDoneSelection();
                dispatchedStartSelection = false;
            } else if (dispatchedStartSelection) selectionListener.onSelected(getCheckedItems());
            else {
                selectionListener.onStartedSelection();
                dispatchedStartSelection = true;
            }
        }

    }

    public MultiSelectGridView(Context context) {
        super(context);
    }

    public MultiSelectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public MultiSelectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initRects();

    }

    private void initRects() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayHeight = size.y;
        hotSpotTop = new Rect(0, 0, size.x, 100);
        hotSpotBottom = new Rect(0, displayHeight - 100, size.x, displayHeight);
    }

    Rect hotSpotTop, hotSpotBottom;

    int displayHeight;
    int mPosition;

    private boolean getCheckedItem(int position) {
        return ((GridAdapter) getAdapter()).getCheckedItem(position);
    }

    public ArrayList<Integer> getCheckedItems() {

        return ((GridAdapter) getAdapter()).getCheckedItems();
    }

    private int getCheckedItemsNumber() {

        return ((GridAdapter) getAdapter()).getCheckedItemsNumber();
    }

    boolean dispatchedStartSelection = false;

    private void notifyCheckedItem(int position, boolean checked) {
        dispatchListeners();
        ((GridAdapter) getAdapter()).setCheckedItem(position, checked);
    }

    boolean touchDown = false;

    class GridGesture extends GestureDetector.SimpleOnGestureListener {
        GridGesture() {
            super();
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (selectionMode && !touchDown) {

                int x = (int) e.getX();
                int y = (int) e.getY();
                int position = pointToPosition(x, y);
                if (position != GridView.INVALID_POSITION) {
                    mPosition = position;
                    //don't check mPosition == position because by the time onSingleTapUp is called, they're already equal

                    GridViewItem cellView = (GridViewItem) findViewWithTag(position);
                    cellView.setChecked(!cellView.isChecked(), true);
                    notifyCheckedItem(position, cellView.isChecked());

                }
                dispatchListeners();
                return true;
            }
            return false;

        }


        @Override
        public void onLongPress(MotionEvent e) {
            shouldSelect = true;


            touchDown = true;
            int x = (int) e.getX();
            int y = (int) e.getY();
            int position = pointToPosition(x, y);
            //don't check mPosition == position because by the time onLongPress is called, they're already equal
            if (position != GridView.INVALID_POSITION) {
                mPosition = position;
                GridViewItem cellView = (GridViewItem) findViewWithTag(position);
                cellView.setChecked(!cellView.isChecked(), true);
                notifyCheckedItem(position, cellView.isChecked());

            }
            dispatchListeners();
        }


    }


    GestureDetector detector;
    int selectedItemsCount = 0;
    boolean checkedCycle = true, selectionMode = false, isScrolling = false, shouldSelect = false;
    boolean startingImageStatus = false;

    float previousY, previousX;

    enum HorizontalDirection {
        LEFT, RIGHT
    }

    enum VerticalDirection {
        DOWN, UP
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    VerticalDirection verticalDraggingDirection;
    HorizontalDirection horizontalDraggingDirection;
    int draggingDirectionThreshold = 20;
    SelectionListener selectionListener;

    interface SelectionListener {
        void onSelected(ArrayList<Integer> selection);

        void onStartedSelection();

        void onDoneSelection();
    }

    public void resetFlyOutListener() {
        dispatchedStartSelection = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector == null) detector = new GestureDetector(getContext(), new GridGesture());

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchDown = false;

                dispatchListeners();
                checkedCycle = !checkedCycle;
                startingImageStatus = false;
                isScrolling = false;
                shouldSelect = false;
                verticalDraggingDirection = null;
                horizontalDraggingDirection = null;

                break;


            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = pointToPosition(x, y);
                if (position != GridView.INVALID_POSITION && mPosition != position) {
                    mPosition = position;
                    GridViewItem cellView = (GridViewItem) findViewWithTag(position);
                    startingImageStatus = cellView.isChecked();

                }
                previousY = y;
                previousX = x;
                touchDown = true;
                break;


        }


        if (!detector.onTouchEvent(event) && shouldSelect) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (hotSpotTop == null || hotSpotBottom == null) initRects();
                    if (hotSpotTop.contains(x, y)) {
                        scrollListBy(-10);
                    } else if (hotSpotBottom.contains(x, y)) {
                        scrollListBy(10);
                    }
                    if (verticalDraggingDirection == null && Math.abs(y - previousY) >= draggingDirectionThreshold)
                        if (y < previousY) {
                            verticalDraggingDirection = VerticalDirection.UP;
                        } else verticalDraggingDirection = VerticalDirection.DOWN;

                    if (horizontalDraggingDirection == null && Math.abs(x - previousX) >= 10)
                        if (x < previousX) {
                            horizontalDraggingDirection = HorizontalDirection.LEFT;
                        } else horizontalDraggingDirection = HorizontalDirection.RIGHT;


                    int position = pointToPosition(x, y);

                    if (position != GridView.INVALID_POSITION && mPosition != position) {

                        int multiplier = position > mPosition ? 1 : -1;
                        int delta = Math.abs(mPosition - position);

                        for (int i = 0; i <= delta; i++) {
                            int viewTag = mPosition + (i * multiplier);
                            GridViewItem cellView = (GridViewItem) findViewWithTag(viewTag);
                            if (cellView != null) {

                                if (verticalDraggingDirection == null) {
                                    if (horizontalDraggingDirection == HorizontalDirection.RIGHT)
                                        cellView.setChecked(position > mPosition != startingImageStatus, true);
                                    else if (horizontalDraggingDirection == HorizontalDirection.LEFT) {
                                        cellView.setChecked(position < mPosition != startingImageStatus, true);
                                    }
                                } else {
                                    if (verticalDraggingDirection == VerticalDirection.DOWN)
                                        cellView.setChecked(position > mPosition != startingImageStatus, true);
                                    else if (verticalDraggingDirection == VerticalDirection.UP) {
                                        cellView.setChecked(position < mPosition != startingImageStatus, true);
                                    }
                                }
                                notifyCheckedItem(viewTag, cellView.isChecked());
                            }

                        }
                        mPosition = position;


                    }
                    return true;
            }

        }
        return super.onTouchEvent(event);
    }
}
