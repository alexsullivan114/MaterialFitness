package peoples.materialfitness.Util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex Sullivan on 3/29/16.
 */
public class HeightAnimator extends ValueAnimator
{
    View view;
    int desiredHeight;

    public HeightAnimator(View view, int desiredHeight)
    {
        this.view = view;
        this.desiredHeight = desiredHeight;
        setIntValues(view.getHeight(), desiredHeight);

        addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = val;
            view.setLayoutParams(layoutParams);
        });
    }
}
