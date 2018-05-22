package com.football.di;

import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.common.presenters.BaseDataPresenter;
import com.football.common.views.IBaseMvpView;

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

    void inject(BaseDataPresenter<IBaseMvpView> presenter);

    void inject(BaseMvpFragment<IBaseMvpView, BaseDataPresenter<IBaseMvpView>> fragment);
}
