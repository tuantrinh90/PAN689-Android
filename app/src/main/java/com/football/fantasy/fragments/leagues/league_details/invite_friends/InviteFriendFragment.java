package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.util.SharedUtils;
import com.bon.util.StringUtils;
import com.football.adapters.InviteFriendAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.responses.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendFragment extends BaseMainMvpFragment<IInviteFriendView, IInviteFriendPresenter<IInviteFriendView>> implements IInviteFriendView {
    public static InviteFriendFragment newInstance() {
        return new InviteFriendFragment();
    }

    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.llInvite)
    LinearLayout llInvite;

    List<Friend> friends;
    InviteFriendAdapter inviteFriendAdapter;

    @Override
    public int getResourceId() {
        return R.layout.invite_friend_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        svSearch.getFilter().setVisibility(View.GONE);
        svSearch.setSearchConsumer(query -> {
            rvRecyclerView.setVisibility(StringUtils.isEmpty(query) ? View.GONE : View.VISIBLE);
            llInvite.setVisibility(StringUtils.isEmpty(query) ? View.VISIBLE : View.GONE);
        });

        friends = new ArrayList<Friend>() {{
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", true));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", true));
            add(new Friend("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Bruce Wayne", false));
        }};
        inviteFriendAdapter = new InviteFriendAdapter(mActivity, friends, detailFriend -> {

        }, inviteFriend -> {

        });
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvRecyclerView.setAdapter(inviteFriendAdapter);
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
}
