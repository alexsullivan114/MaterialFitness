package peoples.materialfitness.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Alex Sullivan on 10/17/2015.
 * <p>
 * Simple animation utility class.
 */
public class AnimationUtils
{
    public static void circularRevealView(View v)
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

        // make the view visible and start the animation
        v.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void circularHideView(View v)
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
            }
        });

        // start the animation
        anim.start();
    }

    public static void circularFadeInView(View v)
    {
        v.animate().alpha(1).start();
    }

    public static void circularFadeOutView(View v)
    {
        v.animate().alpha(0).start();
    }

    public static Animator.AnimatorListener endListener(Runnable r)
    {
        return new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                r.run();
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        };
    }
}
