package peoples.materialfitness.Util.CustomAnimations;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

/**
 * Created by Alex Sullivan on 5/7/2016.
 */
@SuppressLint("NewApi")
public class CircularRevealTransition extends Transition
{
    private static final String PROPNAME_END_RADIUS = "com.peoples.materialfitness:circularreveal:size";
    private static final String PROPNAME_CX = "com.peoples.materialfitness:circularreveal:cx";
    private static final String PROPNAME_CY = "com.peoples.materialfitness:circularreveal:cy";

    private float endRadius = -1;
    private float startingCx = -1;
    private float startingCy = -1;

    private View startingPositionCenteringView;

    public CircularRevealTransition(float endRadius, float startingCx, float startingCy)
    {
        this.endRadius = endRadius;
        this.startingCx = startingCx;
        this.startingCy = startingCy;
    }

    public CircularRevealTransition(View startingPositionCenteringView)
    {
        this.startingPositionCenteringView = startingPositionCenteringView;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues)
    {
        View view = transitionValues.view;


        transitionValues.values.put(PROPNAME_END_RADIUS, this.endRadius);
        transitionValues.values.put(PROPNAME_CX, this.startingCx);
        transitionValues.values.put(PROPNAME_CY, this.startingCy);
     }

    @Override
    public void captureEndValues(TransitionValues transitionValues)
    {
        View view = transitionValues.view;

        float cx, cy;

        if (this.startingCx != -1)
        {
            cx = startingCx;
            cy = startingCy;
        }
        else if (startingPositionCenteringView == null)
        {
            cx = view.getWidth() / 2;
            cy = view.getHeight() / 2;
        }
        else
        {
            cx = startingPositionCenteringView.getRight() - startingPositionCenteringView.getWidth() / 2;
            cy = startingPositionCenteringView.getTop() + startingPositionCenteringView.getHeight()/ 2;
        }

        transitionValues.values.put(PROPNAME_END_RADIUS, Math.max(view.getWidth(), view.getHeight()));
        transitionValues.values.put(PROPNAME_CX, cx);
        transitionValues.values.put(PROPNAME_CY,cy);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues)
    {
        if (endValues.view == null)
        {
            return null;
        }

        int cx = ((Float)endValues.values.get(PROPNAME_CX)).intValue();
        int cy  = ((Float)endValues.values.get(PROPNAME_CY)).intValue();
        int radius = (Integer)endValues.values.get(PROPNAME_END_RADIUS);

        // create the animator for this view (the start radius is zero)
        return  ViewAnimationUtils.createCircularReveal(endValues.view, cx, cy, 0, radius);
    }
}
