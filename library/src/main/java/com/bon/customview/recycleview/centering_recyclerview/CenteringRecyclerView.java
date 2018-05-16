package com.bon.customview.recycleview.centering_recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.bon.library.R;

import java.util.Arrays;

public class CenteringRecyclerView extends RecyclerView {
    public static final int ALIGN_HEAD = 0;
    public static final int ALIGN_TAIL = 1;
    public static final int ALIGN_CENTER = 2;

    public static final int SNAPPING_STRATEGY_HEAD = 0;
    public static final int SNAPPING_STRATEGY_TAIL = 1;
    public static final int SNAPPING_STRATEGY_CENTER = 2;
    public static final int SNAPPING_STRATEGY_NONE = 3;

    private boolean mIgnoreIfVisible;
    private boolean mIgnoreIfCompletelyVisible;
    private int mFallbackCenterOffset;
    private int mFallbackBottomOffset;

    public CenteringRecyclerView(Context context) {
        this(context, null);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenteringRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CenteringRecyclerView, 0, 0);
        try {
            mIgnoreIfVisible = typedArray.getBoolean(R.styleable.CenteringRecyclerView_ignoreIfVisible, false);
            mIgnoreIfCompletelyVisible = typedArray.getBoolean(R.styleable.CenteringRecyclerView_ignoreIfCompletelyVisible, false);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Sets the currently selected position with the given alignment.
     *
     * @param position  The adapter position.
     * @param alignment (ALIGN_HEAD | ALIGN_TAIL ALIGN_CENTER)
     * @see #center(int)
     * @see #head(int)
     * @see #tail(int)
     */
    public void setSelection(int position, int alignment) {
        switch (alignment) {
            case ALIGN_CENTER:
                center(position);
                break;
            case ALIGN_HEAD:
                head(position);
                break;
            case ALIGN_TAIL:
                tail(position);
                break;
            default:
                throw new IllegalArgumentException("unknown alignment");
        }
    }

    /**
     * Scrolls a view at the given position to top (vertical layout) or left (horizontal layout).
     *
     * @param position The adapter position.
     */
    public void head(int position) {
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            staggeredGridLayoutManager.scrollToPositionWithOffset(position, 0);
        } else {
            throw new UnsupportedOperationException("unsupported layout manager");
        }
    }

    /**
     * Scrolls a view at the given position to bottom (vertical layout) or right (horizontal layout).
     *
     * @param position The adapter position.
     */
    public void tail(final int position) {
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int offset = getBottomOffset(linearLayoutManager.getOrientation(), 0);
            linearLayoutManager.scrollToPositionWithOffset(position, offset);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int offset = getBottomOffset(staggeredGridLayoutManager.getOrientation(), 0);
            staggeredGridLayoutManager.scrollToPositionWithOffset(position, offset);

            post(() -> {
                int first = getFirstVisiblePosition();
                int last = getLastVisiblePosition();
                int childPosition = 0;
                for (int i = first; i < last; i++) {
                    if (i == position) {
                        int offsetChild = getBottomOffset(staggeredGridLayoutManager.getOrientation(), childPosition);
                        staggeredGridLayoutManager.scrollToPositionWithOffset(position, offsetChild);
                        break;
                    }
                    childPosition++;
                }
            });
        } else {
            throw new UnsupportedOperationException("unsupported layout manager");
        }
    }

    /**
     * Scrolls a view at the given position to center.
     *
     * @param position The adapter position.
     */
    public void center(final int position) {
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int offset = getCenterOffset(linearLayoutManager.getOrientation(), 0);
            linearLayoutManager.scrollToPositionWithOffset(position, offset);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int offset = getCenterOffset(staggeredGridLayoutManager.getOrientation(), 0);
            staggeredGridLayoutManager.scrollToPositionWithOffset(position, offset);

            post(() -> {
                int first = getFirstVisiblePosition();
                int last = getLastVisiblePosition();
                int childPosition = 0;
                for (int i = first; i < last; i++) {
                    if (i == position) {
                        int offsetChild = getCenterOffset(staggeredGridLayoutManager.getOrientation(), childPosition);
                        staggeredGridLayoutManager.scrollToPositionWithOffset(position, offsetChild);
                        break;
                    }
                    childPosition++;
                }
            });
        } else {
            throw new UnsupportedOperationException("unsupported layout manager");
        }
    }

    /**
     * Snaps a view at the given position to a closer end, top or bottom (left or right).
     *
     * @param position The adapter position.
     * @param strategy The snapping strategy. Applied when the given position has the same distance
     *                 from the both ends.
     */
    public void snap(int position, int strategy) {
        if (mIgnoreIfCompletelyVisible && isCompletelyVisible(position)) {
            return;
        }

        if (mIgnoreIfVisible && isVisible(position)) {
            return;
        }

        if (position < 0) {
            scrollToPosition(0);
            return;
        }

        int diffFirst = getFirstVisiblePosition() - position;
        int diffLast = position - getLastVisiblePosition();
        if (diffFirst > diffLast) {
            head(position);
        } else if (diffFirst < diffLast) {
            tail(position);
        } else {
            switch (strategy) {
                case SNAPPING_STRATEGY_HEAD:
                    head(position);
                    break;
                case SNAPPING_STRATEGY_TAIL:
                    tail(position);
                    break;
                case SNAPPING_STRATEGY_CENTER:
                    center(position);
                    break;
                case SNAPPING_STRATEGY_NONE:
                    // no-op
                    break;
            }
        }
    }

    /**
     * If you want to ignore scrolling when a view at the requested position is completely visible,
     * set this to true.
     *
     * @param ignoreIfCompletelyVisible true | false
     */
    public void setIgnoreIfCompletelyVisible(boolean ignoreIfCompletelyVisible) {
        mIgnoreIfCompletelyVisible = ignoreIfCompletelyVisible;
    }


    /**
     * If you want to ignore scrolling when a view at the requested position is visible, set this to
     * true.
     *
     * @param ignoreIfVisible true | false
     */
    public void setIgnoreIfVisible(boolean ignoreIfVisible) {
        mIgnoreIfVisible = ignoreIfVisible;
    }

    /**
     * Tests if a view at the given position is visible or not.
     *
     * @param position The adapter position.
     * @see #isCompletelyVisible(int)
     */
    public boolean isVisible(int position) {
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        return first <= position && last >= position;
    }

    /**
     * Tests if a view at the given position is completely visible or not.
     *
     * @param position The adapter position.
     * @see #isVisible(int)
     */
    public boolean isCompletelyVisible(int position) {
        int first = getFirstCompletelyVisiblePosition();
        int last = getLastCompletelyVisiblePosition();
        return first <= position && last >= position;
    }

    /**
     * Returns the first visible grid position.
     *
     * @return the first visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getFirstCompletelyVisiblePosition()
     */
    public int getFirstVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findFirstVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] firstVisibleItemPositions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);
            Arrays.sort(firstVisibleItemPositions);
            return firstVisibleItemPositions[0];
        }
    }

    /**
     * Returns the last visible grid position.
     *
     * @return the last visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getLastCompletelyVisiblePosition()
     */
    public int getLastVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findLastVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            Arrays.sort(lastVisibleItemPositions);
            return lastVisibleItemPositions[lastVisibleItemPositions.length - 1];
        }
    }

    /**
     * Returns the first completely visible grid position.
     *
     * @return the first completely visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getFirstVisiblePosition()
     */
    public int getFirstCompletelyVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] firstVisibleItemPositions = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
            Arrays.sort(firstVisibleItemPositions);
            return firstVisibleItemPositions[0];
        }
    }

    /**
     * Returns the last completely visible grid position.
     *
     * @return the last completely visible position or RecyclerView.NO_POSITION if any error occurs.
     * @see #getLastVisiblePosition()
     */
    public int getLastCompletelyVisiblePosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findLastCompletelyVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
            Arrays.sort(lastVisibleItemPositions);
            return lastVisibleItemPositions[lastVisibleItemPositions.length - 1];
        }
    }


    /**
     * Calculates and returns the center offset size.
     *
     * @param orientation   The layout orientation.
     * @param childPosition The visible child position.
     * @return the center offset or the last known offset if a child at the position is null.
     */
    private int getCenterOffset(int orientation, int childPosition) {
        View child = getChildAt(childPosition);
        if (child == null) {
            return mFallbackCenterOffset;
        }

        final Rect rect = new Rect();
        if (getGlobalVisibleRect(rect)) {
            if (orientation == OrientationHelper.HORIZONTAL) {
                mFallbackCenterOffset = rect.width() / 2 - child.getWidth() / 2;
            } else {
                mFallbackCenterOffset = rect.height() / 2 - child.getHeight() / 2;
            }
        } else {
            if (orientation == OrientationHelper.HORIZONTAL) {
                mFallbackCenterOffset = getWidth() / 2 - child.getWidth() / 2;
            } else {
                mFallbackCenterOffset = getHeight() / 2 - child.getHeight() / 2;
            }
        }

        return mFallbackCenterOffset;
    }

    /**
     * Calculates and returns the bottom offset size.
     *
     * @param orientation   The layout orientation.
     * @param childPosition The visible child position.
     * @return the bottom offset or the last known offset if a child at the position is null.
     */
    private int getBottomOffset(int orientation, int childPosition) {
        View child = getChildAt(childPosition);
        if (child == null) {
            return mFallbackBottomOffset;
        }

        final Rect rect = new Rect();
        if (getGlobalVisibleRect(rect)) {
            if (orientation == OrientationHelper.HORIZONTAL) {
                mFallbackBottomOffset = rect.width() - child.getWidth();
            } else {
                mFallbackBottomOffset = rect.height() - child.getHeight();
            }
        } else {
            if (orientation == OrientationHelper.HORIZONTAL) {
                mFallbackBottomOffset = getWidth() - child.getWidth();
            } else {
                mFallbackBottomOffset = getHeight() - child.getHeight();
            }
        }

        return mFallbackBottomOffset;
    }
}
