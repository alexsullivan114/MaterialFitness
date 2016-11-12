package peoples.materialfitness.LogWorkout.LogWorkoutFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peoples.materialfitness.Model.Exercise.Exercise;
import peoples.materialfitness.Model.ExerciseSession.ExerciseSession;
import peoples.materialfitness.Model.WeightSet.WeightSet;
import peoples.materialfitness.Model.WeightUnits.WeightUnit;
import peoples.materialfitness.Model.WorkoutSession.WorkoutSession;
import peoples.materialfitness.R;
import peoples.materialfitness.Util.CustomAnimations.HeightAnimator;
import peoples.materialfitness.Util.PreferenceManager;

/**
 * Created by Alex Sullivan on 1/24/16.
 */
public class ExerciseCardRecyclerAdapter extends RecyclerView.Adapter<ExerciseCardRecyclerAdapter.ExerciseCardViewHolder>
{
    private WorkoutSession workoutSession;
    private ExerciseCardAdapterInterface callback;
    private static final int SHOW_SPILLOVER_ANIMATION_DURATION = 600;

    private static final int NUM_DISPLAY_SETS = 5;

    private Map<Exercise, WeightSet> prs = new HashMap<>();

    public ExerciseCardRecyclerAdapter(WorkoutSession workoutSession,
                                       ExerciseCardAdapterInterface callback)
    {
        this.workoutSession = workoutSession;
        this.callback = callback;
    }

    @Override
    public ExerciseCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView cardView = (CardView)inflater.inflate(R.layout.fragment_workout_card, parent, false);

        return new ExerciseCardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ExerciseCardViewHolder holder, int position)
    {
        ExerciseSession exerciseSession = workoutSession.getExerciseSessions().get(position);

        holder.titleView.setText(exerciseSession.getExercise().getTitle());
        // Reset our height in case this view holder uses to be expanded.
        ViewGroup.LayoutParams params = holder.repContainer.getLayoutParams();
        params.height = ViewPager.LayoutParams.WRAP_CONTENT;
        holder.repContainer.setLayoutParams(params);
        // make sure to set our dropdown layout to GONE.
        holder.dropDownLayout.setVisibility(View.GONE);
        // Clear our any old views.
        holder.repContainer.removeAllViews();
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
            addWeightSetViewToHolder(holder, weightSet, exerciseSession.getExercise());
            // Lets only do this once for performance. Lock the linear layout to the current height,
            // since we want to include the extra items but we don't want the layout to grow with them.
            // We will grow the layout via animation if the user selects the dropdown.
            // +1 for zero indexing offset.
            if (i+1 == NUM_DISPLAY_SETS && !holder.isDropdownToggled)
            {
                LinearLayout repContainer = holder.repContainer;
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
        return workoutSession.getExerciseSessions().size();
    }

    public void updateExerciseCard(ExerciseSession exerciseSession)
    {
        if (!workoutSession.containsExercise(exerciseSession.getExercise()))
        {
            workoutSession.addExerciseSession(exerciseSession);
        }

        notifyItemInserted(workoutSession.getExerciseSessions().size() - 1);
    }

    /**
     * Add a weight set view to the view holder. Convenience method to add a set view to the container.
     * @param holder Holder to add to.
     * @param weightSet The weight set whos view we're constructing.
     */
    private void addWeightSetViewToHolder(ExerciseCardViewHolder holder, WeightSet weightSet, Exercise exercise)
    {
        LayoutInflater inflater = LayoutInflater.from(holder.mCardView.getContext());
        LinearLayout setContainer = (LinearLayout)inflater.inflate(R.layout.workout_card_rep, holder.repContainer, false);

        TextView repsTextView = (TextView)setContainer.findViewById(R.id.num_reps);
        TextView weightTextView = (TextView)setContainer.findViewById(R.id.weight);
        ImageView imageView = (ImageView)setContainer.findViewById(R.id.pr_image);


        WeightUnit weightUnit = PreferenceManager.getInstance().getUnits();
        String repsString = String.valueOf(weightSet.getNumReps());
        String weightString = holder.mCardView.getContext().getResources().getString(R.string.weight_units, weightSet.getUserUnitsWeight(), weightUnit.getUnitString());

        repsTextView.setText(repsString);
        weightTextView.setText(weightString);

        if (prs.containsKey(exercise))
        {
            if (weightSet.getId().equals(prs.get(exercise).getId()))
            {
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                imageView.setVisibility(View.GONE);
            }
        }

        holder.repContainer.addView(setContainer);
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
        ExerciseSession exerciseSession = workoutSession.getExerciseSessions().get(viewHolder.getAdapterPosition());
        int spilloverSetsNum = exerciseSession.getSets().size() - NUM_DISPLAY_SETS;
        // Note: We know we have at least NUM_DISPLAY_SETS children here, which is why this is safe.
        // TODO: However, we're not really sure that the items in the recyclerview are this height.
        // They are right now but I'm sure that'll change.
        View firstChild = viewHolder.repContainer.getChildAt(0);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)firstChild.getLayoutParams();

        int totalHeight = firstChild.getHeight() + layoutParams.bottomMargin + layoutParams.topMargin + firstChild.getPaddingBottom();
        int heightOfSpilloverSets = totalHeight * spilloverSetsNum;
        int heightOfContainer = viewHolder.repContainer.getHeight();

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
        HeightAnimator heightAnimator = new HeightAnimator(viewHolder.repContainer, desiredHeight);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(heightAnimator, rotationAnimator);
        animatorSet.setDuration(SHOW_SPILLOVER_ANIMATION_DURATION);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                callback.onSpilloverAnimationEnd();
            }
        });
        animatorSet.start();
    }

    private void onCardClicked(ExerciseCardViewHolder viewHolder)
    {
        ExerciseSession exerciseSession = workoutSession.getExerciseSessions().get(viewHolder.getAdapterPosition());
        callback.onExerciseClicked(exerciseSession);
    }

    public void setWorkoutSession(WorkoutSession workoutSession)
    {
        this.workoutSession = workoutSession;
        notifyDataSetChanged();
    }

    public void setWeightSetAsPr(WeightSet weightSet, Exercise exercise)
    {
        prs.put(exercise, weightSet);
        for (int i = 0; i < workoutSession.getExerciseSessions().size(); i++)
        {
            ExerciseSession exerciseSession = workoutSession.getExerciseSessions().get(i);
            if (weightSet.getExerciseSessionId() == exerciseSession.getId())
            {
                notifyItemChanged(i);
            }
        }
    }

    class ExerciseCardViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;

        @Bind(R.id.exercise_title) TextView titleView;
        @Bind(R.id.rep_container) LinearLayout repContainer;
        @Bind(R.id.dropdown_layout) LinearLayout dropDownLayout;
        @Bind(R.id.dropdown_indicator) ImageView dividerView;

        boolean isDropdownToggled = false;

        ExerciseCardViewHolder(CardView cardView)
        {
            super(cardView);
            mCardView = cardView;
            ButterKnife.bind(this, cardView);
        }

        @OnClick(R.id.card_view)
        void onCardClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onCardClicked(this);
        }

        @OnClick(R.id.dropdown_layout)
        void onDropDownClicked(View v)
        {
            ExerciseCardRecyclerAdapter.this.onDropdownClicked(this);
        }
    }

    /**
     * Interface for communication between this adapter and the calling/creating class.
     */
    public interface ExerciseCardAdapterInterface
    {
        void onExerciseClicked(ExerciseSession session);
        void onSpilloverAnimationEnd();
    }
}
