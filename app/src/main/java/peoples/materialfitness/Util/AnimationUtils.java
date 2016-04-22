package peoples.materialfitness.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Alex Sullivan on 10/17/2015.
 * <p>
 * Simple animation utility class.
 */
public class AnimationUtils
{
    public static void circularRevealFadeInView(View v, AnimatorListenerAdapter listenerAdapter)
    {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(v, View.ALPHA, 0,1);

        int animDuration = v.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        alphaAnimator.setDuration(animDuration);

        Animator circularHideAnimator = getCircularRevealAnimator(v, listenerAdapter);

        animatorSet.playTogether(alphaAnimator, circularHideAnimator);
        animatorSet.start();
    }

    public static void circularHideFadeOutView(View v, AnimatorListenerAdapter listenerAdapter)
    {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(v, View.ALPHA, 1,0);
        int animDuration = v.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        alphaAnimator.setDuration(animDuration);

        Animator circularHideAnimator = getCircularHideAnimator(v, listenerAdapter);

        animatorSet.playTogether(alphaAnimator, circularHideAnimator);
        animatorSet.start();
    }

    public static void circularRevealView(final View v, AnimatorListenerAdapter listenerAdapter)
    {

        getCircularRevealAnimator(v, listenerAdapter).start();
    }

    public static void circularHideView(View v, AnimatorListenerAdapter listenerAdapter)
    {
        getCircularHideAnimator(v, listenerAdapter).start();
    }

    public static Animator getCircularHideAnimator(View v, AnimatorListenerAdapter listenerAdapter)
    {
        // get the center for the clipping circle
        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = v.getWidth();

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, cx, cy, initialRadius, 0);

        int animDuration = v.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        anim.setDuration(animDuration);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
                if (listenerAdapter == null) return;
                listenerAdapter.onAnimationEnd(animation);
            }
        });

        return anim;
    }

    public static Animator getCircularRevealAnimator(View v, AnimatorListenerAdapter listenerAdapter)
    {
        // get the center for the clipping circle
        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(v.getWidth(), v.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);

        int animDuration = v.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        anim.setDuration(animDuration);
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                v.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animator)
            {
                if (listenerAdapter == null) return;
                listenerAdapter.onAnimationEnd(animator);
            }
        });

        return anim;
    }

    public static void fadeInView(View v)
    {
        v.animate().alpha(1).start();
    }

    public static void fadeOutView(View v)
    {
        v.animate().alpha(0).start();
    }

    public static void fadeVisibilityChange(final View view, final int visibility)
    {
        view.animate().alpha(visibility == View.VISIBLE ? 1 : 0).setListener(new EndAnimatorListener(() -> {
            view.animate().setListener(null);
            view.setVisibility(visibility);
        })).start();
    }

    public static class EndAnimatorListener implements Animator.AnimatorListener
    {
        private Runnable mEndRunnable;

        public EndAnimatorListener(Runnable endRunnable)
        {
            mEndRunnable = endRunnable;
        }
        @Override
        public void onAnimationStart(Animator animation)
        {

        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (mEndRunnable != null)
            {
                mEndRunnable.run();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {

        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {

        }
    }
}
