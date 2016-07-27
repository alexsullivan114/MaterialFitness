package peoples.materialfitness.View.SwipeToReveal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 7/27/16.
 */
public class SwipeToRevealLayout extends FrameLayout implements SwipeToRevealItemTouchHelper.ItemInteractionCallback
{
    private static final int CORRECT_NUM_CHILDREN = 2;
    private static final int stickingAnimationDuration = 600;
    private static final int nonStickingAnimationDuration = 200;

    private final Interpolator stickingInterpolator = new OvershootInterpolator(1.0f);
    private final Interpolator nonStickingInterpolator = new FastOutLinearInInterpolator();

    @Bind(R.id.leftButton)
    ImageView leftButton;
    @Bind(R.id.rightButton)
    ImageView rightButton;
    @Bind(R.id.rootButtonLayout)
    LinearLayout rootButtonLayout;
    @Bind(R.id.leftButtonLayout)
    FrameLayout leftButtonLayout;
    @Bind(R.id.rightButtonLayout)
    FrameLayout rightButtonLayout;

    private @DrawableRes int leftDrawableResource;
    private @DrawableRes int rightDrawableResource;
    private @ColorRes int leftSideBackgroundColor;
    private @ColorRes int rightSideBackgroundColor;

    private SwipeLayoutCallback callback;
    private SwipeToRevealItemTouchHelper touchHelper;
    private View contentView;
    private boolean swipeable = true;

    public SwipeToRevealLayout(final Context context)
    {
        super(context);
        initializeLayout();
    }

    public SwipeToRevealLayout(final Context context,
                               final AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeToRevealLayout, 0, 0);

        leftDrawableResource = typedArray.getResourceId(R.styleable.SwipeToRevealLayout_leftButtonDrawable, 0);
        rightDrawableResource = typedArray.getResourceId(R.styleable.SwipeToRevealLayout_rightButtonDrawable, 0);
        leftSideBackgroundColor = typedArray.getResourceId(R.styleable.SwipeToRevealLayout_leftBackgroundColor, R.color.colorAccent);
        rightSideBackgroundColor = typedArray.getResourceId(R.styleable.SwipeToRevealLayout_rightBackgroundColor, R.color.colorAccent);
        swipeable = typedArray.getBoolean(R.styleable.SwipeToRevealLayout_swipeable, true);

        initializeLayout();
    }

    @Override
    public void onViewAdded(View child)
    {
        super.onViewAdded(child);

        if (getChildCount() > CORRECT_NUM_CHILDREN)
        {
            throw new RuntimeException(SwipeToRevealLayout.class.getSimpleName() + " requires exactly " + (CORRECT_NUM_CHILDREN - 1) + " children!");
        }

        if (getChildCount() == CORRECT_NUM_CHILDREN)
        {
            contentView = getContentView();
            // And assign our touch helper.
            if (swipeable)
            {
                assignTouchHelper();
            }
        }
    }

    @Override
    public void itemRevealed(View v)
    {
        if (callback != null)
        {
            callback.itemRevealed(v);
        }
    }

    @Override
    public void itemMoved(View v, float newPosition)
    {
        contentView.animate()
                .x(newPosition)
                .setDuration(0)
                .start();
    }

    @Override
    public void itemTouched(View v)
    {
        if (callback != null)
        {
            callback.contentTouched();
        }
    }

    @Override
    public void itemReleased(View v, float restingPosition, boolean sticking)
    {
        animateViewToPosition(v, restingPosition, sticking);
    }

    @OnClick(R.id.leftButton)
    public void leftButtonClicked(View v)
    {
        if (callback != null)
        {
            callback.leftButtonClicked(v);
        }
    }

    @OnClick(R.id.rightButton)
    public void rightButtonClicked(View v)
    {
        if (callback != null)
        {
            callback.rightButtonClicked(v);
        }
    }

    private void animateViewToPosition(View v, float restingPosition, boolean sticking)
    {
        v.animate()
                .x(restingPosition)
                .setDuration(sticking ? stickingAnimationDuration : nonStickingAnimationDuration)
                .setInterpolator(sticking ? stickingInterpolator : nonStickingInterpolator)
                .start();
    }

    public void setCallback(SwipeLayoutCallback callback)
    {
        this.callback = callback;
    }

    public void setSwipeable(boolean swipeable)
    {
        this.swipeable = swipeable;

        if (!swipeable)
        {
            contentView.setOnTouchListener(null);
        }
        else
        {
            contentView.setOnTouchListener(touchHelper);
        }
    }

    public void returnToDefaultPositioning(boolean animated)
    {
        if (animated)
        {
            animateViewToPosition(contentView, 0, false);
        }
        else
        {
            contentView.setX(0);
        }
    }

    /**
     * Inflates our views and assigns our colors.
     */
    private void initializeLayout()
    {
        ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.swipe_reveal_layout, this);
        ButterKnife.bind(this);

        leftButton.setImageResource(leftDrawableResource);
        rightButton.setImageResource(rightDrawableResource);
        leftButtonLayout.setBackgroundResource(leftSideBackgroundColor);
        rightButtonLayout.setBackgroundResource(rightSideBackgroundColor);
    }

    /**
     * Simple helper method to assign a {@link SwipeToRevealItemTouchHelper} on our content view
     */
    private void assignTouchHelper()
    {
        touchHelper = new SwipeToRevealItemTouchHelper(this, contentView, leftButton, rightButton);
        contentView.setOnTouchListener(touchHelper);
    }

    /**
     * Fetches the content view that sits above the revealable button layout
     * @return A view representing the root content view.
     */
    private View getContentView()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);

            if (child != rootButtonLayout)
            {
                return child;
            }
        }

        throw new RuntimeException("Failed to find content view");
    }

    public interface SwipeLayoutCallback
    {
        void itemRevealed(View v);
        void contentTouched();
        void leftButtonClicked(View v);
        void rightButtonClicked(View v);
    }
}
