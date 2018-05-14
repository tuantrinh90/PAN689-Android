package com.bon.textstyle;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanNoUnderline extends URLSpan {
    private int linkColor;

    public URLSpanNoUnderline(String url, int linkColor) {
        super(url);
        this.linkColor = linkColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(linkColor);
    }
}