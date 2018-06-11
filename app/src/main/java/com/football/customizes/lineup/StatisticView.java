package com.football.customizes.lineup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticView extends FrameLayout {


    @IntDef({Position.GOALKEEPER, Position.DEFENDER,
            Position.MIDFIELDER, Position.ATTACKER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {

        int NONE = -1;

        int GOALKEEPER = 0;

        int DEFENDER = 1;

        int MIDFIELDER = 2;

        int ATTACKER = 3;

    }

    @IntDef({Position.GOALKEEPER, Position.DEFENDER,
            Position.MIDFIELDER, Position.ATTACKER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PositionValue {

        String NONE = "";

        String GOALKEEPER = "goalkeeper";

        String DEFENDER = "defender";

        String MIDFIELDER = "midfielder";

        String ATTACKER = "attacker";

    }

    @BindView(R.id.tvPosition)
    ExtTextView tvPosition;
    @BindView(R.id.tvCount)
    ExtTextView tvCount;

    private int position;

    public StatisticView(@NonNull Context context) {
        this(context, null, 0);
    }

    public StatisticView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatisticView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            setPositionText(position);
        } else {
            tvPosition.setBackgroundResource(R.drawable.bg_player_position_none);
        }
        super.setSelected(selected);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.statistic_view, this);
        ButterKnife.bind(this, view);

        // type array
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatisticView, 0, 0);

        // position
        position = typedArray.getInt(R.styleable.StatisticView_sv_position, Position.GOALKEEPER);
        setPositionText(position);

        // count
        int count = typedArray.getInt(R.styleable.StatisticView_sv_playerCount, 0);
        tvCount.setText(String.valueOf(count));

    }

    private void setPositionText(int position) {
        switch (position) {
            case Position.GOALKEEPER:
                tvPosition.setText("G");
                tvPosition.setBackgroundResource(R.drawable.bg_player_position_g);
                break;
            case Position.DEFENDER:
                tvPosition.setText("D");
                tvPosition.setBackgroundResource(R.drawable.bg_player_position_d);
                break;
            case Position.MIDFIELDER:
                tvPosition.setText("M");
                tvPosition.setBackgroundResource(R.drawable.bg_player_position_m);
                break;
            case Position.ATTACKER:
                tvPosition.setText("A");
                tvPosition.setBackgroundResource(R.drawable.bg_player_position_a);
                break;
            default:
                int height = getContext().getResources().getDimensionPixelSize(R.dimen.player_position_size);
                tvPosition.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, height));
                tvCount.setVisibility(INVISIBLE);
                tvPosition.setBackgroundResource(R.drawable.bg_player_position_none);
                tvPosition.setText("ALL");
                tvPosition.setTextColor(ContextCompat.getColor(getContext(), R.color.color_gray));
                break;
        }
    }

    public void setCount(int count) {
        tvCount.setText(String.valueOf(count));
    }
}
