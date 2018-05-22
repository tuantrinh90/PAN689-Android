package com.bon.customview.keyvaluepair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.listview.ExtListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.interfaces.Optional;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairDialogFragment extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtKeyValuePairDialogFragment.class.getSimpleName();

    // instance
    public static ExtKeyValuePairDialogFragment newInstance() {
        return new ExtKeyValuePairDialogFragment();
    }

    String value;
    List<ExtKeyValuePair> extKeyValuePairs;
    List<ExtKeyValuePair> extKeyValuePairsOrigins;
    boolean isVisibleFilter;

    ExtKeyValuePairAdapter<ExtKeyValuePair> extKeyValuePairAdapter;
    Consumer<ExtKeyValuePair> onSelectedConsumer;

    ExtTextView tvCancel;
    ExtEditText edtSearch;
    ExtListView lvKeyValuePair;

    int index = 0;
    int positionSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.key_value_pair_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            // cancel
            tvCancel = view.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(v -> onClickCancel());

            // filter
            edtSearch = view.findViewById(R.id.edtSearch);

            // data
            lvKeyValuePair = view.findViewById(R.id.lvKeyValuePair);

            // active key is selected
            if (extKeyValuePairsOrigins != null && extKeyValuePairsOrigins.size() > 0) {
                StreamSupport.stream(extKeyValuePairsOrigins).forEach(n -> {
                    if (!StringUtils.isEmpty(value) && value.equalsIgnoreCase(n.getKey())) {
                        n.setSelected(true);
                        positionSelected = index;
                    } else {
                        n.setSelected(false);
                    }
                    index++;
                });
            }

            // filter
            edtSearch.setVisibility(isVisibleFilter ? View.VISIBLE : View.GONE);
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    filterData();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // don't show keyboard
            getDialog().setOnShowListener(dialog -> KeyboardUtils.hideKeyboard(getActivity(), edtSearch));

            // load data
            loadData();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public void onClickCancel() {
        try {
            Optional.from(onSelectedConsumer).doIfPresent(o -> o.accept(new ExtKeyValuePair("", "")));
            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void filterData() {
        try {
            extKeyValuePairs = new ArrayList<>(extKeyValuePairsOrigins);

            // filter
            String query = edtSearch.getText().toString().toLowerCase();
            if (!StringUtils.isEmpty(query)) {
                extKeyValuePairs = StreamSupport.stream(extKeyValuePairs)
                        .filter(n -> n.getKey().toLowerCase().contains(query) || n.getValue().toLowerCase().contains(query))
                        .collect(Collectors.toList());
            }

            // notification data
            extKeyValuePairAdapter.notifyDataSetChanged(extKeyValuePairs);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void loadData() {
        try {
            extKeyValuePairs = new ArrayList<>(extKeyValuePairsOrigins);
            extKeyValuePairAdapter = new ExtKeyValuePairAdapter(getContext(), extKeyValuePairs);
            lvKeyValuePair.setAdapter(extKeyValuePairAdapter);
            lvKeyValuePair.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    if (onSelectedConsumer != null) {
                        onSelectedConsumer.accept(extKeyValuePairAdapter.getItem(position));
                    }

                    dismiss();
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            lvKeyValuePair.setSelection(positionSelected);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    public ExtKeyValuePairDialogFragment setValue(String value) {
        this.value = value;
        return this;
    }

    public ExtKeyValuePairDialogFragment setExtKeyValuePairs(List<ExtKeyValuePair> extKeyValuePairs) {
        this.extKeyValuePairsOrigins = extKeyValuePairs;
        return this;
    }

    public ExtKeyValuePairDialogFragment setVisibleFilter(boolean isVisibleFilter) {
        this.isVisibleFilter = isVisibleFilter;
        return this;
    }

    public ExtKeyValuePairDialogFragment setOnSelectedConsumer(Consumer<ExtKeyValuePair> onSelectedConsumer) {
        this.onSelectedConsumer = onSelectedConsumer;
        return this;
    }
}

