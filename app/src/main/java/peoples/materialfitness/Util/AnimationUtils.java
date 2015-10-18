package peoples.materialfitness.Util;

import android.animation.Animator;

/**
 * Created by Alex Sullivan on 10/17/2015.
 */
public class AnimationUtils
{
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
