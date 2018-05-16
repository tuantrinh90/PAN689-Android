package com.bon.view_animation.specials.in;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.bon.view_animation.BaseViewAnimator;
import com.bon.view_animation.easing.Glider;
import com.bon.view_animation.easing.Skill;

public class LandingAnimator extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        try {
            getAnimatorAgent().playTogether(
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleX", 1.5f, 1f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "scaleY", 1.5f, 1f)),
                    Glider.glide(Skill.QuintEaseOut, getDuration(), ObjectAnimator.ofFloat(target, "alpha", 0, 1f))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
