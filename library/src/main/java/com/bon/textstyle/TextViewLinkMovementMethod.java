package com.bon.textstyle;

import android.app.Activity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bon.logger.Logger;
import com.bon.util.StringUtils;

import java8.util.function.Consumer;

public class TextViewLinkMovementMethod extends LinkMovementMethod {
    static final String TAG = TextViewLinkMovementMethod.class.getSimpleName();
    Activity activity = null;
    Consumer<String> linkConsumer;

    public TextViewLinkMovementMethod(Activity activity, Consumer<String> linkConsumer) {
        this.activity = activity;
        this.linkConsumer = linkConsumer;
    }

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        try {
            // get action
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);

                if (link != null && link.length != 0) {
                    String url = link[0].getURL();
                    if (!StringUtils.isEmpty(url) && linkConsumer != null) {
                        linkConsumer.accept(url);
                    }

                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    /**
     * @param textView
     */
    public static void stripUnderlines(TextView textView, int linkColor) {
        Spannable spannable = new SpannableString(textView.getText());
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL(), linkColor);
            spannable.setSpan(span, start, end, 0);
        }
        textView.setText(spannable);
    }

    /**
     * @param textView
     * @param color
     */
    public static void changeColorTextSelector(TextView textView, int color) {
        textView.setHighlightColor(color);
    }

    public static MovementMethod newInstance(Activity activity, Consumer<String> linkConsumer) {
        return new TextViewLinkMovementMethod(activity, linkConsumer);
    }
}
