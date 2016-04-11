package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.HeightAnimator;

/**
 * Created by Alex Sullivan on 1/24/16.
 */
public class ExerciseCardRecyclerAdapter extends RecyclerView.Adapter<ExerciseCardRecyclerAdapter.ExerciseCardViewHolder>
{
    private WorkoutSession mWorkoutSession;
    private ExerciseCardAdapterInterface mCallback;
    private static final int SHOW_SPILLOVER_ANIMATION_DURATION = 600;

    private static final int NUM_DISPLAY_SETS = 5;

    public ExerciseCardRecyclerAdapter(WorkoutSession workoutSession,
                                       ExerciseCardAdapterInterface callback)
    {
        mWorkoutSession = workoutSession;
        mCallback = callback;
    }

    @Override
    public ExerciseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView cardView = (CardView)inflater.inflate(R.layout.fragment_workout_card, parent, false);

        ExerciseCardViewHolder viewHolder = new ExerciseCardViewHolder(cardView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExerciseCardViewHolder holder, int position)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(position);

        holder.mTitleView.setText(exerciseSession.getExercise().getTitle());
        // Clear our any old views.
        holder.mRepContainer.removeAllViews();
        // Now loop through our weights and add each one to our container.
        for (int i = 0; i < exerciseSession.getSets().size(); i++)
        {
            // Show our divider view so the user can drop down to reveal more sets.
            // +1 for zeri indexing offsets :/
            if (i+1 > NUM_DISPLAY_SETS)
            {
                holder.dropDownLayout.setVisibility(View.VISIBLE);
            }

            WeightSet weightSet = exerciseSession.getSets().get(i);
            addWeightSetViewToHolder(holder, weightSet);
            // Lets only do this once for performance. Lock the linear layout to the current height,
            // since we want to include the extra items but we don't want the layout to grow with them.
            // We will grow the layout via animation if the user selects the dropdown.
            // +1 for zero indexing offset.
            if (i+1 == NUM_DISPLAY_SETS && !holder.isDropdownToggled)
            {
                LinearLayout repContainer = holder.mRepContainer;
                repContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                // Lock our view at a certain height.
                ViewGroup.LayoutParams oldParams = repContainer.getLayoutParams();
                oldParams.height = repContainer.getMeasuredHeight();
                repContainer.setLayoutParams(oldParams);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mWorkoutSession.getExercises().size();
    }

    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        if (!mWorkoutSession.containsExercise(exerciseSession.getExercise()))
        {
            mWorkoutSession.addExerciseSession(exerciseSession);
        }

        notifyItemInserted(mWorkoutSession.getExercises().size() - 1);
    }

    /**
     * Add a weight set view to the view holder. Convenience method to add a set view to the container.
     * @param holder Holder to add to.
     * @param weightSet The weight set whos view we're constructing.
     */
    private void addWeightSetViewToHolder(ExerciseCardViewHolder holder, WeightSet weightSet)
    {
        LayoutInflater inflater = LayoutInflater.from(holder.mCardView.getContext());
        LinearLayout setContainer = (LinearLayout)inflater.inflate(R.layout.workout_card_rep, holder.mRepContainer, false);

        TextView repsTextView = (TextView)setContainer.findViewById(R.id.num_reps);
        TextView weightTextView = (TextView)setContainer.findViewById(R.id.weight);

        String repsString = String.valueOf(weightSet.getNumReps());
        // TODO: Use our weight units. Should be attached to the rep.
        String weightString = holder.mCardView.getContext().getResources().getString(R.string.weight_units, weightSet.getWeight(), "lbs");

        repsTextView.setText(repsString);
        weightTextView.setText(weightString);

        holder.mRepContainer.addView(setContainer);
    }

    private void onDropdownClicked(ExerciseCardViewHolder viewHolder)
    {
        animateSpilloverSetsToggle(viewHolder, !viewHolder.isDropdownToggled);
        viewHolder.isDropdownToggled = !viewHolder.isDropdownToggled;
    }

    /**
     * Animate showing or hiding our spillover sets. We do two basic animations here - animating the
     * height of the rep container and doing a rotation animation on the dropdown indicator.
     * @param viewHolder Viewholder to animate
     * @param shouldExpand Boolean telling us if we're showing the spillover sets or hiding them.
     */
    private void animateSpilloverSetsToggle(ExerciseCardViewHolder viewHolder, boolean shouldExpand)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(viewHolder.getAdapterPosition());
        int spilloverSetsNum = exerciseSession.getSets().size() - NUM_DISPLAY_SETS;
        // Note: We know we have at least NUM_DISPLAY_SETS children here, which is why this is safe.
        // TODO: However, we're not really sure that the items in the recyclerview are this height.
        // They are right now but I'm sure that'll change.
        View firstChild = viewHolder.mRepContainer.getChildAt(0);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)firstChild.getLayoutParams();

        int totalHeight = firstChild.getHeight() + layoutParams.bottomMargin + layoutParams.topMargin + firstChild.getPaddingBottom();
        int heightOfSpilloverSets = totalHeight * spilloverSetsNum;
        int heightOfContainer = viewHolder.mRepContainer.getHeight();

        int desiredHeight = 0;
        float startRotation = 0;
        float endRotation = 0;

        if (shouldExpand)
        {
            desiredHeight = heightOfContainer + heightOfSpilloverSets;
            startRotation = 0f;
            endRotation = 180f;
        }
        else
        {
            desiredHeight = heightOfContainer - heightOfSpilloverSets;
            startRotation = 180f;
            endRotation = 360f;
        }

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(viewHolder.dividerView, "rotation", startRotation, endRotation);
        HeightAnimator heightAnimator = new HeightAnimator(viewHolder.mRepContainer, desiredHeight);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(heightAnimator, rotationAnimator);
        animatorSet.setDuration(SHOW_SPILLOVER_ANIMATION_DURATION);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
    }

    private void onCardClicked(ExerciseCardViewHolder viewHolder)
    {
        ExerciseSession exerciseSession = mWorkoutSession.getExercises().get(viewHolder.getAdapterPosition());
        mCallback.onExerciseClicked(exerciseSession);
    }

    /**
     * Interface for communication between this adapter and the calling/creating class.
     */
    public interface ExerciseCardAdapterInterface
    {
        void onExerciseClicked(ExerciseSession session);
    }

    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        mWorkoutSession = workoutSession;
        notifyDataSetChanged();
    }


    protected class ExerciseCardViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;

        @Bind(R.id.exercise_title) TextView mTitleView;
        @Bind(R.id.rep_container) LinearLayout mRepContainer;
        @Bind(R.id.dropdown_layout) LinearLayout dropDownLayout;
        @Bind(R.id.dropdown_indicator) ImageView dividerView;

        boolean isDropdownToggled = false;

        public ExerciseCardViewHolder(CardView cardView)
        {
            super(cardView);

            mCardView = cardView;

            ButterKnife.bind(this, cardView);
        }

        @OnClick(R.id.card_view)
        public void onCardClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onCardClicked(this);
        }

        @OnClick(R.id.dropdown_layout)
        public void onDropDownClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onDropdownClicked(this);
        }
    }
}
