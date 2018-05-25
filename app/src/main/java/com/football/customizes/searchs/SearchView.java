package com.football.customizes.searchs;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.interfaces.Optional;
import com.bon.util.ExtUtils;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;
import com.football.fantasy.R;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class SearchView extends LinearLayout {
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.etSearch)
    ExtEditText etSearch;
    @BindView(R.id.ivCancel)
    ImageView ivCancel;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;

    Consumer<String> searchConsumer;
    Consumer<Void> filerConsumer;

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
        ButterKnife.bind(this, view);

        // update hint
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchView, 0, 0);
        etSearch.setHint(typedArray.getString(R.styleable.SearchView_searchViewHint));
        typedArray.recycle();

        // text changes
        RxTextView.textChanges(etSearch).subscribe(charSequence -> ivCancel.setVisibility(StringUtils.isEmpty(charSequence) ? INVISIBLE : VISIBLE));

        // click cancel
        RxView.clicks(ivCancel).subscribe(o -> {
            etSearch.setText("");
            performSearch("");
        });

        // search
        RxTextView.editorActions(etSearch).subscribe(imeActions -> {
            if (imeActions == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
            }
        });

        // filter
        RxView.clicks(ivFilter).subscribe(o -> Optional.from(filerConsumer).doIfPresent(f -> f.accept(null)));
    }

    void performSearch(String query) {
        KeyboardUtils.hideKeyboard(ExtUtils.sTopActivity, etSearch);
        Optional.from(searchConsumer).doIfPresent(s -> s.accept(query));
    }

    public ImageView getSearchImageView() {
        return ivSearch;
    }

    public ExtEditText getSearchView() {
        return etSearch;
    }

    public ImageView getCancelImageView() {
        return ivCancel;
    }

    public ImageView getFilter() {
        return ivFilter;
    }

    public SearchView setSearchConsumer(Consumer<String> searchConsumer) {
        this.searchConsumer = searchConsumer;
        return this;
    }

    public SearchView setFilerConsumer(Consumer<Void> filerConsumer) {
        this.filerConsumer = filerConsumer;
        return this;
    }
}
