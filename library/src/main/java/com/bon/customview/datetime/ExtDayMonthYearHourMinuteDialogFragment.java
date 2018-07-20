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

public class ExtDayMonthYearHourMinuteDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtDayMonthYearHourMinuteDialogFragment.class.getSimpleName();

    // instance
    public static ExtDayMonthYearHourMinuteDialogFragment newInstance() {
        return new ExtDayMonthYearHourMinuteDialogFragment();
    }

    // const
    static final int MIN_INDEX_MONTH = 0;
    static final int MAX_INDEX_MONTH = 11;
    static final int MIN_INDEX_DAY = 1;
    static final int MAX_INDEX_DAY_31 = 31;
    static final int MAX_INDEX_DAY_30 = 30;
    static final int MAX_INDEX_DAY_29 = 29;
    static final int MAX_INDEX_DAY_28 = 28;
    static final int MIN_INDEX_HOUR = 0;
    static final int MAX_INDEX_HOUR = 23;
    static final int MIN_INDEX_MINUTE = 0;
    //    static final int MAX_INDEX_MINUTE = 59;
    static final int MAX_INDEX_MINUTE = 3;
    static final int MINUTE_STEP = 15;

    // view
    ExtTextView tvCancel;
    ExtTextView tvSave;
    ExtNumberPicker numPickerDay;
    ExtNumberPicker numPickerMonth;
    ExtNumberPicker numPickerYear;
    ExtNumberPicker numPickerHour;
    ExtNumberPicker numPickerMinute;

    // variable
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
        return inflater.inflate(R.layout.day_month_year_hour_minute_dialog_fragment, container, false);
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

    /**
     * @param view
     */
    void setUpViews(View view) {
        try {
            // null point exception value
            if (minDate == null || maxDate == null || defaultDate == null || valueDate == null
                    || calendarConsumer == null || conditionFunction == null) {
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
            numPickerHour = view.findViewById(R.id.numPickerHour);
            numPickerMinute = view.findViewById(R.id.numPickerMinute);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * init date picker
     */
    private void initDatePicker() {
        try {
            if (maxDate == null || minDate == null || defaultDate == null || valueDate == null) {
                throw new NullPointerException();
            }

            // wheel
            numPickerDay.setWrapSelectorWheel(false);
            numPickerMonth.setWrapSelectorWheel(false);
            numPickerYear.setWrapSelectorWheel(false);

            // day value display
            numPickerDay.setDisplayedValues(new String[]{
                    "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                    "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                    "31"
            });

            //  max min day
            numPickerDay.setMinValue(MIN_INDEX_DAY);
            numPickerDay.setMaxValue(MAX_INDEX_DAY_31);

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

            // month
            numPickerMonth.setMinValue(MIN_INDEX_MONTH);
            numPickerMonth.setMaxValue(MAX_INDEX_MONTH);

            // max, min year by params
            numPickerYear.setMinValue(minDate.get(Calendar.YEAR));
            numPickerYear.setMaxValue(maxDate.get(Calendar.YEAR));

            // hour values display
            numPickerHour.setDisplayedValues(new String[]{
                    "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
                    "22", "23"});

            // hours
            numPickerHour.setMinValue(MIN_INDEX_HOUR);
            numPickerHour.setMaxValue(MAX_INDEX_HOUR);

            // minute values display
//            numPickerMinute.setDisplayedValues(new String[]{
//                    "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
//                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
//                    "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
//                    "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
//                    "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
//                    "55", "56", "57", "58", "59"});
            numPickerMinute.setDisplayedValues(new String[]{"00", "15", "30", "45"});

            numPickerMinute.setMinValue(MIN_INDEX_MINUTE);
            numPickerMinute.setMaxValue(MAX_INDEX_MINUTE);

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

            // hour
            numPickerHour.setOnValueChangedListener((picker, oldVal, newVal) -> {
                try {
                    valueDate.set(Calendar.HOUR_OF_DAY, newVal);
                    setDisplayButtonDone(conditionFunction.apply(valueDate));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // minute
            numPickerMinute.setOnValueChangedListener((picker, oldVal, newVal) -> {
                try {
                    valueDate.set(Calendar.MINUTE, newVal * MINUTE_STEP);
                    setDisplayButtonDone(conditionFunction.apply(valueDate));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // Set default value
            setValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValue() {
        try {
            numPickerDay.setValue(valueDate.get(Calendar.DAY_OF_MONTH));
            numPickerMonth.setValue(valueDate.get(Calendar.MONTH));
            numPickerYear.setValue(valueDate.get(Calendar.YEAR));
            numPickerHour.setValue(valueDate.get(Calendar.HOUR_OF_DAY));
            numPickerMinute.setValue(valueDate.get(Calendar.MINUTE) / MINUTE_STEP);
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
                    } else {
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

    public ExtDayMonthYearHourMinuteDialogFragment setMinDate(Calendar minDate) {
        this.minDate = minDate;
        return this;
    }

    public ExtDayMonthYearHourMinuteDialogFragment setMaxDate(Calendar maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public ExtDayMonthYearHourMinuteDialogFragment setValueDate(Calendar defaultDate) {
        this.defaultDate = defaultDate;
        this.valueDate = DateTimeUtils.getCalendarTime(defaultDate.get(Calendar.YEAR), defaultDate.get(Calendar.MONTH),
                defaultDate.get(Calendar.DAY_OF_MONTH), defaultDate.get(Calendar.HOUR_OF_DAY), defaultDate.get(Calendar.MINUTE));
        return this;
    }

    public ExtDayMonthYearHourMinuteDialogFragment setCalendarConsumer(Consumer<Calendar> calendarConsumer) {
        this.calendarConsumer = calendarConsumer;
        return this;
    }

    public ExtDayMonthYearHourMinuteDialogFragment setConditionFunction(Function<Calendar, Boolean> conditionFunction) {
        this.conditionFunction = conditionFunction;
        return this;
    }
}
