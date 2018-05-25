package com.bon.customview.checkbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.bon.util.TypefacesUtils;

/**
 * Created by user on 4/22/2015.
 */
@SuppressLint("AppCompatCustomView")
public class ExtCheckBox extends AppCompatCheckBox {
    private static final String TAG = ExtCheckBox.class.getSimpleName();

    public ExtCheckBox(Context context) {
        super(context);
    }

    public ExtCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, attrs);
    }

    public ExtCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void applyAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtCheckBox, 0, 0);
            String fontPath = typedArray.getString(R.styleable.ExtCheckBox_checkBoxFontAssetName);
            if (StringUtils.isEmpty(fontPath)) fontPath = TypefacesUtils.FONT_DEFAULT;
            this.setTypeface(TypefacesUtils.get(getContext(), fontPath));
            this.setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            typedArray.recycle();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
