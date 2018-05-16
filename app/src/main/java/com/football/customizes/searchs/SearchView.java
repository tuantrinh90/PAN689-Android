package com.football.customizes.searchs;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchView extends LinearLayout {
    @BindView(R.id.etSearch)
    ExtEditText etSearch;
    @BindView(R.id.ivFilter)
    AppCompatImageView ivFilter;

    Unbinder unbinder;

    public SearchView(Context context) {
        super(context);
        initView(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view, this);
        unbinder = ButterKnife.bind(this, view);

        // update hint
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        etSearch.setHint(typedArray.getString(R.styleable.SearchView_searchViewHint));
        typedArray.recycle();
    }

    public ExtEditText getSearchView() {
        return etSearch;
    }

    public void setFilterListener(View.OnClickListener onClickListener) {
        this.ivFilter.setOnClickListener(onClickListener);
    }

    public AppCompatImageView getFilter() {
        return ivFilter;
    }

    @Override
    protected void detachViewFromParent(View child) {
        super.detachViewFromParent(child);
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }
}
