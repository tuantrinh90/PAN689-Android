package com.football.utilities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.football.common.fragments.BaseMvpFragment;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

import java.util.List;

import java8.util.function.Consumer;

/**
 * Created by Dang on 9/14/2015.
 */
public class FragmentUtils {
    private static final String TAG = FragmentUtils.class.getSimpleName();
    private static int containerViewId = 0;

    /**
     * set content id
     *
     * @param containerViewId
     */
    public static void setContainerViewId(int containerViewId) {
        FragmentUtils.containerViewId = containerViewId;
    }

    /**
     * @param fragmentActivity
     * @param fragment
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void replaceFragment(FragmentActivity fragmentActivity, BaseMvpFragment<V, P> fragment) {
        replaceFragment(fragmentActivity, fragment, null);
    }

    /**
     * @param fragmentActivity
     * @param fragment
     * @param fragmentConsumer
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void replaceFragment(FragmentActivity fragmentActivity,
                                                                                                 BaseMvpFragment<V, P> fragment, Consumer<BaseMvpFragment<V, P>> fragmentConsumer) {
        if (containerViewId <= 0) throw new NullPointerException();
        replaceFragment(fragmentActivity, containerViewId, fragment, fragmentConsumer);
    }

    /**
     * @param fragmentActivity
     * @param containerViewId
     * @param fragment
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void replaceFragment(FragmentActivity fragmentActivity,
                                                                                                 int containerViewId, BaseMvpFragment<V, P> fragment) {
        replaceFragment(fragmentActivity, containerViewId, fragment, null);
    }

    /**
     * @param fragmentActivity
     * @param containerViewId
     * @param fragment
     * @param fragmentConsumer
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void replaceFragment(FragmentActivity fragmentActivity, int containerViewId,
                                                                                                 BaseMvpFragment<V, P> fragment, Consumer<BaseMvpFragment<V, P>> fragmentConsumer) {
        try {
            Optional.from(fragment).doIfPresent(f -> {
                fragmentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(containerViewId, f)
                        //.addToBackStack(null)
                        .commitAllowingStateLoss();
                if (fragmentConsumer != null) fragmentConsumer.accept(f);
            });


        } catch (ClassCastException e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * clear cache fragment in view pager
     *
     * @param activity
     * @param fragments
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void clearCacheFragment(FragmentActivity activity, List<BaseMvpFragment<V, P>> fragments) {
        try {
            // remove all cache in viewpager
            if (fragments != null && fragments.size() > 0) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                for (BaseMvpFragment<V, P> fragment : fragments) {
                    if (fragment != null) {
                        fragmentTransaction.remove(fragment);
                    }
                }
                fragmentTransaction.commitAllowingStateLoss();
                // clear data
                fragments.clear();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }

    /**
     * clear fragment
     *
     * @param activity
     * @param fragment
     */
    public static <V extends IBaseMvpView, P extends IBaseDataPresenter<V>> void clearCacheFragment(FragmentActivity activity, BaseMvpFragment<V, P> fragment) {
        try {
            // remove all cache in viewpager
            if (fragment != null) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
    }
}
