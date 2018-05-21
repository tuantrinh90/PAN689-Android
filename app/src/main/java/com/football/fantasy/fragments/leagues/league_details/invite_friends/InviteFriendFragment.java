package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.StringUtils;
import com.football.adapters.InviteFriendAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class InviteFriendFragment extends BaseMvpFragment<IInviteFriendView, IInviteFriendPresenter<IInviteFriendView>> implements IInviteFriendView {
    public static InviteFriendFragment newInstance() {
        return new InviteFriendFragment();
    }

    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.tvInviteFriend)
    ExtTextView tvInviteFriend;

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
            tvInviteFriend.setVisibility(StringUtils.isEmpty(query) ? View.VISIBLE : View.GONE);
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

    @Override
    public IInviteFriendPresenter<IInviteFriendView> createPresenter() {
        return new InviteFriendPresenter(getAppComponent());
    }
}
