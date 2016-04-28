package peoples.materialfitness.WorkoutDetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.R;

/**
 * Created by Alex Sullivan on 2/15/16.
 */
public class WorkoutDetailsRecyclerAdapter extends RecyclerView.Adapter<WorkoutDetailsRecyclerAdapter.RepViewHolder>
{
    private static final String TAG = WorkoutDetailsRecyclerAdapter.class.getSimpleName();

    private final ExerciseSession exerciseSession;
    private final boolean allowTouchEvents;

    public WorkoutDetailsRecyclerAdapter(final @NonNull ExerciseSession exerciseSession,
                                         final boolean allowTouchEvents)
    {
        this.exerciseSession = exerciseSession;
        this.allowTouchEvents = allowTouchEvents;
    }

    @Override
    public RepViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View repLayout = inflater.inflate(R.layout.rep_adapter_layout, parent, false);
        return new RepViewHolder(repLayout);
    }

    @Override
    public void onBindViewHolder(RepViewHolder holder, int position)
    {
        WeightSet set = exerciseSession.getSets().get(position);

        // TODO: Weight unit stuff.
        String formattedString = holder.positionTextView.getContext()
                .getResources()
                .getString(R.string.weight_units, set.getWeight(), "lbs");
        holder.weightTextView.setText(formattedString);
        holder.positionTextView.setText(String.valueOf(position + 1));
        holder.numRepsTextView.setText(String.valueOf(set.getNumReps()));

        if (set.getIsPr())
        {
            holder.prImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.prImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return exerciseSession.getSets().size();
    }

    protected final class RepViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener
    {
        @Bind(R.id.position) TextView positionTextView;
        @Bind(R.id.num_reps) TextView numRepsTextView;
        @Bind(R.id.weight) TextView weightTextView;
        @Bind(R.id.pr_image) ImageView prImageView;
        @Bind(R.id.contentView) LinearLayout contentView;
        @Bind(R.id.topContainer) FrameLayout topContainer;

        private float originalXPosition;

        private float originalTouchY;
        private float originalTouchX;

        final static int horizontalThreshold = 50;
        final static int verticalThreshold = 100;

        private int traveledHorizontalDistance = 0;
        private int traveledVerticalDistance = 0;

        private boolean isSwiping = false;

        public RepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (allowTouchEvents)
            {
                contentView.setOnTouchListener(this);
            }
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

                    break;
                }
                case MotionEvent.ACTION_MOVE:
                {
                    traveledHorizontalDistance += Math.abs(originalTouchX - event.getRawX());
                    traveledVerticalDistance += Math.abs(originalTouchY - event.getRawY());

                    Log.d(TAG, "Traveled Horizontal Distance: " + String.valueOf(traveledHorizontalDistance));
                    Log.d(TAG, "Traveled Vertical Distance: " + String.valueOf(traveledVerticalDistance));

                    if (traveledVerticalDistance >= verticalThreshold && !isSwiping)
                    {
                        return false; // We're considering this a scroll up so bounce.
                    }

                    if (traveledHorizontalDistance >= horizontalThreshold)
                    {
                        isSwiping = true;
                        topContainer.getParent().requestDisallowInterceptTouchEvent(true);

                        view.animate()
                                .x(originalXPosition + (event.getRawX() - originalTouchX))
                                .setDuration(0)
                                .start();
                    }

                    break;
                }
                case MotionEvent.ACTION_UP:
                {
                    topContainer.getParent().requestDisallowInterceptTouchEvent(false);
                }
                default:
                    return false;
            }
            return true;
        }
    }
}
