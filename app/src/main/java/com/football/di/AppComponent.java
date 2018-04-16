package com.football.di;

import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.common.presenters.BaseDataPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dangpp on 2/9/2018.
 */

@Singleton
@Component(modules = {
        ApiModule.class,
        DbModule.class,
        DataModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(DataModule dataModule);

    void inject(DbModule dbModule);

    void inject(AppContext appContext);

    void inject(BaseAppCompatActivity activity);

    void inject(BaseDataPresenter<MvpView> presenter);

    void inject(BaseMvpFragment<MvpView, MvpPresenter<MvpView>> fragment);
}
