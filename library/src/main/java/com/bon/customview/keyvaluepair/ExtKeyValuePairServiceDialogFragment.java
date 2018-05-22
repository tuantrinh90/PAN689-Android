package com.bon.customview.keyvaluepair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bon.customview.edittext.ExtEditText;
import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.bon.interfaces.Optional;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;

import java8.util.stream.StreamSupport;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairServiceDialogFragment<AD extends ExtBaseAdapter<T>, T extends ExtKeyValuePair> extends ExtBaseBottomDialogFragment {
    private static final String TAG = ExtKeyValuePairServiceDialogFragment.class.getSimpleName();

    // instance
    public static ExtKeyValuePairServiceDialogFragment newInstance() {
        return new ExtKeyValuePairServiceDialogFragment();
    }

    // view
    RelativeLayout trSearch;
    ExtTextView tvCancel;
    ExtTextView tvTitle;
    ExtEditText edtSearch;
    ExtPagingListView<T> lvData;
    ImageView ivCancel;

    // variable
    AD adapter;
    ExtKeyValuePairListener<T> actionListener;
    String titleDialog;
    boolean isVisibleFilter;
    int index = 0;
    int indexSelected = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.key_value_pair_service_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            // view
            tvTitle = view.findViewById(R.id.tvTitle);
            tvCancel = view.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(v -> onClickCancel());
            ivCancel = view.findViewById(R.id.ivCancel);
            trSearch = view.findViewById(R.id.trSearch);
            edtSearch = view.findViewById(R.id.edtSearch);
            lvData = view.findViewById(R.id.lvData);

            // setup view
            setUpView();

            // load default data
            Optional.from(actionListener).doIfPresent(a -> a.loadData(this));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set up view
     */
    private void setUpView() {
        try {
            // don't show keyboard
            getDialog().setOnShowListener(dialog -> KeyboardUtils.hideKeyboard(getActivity(), edtSearch));

            // set title app
            tvTitle.setText(StringUtils.isEmpty(titleDialog) ? getString(R.string.select_value) : titleDialog);

            // show or hide search view
            trSearch.setVisibility(isVisibleFilter ? View.VISIBLE : View.GONE);
            edtSearch.setOnEditorActionListener((v, actionId, event) -> {
                try {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        // hide keyboard
                        KeyboardUtils.hideKeyboard(getActivity(), edtSearch);

                        // clear data
                        lvData.clearItems();

                        // load data
                        Optional.from(actionListener).doIfPresent(a -> a.loadData(this));

                        return true;
                    }
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }

                return false;
            });

            // search
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        ivCancel.setVisibility(StringUtils.isEmpty(String.valueOf(charSequence)) ? View.GONE : View.VISIBLE);
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            // cancel search
            ivCancel.setOnClickListener(view -> {
                try {
                    // hide keyboard
                    KeyboardUtils.hideKeyboard(getActivity(), edtSearch);

                    // clear search condition
                    edtSearch.setText("");

                    // clear data
                    lvData.clearItems();

                    // load data
                    Optional.from(actionListener).doIfPresent(a -> a.loadData(this));
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // adapter
            if (adapter == null) {
                throw new NullPointerException("Adapter can not null!!!");
            }

            // init data list view
            lvData.init(getContext(), adapter)
                    .setOnExtClickListener((position, item) -> {
                        try {
                            Optional.from(actionListener).doIfPresent(a -> a.onClickItem((T) item));
                            dismiss();
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    })
                    .setOnExtLoadMoreListener(() -> Optional.from(actionListener).doIfPresent(a -> a.loadMoreData(this)))
                    .setEnabledSwipeRefreshing(true)
                    .setOnExtRefreshListener(() -> {
                        lvData.clearItems();
                        Optional.from(actionListener).doIfPresent(a -> a.loadData(this));
                    });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * click cancel
     */
    void onClickCancel() {
        try {
            Optional.from(actionListener).doIfPresent(a -> a.onClickItem(null));
            dismiss();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set adapter
     *
     * @param adapter
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setUpAdapter(AD adapter) {
        try {
            this.adapter = adapter;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return this;
    }

    /**
     * set selected
     *
     * @param item
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setSelectedItem(T item) {
        try {
            if (adapter == null || adapter.getCount() <= 0 || item == null) {
                return this;
            }

            // set selected
            indexSelected = 0;
            index = 0;
            StreamSupport.stream(adapter.getItems()).forEach(n -> {
                if (item.getKey().equalsIgnoreCase(n.getKey())) {
                    n.setSelected(true);
                    indexSelected = index;
                } else {
                    n.setSelected(false);
                }

                index++;
            });

            // notification
            lvData.notifyDataSetChanged(adapter.getItems());
            lvData.setSelection(indexSelected);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * set action listener
     *
     * @param actionListener
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setActionListener(ExtKeyValuePairListener<T> actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * set visible or hide view
     *
     * @param visibleFilter
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setVisibleFilter(boolean visibleFilter) {
        this.isVisibleFilter = visibleFilter;
        return this;
    }

    /**
     * set title dialog
     *
     * @param titleDialog
     * @return
     */
    public ExtKeyValuePairServiceDialogFragment setTitleDialog(String titleDialog) {
        this.titleDialog = titleDialog;
        return this;
    }

    /**
     * get list view data
     *
     * @return
     */
    public ExtPagingListView getPagingListView() {
        if (lvData == null) {
            throw new NullPointerException("PagingListView can not null!!!");
        }

        return lvData;
    }

    /**
     * get view search
     *
     * @return
     */
    public ExtEditText getEditTextSearch() {
        if (edtSearch == null) {
            throw new NullPointerException("EditText can not null!!!");
        }

        return edtSearch;
    }

    /**
     * @return
     */
    public String getConditionText() {
        try {
            return edtSearch.getText().toString();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }
}

