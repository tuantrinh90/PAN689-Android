package com.bon.customview.datetime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.library.R;
import com.bon.logger.Logger;

import java.util.Calendar;

import java8.util.function.Consumer;
import java8.util.function.Function;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtDateTimePickerDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtDateTimePickerDialogFragment.class.getSimpleName();

    // instance
    public static ExtDateTimePickerDialogFragment newInstance() {
        return new ExtDateTimePickerDialogFragment();
    }

    // view
    ExtTextView tvCancel;
    ExtTextView tvSave;
    DatePicker dpDatePicker;
    TimePicker tpTimePicker;

    // variable
    Consumer<Calendar> calendarConsumer;
    Function<Calendar, Boolean> conditionFunction;

    long milliseconds;
    Calendar calendar;
    Calendar minDate;
    Calendar maxDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.datetime_picker_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // null point exception value
            if (minDate == null || maxDate == null || calendar == null
                    || calendarConsumer == null || conditionFunction == null) {
                throw new NullPointerException();
            }

            // cancel
            tvCancel = view.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(v -> onClickCancel());

            // save
            tvSave = view.findViewById(R.id.tvSave);
            tvSave.setOnClickListener(v -> onClickSave());

            // date, time picker
            dpDatePicker = view.findViewById(R.id.dpDatePicker);
            tpTimePicker = view.findViewById(R.id.tpTimePicker);

            // date picker
            dpDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), (viewDatePicker, year, monthOfYear, dayOfMonth) -> {
                        try {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            setDisplayButtonDone(conditionFunction.apply(calendar));
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    });

            // set min, max date
            dpDatePicker.setMinDate(minDate.getTimeInMillis());
            dpDatePicker.setMaxDate(maxDate.getTimeInMillis());

            // set current date time
            tpTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            tpTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            tpTimePicker.setIs24HourView(true);

            // update time when change time
            tpTimePicker.setOnTimeChangedListener((viewTimePicker, hourOfDay, minute) -> {
                try {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    setDisplayButtonDone(conditionFunction.apply(calendar));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            setDisplayButtonDone(conditionFunction.apply(calendar));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void setDisplayButtonDone(boolean isVisible) {
        try {
            tvSave.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void onClickCancel() {
        dismiss();
    }

    void onClickSave() {
        try {
            if (calendarConsumer != null) {
                calendarConsumer.accept(calendar);
            }
            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public ExtDateTimePickerDialogFragment setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(milliseconds);
        return this;
    }

    public ExtDateTimePickerDialogFragment setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public ExtDateTimePickerDialogFragment setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public ExtDateTimePickerDialogFragment setCalendarConsumer(Consumer<Calendar> calendarConsumer) {
        this.calendarConsumer = calendarConsumer;
        return this;
    }

    public ExtDateTimePickerDialogFragment setConditionFunction(Function<Calendar, Boolean> conditionFunction) {
        this.conditionFunction = conditionFunction;
        return this;
    }
}
