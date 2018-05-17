package com.bon.customview.datetime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.numberpicker.ExtNumberPicker;
import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;

import java.util.Calendar;

import java8.util.function.Consumer;
import java8.util.function.Function;

/**
 * Created by dangpp on 8/14/2017.
 */

public class ExtDayMonthYearDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtDayMonthYearDialogFragment.class.getSimpleName();

    // instance
    public static ExtDayMonthYearDialogFragment newInstance() {
        return new ExtDayMonthYearDialogFragment();
    }

    // const
     static final int MIN_INDEX_MONTH = 0;
     static final int MAX_INDEX_MONTH = 11;
     static final int MIN_INDEX_DAY = 1;
     static final int MAX_INDEX_DAY_31 = 31;
     static final int MAX_INDEX_DAY_30 = 30;
     static final int MAX_INDEX_DAY_29 = 29;
     static final int MAX_INDEX_DAY_28 = 28;

    // view
    ExtTextView tvCancel;
    ExtTextView tvSave;
    ExtNumberPicker numPickerDay;
    ExtNumberPicker numPickerMonth;
    ExtNumberPicker numPickerYear;

    // calendar
    Calendar minDate;
    Calendar maxDate;
    Calendar defaultDate;
    Calendar valueDate;
    int dayOfMonth = MAX_INDEX_DAY_31;

    Consumer<Calendar> calendarConsumer;
    Function<Calendar, Boolean> conditionFunction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.day_month_year_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setUpViews(view);
            initDatePicker();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void setUpViews(View view) {
        try {
            // null point exception value
            if (minDate == null || maxDate == null || defaultDate == null
                    || valueDate == null || calendarConsumer == null || conditionFunction == null) {
                throw new NullPointerException();
            }

            // cancel
            tvCancel = view.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(v -> dismiss());

            // save
            tvSave = view.findViewById(R.id.tvSave);
            tvSave.setOnClickListener(v -> onSaveClick());

            // number picker
            numPickerDay = view.findViewById(R.id.numPickerDay);
            numPickerMonth = view.findViewById(R.id.numPickerMonth);
            numPickerYear = view.findViewById(R.id.numPickerYear);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void initDatePicker() {
        try {
            // wheel
            numPickerDay.setWrapSelectorWheel(false);
            numPickerMonth.setWrapSelectorWheel(false);
            numPickerYear.setWrapSelectorWheel(false);

            //  max min day
            numPickerDay.setMinValue(MIN_INDEX_DAY);
            numPickerDay.setMaxValue(MAX_INDEX_DAY_31);

            // day value display
            numPickerDay.setDisplayedValues(new String[]{
                    "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                    "31"
            });

            // max min month
            numPickerMonth.setMinValue(MIN_INDEX_MONTH);
            numPickerMonth.setMaxValue(MAX_INDEX_MONTH);

            // month value display
            numPickerMonth.setDisplayedValues(new String[]{
                    getString(R.string.month_one),
                    getString(R.string.month_two),
                    getString(R.string.month_three),
                    getString(R.string.month_four),
                    getString(R.string.month_five),
                    getString(R.string.month_six),
                    getString(R.string.month_seven),
                    getString(R.string.month_eight),
                    getString(R.string.month_nice),
                    getString(R.string.month_ten),
                    getString(R.string.month_eleven),
                    getString(R.string.month_twelfth)
            });

            // max, min year by params
            numPickerYear.setMinValue(minDate.get(Calendar.YEAR));
            numPickerYear.setMaxValue(maxDate.get(Calendar.YEAR));

            // listener year
            numPickerYear.setOnValueChangedListener((picker, oldVal, newVal) -> {
                try {
                    valueDate.set(Calendar.YEAR, newVal);
                    setDisplayButtonDone(conditionFunction.apply(valueDate));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // month
            numPickerMonth.setOnValueChangedListener((picker, oldVal, newVal) -> {
                try {
                    valueDate.set(Calendar.MONTH, newVal);
                    setDisplayButtonDone(conditionFunction.apply(valueDate));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // day
            numPickerDay.setOnValueChangedListener((picker, oldVal, newVal) -> {
                try {
                    valueDate.set(Calendar.DAY_OF_MONTH, newVal);
                    setDisplayButtonDone(conditionFunction.apply(valueDate));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // Set default value
            setValue();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void setValue() {
        try {
            numPickerDay.setValue(valueDate.get(Calendar.DAY_OF_MONTH));
            numPickerMonth.setValue(valueDate.get(Calendar.MONTH));
            numPickerYear.setValue(valueDate.get(Calendar.YEAR));
            setDisplayButtonDone(conditionFunction.apply(valueDate));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void setDisplayButtonDone(boolean isVisible) {
        try {
            // check month
            switch (valueDate.get(Calendar.MONTH)) {
                case 0:
                case 2:
                case 4:
                case 6:
                case 7:
                case 9:
                case 11:
                    dayOfMonth = MAX_INDEX_DAY_31;
                    break;
                case 1:
                    // check year
                    if (DateTimeUtils.isLeapYear(valueDate.get(Calendar.YEAR))) {
                        dayOfMonth = MAX_INDEX_DAY_29;
                    }else{
                        dayOfMonth = MAX_INDEX_DAY_28;
                    }
                    break;
                case 3:
                case 5:
                case 8:
                case 10:
                    dayOfMonth = MAX_INDEX_DAY_30;
                    break;
            }

            // set max day
            numPickerDay.setMaxValue(dayOfMonth);
            if (valueDate.get(Calendar.DAY_OF_MONTH) > dayOfMonth) {
                numPickerDay.setValue(dayOfMonth);
            }

            tvSave.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void onSaveClick() {
        try {
            if (calendarConsumer != null) {
                calendarConsumer.accept(valueDate);
            }
            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public ExtDayMonthYearDialogFragment setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public ExtDayMonthYearDialogFragment setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public ExtDayMonthYearDialogFragment setValueDate(Calendar defaultDate) {
        this.defaultDate = defaultDate;
        this.valueDate = defaultDate == null ? Calendar.getInstance() :
                DateTimeUtils.getCalendarNoTime(defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH), defaultDate.get(Calendar.DAY_OF_MONTH));
        return this;
    }

    public ExtDayMonthYearDialogFragment setCalendarConsumer(Consumer<Calendar> calendarConsumer) {
        this.calendarConsumer = calendarConsumer;
        return this;
    }

    public ExtDayMonthYearDialogFragment setConditionFunction(Function<Calendar, Boolean> conditionFunction) {
        this.conditionFunction = conditionFunction;
        return this;
    }
}
