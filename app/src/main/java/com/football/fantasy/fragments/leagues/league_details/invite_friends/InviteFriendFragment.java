package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.util.SharedUtils;
import com.bon.util.StringUtils;
import com.football.adapters.InviteFriendAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.responses.FriendResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendFragment extends BaseMainMvpFragment<IInviteFriendView, IInviteFriendPresenter<IInviteFriendView>> implements IInviteFriendView {

    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    public static InviteFriendFragment newInstance(int leagueId) {
        InviteFriendFragment fragment = new InviteFriendFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.llInvite)
    LinearLayout llInvite;

    private int leagueId;
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

    private void getFriends(String keyword) {
        rvRecyclerView.startLoading();
        presenter.getInviteFriends(leagueId, keyword);
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
    }

    void initView() {
        svSearch.getFilter().setVisibility(View.GONE);
        svSearch.setSearchConsumer(query -> {
            rvRecyclerView.setVisibility(StringUtils.isEmpty(query) ? View.GONE : View.VISIBLE);
            llInvite.setVisibility(StringUtils.isEmpty(query) ? View.VISIBLE : View.GONE);
            getFriends(query);
        });

        inviteFriendAdapter = new InviteFriendAdapter(mActivity, new ArrayList<>(), detailFriend -> {

        }, inviteFriend -> {
            presenter.inviteFriend(leagueId, inviteFriend.getId());
        });
        rvRecyclerView.init(mActivity, inviteFriendAdapter)
                .setOnExtRefreshListener(() -> {
                    getFriends(svSearch.getSearchView().getText().toString());
                });
    }

    @NonNull
    @Override
    public IInviteFriendPresenter<IInviteFriendView> createPresenter() {
        return new InviteFriendDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvInvite)
    void onClickInvite() {
        SharedUtils.actionShare(mActivity, getString(R.string.app_name), SharedUtils.TYPE_TEXT, null, getString(R.string.app_name),
                "https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", null, null);
    }

    @Override
    public void displayFriends(List<FriendResponse> friends) {
        inviteFriendAdapter.notifyDataSetChanged(friends);
        rvRecyclerView.stopLoading();
        rvRecyclerView.setMessage(friends.isEmpty() ? "No data" : "");
        rvRecyclerView.displayMessage();
    }

    @Override
    public void inviteSuccess(Integer receiverId) {
        for (FriendResponse friend : inviteFriendAdapter.getItems()) {
            if (friend.getId().equals(receiverId)) {
                friend.setInvited(true);
                break;
            }
        }
        inviteFriendAdapter.notifyDataSetChanged();
    }
}
