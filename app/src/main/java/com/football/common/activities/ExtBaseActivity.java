package com.football.common.activities;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bon.logger.Logger;
import com.bon.util.BackUtils;
import com.bon.util.KeyboardUtils;
import com.bon.util.ProgressDialogUtils;
import com.football.common.fragments.BaseMvpFragment;
import com.football.utilities.FragmentUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class ExtBaseActivity extends AppCompatActivity  implements LifecycleProvider<ActivityEvent> {
    private static final String TAG = com.bon.activity.ExtBaseActivity.class.getSimpleName();

    // number
    public static final int NUMBER_ONE = 1;

    // variable
    private ProgressDialogUtils progressDialog = null;

    // store fragment in back stack
    public Stack<BaseMvpFragment> fragments = new Stack<>();

    // lifecycle
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    // set fragment default
    public abstract void initFragmentDefault();

    // onclick back
    private void onClickBackAction() {
        try {
            // hide keyboard
            KeyboardUtils.dontShowKeyboardActivity(this);

            // stacks
            if (this.fragments != null && this.fragments.size() > NUMBER_ONE) {
                // remove current fragment
                fragments.pop();

                // get previous fragment
                BaseMvpFragment fragment = this.fragments.peek();
                if (fragment == null) return;

                // back to previous fragment
                FragmentUtils.replaceFragment(this, fragment);
            } else {
                // clear stack
                this.fragments.clear();

                // init default fragment
                this.initFragmentDefault();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * onclick back action
     */
    public void onBackPressedAction() {
        try {
            KeyboardUtils.dontShowKeyboardActivity(this);
            if (fragments != null && fragments.size() > 0) {
                onClickBackAction();
            } else {
                BackUtils.onClickExit(this, getString(com.bon.library.R.string.double_tap_to_exit));
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    @Override
    public void onBackPressed() {
        // hide keyboard
        KeyboardUtils.dontShowKeyboardActivity(this);

        super.onBackPressed();
    }

    /**
     * show dialog
     */
    public void showProgressDialog() {
        try {
            this.progressDialog = new ProgressDialogUtils();
            this.progressDialog.setMessage(getString(com.bon.library.R.string.loading));
            this.progressDialog.show(this);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * dismiss dialog
     */
    public void hideProgressDialog() {
        try {
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
                this.progressDialog = null;
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}