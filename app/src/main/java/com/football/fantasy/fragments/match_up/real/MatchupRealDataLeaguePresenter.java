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

public class MatchupRealDataLeaguePresenter extends BaseDataPresenter<IMatchupRealLeagueView> implements IMatchupRealLeaguePresenter<IMatchupRealLeagueView> {

    private String lastDate = "";

    protected MatchupRealDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getRealMatches(String round, int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            if (!TextUtils.isEmpty(round)) {
                queries.put("round", round);
            }
            queries.put("page", String.valueOf(page));
            queries.put("orderBy", "end_at");
            queries.put("sortedBy", "desc");

            if (page == 1) {
                lastDate = "";
            }

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getRealMatches(queries),
                    new ApiCallback<PagingResponse<RealMatchResponse>>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(PagingResponse<RealMatchResponse> response) {
                            Map<String, List<RealMatchResponse>> hashMap = new HashMap<>();

                            List<String> dates = new ArrayList<>();
                            List<RealMatchResponse> realMatches = response.getData();

                            for (RealMatchResponse realMatch : realMatches) {
                                String date = AppUtilities.getDate(realMatch.getEndAt());
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
                            if (matches.size() > 0) {
                                lastDate = matches.get(matches.size() - 1).getDate();
                            }
                            v.displayRealMatches(matches);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
