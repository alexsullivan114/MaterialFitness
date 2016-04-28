package peoples.materialfitness.Util.AnimationHelpers;

import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import peoples.materialfitness.Util.ScreenUtils;

/**
 * Created by Alex Sullivan on 4/28/2016.
 *
 * This class is a pretty simple {@link android.view.View.OnTouchListener} to help facilitate
 * a "swipe to reveal" style of recyclerview. Note that this class isn't a full solution - it just
 * helps to coordinate the animation done on a top level view.
 */
public class SwipeToRevealItemTouchHelper implements View.OnTouchListener
{
    // Original X position of the touched view. We use this to remember where the touched views
    // X position was at the down press moment so we can animate from there.
    private float originalXPosition;

    // Original Y position of the initial down touch. We use this to calculate how far the user has
    // travelled in the Y coordinate (to see if we should disable or allow touch events on the parent
    // recyclerview
    private float originalTouchY;
    // Similar to to the Y original touch variable. This helps us determine if we should begin the
    // animation or allow the user to scroll their recyclerview
    private float originalTouchX;
    // Horizontal pixel threshold at which we should begin animating
    final static int horizontalThreshold = 50;
    // Vertical pixel threshold at which point we should assume the user is just scrolling.
    final static int verticalThreshold = 100;
    // Distance the users finger has traveled in the x axis
    private int traveledHorizontalDistance = 0;
    // same but in the Y axis
    private int traveledVerticalDistance = 0;
    // Remember if the user is swiping rather than scrolling.
    private boolean isSwiping = false;
    // Our callback to notify of important events
    private final ItemInteractionCallback itemInteractionCallback;
    // A DIRECT (top level) child of the recyclerview. We use this child to tell its parent
    // to stop receiving touch inputs.
    private final View recyclerviewDirectChild;
    // The left view we want to "reveal". This view is sticky, so if the user swipes past it the
    // animation will come to rest on the right edge of this view.
    private final View leftStickyView;
    // Same as the above except the right view. The animation will end on the left edge of this view.
    private final View rightStickyView;


    public SwipeToRevealItemTouchHelper(ItemInteractionCallback itemInteractionCallback,
                                        View recyclerviewDirectChild,
                                        View leftStickyView,
                                        View rightStickyView)
    {
        this.itemInteractionCallback = itemInteractionCallback;
        this.recyclerviewDirectChild = recyclerviewDirectChild;
        this.leftStickyView = leftStickyView;
        this.rightStickyView = rightStickyView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            {
                originalTouchX = event.getRawX();
                originalTouchY = event.getRawY();

                originalXPosition = view.getX();

                traveledHorizontalDistance = 0;
                traveledVerticalDistance = 0;

                itemInteractionCallback.itemTouched(view);

                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                traveledHorizontalDistance += Math.abs(originalTouchX - event.getRawX());
                traveledVerticalDistance += Math.abs(originalTouchY - event.getRawY());

                if (traveledVerticalDistance >= verticalThreshold && !isSwiping)
                {
                    return false; // We're considering this a scroll up so bounce.
                }

                if (traveledHorizontalDistance >= horizontalThreshold)
                {
                    isSwiping = true;

                    recyclerviewDirectChild.getParent().requestDisallowInterceptTouchEvent(true);

                    view.animate()
                            .x(originalXPosition + (event.getRawX() - originalTouchX))
                            .setDuration(0)
                            .start();
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            {
                float x = 0;

                if (view.getX() > leftStickyView.getX() + leftStickyView.getWidth())
                {
                    x = leftStickyView.getX() + leftStickyView.getWidth();
                }
                else if (view.getX() + view.getWidth() < rightStickyView.getX())
                {
                    int screenWidth = ScreenUtils.getScreenWidth();
                    x = -1 * (screenWidth - rightStickyView.getX());
                }

                if (x != 0)
                {
                    itemInteractionCallback.itemRevealed(view);
                }

                view.animate()
                        .x(x)
                        .setDuration(x != 0 ? 600 : 200)
                        .setInterpolator(x != 0 ? new OvershootInterpolator() : new FastOutLinearInInterpolator())
                        .start();

                recyclerviewDirectChild.getParent().requestDisallowInterceptTouchEvent(false);
            }
            default:
                return false;
        }
        return true;
    }

    public interface ItemInteractionCallback
    {
        void itemTouched(View v);
        void itemRevealed(View v);
    }
}
