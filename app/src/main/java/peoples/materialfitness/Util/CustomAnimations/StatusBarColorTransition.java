package peoples.materialfitness.Util.CustomAnimations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Alex Sullivan on 5/31/2016.
 *
 * TODO: Generalize this to ColorAnimator and have the status bar version extend that'n.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class StatusBarColorTransition extends Transition
{
    @ColorInt
    private final int startingColor;

    @ColorInt
    private final int endingColor;

    private final Window sceneWindow;

    public StatusBarColorTransition(@ColorInt int startingColor,
                                    @ColorInt int endingColor, Window sceneWindow)
    {
        this.startingColor = startingColor;
        this.endingColor = endingColor;
        this.sceneWindow = sceneWindow;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues)
    {

    }

    @Override
    public void captureEndValues(TransitionValues transitionValues)
    {

    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues)
    {
        ValueAnimator animator = ObjectAnimator.ofArgb(startingColor, endingColor);
        animator.addUpdateListener(animation -> sceneWindow.setStatusBarColor((Integer)animation.getAnimatedValue()));
        return animator;
    }
}
