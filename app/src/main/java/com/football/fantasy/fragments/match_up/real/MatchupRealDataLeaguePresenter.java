package com.football.fantasy.fragments.match_up.real;

import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.RealMatch;
import com.football.models.responses.RealMatchResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.RxUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.football.fantasy.fragments.match_up.real.MatchupRealLeagueFragment.ROUND_DEFAULT;

public class MatchupRealDataLeaguePresenter extends BaseDataPresenter<IMatchupRealLeagueView> implements IMatchupRealLeaguePresenter<IMatchupRealLeagueView> {

    protected MatchupRealDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getRealMatches(String round, int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            if (!TextUtils.isEmpty(round) && !round.equals(ROUND_DEFAULT)) {
                queries.put("round", round);
            }
            queries.put("page", String.valueOf(page));
            queries.put("orderBy", "end_at");
            queries.put("sortedBy", "desc");
            queries.put("per_page", "10");

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getRealMatches(queries),
                    new ApiCallback<PagingResponse<RealMatchResponse>>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<RealMatchResponse> response) {
                            convertData(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    private void convertData(PagingResponse<RealMatchResponse> response) {
        mCompositeDisposable.add(Single
                .create((SingleOnSubscribe<List<RealMatch>>) emitter -> {
                    Map<String, List<RealMatchResponse>> hashMap = new HashMap<>();

                    List<String> dates = new ArrayList<>();
                    List<RealMatchResponse> realMatches = response.getData();

                    for (RealMatchResponse realMatch : realMatches) {
                        String date = AppUtilities.getDate(realMatch.getStartAt());
                        if (!hashMap.containsKey(date)) {
                            List<RealMatchResponse> list = new ArrayList<>();
                            list.add(realMatch);

                            hashMap.put(date, list);

                            dates.add(date);
                        } else {
                            hashMap.get(date).add(realMatch);
                        }
                    }

                    List<RealMatch> matches = new ArrayList<>();
                    for (String date : dates) {
                        matches.add(new RealMatch(date, hashMap.get(date)));
                    }
                    emitter.onSuccess(matches);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Objects.requireNonNull(getOptView().get())::displayRealMatches));
    }
}
