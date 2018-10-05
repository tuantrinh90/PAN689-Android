package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.SharedUtils;
import com.bon.util.StringUtils;
import com.football.adapters.InviteFriendAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.responses.FriendResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.AppUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.utilities.ServiceConfig.DEEP_LINK;
import static com.football.utilities.ServiceConfig.SHARE_APP;

public class InviteFriendFragment extends BaseMvpFragment<IInviteFriendView, IInviteFriendPresenter<IInviteFriendView>> implements IInviteFriendView {
    private static final String TAG = InviteFriendFragment.class.getSimpleName();
    private static final String KEY_LEAGUE = "LEAGUE";
    private static final String KEY_LEAGUE_TYPE = "league_type";
    private static final String KEY_STARTED = "KEY_STARTED";

    public static InviteFriendFragment newInstance(LeagueResponse league, String leagueType, boolean started) {
        InviteFriendFragment fragment = new InviteFriendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        bundle.putBoolean(KEY_STARTED, started);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.rvFriend)
    ExtRecyclerView<FriendResponse> rvFriend;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.llInvite)
    LinearLayout llInvite;

    private boolean started;

    String leagueType;
    private LeagueResponse league;
    int page = 1;

    InviteFriendAdapter inviteFriendAdapter;

    @Override
    public int getResourceId() {
        return R.layout.invite_friend_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getFriends(String keyword) {
        presenter.getInviteFriends(league.getId(), keyword, page);
    }

    void getDataFromBundle() {
        Bundle bundle = getArguments();
        league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
        leagueType = bundle.getString(KEY_LEAGUE_TYPE);
        started = bundle.getBoolean(KEY_STARTED);
    }

    void initView() {
        try {
            svSearch.getFilter().setVisibility(View.GONE);
            svSearch.setSearchConsumer(query -> {
                rvFriend.setVisibility(StringUtils.isEmpty(query) ? View.GONE : View.VISIBLE);
                llInvite.setVisibility(StringUtils.isEmpty(query) ? View.VISIBLE : View.GONE);
                rvFriend.clear();
                rvFriend.startLoading();
                page = 1;
                getFriends(query.trim());
            });

            inviteFriendAdapter = new InviteFriendAdapter(
                    getContext(),
                    detailFriend -> { // detail

                    },
                    friend -> { // invite event
                        if (AppUtilities.isSetupTime(league) && league.getCurrentNumberOfUser().equals(league.getNumberOfUser())) {
                            showMessage(getString(R.string.message_unable_to_invite_friend));
                        } else {
                            presenter.inviteFriend(league.getId(), friend.getId());
                        }
                    },
                    started);

            rvFriend
                    .adapter(inviteFriendAdapter)
                    .refreshListener(() -> {
                        Optional.from(rvFriend).doIfPresent(rv -> rv.clear());
                        page = 1;
                        getFriends(svSearch.getSearchView().getText().toString());
                    })
                    .loadMoreListener(() -> {
                        page++;
                        getFriends(svSearch.getSearchView().getText().toString());
                    })
                    .build();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IInviteFriendPresenter<IInviteFriendView> createPresenter() {
        return new InviteFriendDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvInvite)
    void onClickInvite() {
        SharedUtils.actionShare(mActivity, getString(R.string.app_name), SharedUtils.TYPE_TEXT, null, getString(R.string.app_name),
                SHARE_APP + "?url=" + DEEP_LINK + "?league_id=" + league.getId(), null, null);
    }

    @Override
    public void displayFriends(List<FriendResponse> friends) {
        Optional.from(rvFriend).doIfPresent(rv -> rv.addItems(friends));
    }

    @Override
    public void inviteSuccess(Integer receiverId) {
        try {
            for (FriendResponse friend : inviteFriendAdapter.getDataSet()) {
                if (friend.getId().equals(receiverId)) {
                    friend.setInvited(true);
                    break;
                }
            }

            inviteFriendAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
//        Optional.from(rvFriend).doIfPresent(rv -> {
//            if (isLoading) {
//                rv.startLoading();
//            } else {
//                rv.stopLoading();
//            }
//        });
    }
}
