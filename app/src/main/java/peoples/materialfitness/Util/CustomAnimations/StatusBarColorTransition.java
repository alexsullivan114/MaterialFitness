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
 *
 * TODO: This transition isn't great since it only goes one way - start color -> end color. When we
 * do a return transition this looks bad. We can get the current status bar color pretty easily, but
 * I'm not sure how we'd go about getting the desired end color...
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class StatusBarColorTransition extends Transition
{
    private static final String START_COLOR = "startColorTransitionExtraMaterialFitness";
    private static final String END_COLOR = "endColorTransitionExtraMaterialFitness";

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
        @ColorInt int statusBarColor = sceneWindow.getStatusBarColor();

        if (statusBarColor == startingColor)
        {
            transitionValues.values.put(START_COLOR, startingColor);
        }
        else
        {
            transitionValues.values.put(START_COLOR, endingColor);
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues)
    {
        @ColorInt int statusBarColor = sceneWindow.getStatusBarColor();

        if (statusBarColor == startingColor)
        {
            transitionValues.values.put(END_COLOR, endingColor);
        }
        else
        {
            transitionValues.values.put(END_COLOR, startingColor);
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues)
    {
        @ColorInt int startingColor = (int)startValues.values.get(START_COLOR);
        @ColorInt int endingColor = (int)endValues.values.get(END_COLOR);

        ValueAnimator animator = ObjectAnimator.ofArgb(startingColor, endingColor);
        animator.addUpdateListener(animation -> sceneWindow.setStatusBarColor((Integer)animation.getAnimatedValue()));
        return animator;
    }
}
