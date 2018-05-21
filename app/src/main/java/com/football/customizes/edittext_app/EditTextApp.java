package com.football.customizes.edittext_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.FontUtils;
import com.bon.util.StringUtils;
import com.football.fantasy.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class EditTextApp extends LinearLayout {
    @BindView(R.id.llView)
    LinearLayout llView;
    @BindView(R.id.ivIconLeft)
    AppCompatImageView ivIconLeft;
    @BindView(R.id.tvContent)
    ExtTextView tvContent;
    @BindView(R.id.etContent)
    ExtEditText etContent;
    @BindView(R.id.ivIconRight)
    AppCompatImageView ivIconRight;
    @BindView(R.id.tvError)
    ExtTextView tvError;

    Consumer<String> textChangeConsumer;

    public EditTextApp(Context context) {
        super(context);
        initView(context, null);
    }

    public EditTextApp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public EditTextApp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EditTextApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_view, this);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextApp);

        // update content
        etContent.setText(typedArray.getString(R.styleable.EditTextApp_editTextAppContent));
        etContent.setHint(typedArray.getString(R.styleable.EditTextApp_editTextAppHint));

        // text view content
        tvContent.setText(typedArray.getString(R.styleable.EditTextApp_editTextAppContent));
        tvContent.setHint(typedArray.getString(R.styleable.EditTextApp_editTextAppHint));

        // disable view
        boolean isEnabled = typedArray.getBoolean(R.styleable.EditTextApp_editTextAppEnable, true);
        tvContent.setVisibility(isEnabled ? GONE : VISIBLE);
        etContent.setVisibility(!isEnabled ? GONE : VISIBLE);

        // icon left
        ivIconLeft.setVisibility(GONE);
        Drawable drawableLeft = typedArray.getDrawable(R.styleable.EditTextApp_editTextAppIconLeft);
        Optional.from(drawableLeft).doIfPresent(icon -> {
            ivIconLeft.setImageDrawable(drawableLeft);
            ivIconLeft.setVisibility(VISIBLE);
        });

        // icon right
        ivIconRight.setVisibility(GONE);
        Drawable drawableRight = typedArray.getDrawable(R.styleable.EditTextApp_editTextAppIconRight);
        Optional.from(drawableRight).doIfPresent(icon -> {
            ivIconRight.setImageDrawable(drawableRight);
            ivIconRight.setVisibility(VISIBLE);
        });

        // padding content
        int paddingLeft = (int) (context.getResources().getDimensionPixelSize(R.dimen.padding_layout) * 1.6);
        int paddingRight = (int) (context.getResources().getDimensionPixelSize(R.dimen.padding_layout) * 1.6);

        // left
        if (ivIconLeft.getVisibility() == VISIBLE) {
            paddingLeft = context.getResources().getDimensionPixelSize(R.dimen.padding_layout);
        }

        // right
        if (ivIconRight.getVisibility() == VISIBLE) {
            paddingRight = context.getResources().getDimensionPixelSize(R.dimen.padding_layout);
        }

        // update padding view
        llView.setPadding(paddingLeft, 0, paddingRight, 0);

        // input type
        int inputType = typedArray.getInt(R.styleable.EditTextApp_android_inputType, EditorInfo.TYPE_NULL);
        if (inputType != EditorInfo.TYPE_NULL) {
            etContent.setInputType(inputType);
            // update font
            FontUtils.setCustomTypeface(context, etContent, context.getString(R.string.font_display_bold));
        }

        // ime options
        int imeOptions = typedArray.getInt(R.styleable.EditTextApp_android_imeOptions, EditorInfo.IME_NULL);
        if (imeOptions != EditorInfo.IME_NULL) {
            etContent.setImeOptions(imeOptions);
        }

        // lines
        etContent.setLines(typedArray.getInt(R.styleable.EditTextApp_android_lines, 1));

        // focus view
        etContent.setOnFocusChangeListener((v, hasFocus) -> llView.setPressed(hasFocus));

        // text changes
        RxTextView.textChanges(etContent).subscribe(charSequence -> {
            //FontUtils.setCustomTypeface(context, etContent,
            //        context.getString(StringUtils.isEmpty(charSequence) ? R.string.font_display_bold : R.string.font_display_regular));
            Optional.from(textChangeConsumer).doIfPresent(t -> t.accept(charSequence + ""));
        });

        // recycle
        typedArray.recycle();
    }

    public EditTextApp setContent(String value) {
        etContent.setText(value);
        tvContent.setText(value);
        return this;
    }

    public EditTextApp setError(String error) {
        tvError.setText(error);
        tvError.setVisibility(StringUtils.isEmpty(error) ? GONE : VISIBLE);
        llView.setActivated(StringUtils.isEmpty(error) ? false : true);
        return this;
    }

    public EditTextApp setTextChangeConsumer(Consumer<String> textChangeConsumer) {
        this.textChangeConsumer = textChangeConsumer;
        return this;
    }

    public ExtEditText getContentView() {
        return etContent;
    }

    public AppCompatImageView getIconRightImageView() {
        return ivIconRight;
    }

    public AppCompatImageView getIconLeftImageView() {
        return ivIconLeft;
    }

    public void setIvIconLeft(AppCompatImageView ivIconLeft) {
        this.ivIconLeft = ivIconLeft;
    }

    public ExtTextView getErrorView() {
        return tvError;
    }
}
